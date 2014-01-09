package com.thayer.idsservice.test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.thayer.idsservice.ids.agoda.bean.GetBookingLists;
import com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument;
import com.thayer.idsservice.ids.agoda.bean.HeaderDataDocument.HeaderData;
import com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument.YCSXML;

public class TestXmlParse {
	private static int i = 0;

	public TestXmlParse() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Properties initProp = new Properties(System.getProperties());
		System.out.println("file.encoding: " + initProp.getProperty("file.encoding"));
		System.out.println("file.encoding: " + initProp.getProperty("user.language"));

		// String
		// str="<?xml version=\"1.0\" encoding=\"UTF-8\"?><YCS_XML>	<BookingDetailData>		<BookingID>3600690</BookingID>		<HotelID>71656</HotelID>		<AckID>Auto Ack via email link</AckID>		<CheckInDate>2010-04-01</CheckInDate>		<CheckOutDate>2010-04-04</CheckOutDate>		<NoOfNight>3</NoOfNight>		<NoOfRoom>2</NoOfRoom>		<Adult>2</Adult>		<Child>0</Child>		<ExtraBed>0</ExtraBed>		<BreakfastIncluded>No</BreakfastIncluded>		<Hotel>X Hotel</Hotel>		<RoomTypeID>149722</RoomTypeID>		<RoomName>Deluxe</RoomName>		<RoomDescription/>		<BedType>N/A</BedType>		<Rates LocalCurrency=\"THB\">			<RoomRate Total=\"6000.00\">				<Rate Date=\"2010-04-01\">2200.00</Rate>				<Rate Date=\"2010-04-02\">2000.00</Rate>				<Rate Date=\"2010-04-03\">1800.00</Rate>			</RoomRate>		</Rates>		<UPC>			<IATAID>96-6 2709 0</IATAID>			<Number>5529420035631440</Number>			<Expired>10/2010</Expired>			<CardType>Master</CardType>			<CVV>189</CVV>		</UPC>		<CustomerID>1026169</CustomerID>		<FirstName>Christ</FirstName>		<LastName>Tomas</LastName>		<PhoneNumber>+6624552452</PhoneNumber>		<Email>christ@gmail.com</Email>		<Residency/>		<Smoking>NO</Smoking>		<Floor>No Preference</Floor>		<Nationality>Thailand</Nationality>		<CustomerRequest>need connected rooms</CustomerRequest>		<Promotion/>		<RequestedDate>2010-03-15</RequestedDate>	</BookingDetailData></YCS_XML>";
		// try {
		// YCSXMLDocument newInstance = YCSXMLDocument.Factory.newInstance();
		// newInstance.addNewYCSXML().addNewBookingDetailData().addNewRates().addNewRoomRate().addNewRate().setDate("2010-02-01");
		// System.out.println(newInstance.toString());
		// System.out.println(YCSXMLDocument.Factory.parse(str));
		// YCSXMLDocument parse = YCSXMLDocument.Factory.parse(str);
		// String
		// tmp=parse.getYCSXML().getBookingDetailData().getRates().getRoomRate().getRateArray()[0].getDomNode().getTextContent();
		// System.out.println(tmp);
		// } catch (XmlException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// String str =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><YCS_XML><GetBooking><HeaderData><UserID>testuser</UserID><Password>testpwd</Password><HotelID>71656</HotelID></HeaderData><BookingID>3600690</BookingID></GetBooking></YCS_XML>";
		try {

			YCSXMLDocument document = YCSXMLDocument.Factory.newInstance();
			YCSXML ycsXml = document.addNewYCSXML();
			GetBookingLists bkList = ycsXml.addNewGetBookingLists();
			HeaderData head = bkList.addNewHeaderData();
			head.setHotelID(new BigInteger("71656"));
			head.setUserID("xml004");
			head.setPassword("xml004*");
			bkList.setAction(new BigInteger("0"));
			Map<String, String> parms = new HashMap<String, String>();
			String XMLRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + document.toString();
			parms.put("Message", XMLRequest);
			System.out.println(XMLRequest);
			// String response = HttpClientUtils.postMessage_V3("http://sandbox.xml.ycs.agoda.com/YCS_APIService.aspx",
			// parms);
			// String response = HttpClientUtils.postHttpRequest("http://192.168.19.138:8081/Hubs1PayTest/index.jsp",
			// parms);
			// System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
