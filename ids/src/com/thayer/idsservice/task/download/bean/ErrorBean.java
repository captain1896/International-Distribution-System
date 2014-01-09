package com.thayer.idsservice.task.download.bean;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class ErrorBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -986745213969697411L;

	private String erroCode;

	private String errorDesc;// 错误原因

	private String errorMessage;

	private String ids_cnfnum;// 代理网站订单号

	private String xml;

	private String idsProp;

	private String fogProp;

	private TResvBase tResvBase;

	private boolean needFormat = true;

	public TResvBase gettResvBase() {
		return tResvBase;
	}

	public void settResvBase(TResvBase tResvBase) {
		this.tResvBase = tResvBase;
	}

	public String getErroCode() {
		return erroCode;
	}

	public void setErroCode(String erroCode) {
		this.erroCode = erroCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getXml() {
		String xml = this.xml;
		if (StringUtils.hasText(xml) && needFormat) {
			xml = xml.replaceAll("<", "＜");
			xml = xml.replaceAll(">", "＞");
		}
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getIds_cnfnum() {
		return ids_cnfnum;
	}

	public void setIds_cnfnum(String idsCnfnum) {
		ids_cnfnum = idsCnfnum;
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
	 * @return the needFormat
	 */
	public boolean isNeedFormat() {
		return needFormat;
	}

	/**
	 * @param needFormat
	 *            the needFormat to set
	 */
	public void setNeedFormat(boolean needFormat) {
		this.needFormat = needFormat;
	}
}
