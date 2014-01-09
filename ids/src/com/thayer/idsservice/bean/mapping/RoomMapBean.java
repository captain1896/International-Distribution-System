package com.thayer.idsservice.bean.mapping;

import java.util.ArrayList;
import java.util.List;

public class RoomMapBean {

	private String fogRoomCode;

	private String HotelRoomCode;

	private List<RateMapBean> rateMapBean;

	public RoomMapBean() {
		rateMapBean = new ArrayList<RateMapBean>();
	}

	public String getFogRoomCode() {
		return fogRoomCode;
	}

	public void setFogRoomCode(String fogRoomCode) {
		this.fogRoomCode = fogRoomCode;
	}

	public String getHotelRoomCode() {
		return HotelRoomCode;
	}

	public void setHotelRoomCode(String hotelRoomCode) {
		HotelRoomCode = hotelRoomCode;
	}

	public List<RateMapBean> getRateMapBean() {
		return rateMapBean;
	}

	public void setRateMapBean(List<RateMapBean> rateMapBean) {
		this.rateMapBean = rateMapBean;
	}

}
