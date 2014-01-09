/*****************************************************************<br>
 * <B>FILE :</B> IHttpClientService.java <br>
 * <B>CREATE DATE :</B> 2010-11-1 <br>
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

import java.util.Map;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOutException;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-1<br>
 * @version : v1.0
 */
public interface IHttpClientService {

	public void setHTTPCLIENT_TIMEOUT(int timeoutValue);

	/**
	 * 
	 * 得到Http请求结果 , 以get方式提交
	 * 
	 * @param url请求地址
	 * 
	 * @param parms请求参数
	 * 
	 * @return
	 * 
	 */

	public String postHttpRequest(String url) throws TimeOutException, BizException;

	/**
	 * 
	 * 得到Http请求结果 , 以post方式提交
	 * 
	 * @param url请求地址
	 * 
	 * @param parms请求参数
	 * 
	 * @return
	 * 
	 */

	public String postHttpRequest(String url, String content) throws TimeOutException, BizException;

	/**
	 * 返回接受状态,以post方式提交。 200 为成功发送
	 * 
	 * @param url
	 * @param parms
	 * @return
	 */
	public int callHttpRequest(String url, Map paras) throws TimeOutException, BizException;
	
	public int callHttpRequest(String url ) throws TimeOutException, BizException;

	/**
	 * 以post方式提交。 成功返回信息，不成功返回空字符串
	 * 
	 * 
	 * @param url
	 * @param parms
	 * @return
	 */
	public String postHttpRequest(String url, Map paras) throws TimeOutException, BizException;

}
