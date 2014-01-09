package com.thayer.idsservice.task.download;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.service.interf.IEOrdermailInfoService;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.service.interf.ITimeoutRecoverService;
import com.thayer.idsservice.task.Thread.ExpediaResultFaildThread;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.interf.IDownLoadService;
import com.thayer.idsservice.task.download.interf.IResvCallbackService;
import com.thayer.idsservice.task.interf.IResvTask;

import freemarker.template.TemplateException;

public class ResvDownloadBaseTask extends QuartzJobBean implements IResvTask {
	private transient IDownLoadService downLoadService;

	private transient ITimeoutRecoverService timeoutRecover4ThirdService;

	private transient ITimeoutRecoverService timeoutRecover4FogService;
	
	/**
	 * 处理返回结果失败的线程池
	 */
	private ExecutorService faildResultThreadPool = Executors.newFixedThreadPool(5);
	/**
	 * 订单信息回传服务
	 */
	private IResvCallbackService resvCallbackService;
	
	private IEOrdermailInfoService eordermailInfoService;

	private transient IMailService mailService;

	private List<String> alertMailList;

	/**
	 * Timeout重试时间(毫秒) 默认1分钟
	 */
	private int retryTime = 60000;

	private transient IMapService mapService;

	/**
	 * Iata号码
	 */
	private String iata;

	/**
	 * IATA名称
	 */
	private String iataName;

	/**
	 * 是否需要查询酒店列表(默认true)
	 */
	private boolean needHotelSearch = true;
	/**
	 * IDS代码：例如：AGODA，GENARES...
	 */
	private String thirdIataCode;

	private static Logger log = Logger.getLogger(ResvDownloadBaseTask.class);

	public String getThirdIataCode() {
		return thirdIataCode;

	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
	}

	public IMapService getMapService() {
		return mapService;
	}

	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	public IDownLoadService getDownLoadService() {
		return downLoadService;
	}

	public void setDownLoadService(IDownLoadService downLoadService) {
		this.downLoadService = downLoadService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		ResultBean resultBean = new ResultBean();
		resultBean.setIata(iata);
		resultBean.setIataName(iataName);
		resultBean.setThirdIataCode(thirdIataCode);
		Scheduler scheduler = jobExecutionContext.getScheduler();
		Trigger trigger = jobExecutionContext.getTrigger();
		log.debug("trigger :" + trigger.getName());
		String group = trigger.getGroup();
		log.debug("group :" + group);
		try {
			List<HotelBean> hotelBeanList = null;
			if (needHotelSearch) {
				try {
					hotelBeanList = mapService.getHotelMapList(thirdIataCode);
				} catch (MappingException e1) {
					mailService.sendMail(alertMailList, "IDS Alert——Download " + thirdIataCode
							+ " reservation service failed! ",
							" Can not read mapping information from the database, please contact the Operation!", null);
					return;
				}
			} else {
				hotelBeanList = new ArrayList<HotelBean>();
				hotelBeanList.add(new HotelBean());
			}
			for (HotelBean hotelBean : hotelBeanList) {
				try {
					hotelBean.setFogIataCode(iata);
					hotelBean.setThirdIataCode(thirdIataCode);
					resultBean.setUsername(hotelBean.getUsername());
					resultBean.setPassword(hotelBean.getPassword());
					downLoadService.downLoad(hotelBean, resultBean);

					// 订单信息回传服务
					if (resvCallbackService != null) {
						List<SuccessBean> successlist = resultBean.getSuccesslist();
						for (SuccessBean successbean : successlist) {
							if(hotelBean.getFogHotelCode().equals(successbean.getFogProp())){
								boolean success = resvCallbackService.resvCallback(successbean);
								if(!success){
									//如果上传失败 就启动线程池 进行重传
									faildResultThreadPool.execute(new ExpediaResultFaildThread(resvCallbackService,successbean));
								}
							}
						}
					}

				} catch (TimeOut4ThirdException e) {
					log.warn("连接 " + thirdIataCode + " 订单下载服务超时！ : \n " + ExceptionUtils.getFullStackTrace(e));
					// 邮件通知timeout信息
					mailService
							.sendMail(
									alertMailList,
									"IDS Alert——Download " + thirdIataCode
											+ " reservation service automatically suspended! ",
									"Download "
											+ thirdIataCode
											+ " reservation service automatically suspended! Caused by: reservation download service connection times out! IDS service will test the network and automatically restore! Hotels in the current treatment :"
											+ hotelBean.getFogHotelCode()
											+ " . To avoid losing reservation, please manually check!",
									"IDS Alert——Download " + thirdIataCode + " Reservations Task Timeout!!!");

					// pause task
					scheduler.pauseTrigger(trigger.getName(), group);
					while (true) {
						if (timeoutRecover4ThirdService.isRecover()) {
							break;
						} else {
							Thread.sleep(retryTime);
							log.error("resvDownload service timeout...... recover test again!");
						}
					}
					// resume task
					scheduler.resumeTrigger(trigger.getName(), group);
					// 邮件通知timeout恢复
					mailService.sendMail(alertMailList, "IDS Notify——Download " + thirdIataCode
							+ " reservation service automatically restore!", "Download " + thirdIataCode
							+ " reservation service automatically restore!", "IDS Notify——Download " + thirdIataCode
							+ " Reservations service automatically restore!");

				} catch (TimeOut4FogException e) {
					log.error("Connect " + thirdIataCode + " timeout : \n" + ExceptionUtils.getFullStackTrace(e));
					mailService
							.sendMail(
									alertMailList,
									"IDS Alert——Download " + thirdIataCode
											+ " reservation service automatically suspended!",
									"Download "
											+ thirdIataCode
											+ " reservation service automatically suspended! Caused by: Fog service connection times out! IDS service will test the network and automatically restore! Hotels in the current treatment :"
											+ hotelBean.getFogHotelCode()
											+ " . To avoid losing reservation, please manually check!",
									"IDS Alert——Download " + thirdIataCode + " Reservations Task Timeout!!!");

					// pause task
					scheduler.pauseTrigger(trigger.getName(), group);

					while (true) {
						if (timeoutRecover4FogService.isRecover()) {
							break;
						} else {
							Thread.sleep(retryTime);
							log.error("resvDownload Fog service timeout...... recover test again!");
						}
					}

					// resume task
					scheduler.resumeTrigger(trigger.getName(), group);

					mailService.sendMail(alertMailList, "IDS Notify——Download " + thirdIataCode
							+ " reservation service automatically restore!", "Download " + thirdIataCode
							+ " reservation service automatically restore!", "IDS Notify——Download " + thirdIataCode
							+ " Reservations service automatically restore!");

				} catch (BizException e) {
					log.error(ExceptionUtils.getFullStackTrace(e));
				} catch (Exception e) {
					log.error(ExceptionUtils.getFullStackTrace(e));
				}

			}//end for
			
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean errorBean = new ErrorBean();
			errorBean.setErroCode("");
			errorBean.setErrorDesc("Get Hotel Map List error!");
			resultBean.getErrorlist().add(errorBean);
		}

		// mail result from resultList.
		sendMailNotify(resultBean);
		
		log.info("executeInternal end!");
	}

	private void sendMailNotify(ResultBean resultBean) {
		List<SuccessBean> successlist = resultBean.getSuccesslist();
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		for (SuccessBean successBean : successlist) {//发送成功邮件
			ResultBean tmpResultBean = new ResultBean();
			tmpResultBean.setCreateDate(resultBean.getCreateDate());
			tmpResultBean.setIata(resultBean.getIata());
			tmpResultBean.setIataName(resultBean.getIataName());
			tmpResultBean.setThirdIataCode(resultBean.getThirdIataCode());
			tmpResultBean.getSuccesslist().add(successBean);
			//订单类型
			String orderType = "null";
			if(successBean.gettResvBase()!=null){
				if("n".equals(successBean.gettResvBase().getResvtype())){
					orderType="新单";
				}else if("e".equals(successBean.gettResvBase().getResvtype())){
					orderType="修改单";
				}else if("c".equals(successBean.gettResvBase().getResvtype())){
					orderType="取消单";
				}
			}
			
			String title = "IDS Notify——【 " + thirdIataCode + "】【" + successBean.getIds_cnfnum() + "】【"+orderType+"】【Success】";
			try {
				boolean sendResult = mailService.sendResvMail(alertMailList, title, tmpResultBean);
				eordermailInfoService.saveSuccessMailInfo(successBean, thirdIataCode);
				if (!sendResult) {
					// handle false,持久化结果
					mailService.persistentMailBean(new MailBean(alertMailList, title, tmpResultBean));
				}
			} catch (IOException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				mailService.persistentMailBean(new MailBean(alertMailList, title, tmpResultBean));
			} catch (TemplateException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				mailService.persistentMailBean(new MailBean(alertMailList, title, tmpResultBean));
			} catch (Exception e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				mailService.persistentMailBean(new MailBean(alertMailList, title, tmpResultBean));
			}

		}
		
		for (ErrorBean errorBean : errorlist) {
			ResultBean tmpResultBean = new ResultBean();
			tmpResultBean.setCreateDate(resultBean.getCreateDate());
			tmpResultBean.setIata(resultBean.getIata());
			tmpResultBean.setIataName(resultBean.getIataName());
			tmpResultBean.setThirdIataCode(resultBean.getThirdIataCode());
			tmpResultBean.getErrorlist().add(errorBean);
			String orderType = "null";
			if(errorBean.gettResvBase()!=null){
				if("n".equals(errorBean.gettResvBase().getResvtype())){
					orderType="新单";
				}else if("e".equals(errorBean.gettResvBase().getResvtype())){
					orderType="修改单";
				}else if("c".equals(errorBean.gettResvBase().getResvtype())){
					orderType="取消单";
				}
			}
			String title = "IDS Alert——【 " + thirdIataCode + "】【" + errorBean.getIds_cnfnum() + "】【"+orderType+"】【Failed】";
			
			//错误邮件 根据预定日期和时间点不同 邮件发送给不同人
			List<String> mailList = new ArrayList<String>();
			mailList.addAll(alertMailList);//复制以前的列表
			
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(errorBean.gettResvBase() != null  && sdf.format(today).equals(errorBean.gettResvBase().getIndate())){//如果是当天订单
				//认为预定时间是当前时间
				Calendar now = Calendar.getInstance();
				now.setTime(today);
				int curHour = now.get(Calendar.HOUR_OF_DAY);
				
				if(curHour>= 21 && curHour < 23){//如果实在21到23点之间
					mailList.add("jjzyh@hubs1.net");
					
				}else if(curHour >= 23){//在23点以后
					mailList.add("night@hubs1.net");
					
				}
			}
			try {
				boolean sendResult = mailService.sendResvMail(mailList, title, tmpResultBean);
				eordermailInfoService.saveFailMailInfo(errorBean, thirdIataCode);
				if (!sendResult) {
					// handle false,持久化结果
					mailService.persistentMailBean(new MailBean(mailList, title, tmpResultBean));
				}
			} catch (IOException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				mailService.persistentMailBean(new MailBean(mailList, title, tmpResultBean));
			} catch (TemplateException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				mailService.persistentMailBean(new MailBean(mailList, title, tmpResultBean));
			}catch(Exception e){
				log.error(ExceptionUtils.getFullStackTrace(e));
				mailService.persistentMailBean(new MailBean(mailList, title, tmpResultBean));
			}

		}

	}
	

	public ITimeoutRecoverService getTimeoutRecover4ThirdService() {
		return timeoutRecover4ThirdService;
	}

	public void setTimeoutRecover4ThirdService(ITimeoutRecoverService timeoutRecover4ThirdService) {
		this.timeoutRecover4ThirdService = timeoutRecover4ThirdService;
	}

	public ITimeoutRecoverService getTimeoutRecover4FogService() {
		return timeoutRecover4FogService;
	}

	public void setTimeoutRecover4FogService(ITimeoutRecoverService timeoutRecover4FogService) {
		this.timeoutRecover4FogService = timeoutRecover4FogService;
	}

	public int getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public List<String> getAlertMailList() {
		return alertMailList;
	}

	public void setAlertMailList(List<String> alertMailList) {
		this.alertMailList = alertMailList;
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

	/**
	 * @return the needHotelSearch
	 */
	public boolean isNeedHotelSearch() {
		return needHotelSearch;
	}

	/**
	 * @param needHotelSearch
	 *            the needHotelSearch to set
	 */
	public void setNeedHotelSearch(boolean needHotelSearch) {
		this.needHotelSearch = needHotelSearch;
	}

	/**
	 * @return the resvCallbackService
	 */
	public IResvCallbackService getResvCallbackService() {
		return resvCallbackService;
	}

	/**
	 * @param resvCallbackService
	 *            the resvCallbackService to set
	 */
	public void setResvCallbackService(IResvCallbackService resvCallbackService) {
		this.resvCallbackService = resvCallbackService;
	}

	public IEOrdermailInfoService getEordermailInfoService() {
		return eordermailInfoService;
	}

	public void setEordermailInfoService(
			IEOrdermailInfoService eordermailInfoService) {
		this.eordermailInfoService = eordermailInfoService;
	}
	

}
