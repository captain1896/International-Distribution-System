package com.thayer.idsservice.dao;

import java.util.Date;

public class EIdsCache implements java.io.Serializable{
	private static final long serialVersionUID = 9062939135071594408L;
	private EIdsCacheKey eIdsCacheKey;
	private String avail;
	private String weeks;
	private String currencyCode;
	private int allotment;
	private int minLos;
	private int maxLos;
	private int leadTime;
	private int breakfastNum;
	private double singleRate;
	private double doubleRate;
	private double tripleRate;
	private double quadRate;
	private Date createDate;
	private Date updateDate;
	
	
	public EIdsCache(){
		
	}
	/**
	 * booking.com
	 * @param idsType
	 * @param planCode
	 * @param prop
	 * @param availDate
	 * @param avail
	 * @param allotment
	 * @param minLos
	 */
	public EIdsCache(EIdsCacheKey eIdsCacheKey,String avail,int allotment,int minLos){
		this.eIdsCacheKey = eIdsCacheKey;
		this.avail = avail;
		this.allotment = allotment;
		this.minLos = minLos;
	}
	
	public EIdsCacheKey geteIdsCacheKey() {
		return eIdsCacheKey;
	}
	public void seteIdsCacheKey(EIdsCacheKey eIdsCacheKey) {
		this.eIdsCacheKey = eIdsCacheKey;
	}
	public String getAvail() {
		return avail;
	}
	public void setAvail(String avail) {
		this.avail = avail;
	}
	public String getWeeks() {
		return weeks;
	}
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public int getAllotment() {
		return allotment;
	}
	public void setAllotment(int allotment) {
		this.allotment = allotment;
	}
	public int getMinLos() {
		return minLos;
	}
	public void setMinLos(int minLos) {
		this.minLos = minLos;
	}
	public int getMaxLos() {
		return maxLos;
	}
	public void setMaxLos(int maxLos) {
		this.maxLos = maxLos;
	}
	public int getLeadTime() {
		return leadTime;
	}
	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}
	public double getSingleRate() {
		return singleRate;
	}
	public void setSingleRate(double singleRate) {
		this.singleRate = singleRate;
	}
	public double getDoubleRate() {
		return doubleRate;
	}
	public void setDoubleRate(double doubleRate) {
		this.doubleRate = doubleRate;
	}
	public double getTripleRate() {
		return tripleRate;
	}
	public void setTripleRate(double tripleRate) {
		this.tripleRate = tripleRate;
	}
	public double getQuadRate() {
		return quadRate;
	}
	public void setQuadRate(double quadRate) {
		this.quadRate = quadRate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public int getBreakfastNum() {
		return breakfastNum;
	}
	public void setBreakfastNum(int breakfastNum) {
		this.breakfastNum = breakfastNum;
	}

	

}
