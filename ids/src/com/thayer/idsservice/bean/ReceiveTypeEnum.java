package com.thayer.idsservice.bean;

/**
 * <B>Function :</B> 请求类型<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-4-22<br>
 * @version : v1.0
 */
public enum ReceiveTypeEnum {
	/**
	 * 预订请求
	 */
	OTA_HOTELRES,

	/**
	 * 预订取消
	 */
	OTA_CANCEL,

	/**
	 * 查询酒店可用性
	 */
	OTA_HOTELAVAIL

}
