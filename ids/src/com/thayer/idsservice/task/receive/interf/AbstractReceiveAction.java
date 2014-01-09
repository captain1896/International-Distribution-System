/*****************************************************************<br>
 * <B>FILE :</B> AbstractReceiveAction.java <br>
 * <B>CREATE DATE :</B> 2010-11-22 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.receive.interf;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.thayer.idsservice.bean.ReceiveTypeEnum;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-22<br>
 * @version : v1.0
 */
public abstract class AbstractReceiveAction extends AbstractController implements IReceiveTask {
	/**
	 * 处理订单Initial状态的service
	 * 
	 */
	private IReceiveResvService receiveResvService;

	private IReceiveResvService receiveResvCancelService;

	/**
	 * @return the receiveResvService
	 */
	public IReceiveResvService getReceiveResvService() {
		return receiveResvService;
	}

	/**
	 * @param receiveResvService
	 *            the receiveResvService to set
	 */
	public void setReceiveResvService(IReceiveResvService receiveResvService) {
		this.receiveResvService = receiveResvService;
	}

	/**
	 * 查询可用性service
	 */
	private IReceiveQueryHotelAvailService receiveQueryHotelAvailService;

	protected Logger LOGGER = Logger.getLogger(AbstractReceiveAction.class);

	/**
	 * 判断接收信息的类型
	 * 
	 * @param xml
	 * @return
	 */
	public abstract ReceiveTypeEnum getReceiveType(String xml);

	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			response.setCharacterEncoding("utf-8");

			int datalen = 0;
			try {
				datalen = Integer.parseInt(request.getHeader("content-length"));
			} catch (Exception ex) {
				datalen = 1000000;
			}
			byte[] data = new byte[datalen];
			ServletInputStream sis = request.getInputStream();
			StringBuffer sb = new StringBuffer();
			try {
				int i;
				int k = 0;
				while ((i = sis.read()) != -1) {
					data[k] = (byte) i;
					k++;
				}
				sb.append(new String(data, "UTF-8"));
				sis.close();
			} catch (Exception ex) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(ex));
			}

			String xml = sb.toString().trim();
			LOGGER.info("receive Xml:" + xml);

			ReceiveTypeEnum receiveType = getReceiveType(xml);

			if (receiveType != null) {
				if (ReceiveTypeEnum.OTA_HOTELAVAIL == receiveType) {
					String queryHotelAvail = receiveQueryHotelAvailService.queryHotelAvail(xml,
							ReceiveTypeEnum.OTA_HOTELAVAIL);
					LOGGER.info(queryHotelAvail);
					response.getWriter().append(queryHotelAvail);
				}
				if (ReceiveTypeEnum.OTA_HOTELRES == receiveType) {
					String initiateXml = receiveResvService.downloadResv(xml);
					response.getWriter().append(initiateXml);
				}
				if (ReceiveTypeEnum.OTA_CANCEL == receiveType) {
					String makeResvResult = receiveResvCancelService.downloadResv(xml);
					response.getWriter().append(makeResvResult);
				}
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));

		}
		return null;
	}

	/**
	 * @return the receiveResvCancelService
	 */
	public IReceiveResvService getReceiveResvCancelService() {
		return receiveResvCancelService;
	}

	/**
	 * @param receiveResvCancelService
	 *            the receiveResvCancelService to set
	 */
	public void setReceiveResvCancelService(IReceiveResvService receiveResvCancelService) {
		this.receiveResvCancelService = receiveResvCancelService;
	}

	/**
	 * @return the receiveQueryHotelAvailService
	 */
	public IReceiveQueryHotelAvailService getReceiveQueryHotelAvailService() {
		return receiveQueryHotelAvailService;
	}

	/**
	 * @param receiveQueryHotelAvailService
	 *            the receiveQueryHotelAvailService to set
	 */
	public void setReceiveQueryHotelAvailService(IReceiveQueryHotelAvailService receiveQueryHotelAvailService) {
		this.receiveQueryHotelAvailService = receiveQueryHotelAvailService;
	}

	public static void main(String[] args) {

	}

}
