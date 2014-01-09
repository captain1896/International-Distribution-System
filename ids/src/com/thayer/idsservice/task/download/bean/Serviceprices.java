package com.thayer.idsservice.task.download.bean;

import java.io.Serializable;

public class Serviceprices implements Serializable {
	// serv.setCount(amtTax);
	// serv.setFreq("RS");
	// serv.setInclude("0");
	// serv.setName("Tax");
	// serv.setType("T");
	// serv.setUnit("M");

	private String count;

	private String freq;

	private String include;

	private String name;

	private String type;

	private String unit;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
