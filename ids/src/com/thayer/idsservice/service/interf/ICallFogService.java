/*****************************************************************<br>
 * <B>FILE :</B> ICallFogService.java <br>
 * <B>CREATE DATE :</B> 2010-10-14 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.service.interf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.bean.PriceValidResult;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.FogMQBean;

/**
 * <B>Function :</B> call Fog Service<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-14<br>
 * @version : v1.0
 */
public interface ICallFogService extends Serializable {
	/**
	 * 保存fogNewResv<br>
	 * 并且保存订单关系映射<br>
	 * 成功后发送mail给相关人员
	 * 
	 * @param tresvbase
	 * @return
	 * @throws BizException
	 * @throws TimeOut4FogException
	 */
	public MailFogResvBaseBean saveFogNewResvBean(TResvBase tresvbase, boolean validateBookRate) throws BizException,
			TimeOut4FogException;

	/**
	 * 获得每日价 查询是否符合下单标准
	 * 
	 * @param propid
	 * @param fogIata
	 * @param currencycode
	 * @param date
	 * @param roomcode
	 * @param rateCode
	 * @param rooms
	 * @param nights
	 * @return
	 * @throws BizException
	 */
	public Map<String, String> getExRateMap(String propid, String fogIata, String currencycode, String date,
			String roomcode, String rateCode, String rooms, String nights, String adults, String children)
			throws BizException;

	/**
	 * 根据mq内容查询得到ratedata
	 * 
	 * @param fogMQBean
	 * @return
	 * @throws BizException
	 */
	public AvailBean getzRateMapList(FogMQBean fogMQBean) throws BizException;

	List<RateDataBO> getZRateMap(String prop, Date start, int nights, String planId, String channelCode) throws BizException;

	public PriceValidResult validTotalPrice(double ValidTotalPrice, TResvBase tResvBase);
	
	 public String getOrderByOutNumber(String outNumber,String iata);
}
