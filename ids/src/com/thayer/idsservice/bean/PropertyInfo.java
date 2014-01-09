/*****************************************************************<br>
 * <B>FILE :</B> PropertyInfo.java <br>
 * <B>CREATE DATE :</B> 2011-5-10 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.bean;

import com.thayer.fog2.entity.Prop;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-5-10<br>
 * @version : v1.0
 */
public class PropertyInfo extends Prop {
	private String idsProp;
	private String startDate;
	private String endDate;
	private String contactPhone2;
	private String billMail;
	private String reservationMail;
	private String maxChildAge;
	private String url;
	private String[] roomCode;
	private String[] rateCode;
	private String ratePlanNotifyType;
	private String contactName1_givenName;
	private String contactName1_surname;
	private String contactName1_jobTitle;
	private String contactName2_givenName;
	private String contactName2_surname;
	private String contactName2_jobTitle;
	private String contactName3_givenName;
	private String contactName3_surname;
	private String contactName3_jobTitle;
	private String checkInStr;
	private String checkOutStr;
	/**
	 * 1代码表酒店级、2代表房型级、3代表计划级
	 */
	private String transferType = "1";

	/**
	 * @return the idsProp
	 */
	public String getIdsProp() {
		return idsProp;
	}

	/**
	 * @param idsProp
	 *            the idsProp to set
	 */
	public void setIdsProp(String idsProp) {
		this.idsProp = idsProp;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the contactPhone2
	 */
	public String getContactPhone2() {
		return contactPhone2;
	}

	/**
	 * @param contactPhone2
	 *            the contactPhone2 to set
	 */
	public void setContactPhone2(String contactPhone2) {
		this.contactPhone2 = contactPhone2;
	}

	/**
	 * @return the billMail
	 */
	public String getBillMail() {
		return billMail;
	}

	/**
	 * @param billMail
	 *            the billMail to set
	 */
	public void setBillMail(String billMail) {
		this.billMail = billMail;
	}

	/**
	 * @return the reservationMail
	 */
	public String getReservationMail() {
		return reservationMail;
	}

	/**
	 * @param reservationMail
	 *            the reservationMail to set
	 */
	public void setReservationMail(String reservationMail) {
		this.reservationMail = reservationMail;
	}

	/**
	 * @return the maxChildAge
	 */
	public String getMaxChildAge() {
		return maxChildAge;
	}

	/**
	 * @param maxChildAge
	 *            the maxChildAge to set
	 */
	public void setMaxChildAge(String maxChildAge) {
		this.maxChildAge = maxChildAge;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the roomCode
	 */
	public String[] getRoomCode() {
		return roomCode;
	}

	/**
	 * @param roomCode
	 *            the roomCode to set
	 */
	public void setRoomCode(String[] roomCode) {
		this.roomCode = roomCode;
	}

	/**
	 * @return the contactName1_givenName
	 */
	public String getContactName1_givenName() {
		return contactName1_givenName;
	}

	/**
	 * @param contactName1GivenName
	 *            the contactName1_givenName to set
	 */
	public void setContactName1_givenName(String contactName1GivenName) {
		contactName1_givenName = contactName1GivenName;
	}

	/**
	 * @return the contactName1_surname
	 */
	public String getContactName1_surname() {
		return contactName1_surname;
	}

	/**
	 * @param contactName1Surname
	 *            the contactName1_surname to set
	 */
	public void setContactName1_surname(String contactName1Surname) {
		contactName1_surname = contactName1Surname;
	}

	/**
	 * @return the contactName1_jobTitle
	 */
	public String getContactName1_jobTitle() {
		return contactName1_jobTitle;
	}

	/**
	 * @param contactName1JobTitle
	 *            the contactName1_jobTitle to set
	 */
	public void setContactName1_jobTitle(String contactName1JobTitle) {
		contactName1_jobTitle = contactName1JobTitle;
	}

	/**
	 * @return the contactName2_givenName
	 */
	public String getContactName2_givenName() {
		return contactName2_givenName;
	}

	/**
	 * @param contactName2GivenName
	 *            the contactName2_givenName to set
	 */
	public void setContactName2_givenName(String contactName2GivenName) {
		contactName2_givenName = contactName2GivenName;
	}

	/**
	 * @return the contactName2_surname
	 */
	public String getContactName2_surname() {
		return contactName2_surname;
	}

	/**
	 * @param contactName2Surname
	 *            the contactName2_surname to set
	 */
	public void setContactName2_surname(String contactName2Surname) {
		contactName2_surname = contactName2Surname;
	}

	/**
	 * @return the contactName2_jobTitle
	 */
	public String getContactName2_jobTitle() {
		return contactName2_jobTitle;
	}

	/**
	 * @param contactName2JobTitle
	 *            the contactName2_jobTitle to set
	 */
	public void setContactName2_jobTitle(String contactName2JobTitle) {
		contactName2_jobTitle = contactName2JobTitle;
	}

	/**
	 * @return the contactName3_givenName
	 */
	public String getContactName3_givenName() {
		return contactName3_givenName;
	}

	/**
	 * @param contactName3GivenName
	 *            the contactName3_givenName to set
	 */
	public void setContactName3_givenName(String contactName3GivenName) {
		contactName3_givenName = contactName3GivenName;
	}

	/**
	 * @return the contactName3_surname
	 */
	public String getContactName3_surname() {
		return contactName3_surname;
	}

	/**
	 * @param contactName3Surname
	 *            the contactName3_surname to set
	 */
	public void setContactName3_surname(String contactName3Surname) {
		contactName3_surname = contactName3Surname;
	}

	/**
	 * @return the contactName3_jobTitle
	 */
	public String getContactName3_jobTitle() {
		return contactName3_jobTitle;
	}

	/**
	 * @param contactName3JobTitle
	 *            the contactName3_jobTitle to set
	 */
	public void setContactName3_jobTitle(String contactName3JobTitle) {
		contactName3_jobTitle = contactName3JobTitle;
	}

	/**
	 * @return the rateCode
	 */
	public String[] getRateCode() {
		return rateCode;
	}

	/**
	 * @param rateCode
	 *            the rateCode to set
	 */
	public void setRateCode(String[] rateCode) {
		this.rateCode = rateCode;
	}

	/**
	 * @return the checkInStr
	 */
	public String getCheckInStr() {
		return checkInStr;
	}

	/**
	 * @param checkInStr
	 *            the checkInStr to set
	 */
	public void setCheckInStr(String checkInStr) {
		this.checkInStr = checkInStr;
	}

	/**
	 * @return the checkOutStr
	 */
	public String getCheckOutStr() {
		return checkOutStr;
	}

	/**
	 * @param checkOutStr
	 *            the checkOutStr to set
	 */
	public void setCheckOutStr(String checkOutStr) {
		this.checkOutStr = checkOutStr;
	}

	/**
	 * @return the transferType
	 */
	public String getTransferType() {
		return transferType;
	}

	/**
	 * @param transferType
	 *            the transferType to set
	 */
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	/**
	 * @return the ratePlanNotifyType
	 */
	public String getRatePlanNotifyType() {
		return ratePlanNotifyType;
	}

	/**
	 * @param ratePlanNotifyType
	 *            the ratePlanNotifyType to set
	 */
	public void setRatePlanNotifyType(String ratePlanNotifyType) {
		this.ratePlanNotifyType = ratePlanNotifyType;
	}

}
