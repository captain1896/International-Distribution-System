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
public class GenaresTest extends AbstractDependencyInjectionSpringContextTests {
	public void testCreateHotel() {

		String newXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"> 	<soap:Header> 		<wsa:MessageID>uuid:ec0f34b7-f10d-4df4-92ac-294480751a7b</wsa:MessageID> 		<wsa:ReplyTo> 			<wsa:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:Address> 		</wsa:ReplyTo> 		<wsa:To>https://www.genares.net/cgi/interfaces/ota/ota_server</wsa:To> 		<wsa:Action>OTAHotelBookingRuleNotifRQ</wsa:Action> 		<wsse:Security soap:mustUnderstand=\"1\"> 			<wsu:Timestamp wsu:Id=\"Timestamp-1e2f74c4-c9e9-45dd-bc53-e9e0162da003\"> 				<wsu:Created>2011-03-28 09:36:01</wsu:Created> 				<wsu:Expires>2011-03-28 09:41:01</wsu:Expires> 			</wsu:Timestamp> 			<wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"SecurityToken-08ec41dc-8177-489c-9514-c5573eec2db6\"> 				<wsse:Username>HUBS1</wsse:Username> 				<wsse:Password Type=\"PasswordText\">HUBS1</wsse:Password> 			</wsse:UsernameToken> 		</wsse:Security> 	</soap:Header> 	<soap:Body> 		<OTA_HotelBookingRuleNotifRQ Version=\"1.100\" xmlns=\"http://www.opentravel.org/OTA/2003/05\"> 			<RuleMessages HotelCode=\"8482\"> 				<RuleMessage> 					<StatusApplicationControl InvTypeCode=\"KING\" RatePlanCode=\"RACK\"/> 					<BookingRules> 						<BookingRule Start=\"2011-05-01\" End=\"2011-05-08\" MinAdvancedBookingOffset=\"P0Y0M0DT24H0M0S\"/> 					</BookingRules> 				</RuleMessage> 			</RuleMessages> 		</OTA_HotelBookingRuleNotifRQ> 	</soap:Body> </soap:Envelope> ";
		HttpClientService_v3 clientServiceV3 = new HttpClientService_v3();
		try {
			String result = clientServiceV3.postHttpRequest(
					"http://interface.qa.genares.net/cgi/interfaces/ota/hubs1_server", newXml);
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
