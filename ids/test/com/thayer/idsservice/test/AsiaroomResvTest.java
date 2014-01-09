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

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.HttpClientService_v3;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-24<br>
 * @version : v1.0
 */
public class AsiaroomResvTest extends AbstractDependencyInjectionSpringContextTests {
	public void testCreateHotel() {

		String newXml = "<OTA_HotelResRQ xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" EchoToken=\"75e4c908-3dd6-449d-9d1c-c1303636fbec\" TimeStamp=\"2011-06-10T03:58:54.1054576+01:00\" Version=\"3.0\" ResStatus=\"Initiate\" xmlns=\"http://www.opentravel.org/OTA/2003/05\"><POS><Source><RequestorID MessagePassword=\"8Abrejar9vad\" Type=\"1\" ID=\"Hubs1\" /><BookingChannel Type=\"7\" /></Source></POS><HotelReservations><HotelReservation><RoomStays><RoomStay><RoomTypes><RoomType RoomTypeCode=\"STD\" NonSmoking=\"true\" NumberOfUnits=\"1\" /></RoomTypes><RatePlans><RatePlan RatePlanCode=\"BAR\" /></RatePlans><RoomRates><RoomRate BookingCode=\"BAR\"><Rates><Rate><Base AmountBeforeTax=\"1900.00\" CurrencyCode=\"CNY\" DecimalPlaces=\"2\" /><Total AmountBeforeTax=\"1900.00\" AmountAfterTax=\"1900.00\" CurrencyCode=\"CNY\" DecimalPlaces=\"2\" /></Rate></Rates></RoomRate></RoomRates><GuestCounts IsPerRoom=\"true\"><GuestCount AgeQualifyingCode=\"10\" Count=\"2\" /></GuestCounts><Guarantee GuaranteeCode=\"CC\" GuaranteeType=\"CC/DC/Voucher\"><GuaranteesAccepted><GuaranteeAccepted><PaymentCard CardType=\"1\" CardCode=\"VI\" CardNumber=\"4297690021015146\" SeriesCode=\"845\" ExpireDate=\"1020\"><CardHolderName>jenny chen</CardHolderName></PaymentCard></GuaranteeAccepted></GuaranteesAccepted></Guarantee><BasicPropertyInfo HotelCode=\"4014\" /><ResGuestRPHs><ResGuestRPH RPH=\"1\" /></ResGuestRPHs><SpecialRequests><SpecialRequest Language=\"en\"><Text Language=\"en\">agegwere</Text></SpecialRequest></SpecialRequests></RoomStay><RoomStay><RoomTypes><RoomType RoomTypeCode=\"STD\" NonSmoking=\"true\" NumberOfUnits=\"1\" /></RoomTypes><RatePlans><RatePlan RatePlanCode=\"BAR\" /></RatePlans><RoomRates><RoomRate BookingCode=\"BAR\"><Rates><Rate><Base AmountBeforeTax=\"1900.00\" CurrencyCode=\"CNY\" DecimalPlaces=\"2\" /><Total AmountBeforeTax=\"1900.00\" AmountAfterTax=\"1900.00\" CurrencyCode=\"CNY\" DecimalPlaces=\"2\" /></Rate></Rates></RoomRate></RoomRates><GuestCounts IsPerRoom=\"true\"><GuestCount AgeQualifyingCode=\"10\" Count=\"2\" /></GuestCounts><Guarantee GuaranteeCode=\"CC\" GuaranteeType=\"CC/DC/Voucher\"><GuaranteesAccepted><GuaranteeAccepted><PaymentCard CardType=\"1\" CardCode=\"VI\" CardNumber=\"4297690021015146\" SeriesCode=\"845\" ExpireDate=\"1020\"><CardHolderName>jenny chen</CardHolderName></PaymentCard></GuaranteeAccepted></GuaranteesAccepted></Guarantee><BasicPropertyInfo HotelCode=\"4014\" /><ResGuestRPHs><ResGuestRPH RPH=\"2\" /></ResGuestRPHs><SpecialRequests><SpecialRequest Language=\"en\"><Text Language=\"en\">dsgewgwge</Text></SpecialRequest></SpecialRequests></RoomStay></RoomStays><ResGuests><ResGuest ResGuestRPH=\"1\"><Profiles><ProfileInfo><Profile><Customer><PersonName><GivenName>Jenny</GivenName><Surname>Chen</Surname></PersonName><Telephone PhoneNumber=\"862161226688\" /><Email>jenny.chen@hubs1.net</Email><Address Type=\"1\"><AddressLine>1277 Beijing Road W</AddressLine><AddressLine>jingan</AddressLine><CityName>Shanghai</CityName><PostalCode /><CountryName Code=\"CN\">China</CountryName></Address></Customer></Profile></ProfileInfo></Profiles></ResGuest><ResGuest ResGuestRPH=\"2\"><Profiles><ProfileInfo><Profile><Customer><PersonName><GivenName>Jean</GivenName><Surname>test</Surname></PersonName><Telephone PhoneNumber=\"862161226688\" /><Email>jenny.chen@hubs1.net</Email><Address Type=\"1\"><AddressLine>1277 Beijing Road W</AddressLine><AddressLine>jingan</AddressLine><CityName>Shanghai</CityName><PostalCode /><CountryName Code=\"CN\">China</CountryName></Address></Customer></Profile></ProfileInfo></Profiles></ResGuest></ResGuests><ResGlobalInfo><GuestCounts><GuestCount AgeQualifyingCode=\"10\" Count=\"4\" /></GuestCounts><TimeSpan Start=\"2011-06-23\" Duration=\"P1D\" /><Guarantee GuaranteeCode=\"CC\" GuaranteeType=\"CC/DC/Voucher\"><GuaranteesAccepted><GuaranteeAccepted><PaymentCard CardType=\"1\" CardCode=\"VI\" CardNumber=\"4297690021015146\" SeriesCode=\"845\" ExpireDate=\"1020\"><CardHolderName>jenny chen</CardHolderName></PaymentCard></GuaranteeAccepted></GuaranteesAccepted></Guarantee><HotelReservationIDs><HotelReservationID ResID_Type=\"5\" ResID_Value=\"12722677Y\" ResID_Source=\"Laterooms\" ResID_Date=\"2011-06-23T00:00:00\" /></HotelReservationIDs></ResGlobalInfo></HotelReservation></HotelReservations></OTA_HotelResRQ>";
		HttpClientService_v3 clientServiceV3 = new HttpClientService_v3();
		try {
			String result = clientServiceV3.postHttpRequest(
					"http://222.73.234.174:8580/Hub1HotelService/web/asiarooms/receive.do", newXml);
			System.out.println("return xml :" + result);
		} catch (TimeOutException e) {
			e.printStackTrace();
		} catch (BizException e) {
			e.printStackTrace();
		}
	}
}
