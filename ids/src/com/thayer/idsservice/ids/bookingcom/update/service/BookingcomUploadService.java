/*****************************************************************<br>
 * <B>FILE :</B> BookingcomUploadService.java <br>
 * <B>CREATE DATE :</B> 2010-12-2 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.bookingcom.update.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.booking.eqc.ar.rq.RequestDocument;
import com.booking.eqc.ar.rq.DateDocument.Date;
import com.booking.eqc.ar.rq.RateDocument.Rate;
import com.booking.eqc.ar.rq.RoomDocument.Room;
import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.dao.EIdsCacheKey;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.RsStatusBean;
import com.thayer.idsservice.task.update.bean.UpdateInfoBean;
import com.thayer.idsservice.task.update.interf.AbstractUploadService;
import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.DateUtil;

/**
 * <B>Function :</B> Bookingcom更新service<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-12-2<br>
 * @version : v1.0
 */
public class BookingcomUploadService extends AbstractUploadService {
	private String user;
	private String password;
	private static Logger LOGGER = Logger.getLogger(BookingcomUploadService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.update.interf.AbstractUploadService#getThirdRequestXml(com.thayer.idsservice.task.
	 * update.bean.AvailBean, java.util.List)
	 */
	@Override
	public List<UpdateInfoBean> getThirdRequestXml(AvailBean getzRateMapList, List<ErrorBean> errorlist) throws BizException, MappingException {
		List<RateDataBO> rateDataBOList = getzRateMapList.getRateDataBOList();
		List<UpdateInfoBean> results = new ArrayList<UpdateInfoBean>();
		for (RateDataBO rateDataBO : rateDataBOList) {
			try {
				String planCode = rateDataBO.getPlanCode();
				String prop = rateDataBO.getProp();

				MappingResultBean mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(MappingEnum.BOOKINGCOM, prop, planCode);
				if (mappingResultBean == null) {
					// LOGGER.warn(MappingEnum.AGODA + ",prop:" + prop + ",planCode:" + planCode);
					continue;
				}
				List<AvailAllow> rateList = rateDataBO.getRateList();
				if (rateList == null || rateList.size() == 0) {
					continue;
				}
				
				//因为Booking的保留房的原因 所以 变价和变房态不能放在一个xml中
				RequestDocument arRqDocPrice = this.getRequestDocument(mappingResultBean, rateList, 1);
				RequestDocument arRqDocRoom = this.getRequestDocument(mappingResultBean, rateList, 2);;
				
				UpdateInfoBean updateInfoBeanPrice = new UpdateInfoBean();
				updateInfoBeanPrice.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
				String requestxmlPrice = arRqDocPrice.toString();
				LOGGER.info("getThirdRequestXml :\n" + requestxmlPrice);
				updateInfoBeanPrice.setRequestXml(requestxmlPrice);
				updateInfoBeanPrice.setReqestURL(getThirdRequestXmlURL());
				results.add(updateInfoBeanPrice);
				
				UpdateInfoBean updateInfoBeanRoom = new UpdateInfoBean();
				updateInfoBeanRoom.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
				String requestxmlRoom = arRqDocRoom.toString();
				LOGGER.info("getThirdRequestXml :\n" + requestxmlRoom);
				updateInfoBeanRoom.setRequestXml(requestxmlRoom);
				updateInfoBeanRoom.setReqestURL(getThirdRequestXmlURL());
				results.add(updateInfoBeanRoom);

			} catch (MappingException e) {
				throw new MappingException(e);
			} catch (Exception e) {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErroCode("IDS-0012");
				errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0012"));
				errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
				errorBean.setXml(rateDataBO.toString());
				errorlist.add(errorBean);
				LOGGER.error("parse getThirdRequestXml error:" + ExceptionUtils.getFullStackTrace(e));

			}
		}
		return results;
	}
	
	/**
	 * 拼接Booking的结构(因为booking传xml的时候 需要把房态和房价分开 所以用type区分)
	 * @author Denile.Zhang
	 * @date 2012-11-6
	 * @param mappingResultBean
	 * @param rateList
	 * @param type 1：变价 2：变房态
	 * @return
	 * @throws Exception
	 */
	private RequestDocument getRequestDocument(MappingResultBean mappingResultBean,List<AvailAllow> rateList,int type) throws Exception{
		RequestDocument arRqDoc = RequestDocument.Factory.newInstance();
		RequestDocument.Request rq = arRqDoc.addNewRequest();
		rq.setUsername(user);
		rq.setPassword(password);
		Room roomDoc = rq.addNewRoom();
		roomDoc.setId(mappingResultBean.getHotelRoomCode());
		int i = 0;
		while (i < rateList.size()) {
			AvailAllow availAllow = rateList.get(i);
			String availDate = DateUtil.formatDate(availAllow.getAvailDate());
			String availDateTo = DateUtil.formatDate(availAllow.getAvailDate());
			int allot = availAllow.getAllotment() > 4999 ? 4999 : availAllow.getAllotment();
			allot = allot < 0 ? 0 : allot;
			String avail = "";

			if ("A".equalsIgnoreCase(availAllow.getAvail()) || "L".equalsIgnoreCase(availAllow.getAvail()) || "C".equalsIgnoreCase(availAllow.getAvail())) {
				avail = "0";
			} else {
				avail = "1";
			}
			String minLos = String.valueOf(availAllow.getMinLos());
			String price = "";
			if (availAllow.getAvailRate() != null) {
				BigDecimal bd1 = new BigDecimal(availAllow.getAvailRate().getDoubleRate());
				price = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
			Date newDate = roomDoc.addNewDate();
			Rate newRate = newDate.addNewRate();
			newRate.setId(mappingResultBean.getHotelRateCode());
			newDate.setClosed(avail);
			newDate.setMinimumstay(minLos);
			
			//根据类型区分是变房价 还是变房态
			switch (type) {
			case 1:
				newDate.setPrice(price);
				break;
			case 2:
				newDate.setRoomstosell(String.valueOf(allot));
				break;
			}

			int j = i + 1;
			while (j < rateList.size()) {
				AvailAllow availAllowNext = rateList.get(j);
				AvailAllow temp = rateList.get(j-1);
				String availDateNext = DateUtil.formatDate(availAllowNext.getAvailDate());
				int allotNext = availAllowNext.getAllotment() > 4999 ? 4999 : availAllowNext.getAllotment();
				allotNext = allotNext < 0 ? 0 : allotNext;
				String availNext = "";
				if ("A".equalsIgnoreCase(availAllowNext.getAvail()) || "L".equalsIgnoreCase(availAllowNext.getAvail()) || "C".equalsIgnoreCase(availAllowNext.getAvail())) {
					availNext = "0";
				} else {
					availNext = "1";
				}
				String minLosNext = String.valueOf(availAllowNext.getMinLos());
				String priceNext = "";
				if (availAllowNext.getAvailRate() != null) {
					BigDecimal bd1Next = new BigDecimal(availAllowNext.getAvailRate().getDoubleRate());
					priceNext = bd1Next.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				}

				if ((allotNext == allot) && availNext.equals(avail) && minLosNext.equals(minLos) && priceNext.equals(price)
					&& DateUtil.dateDiff(DateUtil.TIME_UNIT_D, temp.getAvailDate(), availAllowNext.getAvailDate()) == 1
				) {
					availDateTo = availDateNext;
				} else {
					break;
				}
				j++;
			}
			if (availDate.equals(availDateTo)) {
				newDate.setValue(availDate);
			} else {
				newDate.setFrom(availDate);
				// availDateTo需要加一天，否则availDateTo这一天bookingcom不处理。
				java.util.Date availDateToValue = DateUtil.dateValue(availDateTo);
				java.util.Date availRealDateTo = DateUtil.dateAdd(DateUtil.TIME_UNIT_D, 1, availDateToValue);
				newDate.setTo(DateUtil.formatDate(availRealDateTo));
			}
			i = j;
		}
		return arRqDoc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.update.interf.AbstractUploadService#parseResponseXml(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public RsStatusBean parseResponseXml(String updateResponseXml, String type) throws BizException {
		LOGGER.info("parseResponseXml: \n" + updateResponseXml);
		RsStatusBean rsStatusBean = new RsStatusBean();
		try {
			if (StringUtils.hasText(updateResponseXml) && updateResponseXml.startsWith("<ok")) {
				rsStatusBean.setRsResult(true);
				rsStatusBean.setRsStatus("Success");
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsDesc(updateResponseXml);
			} else {
				rsStatusBean.setRsResult(false);
				rsStatusBean.setRsStatus("ERROR");
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsDesc(updateResponseXml);
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			rsStatusBean.setRsResult(false);
			rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
			rsStatusBean.setRsStatus("ERROR");
		}

		return rsStatusBean;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	

	@Override
	protected void addAvailAllowToCacheList(AvailAllow bean,AvailBean availBean, String plan,
			String prop, String ideType, List<EIdsCache> updateCacheList) {
		EIdsCache cache = new EIdsCache();
		EIdsCacheKey key = new EIdsCacheKey();
		key.setPlanCode(plan);
		key.setProp(prop);
		key.setIdsType(ideType);
		key.setAvailDate(bean.getAvailDate());
		cache.seteIdsCacheKey(key);
		cache.setAllotment(bean.getAllotment());
		cache.setAvail(bean.getAvail());
		cache.setMinLos(bean.getMinLos());
		cache.setSingleRate(bean.getAvailRate().getDoubleRate());
		cache.setDoubleRate(bean.getAvailRate().getDoubleRate());
		cache.setQuadRate(bean.getAvailRate().getDoubleRate());
		cache.setTripleRate(bean.getAvailRate().getDoubleRate());
		updateCacheList.add(cache);
	}

	@Override
	protected boolean validateEq(EIdsCache dist, AvailAllow src,AvailBean srcAvailBean) {
		boolean result = true;
		if(dist == null || src == null){
			return false;
		}
		if(dist.getDoubleRate() != src.getAvailRate().getDoubleRate()){
			result = false;
			return result;
		}
		if(dist.getMinLos() != src.getMinLos()){
			result = false;
			return result;
		}
		int distAllot = dist.getAllotment() > 4999 ? 4999 : dist.getAllotment();
		distAllot = distAllot < 0 ? 0 : distAllot;
		int srctAllot = src.getAllotment() > 4999 ? 4999 : src.getAllotment();
		srctAllot = srctAllot < 0 ? 0 : srctAllot;
		if(distAllot != srctAllot){
			result = false;
			return result;
		}
		String distAvail ="2";
		if ("A".equalsIgnoreCase(dist.getAvail()) || "L".equalsIgnoreCase(dist.getAvail()) || "C".equalsIgnoreCase(dist.getAvail())) {
			distAvail = "0";
		} else {
			distAvail = "1";
		}
		String srcAvail ="2";
		if ("A".equalsIgnoreCase(src.getAvail()) || "L".equalsIgnoreCase(src.getAvail()) || "C".equalsIgnoreCase(src.getAvail())) {
			srcAvail = "0";
		} else {
			srcAvail = "1";
		}
		if(!distAvail.equals(srcAvail)){
			result = false;
			return result;
		}
		return result;
	}
	
//	public static void main(String[] args){
//		BookingcomUploadService service = new BookingcomUploadService();
//		String updateResponseXml = "<error>updating availability hasn't been enabled for hotel '405762' (owning room '40576201'), please contact Bookings ( room id    : '40576201')</error>"
//+"<!-- RUID: [UmFuZG9tSVYkc2RlIyh9YYoXRm+fJyVO6FZjXe1kd3mkSKCnQxRQBlSsjKlEG0X2PVTIJjH2pFrGZQis2F8xXA==] -->";
//		String str = "<ok></ok>";
//		try {
//			RsStatusBean bean = service.parseResponseXml(updateResponseXml, null);
//			System.out.println(bean);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}
}
