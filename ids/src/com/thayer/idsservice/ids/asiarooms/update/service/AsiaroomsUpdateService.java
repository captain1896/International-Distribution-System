package com.thayer.idsservice.ids.asiarooms.update.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.opentravel.ota.x2003.x05.AvailStatusMessageType;
import org.opentravel.ota.x2003.x05.ErrorType;
import org.opentravel.ota.x2003.x05.ErrorsType;
import org.opentravel.ota.x2003.x05.MessageAcknowledgementType;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRSDocument;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRSDocument;
import org.opentravel.ota.x2003.x05.POSType;
import org.opentravel.ota.x2003.x05.SellableProductsType;
import org.opentravel.ota.x2003.x05.SourceType;
import org.opentravel.ota.x2003.x05.StatusApplicationControlType;
import org.opentravel.ota.x2003.x05.SuccessType;
import org.opentravel.ota.x2003.x05.AvailStatusMessageType.BookingLimitMessageType;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRQDocument.OTAHotelAvailNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelAvailNotifRQDocument.OTAHotelAvailNotifRQ.AvailStatusMessages;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans.RatePlan;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans.RatePlan.Rates;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans.RatePlan.Rates.Rate;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRSDocument.OTAHotelRatePlanNotifRS;
import org.opentravel.ota.x2003.x05.RateUploadType.BaseByGuestAmts;
import org.opentravel.ota.x2003.x05.RateUploadType.BaseByGuestAmts.BaseByGuestAmt;
import org.opentravel.ota.x2003.x05.SellableProductsType.SellableProduct;
import org.opentravel.ota.x2003.x05.SourceType.BookingChannel;
import org.opentravel.ota.x2003.x05.SourceType.RequestorID;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.fog2.entity.AvailRate;
import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.dao.ERatemap;
import com.thayer.idsservice.dao.ERatemapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.RsStatusBean;
import com.thayer.idsservice.task.update.bean.UpdateInfoBean;
import com.thayer.idsservice.task.update.interf.AbstractUploadService;
import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.Constents;

public class AsiaroomsUpdateService extends AbstractUploadService {

	protected static Logger LOGGER = Logger.getLogger(AsiaroomsUpdateService.class);

	private String asiaroomsUserId;

	private String asiaroomsUserPwd;
	
	private String iata;
	
	private ERatemapDAO eratemapDAO;

	public String getAsiaroomsUserId() {
		return asiaroomsUserId;
	}

	public void setAsiaroomsUserId(String asiaroomsUserId) {
		this.asiaroomsUserId = asiaroomsUserId;
	}

	public String getAsiaroomsUserPwd() {
		return asiaroomsUserPwd;
	}

	public void setAsiaroomsUserPwd(String asiaroomsUserPwd) {
		this.asiaroomsUserPwd = asiaroomsUserPwd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UpdateInfoBean> getThirdRequestXml(AvailBean availbean, List<ErrorBean> errorlist) throws BizException,
			MappingException {
		List<RateDataBO> tempList =	availbean.getRateDataBOList();
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		// 如果RateDataBOList为空则返回空list；
		if (tempList == null || tempList.size() == 0) {
			return null;
		}
		
		//过滤ERateMap表中没有映射关系的
		List<RateDataBO> getzRateMapList = new ArrayList<RateDataBO>();
		ERatemap queryPara = new ERatemap();
		queryPara.setExwebCode(iata);
		List<ERatemap> rateMapList = eratemapDAO.findByExample(queryPara);
		if(CollectionUtils.isNotEmpty(rateMapList)){
			for(RateDataBO bo : tempList){
				String fogpropId = bo.getProp();
				String planCode = bo.getPlanCode();
				if(StringUtils.isNotBlank(fogpropId) && StringUtils.isNotBlank(planCode)){
					for(ERatemap rateMap : rateMapList){
						if(rateMap.getFogpropId().equals(fogpropId)
								&& rateMap.getFograteId().equals(planCode)){
							getzRateMapList.add(bo);
						}
			            	
					}
					
				}
			}
		}else{
			getzRateMapList = tempList;
		}
		
		
		// ratePlanDocument
		OTAHotelRatePlanNotifRQDocument ratePlanNotifRQDocument = OTAHotelRatePlanNotifRQDocument.Factory.newInstance();
		OTAHotelRatePlanNotifRQ addNewOTAHotelRatePlanNotifRQ = ratePlanNotifRQDocument.addNewOTAHotelRatePlanNotifRQ();
		addNewOTAHotelRatePlanNotifRQ.setEchoToken(CommonUtils.getUuid());// ??
		{
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			addNewOTAHotelRatePlanNotifRQ.setTimeStamp(c);
		}

		OTAHotelAvailNotifRQDocument availNotifRQDocument = OTAHotelAvailNotifRQDocument.Factory.newInstance();
		OTAHotelAvailNotifRQ addNewOTAHotelAvailNotifRQ = availNotifRQDocument.addNewOTAHotelAvailNotifRQ();
		addNewOTAHotelAvailNotifRQ.setEchoToken(CommonUtils.getUuid());
		{
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			addNewOTAHotelAvailNotifRQ.setTimeStamp(c);
		}
		// Asiarooms RatePlanPOS
		POSType addNewPOS = addNewOTAHotelRatePlanNotifRQ.addNewPOS();
		SourceType addNewSource = addNewPOS.addNewSource();
		RequestorID addNewRequestorID = addNewSource.addNewRequestorID();
		addNewRequestorID.setID(asiaroomsUserId);
		addNewRequestorID.setMessagePassword(asiaroomsUserPwd);
		BookingChannel addNewBookingChannel = addNewSource.addNewBookingChannel();
		addNewBookingChannel.setType("7");

		// Asiarooms AvailPOS
		POSType addNewAvailPOS = addNewOTAHotelAvailNotifRQ.addNewPOS();
		SourceType addNewAvailSource = addNewAvailPOS.addNewSource();
		RequestorID addNewAvailRequestorID = addNewAvailSource.addNewRequestorID();
		addNewAvailRequestorID.setID(asiaroomsUserId);
		addNewAvailRequestorID.setMessagePassword(asiaroomsUserPwd);
		BookingChannel addNewAvailBookingChannel = addNewAvailSource.addNewBookingChannel();
		addNewAvailBookingChannel.setType("7");

		for (RateDataBO rateDataBO : getzRateMapList) {
			try {

				List<AvailAllow> rateList = rateDataBO.getRateList();

				if (rateList == null || rateList.size() == 0) {
					continue;
				}

				String planCode = rateDataBO.getPlanCode();
				String[] planCodeSplit = planCode.split("-");
				planCode = planCodeSplit[1];
				String prop = rateDataBO.getProp();
				String roomId = rateDataBO.getRoom().getRoomId();
				// Asiarooms RatePlan
				RatePlans addNewRatePlans = addNewOTAHotelRatePlanNotifRQ.addNewRatePlans();
				String hotelcode = prop;
				addNewRatePlans.setHotelCode(hotelcode);

				// Asiarooms Avail
				AvailStatusMessages addNewAvailStatusMessages = addNewOTAHotelAvailNotifRQ.addNewAvailStatusMessages();

				addNewAvailStatusMessages.setHotelCode(hotelcode);

				RatePlan addNewRatePlan = addNewRatePlans.addNewRatePlan();
				addNewRatePlan.setRatePlanCode(planCode);
				Rates addNewRates = addNewRatePlan.addNewRates();

				int a = 0;
				while (a < rateList.size()) {
					AvailAllow availAllow = (AvailAllow) rateList.get(a);
					int allotment = availAllow.getAllotment();
					String avail = availAllow.getAvail();
					Date availDateFrom = availAllow.getAvailDate();
					Date availDateTill = availAllow.getAvailDate();
					// Asiarooms Avail
					AvailStatusMessageType addNewAvailStatusMessage = addNewAvailStatusMessages
							.addNewAvailStatusMessage();
					if ("C".equals(avail.trim())) {
						allotment = 0;
					}

					int b = a + 1;
					while (b < rateList.size()) {
						AvailAllow availAllowNext = rateList.get(b);
						int allotmentNext = availAllowNext.getAllotment();
						String availNext = availAllowNext.getAvail();
						if ("C".equals(availNext.trim())) {
							allotmentNext = 0;
						}
						if (allotment == allotmentNext) {
							availDateTill = availAllowNext.getAvailDate();
						} else {
							break;
						}

						b++;
					}

					XmlCalendar fromDate = new XmlCalendar(dateFmt.format(availDateFrom));
					XmlCalendar toDate = new XmlCalendar(dateFmt.format(availDateTill));

					// Asiarooms Avail
					addNewAvailStatusMessage.setBookingLimitMessageType(BookingLimitMessageType.SET_LIMIT);
					addNewAvailStatusMessage.setBookingLimit(BigInteger.valueOf(allotment));
					StatusApplicationControlType addNewStatusApplicationControl = addNewAvailStatusMessage
							.addNewStatusApplicationControl();
					addNewStatusApplicationControl.setStart(fromDate);
					addNewStatusApplicationControl.setEnd(toDate);
					addNewStatusApplicationControl.setRatePlanCode(planCode);
					addNewStatusApplicationControl.setInvCode(roomId);

					a = b;
				}

				// handle rate
				int x = 0;
				while (x < rateList.size()) {

					AvailAllow availAllow = (AvailAllow) rateList.get(x);
					Date availDateFrom = availAllow.getAvailDate();
					Date availDateTill = availAllow.getAvailDate();
					AvailRate availRate = availAllow.getAvailRate();

					double singleRate = availRate.getSingleRate();
					double doubleRate = availRate.getDoubleRate();
					double tripleRate = availRate.getTripleRate();

					int y = x + 1;
					while (y < rateList.size()) {
						AvailAllow availAllowNext = rateList.get(y);
						AvailRate availRateNext = availAllowNext.getAvailRate();
						double singleRateNext = availRateNext.getSingleRate();
						double doubleRateNext = availRateNext.getDoubleRate();
						double tripleRateNext = availRateNext.getTripleRate();

						if (singleRate == singleRateNext && doubleRate == doubleRateNext
								&& tripleRate == tripleRateNext) {
							availDateTill = availAllowNext.getAvailDate();
						} else {
							break;
						}

						y++;
					}

					Rate addNewRate = addNewRates.addNewRate();
					BaseByGuestAmts addNewBaseByGuestAmts = addNewRate.addNewBaseByGuestAmts();
					// Asiarooms RatePlan
					{
						// 单人价
						BaseByGuestAmt addNewBaseByGuestAmt = addNewBaseByGuestAmts.addNewBaseByGuestAmt();

						addNewBaseByGuestAmt.setAmountAfterTax(new BigDecimal(singleRate));
						addNewBaseByGuestAmt.setNumberOfGuests(1);
						addNewBaseByGuestAmt.setAgeQualifyingCode("10");
					}
					{
						// 双人价
						BaseByGuestAmt addNewBaseByGuestAmt = addNewBaseByGuestAmts.addNewBaseByGuestAmt();
						addNewBaseByGuestAmt.setAmountAfterTax(new BigDecimal(doubleRate));
						addNewBaseByGuestAmt.setNumberOfGuests(2);
						addNewBaseByGuestAmt.setAgeQualifyingCode("10");
					}
					{
						// 三人价
						BaseByGuestAmt addNewBaseByGuestAmt = addNewBaseByGuestAmts.addNewBaseByGuestAmt();
						addNewBaseByGuestAmt.setAmountAfterTax(new BigDecimal(tripleRate));
						addNewBaseByGuestAmt.setNumberOfGuests(3);
						addNewBaseByGuestAmt.setAgeQualifyingCode("10");
					}
					// SellableProducts
					XmlCalendar fromDate = new XmlCalendar(dateFmt.format(availDateFrom));
					addNewRate.setStart(fromDate);
					XmlCalendar toDate = new XmlCalendar(dateFmt.format(availDateTill));
					addNewRate.setEnd(toDate);
					SellableProductsType addNewSellableProducts = addNewRatePlan.addNewSellableProducts();
					SellableProduct addNewSellableProduct = addNewSellableProducts.addNewSellableProduct();
					addNewSellableProduct.setInvCode(roomId);
					addNewSellableProduct.setInvType("ROOM");

					x = y;
				}
			} catch (Exception e) {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErroCode("IDS-0012");
				errorBean.setErrorDesc("IDS-Alert: 组装IDS更新信息出错");
				errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
				errorBean.setXml(rateDataBO.toString());
				errorlist.add(errorBean);
				LOGGER.error("parse getThirdRequestXml error:" + ExceptionUtils.getFullStackTrace(e));

			}

		}

		List<UpdateInfoBean> updateInfoBeanList = new ArrayList();
		{
			if (addNewOTAHotelRatePlanNotifRQ.getRatePlans() != null) {
				String ycsxmldocumentstr = ratePlanNotifRQDocument.toString();
				UpdateInfoBean updateInfoBean = new UpdateInfoBean();
				updateInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
				updateInfoBean.setRequestXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ycsxmldocumentstr);
				LOGGER.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ycsxmldocumentstr);
				updateInfoBean.setReqestURL(getThirdRequestXmlURL());
				updateInfoBeanList.add(updateInfoBean);
			}
		}

		{
			if (addNewOTAHotelAvailNotifRQ.getAvailStatusMessages() != null) {
				String ycsxmldocumentstr = availNotifRQDocument.toString();
				UpdateInfoBean updateInfoBean = new UpdateInfoBean();
				updateInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE2);
				updateInfoBean.setRequestXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ycsxmldocumentstr);
				LOGGER.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ycsxmldocumentstr);
				updateInfoBean.setReqestURL(getThirdRequestXmlURL());
				updateInfoBeanList.add(updateInfoBean);
			}
		}

		return updateInfoBeanList;

	}

	@Override
	public RsStatusBean parseResponseXml(String updateResponseXml, String type) throws BizException {
		LOGGER.info("parseResponseXml:\n" + updateResponseXml);
		RsStatusBean rsStatusBean = new RsStatusBean();
		if (Constents.IDS_UPDATE_REQUEST_TYPE1.equalsIgnoreCase(type)) {
			try {
				OTAHotelRatePlanNotifRSDocument parse = OTAHotelRatePlanNotifRSDocument.Factory
						.parse(updateResponseXml);
				OTAHotelRatePlanNotifRS otaHotelRatePlanNotifRS = parse.getOTAHotelRatePlanNotifRS();
				SuccessType success = otaHotelRatePlanNotifRS.getSuccess();
				if (success != null) {
					rsStatusBean.setRsResult(true);
					rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
					rsStatusBean.setRsStatus("success");
				} else {
					ErrorsType errors = otaHotelRatePlanNotifRS.getErrors();
					ErrorType[] errorArray = errors.getErrorArray();
					if (errorArray.length > 0) {
						rsStatusBean.setRsResult(false);
						rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
						rsStatusBean.setRsStatus("error");
					}
				}
			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(e));
				rsStatusBean.setRsResult(false);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsStatus("error");
			}
		}

		if (Constents.IDS_UPDATE_REQUEST_TYPE2.equalsIgnoreCase(type)) {
			try {
				OTAHotelAvailNotifRSDocument parse = OTAHotelAvailNotifRSDocument.Factory.parse(updateResponseXml);
				MessageAcknowledgementType otaHotelAvailNotifRS = parse.getOTAHotelAvailNotifRS();
				SuccessType success = otaHotelAvailNotifRS.getSuccess();
				if (success != null) {
					rsStatusBean.setRsResult(true);
					rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				} else {
					ErrorsType errors = otaHotelAvailNotifRS.getErrors();
					ErrorType[] errorArray = errors.getErrorArray();
					if (errorArray.length > 0) {
						rsStatusBean.setRsResult(false);
						rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
					}
				}
			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(e));
				rsStatusBean.setRsResult(false);
				rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
				rsStatusBean.setRsStatus("ERROR");
			}
		}

		return rsStatusBean;
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

	/**
	 * @return the eratemapDAO
	 */
	public ERatemapDAO getEratemapDAO() {
		return eratemapDAO;
	}

	/**
	 * @param eratemapDAO the eratemapDAO to set
	 */
	public void setEratemapDAO(ERatemapDAO eratemapDAO) {
		this.eratemapDAO = eratemapDAO;
	}

	/**
	 * @return the iata
	 */
	public String getIata() {
		return iata;
	}

	/**
	 * @param iata the iata to set
	 */
	public void setIata(String iata) {
		this.iata = iata;
	}
	
	

}
