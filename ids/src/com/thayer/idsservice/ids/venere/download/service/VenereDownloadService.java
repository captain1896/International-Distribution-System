/*****************************************************************<br>
 * <B>FILE :</B> VenereDownloadService.java <br>
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
package com.thayer.idsservice.ids.venere.download.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.opentravel.ota.x2003.x05.CommentType;
import org.opentravel.ota.x2003.x05.CustomerType;
import org.opentravel.ota.x2003.x05.DateOrTimeOrDateTimeType;
import org.opentravel.ota.x2003.x05.HotelReservationType;
import org.opentravel.ota.x2003.x05.OTAReadRQDocument;
import org.opentravel.ota.x2003.x05.OTAResRetrieveRSDocument;
import org.opentravel.ota.x2003.x05.PaymentCardType;
import org.opentravel.ota.x2003.x05.RoomRateType;
import org.opentravel.ota.x2003.x05.TPAExtensionsType;
import org.opentravel.ota.x2003.x05.VerificationType;
import org.opentravel.ota.x2003.x05.CommentType.Comment;
import org.opentravel.ota.x2003.x05.OTAReadRQDocument.OTAReadRQ;
import org.opentravel.ota.x2003.x05.OTAReadRQDocument.OTAReadRQ.ReadRequests;
import org.opentravel.ota.x2003.x05.OTAReadRQDocument.OTAReadRQ.ReadRequests.HotelReadRequest;
import org.opentravel.ota.x2003.x05.OTAResRetrieveRSDocument.OTAResRetrieveRS.ReservationsList;
import org.opentravel.ota.x2003.x05.RoomStaysType.RoomStay;
import org.opentravel.ota.x2003.x05.VerificationType.ReservationTimeSpan;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.comm.FogUtil;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.venere.beans.VenereCreditCardDetail;
import com.thayer.idsservice.ids.venere.beans.VenereDetailsBean;
import com.thayer.idsservice.ids.venere.beans.VenereMailBean;
import com.thayer.idsservice.ids.venere.beans.VenerePersonDetail;
import com.thayer.idsservice.ids.venere.beans.VenereRoomDetail;
import com.thayer.idsservice.ids.venere.beans.VenereRoomRate;
import com.thayer.idsservice.ids.venere.util.VenereUtils;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.task.download.interf.AbstractDownLoadService;
import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.DateUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-4-18<br>
 * @version : v1.0
 */
public class VenereDownloadService extends AbstractDownLoadService {
	private static Logger LOGGER = Logger.getLogger(VenereDownloadService.class);
	private String venereUserId;
	private String venereUserPwd;
	private String venereOrgId;
	private int timeZone;
	private int preTime;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8321722488187825068L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.AbstractDownLoadService#getBookingListRequestXml(com.thayer.idsservice
	 * .task.download.bean.HotelBean)
	 */
	@Override
	public String getBookingListRequestXml(HotelBean hotelBean) throws BizException {
		OTAReadRQDocument readRQDocument = OTAReadRQDocument.Factory.newInstance();
		OTAReadRQ readRQ = readRQDocument.addNewOTAReadRQ();
		readRQ.setEchoToken(CommonUtils.getUuid());
		readRQ.setTarget(OTAReadRQDocument.OTAReadRQ.Target.Enum.forString("Production"));
		readRQ.setVersion(new BigDecimal("1.007"));
		readRQ.setReturnListIndicator(true);
		ReadRequests readRequests = readRQ.addNewReadRequests();
		HotelReadRequest hotelReadRequest = readRequests.addNewHotelReadRequest();
		hotelReadRequest.setHotelCode(hotelBean.getThirdHotelCode());
		VerificationType verification = hotelReadRequest.addNewVerification();
		ReservationTimeSpan reservationTimeSpan = verification.addNewReservationTimeSpan();
		Date requestSince = hotelBean.getRequestSince();

		Date requestTillLocal = hotelBean.getRequestTill();
		Calendar calTill = Calendar.getInstance();
		calTill.setTime(requestTillLocal);
		calTill.add(Calendar.MINUTE, timeZone);
		Date requestTillVenere = calTill.getTime();
		Date requestTill = requestTillVenere;
		hotelBean.setRequestTill(requestTillVenere);

		Calendar calSince = Calendar.getInstance();
		calSince.setTime(requestSince);
		calSince.add(Calendar.MINUTE, preTime);
		requestSince = calSince.getTime();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		LOGGER.info(dateFormat.format(requestSince));
		LOGGER.info(dateFormat.format(requestTill));

		DateOrTimeOrDateTimeType xstart = DateOrTimeOrDateTimeType.Factory.newInstance();
		xstart.set(dateFormat.format(requestSince));
		reservationTimeSpan.xsetStart(xstart);

		DateOrTimeOrDateTimeType xend = DateOrTimeOrDateTimeType.Factory.newInstance();
		xend.set(dateFormat.format(requestTill));
		reservationTimeSpan.xsetEnd(xend);

		hotelReadRequest.addNewTPAExtensions();
		String requestXml = readRQDocument.toString();
		String result = requestXml
				.replaceAll(
						"<TPA_Extensions/>",
						" <TPA_Extensions><Venere xmlns=\"http://www.venere.com/XHI/TPA_Extensions\" ReservationDateTimeSpanIndicator=\"update\"/></TPA_Extensions>");
		result = VenereUtils.buildFullXmlRQ(venereOrgId, venereUserId, venereUserPwd, result);
		LOGGER.info("getBookingListRequestXml : \n" + result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.AbstractDownLoadService#handleResponseXml4detailXml(com.thayer.idsservice
	 * .task.download.bean.HotelBean, java.lang.String)
	 */
	@Override
	public String[] handleResponseXml4detailXml(HotelBean hotelBean, String bookingListResponseXml) throws BizException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.AbstractDownLoadService#handleResponseXml4fogbean(com.thayer.idsservice
	 * .task.download.bean.HotelBean, java.lang.String)
	 */
	@Override
	public TResvBase handleResponseXml4fogbean(HotelBean hotelBean, String bookingResponseXml) throws BizException,
			MappingException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.thayer.idsservice.task.download.interf.AbstractDownLoadService#handleResponseXml4fogbeanList(com.thayer.
	 * idsservice.task.download.bean.HotelBean, java.lang.String, com.thayer.idsservice.task.download.bean.ResultBean)
	 */
	@Override
	public List<TResvBase> handleResponseXml4fogbeanList(HotelBean hotelBean, String bookingListResponseXml,
			ResultBean resultBean) throws BizException {
		LOGGER.info("handleResponseXml4fogbeanList :  \n" + bookingListResponseXml);
		// handle resv status;

		bookingListResponseXml = bookingListResponseXml.replaceAll("ResStatus=\"CO\"", "ResStatus=\"Cancel\"");
		bookingListResponseXml = bookingListResponseXml.replaceAll("ResStatus=\"UD\"", "ResStatus=\"Cancel\"");
		bookingListResponseXml = bookingListResponseXml.replaceAll("ResStatus=\"VA\"", "ResStatus=\"Cancel\"");
		bookingListResponseXml = bookingListResponseXml.replaceAll("ResStatus=\"OK\"", "ResStatus=\"Commit\"");
		List<TResvBase> results = new ArrayList<TResvBase>();
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		try {
			if (org.springframework.util.StringUtils.hasText(bookingListResponseXml)) {
				String bookingRSXml = VenereUtils.buildFullXmlRS(bookingListResponseXml);
				XmlOptions options = new XmlOptions();
				options.setValidateTreatLaxAsSkip();
				ArrayList<String> validationErrors = new ArrayList<String>();
				options.setErrorListener(validationErrors);
				OTAResRetrieveRSDocument booking = OTAResRetrieveRSDocument.Factory.parse(bookingRSXml, options);

				if (booking.getOTAResRetrieveRS().getSuccess() != null
						&& booking.getOTAResRetrieveRS().getReservationsList() != null) {
					ReservationsList reservationsList = booking.getOTAResRetrieveRS().getReservationsList();
					org.opentravel.ota.x2003.x05.HotelReservationType[] hotelReservationArray = reservationsList
							.getHotelReservationArray();
					if (hotelReservationArray != null) {
						for (org.opentravel.ota.x2003.x05.HotelReservationType hotelReservationType : hotelReservationArray) {
							String fogProp = "";
							String idsProp = "";
							// 外部订单号
							String resIDValue = "";
							String planDesc = "";
							try {
								TResvBase tresvbase = new TResvBase();

								resIDValue = hotelReservationType.getResGlobalInfo().getHotelReservationIDs()
										.getHotelReservationIDArray(0).getResIDValue();
								tresvbase.setOutcnfnum(resIDValue);
								String lastModifyDateTime = DateUtil.formatDateTime(hotelReservationType
										.getLastModifyDateTime().getTime());

								try {
									EResvMap eResvMap = new EResvMap();
									eResvMap.setAgodaCnfnum(resIDValue);
									eResvMap.setBk5(hotelBean.getThirdIataCode());
									List<EResvMap> eresvMapList = getEresvMapDAO().findByExample(eResvMap);
									/**
									 * 如果存在订单映射的话 修改单
									 */
									EResvMap eResvMapInfo = new EResvMap();

									if (eresvMapList != null && eresvMapList.size() > 0) {
										eResvMapInfo = eresvMapList.get(0);
										// 已经处理过，不需要再处理
										if (lastModifyDateTime.equals(eResvMapInfo.getRequesteddate())) {
											continue;
										} else {
											eResvMapInfo.setRequesteddate(lastModifyDateTime);
										}
										tresvbase.setResvtype(Constents.RESV_TYPE_EDIT);

									} else {
										/**
										 * 不存在的话保存venere订单号 映射
										 */
										eResvMapInfo.setAgodaCnfnum(resIDValue);
										eResvMapInfo.setCreatedate(new Date());
										eResvMapInfo.setRequesteddate(lastModifyDateTime);
										eResvMapInfo.setBk5(hotelBean.getThirdIataCode());
										tresvbase.setResvtype(Constents.RESV_TYPE_NEW);
									}

									getEresvMapDAO().attachDirty(eResvMapInfo);

								} catch (Exception e) {
									LOGGER.error(ExceptionUtils.getFullStackTrace(e));
									throw new BizException(e);
								}

								RoomStay[] roomStayArray = hotelReservationType.getRoomStays().getRoomStayArray();
								if (roomStayArray != null && roomStayArray.length > 0) {
									idsProp = roomStayArray[0].getBasicPropertyInfo().getHotelCode();
									fogProp = getMapService().getFogPropId(hotelBean.getThirdIataCode(), idsProp);
									if (roomStayArray.length > 1) {
										// 多房型订单，不处理！
										throw new BizException("多房型不处理！");
									}
								}
								if (!org.springframework.util.StringUtils.hasText(fogProp)) {
									throw new MappingException("酒店映射不存在！");
								}
								tresvbase.setProp(fogProp);

								// 订单状态(新单还是取消单)
								String resStatus = hotelReservationType.getResStatus();
								if (resStatus.equalsIgnoreCase("Cancel")) {
									tresvbase.setResvtype(Constents.RESV_TYPE_CANCEL);
									continue;
								}

								// 税后总价

								BigDecimal totalAmountAfterTax = hotelReservationType.getResGlobalInfo().getTotal()
										.getAmountAfterTax();
								tresvbase.setTotalrevenue(totalAmountAfterTax.doubleValue());

								// 全局货币类型

								String currencyCode = hotelReservationType.getResGlobalInfo().getTotal()
										.getCurrencyCode();
								// tresvbase.setCurrencyCode(currencyCode);

								Calendar start = hotelReservationType.getResGlobalInfo().getTimeSpan().getStart();
								tresvbase.setIndate(start.getTime());

								// 全局离店时间

								Calendar end = hotelReservationType.getResGlobalInfo().getTimeSpan().getEnd();
								tresvbase.setOutdate(end.getTime());

								long nights = DateUtil.daysBetween(start, end);
								tresvbase.setNights(new Integer(String.valueOf(nights)));

								// resvType
								TPAExtensionsType extensions = hotelReservationType.getTPAExtensions();
								String venereString = extensions.toString();
								// if (venereString != null) {
								// if (venereString.indexOf("IB") != -1) {
								// tresvbase.setResvType("IB");
								// } else {
								// if (venereString.indexOf("OR") != -1) {
								// tresvbase.setResvType("OR");
								// }
								// }
								// }

								RoomStay roomStay = roomStayArray[0];
								RoomRateType roomRate = null;

								roomRate = roomStay.getRoomRates().getRoomRateArray(0);

								// 房型
								String roomTypeCode = roomRate.getRoomTypeCode();
								String roomcode = getMapService().getRoomRateMap(roomTypeCode, fogProp,
										MappingEnum.VENERE, "roomType");
								if (!org.springframework.util.StringUtils.hasText(roomTypeCode)) {
									throw new MappingException("房型映射不存在!");
								}
								tresvbase.setRoomcode(roomcode);
								// 房量
								int rooms = roomRate.getNumberOfUnits();
								tresvbase.setRooms(rooms);
								// 计划
								String ratePlanCode = roomRate.getRatePlanCode();
								String rateClass = getMapService().getRoomRateMap(
										roomTypeCode + "#" + roomTypeCode + "-" + ratePlanCode, fogProp,
										MappingEnum.VENERE, "rateType");
								if (!org.springframework.util.StringUtils.hasText(rateClass)) {
									throw new MappingException("价格代码映射不存在!");
								}
								tresvbase.setRateClass(rateClass);
								// 税后价格
								BigDecimal amountAfterTax = roomRate.getTotal().getAmountAfterTax();

								// roomDetail.setAmountAfterTax(amountAfterTax);
								// 税前价格
								/*
								 * BigDecimal amountBeforeTax = null; try { amountBeforeTax = roomRate.getTotal()
								 * .getAmountBeforeTax(); } catch (Exception e) { xhiDetailsBean.getWarnings().add(
								 * "AmountBeforeTax税前价格不存在"); } roomDetail.setAmountBeforeTax(amountBeforeTax);
								 */
								// adults入住人数
								int adults = 0;

								adults = roomStay.getGuestCounts().getGuestCountArray(0).getCount();

								tresvbase.setAdults(adults);
								// 入住日期
								Calendar indate = null;
								// 每日房价
								String planId = FogUtil.comboPlanCode(roomcode, rateClass);
								List<RateDataBO> zRateMap = getCallFogService().getZRateMap(fogProp, start.getTime(),
										Integer.parseInt(String.valueOf(nights), 10), planId, "Website");
								// 总价
								double fogAmountAfterTax = 0d;
								List<TResvRate> resvRateList = new ArrayList<TResvRate>();
								if (zRateMap != null && zRateMap.size() > 0) {
									for (RateDataBO rateDataBO : zRateMap) {
										List<AvailAllow> rateList = rateDataBO.getRateList();
										for (AvailAllow availAllow : rateList) {
											TResvRate tResvRate = new TResvRate();
											tResvRate.setBookendate(availAllow.getAvailDate());
											fogAmountAfterTax += availAllow.getAvailRate().getDoubleRate() * rooms;
											BigDecimal rate = new BigDecimal(availAllow.getAvailRate().getDoubleRate());
											tResvRate.setProprevenue(rate.doubleValue());
											tResvRate.setPropCurrency(currencyCode);
											resvRateList.add(tResvRate);

										}
										planDesc = rateDataBO.getPlan().getDesc("zh");
									}
								}
								if (fogAmountAfterTax != amountAfterTax.doubleValue()) {
									throw new BizException("价格不一致！");
								}

								tresvbase.setResvRateList(resvRateList);

								PaymentCardType paymentCard = hotelReservationType.getResGlobalInfo().getGuarantee()
										.getGuaranteesAccepted().getGuaranteeAcceptedArray(0).getPaymentCard();

								if (paymentCard != null) {

									/*
									 * 信用卡类型 Supported values are: “VI ” - VISA “MC ” - MASTERCARD “AX ” - AMEX “JC ” -
									 * JCB “DN ” - DINERS “EC ” - EUROCARD
									 */
									String cardCode = paymentCard.getCardCode();
									tresvbase.setCctype(cardCode);
									// 有效期MMYY
									String expireDate = paymentCard.getExpireDate();
									tresvbase.setCcexpiration(expireDate);
									// 信用卡卡号
									String cardNumber = paymentCard.getCardNumber();
									tresvbase.setCcnumber(cardNumber);
									// 持卡人姓名
									String cardHolderName = paymentCard.getCardHolderName();
									tresvbase.setCcname(cardHolderName);
									// 验证码CVV2
									String seriesCode = paymentCard.getSeriesCode();
									tresvbase.setCvv(seriesCode);

								}

								CustomerType customer = hotelReservationType.getResGuests().getResGuestArray(0)
										.getProfiles().getProfileInfoArray(0).getProfile().getCustomer();

								if (customer != null) {

									// First Name
									String firstName = customer.getPersonName().getGivenNameArray(0);
									tresvbase.setGfirstname(firstName);

									// Last Name
									String lastName = customer.getPersonName().getSurname();
									tresvbase.setGlastname(lastName);
									
									//客人姓名的处理
									StringBuffer guestName = new StringBuffer();
									guestName.append(firstName).append(" ").append(lastName);
									tresvbase.setGuestname(guestName.toString());
									List<String> guestList = new ArrayList<String>();
									guestList.add(guestName.toString());
									tresvbase.setGuestList(guestList);

									// 电话
									String phoneNumber = customer.getTelephoneArray(0).getPhoneNumber();
									tresvbase.setGuestphone(phoneNumber);

									// E-mail
									String email = customer.getEmailArray(0).getStringValue();
									tresvbase.setGuestmail(email);

									// 城市
									String cityName = customer.getAddressArray(0).getCityName();
									tresvbase.setGuestcity(cityName);

									// 国家
									String countryName = customer.getAddressArray(0).getCountryName().getStringValue();
									tresvbase.setGuestcountry(countryName);

								}
								StringBuffer remarkBuffer = new StringBuffer();
								remarkBuffer.append("1、Venere ID：").append(tresvbase.getOutcnfnum()).append("；");
								remarkBuffer.append("2、特殊要求：");
								CommentType comments = hotelReservationType.getResGlobalInfo().getComments();
								if (comments != null) {
									Comment[] commentArray = comments.getCommentArray();
									if (commentArray != null) {
										for (Comment comment : commentArray) {
											try {
												String remark = comment.getTextArray(0).getStringValue();
												remarkBuffer.append(remark);
												remarkBuffer.append("；");
											} catch (Exception e) {
											}
										}
									}
								}

								remarkBuffer.append("；");
								remarkBuffer.append("3、信用卡CSC：" + StringUtils.defaultString(tresvbase.getCvv()))
										.append("；");
								remarkBuffer.append("4、计划描述：").append(planDesc).append("；");
								remarkBuffer.append("5、房费：").append(amountAfterTax).append("；");
								remarkBuffer.append("6、房费及杂费由客人前台现付(人民币)；");
								remarkBuffer.append("7、如检查到客人信用卡无效,请及时联系呼叫中心。");
								tresvbase.setRemark(remarkBuffer.toString());
								tresvbase.setIata(hotelBean.getFogIataCode());
								results.add(tresvbase);
							} catch (Exception e) {
								LOGGER.error(ExceptionUtils.getFullStackTrace(e));
								ErrorBean errorBean = new ErrorBean();
								errorBean.setErroCode("IDS-0007");
								errorBean.setErrorDesc("IDS-Alert: IDS订单信息处理，生成FOG订单信息失败。请转人工处理。");
								errorBean.setErrorMessage(e.getMessage());
								errorBean.setFogProp(fogProp);
								errorBean.setIdsProp(idsProp);
								errorBean.setIds_cnfnum(resIDValue);
								VenereDetailsBean venereDetailsBean = buildVenereDetailsBean(hotelReservationType,
										hotelBean);
								if (venereDetailsBean == null) {
									errorBean.setXml(hotelReservationType.toString());
								} else {
									errorBean.setXml(createHtmlFromFreemarker(venereDetailsBean, hotelReservationType
											.toString()));
									errorBean.setNeedFormat(false);
								}
								errorlist.add(errorBean);
								continue;
							}
						}
					}
				}
			} else {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErroCode("IDS-0003");
				errorBean.setErrorDesc("IDS-Alert: 通过IDS接口下载订单时，IDS接口返回查询失败信息。请联系系统管理员。");
				errorBean.setErrorMessage(bookingListResponseXml);
				errorBean.setFogProp("");
				errorBean.setIds_cnfnum("");
				errorBean.setXml(bookingListResponseXml);
				errorlist.add(errorBean);
				throw new BizException("通过IDS接口下载订单时，IDS接口返回查询失败信息。");
			}
		} catch (XmlException e) {
			LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
			throw new BizException(e.getMessage());
		} catch (BizException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
			throw new BizException(e.getMessage());
		}

		return results;
	}

	protected String createHtmlFromFreemarker(VenereDetailsBean detailsBean, String defaultString) throws IOException,
			TemplateException {
		try {
			String ftl = "venereFormat.ftl";

			Configuration cfg = new Configuration();
			String ftlPath = VenereDownloadService.class.getResource("/").getPath();
			File file = new File(ftlPath + "template/");
			cfg.setDirectoryForTemplateLoading(file);
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			/* ------------------------------------------------------------------- */
			/* You usually do these for many times in the application life-cycle: */

			/* Get or create a template */
			Template temp = cfg.getTemplate(ftl);

			/* Create a data-model */
			Map root = new HashMap();
			Calendar start = detailsBean.getStart();
			Date time = start.getTime();
			start = Calendar.getInstance();
			start.setTime(time);

			List<VenereMailBean> mailcontent = new ArrayList<VenereMailBean>();

			List<VenereRoomDetail> roomDetails = detailsBean.getRoomDetails();
			for (int i = 0; i < detailsBean.getNights(); i++) {
				VenereMailBean thirdMailBean = new VenereMailBean();
				mailcontent.add(thirdMailBean);
				String date = DateUtil.formatDate(start.getTime());
				thirdMailBean.setDate(date);
				List<BigDecimal> dayRateList = thirdMailBean.getDayRateList();
				for (VenereRoomDetail xhiRoomDetail : roomDetails) {
					List<VenereRoomRate> rates = xhiRoomDetail.getRates();
					for (VenereRoomRate xhiRoomRate : rates) {
						if (DateUtils.isSameDay(xhiRoomRate.getDate(), start.getTime())) {
							BigDecimal rate = xhiRoomRate.getMailRate();
							dayRateList.add(rate);
							break;
						}
					}
				}
				double dayTotal = 0d;
				for (BigDecimal rate : dayRateList) {
					dayTotal += rate.doubleValue();
				}
				thirdMailBean.setDayTotal(dayTotal);
				start.add(Calendar.DATE, 1);
			}

			double fogTotal = 0d;
			for (VenereMailBean thirdMailBean : mailcontent) {
				fogTotal += thirdMailBean.getDayTotal();
			}

			root.put("detailsBean", detailsBean);
			root.put("mailcontent", mailcontent);
			root.put("fogTotal", fogTotal);
			int size = detailsBean.getRoomDetails().size();
			root.put("mutliRooms", size + 1);

			/* Merge data-model with template */
			StringWriter out = new StringWriter();
			temp.process(root, out);
			return out.toString();
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return defaultString;
		}
	}

	private VenereDetailsBean buildVenereDetailsBean(HotelReservationType hotelReservationType, HotelBean hotelBean) {

		try {
			VenereDetailsBean xhiDetailsBean = new VenereDetailsBean();

			// 订单状态(新单还是取消单)
			String resStatus = null;
			try {
				resStatus = hotelReservationType.getResStatus();
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("ResStatus订单状态解析错误");
			}
			xhiDetailsBean.setResStatus(resStatus);
			// 修改时间
			try {
				Calendar lastModifyDateTime = hotelReservationType.getLastModifyDateTime();
				xhiDetailsBean.setLastModifyDateTime(lastModifyDateTime);
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("lastModifyDateTime最后修改时间解析错误");
			}
			// 外部订单号
			String resIDValue = null;
			try {
				resIDValue = hotelReservationType.getResGlobalInfo().getHotelReservationIDs()
						.getHotelReservationIDArray(0).getResIDValue();
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("ResIDValue订单号解析错误");
			}
			xhiDetailsBean.setResIDValue(resIDValue);
			// 税后总价
			try {
				BigDecimal totalAmountAfterTax = hotelReservationType.getResGlobalInfo().getTotal().getAmountAfterTax();
				xhiDetailsBean.setTotalAmountAfterTax(totalAmountAfterTax);
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("ResGlobalInfo.Total.AmountAfterTax税后总价解析错误");
			}
			// 全局货币类型
			try {
				String currencyCode = hotelReservationType.getResGlobalInfo().getTotal().getCurrencyCode();
				xhiDetailsBean.setCurrencyCode(currencyCode);
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("GlobalInfo.Total.CurrencyCode货币类型解析错误");
			}
			try {
				Calendar start = hotelReservationType.getResGlobalInfo().getTimeSpan().getStart();
				xhiDetailsBean.setStart(start);
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("ResGlobalInfo.TimeSpan.Start全局入住时间解析错误");
			}
			// 全局离店时间
			try {
				Calendar end = hotelReservationType.getResGlobalInfo().getTimeSpan().getEnd();
				xhiDetailsBean.setEnd(end);
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("ResGlobalInfo.TimeSpan.End全局离店时间解析错误");
			}
			// resvType
			TPAExtensionsType extensions = hotelReservationType.getTPAExtensions();
			String venereString = extensions.toString();
			if (venereString != null) {
				if (venereString.indexOf("IB") != -1) {
					xhiDetailsBean.setResvType("IB");
				} else {
					if (venereString.indexOf("OR") != -1) {
						xhiDetailsBean.setResvType("OR");
					}
				}
			}
			RoomStay[] roomStayArray = null;
			try {
				roomStayArray = hotelReservationType.getRoomStays().getRoomStayArray();
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("RoomStays入住详细解析错误");
			}
			if (roomStayArray != null) {
				if (roomStayArray.length > 1) {
					xhiDetailsBean.setMultRoom(true);
				}

				for (RoomStay roomStay : roomStayArray) {
					RoomRateType roomRate = null;
					try {
						roomRate = roomStay.getRoomRates().getRoomRateArray(0);
					} catch (Exception e) {
						xhiDetailsBean.getErrors().add("roomRate入住详细解析错误");
					}
					if (roomRate != null) {
						VenereRoomDetail roomDetail = new VenereRoomDetail();

						// 房型
						String roomTypeCode = roomRate.getRoomTypeCode();
						roomDetail.setRoomTypeCode(roomTypeCode);
						// 房量
						int rooms = roomRate.getNumberOfUnits();
						roomDetail.setRooms(rooms);
						// 计划
						String ratePlanCode = roomRate.getRatePlanCode();
						roomDetail.setRatePlanCode(ratePlanCode);
						// 税后价格
						BigDecimal amountAfterTax = null;
						try {
							amountAfterTax = roomRate.getTotal().getAmountAfterTax();
						} catch (Exception e) {
							xhiDetailsBean.getErrors().add("AmountAfterTax税后价格不存在");
						}
						roomDetail.setAmountAfterTax(amountAfterTax);
						// 税前价格
						/*
						 * BigDecimal amountBeforeTax = null; try { amountBeforeTax = roomRate.getTotal()
						 * .getAmountBeforeTax(); } catch (Exception e) { xhiDetailsBean.getWarnings().add(
						 * "AmountBeforeTax税前价格不存在"); } roomDetail.setAmountBeforeTax(amountBeforeTax);
						 */
						// adults入住人数
						int adults = 0;
						try {
							adults = roomStay.getGuestCounts().getGuestCountArray(0).getCount();
						} catch (Exception e) {
							xhiDetailsBean.getErrors().add("GuestCounts.Count入住人数解析错误");
						}
						roomDetail.setAdults(adults);
						// 入住日期
						Calendar indate = null;
						try {
							indate = roomStay.getTimeSpan().getStart();
						} catch (Exception e) {
							xhiDetailsBean.getErrors().add("roomStay.TimeSpan.Start入住时间解析错误");
						}
						roomDetail.setIndate(indate);
						// 离店日期
						Calendar outdate = null;
						try {
							outdate = roomStay.getTimeSpan().getEnd();
						} catch (Exception e) {
							xhiDetailsBean.getErrors().add("roomStay.TimeSpan.End离店时间解析错误");
						}
						roomDetail.setOutdate(outdate);
						// 房型描述
						try {
							roomDetail.setRoomDesc(roomStay.getRoomTypes().getRoomTypeArray(0).getRoomDescription()
									.getName());
						} catch (Exception e) {
							xhiDetailsBean.getErrors().add("RoomDescription房型描述解析错误");
						}
						// 酒店ID
						String hotelCode = null;
						try {
							hotelCode = roomStay.getBasicPropertyInfo().getHotelCode();
						} catch (Exception e) {
							xhiDetailsBean.getErrors().add("hotelCode酒店Code解析错误");
						}
						roomDetail.setHotelCode(hotelCode);
						// 根级的hotelCode
						xhiDetailsBean.setHotelCode(hotelCode);

						String propId = getMapService().getFogPropId(hotelBean.getThirdIataCode(), hotelCode);
						// propid
						xhiDetailsBean.setPropId(propId);

						xhiDetailsBean.getRoomDetails().add(roomDetail);
					}
				}
			}

			PaymentCardType paymentCard = null;
			try {
				paymentCard = hotelReservationType.getResGlobalInfo().getGuarantee().getGuaranteesAccepted()
						.getGuaranteeAcceptedArray(0).getPaymentCard();
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("paymentCard信用卡信息解析错误");
			}
			if (paymentCard != null) {
				VenereCreditCardDetail creditCardDetail = xhiDetailsBean.getCreditCardDetail();

				/*
				 * 信用卡类型 Supported values are: “VI ” - VISA “MC ” - MASTERCARD “AX ” - AMEX “JC ” - JCB “DN ” - DINERS
				 * “EC ” - EUROCARD
				 */
				String cardCode = paymentCard.getCardCode();
				creditCardDetail.setCardCode(cardCode);
				// 有效期MMYY
				String expireDate = paymentCard.getExpireDate();
				creditCardDetail.setExpireDate(expireDate);
				// 信用卡卡号
				String maskedCardNumber = paymentCard.getMaskedCardNumber();
				creditCardDetail.setMaskedCardNumber(maskedCardNumber);
				String cardNumber = paymentCard.getCardNumber();
				creditCardDetail.setCardNumber(cardNumber);
				// 持卡人姓名
				String cardHolderName = paymentCard.getCardHolderName();
				creditCardDetail.setCardHolderName(cardHolderName);
				// 验证码CVV2
				String seriesCode = paymentCard.getSeriesCode();
				creditCardDetail.setSeriesCode(seriesCode);

			}

			CustomerType customer = null;
			try {
				customer = hotelReservationType.getResGuests().getResGuestArray(0).getProfiles().getProfileInfoArray(0)
						.getProfile().getCustomer();
			} catch (Exception e) {
				xhiDetailsBean.getErrors().add("Customer客户信息解析错误");
			}
			if (customer != null) {
				VenerePersonDetail personDetail = xhiDetailsBean.getPersonDetail();

				try {
					// First Name
					String firstName = customer.getPersonName().getGivenNameArray(0);
					personDetail.setFirstName(firstName);
				} catch (Exception e) {
					xhiDetailsBean.getWarnings().add("GivenName FirstName不存在");
				}
				try {
					// Last Name
					String lastName = customer.getPersonName().getSurname();
					personDetail.setLastName(lastName);
				} catch (Exception e) {
					xhiDetailsBean.getWarnings().add("Surname LastName不存在");
				}
				try {
					// 电话
					String phoneNumber = customer.getTelephoneArray(0).getPhoneNumber();
					personDetail.setPhoneNumber(phoneNumber);
				} catch (Exception e) {
					xhiDetailsBean.getWarnings().add("PhoneNumber电话号码不存在");
				}
				try {
					// E-mail
					String email = customer.getEmailArray(0).getStringValue();
					personDetail.setEmail(email);
				} catch (Exception e) {
					xhiDetailsBean.getWarnings().add("Email不存在");
				}
				try {
					// 城市
					String cityName = customer.getAddressArray(0).getCityName();
					personDetail.setCityName(cityName);
				} catch (Exception e) {
					xhiDetailsBean.getWarnings().add("CityName不存在");
				}
				try {
					// 国家
					String countryName = customer.getAddressArray(0).getCountryName().getStringValue();
					personDetail.setCountryName(countryName);
				} catch (Exception e) {
					xhiDetailsBean.getWarnings().add("CountryName不存在");
				}
			}
			Comment[] commentArray = null;
			try {
				commentArray = hotelReservationType.getResGlobalInfo().getComments().getCommentArray();
			} catch (Exception e) {
				xhiDetailsBean.getWarnings().add("Remark不存在");
			}
			if (commentArray != null) {
				List<String> remarks = xhiDetailsBean.getRemarks();
				for (Comment comment : commentArray) {
					try {
						String remark = comment.getTextArray(0).getStringValue();
						remarks.add(remark);
					} catch (Exception e) {
					}
				}
			}
			return xhiDetailsBean;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return null;
		}
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

	/**
	 * @return the timeZone
	 */
	public int getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the preTime
	 */
	public int getPreTime() {
		return preTime;
	}

	/**
	 * @param preTime
	 *            the preTime to set
	 */
	public void setPreTime(int preTime) {
		this.preTime = preTime;
	}

}
