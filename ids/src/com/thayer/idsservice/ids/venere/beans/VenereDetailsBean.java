package com.thayer.idsservice.ids.venere.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.ids.venere.util.VenereStatus;
import com.thayer.idsservice.util.DateUtil;

/**
 * @{# XhiDetailsBean.java Create on Apr 9, 2008 1:10:47 PM
 * 
 *     Copyright (c) 2007 by HUBS1.
 * 
 * @author <a href="mailto:tony.li@hubs1.net">Tony Li</a>
 * @version 1.0 Description ：
 */
public class VenereDetailsBean {

	/**
	 * XHI的hotelCode
	 */
	private String hotelCode;
	/**
	 * Fog系统中的propID
	 */
	private String propId;

	/**
	 * 是否为多房型，ture为多房型
	 */
	private boolean isMultRoom = false;

	/**
	 * 修改时间
	 */
	private Calendar lastModifyDateTime;

	/**
	 * 全局入住时间
	 */
	private Calendar start;

	/**
	 * 全局离店时间
	 */
	private Calendar end;

	/**
	 * 房夜数
	 */
	private int nights;

	/**
	 * 订单状态
	 */
	private String resStatus;

	/**
	 * 订单状态Mail显示状态
	 */
	private String resStatusText;

	/**
	 * 订单类型
	 */
	private String resvType;
	/**
	 * 订单号
	 */
	private String resIDValue;

	/**
	 * 税后总价
	 */
	private BigDecimal totalAmountAfterTax;

	/**
	 * 入住房间详细
	 */
	private List<VenereRoomDetail> roomDetails = new ArrayList<VenereRoomDetail>();

	/**
	 * 信用卡详细
	 */
	private VenereCreditCardDetail creditCardDetail = new VenereCreditCardDetail();

	/**
	 * 入住客户详细
	 */
	private VenerePersonDetail personDetail = new VenerePersonDetail();

	/**
	 * 备注信息
	 */
	private List<String> remarks = new ArrayList<String>();

	/**
	 * 货币类型
	 */
	private String currencyCode;

	/**
	 * 计划描述
	 */
	private String planDesc;

	/**
	 * 解析中错误记录
	 */
	private List<String> errors = new ArrayList<String>();

	private List<String> warnings = new ArrayList<String>();

	public boolean isMultRoom() {
		return isMultRoom;
	}

	public void setMultRoom(boolean isMultRoom) {
		this.isMultRoom = isMultRoom;
	}

	public String getResStatus() {
		return resStatus;
	}

	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

	public String getResIDValue() {
		return resIDValue;
	}

	public void setResIDValue(String resIDValue) {
		this.resIDValue = resIDValue;
	}

	public List<VenereRoomDetail> getRoomDetails() {
		return roomDetails;
	}

	public void setRoomDetails(List<VenereRoomDetail> roomDetails) {
		this.roomDetails = roomDetails;
	}

	public VenereCreditCardDetail getCreditCardDetail() {
		return creditCardDetail;
	}

	public void setCreditCardDetail(VenereCreditCardDetail creditCardDetail) {
		this.creditCardDetail = creditCardDetail;
	}

	public VenerePersonDetail getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(VenerePersonDetail personDetail) {
		this.personDetail = personDetail;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public List<String> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<String> remarks) {
		this.remarks = remarks;
	}

	public String getResvType() {
		return resvType;
	}

	public void setResvType(String resvType) {
		this.resvType = resvType;
	}

	public BigDecimal getTotalAmountAfterTax() {
		return totalAmountAfterTax;
	}

	public void setTotalAmountAfterTax(BigDecimal totalAmountAfterTax) {
		this.totalAmountAfterTax = totalAmountAfterTax;
	}

	public Calendar getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(Calendar lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public int getNights() {
		try {
			nights = (int) DateUtil.daysBetween(start, end);
		} catch (BizException e) {
			nights = -1;
		}
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getResStatusText() {
		String resStatusDesc = VenereStatus.getResStatusDesc(resStatus);
		if (resStatusDesc == null) {
			return resStatus;
		}
		return resStatusDesc;
	}

	public void setResStatusText(String resStatusText) {
		this.resStatusText = resStatusText;
	}

	/**
	 * @return
	 * @author <a href="mailto:tony.li@hubs1.net">Tony Li</a>
	 * @version 1.0 Description ：是否是取消单
	 */
	public boolean isCancelled() {
		return VenereStatus.isCxlResv(resStatus);
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getPropId() {
		return propId;
	}

	public void setPropId(String propId) {
		this.propId = propId;
	}
}
