package com.thayer.idsservice.ids.venere.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**   
 * @{#} XhiRoomDetail.java Create on Apr 9, 2008 1:10:13 PM   
 *   
 * Copyright (c) 2007 by HUBS1.   
 *  
 * @author <a href="mailto:tony.li@hubs1.net">Tony Li</a>  
 * @version 1.0 
 * Description ：Xhi解析后的入住房间信息类
 */   
public class VenereRoomDetail {
	
	/**
	 * 酒店Code
	 */
	private String hotelCode;
	
	/**
	 *房型Code 
	 */
	private String roomTypeCode;
	
	/**
	 * 入住房数
	 */
	private int rooms;
	
	/**
	 * 计划
	 */
	private String ratePlanCode;
	
	/**
	 * 税后价格
	 */
	private BigDecimal amountAfterTax;
	
	/**
	 * Fog系统中的总价
	 */
	private BigDecimal fogAfterTax;
	
	/**
	 * 税前价格
	 */
	private BigDecimal amountBeforeTax;
	
	/**
	 * 入住人数
	 */
	private int adults;
	/**
	 * 入住时间
	 */
	private Calendar indate;
	
	/**
	 * 离店时间
	 */
	private Calendar outdate;
	
	/**
	 * 房型描述
	 */
	private String roomDesc;
	
	private List<VenereRoomRate> rates = new ArrayList<VenereRoomRate>();
	

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getRoomTypeCode() {
		return roomTypeCode;
	}

	public void setRoomTypeCode(String roomTypeCode) {
		this.roomTypeCode = roomTypeCode;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public String getRatePlanCode() {
		return ratePlanCode;
	}

	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}

	public BigDecimal getAmountAfterTax() {
		return amountAfterTax;
	}

	public void setAmountAfterTax(BigDecimal amountAfterTax) {
		this.amountAfterTax = amountAfterTax;
	}

	public BigDecimal getAmountBeforeTax() {
		return amountBeforeTax;
	}

	public void setAmountBeforeTax(BigDecimal amountBeforeTax) {
		this.amountBeforeTax = amountBeforeTax;
	}

	public int getAdults() {
		return adults;
	}

	public void setAdults(int adults) {
		this.adults = adults;
	}

	public Calendar getIndate() {
		return indate;
	}

	public void setIndate(Calendar indate) {
		this.indate = indate;
	}

	public Calendar getOutdate() {
		return outdate;
	}

	public void setOutdate(Calendar outdate) {
		this.outdate = outdate;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public List<VenereRoomRate> getRates() {
		return rates;
	}

	public void setRates(List<VenereRoomRate> rates) {
		this.rates = rates;
	}

	public BigDecimal getFogAfterTax() {
		return fogAfterTax;
	}

	public void setFogAfterTax(BigDecimal fogAfterTax) {
		this.fogAfterTax = fogAfterTax;
	}
}
