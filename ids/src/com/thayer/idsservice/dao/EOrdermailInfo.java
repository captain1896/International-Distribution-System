package com.thayer.idsservice.dao;

import java.util.Date;

public class EOrdermailInfo implements java.io.Serializable {
	private Long omid;
	private String cnfNum;
	private String outcnfNum;
	private String resvType;
	private String prop;
	private Date bookedTime;
	private Date inDate;
	private String idsType;
	private String result;
	private String errCode;
	private String errMsg;
	
	
	public EOrdermailInfo() {
		super();
	}
	public EOrdermailInfo(Long omid, String cnfNum, String outcnfNum,
			String resvType, String prop, Date bookedTime, Date inDate,
			String idsType, String result, String errCode, String errMsg) {
		super();
		this.omid = omid;
		this.cnfNum = cnfNum;
		this.outcnfNum = outcnfNum;
		this.resvType = resvType;
		this.prop = prop;
		this.bookedTime = bookedTime;
		this.inDate = inDate;
		this.idsType = idsType;
		this.result = result;
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	public Long getOmid() {
		return omid;
	}
	public void setOmid(Long omid) {
		this.omid = omid;
	}
	public String getCnfNum() {
		return cnfNum;
	}
	public void setCnfNum(String cnfNum) {
		this.cnfNum = cnfNum;
	}
	public String getOutcnfNum() {
		return outcnfNum;
	}
	public void setOutcnfNum(String outcnfNum) {
		this.outcnfNum = outcnfNum;
	}
	public String getResvType() {
		return resvType;
	}
	public void setResvType(String resvType) {
		this.resvType = resvType;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public Date getBookedTime() {
		return bookedTime;
	}
	public void setBookedTime(Date bookedTime) {
		this.bookedTime = bookedTime;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getIdsType() {
		return idsType;
	}
	public void setIdsType(String idsType) {
		this.idsType = idsType;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
