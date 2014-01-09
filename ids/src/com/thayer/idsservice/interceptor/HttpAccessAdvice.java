package com.thayer.idsservice.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thayer.idsservice.dao.EHttpLog;
import com.thayer.idsservice.dao.EHttpLogDAO;
import com.thayer.idsservice.exception.TimeOutException;

/**
 * 记录访问渠道的时间
 * @date 2013-6-6
 */
public class HttpAccessAdvice implements MethodInterceptor {
	private static final Log log = LogFactory.getLog(HttpAccessAdvice.class);
	/**
	 * 访问URL和渠道的Mapping
	 * key:访问的URL
	 * value:渠道
	 */
	private Map<String,String> exwebMapping = new HashMap<String,String>();
	
	private EHttpLogDAO ehttpLogDAO;

	public Object invoke(MethodInvocation method) throws Throwable {
		EHttpLog httpLog = new EHttpLog();
		httpLog.setRequtime(new Date());
		httpLog.setTimeout("n");//是否超时：否
		Object result = null;
		try {
			result = method.proceed();
		} catch (TimeOutException e) {
			httpLog.setTimeout("y");//是否超时：是
			throw e;
		}finally{
			httpLog.setIdstype(exwebMapping.get(method.getArguments()[0]));//第一个参数为url 通过URL获取渠道名称
			if(httpLog.getIdstype() == null || httpLog.getIdstype().isEmpty()){//如果渠道为空 就记录URL
				httpLog.setIdstype(method.getArguments()[0]+"");
			}
			httpLog.setResptime(new Date());
			try {
				ehttpLogDAO.save(httpLog);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		
		return result;
	}

	public Map<String, String> getExwebMapping() {
		return exwebMapping;
	}

	public void setExwebMapping(Map<String, String> exwebMapping) {
		this.exwebMapping = exwebMapping;
	}

	public EHttpLogDAO getEhttpLogDAO() {
		return ehttpLogDAO;
	}

	public void setEhttpLogDAO(EHttpLogDAO ehttpLogDAO) {
		this.ehttpLogDAO = ehttpLogDAO;
	}
	
}
