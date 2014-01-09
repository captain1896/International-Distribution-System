package com.thayer.idsservice.task.download.bean;

import java.util.Date;

public class HotelBean {

	private String fogHotelCode;

	private String thirdHotelCode;

	private String fogIataCode;

	private String thirdIataCode;

	private Date requestSince;

	private Date requestTill;

	private String username;

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFogHotelCode() {
		return fogHotelCode;
	}

	public void setFogHotelCode(String fogHotelCode) {
		this.fogHotelCode = fogHotelCode;
	}

	public String getThirdHotelCode() {
		return thirdHotelCode;
	}

	public void setThirdHotelCode(String thirdHotelCode) {
		this.thirdHotelCode = thirdHotelCode;
	}

	public String getFogIataCode() {
		return fogIataCode;
	}

	public void setFogIataCode(String fogIataCode) {
		this.fogIataCode = fogIataCode;
	}

	public String getThirdIataCode() {
		return thirdIataCode;
	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
	}

	public Date getRequestSince() {
		return requestSince;
	}

	public void setRequestSince(Date requestSince) {
		this.requestSince = requestSince;
	}

	public Date getRequestTill() {
		return requestTill;
	}

	public void setRequestTill(Date requestTill) {
		this.requestTill = requestTill;
	}

}
