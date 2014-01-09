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

import org.apache.xmlbeans.XmlException;
import org.opentravel.ota.x2003.x05.OTACancelRQDocument;
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
public class AsiaroomResvCancelTest extends AbstractDependencyInjectionSpringContextTests {
	public void testCreateHotel() {

		String newXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <OTA_CancelRQ xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://www.opentravel.org/OTA/2003/05\" Target=\"Test\" Version=\"1.002\" CancelType=\"Commit\"> 	<POS> 		<Source> 			<RequestorID MessagePassword=\"8Abrejar9vad\" Type=\"1\" ID=\"Hubs1\"/> 			<BookingChannel Type=\"7\"/> 		</Source> 	</POS> 	<UniqueID Type=\"14\" ID=\"401400039\"> 		<CompanyName>Hubs1</CompanyName> 	</UniqueID> 	<UniqueID Type=\"5\" ID=\"12724766R\"> 		<CompanyName>Laterooms</CompanyName> 	</UniqueID> 	<Verification> 		<PersonName> 			<GivenName>Simon</GivenName> 			<Surname>Dutton</Surname> 		</PersonName> 		<Vendor Code=\"2\">Hubs1</Vendor> 		<Vendor Code=\"3\">4014</Vendor> 		<ReservationTimeSpan Start=\"2011-07-19\" Duration=\"P1D\"/> 	</Verification> </OTA_CancelRQ> ";

		HttpClientService_v3 clientServiceV3 = new HttpClientService_v3();
		try {
			OTACancelRQDocument cancelRQDocument = OTACancelRQDocument.Factory.parse(newXml);
			String result = clientServiceV3.postHttpRequest(
					"http://222.73.234.174:8580/Hub1HotelService/web/asiarooms/receive.do", newXml);
			System.out.println("return xml :" + result);
		} catch (TimeOutException e) {
			e.printStackTrace();
		} catch (BizException e) {
			e.printStackTrace();
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
