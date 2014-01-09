package com.thayer.idsservice.service.interf;

import java.io.Serializable;

import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.SuccessBean;

/**
 * 订单邮件
 * @date 2013-3-21
 */
public interface IEOrdermailInfoService extends Serializable{
	/**
	 * 保存成功的邮件信息
	 * @date 2013-3-21
	 * @param successBean 下单成功的信息
	 * @param thirdIataCode iata的名字
	 */
	public void saveSuccessMailInfo(SuccessBean successBean,String thirdIataCode);
	
	/**
	 * 保存失败的下单信息
	 * @date 2013-3-21
	 * @param errorBean 下单失败的信息
	 * @param thirdIataCode iata的名字
	 */
	public void saveFailMailInfo(ErrorBean errorBean,String thirdIataCode);
}
