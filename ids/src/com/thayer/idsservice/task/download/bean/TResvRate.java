package com.thayer.idsservice.task.download.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.util.DateUtil;

public class TResvRate implements Serializable {
	/**
	 * 序列化、反序列化版本号
	 */
	private static final long serialVersionUID = 3245064852424841231L;

	/**
	 * 日期
	 */
	private Date bookendate;

	private String bookendatestr;

	/**
	 * 早餐数量
	 */
	private Integer breakfastNum;

	/**
	 * 加床价
	 */
	private Double extrate;

	/**
	 * 价格有效标记
	 */
	private String isactive;

	/**
	 * 酒店底价货币码(T_RESV_RATE表没有保存此属性,仅对内使用,不对外)
	 */
	private String propCurrency;

	/**
	 * 酒店底价/天
	 */
	private Double proprevenue;

	/**
	 * 买价货币码(T_RESV_RATE表没有保存此属性,仅对内使用,不对外)
	 */
	private String resvCurrency;

	/**
	 * 订单流水号
	 */
	private Long resvid;

	/**
	 * 订单买价/天
	 */
	private Double revenue;

	/**
	 * 时间
	 */
	private Date settime;

	/**
	 * 用户
	 */
	private String setuser;

	public Date getBookendate() {
		return bookendate;
	}

	public String getBookendatestr() {
		if (StringUtils.hasText(bookendatestr)) {
			return bookendatestr;
		} else {
			return DateUtil.formatDate(bookendate);
		}

	}

	public Integer getBreakfastNum() {
		return breakfastNum;
	}

	public Double getExtrate() {
		return extrate;
	}

	public String getIsactive() {
		return isactive;
	}

	public String getPropCurrency() {
		return propCurrency;
	}

	public Double getProprevenue() {
		return proprevenue;
	}

	public String getResvCurrency() {
		return resvCurrency;
	}

	public Long getResvid() {
		return resvid;
	}

	public Double getRevenue() {
		return revenue;
	}

	public Date getSettime() {
		return settime;
	}

	public String getSetuser() {
		return setuser;
	}

	public void setBookendate(Date bookendate) {
		this.bookendate = bookendate;
	}

	public void setBookendatestr(String bookendatestr) {
		this.bookendatestr = bookendatestr;
	}

	public void setBreakfastNum(Integer breakfastNum) {
		this.breakfastNum = breakfastNum;
	}

	public void setExtrate(Double extrate) {
		this.extrate = extrate;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public void setPropCurrency(String propCurrency) {
		this.propCurrency = propCurrency;
	}

	public void setProprevenue(Double proprevenue) {
		this.proprevenue = proprevenue;
	}

	public void setResvCurrency(String resvCurrency) {
		this.resvCurrency = resvCurrency;
	}

	public void setResvid(Long resvid) {
		this.resvid = resvid;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public void setSettime(Date settime) {
		this.settime = settime;
	}

	public void setSetuser(String setuser) {
		this.setuser = setuser;
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

}