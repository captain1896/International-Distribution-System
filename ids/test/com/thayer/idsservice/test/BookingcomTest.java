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
public class BookingcomTest extends AbstractDependencyInjectionSpringContextTests {
	@Override
	protected String[] getConfigLocations() {

		return new String[] { "applicationContext-dao.xml", "applicationContext-ids-init-test.xml",
				"applicationContext-service-configure.xml", "applicationContext-ids-fogWSClient.xml",
				"applicationContext-utils.xml" };
	}

	public void tttestdownloadTask() {
		ResvDownloadBaseTask venereDljobDetail = (ResvDownloadBaseTask) this.getApplicationContext().getBean(
				"bookingcomDljobDetail");
		try {
			venereDljobDetail.executeInternal(null);
		} catch (JobExecutionException e) {

			e.printStackTrace();
		}
	}

	public void testdownload() {
		IDownLoadService downloadResvTask = (IDownLoadService) this.getApplicationContext().getBean(
				"bookingcom_downLoadService");
		ResultBean resultBean = new ResultBean();
		resultBean.setIata("970003");
		HotelBean hotelBean = new HotelBean();
		hotelBean.setFogIataCode("970003");
		hotelBean.setThirdHotelCode("14331");
		hotelBean.setFogHotelCode("1074");
		hotelBean.setThirdIataCode("BOOKINGCOM");
		hotelBean.setRequestSince(DateUtil.dateValue("2011-05-07 07:30:13"));
		hotelBean.setRequestTill(DateUtil.dateValue("2011-05-07 07:42:16"));
		try {
			downloadResvTask.downLoad(hotelBean, resultBean);
		} catch (BizException e) {

			e.printStackTrace();
		} catch (TimeOut4ThirdException e) {

			e.printStackTrace();
		} catch (TimeOut4FogException e) {

			e.printStackTrace();
		} catch (MappingException e) {

			e.printStackTrace();
		}
	}

}
