/*****************************************************************<br>
 * <B>FILE :</B> SimpleOSCacheImpl.java <br>
 * <B>CREATE DATE :</B> Nov 30, 2007 <br>
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

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.thayer.idsservice.service.interf.IServiceCache;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : Nov 30, 2007<br>
 * @version : v1.0
 */
public class SimpleOSCacheImpl implements IServiceCache {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.fog.security.cache.SecurityCache#refreshCache(java.lang.String, java.lang.Object)
	 */
	public void refreshCache(String Key, Object obj) {
		// TODO Auto-generated method stub
		generalCacheAdministrator.flushEntry(Key);
		generalCacheAdministrator.putInCache(Key, obj);
	}

	private static final GeneralCacheAdministrator generalCacheAdministrator = new GeneralCacheAdministrator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.fog.security.cache.SecurityCache#addCache(java.lang.String, java.lang.Object)
	 */
	public void addCache(String key, Object obj) {
		// TODO Auto-generated method stub
		generalCacheAdministrator.putInCache(key, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.fog.security.cache.SecurityCache#delCache(java.lang.String)
	 */
	public void delCache(String key) {
		// TODO Auto-generated method stub
		generalCacheAdministrator.flushEntry(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.fog.security.cache.SecurityCache#flushCache()
	 */
	public void flushCache() {
		// TODO Auto-generated method stub
		generalCacheAdministrator.flushAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.fog.security.cache.SecurityCache#getCache(java.lang.String)
	 */
	public Object getCache(String key) {
		// TODO Auto-generated method stub
		try {
			return generalCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException e) {
			// TODO Auto-generated catch block
			generalCacheAdministrator.cancelUpdate(key);
			return null;
		}
	}

}
