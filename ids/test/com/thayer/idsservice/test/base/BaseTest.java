package com.thayer.idsservice.test.base;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class BaseTest extends AbstractDependencyInjectionSpringContextTests {

	protected String[] getConfigLocations() {

		return new String[] { "applicationContext-dao.xml", "applicationContext-ids-init-test.xml",
		        "applicationContext-service-configure.xml", "applicationContext-ids-fogWSClient.xml",
		        "applicationContext-utils.xml","idsconfig/applicationContext-ids-service.xml" };
	}

	public BaseTest() {
		super();
		this.setAutowireMode(AbstractDependencyInjectionSpringContextTests.AUTOWIRE_BY_NAME);
	}

}
