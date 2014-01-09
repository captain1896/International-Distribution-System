package com.thayer.idsservice.task.download.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thayer.idsservice.util.DateUtil;

public class ResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4699785058335784590L;

	private String iata;

	private String iataName;

	private String thirdIataCode;

	private List<ErrorBean> errorlist = new ArrayList<ErrorBean>();

	private List<SuccessBean> successlist = new ArrayList<SuccessBean>();

	private String createDate;

	private String username;

	private String password;

	public String getThirdIataCode() {
		return thirdIataCode;
	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
	}

	public ResultBean() {
		createDate = DateUtil.formatDateTime(new Date());
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getIataName() {
		return iataName;
	}

	public void setIataName(String iataName) {
		this.iataName = iataName;
	}

	public List<ErrorBean> getErrorlist() {
		return errorlist;
	}

	public void setErrorlist(List<ErrorBean> errorlist) {
		this.errorlist = errorlist;
	}

	public List<SuccessBean> getSuccesslist() {
		return successlist;
	}

	public void setSuccesslist(List<SuccessBean> successlist) {
		this.successlist = successlist;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
