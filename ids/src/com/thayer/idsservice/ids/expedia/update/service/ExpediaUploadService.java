/*****************************************************************<br>
 * <B>FILE :</B> ExpediaUploadService.java <br>
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
package com.thayer.idsservice.ids.expedia.update.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import com.expediaconnect.eqc.ar.x2011.x06.AvailRateUpdateRQDocument;
import com.expediaconnect.eqc.ar.x2011.x06.AuthenticationDocument.Authentication;
import com.expediaconnect.eqc.ar.x2011.x06.AvailRateUpdateDocument.AvailRateUpdate;
import com.expediaconnect.eqc.ar.x2011.x06.AvailRateUpdateRQDocument.AvailRateUpdateRQ;
import com.expediaconnect.eqc.ar.x2011.x06.DateRangeDocument.DateRange;
import com.expediaconnect.eqc.ar.x2011.x06.HotelDocument.Hotel;
import com.expediaconnect.eqc.ar.x2011.x06.InventoryDocument.Inventory;
import com.expediaconnect.eqc.ar.x2011.x06.PerDayDocument.PerDay;
import com.expediaconnect.eqc.ar.x2011.x06.RateDocument.Rate;
import com.expediaconnect.eqc.ar.x2011.x06.RatePlanDocument.RatePlan;
import com.expediaconnect.eqc.ar.x2011.x06.RestrictionsDocument.Restrictions;
import com.expediaconnect.eqc.ar.x2011.x06.RoomTypeDocument.RoomType;
import com.expediaconnect.eqc.ar.x2007.x02.AvailRateUpdateRSDocument;
import com.expediaconnect.eqc.ar.x2007.x02.AvailRateUpdateRSDocument.AvailRateUpdateRS;
import com.expediaconnect.eqc.ar.x2007.x02.AvailRateUpdateRSDocument.AvailRateUpdateRS.Error;
import com.expediaconnect.eqc.ar.x2007.x02.AvailRateUpdateRSDocument.AvailRateUpdateRS.Success;
import com.expediaconnect.eqc.ar.x2007.x02.AvailRateUpdateRSDocument.AvailRateUpdateRS.Success.Warning;
import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.fog2.entity.AvailRate;
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
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-12-2<br>
 * @version : v1.0
 */
public class ExpediaUploadService extends AbstractUploadService {

	private static Logger LOGGER = Logger.getLogger(ExpediaUploadService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.thayer.idsservice.task.update.interf.AbstractUploadService#
	 * getThirdRequestXml(com.thayer.idsservice.task. update.bean.AvailBean,
	 * java.util.List)
	 */
	@Override
	public List<UpdateInfoBean> getThirdRequestXml(AvailBean availBean,
			List<ErrorBean> errorlist) throws BizException, MappingException {
		List<UpdateInfoBean> results = new ArrayList<UpdateInfoBean>();
		List<RateDataBO> rateDataBOList = availBean.getRateDataBOList();
		List<UpdateInfoBean> updateInfoBeanList = new ArrayList();
		for (RateDataBO rateDataBO : rateDataBOList) {
			String planCode = rateDataBO.getPlanCode();
			String prop = rateDataBO.getProp();
			List<AvailAllow> rateList = rateDataBO.getRateList();
			List<MappingResultBean> mappingResultBeanList = getMapService()
					.getThirdHotelRateCodeByPlanLevel4Expedia(
							MappingEnum.EXPEDIA, prop, planCode);
			if (mappingResultBeanList.size() == 0 || rateList == null
					|| rateList.size() == 0) {
				// LOGGER.warn(MappingEnum.AGODA + ",prop:" + prop +
				// ",planCode:" + planCode);
				continue;
			}
			for (MappingResultBean mappingResultBean : mappingResultBeanList) {
				int i = 0;
				while (i < rateList.size()) {
					AvailAllow availAllow = rateList.get(i);
					String availDate = DateUtil.formatDate(availAllow
							.getAvailDate());
					String availDateTo = DateUtil.formatDate(availAllow
							.getAvailDate());
					// AvailRateUpdateRQ
					AvailRateUpdateRQDocument arRqDoc = AvailRateUpdateRQDocument.Factory
							.newInstance();
					AvailRateUpdateRQ rq = arRqDoc.addNewAvailRateUpdateRQ();

					// Authentication
					Authentication autDoc = rq.addNewAuthentication();
					autDoc.setUsername(mappingResultBean.getUserId());
					autDoc.setPassword(mappingResultBean.getPassword());

					// Hotel
					Hotel hotel = rq.addNewHotel();
					hotel
							.setId(new BigInteger(mappingResultBean
									.getHotelCode()));
					AvailRateUpdate availRateUpdate = rq
							.addNewAvailRateUpdate();
					DateRange dateRange = availRateUpdate.addNewDateRange();
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"1")) {
						dateRange.setSun(false);
					} else {
						dateRange.setSun(true);
					}
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"2")) {
						dateRange.setMon(false);
					} else {
						dateRange.setMon(true);
					}
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"3")) {
						dateRange.setTue(false);
					} else {
						dateRange.setTue(true);
					}
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"4")) {
						dateRange.setWed(false);
					} else {
						dateRange.setWed(true);
					}
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"5")) {
						dateRange.setThu(false);
					} else {
						dateRange.setThu(true);
					}
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"6")) {
						dateRange.setFri(false);
					} else {
						dateRange.setFri(true);
					}
					if (availBean.getWeeks() != null
							&& !Arrays.toString(availBean.getWeeks()).contains(
									"7")) {
						dateRange.setSat(false);
					} else {
						dateRange.setSat(true);
					}

					// RoomType
					RoomType addNewRoomType = availRateUpdate.addNewRoomType();
					String hotelRoomCode = mappingResultBean.getHotelRoomCode();
					addNewRoomType.setId(new BigInteger(hotelRoomCode));
					RatePlan addNewRatePlan = addNewRoomType.addNewRatePlan();
					String hotelRateCode = mappingResultBean.getHotelRateCode();
					addNewRatePlan.setId(new BigInteger(hotelRateCode));
					String avail = availAllow.getAvail();

					// room amount
					Inventory inventory = addNewRoomType.addNewInventory();
					int allot = availAllow.getAllotment() > 4999 ? 4999
							: availAllow.getAllotment();
					inventory.setFlexibleAllocation(allot);
					boolean isClose = true;
					if ("A".equalsIgnoreCase(avail)
							|| "L".equalsIgnoreCase(avail)
							|| "C".equalsIgnoreCase(avail)) {
						isClose = false;
						addNewRatePlan.setClosed(false);
					} else {
						isClose = true;
						addNewRatePlan.setClosed(true);
					}
					// Planid
					int minLos = 0;
					if (availAllow.getAvail() != null
							&& "A".equals(availAllow.getAvail().trim())) {
						addNewRatePlan.setClosed(false);
					} else if (availAllow.getAvail() != null
							&& "L".equals(availAllow.getAvail().trim())) {
						addNewRatePlan.setClosed(false);
						// restrictions restrictions
						Restrictions restrictions = addNewRatePlan
								.addNewRestrictions();
						minLos = availAllow.getMinLos();
						if (minLos < 1)
							minLos = 1;
						if (minLos > 28)
							minLos = 28;
						restrictions.setMinLOS(minLos);
						restrictions.setMaxLOS(availAllow.getMaxLos() > 28 ? 28
								: availAllow.getMaxLos());
					} else {
						addNewRatePlan.setClosed(false);
						inventory.setFlexibleAllocation(0);
					}

					// Rate
					Rate addNewRate = addNewRatePlan.addNewRate();
					AvailRate availRate = availAllow.getAvailRate();
					String currencyCode = availRate.getCurrencyCode();
					addNewRate.setCurrency(currencyCode);
					BigDecimal rate = new BigDecimal(0);
					if (availRate != null) {
						PerDay addNewPerDay = addNewRate.addNewPerDay();
						BigDecimal bd1 = BigDecimal.valueOf(availRate
								.getDoubleRate());
						addNewPerDay
								.setRate(bd1.setScale(2, bd1.ROUND_HALF_UP));
						rate = bd1.setScale(2, bd1.ROUND_HALF_UP);
					}

					int j = i + 1;
					while (j < rateList.size()) {
						AvailAllow availAllowNext = rateList.get(j);
						String availDateNext = DateUtil
								.formatDate(availAllowNext.getAvailDate());
						int allotNext = availAllowNext.getAllotment() > 4999 ? 4999
								: availAllowNext.getAllotment();
						String availNext = availAllowNext.getAvail();
						boolean isCloseNext = true;
						if ("A".equalsIgnoreCase(availNext)
								|| "L".equalsIgnoreCase(availNext)
								|| "C".equalsIgnoreCase(availNext)) {
							isCloseNext = false;
						} else {
							isCloseNext = true;
						}
						int minLosNext = 0;
						if (availNext != null && "L".equals(availNext.trim())) {
							minLosNext = availAllowNext.getMinLos();
							if (minLosNext < 1)
								minLosNext = 1;
							if (minLosNext > 28)
								minLosNext = 28;
						}
						AvailRate availRateNext = availAllowNext.getAvailRate();
						String currencyCodeNext = availRateNext
								.getCurrencyCode();
						BigDecimal rateNext = new BigDecimal(0);
						if (availRateNext != null) {
							BigDecimal bd1Next = BigDecimal
									.valueOf(availRateNext.getDoubleRate());
							rateNext = bd1Next.setScale(2,
									bd1Next.ROUND_HALF_UP);
						}

						if (allotNext == allot && (isCloseNext == isClose)
								&& minLosNext == minLos
								&& currencyCodeNext.equals(currencyCode)
								&& rateNext.doubleValue() == rate.doubleValue()) {
							availDateTo = availDateNext;
						} else {
							break;
						}

						j++;
					}

					// DateRange
					Calendar fromDate = Calendar.getInstance();
					fromDate.setTime(DateUtil.dateValue(availDate));
					fromDate.set(Calendar.HOUR_OF_DAY, 0);
					fromDate.set(Calendar.MINUTE, 0);
					fromDate.set(Calendar.SECOND, 0);
					Calendar toDate = Calendar.getInstance();
					toDate.setTime(DateUtil.dateValue(availDateTo));
					toDate.set(Calendar.HOUR_OF_DAY, 23);
					toDate.set(Calendar.MINUTE, 59);
					toDate.set(Calendar.SECOND, 59);
					dateRange.setFrom(fromDate);
					dateRange.setTo(toDate);

					//Day of Week的维护 日期有效性检查
					Calendar checkDate = fromDate;
					List<Integer> dayofWeeks = new ArrayList<Integer>();
					while (checkDate.compareTo(toDate) <= 0) {
						int dayOfWeek = checkDate.get(Calendar.DAY_OF_WEEK);
						dayofWeeks.add(dayOfWeek);
						checkDate.set(Calendar.DAY_OF_MONTH, checkDate.get(Calendar.DAY_OF_MONTH) + 1);
					}	
					if (dateRange.getSun()) {
						if (!dayofWeeks.contains(Integer.valueOf(1))) {
							dateRange.setSun(false);
						}
					}
					if (dateRange.getMon()) {
						if (!dayofWeeks.contains(Integer.valueOf(2))) {
							dateRange.setMon(false);
						}
					}
					if (dateRange.getTue()) {
						if (!dayofWeeks.contains(Integer.valueOf(3))) {
							dateRange.setTue(false);
						}
					}
					if (dateRange.getWed()) {
						if (!dayofWeeks.contains(Integer.valueOf(4))) {
							dateRange.setWed(false);
						}
					}
					if (dateRange.getThu()) {
						if (!dayofWeeks.contains(Integer.valueOf(5))) {
							dateRange.setThu(false);
						}
					}					
                    if (dateRange.getFri()) {
                    	if (!dayofWeeks.contains(Integer.valueOf(6))) {
							dateRange.setFri(false);
						}
					}	
                    if (dateRange.getSat()) {
                    	if (!dayofWeeks.contains(Integer.valueOf(7))) {
							dateRange.setSat(false);
						}
					}				
					
					if (dateRange.getSun()||dateRange.getMon()||dateRange.getTue()
							||dateRange.getWed()||dateRange.getThu()||dateRange.getFri()||dateRange.getSat()) {
					String ycsxmldocumentstr = arRqDoc.toString();
					ycsxmldocumentstr = ycsxmldocumentstr.replaceAll(
							"\\+08:00", "");
					UpdateInfoBean updateInfoBean = new UpdateInfoBean();
					updateInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
					updateInfoBean.setRequestXml(ycsxmldocumentstr);
					LOGGER.info("getThirdRequestXml :\n" + ycsxmldocumentstr);
					updateInfoBean.setReqestURL(getThirdRequestXmlURL());
					//组装chacheKey hotelId + roomId+planeId
					String cacheKey = mappingResultBean.getHotelCode() +" " + hotelRoomCode + " " +hotelRateCode;
					updateInfoBean.setKey(cacheKey);
					updateInfoBeanList.add(updateInfoBean);
					} 
					i = j;
				}

			}

		}
		return updateInfoBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.thayer.idsservice.task.update.interf.AbstractUploadService#
	 * parseResponseXml(java.lang.String, java.lang.String)
	 */
	@Override
	public RsStatusBean parseResponseXml(String updateResponseXml, String type)
			throws BizException {
		LOGGER.info("parseResponseXml : \n" + updateResponseXml);
		RsStatusBean rsStatusBean = new RsStatusBean();
		try {
			AvailRateUpdateRSDocument parse = AvailRateUpdateRSDocument.Factory
					.parse(updateResponseXml);
			AvailRateUpdateRS availRateUpdateRS = parse.getAvailRateUpdateRS();
			Success success = availRateUpdateRS.getSuccess();
			if (success != null) {
				rsStatusBean.setRsResult(true);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsStatus("success");
				Warning[] warningArray = success.getWarningArray();
				if (warningArray.length > 0) {
					rsStatusBean.setRsResult(false);
					rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				}
			}
			Error[] errorArray = availRateUpdateRS.getErrorArray();
			if (errorArray.length > 0) {
				rsStatusBean.setRsResult(false);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
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

	@Override
	protected void addAvailAllowToCacheList(AvailAllow bean,
			AvailBean availBean, String plan, String prop, String ideType,
			List<EIdsCache> updateCacheList) {
		EIdsCache cache = new EIdsCache();
		EIdsCacheKey key = new EIdsCacheKey();
		key.setPlanCode(plan);
		key.setProp(prop);
		key.setIdsType(ideType);
		key.setAvailDate(bean.getAvailDate());
		cache.setWeeks(weeksArrayToString(availBean.getWeeks()));
		cache.seteIdsCacheKey(key);
		cache.setAllotment(bean.getAllotment());
		cache.setAvail(bean.getAvail());
		cache.setMinLos(bean.getMinLos());
		cache.setMaxLos(bean.getMaxLos());
		cache.setCurrencyCode(bean.getAvailRate().getCurrencyCode());
		cache.setSingleRate(bean.getAvailRate().getDoubleRate());
		cache.setDoubleRate(bean.getAvailRate().getDoubleRate());
		cache.setQuadRate(bean.getAvailRate().getDoubleRate());
		cache.setTripleRate(bean.getAvailRate().getDoubleRate());
		updateCacheList.add(cache);

	}

	@Override
	protected boolean validateEq(EIdsCache dist, AvailAllow src,
			AvailBean srcAvailBean) {
		boolean result = true;
		if (dist == null || src == null) {
			return false;
		}

		if (dist.getDoubleRate() != src.getAvailRate().getDoubleRate()) {
			result = false;
			return result;
		}

		if (!dist.getWeeks()
				.equals(weeksArrayToString(srcAvailBean.getWeeks()))) {
			result = false;
			return result;
		}

		int distAllot = dist.getAllotment() > 4999 ? 4999 : dist.getAllotment();
		distAllot = distAllot < 0 ? 0 : distAllot;
		int srctAllot = src.getAllotment() > 4999 ? 4999 : src.getAllotment();
		srctAllot = srctAllot < 0 ? 0 : srctAllot;
		if (distAllot != srctAllot) {
			result = false;
			return result;
		}
		String distAvail = dist.getAvail();
		String srcAvail = src.getAvail();

		if (!StringUtils.equals(distAvail, srcAvail)) {
			result = false;
			return result;
		}

		if ("L".equalsIgnoreCase(distAvail.trim())
				&& "L".equalsIgnoreCase(srcAvail.trim())) {
			int distMin = dist.getMinLos();
			int srcMin = src.getMinLos();
			if (distMin < 1)
				distMin = 1;
			if (srcMin < 1)
				srcMin = 1;
			if (distMin > 28)
				distMin = 28;
			if (srcMin > 28)
				srcMin = 28;
			if (distMin != srcMin) {
				result = false;
				return result;
			}
			int distMax = dist.getMaxLos() > 28 ? 28 : dist.getMaxLos();
			int srcMax = src.getMaxLos() > 28 ? 28 : src.getMaxLos();
			if (distMax != srcMax) {
				result = false;
				return result;
			}
		}

		return result;
	}

}
