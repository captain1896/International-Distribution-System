package com.thayer.idsservice.task.update.bean;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class ErrorBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1873160286936920958L;

	private String msgId;

	private String erroCode;

	private String errorDesc;// 错误原因

	private String xml;

	private TProp tProp;

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public TProp gettProp() {
		return tProp;
	}

	public void settProp(TProp tProp) {
		this.tProp = tProp;
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
		if (StringUtils.hasText(xml)) {
			xml = xml.replaceAll("<", "＜");
			xml = xml.replaceAll(">", "＞");
		}
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId
	 *            the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
