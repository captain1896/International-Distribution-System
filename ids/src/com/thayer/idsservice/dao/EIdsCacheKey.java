package com.thayer.idsservice.dao;

import java.util.Date;

public class EIdsCacheKey implements java.io.Serializable{
	private static final long serialVersionUID = 9062939135071594408L;
	private String idsType;
	private String planCode;
	private String prop;
	private Date availDate;
	public EIdsCacheKey(){
		
	}
	public EIdsCacheKey(String idsType,String planCode,String prop,Date availDate){
		this.idsType = idsType;
		this.planCode = planCode;
		this.prop = prop;
		this.availDate = availDate;
	}
	public String getIdsType() {
		return idsType;
	}
	public void setIdsType(String idsType) {
		this.idsType = idsType;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public Date getAvailDate() {
		return availDate;
	}
	public void setAvailDate(Date availDate) {
		this.availDate = availDate;
	}

}
