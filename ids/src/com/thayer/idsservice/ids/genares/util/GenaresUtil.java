package com.thayer.idsservice.ids.genares.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class GenaresUtil {

	private static Logger LOGGER = Logger.getLogger(GenaresUtil.class);

	public static String getPropertiesValue(String key) {
		String returnValue = "";
		Properties props = new Properties();
		InputStream fis = null;
		try {
			fis = GenaresUtil.class.getResourceAsStream("/idsconfig/genares/genaresconfig.properties");
			props.load(fis);

			returnValue = props.getProperty(key);
			// fis.close();
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e2) {
					LOGGER.error(ExceptionUtils.getFullStackTrace(e2));
				}
			}
		}
		return returnValue;
	}
	public static void main(String[] args) {
		System.out.println(GenaresUtil.getPropertiesValue("genares.iata"));
	}
}
