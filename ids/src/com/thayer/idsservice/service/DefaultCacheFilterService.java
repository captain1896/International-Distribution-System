/*****************************************************************<br>
 * <B>FILE :</B> DefaultCacheFilterService.java <br>
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
package com.thayer.idsservice.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.fog2.vo.RateMQVO;
import com.thayer.idsservice.service.interf.ICacheFilterService;
import com.thayer.idsservice.service.interf.IServiceCache;
import com.thayer.idsservice.task.update.bean.FogMQBean;

/**
 * <B>Function :</B> 默认cache过滤重复信息类<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-31<br>
 * @version : v1.0
 */
public class DefaultCacheFilterService implements ICacheFilterService {
	private IServiceCache serviceCache;
	protected static Logger LOGGER = Logger.getLogger(DefaultCacheFilterService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.ICacheFilterService#addCache(java.lang.String, java.lang.String)
	 */
	@Override
	public void addCache(String key, String xml,String type) {
		try {
			serviceCache.addCache(key, xml);
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
			if (requestxml!= null && requestxml.equals(serviceCache.getCache(key))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return false;
		}
//		try {
//			if (serviceCache.getCache(requestxml) != null) {
//				return true;
//			} else {
//				return false;
//			}
//		} catch (Exception e) {
//			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
//			return false;
//		}
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
