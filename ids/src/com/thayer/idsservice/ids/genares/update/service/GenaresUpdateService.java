package com.thayer.idsservice.ids.genares.update.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.hubs1.ids.genaresbookingrulenotifrq.OTAHotelBookingRuleNotifRQDocument;
import net.hubs1.ids.genaresbookingrulenotifrq.OTAHotelBookingRuleNotifRQDocument.OTAHotelBookingRuleNotifRQ;
import net.hubs1.ids.genaresbookingrulenotifrq.RuleMessageDocument.RuleMessage;
import net.hubs1.ids.genaresbookingrulenotifrq.RuleMessagesDocument.RuleMessages;
import net.hubs1.ids.genareshotelavailnotifyrq.OTAHotelAvailNotifRQDocument;
import net.hubs1.ids.genareshotelavailnotifyrq.AvailStatusMessageDocument.AvailStatusMessage;
import net.hubs1.ids.genareshotelavailnotifyrq.AvailStatusMessagesDocument.AvailStatusMessages;
import net.hubs1.ids.genareshotelavailnotifyrq.LengthOfStayDocument.LengthOfStay;
import net.hubs1.ids.genareshotelavailnotifyrq.LengthsOfStayDocument.LengthsOfStay;
import net.hubs1.ids.genareshotelavailnotifyrq.OTAHotelAvailNotifRQDocument.OTAHotelAvailNotifRQ;
import net.hubs1.ids.genareshotelavailnotifyrq.RestrictionStatusDocument.RestrictionStatus;
import net.hubs1.ids.genareshotelavailnotifyrq.StatusApplicationControlDocument.StatusApplicationControl;
import net.hubs1.ids.genaresrateplannotifyrq.OTAHotelRatePlanNotifRQDocument;
import net.hubs1.ids.genaresrateplannotifyrq.AdditionalGuestAmountDocument.AdditionalGuestAmount;
import net.hubs1.ids.genaresrateplannotifyrq.AdditionalGuestAmountsDocument.AdditionalGuestAmounts;
import net.hubs1.ids.genaresrateplannotifyrq.BaseByGuestAmtDocument.BaseByGuestAmt;
import net.hubs1.ids.genaresrateplannotifyrq.BaseByGuestAmtsDocument.BaseByGuestAmts;
import net.hubs1.ids.genaresrateplannotifyrq.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ;
import net.hubs1.ids.genaresrateplannotifyrq.RateDocument.Rate;
import net.hubs1.ids.genaresrateplannotifyrq.RatePlanDocument.RatePlan;
import net.hubs1.ids.genaresrateplannotifyrq.RatePlansDocument.RatePlans;
import net.hubs1.ids.genaresrateplannotifyrq.RatesDocument.Rates;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.fog2.entity.AvailRate;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.genares.util.AssemblyUtil;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.RsStatusBean;
import com.thayer.idsservice.task.update.bean.UpdateInfoBean;
import com.thayer.idsservice.task.update.interf.AbstractUploadService;
import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.Convert;

public class GenaresUpdateService extends AbstractUploadService {
	private static Logger LOGGER = Logger.getLogger(GenaresUpdateService.class);

	private String maxAllotment;

	@Override
	public RsStatusBean parseResponseXml(String updateResponseXml, String type) throws BizException {
		RsStatusBean rsStatusBean = new RsStatusBean();
		LOGGER.info("type:" + type + ",updateResponseXml:" + updateResponseXml);
		try {
			if (StringUtils.hasText(updateResponseXml) && updateResponseXml.toUpperCase().contains("<SUCCESS/>")) {
				rsStatusBean.setRsResult(true);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsStatus("success");
			} else {
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

	public String getMaxAllotment() {
		return maxAllotment;
	}

	public void setMaxAllotment(String maxAllotment) {
		this.maxAllotment = maxAllotment;
	}

	@Override
	public List<UpdateInfoBean> getThirdRequestXml(AvailBean availBean, List<ErrorBean> errorlist) throws BizException,
			MappingException {
		List<AvailAllow> availAllowList = availBean.getAvailAllowList();
		List<RateDataBO> getzRateMapList = availBean.getRateDataBOList();
		LOGGER.debug("availAllowList size :" + availAllowList.size() + ",getzRateMapList size:"
				+ getzRateMapList.size());
		List<UpdateInfoBean> updateInfoBeanList = new ArrayList();
		String grAvailPropXml = null;
		String grBookingRulePropXml = null;
		String grAvailRoomXml = null;
		if (availAllowList.size() == 0 && getzRateMapList.size() == 0) {
			return updateInfoBeanList;
		}
		String wsseUsername = "";
		String wssePassword = "";
		// Avail notify RQ
		OTAHotelAvailNotifRQDocument otaHotelAvailNotifRQDocument = OTAHotelAvailNotifRQDocument.Factory.newInstance();
		OTAHotelAvailNotifRQ otaHotelAvailNotifRQ = otaHotelAvailNotifRQDocument.addNewOTAHotelAvailNotifRQ();
		otaHotelAvailNotifRQ.setVersion("1.100");
		AvailStatusMessages availStatusMessages = otaHotelAvailNotifRQ.addNewAvailStatusMessages();

		// booking rule notify RQ
		OTAHotelBookingRuleNotifRQDocument otaHotelBookingRuleNotifRQDocument = OTAHotelBookingRuleNotifRQDocument.Factory
				.newInstance();
		OTAHotelBookingRuleNotifRQ otaHotelBookingRuleNotifRQ = otaHotelBookingRuleNotifRQDocument
				.addNewOTAHotelBookingRuleNotifRQ();
		otaHotelBookingRuleNotifRQ.setVersion("1.100");
		RuleMessages addNewRuleMessages = otaHotelBookingRuleNotifRQ.addNewRuleMessages();

		OTAHotelRatePlanNotifRQDocument otaHotelRatePlanNotifRQDocument = null;
		OTAHotelRatePlanNotifRQ otaHotelRatePlanNotifRQ = null;
		RatePlans ratePlans = null;
		if (getzRateMapList.size() > 0) {
			// rate plan notify RQ
			otaHotelRatePlanNotifRQDocument = OTAHotelRatePlanNotifRQDocument.Factory.newInstance();
			otaHotelRatePlanNotifRQ = otaHotelRatePlanNotifRQDocument.addNewOTAHotelRatePlanNotifRQ();
			otaHotelRatePlanNotifRQ.setVersion("1.100");
			ratePlans = otaHotelRatePlanNotifRQ.addNewRatePlans();
		}

		// handle Avail notify RQ
		int i = 0;
		while (i < availAllowList.size()) {
			AvailAllow availAllow = (AvailAllow) availAllowList.get(i);
			int allotment = availAllow.getAllotment();
			String avaLevel = availAllow.getAvaLevel();
			String avail = availAllow.getAvail();
			String prop = availAllow.getProp();
			// String planCode = availAllow.getPlanCode();
			String roomCode = availAllow.getRoomCode();
			Date availDateStart = availAllow.getAvailDate();
			Date availDateEnd = availAllow.getAvailDate();
			int minLos = availAllow.getMinLos();
			int maxLos = availAllow.getMaxLos();
			int leadTime = availAllow.getLeadTime();
			// 强制满房 1: 强制满房
			int pr = availAllow.getPr();
			// 房间限制 1: 不限量
			String allotmenitLimit = availAllow.getAllotmenitLimit();
			String thirdHotelCode = "";

			try {

				List<HotelBean> hotelMapList = getMapService().getHotelMapList(MappingEnum.GENARES.toString());
				for (HotelBean hotelbean : hotelMapList) {
					if (prop.equalsIgnoreCase(hotelbean.getFogHotelCode())) {
						thirdHotelCode = hotelbean.getThirdHotelCode();
						wsseUsername = hotelbean.getUsername();
						wssePassword = hotelbean.getPassword();
						break;
					}
				}
			} catch (MappingException e) {
				throw new MappingException(e);
			}
			if (!StringUtils.hasText(thirdHotelCode)) {
				LOGGER.debug("prop:" + prop + ",thirdHotelCode is null");
				i++;
				continue;
			}
			// 按天数合并内容
			int j = i + 1;
			while (j < availAllowList.size()) {
				AvailAllow availAllowNext = availAllowList.get(j);
				int allotmentNext = availAllowNext.getAllotment();
				String avaLevelNext = availAllowNext.getAvaLevel();
				String availNext = availAllowNext.getAvail();
				String propNext = availAllowNext.getProp();
				// String planCodeNext = availAllowNext.getPlanCode();
				String roomCodeNext = availAllowNext.getRoomCode();
				int minLosNext = availAllowNext.getMinLos();
				int maxLosNext = availAllowNext.getMaxLos();
				int leadTimeNext = availAllowNext.getLeadTime();
				// 强制满房 1: 强制满房
				int prNext = availAllowNext.getPr();
				// 房间限制 1: 不限量
				String allotmenitLimitNext = availAllowNext.getAllotmenitLimit();
				if (allotment == allotmentNext && avaLevel.equals(avaLevelNext) && avail.equals(availNext)
						&& prop.equals(propNext)
						&& (roomCode == null ? (roomCodeNext == null ? true : false) : roomCode.equals(roomCodeNext))
						&& minLos == minLosNext && maxLos == maxLosNext && pr == prNext
						&& allotmenitLimit.equals(allotmenitLimitNext)) {
					availDateEnd = availAllowNext.getAvailDate();
				} else {
					break;
				}

				j++;
			}

			LOGGER.debug("avaLevel:" + avaLevel + ",prop:" + prop + ",roomCode:" + roomCode + ",avail:" + avail
					+ ",allotment:" + allotment + ",availDateStar:" + Convert.DateToString(availDateStart)
					+ ",availDateEnd:" + Convert.DateToString(availDateEnd) + ",minLos:" + minLos + ",maxLos:" + maxLos
					+ ",leadTime:" + leadTime + ",pr:" + pr + ",allotmenitLimit:" + allotmenitLimit);

			if ("A".equalsIgnoreCase(avaLevel)) {
				// Property Level房量拼装
				otaHotelAvailNotifRQ.setMessageContentCode("6");

				availStatusMessages.setHotelCode(thirdHotelCode);
				AvailStatusMessage availStatusMessage = availStatusMessages.addNewAvailStatusMessage();
				if ("1".equalsIgnoreCase(allotmenitLimit)) {
					availStatusMessage.setBookingLimit(maxAllotment);
				} else {
					availStatusMessage.setBookingLimit(Convert.ToString(allotment));
				}
				availStatusMessage.setBookingLimitMessageType("SetLimit");
				StatusApplicationControl statusApplicationControl = availStatusMessage.addNewStatusApplicationControl();
				statusApplicationControl.setStart(Convert.DateToString(availDateStart));
				statusApplicationControl.setEnd(Convert.DateToString(availDateEnd));
				LengthsOfStay lengthsOfStay = availStatusMessage.addNewLengthsOfStay();
				lengthsOfStay.addNewLengthOfStay();
				lengthsOfStay.addNewLengthOfStay();
				LengthOfStay lengthOfStayMin = LengthOfStay.Factory.newInstance();
				lengthOfStayMin.setMinMaxMessageType("SetMinLOS");
				lengthOfStayMin.setTime(Convert.ToString(minLos));
				lengthOfStayMin.setTimeUnit("Day");
				LengthOfStay lengthOfStayMax = LengthOfStay.Factory.newInstance();
				lengthOfStayMax.setMinMaxMessageType("SetMaxLOS");
				lengthOfStayMax.setTime(Convert.ToString(maxLos));
				lengthOfStayMax.setTimeUnit("Day");
				lengthsOfStay.setLengthOfStayArray(0, lengthOfStayMin);
				lengthsOfStay.setLengthOfStayArray(1, lengthOfStayMax);

				RestrictionStatus restrictionStatus = availStatusMessage.addNewRestrictionStatus();
				if ("A".equalsIgnoreCase(avail) || "L".equalsIgnoreCase(avail)) {
					restrictionStatus.setStatus("Open");
				} else if ("C".equalsIgnoreCase(avail)) {
					restrictionStatus.setStatus("Close");
					restrictionStatus.setRestriction("Arrival");
				} else {
					restrictionStatus.setStatus("Close");
				}

			} else if ("B".equalsIgnoreCase(avaLevel)) {
				String thirdRoomCode = "";
				try {

					thirdRoomCode = getMapService().getExRoomRateMap(roomCode, prop, MappingEnum.GENARES, "roomType");
				} catch (MappingException e) {
					throw new MappingException(e);
				}
				if (!StringUtils.hasText(thirdRoomCode)) {
					LOGGER.debug("prop:" + prop + ",roomCode:" + roomCode + ",thirdRoomCode is null");
					i++;
					continue;
				}
				// Room Type Level房量拼装

				otaHotelAvailNotifRQ.setMessageContentCode("1");

				availStatusMessages.setHotelCode(thirdHotelCode);
				AvailStatusMessage availStatusMessage = availStatusMessages.addNewAvailStatusMessage();

				if ("1".equalsIgnoreCase(allotmenitLimit)) {
					availStatusMessage.setBookingLimit(maxAllotment);
				} else {
					availStatusMessage.setBookingLimit(Convert.ToString(allotment));
				}
				availStatusMessage.setBookingLimitMessageType("SetLimit");
				StatusApplicationControl statusApplicationControl = availStatusMessage.addNewStatusApplicationControl();
				statusApplicationControl.setStart(Convert.DateToString(availDateStart));
				statusApplicationControl.setEnd(Convert.DateToString(availDateEnd));

				statusApplicationControl.setInvTypeCode(thirdRoomCode);
				LengthsOfStay lengthsOfStay = availStatusMessage.addNewLengthsOfStay();
				lengthsOfStay.addNewLengthOfStay();
				lengthsOfStay.addNewLengthOfStay();
				LengthOfStay lengthOfStayMin = LengthOfStay.Factory.newInstance();
				lengthOfStayMin.setMinMaxMessageType("SetMinLOS");
				lengthOfStayMin.setTime(Convert.ToString(minLos));
				lengthOfStayMin.setTimeUnit("Day");
				LengthOfStay lengthOfStayMax = LengthOfStay.Factory.newInstance();
				lengthOfStayMax.setMinMaxMessageType("SetMaxLOS");
				lengthOfStayMax.setTime(Convert.ToString(maxLos));
				lengthOfStayMax.setTimeUnit("Day");
				lengthsOfStay.setLengthOfStayArray(0, lengthOfStayMin);
				lengthsOfStay.setLengthOfStayArray(1, lengthOfStayMax);

				RestrictionStatus restrictionStatus = availStatusMessage.addNewRestrictionStatus();
				// 强制满房优先级别最高 .满房的话房型关闭
				if (1 == pr) {
					restrictionStatus.setStatus("Close");
				} else if ("A".equalsIgnoreCase(avail) || "L".equalsIgnoreCase(avail)) {
					restrictionStatus.setStatus("Open");
				} else if ("C".equalsIgnoreCase(avail)) {
					restrictionStatus.setStatus("Close");
					restrictionStatus.setRestriction("Arrival");
				} else {
					restrictionStatus.setStatus("Close");
				}

			}
			i = j;
		}

		// handle booking rule

		int a = 0;
		while (a < availAllowList.size()) {
			AvailAllow availAllow = (AvailAllow) availAllowList.get(a);
			String avaLevel = availAllow.getAvaLevel();
			String prop = availAllow.getProp();
			// String planCode = availAllow.getPlanCode();
			String roomCode = availAllow.getRoomCode();
			Date availDateStart = availAllow.getAvailDate();
			Date availDateEnd = availAllow.getAvailDate();

			int leadTime = availAllow.getLeadTime();

			String thirdHotelCode = "";
			try {

				List<HotelBean> hotelMapList = getMapService().getHotelMapList(MappingEnum.GENARES.toString());
				for (HotelBean hotelbean : hotelMapList) {
					if (prop.equalsIgnoreCase(hotelbean.getFogHotelCode())) {
						thirdHotelCode = hotelbean.getThirdHotelCode();
						wsseUsername = hotelbean.getUsername();
						wssePassword = hotelbean.getPassword();
						break;
					}
				}
			} catch (MappingException e) {
				throw new MappingException(e);
			}
			if (!StringUtils.hasText(thirdHotelCode)) {
				LOGGER.debug("prop:" + prop + ",thirdHotelCode is null");
				i++;
				continue;
			}
			// 按天数合并内容
			int b = a + 1;
			while (b < availAllowList.size()) {
				AvailAllow availAllowNext = availAllowList.get(b);
				String avaLevelNext = availAllowNext.getAvaLevel();
				String propNext = availAllowNext.getProp();
				// String planCodeNext = availAllowNext.getPlanCode();
				String roomCodeNext = availAllowNext.getRoomCode();
				int leadTimeNext = availAllowNext.getLeadTime();

				if (avaLevel.equals(avaLevelNext) && prop.equals(propNext)
						&& (roomCode == null ? (roomCodeNext == null ? true : false) : roomCode.equals(roomCodeNext))
						&& leadTime == leadTimeNext) {
					availDateEnd = availAllowNext.getAvailDate();
				} else {
					break;
				}

				b++;
			}

			LOGGER.debug("avaLevel:" + avaLevel + ",prop:" + prop + ",roomCode:" + roomCode + ",availDateStar:"
					+ Convert.DateToString(availDateStart) + ",availDateEnd:" + Convert.DateToString(availDateEnd)
					+ ",leadTime:" + leadTime);

			// 设置酒店级提前预定日期
			if (leadTime >= 0) {
				if ("A".equalsIgnoreCase(avaLevel)) {
					addNewRuleMessages.setHotelCode(thirdHotelCode);
					RuleMessage addNewRuleMessage = addNewRuleMessages.addNewRuleMessage();
					net.hubs1.ids.genaresbookingrulenotifrq.BookingRulesDocument.BookingRules addNewBookingRules = addNewRuleMessage
							.addNewBookingRules();
					net.hubs1.ids.genaresbookingrulenotifrq.BookingRuleDocument.BookingRule addNewBookingRule = addNewBookingRules
							.addNewBookingRule();
					addNewBookingRule.setStart(Convert.DateToString(availDateStart));
					addNewBookingRule.setEnd(Convert.DateToString(availDateEnd));
					addNewBookingRule.setMinAdvancedBookingOffset("P0Y0M0DT" + leadTime + "H0M0S");
				} else if ("B".equalsIgnoreCase(avaLevel)) {
					String thirdRoomCode = "";
					try {

						thirdRoomCode = getMapService().getExRoomRateMap(roomCode, prop, MappingEnum.GENARES,
								"roomType");
					} catch (MappingException e) {
						throw new MappingException(e);
					}
					if (!StringUtils.hasText(thirdRoomCode)) {
						LOGGER.debug("prop:" + prop + ",roomCode:" + roomCode + ",thirdRoomCode is null");
						i++;
						continue;
					}
					addNewRuleMessages.setHotelCode(thirdHotelCode);
					RuleMessage addNewRuleMessage = addNewRuleMessages.addNewRuleMessage();
					net.hubs1.ids.genaresbookingrulenotifrq.BookingRulesDocument.BookingRules addNewBookingRules = addNewRuleMessage
							.addNewBookingRules();
					net.hubs1.ids.genaresbookingrulenotifrq.BookingRuleDocument.BookingRule addNewBookingRule = addNewBookingRules
							.addNewBookingRule();
					addNewBookingRule.setStart(Convert.DateToString(availDateStart));
					addNewBookingRule.setEnd(Convert.DateToString(availDateEnd));
					addNewBookingRule.setMinAdvancedBookingOffset("P0Y0M0DT" + leadTime + "H0M0S");
					net.hubs1.ids.genaresbookingrulenotifrq.StatusApplicationControlDocument.StatusApplicationControl addNewStatusApplicationControl = addNewRuleMessage
							.addNewStatusApplicationControl();
					addNewStatusApplicationControl.setInvTypeCode(thirdRoomCode);
				}
			}

			a = b;
		}

		// handle plan level for rateplan,avail,booing rule
		String grPriceXml = null;
		String grAvailPlanXml = null;
		for (RateDataBO rateDataBO : getzRateMapList) {

			try {
				String planCode = rateDataBO.getPlanCode();
				String prop = rateDataBO.getProp();
				MappingResultBean mappingResultBean;
				try {
					mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(MappingEnum.GENARES, prop,
							planCode);

				} catch (MappingException e) {
					throw new MappingException(e);
				}
				if (mappingResultBean == null) {
					LOGGER.debug("prop:" + prop + ",planCode:" + planCode + ",mappingResultBean is null");
					continue;
				}
				wsseUsername = mappingResultBean.getUserId();
				wssePassword = mappingResultBean.getPassword();
				otaHotelRatePlanNotifRQ.setMessageContentCode("8");

				ratePlans.setHotelCode(mappingResultBean.getHotelCode());
				// Rate Plan Level

				otaHotelAvailNotifRQ.setMessageContentCode("3");
				availStatusMessages.setHotelCode(mappingResultBean.getHotelCode());

				// 设置计划级提前预定日期

				addNewRuleMessages.setHotelCode(mappingResultBean.getHotelCode());

				if (rateDataBO.getRateList() == null || rateDataBO.getRateList().size() == 0) {
					LOGGER
							.debug("prop:" + prop + ",planCode:" + planCode
									+ ",rateDataBO.getRateList().size() equals 0");
					continue;
				}
				List<AvailAllow> rateList = rateDataBO.getRateList();
				// 合并处理
				int x = 0;
				while (x < rateList.size()) {
					AvailAllow availAllow = rateList.get(x);
					AvailRate availRate = availAllow.getAvailRate();
					String currencyCode = availRate.getCurrencyCode();
					Double singleRate = availRate.getSingleRate();
					Double doubleRate = availRate.getDoubleRate();
					Double tripleRate = availRate.getTripleRate();
					Double quadRate = availRate.getQuadRate();
					Double exteraRate = availRate.getExteraRate();

					Date availDateStart = availAllow.getAvailDate();
					Date availDateEnd = availAllow.getAvailDate();

					// 按天数合并内容
					int y = x + 1;
					while (y < rateList.size()) {
						AvailAllow availAllowNext = rateList.get(y);

						AvailRate availRateNext = availAllowNext.getAvailRate();
						String currencyCodeNext = availRateNext.getCurrencyCode();
						Double singleRateNext = availRateNext.getSingleRate();
						Double doubleRateNext = availRateNext.getDoubleRate();
						Double tripleRateNext = availRateNext.getTripleRate();
						Double quadRateNext = availRateNext.getQuadRate();
						Double exteraRateNext = availRateNext.getExteraRate();

						if (currencyCode.equals(currencyCodeNext)
								&& singleRate.doubleValue() == singleRateNext.doubleValue()
								&& doubleRate.doubleValue() == doubleRateNext.doubleValue()
								&& tripleRate.doubleValue() == tripleRateNext.doubleValue()
								&& quadRate.doubleValue() == quadRateNext.doubleValue()
								&& exteraRate.doubleValue() == exteraRateNext.doubleValue()) {
							availDateEnd = availAllowNext.getAvailDate();
						} else {
							break;
						}

						y++;

					}

					LOGGER.debug("prop:" + prop + ",planCode:" + planCode + ",currencyCode:" + currencyCode
							+ ",singleRate:" + singleRate + ",doubleRate:" + doubleRate + ",tripleRate:" + tripleRate
							+ ",quadRate:" + quadRate + ",exteraRate:" + exteraRate + ",availDate:"
							+ Convert.DateToString(availDateStart) + ",availFrom:"
							+ Convert.DateToString(availDateStart) + ",availTo:" + Convert.DateToString(availDateEnd));
					// 价格拼装
					RatePlan ratePlan = ratePlans.addNewRatePlan();
					ratePlan.setRatePlanNotifType("Delta");
					ratePlan.setRatePlanCode(mappingResultBean.getHotelRateCode());

					ratePlan.setCurrencyCode(currencyCode);

					Rates rates = ratePlan.addNewRates();
					Rate rate = rates.addNewRate();
					rate.setInvTypeCode(mappingResultBean.getHotelRoomCode());
					rate.setStart(Convert.DateToString(availDateStart));
					rate.setEnd(Convert.DateToString(availDateEnd));
					BaseByGuestAmts baseByGuestAmts = rate.addNewBaseByGuestAmts();
					baseByGuestAmts.addNewBaseByGuestAmt();
					baseByGuestAmts.addNewBaseByGuestAmt();
					baseByGuestAmts.addNewBaseByGuestAmt();
					baseByGuestAmts.addNewBaseByGuestAmt();

					// 单人价格
					BaseByGuestAmt baseByGuestAmtSingle = BaseByGuestAmt.Factory.newInstance();
					baseByGuestAmtSingle.setAgeQualifyingCode("10");
					baseByGuestAmtSingle.setNumberOfGuests("1");
					baseByGuestAmtSingle.setAmountBeforeTax(Convert.ToString(singleRate));
					// 双人价格
					BaseByGuestAmt baseByGuestAmtDouble = BaseByGuestAmt.Factory.newInstance();
					baseByGuestAmtDouble.setAgeQualifyingCode("10");
					baseByGuestAmtDouble.setNumberOfGuests("2");
					baseByGuestAmtDouble.setAmountBeforeTax(Convert.ToString(doubleRate));
					// 三人价格
					BaseByGuestAmt baseByGuestAmtTriple = BaseByGuestAmt.Factory.newInstance();
					baseByGuestAmtTriple.setAgeQualifyingCode("10");
					baseByGuestAmtTriple.setNumberOfGuests("3");
					baseByGuestAmtTriple.setAmountBeforeTax(Convert.ToString(tripleRate));
					// 四人价格
					BaseByGuestAmt baseByGuestAmtQuad = BaseByGuestAmt.Factory.newInstance();
					baseByGuestAmtQuad.setAgeQualifyingCode("10");
					baseByGuestAmtQuad.setNumberOfGuests("4");
					baseByGuestAmtQuad.setAmountBeforeTax(Convert.ToString(quadRate));

					baseByGuestAmts.setBaseByGuestAmtArray(0, baseByGuestAmtSingle);
					baseByGuestAmts.setBaseByGuestAmtArray(1, baseByGuestAmtDouble);
					baseByGuestAmts.setBaseByGuestAmtArray(2, baseByGuestAmtTriple);
					baseByGuestAmts.setBaseByGuestAmtArray(3, baseByGuestAmtQuad);

					// 加床价格
					AdditionalGuestAmounts additionalGuestAmounts = rate.addNewAdditionalGuestAmounts();
					additionalGuestAmounts.addNewAdditionalGuestAmount();
					additionalGuestAmounts.addNewAdditionalGuestAmount();

					AdditionalGuestAmount additionalGuestAdultsAmount = AdditionalGuestAmount.Factory.newInstance();
					additionalGuestAdultsAmount.setAgeQualifyingCode("10");
					additionalGuestAdultsAmount.setAmount(Convert.ToString(exteraRate));

					AdditionalGuestAmount additionalGuestChildAmount = AdditionalGuestAmount.Factory.newInstance();
					additionalGuestChildAmount.setAgeQualifyingCode("8");
					additionalGuestChildAmount.setAmount(Convert.ToString(exteraRate));
					additionalGuestAmounts.setAdditionalGuestAmountArray(0, additionalGuestAdultsAmount);
					additionalGuestAmounts.setAdditionalGuestAmountArray(1, additionalGuestChildAmount);

					x = y;
				}

				// 房量拼装
				// handle Avail notify RQ

				int m = 0;
				while (m < rateList.size()) {
					AvailAllow availAllow = (AvailAllow) rateList.get(m);
					int allotment = availAllow.getAllotment();
					String avail = availAllow.getAvail();
					Date availDateStart = availAllow.getAvailDate();
					Date availDateEnd = availAllow.getAvailDate();
					int minLos = availAllow.getMinLos();
					int maxLos = availAllow.getMaxLos();

					// 房间限制 1: 不限量
					String allotmenitLimit = availAllow.getAllotmenitLimit();
					String thirdHotelCode = "";
					try {

						List<HotelBean> hotelMapList = getMapService().getHotelMapList(MappingEnum.GENARES.toString());
						for (HotelBean hotelbean : hotelMapList) {
							if (prop.equalsIgnoreCase(hotelbean.getFogHotelCode())) {
								thirdHotelCode = hotelbean.getThirdHotelCode();
								break;
							}
						}
					} catch (MappingException e) {
						throw new MappingException(e);
					}
					if (!StringUtils.hasText(thirdHotelCode)) {
						LOGGER.debug("prop:" + prop + ",thirdHotelCode is null");
						i++;
						continue;
					}
					// 按天数合并内容
					int n = m + 1;
					while (n < rateList.size()) {
						AvailAllow availAllowNext = rateList.get(n);
						int allotmentNext = availAllowNext.getAllotment();
						String availNext = availAllowNext.getAvail();
						int minLosNext = availAllowNext.getMinLos();
						int maxLosNext = availAllowNext.getMaxLos();

						// 房间限制 1: 不限量
						String allotmenitLimitNext = availAllowNext.getAllotmenitLimit();
						if (allotment == allotmentNext && avail.equals(availNext) && minLos == minLosNext
								&& maxLos == maxLosNext && allotmenitLimit.equals(allotmenitLimitNext)) {
							availDateEnd = availAllowNext.getAvailDate();
						} else {
							break;
						}

						n++;
					}
					AvailStatusMessage availStatusMessage = availStatusMessages.addNewAvailStatusMessage();
					if ("1".equalsIgnoreCase(allotmenitLimit)) {
						availStatusMessage.setBookingLimit(maxAllotment);
					} else {
						availStatusMessage.setBookingLimit(Convert.ToString(allotment));
					}
					availStatusMessage.setBookingLimitMessageType("SetLimit");
					StatusApplicationControl statusApplicationControl = availStatusMessage
							.addNewStatusApplicationControl();
					statusApplicationControl.setStart(Convert.DateToString(availDateStart));
					statusApplicationControl.setEnd(Convert.DateToString(availDateEnd));
					statusApplicationControl.setInvTypeCode(mappingResultBean.getHotelRoomCode());
					statusApplicationControl.setRatePlanCode(mappingResultBean.getHotelRateCode());
					LengthsOfStay lengthsOfStay = availStatusMessage.addNewLengthsOfStay();
					lengthsOfStay.addNewLengthOfStay();
					lengthsOfStay.addNewLengthOfStay();
					LengthOfStay lengthOfStayMin = LengthOfStay.Factory.newInstance();
					lengthOfStayMin.setMinMaxMessageType("SetMinLOS");
					lengthOfStayMin.setTime(Convert.ToString(minLos));
					lengthOfStayMin.setTimeUnit("Day");
					LengthOfStay lengthOfStayMax = LengthOfStay.Factory.newInstance();
					lengthOfStayMax.setMinMaxMessageType("SetMaxLOS");
					lengthOfStayMax.setTime(Convert.ToString(maxLos));
					lengthOfStayMax.setTimeUnit("Day");
					lengthsOfStay.setLengthOfStayArray(0, lengthOfStayMin);
					lengthsOfStay.setLengthOfStayArray(1, lengthOfStayMax);
					RestrictionStatus restrictionStatus = availStatusMessage.addNewRestrictionStatus();

					if ("A".equalsIgnoreCase(avail) || "L".equalsIgnoreCase(avail)) {
						restrictionStatus.setStatus("Open");
					} else if ("C".equalsIgnoreCase(avail)) {
						restrictionStatus.setStatus("Close");
						restrictionStatus.setRestriction("Arrival");
					} else {
						restrictionStatus.setStatus("Close");
					}

					m = n;
				}

				// 计划级提前预定日期拼装
				// handle booking rule
				a = 0;
				while (a < rateList.size()) {
					AvailAllow availAllow = (AvailAllow) rateList.get(a);
					Date availDateStart = availAllow.getAvailDate();
					Date availDateEnd = availAllow.getAvailDate();

					int leadTime = availAllow.getLeadTime();

					String thirdHotelCode = "";
					try {

						List<HotelBean> hotelMapList = getMapService().getHotelMapList(MappingEnum.GENARES.toString());
						for (HotelBean hotelbean : hotelMapList) {
							if (prop.equalsIgnoreCase(hotelbean.getFogHotelCode())) {
								thirdHotelCode = hotelbean.getThirdHotelCode();
								break;
							}
						}
					} catch (MappingException e) {
						throw new MappingException(e);
					}
					if (!StringUtils.hasText(thirdHotelCode)) {
						LOGGER.debug("prop:" + prop + ",thirdHotelCode is null");
						i++;
						continue;
					}
					// 按天数合并内容
					int b = a + 1;
					while (b < rateList.size()) {
						AvailAllow availAllowNext = rateList.get(b);
						int leadTimeNext = availAllowNext.getLeadTime();
						if (leadTime == leadTimeNext) {
							availDateEnd = availAllowNext.getAvailDate();
						} else {
							break;
						}

						b++;
					}

					LOGGER.debug("availDateStar:" + Convert.DateToString(availDateStart) + ",availDateEnd:"
							+ Convert.DateToString(availDateEnd) + ",leadTime:" + leadTime);

					// 设置酒店级提前预定日期
					if (leadTime >= 0) {

						addNewRuleMessages.setHotelCode(thirdHotelCode);
						RuleMessage addNewRuleMessage = addNewRuleMessages.addNewRuleMessage();
						net.hubs1.ids.genaresbookingrulenotifrq.BookingRulesDocument.BookingRules addNewBookingRules = addNewRuleMessage
								.addNewBookingRules();
						net.hubs1.ids.genaresbookingrulenotifrq.BookingRuleDocument.BookingRule addNewBookingRule = addNewBookingRules
								.addNewBookingRule();
						addNewBookingRule.setStart(Convert.DateToString(availDateStart));
						addNewBookingRule.setEnd(Convert.DateToString(availDateEnd));
						addNewBookingRule.setMinAdvancedBookingOffset("P0Y0M0DT" + leadTime + "H0M0S");
						net.hubs1.ids.genaresbookingrulenotifrq.StatusApplicationControlDocument.StatusApplicationControl addNewStatusApplicationControl = addNewRuleMessage
								.addNewStatusApplicationControl();
						addNewStatusApplicationControl.setInvTypeCode(mappingResultBean.getHotelRoomCode());
						addNewStatusApplicationControl.setRatePlanCode(mappingResultBean.getHotelRateCode());
					}

					a = b;
				}
			} catch (Exception e) {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErroCode("IDS-0012");
				errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0012"));
				errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
				errorBean.setXml(rateDataBO.toString());
				errorlist.add(errorBean);
				LOGGER.error("parse getThirdRequestXml error:" + ExceptionUtils.getFullStackTrace(e));
				throw new BizException(e);

			}

		}
		// handle booking role xml
		grBookingRulePropXml = otaHotelBookingRuleNotifRQDocument.toString();
		grBookingRulePropXml = grBookingRulePropXml.replaceAll("http://www.hubs1.net/ids/genaresbookingrulenotifrq\"",
				"http://www.opentravel.org/OTA/2003/05\"");
		String headerBookingRuleProp = AssemblyUtil.getSoapHead("OTAHotelBookingRuleNotifRQ", wsseUsername,
				wssePassword);
		String bodyBookingRuleProp = "<soap:Body>" + grBookingRulePropXml + "</soap:Body></soap:Envelope>";
		String grBookingRulePropRequestXml = headerBookingRuleProp + bodyBookingRuleProp;
		LOGGER.info("grBookingRulePropRequestXml:" + grBookingRulePropRequestXml);
		UpdateInfoBean bookingRuleBean = new UpdateInfoBean();
		bookingRuleBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE3);
		bookingRuleBean.setRequestXml(grBookingRulePropRequestXml);
		bookingRuleBean.setReqestURL(getThirdRequestXmlURL());
		updateInfoBeanList.add(bookingRuleBean);

		// handle Hotel avail notify RQ XML
		grAvailPropXml = otaHotelAvailNotifRQDocument.toString();
		grAvailPropXml = grAvailPropXml.replaceAll("http://www.hubs1.net/ids/genareshotelavailnotifyrq\"",
				"http://www.opentravel.org/OTA/2003/05\"");
		String headerAvailProp = AssemblyUtil.getSoapHead("OTAHotelAvailNotifRQ", wsseUsername, wssePassword);
		String bodyAvailProp = "<soap:Body>" + grAvailPropXml + "</soap:Body></soap:Envelope>";
		String grAvailPropRequestXml = headerAvailProp + bodyAvailProp;
		LOGGER.info("grAvailPropRequestXml:" + grAvailPropRequestXml);
		UpdateInfoBean availPlanBean = new UpdateInfoBean();
		availPlanBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE2);
		availPlanBean.setRequestXml(grAvailPropRequestXml);
		availPlanBean.setReqestURL(getThirdRequestXmlURL());
		updateInfoBeanList.add(availPlanBean);

		// handle hotel rate plan notify RQ XML
		if (otaHotelRatePlanNotifRQDocument != null) {
			grPriceXml = otaHotelRatePlanNotifRQDocument.toString();
			grPriceXml = grPriceXml.replaceAll("http://www.hubs1.net/ids/genaresrateplannotifyrq\"",
					"http://www.opentravel.org/OTA/2003/05\"");
			String headerPrice = AssemblyUtil.getSoapHead("OTAHotelRatePlanNotifRQ", wsseUsername, wssePassword);
			String bodyPrice = "<soap:Body>" + grPriceXml + "</soap:Body></soap:Envelope>";
			String grPriceRequestXml = headerPrice + bodyPrice;
			LOGGER.info("grPriceRequestXml:" + grPriceRequestXml);
			UpdateInfoBean priceBean = new UpdateInfoBean();
			priceBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
			priceBean.setRequestXml(grPriceRequestXml);
			priceBean.setReqestURL(getThirdRequestXmlURL());
			updateInfoBeanList.add(priceBean);
		}
		return updateInfoBeanList;
	}


	@Override
	protected void addAvailAllowToCacheList(AvailAllow bean,AvailBean availBean, String plan,
			String prop, String ideType, List<EIdsCache> updateCacheList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean validateEq(EIdsCache dist, AvailAllow src,AvailBean srcAvailBean) {
		// TODO Auto-generated method stub
		return false;
	}

}
