/*****************************************************************<br>
 * <B>FILE :</B> AsiaroomTest.java <br>
 * <B>CREATE DATE :</B> 2011-3-24 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.test;

import java.util.Calendar;
import java.util.Date;

import org.opentravel.ota.x2003.x05.ContactInfoRootType;
import org.opentravel.ota.x2003.x05.ContactInfosType;
import org.opentravel.ota.x2003.x05.ContactsType;
import org.opentravel.ota.x2003.x05.EmailsType;
import org.opentravel.ota.x2003.x05.FacilityInfoType;
import org.opentravel.ota.x2003.x05.HotelInfoType;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument;
import org.opentravel.ota.x2003.x05.POSType;
import org.opentravel.ota.x2003.x05.ContactsType.Name;
import org.opentravel.ota.x2003.x05.EmailsType.Email;
import org.opentravel.ota.x2003.x05.FacilityInfoType.GuestRooms;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument.OTAHotelDescriptiveContentNotifRQ;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument.OTAHotelDescriptiveContentNotifRQ.HotelDescriptiveContents;
import org.opentravel.ota.x2003.x05.OTAHotelDescriptiveContentNotifRQDocument.OTAHotelDescriptiveContentNotifRQ.HotelDescriptiveContents.HotelDescriptiveContent;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.HttpClientService_v3;
import com.thayer.idsservice.util.CommonUtils;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-24<br>
 * @version : v1.0
 */
public class AsiaroomTest extends AbstractDependencyInjectionSpringContextTests {
	public void testCreateHotel() {
		OTAHotelDescriptiveContentNotifRQDocument contentNotifRQDocument = OTAHotelDescriptiveContentNotifRQDocument.Factory
				.newInstance();
		OTAHotelDescriptiveContentNotifRQ addNewOTAHotelDescriptiveContentNotifRQ = contentNotifRQDocument
				.addNewOTAHotelDescriptiveContentNotifRQ();
		addNewOTAHotelDescriptiveContentNotifRQ.setEchoToken(CommonUtils.getUuid());
		{
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			addNewOTAHotelDescriptiveContentNotifRQ.setTimeStamp(c);
		}
		POSType pos = addNewOTAHotelDescriptiveContentNotifRQ.addNewPOS();
		HotelDescriptiveContents addNewHotelDescriptiveContents = addNewOTAHotelDescriptiveContentNotifRQ
				.addNewHotelDescriptiveContents();
		HotelDescriptiveContent addNewHotelDescriptiveContent = addNewHotelDescriptiveContents
				.addNewHotelDescriptiveContent();
		addNewHotelDescriptiveContent.setHotelCodeContext("Hubs1");
		addNewHotelDescriptiveContent.setChainCode("Hubs1");
		addNewHotelDescriptiveContent.setHotelCode("Hubs1Hotel");
		addNewHotelDescriptiveContent.setHotelName("Hubs1 Hotel");
		addNewHotelDescriptiveContent.setChainName("Hubs1");
		addNewHotelDescriptiveContent.setCurrencyCode("CNY");
		addNewHotelDescriptiveContent.setTimeZone("8");
		addNewHotelDescriptiveContent.setLanguageCode("EN");

		HotelInfoType addNewHotelInfo = addNewHotelDescriptiveContent.addNewHotelInfo();
		ContactInfosType addNewContactInfos = addNewHotelDescriptiveContent.addNewContactInfos();
		ContactInfoRootType addNewContactInfo = addNewContactInfos.addNewContactInfo();
		ContactsType addNewNames = addNewContactInfo.addNewNames();
		Name addNewName = addNewNames.addNewName();
		addNewName.setCodeDetail("1");
		addNewName.addGivenName("");
		addNewName.setSurname("Smith");

		EmailsType addNewEmails = addNewContactInfo.addNewEmails();
		Email addNewEmail = addNewEmails.addNewEmail();

		FacilityInfoType addNewFacilityInfo = addNewHotelDescriptiveContent.addNewFacilityInfo();
		GuestRooms addNewGuestRooms = addNewFacilityInfo.addNewGuestRooms();

		String xml = contentNotifRQDocument.toString();
		StringBuffer availSb = new StringBuffer("");
		String asiaroomsUserPwd = "8Abrejar9vad";
		String asiaroomsUserId = "Hubs1";
		availSb.append("<POS><Source><RequestorID MessagePassword=\"" + asiaroomsUserPwd + "\" Type=\"1\" ID=\""
				+ asiaroomsUserId
				+ "\"> <CompanyName>Hubs1</CompanyName></RequestorID><BookingChannel Type=\"7\"/></Source></POS>");
		xml = xml.replaceAll("<POS/>", availSb.toString());
		xml = xml
				.replaceAll(
						"<GuestRooms/>",
						"<GuestRooms> <GuestRoom ID=\"1\" Code=\"1\" Quantity=\"10\" MaxOccupancy=\"1\"> <TypeRoom StandardNumBeds=\"1\" StandardOccupancy=\"1\" MaxRollaways=\"1\" Configuration=\"double\"/> <Amenities> <Amenity RoomAmenityCode=\"140\" CodeDetail=\"Pets Allowed\"/> </Amenities> <Description> <Text>Test room 1</Text> </Description> </GuestRoom> <GuestRoom ID=\"2\" Code=\"2\" Quantity=\"20\" MaxOccupancy=\"2\"> <TypeRoom StandardNumBeds=\"1\" StandardOccupancy=\"1\" Configuration=\"twin\"/> <Amenities> <Amenity RoomAmenityCode=\"140\" CodeDetail=\"Pets Allowed\"/> </Amenities> <Description> <Text>Test room 2</Text> </Description> </GuestRoom> </GuestRooms>");
		xml = xml
				.replaceAll(
						"<OTA_HotelAvailRQ",
						"<OTA_HotelAvailRQ xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
		xml = xml.replaceAll("\\+08:00", "");
		String requestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml;

		String newXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <OTA_HotelDescriptiveContentNotifRQ EchoToken=\"e4c62bec-3123-4625-a391-ca5c672d8ceb\" TimeStamp=\"2011-03-24T15:30:34.063\" xmlns=\"http://www.opentravel.org/OTA/2003/05\"> 	<POS> 		<Source> 			<RequestorID MessagePassword=\"8Abrejar9vad\" Type=\"1\" ID=\"Hubs1\"> 				<CompanyName>Hubs1</CompanyName> 			</RequestorID> 			<BookingChannel Type=\"7\"/> 		</Source> 	</POS> 	<HotelDescriptiveContents> 		<HotelDescriptiveContent HotelCodeContext=\"Hubs1\" ChainCode=\"Hubs1\" HotelCode=\"Hubs1Hotel\" HotelName=\"Hubs1 Test Hotel\" ChainName=\"Hubs1\" CurrencyCode=\"CNY\" TimeZone=\"8\" LanguageCode=\"EN\"> 			<HotelInfo> 				<RelativePositions> 					<RelativePosition> 						<Transportations> 							<Transportation> 								<Descriptions> 									<Description Language=\"EN\"> 										<Text>English Directions.</Text> 									</Description> 									<Description Language=\"FR\"> 										<Text>French Directions.</Text> 									</Description> 									<Description Language=\"DE\"> 										<Text>German Directions.</Text> 									</Description> 									<Description Language=\"IT\"> 										<Text>Italian Directions.</Text> 									</Description> 									<Description Language=\"ES\"> 										<Text>Spanish Directions.</Text> 									</Description> 								</Descriptions> 							</Transportation> 						</Transportations> 					</RelativePosition> 				</RelativePositions> 				<CategoryCodes> 					<LocationCategory Removal=\"false\" Code=\"1\" CodeDetail=\"String\"/> 					<SegmentCategory Removal=\"false\" Code=\"1\" CodeDetail=\"String\"/> 					<HotelCategory Removal=\"false\" Code=\"1\" CodeDetail=\"String\"/> 					<ArchitecturalStyle Removal=\"false\" Code=\"1\" CodeDetail=\"String\"/> 					<GuestRoomInfo Removal=\"false\" Code=\"1\" Quantity=\"1\" CodeDetail=\"String\"> 						<RateRanges> 							<RateRange MinRate=\"3.141\" End=\"2012-08-13\" DecimalPlaces=\"1\" RateTimeUnit=\"Year\" Duration=\"Text\" MaxRate=\"3.141\" Start=\"2008-08-13\" FixedRate=\"3.141\" CurrencyCode=\"CNY\"/> 						</RateRanges> 						<Description ParagraphNumber=\"1\" LastModifierID=\"String\" Name=\"String\" LastModifyDateTime=\"2001-12-17T09:30:47.0Z\" CreateDateTime=\"2001-12-17T09:30:47.0Z\" Language=\"en-us\" CreatorID=\"String\"> 							<Text Formatted=\"true\" Language=\"en-us\">String</Text> 						</Description> 					</GuestRoomInfo> 				</CategoryCodes> 				<Descriptions> 					<Description Language=\"EN\"> 						<Text>English Hotel Description</Text> 						<URL>http://www.hotelwebsite.com/description</URL> 					</Description> 					<Description Language=\"FR\"> 						<Text>French Hotel Description</Text> 						<URL>http://www.hotelwebsite.com/description</URL> 					</Description> 					<Description Language=\"DE\"> 						<Text>German Hotel Description</Text> 						<URL>http://www.hotelwebsite.com/description</URL> 					</Description> 					<Description Language=\"IT\"> 						<Text>Italian Hotel Description</Text> 						<URL>http://www.hotelwebsite.com/description</URL> 					</Description> 					<Description Language=\"ES\"> 						<Text>Spanish Hotel Description</Text> 						<URL>http://www.hotelwebsite.com/description</URL> 					</Description> 				</Descriptions> 				<Position Latitude=\"53.47490000000\" Longitude=\"-2.25120000000\"/> 				<Services> 					<Service Code=\"148\"> 						<OperationSchedules> 							<OperationSchedule> 								<OperationTimes> 									<OperationTime Mon=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 									<OperationTime Tue=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 									<OperationTime Wed=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 									<OperationTime Thur=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 									<OperationTime Fri=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 									<OperationTime Sat=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 									<OperationTime Sun=\"true\" Start=\"06:00:00\" End=\"18:00:00\"/> 								</OperationTimes> 							</OperationSchedule> 						</OperationSchedules> 						<Description> 							<Text>ReservationDesk</Text> 						</Description> 					</Service> 				</Services> 			</HotelInfo> 			<FacilityInfo> 				<GuestRooms> 					<GuestRoom ID=\"1\" Code=\"1\" Quantity=\"10\" MaxOccupancy=\"1\"> 						<TypeRoom StandardNumBeds=\"1\" StandardOccupancy=\"1\" MaxRollaways=\"1\" Configuration=\"double\"/> 						<Amenities> 							<Amenity RoomAmenityCode=\"140\" CodeDetail=\"Pets Allowed\"/> 						</Amenities> 						<Description> 							<Text>Test room 1</Text> 						</Description> 					</GuestRoom> 				</GuestRooms> 			</FacilityInfo> 			<Policies> 				<Policy> 					<PolicyInfo CheckInTime=\"14:00:00\" CheckOutTime=\"12:00:00\"/> 				</Policy> 			</Policies> 			<AreaInfo> 				<Recreations> 					<Recreation Code=\"7\"/> 				</Recreations> 			</AreaInfo> 			<AffiliationInfo> 				<Awards> 					<Award Provider=\"AA\" Rating=\"4\"/> 				</Awards> 			</AffiliationInfo> 			<ContactInfos> 				<ContactInfo> 					<Names> 						<Name CodeDetail=\"1\"> 							<GivenName>John</GivenName> 							<Surname>Smith</Surname> 							<JobTitle>Reservations manager</JobTitle> 						</Name> 						<Name CodeDetail=\"2\"> 							<GivenName>Bertie</GivenName> 							<Surname>Bear</Surname> 							<JobTitle>Invoice manager</JobTitle> 						</Name> 						<Name CodeDetail=\"3\"> 							<GivenName>Paul</GivenName> 							<Surname>Jones</Surname> 							<JobTitle>Accounts Manager</JobTitle> 						</Name> 					</Names> 					<Addresses> 						<Address UseType=\"7\"> 							<AddressLine>303 Deansgate</AddressLine> 							<AddressLine>Address Line 2</AddressLine> 							<CityName>Manchester</CityName> 							<PostalCode>M3 4LQ</PostalCode> 							<StateProv>NA</StateProv> 							<CountryName>UK</CountryName> 						</Address> 						<Address UseType=\"3\"> 							<AddressLine>305 Deansgate</AddressLine> 							<AddressLine>Address Line 2</AddressLine> 							<CityName>Manchester</CityName> 							<PostalCode>M3 4LQ</PostalCode> 							<StateProv>NA</StateProv> 							<CountryName Code=\"GB\">UK</CountryName> 						</Address> 					</Addresses> 					<Phones> 						<Phone CountryAccessCode=\"+44\" PhoneNumber=\"1618701601\" PhoneTechType=\"1\" PhoneLocationType=\"3\"/> 						<Phone CountryAccessCode=\"+44\" PhoneNumber=\"1618701652\" PhoneTechType=\"3\" PhoneLocationType=\"3\"/> 						<Phone CountryAccessCode=\"+44\" PhoneNumber=\"1618701653\" PhoneTechType=\"5\" PhoneLocationType=\"3\"/> 						<Phone CountryAccessCode=\"+44\" PhoneNumber=\"1618701654\" PhoneTechType=\"5\" PhoneLocationType=\"3\"/> 						<Phone CountryAccessCode=\"+44\" PhoneNumber=\"1618701655\" PhoneTechType=\"3\" PhoneLocationType=\"8\"/> 					</Phones> 					<Emails> 						<!-- EmailType 6 for Billing, 7 for reservations --> 						<Email EmailType=\"6\">jenny.chen@hubs1.net</Email> 						<Email EmailType=\"7\">jenny.chen@hubs1.net</Email> 					</Emails> 				</ContactInfo> 			</ContactInfos> 			<TPA_Extensions> 				<MaxChildAge Value=\"8\"/> 			</TPA_Extensions> 		</HotelDescriptiveContent> 	</HotelDescriptiveContents> </OTA_HotelDescriptiveContentNotifRQ> ";
		HttpClientService_v3 clientServiceV3 = new HttpClientService_v3();
		try {
			String result = clientServiceV3.postHttpRequest(
					"http://lrconnect.providers.sandbox.laterooms.com/index.aspx?provider=Hubs1", newXml);
			System.out.println(result);
		} catch (TimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
