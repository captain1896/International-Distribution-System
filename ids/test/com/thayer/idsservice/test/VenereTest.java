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

import org.quartz.JobExecutionException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.HttpClientService_v3;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.task.download.ResvDownloadBaseTask;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.interf.IDownLoadService;
import com.thayer.idsservice.util.DateUtil;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-24<br>
 * @version : v1.0
 */
public class VenereTest extends AbstractDependencyInjectionSpringContextTests {
	@Override
	protected String[] getConfigLocations() {

		return new String[] { "applicationContext-dao.xml", "applicationContext-ids-init-test.xml",
				"applicationContext-service-configure.xml", "applicationContext-ids-fogWSClient.xml",
				"applicationContext-utils.xml" };
	}

	public void tes1tdownloadTask() {
		ResvDownloadBaseTask venereDljobDetail = (ResvDownloadBaseTask) this.getApplicationContext().getBean(
				"venereDljobDetail");
		try {
			venereDljobDetail.executeInternal(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void t2estdownload() {
		IDownLoadService downloadResvTask = (IDownLoadService) this.getApplicationContext().getBean(
				"venere_downLoadService");
		ResultBean resultBean = new ResultBean();
		resultBean.setIata("970013");
		HotelBean hotelBean = new HotelBean();
		hotelBean.setFogIataCode("970013");
		hotelBean.setThirdHotelCode("14331");
		hotelBean.setFogHotelCode("1074");
		hotelBean.setThirdIataCode("VENERE");
		hotelBean.setRequestSince(DateUtil.dateValue("2011-05-07 07:30:13"));
		hotelBean.setRequestTill(DateUtil.dateValue("2011-05-07 07:42:16"));
		try {
			downloadResvTask.downLoad(hotelBean, resultBean);
			// downloadResvTask.downLoad("65324", resultBean);
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeOut4ThirdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeOut4FogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testCreateHotel() {
		StringBuilder newXml = new StringBuilder();
		newXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		newXml
				.append("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		newXml.append("<Header><Authentication xmlns=\"http://www.venere.com/XHI\">");
		newXml.append("<UserOrgID>HUBS1</UserOrgID>");
		newXml.append("<UserID>XHI_Hubs1</UserID>");
		newXml.append("<UserPSW>HubsXhi0805504</UserPSW>");
		newXml.append("</Authentication>");
		newXml.append("</Header>");
		newXml.append("<Body>");

		newXml
				.append("<OTA_ReadRQ EchoToken=\"e065c0bc-df30-419e-b5d1-7fdc0144844f\" Target=\"Production\" Version=\"1.007\" ReturnListIndicator=\"true\" xmlns=\"http://www.opentravel.org/OTA/2003/05\"> 			<ReadRequests> 				<HotelReadRequest > 					<Verification> 						<ReservationTimeSpan Start=\"2011-05-10T00:00:00\" End=\"2011-05-10T13:05:16\"/> 					</Verification> 					<TPA_Extensions> 						<Venere xmlns=\"http://www.venere.com/XHI/TPA_Extensions\" ReservationDateTimeSpanIndicator=\"update\"/> 					</TPA_Extensions> 				</HotelReadRequest> 			</ReadRequests> 		</OTA_ReadRQ>");
		newXml.append("</Body>");
		newXml.append("</Envelope>");
		IHttpClientService clientServiceV3 = new HttpClientService_v3();
		System.out.println(newXml.toString());
		try {
			String result = clientServiceV3.postHttpRequest("https://xhi.venere.com/xhi-1.0/services/OTA_Read.soap",
					newXml.toString());
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
