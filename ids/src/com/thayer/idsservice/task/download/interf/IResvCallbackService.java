/*****************************************************************<br>
 * <B>FILE :</B> IResvCallbackService.java <br>
 * <B>CREATE DATE :</B> 2011-3-30 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.download.interf;

import com.thayer.idsservice.task.download.bean.SuccessBean;

/**
 * <B>Function :</B>回传FOG订单信息给IDS接口 <br>
 * <B>General Usage :</B> 如果需要回传订单信息，需实现此接口并注入到ResvDownloadBaseTask实现类中<br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-30<br>
 * @version : v1.0
 * @see com.thayer.idsservice.task.download.interf.AbstractResvCallbackService
 */
public interface IResvCallbackService {
	/**
	 * call back resv info.
	 * 
	 * @param successBean
	 * @return
	 */
	boolean resvCallback(SuccessBean successBean);
}
