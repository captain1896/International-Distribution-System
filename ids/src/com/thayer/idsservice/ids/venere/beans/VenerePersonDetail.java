package com.thayer.idsservice.ids.venere.beans;


/**   
 * @{#} XhiPersonDetail.java Create on Apr 9, 2008 1:08:20 PM   
 *   
 * Copyright (c) 2007 by HUBS1.   
 *  
 * @author <a href="mailto:tony.li@hubs1.net">Tony Li</a>  
 * @version 1.0 
 * Description ：Xhi解析后的入住客户信息类
 */   
public class VenerePersonDetail {
	//First Name
	private String firstName;
	//Last Name
	private String lastName;
	//电话
	private String phoneNumber;
	//E-mail
	private String email;
	//城市
	private String cityName;
	//国家
	private String countryName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
}
