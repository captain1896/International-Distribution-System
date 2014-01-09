/*****************************************************************<br>
 * <B>FILE :</B> SecurityCache.java <br>
 * <B>CREATE DATE :</B> Nov 29, 2007 <br>
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

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : Nov 29, 2007<br>
 * @version : v1.0
 */
public interface IServiceCache extends Serializable {

	void addCache(String key, Object obj);

	void delCache(String key);

	void flushCache();

	Object getCache(String key);

	void refreshCache(String Key, Object obj);
}
