package com.thayer.idsservice.task.update.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thayer.fog2.vo.RateMQVO;
import com.thayer.idsservice.util.DateUtil;

public class ResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6812889397580790203L;

	private String iata;

	private String iataName;

	private String thirdIataCode;

	private String fogProp;

	private String fogRoom;

	private String fogRate;

	private List<ErrorBean> errorlist = new ArrayList<ErrorBean>();

	private List<SuccessBean> successlist = new ArrayList<SuccessBean>();

	private RateMQVO rateMQVO;

	private String updateInfo;

	private String createDate;

	public RateMQVO getRateMQVO() {
		return rateMQVO;
	}

	public void setRateMQVO(RateMQVO rateMQVO) {
		this.rateMQVO = rateMQVO;
	}

	public ResultBean() {
		createDate = DateUtil.formatDateTime(new Date());
	}

	public String getFogProp() {
		return fogProp;
	}

	public void setFogProp(String fogProp) {
		this.fogProp = fogProp;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getIataName() {
		return iataName;
	}

	public void setIataName(String iataName) {
		this.iataName = iataName;
	}

	public List<ErrorBean> getErrorlist() {
		return errorlist;
	}

	public void setErrorlist(List<ErrorBean> errorlist) {
		this.errorlist = errorlist;
	}

	public List<SuccessBean> getSuccesslist() {
		return successlist;
	}

	public void setSuccesslist(List<SuccessBean> successlist) {
		this.successlist = successlist;
	}

	public String getThirdIataCode() {
		return thirdIataCode;
	}

	public void setThirdIataCode(String thirdIataCode) {
		this.thirdIataCode = thirdIataCode;
	}

	public String getFogRoom() {
		return fogRoom;
	}

	public void setFogRoom(String fogRoom) {
		this.fogRoom = fogRoom;
	}

	public String getFogRate() {
		return fogRate;
	}

	public void setFogRate(String fogRate) {
		this.fogRate = fogRate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateInfo() {
		if (rateMQVO != null) {
			return rateMQVO.toString();
		} else {
			return updateInfo;
		}
	}

}
