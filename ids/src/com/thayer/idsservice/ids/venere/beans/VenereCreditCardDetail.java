package com.thayer.idsservice.ids.venere.beans;

/**   
 * @{#} XhiCreditCardDetail.java Create on Apr 9, 2008 1:09:37 PM   
 *   
 * Copyright (c) 2007 by HUBS1.   
 *  
 * @author <a href="mailto:tony.li@hubs1.net">Tony Li</a>  
 * @version 1.0 
 * Description ：Xhi解析后的保证人信用卡信息类
 */   
public class VenereCreditCardDetail {
	/**
	 *信用卡类型
				Supported values are:
					“VI ” - VISA
					“MC ” - MASTERCARD
					“AX ” - AMEX
					“JC ” - JCB
					“DN ” - DINERS
					“EC ” - EUROCARD
				
	 */
	private String cardCode;
	
	/**
	 * 信用卡有效期MMYY
	 */
	private String expireDate;
	
	/**
	 * 信用卡卡号(隐式的)
	 */
	private String maskedCardNumber;
	
	/**
	 * 信用卡卡号
	 */
	private String cardNumber;
	
	/**
	 * 持卡人姓名
	 */
	private String cardHolderName;
	
	/**
	 * CVV2
	 */
	private String seriesCode;

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getMaskedCardNumber() {
		return maskedCardNumber;
	}

	public void setMaskedCardNumber(String maskedCardNumber) {
		this.maskedCardNumber = maskedCardNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}
