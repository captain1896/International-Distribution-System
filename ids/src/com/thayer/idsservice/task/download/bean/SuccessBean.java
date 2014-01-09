package com.thayer.idsservice.task.download.bean;

import java.io.Serializable;

import com.thayer.idsservice.bean.MailFogResvBaseBean;

public class SuccessBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5744767968932497200L;
	private TResvBase tResvBase;
	private String cnfnum;
	private String ids_cnfnum;
	private String remark;
	private String idsProp;
	private String fogProp;
	private String thirdIataCode;
	private String username;
	private String password;

	private MailFogResvBaseBean mailFogResvBaseBean;

	public MailFogResvBaseBean getMailFogResvBaseBean() {
		return mailFogResvBaseBean;
	}

	public void setMailFogResvBaseBean(MailFogResvBaseBean mailFogResvBaseBean) {
		this.mailFogResvBaseBean = mailFogResvBaseBean;
	}

	public TResvBase gettResvBase() {
		return tResvBase;
	}

	public void settResvBase(TResvBase tResvBase) {
		this.tResvBase = tResvBase;
	}

	public String getCnfnum() {
		return cnfnum;
	}

	public void setCnfnum(String cnfnum) {
		this.cnfnum = cnfnum;
	}

	public String getIds_cnfnum() {
		return ids_cnfnum;
	}

	public void setIds_cnfnum(String idsCnfnum) {
		ids_cnfnum = idsCnfnum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdsProp() {
		return idsProp;
	}

	public void setIdsProp(String idsProp) {
		this.idsProp = idsProp;
	}

	public String getFogProp() {
		return fogProp;
	}

	public void setFogProp(String fogProp) {
		this.fogProp = fogProp;
	}

	/**
	 * @return the thirdIataCode
	 */
	public String getThirdIataCode() {
		return thirdIataCode;
	}

	/**
	 * @param thirdIataCode
	 *            the thirdIataCode to set
	 */
	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
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
