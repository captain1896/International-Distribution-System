package com.thayer.idsservice.task.download.interf;

import java.io.Serializable;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;

public interface IDownLoadService extends Serializable {
	// /**
	// * 根据酒店代码获取酒店的所有订单
	// *
	// * @param hotelCode
	// */
	// public void getResvByHotel(String hotelCode, Date since, Date till) ;

	/**
	 * 下载订单入口
	 * 
	 * @param hotelCode
	 * @param since
	 * @param till
	 */
	public void downLoad(HotelBean hotelBean, ResultBean resultBean) throws BizException, TimeOut4ThirdException, TimeOut4FogException, MappingException;

}
