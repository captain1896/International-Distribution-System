/*****************************************************************<br>
 * <B>FILE :</B> RsStatusCheckTask.java <br>
 * <B>CREATE DATE :</B> 2010-11-26 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.check;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.bean.MsgLogQueryBean;
import com.thayer.idsservice.dao.EMsgLog;
import com.thayer.idsservice.dao.EMsgLogDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.task.check.interf.ICheckService;
import com.thayer.idsservice.task.interf.IRsStatusCheckTask;
import com.thayer.idsservice.task.update.bean.ResultBean;

import freemarker.template.TemplateException;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-26<br>
 * @version : v1.0
 */
public class RsStatusCheckTask extends QuartzJobBean implements IRsStatusCheckTask {
	private EMsgLogDAO emsgLogDAO;
	private String iata;
	private String iataName;
	private String thirdIataCode;
	private String msgStatus;
	private int intervalMinutes;
	private ICheckService checkService;
	private static Logger log = Logger.getLogger(RsStatusCheckTask.class);
	private IMailService mailService;
	private List alertMailList;

	/**
	 * 
	 */
	public RsStatusCheckTask() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.interf.IRsStatusCheckTask#checkRsStatus()
	 */
	@Override
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("start " + iataName + " RsStatus Check Task .........");
		try {
			MsgLogQueryBean logQueryBean = new MsgLogQueryBean();
			logQueryBean.setExwebCode(iata);
			logQueryBean.setMsgStatus(msgStatus);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE, intervalMinutes);
			logQueryBean.setCreatetimeTill(calendar.getTime());
			List<EMsgLog> msgLogList = emsgLogDAO.findMsgLogsByQuery(logQueryBean);
			log.info("Handle Rs Status num:  " + msgLogList.size());
			ResultBean resultBean = new ResultBean();
			resultBean.setIata(iata);
			resultBean.setIataName(iataName);
			resultBean.setThirdIataCode(thirdIataCode);
			checkService.doCheckRs(msgLogList, resultBean);

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
		} catch (BizException e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		log.info("end " + iataName + "  RsStatus Check Task .........");
	}

	public EMsgLogDAO getEmsgLogDAO() {
		return emsgLogDAO;
	}

	public void setEmsgLogDAO(EMsgLogDAO emsgLogDAO) {
		this.emsgLogDAO = emsgLogDAO;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public int getIntervalMinutes() {
		return intervalMinutes;
	}

	public void setIntervalMinutes(int intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
	}

	public ICheckService getCheckService() {
		return checkService;
	}

	public void setCheckService(ICheckService checkService) {
		this.checkService = checkService;
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

	public String getThirdIataCode() {
		return thirdIataCode;
	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
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

}
