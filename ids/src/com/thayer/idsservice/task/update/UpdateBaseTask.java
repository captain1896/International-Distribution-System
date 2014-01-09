package com.thayer.idsservice.task.update;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.fog2.vo.RateMQVO;
import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.service.interf.ITimeoutRecoverService;
import com.thayer.idsservice.task.interf.IRateAvailUploadTask;
import com.thayer.idsservice.task.update.bean.FogMQBean;
import com.thayer.idsservice.task.update.bean.ResultBean;
import com.thayer.idsservice.task.update.interf.IUpdateService;

import freemarker.template.TemplateException;

public class UpdateBaseTask implements IRateAvailUploadTask {
	private IUpdateService updateService;
	private IMailService mailService;
	private IMapService mapService;
	private List alertMailList;
	private String iata;
	private String iataName;
	private String thirdIataCode;
	private ITimeoutRecoverService timeoutRecover4ThirdService;
	/**
	 * Timeout重试时间(毫秒) 默认1分钟
	 */
	private int retryTime = 60000;

	private static Logger log = Logger.getLogger(UpdateBaseTask.class);

	public IMapService getMapService() {
		return mapService;
	}

	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	public IUpdateService getUpdateService() {
		return updateService;
	}

	public void setUpdateService(IUpdateService updateService) {
		this.updateService = updateService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.interf.IRateAvailUploadTask#onMessage(java.lang.Object)
	 */
	public void onMessage(Object message) {

		if (message instanceof RateMQVO) {
			log.debug("onMessage begin!");
			ResultBean resultBean = new ResultBean();
			resultBean.setIata(iata);
			resultBean.setIataName(iataName);
			resultBean.setThirdIataCode(thirdIataCode);
			FogMQBean fogMQBean = new FogMQBean();
			fogMQBean.setIata(iata);
			RateMQVO rateMQVO = (RateMQVO) message;
			//TODO 缺少rateMQVO Mapping过滤，会导致频繁查FOG数据库
			
			StringBuffer sb = new StringBuffer();
			sb.append("onMessage prop: ").append(rateMQVO.getProp()).append(rateMQVO.toString());
			log.info(sb.toString());
			
			//如果 开始 日期 或者结束日期 在当日之前 就返回 不处理
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.MILLISECOND, 0);
			//如果结束日期 小于当前日期 就直接返回
			if(rateMQVO.getToDate() == null || rateMQVO.getFromDate() == null){
				return;
			}
			
			if(rateMQVO.getToDate().getTime() < today.getTimeInMillis()){
				log.warn("messageTime warn toDate is befour today:" + sb.toString());
				return;
			}
			
			//如果开始日期小于当前日期 就把当前日期 赋予开始日期
			if(rateMQVO.getFromDate().getTime() < today.getTimeInMillis()){
				log.warn("messageTime warn FromDate is befour today:" + sb.toString());
				rateMQVO.setFromDate(today.getTime());
			}
			
			resultBean.setRateMQVO(rateMQVO);
			resultBean.setFogProp(rateMQVO.getProp());
			fogMQBean.setRateMQVO(rateMQVO);
			// ServiceContext serviceContext = new ServiceContext();
			// serviceContext.setResultBean(resultBean);
			// serviceContext.setFogMQBean(fogMQBean);
			// ServiceContextHolder.setContext(serviceContext);

			startUpdate(resultBean, fogMQBean);

			String title = "IDS Notify——Update " + thirdIataCode + " Rate And Avail Task Report";
			try {

				boolean sendResult = mailService.sendUpdateMail(alertMailList, title, resultBean);
				if (!sendResult) {
					// TODO: handle false,持久化结果
					mailService.persistentMailBean(new MailBean(alertMailList, title, resultBean));
				}
			} catch (IOException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));

				mailService.persistentMailBean(new MailBean(alertMailList, title, resultBean));

			} catch (TemplateException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));

				mailService.persistentMailBean(new MailBean(alertMailList, title, resultBean));

			}
		}
	}

	/**
	 * @param resultBean
	 * @param fogMQBean
	 */
	private void startUpdate(ResultBean resultBean, FogMQBean fogMQBean) {
		try {
			updateService.update(fogMQBean, resultBean);
		} catch (MappingException e) {
			mailService.sendMail(alertMailList, "IDS Alert——Update " + thirdIataCode + " Rate And Avail Task Pause! ",
					" Can't get RateMap mapping, please contect administrator!", null);
		} catch (BizException e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		} catch (TimeOut4ThirdException e) {
			log.warn("Connect " + thirdIataCode + " timeout : \n " + ExceptionUtils.getFullStackTrace(e));
			// 邮件通知timeout信息
			// mailService.sendMail(alertMailList, "IDS Alert——Update " + thirdIataCode +
			// " Rate And Avail Task Pause! ", "Update " + thirdIataCode
			// + " Rate And Avail Task Pause! Caused by : IDS Interface Timeout! Update in the current treatment :"
			// + message.toString(), "IDS Alert——Update " + thirdIataCode
			// + " Rate And Avail Task Timeout!!!");

			while (true) {
				if (timeoutRecover4ThirdService.isRecover()) {
					break;
				} else {
					try {
						Thread.sleep(retryTime);
					} catch (InterruptedException e1) {
						log.error(e1);
					}
					log.error("update service timeout...... recover test again!");
				}
			}
			// 邮件通知timeout恢复
			// mailService.sendMail(alertMailList, "IDS Alert——Update " + thirdIataCode +
			// " Rate And Avail Task resume! ", "Update " + thirdIataCode + " Rate And Avail Task Resume! ",
			// "IDS Alert——Update " + thirdIataCode + " Rate And Avail Task resume!!!");

			// 重试上传。
			startUpdate(resultBean, fogMQBean);
		}
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public List getAlertMailList() {
		return alertMailList;
	}

	public void setAlertMailList(List alertMailList) {
		this.alertMailList = alertMailList;
	}

	public String getThirdIataCode() {
		return thirdIataCode;
	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
	}

	public ITimeoutRecoverService getTimeoutRecover4ThirdService() {
		return timeoutRecover4ThirdService;
	}

	public void setTimeoutRecover4ThirdService(ITimeoutRecoverService timeoutRecover4ThirdService) {
		this.timeoutRecover4ThirdService = timeoutRecover4ThirdService;
	}

	public int getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getIataName() {
		return iataName;
	}

	public void setIataName(String iataName) {
		this.iataName = iataName;
	}

}
