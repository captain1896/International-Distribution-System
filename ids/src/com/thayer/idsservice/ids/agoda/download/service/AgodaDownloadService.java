package com.thayer.idsservice.ids.agoda.download.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlException;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.agoda.bean.Booking;
import com.thayer.idsservice.ids.agoda.bean.BookingData;
import com.thayer.idsservice.ids.agoda.bean.BookingDetail;
import com.thayer.idsservice.ids.agoda.bean.GetBooking;
import com.thayer.idsservice.ids.agoda.bean.GetBookingLists;
import com.thayer.idsservice.ids.agoda.bean.RateType;
import com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument;
import com.thayer.idsservice.ids.agoda.bean.BookingDetail.Rates;
import com.thayer.idsservice.ids.agoda.bean.BookingDetail.UPC;
import com.thayer.idsservice.ids.agoda.bean.BookingDetail.Rates.RoomRate;
import com.thayer.idsservice.ids.agoda.bean.HeaderDataDocument.HeaderData;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.task.download.interf.AbstractDownLoadService;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.DateUtil;

public class AgodaDownloadService extends AbstractDownLoadService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7037474769295193930L;

	private String agodaUserId;

	private String agodaUserPwd;

	private static Logger LOGGER = Logger.getLogger(AgodaDownloadService.class);

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
	 * 转换成fogNewResv
	 * 
	 * @param responseXml
	 */
	public void pars2FogNewResvBean(String responseXml) {
		return;
	}

	/**
	 * 获得请求代理接口订单列表的requestxml
	 * 
	 * @param hotelid
	 * @param since
	 * @param till
	 * @return
	 */
	@Override
	public String getBookingListRequestXml(HotelBean hotelBean) throws BizException {
		YCSXMLDocument newInstance = YCSXMLDocument.Factory.newInstance();
		GetBookingLists getBookingLists = newInstance.addNewYCSXML().addNewGetBookingLists();

		HeaderData addNewHeaderData = getBookingLists.addNewHeaderData();
		addNewHeaderData.setUserID(agodaUserId);
		addNewHeaderData.setPassword(agodaUserPwd);
		addNewHeaderData.setHotelID(new BigInteger(hotelBean.getThirdHotelCode()));

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		Date requestSince = hotelBean.getRequestSince();

		XmlCalendar calendarSince = new XmlCalendar(f.format(DateUtil.dateAdd(DateUtil.TIME_UNIT_D, -1, requestSince)));
		getBookingLists.setRequestSince(calendarSince);

		Date requestTill = hotelBean.getRequestTill();
		XmlCalendar calendarTill = new XmlCalendar(f.format(DateUtil.dateAdd(DateUtil.TIME_UNIT_D, 1, requestTill)));
		getBookingLists.setRequestTill(calendarTill);

		getBookingLists.setAction(new BigInteger("0"));
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + newInstance.toString();
		LOGGER.info("start getBookingListRequestXml : \n" + result);
		return result;
	}

	@Override
	public String[] handleResponseXml4detailXml(HotelBean hotelBean, String bookingListResponseXml) throws BizException {
		LOGGER.info("handleResponseXml4detailXml : \n" + bookingListResponseXml);
		if (StringUtils.hasText(bookingListResponseXml)) {
			if (bookingListResponseXml.toLowerCase().contains("false")) {
				throw new BizException("bookingListResponseXml return false xml:" + bookingListResponseXml);
			}

			com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument bookdingListResponsedocument = null;
			try {
				bookdingListResponsedocument = com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument.Factory
						.parse(bookingListResponseXml);
			} catch (XmlException e) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			}
			BookingData bookingData = bookdingListResponsedocument.getYCSXML().getBookingData();
			Booking[] bookingArray = bookingData.getBookings().getBookingArray();
			String[] requestlist = new String[bookingArray.length];
			int i = 0;
			for (Booking booking : bookingArray) {
				LOGGER.info("Booking : \n" + booking.toString());
				String cnfnum = null;
				BigInteger bookingID = booking.getBookingID();// agoda订单号
				try {
					EResvMap eResvMap = new EResvMap();
					eResvMap.setAgodaCnfnum(bookingID.toString());
					eResvMap.setBk5(hotelBean.getThirdIataCode());
					List<EResvMap> eresvMapList = getEresvMapDAO().findByExample(eResvMap);
					/**
					 * 如果存在订单映射的话 跳过
					 */
					if (eresvMapList != null && eresvMapList.size() > 0) {
						continue;
					} else {
						/**
						 * 不存在的话保存agoda订单号 映射
						 */
						EResvMap eresvmap = new EResvMap();
						eresvmap.setAgodaCnfnum(bookingID.toString());
						eresvmap.setCreatedate(new Date());
						eresvmap.setBk3(booking.getCheckInDate().getTime());//入住时间
						eresvmap.setRequesteddate(DateUtil.formatDate(new Date()));
						eresvmap.setBk5(hotelBean.getThirdIataCode());
						getEresvMapDAO().attachDirty(eresvmap);
					}
				} catch (Exception e) {
					LOGGER.error(ExceptionUtils.getFullStackTrace(e));
					throw new BizException(e);
				}
				try {
					// String bookingID = booking.getBookingID();
					com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument newInstance = com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument.Factory
							.newInstance();
					GetBooking addNewGetBooking = newInstance.addNewYCSXML().addNewGetBooking();
					HeaderData addNewHeaderData = addNewGetBooking.addNewHeaderData();
					addNewHeaderData.setHotelID(new BigInteger(hotelBean.getThirdHotelCode()));// 酒店号
					addNewHeaderData.setUserID(agodaUserId);
					addNewHeaderData.setPassword(agodaUserPwd);

					addNewGetBooking.setBookingID(bookingID);
					String resultString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + newInstance.toString();
					LOGGER.info("handle detail request Xml : \n" + resultString);
					requestlist[i] = resultString;

				} catch (Exception e) {
					LOGGER.error(ExceptionUtils.getFullStackTrace(e));
				}
				i++;
			}

			return requestlist;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.AbstractDownLoadService#handleResponseXml4fogbean(java.lang.String)
	 */
	@Override
	public TResvBase handleResponseXml4fogbean(HotelBean hotelBean, String bookingResponseXml) throws BizException,
			MappingException {
		LOGGER.info("handleResponseXml4fogbean : \n" + bookingResponseXml);
		TResvBase tresvbase = new TResvBase();
		try {
			com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument parseInstance = com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument.Factory
					.parse(bookingResponseXml);
			BookingDetail bookingDetailData = parseInstance.getYCSXML().getBookingDetailData();
			String bookingID = bookingDetailData.getBookingID().toString();
			double extraBed = 0.0;
			if (bookingDetailData.getExtrabed() != null) {
				extraBed = bookingDetailData.getExtrabed().doubleValue();
			}
			tresvbase.setResvtype(Constents.RESV_TYPE_NEW);
			tresvbase.setOutcnfnum(bookingID);
			tresvbase.setProp(hotelBean.getFogHotelCode());
			tresvbase.setIata(hotelBean.getFogIataCode());
			tresvbase.setIndate(bookingDetailData.getCheckInDate().getTime());
			tresvbase.setOutdate(bookingDetailData.getCheckOutDate().getTime());
			tresvbase.setNights(bookingDetailData.getNoOfNight().intValue());
			tresvbase.setRooms(bookingDetailData.getNoOfRoom().intValue());
			tresvbase.setAdults(bookingDetailData.getAdult().intValue());
			if(bookingDetailData.getChild()!=null){
				tresvbase.setChildren(bookingDetailData.getChild().intValue());
			}else{
				tresvbase.setChildren(0);
			}
			
			String fogroomtype = "";

			fogroomtype = getMapService().getRoomRateMap(String.valueOf(bookingDetailData.getRoomTypeID()),
					hotelBean.getFogHotelCode(), MappingEnum.AGODA, "roomType");

			tresvbase.setRoomcode(fogroomtype);
			String fograteClass = "";

			fograteClass = getMapService().getRoomRateMap(
					String.valueOf(bookingDetailData.getRoomTypeID()) + "#"
							+ String.valueOf(bookingDetailData.getRoomTypeID()), hotelBean.getFogHotelCode(),
					MappingEnum.AGODA, "rateType");

			tresvbase.setRateClass(fograteClass);

			if (StringUtils.hasLength(bookingDetailData.getFirstName())) {
				tresvbase.setGfirstname(bookingDetailData.getFirstName());
			} else {
				tresvbase.setGfirstname("");
			}
			if (StringUtils.hasLength(bookingDetailData.getLastName())) {
				tresvbase.setGlastname(bookingDetailData.getLastName());
			} else {
				tresvbase.setGlastname("");
			}
			
			//客人姓名的处理
			StringBuffer guestName = new StringBuffer();
			guestName.append(bookingDetailData.getFirstName()).append(" ").append(bookingDetailData.getLastName());
			tresvbase.setGuestname(guestName.toString());
			List<String> guestList = new ArrayList<String>();
			guestList.add(guestName.toString());
			tresvbase.setGuestList(guestList);

			if (StringUtils.hasText(bookingDetailData.getPhoneNumber())) {
				tresvbase.setGuestphone(bookingDetailData.getPhoneNumber());
			} else {
				tresvbase.setGuestphone("02161226688*7800");
			}

			if (StringUtils.hasText(bookingDetailData.getEmail())) {
				tresvbase.setGuestmail(bookingDetailData.getEmail());
			} else {
				tresvbase.setGuestmail("service@hubs1.net");
			}

			/**
			 * Node :Crsmessage/Reservation/Bookedrates
			 */
			// tresvbase.setCctype("Pre-Paid");
			UPC upc = bookingDetailData.getUPC();
			if (upc != null && !upc.isNil()) {
				tresvbase.setCvv(upc.getCVV());
				tresvbase.setCcnumber(upc.getNumber());
				tresvbase.setCcexpiration(upc.getExpired());
				tresvbase.setCctype(upc.getCardType());
			}

			tresvbase.setRemark("");
			if (StringUtils.hasText(bookingDetailData.getPromotion())
					|| StringUtils.hasText(bookingDetailData.getCustomerRequest())) {
				if (StringUtils.hasText(bookingDetailData.getPromotion())) {

					tresvbase.setRemark(tresvbase.getRemark() + bookingDetailData.getPromotion());
				}
				if (StringUtils.hasText(bookingDetailData.getCustomerRequest())) {
					tresvbase.setRemark(tresvbase.getRemark() + bookingDetailData.getCustomerRequest());
				}
			}
			tresvbase.setRemark(tresvbase.getRemark() + "agoda订单号:" + bookingID);
			tresvbase.setRemark(tresvbase.getRemark() + "房费和服务费已预付，请勿向客人收取，其他费用客人自理。任何情况下都不能向客人透漏房价信息.");

			List<TResvRate> resvRateList = new ArrayList<TResvRate>();
			tresvbase.setResvRateList(resvRateList);
			Rates rates = bookingDetailData.getRates();
			String localCurrency = rates.getLocalCurrency().getStringValue();
			RoomRate roomRate = rates.getRoomRate();
			String ratestotal = String.valueOf(roomRate.getTotal());
			int i = 0;
			String datefrom = "";
			// Map<String, BigDecimal> agodaRateMap = new LinkedHashMap<String, BigDecimal>();
			for (RateType rate : roomRate.getRateArray()) {
				if (i == 0) {
					datefrom = rate.getDate();
				}
				TResvRate tResvRate = new TResvRate();
				tResvRate.setBookendate(DateUtil.dateValue(rate.getDate()));
				tResvRate.setBookendatestr(rate.getDate());
				// TODO 货币代码
				tResvRate.setPropCurrency(localCurrency);
				tResvRate.setExtrate(extraBed);

				tResvRate.setProprevenue(rate.getBigDecimalValue().doubleValue());
				resvRateList.add(tResvRate);

				// agodaRateMap.put(rate.getDate(), rate.getBigDecimalValue());
				i++;
			}

		} catch (MappingException e) {
			LOGGER.error("Mapping error:" + ExceptionUtils.getFullStackTrace(e));
			throw new MappingException(e.getMessage());
		} catch (XmlException e) {
			LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
			throw new BizException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
			throw new BizException(e.getMessage());
		}
		return tresvbase;
	}

	@Override
	public List<TResvBase> handleResponseXml4fogbeanList(HotelBean hotelBean, String bookingListResponseXml,
			ResultBean resultBean) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		System.out.println("＜".equals("<"));
	}

}
