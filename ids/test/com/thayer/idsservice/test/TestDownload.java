package com.thayer.idsservice.test;

import java.util.Calendar;
import java.util.Date;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.interf.IDownLoadService;

public class TestDownload extends AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {

		return new String[] { "applicationContext-dao.xml", "applicationContext-ids-init-test.xml",
				"applicationContext-service-configure.xml", "applicationContext-utils.xml" };
	}

	public void testDownload() throws MappingException {
		IDownLoadService downloadResvTask = (IDownLoadService) this.getApplicationContext().getBean(
				"agoda_downLoadService");
		ResultBean resultBean = new ResultBean();
		resultBean.setIata("970003");
		HotelBean hotelBean = new HotelBean();
		hotelBean.setFogIataCode("960007");
		hotelBean.setThirdHotelCode("71656");
		hotelBean.setFogHotelCode("1064");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		hotelBean.setRequestSince(cal.getTime());
		hotelBean.setRequestTill(new Date());
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
		}

	}

}
