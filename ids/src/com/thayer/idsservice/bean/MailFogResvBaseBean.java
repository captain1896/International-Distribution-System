package com.thayer.idsservice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thayer.idsservice.task.download.bean.TResvRate;

public class MailFogResvBaseBean implements Serializable {

	/**
	 * 每晚入住的成人人数
	 */
	private Integer adults;

	/**
	 * 预定日期
	 */
	private Date bookeddate;

	/**
	 * 支付方式的有效期
	 */
	private String ccexpiration;

	/**
	 * 支付方式的名称
	 */
	private String ccname;

	/**
	 * 号码
	 */
	private String ccnumber;

	/**
	 * 支付方式
	 */
	private String cctype;

	/**
	 * 渠道
	 */
	private String channel;

	/**
	 * 每晚入住的儿童人数
	 */
	private Integer children;

	/**
	 * 订单号
	 */
	private String cnfnum;

	/**
	 * 信用卡验证码(信用卡背面的后三位数字)
	 */
	private String cvv;

	/**
	 * 货币单位
	 */
	private String currencyCode;
	/**
	 * 取消金描述
	 */
	private String cxldesc;

	/**
	 * 顾客的名
	 */
	private String gfirstname;

	/**
	 * 顾客的姓
	 */
	private String glastname;

	/**
	 * 保证金代码
	 */
	private String guaCode;

	/**
	 * 保证金描述
	 */
	private String guadesc;

	/**
	 * 担保状态　0:无　1:待确认 2:确认 3:失败 4:已撤销 5:已扣款 6:待撤销 7:待扣款 10:酒店处理
	 */
	private Integer guaStatus;

	/**
	 * 顾客地址
	 */
	private String guestaddress;

	/**
	 * 顾客mail
	 */
	private String guestmail;

	/**
	 * 顾客手机
	 */
	private String guestmobile;

	/**
	 * 顾客姓名
	 */
	private String guestname;

	/**
	 * 顾客电话
	 */
	private String guestphone;

	/**
	 * 酒店确认备注
	 */
	private String hotelRemark;

	/**
	 * iata号
	 */
	private String iata;

	private String iataEmail;

	private String iataName;

	private String iataPhone;

	/**
	 * 实际入住日期
	 */
	private String indate;

	/**
	 * 最后修改时间
	 */
	private String lastmodifydate;

	/**
	 * 房夜数
	 */
	private Integer nights;

	/**
	 * 其他顾客信息
	 */
	private String otherguestinfo;

	/**
	 * 外部订单号
	 */
	private String outcnfnum;

	/**
	 * 实际离点日期
	 */
	private String outdate;

	/**
	 * 支付方式的规则
	 */
	private String payment;

	/**
	 * 计划码
	 */
	private String plancode;

	/**
	 * 计划码名称
	 */
	private String planname;

	/**
	 * 支付状态 0:无 1:待预付 2:已预付 3:失败 4:待扣款 5:已扣款 10:酒店处理
	 */
	private Integer prepayStatus;

	private String prop;

	private String propName;

	/**
	 * 酒店价总金额
	 */
	private Double proptotalrevenue;

	/**
	 * 实际入住日期
	 */
	private String realindate;

	/**
	 * 实际离点日期
	 */
	private String realoutdate;

	/**
	 * 订单备注
	 */
	private String remark;

	private List<TResvRate> resvRateList;

	/**
	 * 订单类型 n:新单 e:修改单 c:取消单
	 */
	private String resvtype;

	/**
	 * 房间代码
	 */
	private String roomcode;

	/**
	 * 房间名称
	 */
	private String roomname;

	/**
	 * 入住的房间号
	 */
	private String roomnumber;

	/**
	 * 房间数
	 */
	private Integer rooms;

	/**
	 * 价格代码
	 */
	private String rateCode;
	/**
	 * 订单的状态 0:新单 1:已处理 2:手工单 3:已确认修改 4:已确认取消 5:应到未到 6:代定 7:已入住 8:已离店
	 */
	private String status;

	/**
	 * 销售价总金额
	 */
	private Double totalrevenue;

	public MailFogResvBaseBean() {
		resvRateList = new ArrayList<TResvRate>();
	}

	public Integer getAdults() {
		return adults;
	}

	public Date getBookeddate() {
		return bookeddate;
	}

	public String getCcexpiration() {
		return ccexpiration;
	}

	public String getCcname() {
		return ccname;
	}

	public String getCcnumber() {
		return ccnumber;
	}

	public String getCctype() {
		return cctype;
	}

	public String getChannel() {
		return channel;
	}

	public Integer getChildren() {
		return children;
	}

	public String getCnfnum() {
		return cnfnum;
	}

	public String getCvv() {
		return cvv;
	}

	public String getCxldesc() {
		return cxldesc;
	}

	public String getGfirstname() {
		return gfirstname;
	}

	public String getGlastname() {
		return glastname;
	}

	/**
	 * @return the guaCode
	 */
	public String getGuaCode() {
		return guaCode;
	}

	public String getGuadesc() {
		return guadesc;
	}

	public Integer getGuaStatus() {
		return guaStatus;
	}

	public String getGuestaddress() {
		return guestaddress;
	}

	public String getGuestmail() {
		return guestmail;
	}

	public String getGuestmobile() {
		return guestmobile;
	}

	public String getGuestname() {
		return guestname;
	}

	public String getGuestphone() {
		return guestphone;
	}

	public String getHotelRemark() {
		return hotelRemark;
	}

	public String getIata() {
		return iata;
	}

	public String getIataEmail() {
		return iataEmail;
	}

	public String getIataName() {
		return iataName;
	}

	public String getIataPhone() {
		return iataPhone;
	}

	public String getIndate() {
		return indate;
	}

	public String getLastmodifydate() {
		return lastmodifydate;
	}

	public Integer getNights() {
		return nights;
	}

	public String getOtherguestinfo() {
		return otherguestinfo;
	}

	public String getOutcnfnum() {
		return outcnfnum;
	}

	public String getOutdate() {
		return outdate;
	}

	public String getPayment() {
		return payment;
	}

	public String getPlancode() {
		return plancode;
	}

	public String getPlanname() {
		return planname;
	}

	public Integer getPrepayStatus() {
		return prepayStatus;
	}

	public String getProp() {
		return prop;
	}

	public String getPropName() {
		return propName;
	}

	public Double getProptotalrevenue() {
		return proptotalrevenue;
	}

	public String getRealindate() {
		return realindate;
	}

	public String getRealoutdate() {
		return realoutdate;
	}

	public String getRemark() {
		return remark;
	}

	public List<TResvRate> getResvRateList() {
		return resvRateList;
	}

	public String getResvtype() {
		return resvtype;
	}

	public String getRoomcode() {
		return roomcode;
	}

	public String getRoomname() {
		return roomname;
	}

	public String getRoomnumber() {
		return roomnumber;
	}

	public Integer getRooms() {
		return rooms;
	}

	public String getStatus() {
		return status;
	}

	public Double getTotalrevenue() {
		return totalrevenue;
	}

	public void setAdults(Integer adults) {
		this.adults = adults;
	}

	public void setBookeddate(Date bookeddate) {
		this.bookeddate = bookeddate;
	}

	public void setCcexpiration(String ccexpiration) {
		this.ccexpiration = ccexpiration;
	}

	public void setCcname(String ccname) {
		this.ccname = ccname;
	}

	public void setCcnumber(String ccnumber) {
		this.ccnumber = ccnumber;
	}

	public void setCctype(String cctype) {
		this.cctype = cctype;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public void setCnfnum(String cnfnum) {
		this.cnfnum = cnfnum;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public void setCxldesc(String cxldesc) {
		this.cxldesc = cxldesc;
	}

	public void setGfirstname(String gfirstname) {
		this.gfirstname = gfirstname;
	}

	public void setGlastname(String glastname) {
		this.glastname = glastname;
	}

	/**
	 * @param guaCode
	 *            the guaCode to set
	 */
	public void setGuaCode(String guaCode) {
		this.guaCode = guaCode;
	}

	public void setGuadesc(String guadesc) {
		this.guadesc = guadesc;
	}

	public void setGuaStatus(Integer guaStatus) {
		this.guaStatus = guaStatus;
	}

	public void setGuestaddress(String guestaddress) {
		this.guestaddress = guestaddress;
	}

	public void setGuestmail(String guestmail) {
		this.guestmail = guestmail;
	}

	public void setGuestmobile(String guestmobile) {
		this.guestmobile = guestmobile;
	}

	public void setGuestname(String guestname) {
		this.guestname = guestname;
	}

	public void setGuestphone(String guestphone) {
		this.guestphone = guestphone;
	}

	public void setHotelRemark(String hotelRemark) {
		this.hotelRemark = hotelRemark;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public void setIataEmail(String iataEmail) {
		this.iataEmail = iataEmail;
	}

	public void setIataName(String iataName) {
		this.iataName = iataName;
	}

	public void setIataPhone(String iataPhone) {
		this.iataPhone = iataPhone;
	}

	public void setIndate(String indate) {
		this.indate = indate;
	}

	public void setLastmodifydate(String lastmodifydate) {
		this.lastmodifydate = lastmodifydate;
	}

	public void setNights(Integer nights) {
		this.nights = nights;
	}

	public void setOtherguestinfo(String otherguestinfo) {
		this.otherguestinfo = otherguestinfo;
	}

	public void setOutcnfnum(String outcnfnum) {
		this.outcnfnum = outcnfnum;
	}

	public void setOutdate(String outdate) {
		this.outdate = outdate;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public void setPlancode(String plancode) {
		this.plancode = plancode;
	}

	public void setPlanname(String planname) {
		this.planname = planname;
	}

	public void setPrepayStatus(Integer prepayStatus) {
		this.prepayStatus = prepayStatus;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public void setProptotalrevenue(Double proptotalrevenue) {
		this.proptotalrevenue = proptotalrevenue;
	}

	public void setRealindate(String realindate) {
		this.realindate = realindate;
	}

	public void setRealoutdate(String realoutdate) {
		this.realoutdate = realoutdate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setResvRateList(List<TResvRate> resvRateList) {
		this.resvRateList = resvRateList;
	}

	public void setResvtype(String resvtype) {
		this.resvtype = resvtype;
	}

	public void setRoomcode(String roomcode) {
		this.roomcode = roomcode;
	}

	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}

	public void setRoomnumber(String roomnumber) {
		this.roomnumber = roomnumber;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotalrevenue(Double totalrevenue) {
		this.totalrevenue = totalrevenue;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode
	 *            the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the rateCode
	 */
	public String getRateCode() {
		return rateCode;
	}

	/**
	 * @param rateCode
	 *            the rateCode to set
	 */
	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

}
