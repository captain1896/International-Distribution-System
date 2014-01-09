package com.thayer.idsservice.ids.venere.beans;

import java.math.BigDecimal;
import java.util.Date;

public class VenereRoomRate {
	private Date date;
	private BigDecimal rate;
	private BigDecimal mailRate;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getMailRate() {
		return mailRate;
	}

	public void setMailRate(BigDecimal mailRate) {
		this.mailRate = mailRate;
	}

}
