/*****************************************************************<br>
 * <B>FILE :</B> ITransPropertyInfoService.java <br>
 * <B>CREATE DATE :</B> 2011-5-12 <br>
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

import com.thayer.idsservice.bean.PropertyInfo;
import com.thayer.idsservice.exception.BizException;

/**
 * <B>Function :</B>发布酒店信息接口 <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-5-12<br>
 * @version : v1.0
 */
public interface ITransPropertyInfoService {
	/**
	 * 发布酒店信息
	 * 
	 * @param propertyInfo
	 * @throws BizException
	 */
	void transPropertyInfo(PropertyInfo propertyInfo) throws BizException;
}
