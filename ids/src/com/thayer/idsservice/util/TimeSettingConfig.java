package com.thayer.idsservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-9-29<br>
 * @version : v1.0
 */
public class TimeSettingConfig implements Serializable {
	private static Logger LOGGER = Logger.getLogger(TimeSettingConfig.class);

	private static TimeSettingConfig settingConfig = new TimeSettingConfig();

	private TimeSettingConfig() {
	};

	public static TimeSettingConfig getInstance() {
		return settingConfig;
	}

	/**
	 * 设置请求查询订单的起始结束时间 起始时间来自time_setting.properties 结束时间拿系统时间
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public Date getTimeSettingValue(String settingFile, String timeSetKey, String defaultDate) {
		if (!StringUtils.hasText(timeSetKey)) {
			return null;
		}
		try {
			String s = "";

			ClassPathResource settingFileResource = new ClassPathResource(settingFile);
			File file = settingFileResource.getFile();
			if (!file.exists()) {
				file.createNewFile();
			}
			Properties props = new Properties();
			FileInputStream inStream = new FileInputStream(file);
			props.load(inStream);

			s = props.getProperty(timeSetKey);
			if (!StringUtils.hasText(s)) {
				s = defaultDate;
			}
			inStream.close();
			return DateUtil.dateValue(s);

		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}

	/**
	 * 设置下次请求的起始时间
	 * 
	 * @param date
	 * @throws IOException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	public void setTimeSettingValue(String settingFile, String timeSetKey, Date date) {
		try {
			ClassPathResource settingFileResource = new ClassPathResource(settingFile);
			File file = settingFileResource.getFile();
			if (!file.exists()) {
				file.createNewFile();
			}
			Properties props = new Properties();
			FileInputStream inStream = new FileInputStream(file);
			props.load(inStream);

			props.put(timeSetKey, DateUtil.formatDateTime(date));
			FileOutputStream oFile = new FileOutputStream(file);
			props.store(oFile, "");
			oFile.flush();// 清空缓存，写入磁盘
			oFile.close();// 关闭输出
			inStream.close();
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		}

	}

	public static void main(String[] args) {
		try {
			TimeSettingConfig.getInstance().setTimeSettingValue("test.properties", "agodaDownload.LastModifyDate1",
					new Date());
			TimeSettingConfig.getInstance().setTimeSettingValue("test.properties", "agodaDownload.LastModifyDate3",
					new Date());
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
