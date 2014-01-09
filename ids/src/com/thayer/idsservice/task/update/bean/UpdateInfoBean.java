package com.thayer.idsservice.task.update.bean;

import java.io.Serializable;

import com.thayer.fog2.entity.AvailAllow;

public class UpdateInfoBean implements Serializable {

	private String type;

	private String requestXml;

	private String responseXml;

	private String reqestURL;

	private AvailAllow availAllow;

	/**
	 * cache key
	 */
	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public String getResponseXml() {
		return responseXml;
	}

	public void setResponseXml(String responseXml) {
		this.responseXml = responseXml;
	}

	public String getReqestURL() {
		return reqestURL;
	}

	public void setReqestURL(String reqestURL) {
		this.reqestURL = reqestURL;
	}

	/**
	 * @return the availAllow
	 */
	public AvailAllow getAvailAllow() {
		return availAllow;
	}

	/**
	 * @param availAllow
	 *            the availAllow to set
	 */
	public void setAvailAllow(AvailAllow availAllow) {
		this.availAllow = availAllow;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
