/*****************************************************************<br>
 * <B>FILE :</B> HttpClientService_v3.java <br>
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
package com.thayer.idsservice.service;

import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.thayer.idsservice.bean.MySecureProtocolSocketFactory;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.interf.IHttpClientService;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-1<br>
 * @version : v1.0
 */
public class HttpClientService_v3 implements IHttpClientService {
	public static int HTTPCLIENT_TIMEOUT = 60000;
	public static int SO_TIMEOUT = 120000;
	protected static Logger LOGGER = Logger.getLogger(HttpClientService_v3.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#callHttpRequest(java.lang.String, java.util.Map)
	 */
	@Override
	public int callHttpRequest(String url, Map paras) throws TimeOutException, BizException {
		// LOGGER.debug("Message=\n" + message);
		PostMethod post = new PostMethod(url);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		NameValuePair[] pairs = null;
		if (paras != null) {
			pairs = new NameValuePair[paras.size()];
			Iterator it = paras.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				NameValuePair postMessage = new NameValuePair(key.toString(), value == null ? "" : value.toString());
				pairs[i] = postMessage;
				i++;
			}
		}

		post.setRequestBody(pairs);
		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTPCLIENT_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		// Execute request
		if (url.toUpperCase().startsWith("HTTPS")) {
			Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", myhttps);
		}
		try {
			int result = httpclient.executeMethod(post);
			post.releaseConnection();
			return result;
		} catch (SocketTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ConnectTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new BizException(e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new BizException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#callHttpRequest(java.lang.String, java.util.Map)
	 */
	@Override
	public int callHttpRequest(String url) throws TimeOutException, BizException {
		try {
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			HttpClient httpclient = new HttpClient();
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTPCLIENT_TIMEOUT);
			httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
			// Execute request
			if (url.toUpperCase().startsWith("HTTPS")) {
				Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
			}
			int result = httpclient.executeMethod(post);
            post.releaseConnection();
			return result;

		} catch (SocketTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ConnectTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new BizException(e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new BizException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#postHttpRequest(java.lang.String)
	 */
	@Override
	public String postHttpRequest(String url) throws TimeOutException, BizException {
		// LOGGER.debug("Message=\n" + message);
		try {
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			HttpClient httpclient = new HttpClient();
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTPCLIENT_TIMEOUT);
			httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
			// Execute request
			if (url.toUpperCase().startsWith("HTTPS")) {
				Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
			}
			int result = httpclient.executeMethod(post);
			
			if (HttpStatus.SC_OK != result) {
				throw new Exception("Http client post error. error code:"+result);
			}
			// Display status code
			LOGGER.debug("Http Response status code: " + result);
			LOGGER.debug("Http Response body: " + post.getResponseBodyAsString());
			String resultValue = post.getResponseBodyAsString();
			
			post.releaseConnection();
			return resultValue;
		} catch (SocketTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ConnectTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new BizException(e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new BizException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#postHttpRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public String postHttpRequest(String url, String message) throws TimeOutException, BizException {
		LOGGER.debug("Message=\n" + message);
		try {
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			RequestEntity entity = new StringRequestEntity(message, "text/xml", "UTF-8");
			post.setRequestEntity(entity);

			HttpClient httpclient = new HttpClient();
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTPCLIENT_TIMEOUT);
			httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
			// Execute request
			if (url.toUpperCase().startsWith("HTTPS")) {
				Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
			}
			int result = httpclient.executeMethod(post);

			if (HttpStatus.SC_OK != result) {
				LOGGER.error(result);
				throw new Exception("Http client post error.");
			}
			// Display status code
			LOGGER.debug("Http Response status code: " + result);
			LOGGER.debug("Http Response body: " + post.getResponseBodyAsString());
			String resultValue = post.getResponseBodyAsString();
			
			post.releaseConnection();
			return resultValue;

		} catch (SocketTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ConnectTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new BizException(e);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new BizException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#postHttpRequest(java.lang.String, java.util.Map)
	 */
	@Override
	public String postHttpRequest(String url, Map paras) throws TimeOutException, BizException {
		// LOGGER.debug("Message=\n" + message);
		PostMethod post = new PostMethod(url);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		NameValuePair[] pairs = null;
		if (paras != null) {
			pairs = new NameValuePair[paras.size()];
			Iterator it = paras.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				NameValuePair postMessage = new NameValuePair(key.toString(), value == null ? "" : value.toString());
				pairs[i] = postMessage;
				i++;
			}
		}
		post.setRequestBody(pairs);
		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTPCLIENT_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		if (url.toUpperCase().startsWith("HTTPS")) {
			Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", myhttps);
		}
		// Execute request
		try {
			int result = httpclient.executeMethod(post);
			LOGGER.debug("Http Response body: " + post.getResponseBodyAsString());
			if (HttpStatus.SC_OK != result) {
				throw new Exception("Http client post error.");
			}
			// Display status code
			LOGGER.debug("Http Response status code: " + result);
			LOGGER.debug("Http Response body: " + post.getResponseBodyAsString());
			String resultValue = post.getResponseBodyAsString();
			return resultValue;

		} catch (SocketTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ConnectTimeoutException e) {
			LOGGER.error(e);
			throw new TimeOutException(e);
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new BizException(e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new BizException(e);
		}
	}

	@Override
	public void setHTTPCLIENT_TIMEOUT(int timeoutValue) {
		this.HTTPCLIENT_TIMEOUT = timeoutValue;

	}

	/**
	 * @param sOTIMEOUT
	 *            the sO_TIMEOUT to set
	 */
	public static void setSO_TIMEOUT(int sOTIMEOUT) {
		SO_TIMEOUT = sOTIMEOUT;
	}

}
