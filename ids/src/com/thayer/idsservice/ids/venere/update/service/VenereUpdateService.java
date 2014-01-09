/*****************************************************************<br>
 * <B>FILE :</B> VenereUpdateService.java <br>
 * <B>CREATE DATE :</B> 2011-4-18 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.venere.update.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.opentravel.ota.x2003.x05.AvailStatusMessageType;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelRateAmountNotifRQDocument;
import org.opentravel.ota.x2003.x05.RateAmountMessageType;
import org.opentravel.ota.x2003.x05.StatusApplicationControlType;
import org.opentravel.ota.x2003.x05.TimeUnitType;
import org.opentravel.ota.x2003.x05.AvailStatusMessageType.BookingLimitMessageType;
import org.opentravel.ota.x2003.x05.AvailStatusMessageType.LengthsOfStay;
import org.opentravel.ota.x2003.x05.LengthsOfStayType.LengthOfStay;
import org.opentravel.ota.x2003.x05.LengthsOfStayType.LengthOfStay.MinMaxMessageType;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRQDocument.OTAHotelAvailNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRQDocument.OTAHotelAvailNotifRQ.AvailStatusMessages;
import org.opentravel.ota.x2003.x05.OTAHotelRateAmountNotifRQDocument.OTAHotelRateAmountNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelRateAmountNotifRQDocument.OTAHotelRateAmountNotifRQ.RateAmountMessages;
import org.opentravel.ota.x2003.x05.RateAmountMessageType.Rates;
import org.opentravel.ota.x2003.x05.RateAmountMessageType.Rates.Rate;
import org.opentravel.ota.x2003.x05.RateUploadType.BaseByGuestAmts.BaseByGuestAmt;
import org.opentravel.ota.x2003.x05.StatusApplicationControlType.RatePlanCodeType;
import org.springframework.util.StringUtils;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.dao.EIdsCacheKey;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.venere.util.VenereUtils;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.RsStatusBean;
import com.thayer.idsservice.task.update.bean.UpdateInfoBean;
import com.thayer.idsservice.task.update.interf.AbstractUploadService;
import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.Constents;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-4-18<br>
 * @version : v1.0
 */
public class VenereUpdateService extends AbstractUploadService {
	private String venereUserId;
	private String venereUserPwd;
	private String venereOrgId;
	private static Logger LOGGER = Logger.getLogger(VenereUpdateService.class);
	private String hotelAvailNotifUrl;
	private String hotelRateAmountNotifUrl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.update.interf.AbstractUploadService#getThirdRequestXml(com.thayer.idsservice.task.
	 * update.bean.AvailBean, java.util.List)
	 */
	@Override
	public List<UpdateInfoBean> getThirdRequestXml(AvailBean availBean, List<ErrorBean> errorlist) throws BizException,
			MappingException {
		List<UpdateInfoBean> results = new ArrayList<UpdateInfoBean>();
		List<RateDataBO> rateDataBOList = availBean.getRateDataBOList();
		for (RateDataBO rateDataBO : rateDataBOList) {
			String planCode = rateDataBO.getPlanCode();
			String prop = rateDataBO.getProp();
			List<AvailAllow> rateList = rateDataBO.getRateList();

			List<MappingResultBean> mappingResultBeanList = getMapService().getThirdHotelRateCodeByPlanLevel4Venere(
					MappingEnum.VENERE, prop, planCode);

			if (mappingResultBeanList == null || mappingResultBeanList.size() == 0) {
				continue;
			}
			if (rateList == null || rateList.size() == 0) {
				continue;
			}
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			// build OTAHotelAvailNotifRQ xml

			for (MappingResultBean mappingResultBean : mappingResultBeanList) {

				OTAHotelAvailNotifRQDocument req = OTAHotelAvailNotifRQDocument.Factory.newInstance();
				OTAHotelAvailNotifRQ root = req.addNewOTAHotelAvailNotifRQ();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				XmlCalendar nowDate = new XmlCalendar(dateFormat.format(new Date()));
				root.setTimeStamp(nowDate);
				root.setEchoToken(CommonUtils.getUuid());
				root.setTarget(OTAHotelAvailNotifRQDocument.OTAHotelAvailNotifRQ.Target.Enum.forString("Production"));
				BigDecimal version = new BigDecimal("2.000");
				root.setVersion(version);
				AvailStatusMessages availStatusMessages = root.addNewAvailStatusMessages();
				// 酒店ID
				availStatusMessages.setHotelCode(mappingResultBean.getHotelCode());
				int i = 0;
				while (i < rateList.size()) {
					AvailAllow availAllow = rateList.get(i);

					int limit = availAllow.getAllotment() > 255 ? 255 : availAllow.getAllotment();
					if (availAllow.getAvail().equalsIgnoreCase("B") || availAllow.getAvail().equalsIgnoreCase("C")) {
						limit = 0;
					}

					int minLos = availAllow.getMinLos();
					if (availAllow.getAvail().equalsIgnoreCase("A")) {
						minLos = 1;
					}
					Date startDate = availAllow.getAvailDate();
					Date endDate = availAllow.getAvailDate();
					String roomCode = mappingResultBean.getHotelRoomCode();
					int j = i + 1;
					// venere最多只能更新7天
					int count = 0;
					while (j < rateList.size()) {
						if (count > 6) {
							break;
						}
						AvailAllow availAllowNext = rateList.get(j);
						Date endDateNext = availAllowNext.getAvailDate();
						int limitNext = availAllowNext.getAllotment() > 255 ? 255 : availAllowNext.getAllotment();
						if (availAllowNext.getAvail().equalsIgnoreCase("B")
								|| availAllowNext.getAvail().equalsIgnoreCase("C")) {
							limitNext = 0;
						}

						int minLosNext = availAllowNext.getMinLos();
						if (availAllowNext.getAvail().equalsIgnoreCase("A")) {
							minLosNext = 1;
						}
						if ((minLos == minLosNext) && limit == limitNext) {
							endDate = endDateNext;
						} else {
							break;
						}

						j++;
						count++;
					}

					AvailStatusMessageType availStatusMessage = availStatusMessages.addNewAvailStatusMessage();
					BigInteger bookingLimit = new BigInteger(String.valueOf(limit));
					availStatusMessage.setBookingLimit(bookingLimit);
					availStatusMessage.setBookingLimitMessageType(BookingLimitMessageType.SET_LIMIT);
					StatusApplicationControlType statusApplicationControl = availStatusMessage
							.addNewStatusApplicationControl();
					XmlCalendar start = new XmlCalendar(dateFmt.format(startDate));
					statusApplicationControl.setStart(start);

					XmlCalendar end = new XmlCalendar(dateFmt.format(endDate));
					statusApplicationControl.setEnd(end);

					statusApplicationControl.setRatePlanCode("URP");
					statusApplicationControl.setRatePlanCodeType(RatePlanCodeType.RATE_PLAN_CODE);

					// 房型
					statusApplicationControl.setInvCode(roomCode);
					statusApplicationControl.setIsRoom(true);
					LengthsOfStay lengthsOfStay = availStatusMessage.addNewLengthsOfStay();
					lengthsOfStay.setArrivalDateBased(true);
					LengthOfStay lengthOfStay = lengthsOfStay.addNewLengthOfStay();
					lengthOfStay.setMinMaxMessageType(MinMaxMessageType.SET_MIN_LOS);
					// 最小入住天数
					BigInteger time = new BigInteger(String.valueOf(minLos));
					lengthOfStay.setTime(time);
					lengthOfStay.setTimeUnit(TimeUnitType.DAY);
					i = j;
				}

				UpdateInfoBean updateInfoBean = new UpdateInfoBean();
				updateInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
				String requestxml = VenereUtils
						.buildFullXmlRQ(venereOrgId, venereUserId, venereUserPwd, req.toString());
				LOGGER.info("getThirdRequestXml :\n" + requestxml);
				updateInfoBean.setRequestXml(requestxml);
				updateInfoBean.setReqestURL(hotelAvailNotifUrl);
				results.add(updateInfoBean);

				// build OTAHotelRateAmountNotifRQ
				org.opentravel.ota.x2003.x05.OTAHotelRateAmountNotifRQDocument hotelRateAmountNotifRQDocument = OTAHotelRateAmountNotifRQDocument.Factory
						.newInstance();
				OTAHotelRateAmountNotifRQ hotelRateAmountNotifRQ = hotelRateAmountNotifRQDocument
						.addNewOTAHotelRateAmountNotifRQ();
				hotelRateAmountNotifRQ.setTimeStamp(nowDate);
				hotelRateAmountNotifRQ.setEchoToken(CommonUtils.getUuid());

				hotelRateAmountNotifRQ
						.setTarget(OTAHotelRateAmountNotifRQDocument.OTAHotelRateAmountNotifRQ.Target.Enum
								.forString("Production"));
				BigDecimal versionRateAmount = new BigDecimal("3.000");
				hotelRateAmountNotifRQ.setVersion(versionRateAmount);

				RateAmountMessages rateAmountMessages = hotelRateAmountNotifRQ.addNewRateAmountMessages();
				// 酒店ID
				rateAmountMessages.setHotelCode(mappingResultBean.getHotelCode());
				int x = 0;
				while (x < rateList.size()) {
					AvailAllow availAllow = rateList.get(x);
					Date DateFrom = availAllow.getAvailDate();
					Date DateTo = availAllow.getAvailDate();
					// 税后价格
					BigDecimal amountAfterTax = new BigDecimal(availAllow.getAvailRate().getDoubleRate());

					int y = x + 1;
					// venere最多只能更新7天
					int count = 0;
					while (y < rateList.size()) {
						if (count > 6) {
							break;
						}
						AvailAllow availAllowNext = rateList.get(y);
						Date DateToNext = availAllowNext.getAvailDate();
						BigDecimal amountAfterTaxNext = new BigDecimal(availAllowNext.getAvailRate().getDoubleRate());
						if (amountAfterTax.doubleValue() == amountAfterTaxNext.doubleValue()) {
							DateTo = DateToNext;
						} else {
							break;
						}

						y++;
						count++;
					}

					RateAmountMessageType rateAmountMessage = rateAmountMessages.addNewRateAmountMessage();
					StatusApplicationControlType statusApplicationControl = rateAmountMessage
							.addNewStatusApplicationControl();

					// 开始日期
					XmlCalendar start = new XmlCalendar(dateFmt.format(DateFrom));
					statusApplicationControl.setStart(start);
					// 结束日期
					XmlCalendar end = new XmlCalendar(dateFmt.format(DateTo));
					statusApplicationControl.setEnd(end);

					statusApplicationControl.setRatePlanCode("URP");
					statusApplicationControl.setRatePlanCodeType(RatePlanCodeType.RATE_PLAN_CODE);
					// 房型
					statusApplicationControl.setInvCode(mappingResultBean.getHotelRoomCode());
					statusApplicationControl.setIsRoom(true);

					Rates rates = rateAmountMessage.addNewRates();
					Rate rate = rates.addNewRate();
					rate.setRateTimeUnit(TimeUnitType.Enum.forString("Day"));
					rate.setUnitMultiplier(1);
					// 货币类型
					rate.setCurrencyCode("CNY");
					BaseByGuestAmt baseByGuestAmt = rate.addNewBaseByGuestAmts().addNewBaseByGuestAmt();
					baseByGuestAmt.setAmountAfterTax(amountAfterTax);
					BigInteger decimalPlaces = new BigInteger("0");
					baseByGuestAmt.setDecimalPlaces(decimalPlaces);
					x = y;
				}

				UpdateInfoBean updateRateAmountInfoBean = new UpdateInfoBean();
				updateRateAmountInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE2);
				String requestRateAmountxml = VenereUtils.buildFullXmlRQ(venereOrgId, venereUserId, venereUserPwd,
						hotelRateAmountNotifRQDocument.toString());
				LOGGER.info("getThirdRequestXml :\n" + requestRateAmountxml);
				updateRateAmountInfoBean.setRequestXml(requestRateAmountxml);
				updateRateAmountInfoBean.setReqestURL(hotelRateAmountNotifUrl);
				results.add(updateRateAmountInfoBean);
			}
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.update.interf.AbstractUploadService#parseResponseXml(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public RsStatusBean parseResponseXml(String updateResponseXml, String type) throws BizException {
		RsStatusBean rsStatusBean = new RsStatusBean();
		LOGGER.info("type:" + type + ",updateResponseXml:" + updateResponseXml);
		try {
			if (StringUtils.hasText(updateResponseXml) && updateResponseXml.toUpperCase().contains("<SUCCESS")) {
				rsStatusBean.setRsResult(true);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsStatus("success");
			} else {
				rsStatusBean.setRsResult(false);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsDesc(updateResponseXml);
				rsStatusBean.setRsStatus("error");
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			rsStatusBean.setRsResult(false);
			rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
			rsStatusBean.setRsStatus("error");
		}
		return rsStatusBean;
	}

	/**
	 * @return the venereUserId
	 */
	public String getVenereUserId() {
		return venereUserId;
	}

	/**
	 * @param venereUserId
	 *            the venereUserId to set
	 */
	public void setVenereUserId(String venereUserId) {
		this.venereUserId = venereUserId;
	}

	/**
	 * @return the venereUserPwd
	 */
	public String getVenereUserPwd() {
		return venereUserPwd;
	}

	/**
	 * @param venereUserPwd
	 *            the venereUserPwd to set
	 */
	public void setVenereUserPwd(String venereUserPwd) {
		this.venereUserPwd = venereUserPwd;
	}

	/**
	 * @return the hotelAvailNotifUrl
	 */
	public String getHotelAvailNotifUrl() {
		return hotelAvailNotifUrl;
	}

	/**
	 * @param hotelAvailNotifUrl
	 *            the hotelAvailNotifUrl to set
	 */
	public void setHotelAvailNotifUrl(String hotelAvailNotifUrl) {
		this.hotelAvailNotifUrl = hotelAvailNotifUrl;
	}

	/**
	 * @return the hotelRateAmountNotifUrl
	 */
	public String getHotelRateAmountNotifUrl() {
		return hotelRateAmountNotifUrl;
	}

	/**
	 * @param hotelRateAmountNotifUrl
	 *            the hotelRateAmountNotifUrl to set
	 */
	public void setHotelRateAmountNotifUrl(String hotelRateAmountNotifUrl) {
		this.hotelRateAmountNotifUrl = hotelRateAmountNotifUrl;
	}

	/**
	 * @return the venereOrgId
	 */
	public String getVenereOrgId() {
		return venereOrgId;
	}

	/**
	 * @param venereOrgId
	 *            the venereOrgId to set
	 */
	public void setVenereOrgId(String venereOrgId) {
		this.venereOrgId = venereOrgId;
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
		
		int distMinLos = dist.getMinLos();
		int srcMinLos = src.getMinLos();
		if (dist.getAvail().equalsIgnoreCase("A")) {
			distMinLos = 1;
		}
		if (src.getAvail().equalsIgnoreCase("A")) {
			srcMinLos = 1;
		}
		if(distMinLos != srcMinLos){
			result = false;
			return result;
		}
		int distAllot = dist.getAllotment() > 255 ? 255 : dist.getAllotment();
		int srctAllot =  src.getAllotment() > 255 ? 255 : src.getAllotment();
		if(distAllot != srctAllot){
			result = false;
			return result;
		}
		
		String distAvail ="2";
		if (dist.getAvail().equalsIgnoreCase("B") || dist.getAvail().equalsIgnoreCase("C")) {
			distAvail = "0";
		} 
		String srcAvail ="2";
		if (src.getAvail().equalsIgnoreCase("B") || src.getAvail().equalsIgnoreCase("C")) {
			srcAvail = "0";
		} 
		if(!distAvail.equals(srcAvail)){
			result = false;
			return result;
		}
		return result;
	}

}
