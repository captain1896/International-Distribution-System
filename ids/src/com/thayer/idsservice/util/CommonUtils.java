package com.thayer.idsservice.util;

import java.util.UUID;

import org.apache.log4j.Logger;

public class CommonUtils {
	private static Logger LOGGER = Logger.getLogger(CommonUtils.class);

	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	public static void main(String[] args) {
		System.out.println(createRequestID());
	}

	private CommonUtils() {
	};

	public synchronized static String createRequestID() {

		char[] chars = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'c', 'q', 'z', 'o', 'k' };

		StringBuffer groupid = new StringBuffer().append(chars[(int) Math.random() * 15]).append(chars[(int) Math.round((Math.random() * 15))]).append(System.currentTimeMillis()).append(
				chars[(int) Math.round((Math.random() * 15))]);

		return "IDS-" + groupid.toString();
	}
}
