/*****************************************************************<br>
 * <B>FILE :</B> AsiaroomsTransPropertyService.java <br>
 * <B>CREATE DATE :</B> 2011-5-12 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.asiarooms.trans;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.opentravel.ota.x2003.x05.AddressesType;
import org.opentravel.ota.x2003.x05.ContactInfoRootType;
import org.opentravel.ota.x2003.x05.CountryNameType;
import org.opentravel.ota.x2003.x05.EmailsType;
import org.opentravel.ota.x2003.x05.ErrorsType;
import org.opentravel.ota.x2003.x05.HotelInfoType;
import org.opentravel.ota.x2003.x05.MultimediaObjectType;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRSDocument;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRSDocument;
import org.opentravel.ota.x2003.x05.POSType;
import org.opentravel.ota.x2003.x05.ParagraphType;
import org.opentravel.ota.x2003.x05.SourceType;
import org.opentravel.ota.x2003.x05.StateProvType;
import org.opentravel.ota.x2003.x05.URLsType;
import org.opentravel.ota.x2003.x05.WarningsType;
import org.opentravel.ota.x2003.x05.AddressesType.Address;
import org.opentravel.ota.x2003.x05.EmailsType.Email;
import org.opentravel.ota.x2003.x05.FacilityInfoType.GuestRooms;
import org.opentravel.ota.x2003.x05.FacilityInfoType.GuestRooms.GuestRoom;
import org.opentravel.ota.x2003.x05.FacilityInfoType.GuestRooms.GuestRoom.TypeRoom;
import org.opentravel.ota.x2003.x05.FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument.OTAHotelDescriptiveContentNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument.OTAHotelDescriptiveContentNotifRQ.HotelDescriptiveContents;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument.OTAHotelDescriptiveContentNotifRQ.HotelDescriptiveContents.HotelDescriptiveContent;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans.RatePlan;
import org.opentravel.ota.x2003.x05.OTAHotelRatePlanNotifRQDocument.OTAHotelRatePlanNotifRQ.RatePlans.RatePlan.RatePlanNotifType;
import org.opentravel.ota.x2003.x05.SourceType.RequestorID;
import org.opentravel.ota.x2003.x05.URLsType.URL;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import com.thayer.fog2.entity.Rate;
import com.thayer.fog2.entity.Room;
import com.thayer.fogservice.webservice.crs.RateWebService;
import com.thayer.fogservice.webservice.crs.RoomWebService;
import com.thayer.idsservice.bean.PropertyInfo;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.service.interf.ITransPropertyInfoService;
import com.thayer.idsservice.util.Constents;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-5-12<br>
 * @version : v1.0
 */
public class AsiaroomsTransPropertyService implements ITransPropertyInfoService {
	private static Logger LOGGER = Logger.getLogger(AsiaroomsTransPropertyService.class);
	private String asiaroomsUserId;
	private String asiaroomsUserPwd;
	private RoomWebService fogServiceRoomWSClient;
	private RateWebService fogServiceRateWSClient;
	private IHttpClientService httpClientService;
	private String thirdRequestXmlURL;
	private static Map<String, String> roomTypeMap = new HashMap<String, String>();
	static {
		roomTypeMap.put("Premium", "1");
		roomTypeMap.put("Economy", "1");
		roomTypeMap.put("Standard", "1");
		roomTypeMap.put("Superior", "4");
		roomTypeMap.put("Deluxe", "3");
		roomTypeMap.put("Excecutive", "2");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.thayer.idsservice.service.interf.ITransPropertyInfoService#transPropertyInfo(com.thayer.idsservice.bean.
	 * PropertyInfo)
	 */
	@Override
	public void transPropertyInfo(PropertyInfo propertyInfo) throws BizException {
		try {
			String prop = propertyInfo.getProp();

			if ("1".equals(propertyInfo.getTransferType())) {
				ClassPathResource resource = new ClassPathResource(
						"/idsconfig/asiarooms/OTA_HotelDescriptiveContentNotifRQ.xml");
				File file = resource.getFile();
				OTAHotelDescriptiveContentNotifRQDocument contentNotifRQDocument = OTAHotelDescriptiveContentNotifRQDocument.Factory
						.parse(file);
				OTAHotelDescriptiveContentNotifRQ otaHotelDescriptiveContentNotifRQ = contentNotifRQDocument
						.getOTAHotelDescriptiveContentNotifRQ();
				POSType pos = otaHotelDescriptiveContentNotifRQ.getPOS();
				SourceType source = pos.getSourceArray(0);
				RequestorID request = source.getRequestorID();
				request.setID(asiaroomsUserId);
				request.setMessagePassword(asiaroomsUserPwd);
				HotelDescriptiveContents contents = otaHotelDescriptiveContentNotifRQ.getHotelDescriptiveContents();
				HotelDescriptiveContent content = contents.getHotelDescriptiveContentArray(0);

				if (StringUtils.hasText(propertyInfo.getIdsProp())) {
					content.setHotelCodeContext(propertyInfo.getIdsProp());
				}
				content.setHotelCode(prop);
				content.setHotelName(propertyInfo.getPropName_en());
				// content.setChainCode("Hubs1");
				content.setCurrencyCode(propertyInfo.getCurrencycode());
				HotelInfoType hotelInfo = content.getHotelInfo();
				hotelInfo.getRelativePositions().getRelativePositionArray(0).getTransportations()
						.getTransportationArray(0).getDescriptionsArray(0).getDescriptionArray(0).setDescription(
								propertyInfo.getDescription_en());
				hotelInfo.getDescriptions().getDescriptionArray(0).getTextArray(0).setStringValue(
						propertyInfo.getSimplDesc_en());
				hotelInfo.getDescriptions().getDescriptionArray(0).setURLArray(0, propertyInfo.getWebsite());
				hotelInfo.getPosition().setLatitude(propertyInfo.getLatitude());
				hotelInfo.getPosition().setLongitude(propertyInfo.getLongitude());

				GuestRooms guestRooms = content.getFacilityInfo().getGuestRooms();
				String[] fogrooms = propertyInfo.getRoomCode();

				if (fogrooms == null) {
					throw new BizException("Please select room types!");
				}

				for (String roomCode : fogrooms) {
					GuestRoom guestRoom = guestRooms.addNewGuestRoom();
					Room fogRoomInfo = fogServiceRoomWSClient.findRoomInfoByIdProp(roomCode, prop);
					guestRoom.setCode(roomTypeMap.get(fogRoomInfo.getCategory()));
					guestRoom.setID(roomCode);
					guestRoom.setQuantity(fogRoomInfo.getAmount());
					Integer maxOccupancy = fogRoomInfo.getMaxOccupancy();
					guestRoom.setMaxOccupancy(new BigInteger(maxOccupancy.toString()));
					TypeRoom typeRoom = guestRoom.addNewTypeRoom();
					typeRoom.setStandardNumBeds(new BigInteger("2"));
					typeRoom.setStandardOccupancy(new BigInteger(fogRoomInfo.getAdultNoRollaway().toString()));
					typeRoom.setMaxRollaways(new BigInteger(fogRoomInfo.getMaxRollaway().toString()));
					typeRoom.setConfiguration("double");
					Amenity amenity = guestRoom.addNewAmenities().addNewAmenity();
					amenity.setRoomAmenityCode("148");
					amenity.setCodeDetail("Direct Dial Telephone");
					MultimediaObjectType description = guestRoom.addNewDescription();
					description.addNewText().setStringValue(fogRoomInfo.getDesc_en());

				}

				content.getAffiliationInfo().getAwards().getAwardArray(0).setRating(
						propertyInfo.getStarRating().toString());

				ContactInfoRootType contactInfoArray = content.getContactInfos().getContactInfoArray(0);
				AddressesType addresses = contactInfoArray.getAddresses();
				Address address = addresses.addNewAddress();
				address.setUseType("7");
				address.addNewAddressLine().setStringValue(propertyInfo.getAddress1_en());
				address.addNewAddressLine().setStringValue(propertyInfo.getAddress2_en());
				address.setCityName(propertyInfo.getCityId());
				address.setPostalCode(propertyInfo.getZip());
				CountryNameType countryName = address.addNewCountryName();
				countryName.setCode("CN");
				countryName.setStringValue("CN");
				StateProvType stateProv = address.addNewStateProv();
				stateProv.setStringValue("NA");

				EmailsType emails = contactInfoArray.getEmails();
				Email mail1 = emails.addNewEmail();
				mail1.setEmailType("6");
				mail1.setStringValue(propertyInfo.getBillMail());

				Email mail2 = emails.addNewEmail();
				mail2.setEmailType("7");
				mail2.setStringValue(propertyInfo.getReservationMail());
				URLsType urls = contactInfoArray.addNewURLs();
				URL url = urls.addNewURL();
				url.setStringValue(propertyInfo.getUrl());
				LOGGER.info("build new Property request xml: \n" + contentNotifRQDocument.toString());
				String rs = httpClientService.postHttpRequest(thirdRequestXmlURL, contentNotifRQDocument.toString());
				LOGGER.info("build new Property response xml: \n" + rs);

				OTAHotelDescriptiveContentNotifRSDocument contentNotifRSDocument = OTAHotelDescriptiveContentNotifRSDocument.Factory
						.parse(rs);
				ErrorsType errors = contentNotifRSDocument.getOTAHotelDescriptiveContentNotifRS().getErrors();
				if (errors != null) {
					throw new BizException("build new Property error : " + errors.getErrorArray(0).getShortText() + "－"
							+ errors.getErrorArray(0).getStringValue());
				}
			}
			ClassPathResource rateInfoResource = new ClassPathResource(
					"/idsconfig/asiarooms/OTA_HotelRatePlanNotifRQ.xml");
			File rateFile = rateInfoResource.getFile();

			String[] rateCodes = propertyInfo.getRateCode();
			if (rateCodes != null) {
				for (String rateCode : rateCodes) {
					OTAHotelRatePlanNotifRQDocument ratePlanNotifRQDocument = OTAHotelRatePlanNotifRQDocument.Factory
							.parse(rateFile);
					OTAHotelRatePlanNotifRQ otaHotelRatePlanNotifRQ = ratePlanNotifRQDocument
							.getOTAHotelRatePlanNotifRQ();
					POSType ratepos = otaHotelRatePlanNotifRQ.getPOS();
					SourceType ratesource = ratepos.getSourceArray(0);
					RequestorID raterequest = ratesource.getRequestorID();
					raterequest.setID(asiaroomsUserId);
					raterequest.setMessagePassword(asiaroomsUserPwd);
					RatePlans ratePlans = otaHotelRatePlanNotifRQ.getRatePlans();
					ratePlans.setHotelCode(prop);
					RatePlan ratePlan = ratePlans.getRatePlanArray(0);
					ratePlan.setRatePlanCode(rateCode);
					if (Constents.RATE_PLAN_NOTIFY_TYPE_NEW.equals(propertyInfo.getRatePlanNotifyType())) {
						ratePlan.setRatePlanNotifType(RatePlanNotifType.NEW);
					}
					if (Constents.RATE_PLAN_NOTIFY_TYPE_OVERLAY.equals(propertyInfo.getRatePlanNotifyType())) {
						ratePlan.setRatePlanNotifType(RatePlanNotifType.OVERLAY);
					}
					if (Constents.RATE_PLAN_NOTIFY_TYPE_REMOVE.equals(propertyInfo.getRatePlanNotifyType())) {
						ratePlan.setRatePlanNotifType(RatePlanNotifType.REMOVE);
					}
					Rate rate = fogServiceRateWSClient.findRateById(rateCode, prop);
					ParagraphType descriptionArray = ratePlan.getDescriptionArray(0);
					descriptionArray.getTextArray(0).setStringValue(rate.getDesc_en());
					LOGGER.info("build new ratePlan request xml: \n" + ratePlanNotifRQDocument.toString());
					String ratePlanRs = httpClientService.postHttpRequest(thirdRequestXmlURL, ratePlanNotifRQDocument
							.toString());

					OTAHotelRatePlanNotifRSDocument ratePlanNotifRSDocument = OTAHotelRatePlanNotifRSDocument.Factory
							.parse(ratePlanRs);
					ErrorsType errors2 = ratePlanNotifRSDocument.getOTAHotelRatePlanNotifRS().getErrors();
					if (errors2 != null) {
						throw new BizException("build new ratePlan error : " + errors2.getErrorArray(0).getShortText()
								+ "－" + errors2.getErrorArray(0).getStringValue());
					}

					WarningsType warnings = ratePlanNotifRSDocument.getOTAHotelRatePlanNotifRS().getWarnings();
					if (warnings != null) {
						throw new BizException("build new ratePlan error : "
								+ warnings.getWarningArray(0).getStringValue());
					}

					LOGGER.info("build new ratePlan response xml: \n" + ratePlanRs);
				}
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new BizException("Can not load transfer template!");
		} catch (XmlException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new BizException("Can not read transfer xml content!");
		} catch (TimeOutException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new BizException("Transfer Time out!");
		} catch (BizException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new BizException("Can not transfer to IDS site!");
		}

	}

	/**
	 * @return the asiaroomsUserId
	 */
	public String getAsiaroomsUserId() {
		return asiaroomsUserId;
	}

	/**
	 * @param asiaroomsUserId
	 *            the asiaroomsUserId to set
	 */
	public void setAsiaroomsUserId(String asiaroomsUserId) {
		this.asiaroomsUserId = asiaroomsUserId;
	}

	/**
	 * @return the asiaroomsUserPwd
	 */
	public String getAsiaroomsUserPwd() {
		return asiaroomsUserPwd;
	}

	/**
	 * @param asiaroomsUserPwd
	 *            the asiaroomsUserPwd to set
	 */
	public void setAsiaroomsUserPwd(String asiaroomsUserPwd) {
		this.asiaroomsUserPwd = asiaroomsUserPwd;
	}

	/**
	 * @return the fogServiceRoomWSClient
	 */
	public RoomWebService getFogServiceRoomWSClient() {
		return fogServiceRoomWSClient;
	}

	/**
	 * @param fogServiceRoomWSClient
	 *            the fogServiceRoomWSClient to set
	 */
	public void setFogServiceRoomWSClient(RoomWebService fogServiceRoomWSClient) {
		this.fogServiceRoomWSClient = fogServiceRoomWSClient;
	}

	/**
	 * @return the fogServiceRateWSClient
	 */
	public RateWebService getFogServiceRateWSClient() {
		return fogServiceRateWSClient;
	}

	/**
	 * @param fogServiceRateWSClient
	 *            the fogServiceRateWSClient to set
	 */
	public void setFogServiceRateWSClient(RateWebService fogServiceRateWSClient) {
		this.fogServiceRateWSClient = fogServiceRateWSClient;
	}

	/**
	 * @return the httpClientService
	 */
	public IHttpClientService getHttpClientService() {
		return httpClientService;
	}

	/**
	 * @param httpClientService
	 *            the httpClientService to set
	 */
	public void setHttpClientService(IHttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}

	/**
	 * @return the thirdRequestXmlURL
	 */
	public String getThirdRequestXmlURL() {
		return thirdRequestXmlURL;
	}

	/**
	 * @param thirdRequestXmlURL
	 *            the thirdRequestXmlURL to set
	 */
	public void setThirdRequestXmlURL(String thirdRequestXmlURL) {
		this.thirdRequestXmlURL = thirdRequestXmlURL;
	}

}
