package com.thayer.idsservice.ids.venere.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VenereMailBean {
	private String date;
	private List<BigDecimal> dayRateList = new ArrayList<BigDecimal>();
	private double dayTotal;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<BigDecimal> getDayRateList() {
		return dayRateList;
	}

	public void setDayRateList(List<BigDecimal> dayRateList) {
		this.dayRateList = dayRateList;
	}

	public double getDayTotal() {
		return dayTotal;
	}

	public void setDayTotal(double dayTotal) {
		this.dayTotal = dayTotal;
	}
}
