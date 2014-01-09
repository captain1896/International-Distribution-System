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
public class AsiaroomAvailTest extends AbstractDependencyInjectionSpringContextTests {
	public void testAvailQuery() {

		HttpClientService_v3 clientServiceV3 = new HttpClientService_v3();
		try {
			String newXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <OTA_HotelAvailRQ xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" TimeStamp=\"2011-06-06T10:49:39.7378569+01:00\" Target=\"Test\" Version=\"1.003\" xmlns=\"http://www.opentravel.org/OTA/2003/05\"> 	<POS> 		<Source> 			<RequestorID MessagePassword=\"8Abrejar9vad\" Type=\"1\" ID=\"Hubs1\"/> 			<BookingChannel Type=\"7\"/> 		</Source> 	</POS> 	<AvailRequestSegments> 		<AvailRequestSegment> 			<StayDateRange Start=\"2011-06-20\" Duration=\"P3D\" End=\"2011-06-23\"/> 			<RoomStayCandidates> 				<RoomStayCandidate Quantity=\"1\"> 					<GuestCounts IsPerRoom=\"true\"> 						<GuestCount AgeQualifyingCode=\"10\" Count=\"3\"/> 					</GuestCounts> 				</RoomStayCandidate> 			</RoomStayCandidates> 			<HotelSearchCriteria> 				<Criterion> 					<HotelRef HotelCode=\"4014\"/> 				</Criterion> 			</HotelSearchCriteria> 		</AvailRequestSegment> 	</AvailRequestSegments> </OTA_HotelAvailRQ> ";
			String result = clientServiceV3.postHttpRequest(
					"http://222.73.234.174:8580/Hub1HotelService/web/asiarooms/receive.do", newXml);
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
