package com.thayer.idsservice.task.update.bean;

import com.thayer.fog2.vo.RateMQVO;

public class FogMQBean {

	private RateMQVO rateMQVO;
	
	private boolean update3LevelAvail=false;//是否更新三级房量
	
	private String iata;//渠道号

	public boolean isUpdate3LevelAvail() {
		return update3LevelAvail;
	}

	public void setUpdate3LevelAvail(boolean update3LevelAvail) {
		this.update3LevelAvail = update3LevelAvail;
	}

	public RateMQVO getRateMQVO() {
		return rateMQVO;
	}

	public void setRateMQVO(RateMQVO rateMQVO) {
		this.rateMQVO = rateMQVO;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}
	

}
