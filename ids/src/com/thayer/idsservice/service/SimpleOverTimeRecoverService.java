package com.thayer.idsservice.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.service.interf.ITimeoutRecoverService;

/**
 * <B>Function :</B> 超时检测服务简单实现类<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-8<br>
 * @version : v1.0
 */
public class SimpleOverTimeRecoverService implements ITimeoutRecoverService {
	private IHttpClientService httpClientService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2773904065638374611L;
	/**
	 * 测试的url地址
	 */
	private String testServiceUrl;
	/**
	 * 重试次数. 默认5次
	 */
	private int testCount = 1;

	/**
	 * 测试间隔时间，默认5秒
	 */
	private int testIntervalTime = 10000;

	protected static transient Logger LOGGER = Logger.getLogger(SimpleOverTimeRecoverService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.ITimeoutRecoverService#isRecover()
	 */
	public boolean isRecover() {
		LOGGER.info("start test Url :" + testServiceUrl);
		int successCount = 0;
		while (true) {
			if (StringUtils.hasText(testServiceUrl)) {
				try {
					int testResult = httpClientService.callHttpRequest(testServiceUrl);
					LOGGER.info("start test Url :" + testServiceUrl + "Result = " + testResult);
					if (testResult == 200  || testResult == 400) {
						successCount++;
						LOGGER.info("test url :" + testServiceUrl + "success count:" + successCount);
						if (successCount > testCount) {
							LOGGER.info("test url :" + testServiceUrl + " success count >" + testCount
									+ " recover success!");
							return true;
						}
						Thread.sleep(testIntervalTime);
					}
				} catch (Exception e) {
					LOGGER.warn("test url :" + testServiceUrl + " error : \n" + ExceptionUtils.getFullStackTrace(e)
							+ " \n recover failed!");
					return false;
				}
			} else {
				LOGGER.error("test url is null!!!");
				return false;
			}
		}

	}

	public String getTestServiceUrl() {
		return testServiceUrl;
	}

	public void setTestServiceUrl(String testServiceUrl) {
		this.testServiceUrl = testServiceUrl;
	}

	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}

	public int getTestIntervalTime() {
		return testIntervalTime;
	}

	public void setTestIntervalTime(int testIntervalTime) {
		this.testIntervalTime = testIntervalTime;
	}

	public IHttpClientService getHttpClientService() {
		return httpClientService;
	}

	public void setHttpClientService(IHttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}

}
