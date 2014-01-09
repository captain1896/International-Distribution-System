/*****************************************************************<br>
 * <B>FILE :</B> ExpediaCacheFilterService.java <br>
 * <B>CREATE DATE :</B> 2011-3-31 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.genares.update.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.fog2.vo.RateMQVO;
import com.thayer.idsservice.service.interf.ICacheFilterService;
import com.thayer.idsservice.service.interf.IServiceCache;
import com.thayer.idsservice.task.update.bean.FogMQBean;
import com.thayer.idsservice.util.Constents;

/**
 * <B>Function :</B> Genares cache过滤重复信息实现类<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-31<br>
 * @version : v1.0
 */
public class GenaresCacheFilterService implements ICacheFilterService {
	private IServiceCache serviceCache;
	protected static Logger LOGGER = Logger.getLogger(GenaresCacheFilterService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.ICacheFilterService#addCache(java.lang.String, java.lang.String)
	 */
	@Override
	public void addCache(String key, String requestxml,String type) {

		try {
			if (Constents.IDS_UPDATE_REQUEST_TYPE1.equals(type)) {
				// Hotel Rate Plan NotifRQ
				String distxml = requestxml.substring(requestxml.indexOf("<OTA_HotelRatePlanNotifRQ"), requestxml
						.indexOf("</OTA_HotelRatePlanNotifRQ>")
						+ "</OTA_HotelRatePlanNotifRQ>".length());
				LOGGER.debug("add xml :\n" + distxml.trim());
				serviceCache.addCache(distxml.trim(), distxml.trim());

			}

			if (Constents.IDS_UPDATE_REQUEST_TYPE2.equals(type)) {
				// Hotel Avail NotifRQ
				String distxml = requestxml.substring(requestxml.indexOf("<OTA_HotelAvailNotifRQ"), requestxml
						.indexOf("</OTA_HotelAvailNotifRQ>")
						+ "</OTA_HotelAvailNotifRQ>".length());
				LOGGER.debug("add xml :\n" + distxml.trim());
				serviceCache.addCache(distxml.trim(), distxml.trim());
			}

			if (Constents.IDS_UPDATE_REQUEST_TYPE3.equals(type)) {
				// Hotel Booking Rule NotifRQ
				// Hotel Rate Plan NotifRQ
				String distxml = requestxml.substring(requestxml.indexOf("<OTA_HotelBookingRuleNotifRQ"), requestxml
						.indexOf("</OTA_HotelBookingRuleNotifRQ>")
						+ "</OTA_HotelBookingRuleNotifRQ>".length());
				LOGGER.debug("add xml :\n" + distxml.trim());
				serviceCache.addCache(distxml.trim(), distxml.trim());
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.ICacheFilterService#containsXml(java.lang.String)
	 */
	@Override
	public boolean containsXml(String key, String requestxml,String type) {
		try {
			if (Constents.IDS_UPDATE_REQUEST_TYPE1.equals(type)) {
				// Hotel Rate Plan NotifRQ
				String distxml = requestxml.substring(requestxml.indexOf("<OTA_HotelRatePlanNotifRQ"), requestxml
						.indexOf("</OTA_HotelRatePlanNotifRQ>")
						+ "</OTA_HotelRatePlanNotifRQ>".length());
				LOGGER.debug("compare xml :\n" + distxml.trim());
				if (serviceCache.getCache(distxml.trim()) != null) {
					return true;
				}

			}

			if (Constents.IDS_UPDATE_REQUEST_TYPE2.equals(type)) {
				// Hotel Avail NotifRQ
				String distxml = requestxml.substring(requestxml.indexOf("<OTA_HotelAvailNotifRQ"), requestxml
						.indexOf("</OTA_HotelAvailNotifRQ>")
						+ "</OTA_HotelAvailNotifRQ>".length());
				LOGGER.debug("compare xml :\n" + distxml.trim());
				if (serviceCache.getCache(distxml.trim()) != null) {
					return true;
				}
			}

			if (Constents.IDS_UPDATE_REQUEST_TYPE3.equals(type)) {
				// Hotel Booking Rule NotifRQ
				// Hotel Rate Plan NotifRQ
				String distxml = requestxml.substring(requestxml.indexOf("<OTA_HotelBookingRuleNotifRQ"), requestxml
						.indexOf("</OTA_HotelBookingRuleNotifRQ>")
						+ "</OTA_HotelBookingRuleNotifRQ>".length());
				LOGGER.debug("compare xml :\n" + distxml.trim());
				if (serviceCache.getCache(distxml.trim()) != null) {
					return true;
				}
			}

			return false;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return false;
		}
	}

	/**
	 * @return the serviceCache
	 */
	public IServiceCache getServiceCache() {
		return serviceCache;
	}

	/**
	 * @param serviceCache
	 *            the serviceCache to set
	 */
	public void setServiceCache(IServiceCache serviceCache) {
		this.serviceCache = serviceCache;
	}

	@Override
	public String getCacheKeyByMessage(FogMQBean fogMQBean) {
		StringBuilder str = new StringBuilder();
		str.append(fogMQBean.getIata());
		str.append(" ");
		RateMQVO mq = fogMQBean.getRateMQVO();
		
		if(mq!=null){
			str.append(fogMQBean.getRateMQVO().getProp());
			str.append(" ");
			str.append(fogMQBean.getRateMQVO().getFromDate());
			str.append(" ");
			str.append(fogMQBean.getRateMQVO().getToDate());
			str.append(" ");
			str.append(fogMQBean.getRateMQVO().getOpetype());
			str.append(" ");
			str.append(fogMQBean.getRateMQVO().getPlancodes().toString());
		}
		
		return str.toString();
	}

}
