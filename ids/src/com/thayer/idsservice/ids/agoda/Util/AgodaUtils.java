/*****************************************************************<br>
 * <B>FILE :</B> AgodaUtils.java <br>
 * <B>CREATE DATE :</B> 2010-11-29 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.agoda.Util;

import org.springframework.util.StringUtils;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-29<br>
 * @version : v1.0
 */
public class AgodaUtils {

	/**
	 * 
	 */
	private AgodaUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String reHandleRS(String rsXml) {
		String result = "";
		if (StringUtils.hasText(rsXml)) {
			result = rsXml.replaceAll("True", "true");
			result = result.replaceAll("False", "false");
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
