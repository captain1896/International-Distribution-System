package com.thayer.idsservice.task.download;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.opentravel.ota.x2003.x05.TransactionStatusType;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.service.interf.ITimeoutRecoverService;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.interf.IResvTask;
import com.thayer.idsservice.util.Constents;

public class ResvCheckStatusBaseTask extends QuartzJobBean implements IResvTask {

	private transient ITimeoutRecoverService timeoutRecover4FogService;
	private ICallFogService callFogService;
	private transient IMailService mailService;
	private EResvMapDAO eresvMapDAO;
	private List<String> alertMailList;

	/**
	 * Timeout重试时间(毫秒) 默认1分钟
	 */
	private int retryTime = 60000;

	/**
	 * Iata号码
	 */
	private String iata;

	/**
	 * IATA名称
	 */
	private String iataName;

	/**
	 * IDS代码：例如：AGODA，GENARES...
	 */
	private String thirdIataCode;

	private static Logger log = Logger.getLogger(ResvCheckStatusBaseTask.class);

	public String getThirdIataCode() {
		return thirdIataCode;

	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("start ResvCheckStatusBaseTask task for " + thirdIataCode + "...................");
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
			EResvMap instance = new EResvMap();
			instance.setBk5(thirdIataCode);
			instance.setBk6(String.valueOf(TransactionStatusType.PENDING.intValue()));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -10);
			instance.setCreatedate(cal.getTime());
			List<EResvMap> pendingResvList = eresvMapDAO.findPendingResv(instance);
			log.info("Cancel " + thirdIataCode + " reservation size :" + pendingResvList.size());
			for (EResvMap eResvMap : pendingResvList) {
				try {
					TResvBase tresvbase = new TResvBase();
					tresvbase.setProp(eResvMap.getFogCnfnum().substring(0, 4));
					tresvbase.setOutcnfnum(eResvMap.getAgodaCnfnum());
					tresvbase.setResvtype(Constents.RESV_TYPE_CANCEL);
					callFogService.saveFogNewResvBean(tresvbase, false);
					eResvMap.setBk6(String.valueOf(TransactionStatusType.CANCELLED.intValue()));
					eresvMapDAO.attachDirty(eResvMap);
				} catch (TimeOut4FogException e) {
					log.error("Connect " + thirdIataCode + " timeout : \n" + ExceptionUtils.getFullStackTrace(e));
					mailService
							.sendMail(
									alertMailList,
									"IDS Alert——Cancel " + thirdIataCode + " reservation : " + eResvMap.getFogCnfnum()
											+ " Failed!",
									"Cancel "
											+ thirdIataCode
											+ " reservation Failed! Caused by: Fog service connection times out! IDS service will test the network and automatically restore! Reservation in the current treatment :"
											+ eResvMap.getFogCnfnum() + " . Please manually cancel!",
									"IDS Alert——Cancel " + thirdIataCode + " Reservations Task Timeout!!!");

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

					mailService.sendMail(alertMailList, "IDS Notify——Cancel " + thirdIataCode
							+ " reservation service automatically restore!", "Cancel " + thirdIataCode
							+ " reservation service automatically restore!", "IDS Notify——Cancel " + thirdIataCode
							+ " Reservations service automatically restore!");

				} catch (BizException e) {
					log.error(ExceptionUtils.getFullStackTrace(e));
					mailService
							.sendMail(
									alertMailList,
									"IDS Alert——Cancel " + thirdIataCode + " reservation : " + eResvMap.getFogCnfnum()
											+ " Failed!",
									"Cancel "
											+ thirdIataCode
											+ " reservation Failed! Caused by: "
											+ e.getMessage()
											+ "! IDS service will test the network and automatically restore! Reservation in the current treatment :"
											+ eResvMap.getFogCnfnum() + " . Please manually cancel!",
									"IDS Alert——Cancel " + thirdIataCode + " Reservations Task Timeout!!!");
				} catch (Exception e) {
					log.error(ExceptionUtils.getFullStackTrace(e));
					mailService
							.sendMail(
									alertMailList,
									"IDS Alert——Cancel " + thirdIataCode + " reservation : " + eResvMap.getFogCnfnum()
											+ " Failed!",
									"Cancel "
											+ thirdIataCode
											+ " reservation Failed! Caused by: "
											+ e.getMessage()
											+ "! IDS service will test the network and automatically restore! Reservation in the current treatment :"
											+ eResvMap.getFogCnfnum() + " . Please manually cancel!",
									"IDS Alert——Cancel " + thirdIataCode + " Reservations Task Timeout!!!");
				}

			}
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean errorBean = new ErrorBean();
			errorBean.setErroCode("");
			errorBean.setErrorDesc("Get reservations Map List error!");
			resultBean.getErrorlist().add(errorBean);
		}
		log.info("end ResvCheckStatusBaseTask task for " + thirdIataCode + "...................");
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
	 * @return the eresvMapDAO
	 */
	public EResvMapDAO getEresvMapDAO() {
		return eresvMapDAO;
	}

	/**
	 * @param eresvMapDAO
	 *            the eresvMapDAO to set
	 */
	public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
		this.eresvMapDAO = eresvMapDAO;
	}

	/**
	 * @return the callFogService
	 */
	public ICallFogService getCallFogService() {
		return callFogService;
	}

	/**
	 * @param callFogService
	 *            the callFogService to set
	 */
	public void setCallFogService(ICallFogService callFogService) {
		this.callFogService = callFogService;
	}

}
