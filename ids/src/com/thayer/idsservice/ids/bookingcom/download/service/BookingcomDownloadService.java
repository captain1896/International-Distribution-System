/*****************************************************************<br>
 * <B>FILE :</B> BookingcomDownloadService.java <br>
 * <B>CREATE DATE :</B> 2010-12-3 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.bookingcom.download.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import com.booking.eqc.br.rq.RequestDocument;
import com.booking.eqc.br.rs.ReservationsDocument;
import com.booking.eqc.br.rs.CustomerDocument.Customer;
import com.booking.eqc.br.rs.PriceDocument.Price;
import com.booking.eqc.br.rs.ReservationDocument.Reservation;
import com.booking.eqc.br.rs.ReservationsDocument.Reservations;
import com.booking.eqc.br.rs.RoomDocument.Room;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.bookingcom.beans.BookingComResv;
import com.thayer.idsservice.ids.bookingcom.beans.BookingComRoom;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.task.download.interf.AbstractDownLoadService;
import com.thayer.idsservice.util.DateUtil;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-12-3<br>
 * @version : v1.0
 */
public class BookingcomDownloadService extends AbstractDownLoadService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6972132948625988978L;
	private String bookingcomUserId;
	private String bookingcomUserPwd;
	private static Logger LOGGER = Logger.getLogger(BookingcomDownloadService.class);
	private transient VelocityEngine velocityEngine;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.AbstractDownLoadService#getBookingListRequestXml(com.thayer.idsservice
	 * .task.download.bean.HotelBean)
	 */
	@Override
	public String getBookingListRequestXml(HotelBean hotelBean) throws BizException {
		XmlOptions opt = new XmlOptions();
		opt.setUseDefaultNamespace();
		opt.setSavePrettyPrint();
		RequestDocument brRqDoc = RequestDocument.Factory.newInstance();
		RequestDocument.Request rq = brRqDoc.addNewRequest();
		rq.setUsername(bookingcomUserId);
		rq.setPassword(bookingcomUserPwd);
		String rqXml = brRqDoc.xmlText(opt);
		LOGGER.info("getBookingListRequestXml : \n" + rqXml);
		return rqXml;
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.thayer.idsservice.task.download.interf.AbstractDownLoadService#handleResponseXml4fogbeanList(com.thayer.
	 * idsservice.task.download.bean.HotelBean, java.lang.String)
	 */
	@Override
	public List<TResvBase> handleResponseXml4fogbeanList(HotelBean hotelBean, String bookingListResponseXml,
			ResultBean resultBean) throws BizException {
		LOGGER.info("handleResponseXml4fogbeanList :\n" + bookingListResponseXml);
		List<TResvBase> results = new ArrayList<TResvBase>();
		List<ErrorBean> errorList = resultBean.getErrorlist();

		if (StringUtils.hasText(bookingListResponseXml) && !bookingListResponseXml.contains("<error>")) {
			String xmlStr = bookingListResponseXml.replaceAll("&", "");
			ReservationsDocument rsDoc;
			try {
				rsDoc = ReservationsDocument.Factory.parse(xmlStr);
			} catch (XmlException e1) {
				throw new BizException("bookingListResponseXml return false xml:" + bookingListResponseXml);
			}
			Reservations bookings = rsDoc.getReservations();
			if (bookings != null) {
				Reservation[] bookDtails = bookings.getReservationArray();
				for (Reservation booking : bookDtails) {
					try {
						TResvBase tresvbase = new TResvBase();
						String bookType = booking.getStatus();
						String expBookId = booking.getId();
						String hotelId = booking.getHotelId();
						String fogPropId = getMapService().getFogPropId(hotelBean.getThirdIataCode(), hotelId);
						tresvbase.setProp(fogPropId);
						if ("new".equals(bookType)) {
							tresvbase.setResvtype("n");

						} else if ("modified".equals(bookType)) {
							tresvbase.setResvtype("e");

						} else if ("cancelled".equals(bookType)) {
							tresvbase.setResvtype("c");
							tresvbase.setOutcnfnum(expBookId);
							results.add(tresvbase);
							LOGGER.info("订单类型是取消单！！！");
							continue;
						} else {
							ErrorBean errorBean = new ErrorBean();
							errorBean.setErroCode("IDS-0004");
							errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0004"));
							errorBean.setErrorMessage("没有匹配的订单类型!");
							errorBean.setXml(formatResvInfo(booking));
							errorBean.setNeedFormat(false);
							errorBean.setIdsProp(booking.getHotelId());
							errorBean.setIds_cnfnum(booking.getId());
							errorList.add(errorBean);
							LOGGER.error("没有匹配的订单类型!");
							continue;
						}

						if (!StringUtils.hasText(fogPropId)) {
							ErrorBean errorBean = new ErrorBean();
							errorBean.setErroCode("IDS-0014");
							errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0014"));
							errorBean.setErrorMessage("没有匹配的酒店ID号!");
							errorBean.setXml(formatResvInfo(booking));
							errorBean.setNeedFormat(false);
							errorBean.setIdsProp(booking.getHotelId());
							errorBean.setIds_cnfnum(booking.getId());
							errorList.add(errorBean);
							LOGGER.error("没有匹配的酒店ID号!");
							continue;
						}

						String ratePlan = "";
						String fogRoomType = "";
						String fogPlanId = "";
						Room room = null;
						Room[] roomStays = booking.getRoomArray();
						String checkRoomType = null;
						String checkRateType = null;
						String checkRoomIn = null;
						String checkRoomOut = null;
						// check same room type.
						if (roomStays != null && roomStays.length > 1) {
							
							//判断是否多房型
							room= roomStays[0];
							String tmpRoomId=room.getId();
							for (Room roomStay : roomStays) {
								if (!tmpRoomId.equals(roomStay.getId())){
									//throw new BizException();
									tresvbase.setMultiRoomType(true);
								}
							}
							
							for (Room roomStay : roomStays) {
								if (!StringUtils.hasText(checkRoomIn)) {
									checkRoomIn = roomStay.getArrivalDate();
								} else {
									if (!checkRoomIn.equals(roomStay.getArrivalDate())) {
										LOGGER.warn("The room Check in is different. Throw BizException. ");
										throw new BizException("The room Check in is different.");
									}
								}
								if (!StringUtils.hasText(checkRoomOut)) {
									checkRoomOut = roomStay.getDepartureDate();
								} else {
									if (!checkRoomOut.equals(roomStay.getDepartureDate())) {
										LOGGER.warn("The room Check Out is different. Throw BizException. ");
										throw new BizException("The room Check Out is different.");
									}
								}

								if (StringUtils.hasText(checkRoomType)) {
									if (!checkRoomType.equals(roomStay.getId())) {
										LOGGER.warn("The roomType is different. Throw BizException. ");
										throw new BizException("The roomType is different.");
									}

									Price[] prices = roomStay.getPriceArray();
									if (prices == null || prices.length == 0) {
										LOGGER.error("The room Type :" + roomStay.getId() + " has not price! Throw BizException. ");
										throw new BizException("The room Type :" + roomStay.getId() + " has not price!");
									}
									if (StringUtils.hasText(checkRateType)) {
										if (!checkRateType.equals(roomStay.getPriceArray()[0].getRateId())) {
											LOGGER.error("The rateType is different. Throw BizException. ");
											throw new BizException("The rateType is different.");
										}
									} else {
										checkRateType = roomStay.getPriceArray()[0].getRateId();
									}

								} else {
									checkRoomType = roomStay.getId();
								}
							}
						}
						
						room = roomStays[0];
						String roomType = room.getId();
						Price[] prices = room.getPriceArray();
						ratePlan = prices[0].getRateId();
						fogRoomType = getMapService().getRoomRateMap(roomType, fogPropId, MappingEnum.BOOKINGCOM,
								"roomType");
						LOGGER.info("roomType:" + roomType + ",ratePlan:" + ratePlan);
						fogPlanId = getMapService().getRoomRateMap(roomType + "#" + ratePlan, fogPropId,
								MappingEnum.BOOKINGCOM, "rateType");
						LOGGER.info("fogRoomType:" + fogRoomType);
						LOGGER.info("fogPlanId:" + fogPlanId);

						if (!StringUtils.hasText(fogRoomType)) {
							ErrorBean errorBean = new ErrorBean();
							errorBean.setErroCode("IDS-0014");
							errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0014"));
							errorBean.setErrorMessage("没有匹配的房型代码!");
							errorBean.setXml(formatResvInfo(booking));
							errorBean.setNeedFormat(false);
							errorBean.setIdsProp(booking.getHotelId());
							errorBean.setIds_cnfnum(booking.getId());
							errorList.add(errorBean);
							LOGGER.error("没有匹配的房型代码!");
							continue;
						}
						if (!StringUtils.hasText(fogPlanId)) {
							ErrorBean errorBean = new ErrorBean();
							errorBean.setErroCode("IDS-0014");
							errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0014"));
							errorBean.setErrorMessage("没有匹配的计划代码!");
							errorBean.setXml(formatResvInfo(booking));
							errorBean.setNeedFormat(false);
							errorBean.setIdsProp(booking.getHotelId());
							errorBean.setIds_cnfnum(booking.getId());
							errorList.add(errorBean);
							LOGGER.error("没有匹配的计划代码!");
							continue;
						}
						
						//设置客人名称，注意和联系人的区别
						if(roomStays != null && roomStays.length != 0){
							Set<String> set = new LinkedHashSet<String>();
							for (Room roomStay : roomStays) {
								if(StringUtils.hasText(roomStay.getGuestName())){
									set.add(roomStay.getGuestName());
								}
							}
							if(StringUtils.hasText(set.toString())){
								tresvbase.setGuestList(new ArrayList<String>(set));
								tresvbase.setGuestname(set.toString().replace("[", "").replace("]", ""));
							}
						}
						
						tresvbase.setIata(hotelBean.getFogIataCode());
						tresvbase.setBookeddate(new Date());

						tresvbase.setOutcnfnum(expBookId);
						tresvbase.setIndate(DateUtil.dateValue(room.getArrivalDate()));
						tresvbase.setOutdate(DateUtil.dateValue(room.getDepartureDate()));
						tresvbase.setNights(DateUtil.dateDiff(DateUtil.TIME_UNIT_D, tresvbase.getIndate(), tresvbase
								.getOutdate()));
						tresvbase.setRooms(roomStays.length);
						tresvbase.setAdults(Integer.parseInt(room.getNumberofguests(), 10));
						tresvbase.setChildren(0);
						Customer guest = booking.getCustomer();
						tresvbase.setRoomcode(fogRoomType);
						tresvbase.setRateClass(fogPlanId);
						if (guest != null) {
							// 客户信息
							tresvbase.setGfirstname(guest.getFirstName());
							tresvbase.setGlastname(guest.getLastName());
							tresvbase.setGuestaddress(guest.getAddress());
							tresvbase.setGuestmail(guest.getEmail());
							tresvbase.setGuestmobile(guest.getTelephone());
							// 信用卡信息
							tresvbase.setCvv(guest.getCcCvc());
							tresvbase.setCcnumber(guest.getCcNumber());
							tresvbase.setCcexpiration(guest.getCcExpirationDate());
							tresvbase.setCctype(guest.getCcType());
							tresvbase.setCcname(guest.getCcName());
						}

						// remarks
						StringBuilder remarks = new StringBuilder();

						String somking = room.getSmoking();
						int index = 0;
						if (guest != null) {
							index++;
							remarks.append(index + ".Booker name:" + guest.getFirstName() + " " + guest.getLastName());
						}
						index++;
						remarks.append(index + ".bookingcom Booking ID:" + expBookId);
						if (somking != null && !somking.equals("")) {

							index++;
							somking = somking.trim().equals("1") ? "\u5438\u70df\u623f" : "\u65e0\u70df\u623f";
							remarks.append(index + ".\u7279\u6b8a\u8981\u6c42Special Request:");

							remarks.append(somking);

						}

						String taxesBrft = room.getInfo();
						if (taxesBrft != null && taxesBrft.length() > 0) {
							int s1 = taxesBrft.indexOf("Children and extra bed policy");
							if (s1 > 0) {
								index++;
								remarks.append(index + "." + taxesBrft.substring(0, s1));
							}
						}

						String roomRemark = room.getRemarks();
						if (roomRemark != null && !roomRemark.equals("")) {
							index++;
							remarks.append(index + ".room remarks:");

							remarks.append(roomRemark);

						}

						String customerRemark = booking.getCustomer().getRemarks();
						if (customerRemark != null && !customerRemark.equals("")) {
							index++;
							remarks.append(index + ".customer remarks:");

							remarks.append(customerRemark);

						}

						// 3.客人已提供信用卡做担保，请酒店在收到预订后马上进行验证并为客人保留房间
						index++;
						remarks
								.append(index
										+ ".\u5BA2\u4EBA\u5DF2\u63D0\u4F9B\u4FE1\u7528\u5361\uFF0C\u8BF7\u9152\u5E97\u5728\u6536\u5230\u9884\u8BA2\u540E\u9A6C\u4E0A\u8FDB\u884C\u5904\u7406\u5E76\u4E3A\u5BA2\u4EBA\u4FDD\u7559\u623F\u95F4");
						// 4.所有费用客人自付前台，显示为美金，按入住当日汇率换算
						// index++;
						// remarks
						// .addRemark(index
						// + ".\u6240\u6709\u8d39\u7528\u5ba2\u4eba\u81ea\u4ed8\u524d\u53f0");
						// 5.信用卡有效期: <cc_expiration_date>, CVS: <cc_cvs>
						if (guest != null) {
							index++;
							remarks.append(index + ".\u4fe1\u7528\u5361\u6709\u6548\u671f:"
									+ guest.getCcExpirationDate() + ",CVS:" + guest.getCcCvc());
						}
						// 6. 实际到店时间：不限
						index++;
						remarks.append(index + ".\u5b9e\u9645\u5230\u5e97\u65f6\u95f4\uff1a\u4e0d\u9650");

						tresvbase.setRemark(remarks.toString());

						// bookedrates
						List<TResvRate> resvRateList = new ArrayList<TResvRate>();
						tresvbase.setResvRateList(resvRateList);

						com.booking.eqc.br.rs.PriceDocument.Price[] perDayRates = room.getPriceArray();
						if (perDayRates != null) {
							for (int p = 0; p < perDayRates.length; p++) {
								TResvRate rate = new TResvRate();
								com.booking.eqc.br.rs.PriceDocument.Price perRate = perDayRates[p];
								rate.setBookendate(DateUtil.dateValue(perRate.getDate()));
								rate.setProprevenue(Double.valueOf(perRate.getStringValue()));
								rate.setPropCurrency(booking.getCurrencycode());
								resvRateList.add(rate);
							}
						}

						List<EResvMap> eresvMapList = eresvMapDAO.findByAgodaCnfnum(expBookId);//通过外部订单号查找
						if(eresvMapList != null && !eresvMapList.isEmpty()){
							EResvMap resv = eresvMapList.get(0);
							tresvbase.setOrderMsgType(resv.getBk6());
						}
						
						//如果是修改单，和以前的单子的价格做比较 如果不相同的话 发邮件转人工处理
						if("e".equals(tresvbase.getResvtype())){
							if(eresvMapList != null && !eresvMapList.isEmpty()){
								EResvMap resv = eresvMapList.get(0);
								tresvbase.setOrderMsgType(resv.getBk6());
								String priceInfo = resv.getPriceInfo().trim();
								//拼接当前订单的价格信息
								String curPriceInfo = super.getPriceInfoByTResvRate(tresvbase);
								if(!priceInfo.equals(curPriceInfo)){//如果两个价格信息不相等 就发邮件转人工处理
									ErrorBean errorBean = new ErrorBean();
									errorBean.setErroCode("IDS-0017");
									errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0017"));
									errorBean.setErrorMessage("IDS订单修改单和原订单价格不一致!");
									errorBean.setXml(formatResvInfo(booking));
									errorBean.setNeedFormat(false);
									errorBean.setIdsProp(booking.getHotelId());
									errorBean.setIds_cnfnum(booking.getId());
									errorBean.setFogProp(tresvbase.getProp());
									errorBean.settResvBase(tresvbase);
									errorList.add(errorBean);
									LOGGER.warn("修改单信息与以前订单价格信息不匹配!");
									continue;
								}
								
							}
							
						}
						results.add(tresvbase);

					} catch (Exception e) {
						ErrorBean errorBean = new ErrorBean();
						errorBean.setErroCode("IDS-0004");
						errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0004"));
						errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
						errorBean.setXml(formatResvInfo(booking));
						errorBean.setNeedFormat(false);
						errorBean.setIdsProp(booking.getHotelId());
						errorBean.setIds_cnfnum(booking.getId());
						errorList.add(errorBean);
						LOGGER.error(ExceptionUtils.getFullStackTrace(e));
					}
					
					
					
				}
			}
			return results;
		} else {
			throw new BizException("bookingListResponseXml return false xml:" + bookingListResponseXml);
		}

	}

	/**
	 * @return the bookingcomUserId
	 */
	public String getBookingcomUserId() {
		return bookingcomUserId;
	}

	/**
	 * @param bookingcomUserId
	 *            the bookingcomUserId to set
	 */
	public void setBookingcomUserId(String bookingcomUserId) {
		this.bookingcomUserId = bookingcomUserId;
	}

	/**
	 * @return the bookingcomUserPwd
	 */
	public String getBookingcomUserPwd() {
		return bookingcomUserPwd;
	}

	/**
	 * @param bookingcomUserPwd
	 *            the bookingcomUserPwd to set
	 */
	public void setBookingcomUserPwd(String bookingcomUserPwd) {
		this.bookingcomUserPwd = bookingcomUserPwd;
	}

	protected String formatResvInfo(com.booking.eqc.br.rs.ReservationDocument.Reservation booking) {
		BookingComResv bookingComResv = new BookingComResv();
		String out = null;
		try {
			setBookingComResv(bookingComResv, booking);
			String vm = "bookingcomFormat.vm";
			Map model = new HashMap();
			model.put("ObjectTO", bookingComResv);
			out = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vm, model);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			out = booking.toString();
		}
		return out;
	}

	protected void setBookingComResv(BookingComResv bookingComResv,
			com.booking.eqc.br.rs.ReservationDocument.Reservation booking) {
		bookingComResv.setReservation_number(booking.getId());
		// String createDate = booking.getDate();
		bookingComResv.setBooking_date(DateUtil.formatDateTime(new Date()));
		bookingComResv.setHotelnumber_name(booking.getHotelId() + "-" + booking.getHotelName());
		com.booking.eqc.br.rs.CustomerDocument.Customer customer = booking.getCustomer();
		if (customer != null) {
			bookingComResv.setBooker_name(customer.getFirstName() + " " + customer.getLastName());
			bookingComResv.setE_mail(customer.getEmail());
			bookingComResv.setStreet(customer.getAddress());
			bookingComResv.setCity(customer.getCity());
			bookingComResv.setCountry(customer.getCountrycode());
			bookingComResv.setTelephone(customer.getTelephone());
			bookingComResv.setLanguage("");
			bookingComResv.setGuest_remarks(customer.getRemarks());
			bookingComResv.setCc_type(customer.getCcType());
			bookingComResv.setCc_number(customer.getCcNumber());
			bookingComResv.setCc_name(customer.getCcName());
			bookingComResv.setCc_cvc(customer.getCcCvc());
			bookingComResv.setCc_expiration_date(customer.getCcExpirationDate());
			bookingComResv.setDc_issue_number(customer.getDcIssueNumber());
			bookingComResv.setDc_start_date(customer.getDcStartDate());
		}
		bookingComResv.setTotal_number_of_rooms(booking.getRoomArray().length + "");

		bookingComResv.setTotal_costs_all_rooms(booking.getCurrencycode() + " " + booking.getTotalprice());
		bookingComResv.setTotal_commission(booking.getCurrencycode() + " " + booking.getCommissionamount());
		int order = 0;
		int guestCount = 0;
		com.booking.eqc.br.rs.RoomDocument.Room[] rooms = booking.getRoomArray();
		Collection roomCol = new ArrayList();
		for (com.booking.eqc.br.rs.RoomDocument.Room room : rooms) {
			order++;
			BookingComRoom bookingComRoom = new BookingComRoom();
			bookingComRoom.setOrder(order + "");
			bookingComRoom.setGuest_name(room.getGuestName());
			bookingComRoom.setRoom_type(room.getId() + "-" + room.getName());
			bookingComRoom.setNumber_of_persons(room.getNumberofguests());
			bookingComRoom.setArrival(room.getArrivalDate());
			bookingComRoom.setDeparture(room.getDepartureDate());
			bookingComResv.setArriveDate(room.getArrivalDate());
			Date arrvDate = DateUtil.dateValue(room.getArrivalDate());
			Date deparDate = DateUtil.dateValue(room.getDepartureDate());

			try {
				int nights = DateUtil.dateDiff(DateUtil.TIME_UNIT_D, arrvDate, deparDate);
				bookingComRoom.setNumber_of_nights(nights + "");
				bookingComRoom.setTotal_costs(room.getCurrencycode() + " " + room.getTotalprice());
				bookingComRoom.setCosts_per_night((NumberUtils.toDouble(room.getTotalprice()) / nights) + "");
				bookingComRoom.setStatus(booking.getStatus());
				String somk = room.getSmoking();
				if (StringUtils.hasText(somk)) {
					somk = somk.trim().equals("1") ? "\u6709\u70DF\u623F" : "\u65e0\u70df\u623f";
				} else {
					somk = "\u65E0\u8981\u6C42";
				}

				bookingComRoom.setSmoking_preference(somk);
				bookingComRoom.setGuest_remarks(room.getRemarks());
			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			}

			guestCount = guestCount + NumberUtils.toInt(room.getNumberofguests());
			roomCol.add(bookingComRoom);

		}
		bookingComResv.setResvRooms(roomCol);
		bookingComResv.setTotal_number_of_guests(guestCount + "");

	}

	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine
	 *            the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	public static void main(String[] args) {
//		Set<String> set = new LinkedHashSet<String>();
//		set.add("aaa");
//		set.add("bbb");
//		set.add("ccc");
//		set.add("a");
//		System.out.println(new ArrayList<String>(set));
//		System.out.println(set.toString().replace("[", "").replace("]", ""));
		
 	}
}
