/*****************************************************************<br>
 * <B>FILE :</B> HttpClientService_v4.java <br>
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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

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
public class HttpClientService_v4 implements IHttpClientService {
	protected static Logger LOGGER = Logger.getLogger(HttpClientService_v4.class);

	public static int HTTPCLIENT_TIMEOUT = 60000;

	public static int SO_TIMEOUT = 600000*3;

	private DefaultHttpClient useTrustingTrustManager(DefaultHttpClient httpClient) throws BizException {
		try {
			// First create a trust manager that won't care.
			X509TrustManager trustManager = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// Don't do anything.
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// Don't do anything.
				}

				public X509Certificate[] getAcceptedIssuers() {
					// Don't do anything.
					return null;
				}
			};

			// Now put the trust manager into an SSLContext.
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { trustManager }, null);

			// Use the above SSLContext to create your socket factory
			// (I found trying to extend the factory a bit difficult due to a
			// call to createSocket with no arguments, a method which doesn't
			// exist anywhere I can find, but hey-ho).
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// If you want a thread safe client, use the ThreadSafeConManager,
			// but
			// otherwise just grab the one from the current client, and get hold
			// of its
			// schema registry. THIS IS THE KEY THING.
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();

			// Register our new socket factory with the typical SSL port and the
			// correct protocol name.
			schemeRegistry.register(new Scheme("https", sf, 443));

			// Finally, apply the ClientConnectionManager to the Http Client
			// or, as in this example, create a new one.
			return new DefaultHttpClient(ccm, httpClient.getParams());
		} catch (Throwable t) {
			// AND NEVER EVER EVER DO THIS, IT IS LAZY AND ALMOST ALWAYS WRONG!
			LOGGER.error(t);
			throw new BizException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#callHttpRequest(java.lang.String, java.util.Map)
	 */
	@Override
	public int callHttpRequest(String url, Map parms) throws TimeOutException, BizException {
		try {

			if (!StringUtils.hasText(url)) {
				LOGGER.error("callBack url is null!");
				return 400;
			}

			byte[] body = null;

			DefaultHttpClient httpclient = new DefaultHttpClient();

			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
			httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

			if (url.toUpperCase().startsWith("HTTPS")) {
				// add ssl
				httpclient = useTrustingTrustManager(httpclient);
			}

			httpclient.getParams().setIntParameter("http.socket.timeout", HTTPCLIENT_TIMEOUT);

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (parms != null) {

				Iterator it = parms.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					qparams.add(new BasicNameValuePair(key.toString(), value == null ? "" : value.toString()));
				}
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(qparams, "UTF-8");

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			httppost.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
			httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			HttpResponse response = httpclient.execute(httppost);
			
			
			
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				return HttpStatus.SC_OK;
			}
		} catch (SocketTimeoutException e) {
			LOGGER.warn(e);
			throw new TimeOutException(e);
		} catch (ConnectTimeoutException e) {
			LOGGER.warn(e);
			throw new TimeOutException(e);
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new BizException(e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new BizException(e);
		}

		return 400;

	}

	@Override
	public int callHttpRequest(String url) throws TimeOutException, BizException {
		if (!StringUtils.hasText(url)) {
			LOGGER.error("callBack url is null!");
			return 400;
		}

		String body = null;

		// 构造HttpClient的实例

		DefaultHttpClient httpclient = new DefaultHttpClient();

		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
		httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

		if (url.toUpperCase().startsWith("HTTPS")) {
			// add ssl
			httpclient = useTrustingTrustManager(httpclient);
		}

		httpclient.getParams().setIntParameter("http.socket.timeout", HTTPCLIENT_TIMEOUT);

		HttpGet httpget = new HttpGet(url);
		httpget.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpget.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpget.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		try {
			HttpResponse response = httpclient.execute(httpget);

			return response.getStatusLine().getStatusCode();
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
		if (!StringUtils.hasText(url)) {
			LOGGER.error("callBack url is null!");
			return "";
		}

		String body = null;

		// 构造HttpClient的实例

		DefaultHttpClient httpclient = new DefaultHttpClient();

		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
		httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

		if (url.toUpperCase().startsWith("HTTPS")) {
			// add ssl
			httpclient = useTrustingTrustManager(httpclient);
		}

		httpclient.getParams().setIntParameter("http.socket.timeout", HTTPCLIENT_TIMEOUT);

		HttpGet httpget = new HttpGet(url);
		httpget.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpget.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpget.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		try {
			HttpResponse response = httpclient.execute(httpget);

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

				StringBuilder sb = new StringBuilder();
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					body = EntityUtils.toString(entity);
				}

			}
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

		return body;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#postHttpRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public String postHttpRequest(String url, String content) throws TimeOutException, BizException {
		try {

			if (!StringUtils.hasText(url)) {
				LOGGER.error("callBack url is null!");
				return "";
			}

			byte[] body = null;

			DefaultHttpClient httpclient = new DefaultHttpClient();

			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
			httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

			if (url.toUpperCase().startsWith("HTTPS")) {
				// add ssl
				httpclient = useTrustingTrustManager(httpclient);
			}

			httpclient.getParams().setIntParameter("http.socket.timeout", HTTPCLIENT_TIMEOUT);

			HttpEntity entity = new StringEntity(content, "UTF-8");

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			httppost.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
			httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			HttpResponse response = httpclient.execute(httppost);

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

				StringBuilder sb = new StringBuilder();
				HttpEntity resultEntity = response.getEntity();
				if (resultEntity != null) {
					return EntityUtils.toString(resultEntity);
				}

			} else {
				LOGGER.error("postRequest error: status code: " + response.getStatusLine().getStatusCode());
			}
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

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IHttpClientService#postHttpRequest(java.lang.String, java.util.Map)
	 */
	@Override
	public String postHttpRequest(String url, Map parms) throws TimeOutException, BizException {
		try {

			if (!StringUtils.hasText(url)) {
				LOGGER.error("callBack url is null!");
				return "";
			}

			byte[] body = null;

			DefaultHttpClient httpclient = new DefaultHttpClient();

			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
			httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

			if (url.toUpperCase().startsWith("HTTPS")) {
				// add ssl
				httpclient = useTrustingTrustManager(httpclient);
			}

			httpclient.getParams().setIntParameter("http.socket.timeout", HTTPCLIENT_TIMEOUT);

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (parms != null) {

				Iterator it = parms.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					qparams.add(new BasicNameValuePair(key.toString(), value == null ? "" : value.toString()));
				}
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(qparams, "UTF-8");

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			httppost.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
			httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			HttpResponse response = httpclient.execute(httppost);

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

				StringBuilder sb = new StringBuilder();
				HttpEntity resultEntity = response.getEntity();
				if (resultEntity != null) {
					return EntityUtils.toString(resultEntity);
				}

			} else {
				LOGGER.error("postRequest error: status code: " + response.getStatusLine().getStatusCode());
			}
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

		return "";

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
