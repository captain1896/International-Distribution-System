/*****************************************************************<br>
 * <B>FILE :</B> ICacheFilterService.java <br>
 * <B>CREATE DATE :</B> 2011-3-31 <br>
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

import com.thayer.idsservice.task.update.bean.FogMQBean;

/**
 * <B>Function :</B> 通过cache过滤重复信息接口<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-31<br>
 * @version : v1.0
 */
public interface ICacheFilterService {

	/**
	 * 是否包含重复的请求
	 * 
	 * @param requestxml
	 * @return
	 */
	boolean containsXml(String key, String requestxml,String type);

	/**
	 * 将新的请求加入缓存
	 * 
	 * @param key
	 * @param xml
	 */
	void addCache(String key, String xml,String type);
	
	/**
	 * 通过消息 组装成一个key
	 * @author Denile.Zhang
	 * @date 2012-11-10
	 * @param fogMQBean
	 * @return
	 */
	public String getCacheKeyByMessage(FogMQBean fogMQBean);

}
