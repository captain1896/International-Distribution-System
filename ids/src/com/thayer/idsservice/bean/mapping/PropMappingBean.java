package com.thayer.idsservice.bean.mapping;

import java.util.ArrayList;
import java.util.List;

public class PropMappingBean {

	private String fogProp;
	
	private String hotelCode;
	
	private String userId;
	
	private String password;
	
	private List<RoomMapBean> roomMapBean;
	
	public PropMappingBean(){
		roomMapBean=new ArrayList<RoomMapBean>();
	}

	public List<RoomMapBean> getRoomMapBean() {
		return roomMapBean;
	}

	public void setRoomMapBean(List<RoomMapBean> roomMapBean) {
		this.roomMapBean = roomMapBean;
	}

	public String getFogProp() {
		return fogProp;
	}

	public void setFogProp(String fogProp) {
		this.fogProp = fogProp;
	}

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
