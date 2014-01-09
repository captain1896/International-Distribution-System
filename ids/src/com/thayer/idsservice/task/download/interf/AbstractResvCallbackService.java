/*****************************************************************<br>
 * <B>FILE :</B> AbstractResvCallbackService.java <br>
 * <B>CREATE DATE :</B> 2011-4-2 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.download.interf;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.service.interf.ITimeoutRecoverService;
import com.thayer.idsservice.task.download.bean.SuccessBean;

/**
 * <B>Function :</B> ResvCallbackService接口抽象类<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-4-2<br>
 * @version : v1.0
 */
public abstract class AbstractResvCallbackService implements IResvCallbackService {
	private IHttpClientService httpClientService;
	private String url;
	protected static Logger LOGGER = Logger.getLogger(AbstractResvCallbackService.class);
	private ITimeoutRecoverService timeoutRecover4ThirdService;
	/**
	 * Timeout重试时间(毫秒) 默认1分钟
	 */
	private int retryTime = 60000;
	private IMailService mailService;
	private List alertMailList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.IResvCallbackService#resvCallback(com.thayer.idsservice.task.download
	 * .bean.SuccessBean)
	 */
	@Override
	public boolean resvCallback(SuccessBean successBean) {
		String resvCallbackRQ = buildResvCallbackRQ(successBean);
		try {
			String resvCallbackRS = httpClientService.postHttpRequest(url, resvCallbackRQ);
			if (parseResponseXml(resvCallbackRS)) {
				return true;
			}
		} catch (TimeOutException e) {
			while (true) {
				if (timeoutRecover4ThirdService.isRecover()) {
					break;
				} else {
					try {
						Thread.sleep(retryTime);
					} catch (InterruptedException e1) {
						LOGGER.error(e1);
					}
					LOGGER.error("update service timeout...... recover test again!");
				}
			}
			return resvCallback(successBean);

		} catch (BizException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			sendErrorMail(successBean);
		}

		return false;
	}

	/**
	 * @param successBean
	 */
	private void sendErrorMail(SuccessBean successBean) {
		String title = "IDS Notify —— Confirm " + successBean.getThirdIataCode() + " Reservation Error!";
		String content = "Confirm FOG Reservation : " + successBean.getCnfnum() + " IDS Reservation : "
				+ successBean.getIds_cnfnum() + " Failed! Please check it manually.";
		try {

			boolean sendResult = mailService.sendMail(alertMailList, title, content, "");
			if (!sendResult) {
				// TODO: handle false,持久化结果
				mailService.persistentMailBean(new MailBean(alertMailList, title, content));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));

			mailService.persistentMailBean(new MailBean(alertMailList, title, content));
		}

	}

	/**
	 * 生成订单信息回传的xml
	 * 
	 * @param successBean
	 * @return
	 */
	public abstract String buildResvCallbackRQ(SuccessBean successBean);

	/**
	 * 处理回传信息是否发送成功
	 * 
	 * @param resvCallbackRS
	 * @return
	 * @throws BizException
	 */
	public abstract boolean parseResponseXml(String resvCallbackRS) throws BizException;

	/**
	 * @return the httpClientService
	 */
	public IHttpClientService getHttpClientService() {
		return httpClientService;
	}

	/**
	 * @param httpClientService
	 *            the httpClientService to set
	 */
	public void setHttpClientService(IHttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the timeoutRecover4ThirdService
	 */
	public ITimeoutRecoverService getTimeoutRecover4ThirdService() {
		return timeoutRecover4ThirdService;
	}

	/**
	 * @param timeoutRecover4ThirdService
	 *            the timeoutRecover4ThirdService to set
	 */
	public void setTimeoutRecover4ThirdService(ITimeoutRecoverService timeoutRecover4ThirdService) {
		this.timeoutRecover4ThirdService = timeoutRecover4ThirdService;
	}

	/**
	 * @return the retryTime
	 */
	public int getRetryTime() {
		return retryTime;
	}

	/**
	 * @param retryTime
	 *            the retryTime to set
	 */
	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}

	/**
	 * @return the mailService
	 */
	public IMailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService
	 *            the mailService to set
	 */
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * @return the alertMailList
	 */
	public List getAlertMailList() {
		return alertMailList;
	}

	/**
	 * @param alertMailList
	 *            the alertMailList to set
	 */
	public void setAlertMailList(List alertMailList) {
		this.alertMailList = alertMailList;
	}
}
