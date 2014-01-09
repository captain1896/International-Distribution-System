package com.thayer.idsservice.ids.genares.download.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.hubs1.ids.genareshotelgetmsgrq.OTAHotelGetMsgRQDocument;
import net.hubs1.ids.genareshotelgetmsgrq.MessageDocument.Message;
import net.hubs1.ids.genareshotelgetmsgrq.MessagesDocument.Messages;
import net.hubs1.ids.genareshotelgetmsgrq.OTAHotelGetMsgRQDocument.OTAHotelGetMsgRQ;
import net.hubs1.ids.genareshotelgetmsgrs.OTAHotelGetMsgRSDocument;
import net.hubs1.ids.genareshotelgetmsgrs.AddressInfoDocument.AddressInfo;
import net.hubs1.ids.genareshotelgetmsgrs.BookingChannelDocument.BookingChannel;
import net.hubs1.ids.genareshotelgetmsgrs.CountryNameDocument.CountryName;
import net.hubs1.ids.genareshotelgetmsgrs.GuestCountDocument.GuestCount;
import net.hubs1.ids.genareshotelgetmsgrs.GuestCountsDocument.GuestCounts;
import net.hubs1.ids.genareshotelgetmsgrs.HotelReservationDocument.HotelReservation;
import net.hubs1.ids.genareshotelgetmsgrs.HotelReservationsDocument.HotelReservations;
import net.hubs1.ids.genareshotelgetmsgrs.MessageContentDocument.MessageContent;
import net.hubs1.ids.genareshotelgetmsgrs.OTAHotelGetMsgRSDocument.OTAHotelGetMsgRS;
import net.hubs1.ids.genareshotelgetmsgrs.OTAHotelResNotifRQDocument.OTAHotelResNotifRQ;
import net.hubs1.ids.genareshotelgetmsgrs.POSDocument.POS;
import net.hubs1.ids.genareshotelgetmsgrs.PaymentCardDocument.PaymentCard;
import net.hubs1.ids.genareshotelgetmsgrs.PersonNameDocument.PersonName;
import net.hubs1.ids.genareshotelgetmsgrs.ProfileDocument.Profile;
import net.hubs1.ids.genareshotelgetmsgrs.ProfileInfoDocument.ProfileInfo;
import net.hubs1.ids.genareshotelgetmsgrs.RateDocument.Rate;
import net.hubs1.ids.genareshotelgetmsgrs.RatePlanDocument.RatePlan;
import net.hubs1.ids.genareshotelgetmsgrs.RatePlansDocument.RatePlans;
import net.hubs1.ids.genareshotelgetmsgrs.RoomStayDocument.RoomStay;
import net.hubs1.ids.genareshotelgetmsgrs.RoomStaysDocument.RoomStays;
import net.hubs1.ids.genareshotelgetmsgrs.RoomTypeDocument.RoomType;
import net.hubs1.ids.genareshotelgetmsgrs.RoomTypesDocument.RoomTypes;
import net.hubs1.ids.genareshotelgetmsgrs.SourceDocument.Source;
import net.hubs1.ids.genareshotelgetmsgrs.StateProvDocument.StateProv;
import net.hubs1.ids.genareshotelgetmsgrs.TelephoneInfoDocument.TelephoneInfo;
import net.hubs1.ids.genareshotelgetmsgrs.TimeSpanDocument.TimeSpan;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.util.StringUtils;

import com.thayer.fog.vo.IataDataResultVo;
import com.thayer.fog.vo.IataDataVo;
import com.thayer.fogservice.webservice.ids.IDSWebService;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.genares.util.AssemblyUtil;
import com.thayer.idsservice.ids.genares.util.GenaresUtil;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.task.download.interf.AbstractDownLoadService;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.Convert;
import com.thayer.idsservice.util.DateUtil;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jimmy.Chen<br>
 * @since : Oct 12, 2010<br>
 * @version : v1.0
 */
public class GenaresDownloadService extends AbstractDownLoadService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4570760653692612485L;

	private static Logger LOGGER = Logger.getLogger(GenaresDownloadService.class);

	private IDSWebService fogIDSServiceWSClient;

	public IDSWebService getFogIDSServiceWSClient() {
		return fogIDSServiceWSClient;
	}

	public void setFogIDSServiceWSClient(IDSWebService fogIDSServiceWSClient) {
		this.fogIDSServiceWSClient = fogIDSServiceWSClient;
	}

	@Override
	public String getBookingListRequestXml(HotelBean hotelBean) throws BizException {
		String grRequestXml = null;
		String wsseUsername = hotelBean.getUsername();
		String wssePassword = hotelBean.getPassword();
		OTAHotelGetMsgRQDocument otaHotelGetMsgRQDocument = OTAHotelGetMsgRQDocument.Factory.newInstance();
		OTAHotelGetMsgRQ otaHotelGetMsgRQ = otaHotelGetMsgRQDocument.addNewOTAHotelGetMsgRQ();
		otaHotelGetMsgRQ.setVersion("1.100");
		Messages messages = otaHotelGetMsgRQ.addNewMessages();
		Message message = messages.addNewMessage();
		message.setHotelCode(hotelBean.getThirdHotelCode());
		message.setReasonForRequest("Reservation Retrieval");
		message.setStartSeqNmbr("0");
		message.setEndSeqNmbr("9");
		grRequestXml = otaHotelGetMsgRQDocument.toString();
		grRequestXml = grRequestXml.replaceAll("http://www.hubs1.net/ids/genareshotelgetmsgrq\"",
				"http://www.opentravel.org/OTA/2003/05\"");
		String wsaAction = "OTA_HotelGetMsgRQ";

		String header = AssemblyUtil.getSoapHead(wsaAction, wsseUsername, wssePassword);
		String body = "<soap:Body>" + grRequestXml + "</soap:Body></soap:Envelope>";
		String requestXml = header + body;
		LOGGER.info("getBookingListRequestXml: " + requestXml);

		return requestXml;
	}

	@Override
	public String[] handleResponseXml4detailXml(HotelBean hotelBean, String bookingListResponseXml) throws BizException {
		if (StringUtils.hasText(bookingListResponseXml)) {
			LOGGER.info("handleResponseXml4detailXml: " + bookingListResponseXml);
			String returnXML = bookingListResponseXml;
			try {
				String startFlag = "<soap:Body>";
				String endFlag = "</soap:Body>";
				int frPos = returnXML.indexOf(startFlag);
				int toPos = returnXML.indexOf(endFlag);
				returnXML = returnXML.substring(frPos + startFlag.length(), toPos);
				returnXML = returnXML.replaceFirst("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
						"xmlns=\"http://www.hubs1.net/ids/genareshotelgetmsgrs\"");
				returnXML = returnXML.replaceFirst(
						"xsi:schemaLocation=\"http://www.opentravel.org/OTA/2003/05/OTA_HotelGetMsgRS.xsd\"", "");
				returnXML = returnXML
						.replace(
								"<OTA_HotelResNotifRQ xmlns=\"http://www.opentravel.org/OTA/2003/05\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opentravel.org/OTA/2003/05 OTA_HotelResNotifRQ.xsd\"",
								"<OTA_HotelResNotifRQ");
				LOGGER.info("returnXML: " + returnXML);
			} catch (Exception e) {
				LOGGER.error("parse returnXML xml error:" + ExceptionUtils.getFullStackTrace(e));
				throw new BizException("parse returnXML Error");

			}
			try {
				OTAHotelGetMsgRSDocument otaHotelGetMsgRSDocument = OTAHotelGetMsgRSDocument.Factory.parse(returnXML);
				OTAHotelGetMsgRS otaHotelGetMsgRS = otaHotelGetMsgRSDocument.getOTAHotelGetMsgRS();
				String success = otaHotelGetMsgRS.getSuccess();
				if (success != null) {
					// 判断是否有定单,没有的时候数组长度为0
					if (otaHotelGetMsgRS.getMessages() != null
							&& otaHotelGetMsgRS.getMessages().getMessageArray() != null) {
						net.hubs1.ids.genareshotelgetmsgrs.MessageDocument.Message[] messageArray = otaHotelGetMsgRS
								.getMessages().getMessageArray();
						String[] requestlist = new String[messageArray.length];
						if (messageArray.length > 0) {
							int i = 0;
							for (net.hubs1.ids.genareshotelgetmsgrs.MessageDocument.Message messageRS : messageArray) {
								OTAHotelGetMsgRSDocument otaNewHotelGetMsgRSDocument = OTAHotelGetMsgRSDocument.Factory
										.newInstance();
								otaNewHotelGetMsgRSDocument.addNewOTAHotelGetMsgRS().addNewMessages().addNewMessage()
										.setMessageContent(messageRS.getMessageContent());
								LOGGER.info("handle detail request Xml:" + otaNewHotelGetMsgRSDocument.toString());
								requestlist[i] = otaNewHotelGetMsgRSDocument.toString();
								i++;
							}

						}
						return requestlist;
					} else {
						LOGGER.info("otaHotelGetMsgRS.getMessages().getMessageArray size is 0");
						String[] requestlist = new String[0];
						return requestlist;
					}

				} else {
					LOGGER.error("parse OTAHotelGetMsgRSDocument xml not success error returnXML :" + returnXML);
					throw new BizException("parse OTAHotelGetMsgRSDocument xml not success error  Error");
				}
			} catch (Exception e) {
				LOGGER.error("parse OTAHotelGetMsgRSDocument xml error:" + ExceptionUtils.getFullStackTrace(e));
				throw new BizException("parse OTAHotelGetMsgRSDocument xml Error");
			}

		} else {
			LOGGER.error(" bookingListResponseXml is null error");
			throw new BizException("bookingListResponseXml is null error");
		}
	}

	@Override
	public TResvBase handleResponseXml4fogbean(HotelBean hotelBean, String bookingResponseXml) throws BizException,
			MappingException {
		LOGGER.info("handleResponseXml4fogbean: " + bookingResponseXml);
		String returnXML = bookingResponseXml;
		TResvBase tresvbase = new TResvBase();
		if (StringUtils.hasText(returnXML)) {
			try {
				OTAHotelGetMsgRSDocument otaHotelGetMsgRSDocument = OTAHotelGetMsgRSDocument.Factory.parse(returnXML);
				OTAHotelGetMsgRS otaHotelGetMsgRS = otaHotelGetMsgRSDocument.getOTAHotelGetMsgRS();
				net.hubs1.ids.genareshotelgetmsgrs.MessageDocument.Message[] messageArray = otaHotelGetMsgRS
						.getMessages().getMessageArray();
				if (messageArray.length > 0) {
					for (net.hubs1.ids.genareshotelgetmsgrs.MessageDocument.Message messageRS : messageArray) {
						MessageContent messageContent = messageRS.getMessageContent();
						OTAHotelResNotifRQ otaHotelResNotifRQ = messageContent.getOTAHotelResNotifRQ();
						POS pos = otaHotelResNotifRQ.getPOS();
						Source source = pos.getSource();
						BookingChannel bookingChannel = source.getBookingChannel();
						String bookingChannelType = bookingChannel.getType();
						String companyName = bookingChannel.getCompanyName();
						HotelReservations hotelReservations = otaHotelResNotifRQ.getHotelReservations();
						HotelReservation hotelReservation = hotelReservations.getHotelReservation();
						// 保存定单号
						String outcnfnum = hotelReservation.getUniqueID().getID();
						tresvbase.setOutcnfnum(outcnfnum);
						tresvbase.setProp(hotelBean.getFogHotelCode());
						String resStatus = otaHotelResNotifRQ.getResStatus();
						if (resStatus.equalsIgnoreCase("Cancel")) {
							tresvbase.setResvtype(Constents.RESV_TYPE_CANCEL);
							return tresvbase;
						}
						if (hotelReservation.getRoomStays() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument getRoomStays is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument getRoomStays is null");
						}
						RoomStays roomStays = hotelReservation.getRoomStays();
						if (roomStays.getRoomStay() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocumentroomStays.getRoomStay() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument roomStays.getRoomStay() is null");

						}
						RoomStay roomStay = roomStays.getRoomStay();
						if (roomStay.getTimeSpan() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument roomStay.getTimeSpan() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument roomStay.getTimeSpan() is null");

						}
						TimeSpan timeSpan = roomStay.getTimeSpan();
						Date indate = DateUtil.dateValue(timeSpan.getStart());
						Date outdate = DateUtil.dateValue(timeSpan.getEnd());
						int nights = DateUtil.dateDiff(DateUtil.TIME_UNIT_D, indate, outdate);
						if (roomStay.getRatePlans() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument roomStay.getRatePlans() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument roomStay.getRatePlans() is null");

						}
						RatePlans ratePlans = roomStay.getRatePlans();
						if (ratePlans.getRatePlan() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument ratePlans.getRatePlan() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument ratePlans.getRatePlan() is null");

						}
						RatePlan ratePlan = ratePlans.getRatePlan();

						if (roomStay.getRoomTypes() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument roomStay.getRoomTypes() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument roomStay.getRoomTypes() is null");

						}

						RoomTypes roomTypes = roomStay.getRoomTypes();
						if (roomTypes.getRoomType() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument roomTypes.getRoomType() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument roomTypes.getRoomType() is null");

						}
						RoomType roomType = roomTypes.getRoomType();
						int rooms = Convert.ToInt(roomType.getNumberOfUnits());

						if (roomStay.getGuestCounts() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument roomStay.getGuestCounts() is null");
							throw new BizException("parse OTAHotelGetMsgRSDocument roomStay.getGuestCounts() is null");

						}
						GuestCounts guestCounts = roomStay.getGuestCounts();
						int audlts = 0;
						int children = 0;
						if (guestCounts.getGuestCountArray() == null) {
							LOGGER.error("parse OTAHotelGetMsgRSDocument guestCounts.getGuestCountArray() is null");
							throw new BizException(
									"parse OTAHotelGetMsgRSDocument guestCounts.getGuestCountArray() is null");

						}
						GuestCount[] guestCountArray = guestCounts.getGuestCountArray();
						for (GuestCount guestCount : guestCountArray) {
							String ageQualifyingCode = guestCount.getAgeQualifyingCode();
							String count = guestCount.getCount();
							if ("10".equalsIgnoreCase(ageQualifyingCode)) {
								audlts = Integer.parseInt(count) * rooms;
							}
							if ("8".equalsIgnoreCase(ageQualifyingCode)) {
								children = Integer.parseInt(count) * rooms;
							}
						}
						String cctype = "";
						String ccname = "";
						String ccnumber = "";
						String ccexpiration = "";
						String guaranteeCode = "";
						if (roomStay.getGuarantee() != null
								&& roomStay.getGuarantee().getGuaranteesAccepted() != null
								&& roomStay.getGuarantee().getGuaranteesAccepted().getGuaranteeAccepted() != null
								&& roomStay.getGuarantee().getGuaranteesAccepted().getGuaranteeAccepted()
										.getPaymentCard() != null) {
							PaymentCard paymentCard = roomStay.getGuarantee().getGuaranteesAccepted()
									.getGuaranteeAccepted().getPaymentCard();
							cctype = paymentCard.getCardCode();
							ccname = paymentCard.getCardHolderName();
							ccnumber = paymentCard.getCardNumber();
							ccexpiration = paymentCard.getExpireDate();

						}
						// 设置保证金制度
						if (roomStay.getGuarantee() != null) {
							guaranteeCode = roomStay.getGuarantee().getGuaranteeCode();
						}

						String roomTypeCode = roomType.getRoomTypeCode();
						String ratePlanCode = ratePlan.getRatePlanCode();
						StringBuffer guestName = new StringBuffer();
						String gfirstname = "";
						String glastname = "";
						String guestmail = "";
						String guestphone = "";
						String guestaddress = "";
						String iata = "";
						String iataName = "";
						String iataAddressLine1 = "";
						String iataAddressLine2 = "";
						String iataCityName = "";
						String iataPostalCode = "";
						String iataStateProv = "";
						String iataCountryName = "";
						String iataEmail = "";
						String iataTel = "";
						String subsourcecode = "";
						if (hotelReservation.getResGuests() != null
								&& hotelReservation.getResGuests().getResGuest() != null
								&& hotelReservation.getResGuests().getResGuest().getProfiles() != null
								&& hotelReservation.getResGuests().getResGuest().getProfiles().getProfileInfoArray() != null) {
							ProfileInfo[] profileInfoArray = hotelReservation.getResGuests().getResGuest()
									.getProfiles().getProfileInfoArray();
							for (ProfileInfo profileInfo : profileInfoArray) {
								Profile profile = profileInfo.getProfile();
								String profileType = profile.getProfileType();
								// 表示个人
								if ("1".equalsIgnoreCase(profileType)) {
									if (profile.getCustomer() != null) {
										if (profile.getCustomer().getPersonName() != null) {
											PersonName personName = profile.getCustomer().getPersonName();
											gfirstname = personName.getGivenName();
											glastname = personName.getSurname();
											guestName.append(personName.getGivenName()).append(" ").append(personName.getSurname());
										}
										if (profile.getCustomer().getEmail() != null) {

											guestmail = profile.getCustomer().getEmail();

											// guestmail = guestmail.replace("<xml-fragment DefaultInd=\"1\">", "");
											// guestmail = guestmail.replace("</xml-fragment>", "");
										}
										if (profile.getCustomer().getTelephone() != null) {
											guestphone = profile.getCustomer().getTelephone().getPhoneNumber();
										} else {
											guestphone = "00000000";
										}
										if (profile.getCustomer().getAddress() != null
												&& profile.getCustomer().getAddress().getAddressLineArray() != null) {
											guestaddress = profile.getCustomer().getAddress().getAddressLineArray(0);
										}

									}

								}
								// 取iata号
								if (profileInfo.getUniqueID() != null) {
									// 取iata号
									String idcontext = profileInfo.getUniqueID().getIDContext();
									String id = profileInfo.getUniqueID().getID();
									String type = profileInfo.getUniqueID().getType();
									if ("5".equalsIgnoreCase(type) && "IATA".equalsIgnoreCase(idcontext)) {
										iata = id;
										iataName = profile.getCompanyInfo().getCompanyName();
										AddressInfo addressInfo = profile.getCompanyInfo().getAddressInfo();
										iataEmail = profile.getCompanyInfo().getEmail();
										TelephoneInfo[] telephoneInfoArray = profile.getCompanyInfo()
												.getTelephoneInfoArray();
										if (telephoneInfoArray != null) {
											for (TelephoneInfo tel : telephoneInfoArray) {
												iataTel += tel.getPhoneNumber() + " ";
											}
										}
										String[] addressLineArray = addressInfo.getAddressLineArray();
										if (addressLineArray != null) {

											for (String s : addressLineArray) {
												iataAddressLine1 += s + " ";
											}

										}
										iataCityName = addressInfo.getCityName();
										iataPostalCode = addressInfo.getPostalCode();
										StateProv stateProv = addressInfo.getStateProv();
										if (stateProv != null) {
											iataStateProv = stateProv.getStateCode();
										}
										CountryName countryName = addressInfo.getCountryName();
										if (countryName != null) {
											iataCountryName = countryName.getCode();
										}
									}
								}

							}
						}

						LOGGER.info("iata:" + iata);
						if (StringUtils.hasText(iata) && StringUtils.hasText(bookingChannelType)
								&& (bookingChannelType.equals("7") || bookingChannelType.equals("1"))) {
							try {
								tresvbase.setChannel("!DEFAULT!");
								IataDataVo iataDataVo = new IataDataVo();
								iataDataVo.setIataCode(iata);
								iataDataVo.setIataName(iataName);
								iataDataVo.setAddress1(iataAddressLine1);
								// iataDataVo.setAddress2(iataAddressLine2);
								iataDataVo.setCity(iataCityName);
								iataDataVo.setCountry(iataCountryName);
								iataDataVo.setZip(iataPostalCode);
								iataDataVo.setEmail(iataEmail);
								iataDataVo.setPhNumber(iataTel);
								iataDataVo.setBookingSourceCode("GDS");
								if (bookingChannelType.equals("7")) {
									// Internet渠道
									iataDataVo.setBookingSubsourceCode("Internet");
								}
								if (bookingChannelType.equals("1")) {
									// GDS渠道，需要设置子渠道
									iataDataVo.setBookingSubsourceCode(companyName);
								}
								LOGGER.info("BookingSubsourceCode:" + iataDataVo.getBookingSubsourceCode());

								IataDataResultVo iataDataResultVo = fogIDSServiceWSClient.saveIata(iataDataVo);
								String result = iataDataResultVo.getResult();
								String error = iataDataResultVo.getError();
								LOGGER.info("error:" + error + ",result:" + result);
								if (!IataDataResultVo.SUCESS.equalsIgnoreCase(result)) {
									LOGGER.error("fogIDSServiceWSClient.saveIata getResult" + result);
									throw new BizException("fogIDSServiceWSClient.saveIata getResult Error");
								}
							} catch (Exception e) {
								LOGGER.error("fogIDSServiceWSClient.saveIata error:"
										+ ExceptionUtils.getFullStackTrace(e));
								throw new BizException("fogIDSServiceWSClient.saveIata Error");
							}
						}
						String remark = "";
						if (roomStay.getComments() != null && roomStay.getComments().getComment() != null) {
							remark = roomStay.getComments().getComment().getText();
						}

						if (resStatus.equalsIgnoreCase("Commit")) {
							tresvbase.setResvtype(Constents.RESV_TYPE_NEW);
						} else if (resStatus.equalsIgnoreCase("Modify")) {
							tresvbase.setResvtype(Constents.RESV_TYPE_EDIT);
						}

						if (StringUtils.hasText(iata)) {
							tresvbase.setIata(iata);
						} else {
							tresvbase.setIata(GenaresUtil.getPropertiesValue("genares.iata"));
						}

						tresvbase.setIndate(indate);
						tresvbase.setOutdate(outdate);
						tresvbase.setNights(nights);
						tresvbase.setRooms(rooms);
						tresvbase.setAdults(audlts);
						tresvbase.setChildren(children);
						tresvbase.setCctype(cctype);
						tresvbase.setCcname(ccname);
						tresvbase.setCcexpiration(ccexpiration);
						tresvbase.setCcnumber(ccnumber);
						
						//客人姓名的处理
						tresvbase.setGuestname(guestName.toString());
						List<String> guestList = new ArrayList<String>();
						guestList.add(guestName.toString());
						tresvbase.setGuestList(guestList);
						
						tresvbase.setGfirstname(gfirstname);
						tresvbase.setGlastname(glastname);
						tresvbase.setGuestphone(guestphone);
						tresvbase.setGuestmail(guestmail);
						tresvbase.setGuestaddress(guestaddress);
						tresvbase.setRemark(remark);
						// tresvbase.setGuaCode(guaranteeCode);
						// 保证金制度表述
						tresvbase.setGuadesc(guaranteeCode);
						String fogroomtype;

						fogroomtype = getMapService().getRoomRateMap(roomTypeCode, hotelBean.getFogHotelCode(),
								MappingEnum.GENARES, "roomType");

						tresvbase.setRoomcode(fogroomtype);
						String fograteClass;

						fograteClass = getMapService().getRoomRateMap(roomTypeCode + "#" + ratePlanCode,
								hotelBean.getFogHotelCode(), MappingEnum.GENARES, "rateType");

						tresvbase.setRateClass(fograteClass);

						LOGGER.info("outcnfnum:" + outcnfnum + ",Prop:" + hotelBean.getFogHotelCode()
								+ ",hotelBean.getFogIataCode():" + hotelBean.getFogIataCode() + ",indate:" + indate
								+ ",outdate:" + outdate + ",nights:" + nights + ",rooms:" + rooms + ",audlts:" + audlts
								+ ",children:" + children + ",cctype:" + cctype + ",ccname:" + ccname
								+ ",ccexpiration:" + ccexpiration + ",ccnumber:" + ccnumber + ",gfirstname:"
								+ gfirstname + ",glastname:" + glastname + ",guestphone:" + guestphone + ",guestmail:"
								+ guestmail + ",fogroomtype:" + fogroomtype + ",fograteClass:" + fograteClass
								+ ",remark:" + remark);
						List<TResvRate> resvRateList = new ArrayList<TResvRate>();
						Rate[] rateArray = hotelReservation.getRoomStays().getRoomStay().getRoomRates().getRoomRate()
								.getRates().getRateArray();
						for (Rate rate : rateArray) {
							String effectiveDate = rate.getEffectiveDate();
							String expireDate = rate.getExpireDate();
							String amountBeforeTax = rate.getBase().getAmountBeforeTax();
							String currencyCode = rate.getBase().getCurrencyCode();
							int days = DateUtil.dateDiff(DateUtil.TIME_UNIT_D, DateUtil.dateValue(effectiveDate),
									DateUtil.dateValue(expireDate));
							LOGGER.info("days:" + days + ",effectiveDate:" + effectiveDate + ",expireDate:"
									+ expireDate + ",amountBeforeTax:" + amountBeforeTax + ",currencyCode:"
									+ currencyCode);
							for (int l = 0; l < days; l++) {
								TResvRate tResvRate = new TResvRate();
								tResvRate.setBookendate(DateUtil.dateAdd(DateUtil.TIME_UNIT_D, l, DateUtil
										.dateValue(effectiveDate)));
								tResvRate.setBookendatestr(DateUtil.formatDate(tResvRate.getBookendate()));
								tResvRate.setPropCurrency(currencyCode);
								tResvRate.setProprevenue(Double.parseDouble(amountBeforeTax));
								resvRateList.add(tResvRate);
							}
						}
						tresvbase.setResvRateList(resvRateList);
					}
				}
			} catch (MappingException e) {
				LOGGER.error("Mapping error:" + ExceptionUtils.getFullStackTrace(e));
				throw new MappingException(e.getMessage());
			} catch (XmlException e) {
				LOGGER.error("parse OTAHotelGetMsgRSDocument xml error:" + ExceptionUtils.getFullStackTrace(e));
				throw new BizException(e.getMessage());
			} catch (Exception e) {
				LOGGER.error("parse OTAHotelGetMsgRSDocument xml error:" + ExceptionUtils.getFullStackTrace(e));
				throw new BizException(e.getMessage());
			}

		}
		return tresvbase;
	}

	@Override
	public List<TResvBase> handleResponseXml4fogbeanList(HotelBean hotelBean, String bookingListResponseXml,
			ResultBean resultBean) throws BizException {
		// TODO Auto-generated method stub
		return null;
	}

}
