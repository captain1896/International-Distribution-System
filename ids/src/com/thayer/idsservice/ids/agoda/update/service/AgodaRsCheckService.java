/*****************************************************************<br>
 * <B>FILE :</B> AgodaRsCheckService.java <br>
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
package com.thayer.idsservice.ids.agoda.update.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.idsservice.dao.EMsgLog;
import com.thayer.idsservice.dao.EMsgLogDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.ids.agoda.Util.AgodaUtils;
import com.thayer.idsservice.ids.agoda.bean.GetBatchStatus;
import com.thayer.idsservice.ids.agoda.bean.Return;
import com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument;
import com.thayer.idsservice.ids.agoda.bean.HeaderDataDocument.HeaderData;
import com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument.YCSXML;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.task.check.interf.ICheckService;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.ResultBean;

/**
 * <B>Function :</B> Agoda update pending状态回查服务<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-26<br>
 * @version : v1.0
 */
public class AgodaRsCheckService implements ICheckService {
	private static Logger LOGGER = Logger.getLogger(AgodaRsCheckService.class);
	private IMapService mapService;
	private String agodaUserId;

	private String agodaUserPwd;

	private IHttpClientService httpClientService;

	private String requestXmlKey;// 请求postXml的key值

	private String thirdRequestXmlURL;// Agoda 接口URL

	private EMsgLogDAO emsgLogDAO;

	/**
	 * @return the emsgLogDAO
	 */
	public EMsgLogDAO getEmsgLogDAO() {
		return emsgLogDAO;
	}

	/**
	 * @param emsgLogDAO
	 *            the emsgLogDAO to set
	 */
	public void setEmsgLogDAO(EMsgLogDAO emsgLogDAO) {
		this.emsgLogDAO = emsgLogDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.check.interf.ICheckService#doCheckRs(java.util.List,
	 * com.thayer.idsservice.task.update.bean.ResultBean)
	 */
	@Override
	public void doCheckRs(List<EMsgLog> eMsgLogs, ResultBean resultBean) throws BizException {
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		for (EMsgLog msgLog : eMsgLogs) {
			try {
				YCSXMLDocument document = YCSXMLDocument.Factory.newInstance();
				YCSXML ycxXml = document.addNewYCSXML();
				GetBatchStatus batchStatus = ycxXml.addNewGetBatchStatus();
				HeaderData headerData = batchStatus.addNewHeaderData();
				headerData.setPassword(agodaUserPwd);
				headerData.setUserID(agodaUserId);
				headerData.setHotelID(new BigInteger(msgLog.getExpropId()));
				batchStatus.setTicketID(msgLog.getMsgId());
				Map parms = new HashMap();
				parms.put(requestXmlKey, document.toString());
				LOGGER.info("doCheckRs request xml: \n" + document.toString());
				String rsXml = httpClientService.postHttpRequest(thirdRequestXmlURL, parms);
				LOGGER.info("doCheckRs response xml: \n" + rsXml);
				String rsNewXml = AgodaUtils.reHandleRS(rsXml);
				YCSXMLDocument rsDoc = YCSXMLDocument.Factory.parse(rsNewXml);
				Return RsReturn = rsDoc.getYCSXML().getReturn();
				if (RsReturn.getValid()) {
					String ticketStatus = org.apache.commons.lang.StringUtils.lowerCase(RsReturn.getTicketStatus());
					if ("Failed".equalsIgnoreCase(ticketStatus) || "Partial Failed".equalsIgnoreCase(ticketStatus) || (RsReturn.getErrors() != null && !RsReturn.getErrors().isNil())) {
						// 处理错误结果
						ErrorBean errorBean = new ErrorBean();
						errorBean.setErroCode("IDS-0015");
						errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0015"));
						errorBean.setXml(rsXml);
						errorlist.add(errorBean);
					}
					if (!"Processing".equalsIgnoreCase(ticketStatus)) {
						msgLog.setMsgStatus(ticketStatus);
					}

				} else {
					ErrorBean errorBean = new ErrorBean();
					errorBean.setErroCode("IDS-0015");
					errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0015"));
					errorBean.setXml(rsXml);
					errorlist.add(errorBean);
					msgLog.setMsgStatus("error");
				}

				msgLog.setUpdatetime(new Date());
				msgLog.setMsgRsFinal(rsXml);
				emsgLogDAO.attachDirty(msgLog);

			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
	}

	public String getAgodaUserId() {
		return agodaUserId;
	}

	public void setAgodaUserId(String agodaUserId) {
		this.agodaUserId = agodaUserId;
	}

	public String getAgodaUserPwd() {
		return agodaUserPwd;
	}

	public void setAgodaUserPwd(String agodaUserPwd) {
		this.agodaUserPwd = agodaUserPwd;
	}

	/**
	 * @return the thirdRequestXmlURL
	 */
	public String getThirdRequestXmlURL() {
		return thirdRequestXmlURL;
	}

	/**
	 * @param thirdRequestXmlURL
	 *            the thirdRequestXmlURL to set
	 */
	public void setThirdRequestXmlURL(String thirdRequestXmlURL) {
		this.thirdRequestXmlURL = thirdRequestXmlURL;
	}

	/**
	 * @return the requestXmlKey
	 */
	public String getRequestXmlKey() {
		return requestXmlKey;
	}

	/**
	 * @param requestXmlKey
	 *            the requestXmlKey to set
	 */
	public void setRequestXmlKey(String requestXmlKey) {
		this.requestXmlKey = requestXmlKey;
	}

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
	 * @return the mapService
	 */
	public IMapService getMapService() {
		return mapService;
	}

	/**
	 * @param mapService
	 *            the mapService to set
	 */
	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}
}
