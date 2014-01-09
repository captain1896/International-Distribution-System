package com.thayer.idsservice.test;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.interf.IDownLoadService;

public class TestGenaresDownload extends AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {

		return new String[] { "applicationContext-ids-init.xml", "applicationContext-service-configure.xml", "applicationContext-utils.xml" };
	}

	public void testDownload() {
		IDownLoadService downloadResvTask = (IDownLoadService) this.getApplicationContext().getBean("genares_downLoadService");
		ResultBean resultBean = new ResultBean();
		resultBean.setIata("970020");
		try {
			HotelBean hotelBean = new HotelBean();
			hotelBean.setThirdHotelCode("8482");
			downloadResvTask.downLoad(hotelBean, resultBean);
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

}
