package com.thayer.idsservice.context;

import org.apache.log4j.Logger;

/**
 * <B>Function :</B>Security context holder. <br>
 * <B>General Usage :</B>use static method.<br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : Jul 12, 2007<br>
 * @version : v1.0
 */
public class ServiceContextHolder {
	private static final Logger LOGGER = Logger.getLogger(ServiceContextHolder.class);

	private static IContextHolderStrategy strategy = new ThreadLocalServiceContextHolderStrategy();;

	/**
	 * Clear Authentication Context
	 */
	public static void clearContext() {
		strategy.clearContext();
	}

	/**
	 * Get Authentication Context
	 * 
	 * @return
	 */
	public static ServiceContext getContext() {
		return strategy.getContext();
	}

	/**
	 * Set Authentication Context
	 * 
	 * @param context
	 */
	public static void setContext(ServiceContext context) {
		strategy.setContext(context);
	}

	private ServiceContextHolder() {
	}

}
