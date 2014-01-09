/*****************************************************************<br>
 * <B>FILE :</B> PropGroupException.java <br>
 * <B>CREATE DATE :</B> Sep 2, 2008 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.exception;


 /**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * @author : Zolt Zhang<br>
 * @since : 2010-9-10<br>
 * @version : v1.0
 */
public class TimeOut4FogException extends Exception {

	public TimeOut4FogException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public TimeOut4FogException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public TimeOut4FogException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TimeOut4FogException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
