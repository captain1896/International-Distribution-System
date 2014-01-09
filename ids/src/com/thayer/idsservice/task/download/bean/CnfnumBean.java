package com.thayer.idsservice.task.download.bean;

public class CnfnumBean {

	private String fogCnfnum;//fog订单号

	private String thirdCnfnum;//代理订单号

	private String thirdCode;//代理号
	
	private String thirdCodeName;//代理名稱

	public String getThirdCodeName() {
		return thirdCodeName;
	}

	public void setThirdCodeName(String thirdCodeName) {
		this.thirdCodeName = thirdCodeName;
	}

	public String getFogCnfnum() {
		return fogCnfnum;
	}

	public void setFogCnfnum(String fogCnfnum) {
		this.fogCnfnum = fogCnfnum;
	}

	public String getThirdCnfnum() {
		return thirdCnfnum;
	}

	public void setThirdCnfnum(String thirdCnfnum) {
		this.thirdCnfnum = thirdCnfnum;
	}

	public String getThirdCode() {
		return thirdCode;
	}

	public void setThirdCode(String thirdCode) {
		this.thirdCode = thirdCode;
	}

}
