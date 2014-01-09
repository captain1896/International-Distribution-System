/*****************************************************************<br>
 * <B>FILE :</B> IReceiveQueryHotelAvailService.java <br>
 * <B>CREATE DATE :</B> 2011-1-19 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.receive.interf;

import com.thayer.idsservice.bean.ReceiveTypeEnum;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;

/**
 * <B>Function :</B> 查询酒店实时的可用性服务<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-1-19<br>
 * @version : v1.0
 */
public interface IReceiveQueryHotelAvailService {
	/**
	 * 查询酒店实时的可用性
	 * 
	 * @param xml,type
	 * @throws BizException
	 * @throws TimeOut4ThirdException
	 * @throws TimeOut4FogException
	 * @throws MappingException
	 */
	public String queryHotelAvail(String xml,ReceiveTypeEnum type) throws BizException, TimeOut4ThirdException, TimeOut4FogException, MappingException;
}
