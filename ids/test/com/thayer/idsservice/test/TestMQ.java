package com.thayer.idsservice.test;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.thayer.fog2.entity.Prop;
import com.thayer.fogservice.webservice.crs.AvailExchangeRateWebService;
import com.thayer.fogservice.webservice.crs.PropWebService;

public class TestMQ extends AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {

		return new String[] { "applicationContext-ids-init-test.xml", "applicationContext-ids-fogWSClient.xml" };
	}

	public void testDownload() {
		AvailExchangeRateWebService availExchangeRateWebService = (AvailExchangeRateWebService) this
				.getApplicationContext().getBean("fogServiceWSClient");
		PropWebService propWebService = (PropWebService) this.getApplicationContext().getBean("fogServicePropWSClient");
		try {

			Prop propinfo = propWebService.findPropById("4014");
			System.out.println(propinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
