package com.thayer.idsservice.dao;

import java.util.Date;

/**
 * AbstractEMsgLog entity provides the base persistence definition of the EMsgLog entity. @author MyEclipse Persistence
 * Tools
 */

public abstract class AbstractEMsgLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private String exwebCode;
	private String msgId;
	private String msgStatus;
	private String msgRq;
	private String msgRs;
	private String msgRsFinal;
	private String msgErrorcode;
	private String msgError;
	private String expropId;
	private String fogpropId;
	private Date createtime;
	private Date updatetime;

	// Constructors

	/** default constructor */
	public AbstractEMsgLog() {
	}

	/** minimal constructor */
	public AbstractEMsgLog(String exwebCode, Date createtime, Date updatetime) {
		this.exwebCode = exwebCode;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	/** full constructor */
	public AbstractEMsgLog(String exwebCode, String msgId, String msgStatus, String msgRq, String msgRs, String msgRsFinal, String msgErrorcode, String msgError, Date createtime, Date updatetime) {
		this.exwebCode = exwebCode;
		this.msgId = msgId;
		this.msgStatus = msgStatus;
		this.msgRq = msgRq;
		this.msgRs = msgRs;
		this.msgRsFinal = msgRsFinal;
		this.msgErrorcode = msgErrorcode;
		this.msgError = msgError;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExwebCode() {
		return this.exwebCode;
	}

	public void setExwebCode(String exwebCode) {
		this.exwebCode = exwebCode;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgStatus() {
		return this.msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public String getMsgRq() {
		return this.msgRq;
	}

	public void setMsgRq(String msgRq) {
		this.msgRq = msgRq;
	}

	public String getMsgRs() {
		return this.msgRs;
	}

	public void setMsgRs(String msgRs) {
		this.msgRs = msgRs;
	}

	public String getMsgRsFinal() {
		return this.msgRsFinal;
	}

	public void setMsgRsFinal(String msgRsFinal) {
		this.msgRsFinal = msgRsFinal;
	}

	public String getMsgErrorcode() {
		return this.msgErrorcode;
	}

	public void setMsgErrorcode(String msgErrorcode) {
		this.msgErrorcode = msgErrorcode;
	}

	public String getMsgError() {
		return this.msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getExpropId() {
		return expropId;
	}

	public void setExpropId(String expropId) {
		this.expropId = expropId;
	}

	public String getFogpropId() {
		return fogpropId;
	}

	public void setFogpropId(String fogpropId) {
		this.fogpropId = fogpropId;
	}

}