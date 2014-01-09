package com.thayer.idsservice.task.download.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.util.DateUtil;

/**
 * 订单信息
 * 
 */
public class TResvBase implements Serializable {

	/**
	 * 序列化、反序列化版本号
	 */
	private static final long serialVersionUID = -2005244659156986962L;

	/**
	 * 清算中心最后操作时间戳
	 */
	private Date acOperateTime;

	/**
	 * 每晚入住的成人人数
	 */
	private Integer adults;

	/**
	 * 客人最早到店时间
	 */
	private Date arrivaltime;

	/**
	 * 预订用掉保留房数
	 */
	private Integer baseAllotment;

	/**
	 * 预定日期
	 */
	private Date bookeddate;

	/**
	 * 预定日期(字符)
	 */
	private String bookeddateStr;

	/**
	 * 预定用户（在Fog中注册的用户）
	 */
	private String bookeduser;

	/**
	 * 预订渠道
	 */
	private String bookSource;

	/**
	 * 预订子渠道
	 */
	private String bookSubSource;

	/**
	 * 是否含早
	 */
	private String breakfast;

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
	 * 确认时间
	 */
	private Date confirmDate;

	/**
	 * 联系人传真
	 */
	private String contactfax;

	/**
	 * 联系人EMAIL
	 */
	private String contactmail;

	/**
	 * 联系人手机
	 */
	private String contactmobile;

	/**
	 * 联系人
	 */
	private String contactperson;

	/**
	 * 联系人电话
	 */
	private String contactphone;

	/**
	 * 信用卡验证码(信用卡背面的后三位数字)
	 */
	private String cvv;

	/**
	 * 订单取消确认号
	 */
	private String cxfnum;

	/**
	 * 取消金描述
	 */
	private String cxldesc;

	/**
	 * 订单取消原因代码
	 */
	private String cxlReason;

	/**
	 * 取消原因
	 */
	private String desc;

	/**
	 * 当日汇率列表值
	 */
	private String exchangeTable;

	/**
	 * 冻结状态：1 审核结束， 不允许修改
	 */
	private Integer freeze;

	/**
	 * GDS名,订房宝使用
	 */
	private String gdssubcode;

	/**
	 * 顾客的名
	 */
	private String gfirstname;

	/**
	 * 顾客的姓
	 */
	private String glastname;

	/**
	 * 保证金描述
	 */
	private String guadesc;

	/**
	 * 保证金代码
	 */
	private String guaCode;

	/**
	 * 担保状态　0:无　1:待确认 2:确认 3:失败 4:已撤销 5:已扣款 6:待撤销 7:待扣款 10:酒店处理
	 */
	private Integer guaStatus;

	/**
	 * 顾客地址
	 */
	private String guestaddress;

	/**
	 * 顾客city
	 */
	private String guestcity;

	/**
	 * 顾客guestcountry
	 */
	private String guestcountry;

	/**
	 * 顾客预订时的特殊要求
	 */
	private String guestdemand;

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
	 * 房间客人姓名列表
	 */
	private List<String> guestList;

	/**
	 * 顾客电话
	 */
	private String guestphone;

	/**
	 * 客人最晚到店时间（保留时间）
	 */
	private Date holdtime;

	/**
	 * 酒店确认备注
	 */
	private String hotelRemark;

	/**
	 * 酒店确认状态 1: 全部确认 2:部分确认 3:不确认
	 */
	private Integer hotelStatus;

	/**
	 * iata号
	 */
	private String iata;

	/**
	 * 入住日期
	 */
	private Date indate;

	/**
	 * 入住日期字符
	 */
	private String indateStr;

	/**
	 * 酒店集团接口标记 1:表示是酒店集团接口 2:OnRequest订单
	 */
	private Integer interfaceFlag;

	/**
	 * 是担保单还是预付单
	 */
	private String isassure;

	/**
	 * 表示是否已经发送邮件或传真
	 */
	private Integer issendmessage;

	/**
	 * 最晚取消时间
	 */
	private Date lastcxltime;

	/**
	 * 最后修改时间
	 */
	private Date lastmodifydate;

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
	 * 离店日期
	 */
	private Date outdate;

	/**
	 * 离店日期字符
	 */
	private String outdateStr;

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
	 * 订房室使用
	 */
	private String pmsno;

	/**
	 * 支付状态 0:无 1:待预付 2:已预付 3:失败 4:待扣款 5:已扣款 10:酒店处理
	 */
	private Integer prepayStatus;

	/**
	 * 酒店ID
	 */
	private String prop;

	/**
	 * 酒店的货币码
	 */
	private String propCurrency;

	/**
	 * 酒店名称
	 */
	private String propname;

	/**
	 * 酒店价总金额
	 */
	private Double proptotalrevenue;

	private String rateClass;

	/**
	 * 实际入住日期
	 */
	private Date realindate;

	/**
	 * 实际离点日期
	 */
	private Date realoutdate;

	/**
	 * 订单备注
	 */
	private String remark;

	/**
	 * 订单的种类 n: 正常单 p: 问题单（手工单） s: 特殊订单
	 */
	private String resvcategory;

	/**
	 * RT:即时确认 RQ:非即时确认 NULL:历史数据
	 */
	private String resvclass;

	/**
	 * 订单的货币码
	 */
	private String resvCurrency;

	/**
	 * 主建
	 */
	private Long resvid;

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
	 * 服务费和税费 S=12|T=129
	 */
	private String serviceprice;

	/**
	 * 服务费税金bean
	 */
	private List<Serviceprices> serviceprices = new ArrayList<Serviceprices>();

	/**
	 * 蓄水状态 1:未使用 2: 已使用 3:已取消 4:被匹配
	 */
	private Integer sluice;

	/**
	 * 特殊需求状态　0:无 1:待确认 2:确认 3:不确认
	 */
	private String specialReqStatus;

	/**
	 * 特殊需求 , (逗号)分割
	 */
	private String specialRequirement;

	/**
	 * 订单的状态 0:新单 1:已处理 2:手工单 3:已确认修改 4:已确认取消 5:应到未到 6:代定 7:已入住 8:已离店
	 */
	private String status;

	/**
	 * 子渠道
	 */
	private String subsource;

	/**
	 * 团队订单号
	 */
	private String teamid;

	private String thirdCnfnum;

	private String idsProp;
	private String idsStatus;

	private String orderMsgType;
	
	/**
	 * 订单是否多房型
	 */
	private boolean isMultiRoomType;
	
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
	 * 销售价总金额
	 */
	private Double totalrevenue;

	public Date getAcOperateTime() {
		return acOperateTime;
	}

	public Integer getAdults() {
		return adults;
	}

	public Date getArrivaltime() {
		return arrivaltime;
	}

	/**
	 * @return the baseAllotment
	 */
	public Integer getBaseAllotment() {
		return baseAllotment;
	}

	public Date getBookeddate() {
		return bookeddate;
	}

	/**
	 * @return the bookeddateStr
	 */
	public String getBookeddateStr() {
		if (StringUtils.hasText(bookeddateStr)) {
			return bookeddateStr;
		} else {
			return DateUtil.formatDate(bookeddate);
		}
	}

	public String getBookeduser() {
		return bookeduser;
	}

	public String getBookSource() {
		return bookSource;
	}

	public String getBookSubSource() {
		return bookSubSource;
	}

	public String getBreakfast() {
		return breakfast;
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

	public Date getConfirmDate() {
		return confirmDate;
	}

	public String getContactfax() {
		return contactfax;
	}

	public String getContactmail() {
		return contactmail;
	}

	public String getContactmobile() {
		return contactmobile;
	}

	public String getContactperson() {
		return contactperson;
	}

	public String getContactphone() {
		return contactphone;
	}

	public String getCvv() {
		return cvv;
	}

	public String getCxfnum() {
		return cxfnum;
	}

	public String getCxldesc() {
		return cxldesc;
	}

	public String getCxlReason() {
		return cxlReason;
	}

	public String getDesc() {
		return desc;
	}

	public String getExchangeTable() {
		return exchangeTable;
	}

	/**
	 * @return the freeze
	 */
	public Integer getFreeze() {
		return freeze;
	}

	public String getGdssubcode() {
		return gdssubcode;
	}

	public String getGfirstname() {
		return gfirstname;
	}

	public String getGlastname() {
		return glastname;
	}

	public String getGuadesc() {
		return guadesc;
	}

	/**
	 * @return the guaStatus
	 */
	public Integer getGuaStatus() {
		return guaStatus;
	}

	public String getGuestaddress() {
		return guestaddress;
	}

	public String getGuestcity() {
		return guestcity;
	}

	public String getGuestcountry() {
		return guestcountry;
	}

	public String getGuestdemand() {
		return guestdemand;
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

	public Date getHoldtime() {
		return holdtime;
	}

	public String getHotelRemark() {
		return hotelRemark;
	}

	public Integer getHotelStatus() {
		return hotelStatus;
	}

	public String getIata() {
		return iata;
	}

	public Date getIndate() {
		return indate;
	}

	/**
	 * @return the indateStr
	 */
	public String getIndateStr() {
		if (StringUtils.hasText(indateStr)) {
			return indateStr;
		} else {
			return DateUtil.formatDate(indate);
		}
	}

	public Integer getInterfaceFlag() {
		return interfaceFlag;
	}

	public String getIsassure() {
		return isassure;
	}

	public Integer getIssendmessage() {
		return issendmessage;
	}

	/**
	 * @return the lastcxltime
	 */
	public Date getLastcxltime() {
		return lastcxltime;
	}

	public Date getLastmodifydate() {
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

	public Date getOutdate() {
		return outdate;
	}

	/**
	 * @return the outdateStr
	 */
	public String getOutdateStr() {
		if (StringUtils.hasText(outdateStr)) {
			return outdateStr;
		} else {
			return DateUtil.formatDate(outdate);
		}
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

	public String getPmsno() {
		return pmsno;
	}

	/**
	 * @return the prepayStatus
	 */
	public Integer getPrepayStatus() {
		return prepayStatus;
	}

	public String getProp() {
		return prop;
	}

	public String getPropCurrency() {
		return propCurrency;
	}

	public String getPropname() {
		return propname;
	}

	public Double getProptotalrevenue() {
		return proptotalrevenue;
	}

	public String getRateClass() {
		return rateClass;
	}

	public Date getRealindate() {
		return realindate;
	}

	public Date getRealoutdate() {
		return realoutdate;
	}

	public String getRemark() {
		return remark;
	}

	public String getResvcategory() {
		return resvcategory;
	}

	public String getResvclass() {
		return resvclass;
	}

	public String getResvCurrency() {
		return resvCurrency;
	}

	public Long getResvid() {
		return resvid;
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

	public String getServiceprice() {
		return serviceprice;
	}

	public List<Serviceprices> getServiceprices() {
		return serviceprices;
	}

	/**
	 * @return the sluice
	 */
	public Integer getSluice() {
		return sluice;
	}

	/**
	 * @return the specialReqStatus
	 */
	public String getSpecialReqStatus() {
		return specialReqStatus;
	}

	/**
	 * @return the specialRequirement
	 */
	public String getSpecialRequirement() {
		return specialRequirement;
	}

	public String getStatus() {
		return status;
	}

	public String getSubsource() {
		return subsource;
	}

	public String getTeamid() {
		return teamid;
	}

	public String getThirdCnfnum() {
		return thirdCnfnum;
	}

	public Double getTotalrevenue() {
		return totalrevenue;
	}

	public void setAcOperateTime(Date acOperateTime) {
		this.acOperateTime = acOperateTime;
	}

	public void setAdults(Integer adults) {
		this.adults = adults;
	}

	public void setArrivaltime(Date arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	/**
	 * @param baseAllotment
	 *            the baseAllotment to set
	 */
	public void setBaseAllotment(Integer baseAllotment) {
		this.baseAllotment = baseAllotment;
	}

	public void setBookeddate(Date bookeddate) {
		this.bookeddate = bookeddate;
	}

	/**
	 * @param bookeddateStr
	 *            the bookeddateStr to set
	 */
	public void setBookeddateStr(String bookeddateStr) {
		this.bookeddateStr = bookeddateStr;
	}

	public void setBookeduser(String bookeduser) {
		this.bookeduser = bookeduser;
	}

	public void setBookSource(String bookSource) {
		this.bookSource = bookSource;
	}

	public void setBookSubSource(String bookSubSource) {
		this.bookSubSource = bookSubSource;
	}

	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
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

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public void setContactfax(String contactfax) {
		this.contactfax = contactfax;
	}

	public void setContactmail(String contactmail) {
		this.contactmail = contactmail;
	}

	public void setContactmobile(String contactmobile) {
		this.contactmobile = contactmobile;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public void setCxfnum(String cxfnum) {
		this.cxfnum = cxfnum;
	}

	public void setCxldesc(String cxldesc) {
		this.cxldesc = cxldesc;
	}

	public void setCxlReason(String cxlReason) {
		this.cxlReason = cxlReason;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setExchangeTable(String exchangeTable) {
		this.exchangeTable = exchangeTable;
	}

	/**
	 * @param freeze
	 *            the freeze to set
	 */
	public void setFreeze(Integer freeze) {
		this.freeze = freeze;
	}

	public void setGdssubcode(String gdssubcode) {
		this.gdssubcode = gdssubcode;
	}

	public void setGfirstname(String gfirstname) {
		this.gfirstname = gfirstname;
	}

	public void setGlastname(String glastname) {
		this.glastname = glastname;
	}

	public void setGuadesc(String guadesc) {
		this.guadesc = guadesc;
	}

	/**
	 * @param guaStatus
	 *            the guaStatus to set
	 */
	public void setGuaStatus(Integer guaStatus) {
		this.guaStatus = guaStatus;
	}

	public void setGuestaddress(String guestaddress) {
		this.guestaddress = guestaddress;
	}

	public void setGuestcity(String guestcity) {
		this.guestcity = guestcity;
	}

	public void setGuestcountry(String guestcountry) {
		this.guestcountry = guestcountry;
	}

	public void setGuestdemand(String guestdemand) {
		this.guestdemand = guestdemand;
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

	public void setHoldtime(Date holdtime) {
		this.holdtime = holdtime;
	}

	public void setHotelRemark(String hotelRemark) {
		this.hotelRemark = hotelRemark;
	}

	public void setHotelStatus(Integer hotelStatus) {
		this.hotelStatus = hotelStatus;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}

	/**
	 * @param indateStr
	 *            the indateStr to set
	 */
	public void setIndateStr(String indateStr) {
		this.indateStr = indateStr;
	}

	public void setInterfaceFlag(Integer interfaceFlag) {
		this.interfaceFlag = interfaceFlag;
	}

	public void setIsassure(String isassure) {
		this.isassure = isassure;
	}

	public void setIssendmessage(Integer issendmessage) {
		this.issendmessage = issendmessage;
	}

	/**
	 * @param lastcxltime
	 *            the lastcxltime to set
	 */
	public void setLastcxltime(Date lastcxltime) {
		this.lastcxltime = lastcxltime;
	}

	public void setLastmodifydate(Date lastmodifydate) {
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

	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}

	/**
	 * @param outdateStr
	 *            the outdateStr to set
	 */
	public void setOutdateStr(String outdateStr) {
		this.outdateStr = outdateStr;
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

	public void setPmsno(String pmsno) {
		this.pmsno = pmsno;
	}

	/**
	 * @param prepayStatus
	 *            the prepayStatus to set
	 */
	public void setPrepayStatus(Integer prepayStatus) {
		this.prepayStatus = prepayStatus;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public void setPropCurrency(String propCurrency) {
		this.propCurrency = propCurrency;
	}

	public void setPropname(String propname) {
		this.propname = propname;
	}

	public void setProptotalrevenue(Double proptotalrevenue) {
		this.proptotalrevenue = proptotalrevenue;
	}

	public void setRateClass(String rateClass) {
		this.rateClass = rateClass;
	}

	public void setRealindate(Date realindate) {
		this.realindate = realindate;
	}

	public void setRealoutdate(Date realoutdate) {
		this.realoutdate = realoutdate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setResvcategory(String resvcategory) {
		this.resvcategory = resvcategory;
	}

	public void setResvclass(String resvclass) {
		this.resvclass = resvclass;
	}

	public void setResvCurrency(String resvCurrency) {
		this.resvCurrency = resvCurrency;
	}

	public void setResvid(Long resvid) {
		this.resvid = resvid;
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

	public void setServiceprice(String serviceprice) {
		this.serviceprice = serviceprice;
	}

	public void setServiceprices(List<Serviceprices> serviceprices) {
		this.serviceprices = serviceprices;
	}

	/**
	 * @param sluice
	 *            the sluice to set
	 */
	public void setSluice(Integer sluice) {
		this.sluice = sluice;
	}

	/**
	 * @param specialReqStatus
	 *            the specialReqStatus to set
	 */
	public void setSpecialReqStatus(String specialReqStatus) {
		this.specialReqStatus = specialReqStatus;
	}

	/**
	 * @param specialRequirement
	 *            the specialRequirement to set
	 */
	public void setSpecialRequirement(String specialRequirement) {
		this.specialRequirement = specialRequirement;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSubsource(String subsource) {
		this.subsource = subsource;
	}

	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}

	public void setThirdCnfnum(String thirdCnfnum) {
		this.thirdCnfnum = thirdCnfnum;
	}

	public void setTotalrevenue(Double totalrevenue) {
		this.totalrevenue = totalrevenue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		Map map = null;
		try {
			map = BeanUtils.describe(this);
		} catch (Exception e) {
			buf.append("BeanUtils describe bean occur exception:" + e.getMessage());
			return buf.toString();
		}
		return map.toString();
	}

	/**
	 * @return the guaCode
	 */
	public String getGuaCode() {
		return guaCode;
	}

	/**
	 * @param guaCode
	 *            the guaCode to set
	 */
	public void setGuaCode(String guaCode) {
		this.guaCode = guaCode;
	}

	/**
	 * @return the idsStatus
	 */
	public String getIdsStatus() {
		return idsStatus;
	}

	/**
	 * @param idsStatus
	 *            the idsStatus to set
	 */
	public void setIdsStatus(String idsStatus) {
		this.idsStatus = idsStatus;
	}

	/**
	 * @return the guestList
	 */
	public List<String> getGuestList() {
		return guestList;
	}

	/**
	 * @param guestList the guestList to set
	 */
	public void setGuestList(List<String> guestList) {
		this.guestList = guestList;
	}
	
	public String getOrderMsgType() {
		return orderMsgType;
	}

	public void setOrderMsgType(String orderMsgType) {
		this.orderMsgType = orderMsgType;
	}

	public boolean isMultiRoomType() {
		return isMultiRoomType;
	}

	public void setMultiRoomType(boolean isMultiRoomType) {
		this.isMultiRoomType = isMultiRoomType;
	}
	
}
