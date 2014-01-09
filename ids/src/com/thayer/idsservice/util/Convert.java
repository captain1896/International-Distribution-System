package com.thayer.idsservice.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Convert {
	public static Calendar ToCalendar(Object obj1) {
		if (obj1 == null)
			obj1 = "";
		if (obj1.toString() != "") {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(format.parse(obj1.toString()));
				return calendar;
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	public static String DateToString(Object obj1) {
		if (obj1 != null) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String newDate = simpleDateFormat.format(obj1);
				return newDate;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public static String DateTimeToString(Object obj1) {
		if (obj1 != null) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String newDate = simpleDateFormat.format(obj1);
				return newDate;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public static Date ToDate(Object obj1) {
		if (obj1 == null)
			obj1 = "";
		if (obj1.toString() != "") {
			try {
				Date date = null;
				date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(obj1.toString());
				return date;
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	public static Date ToDateTime(Object obj1) {
		if (obj1 == null)
			obj1 = "";
		if (obj1.toString() != "") {
			try {
				Date date = null;
				date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj1.toString());
				return date;
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	public static int ToInt(Object obj1) {
		if (obj1 != null && obj1.toString() != "") {
			try {
				int dest = new Integer(obj1.toString()).intValue();
				return dest;
			} catch (Exception e) {
				return 0;
			}

		}
		return 0;
	}

	// 专门给查询价格用道HotelMFService类中pgsAvailAllowRateList方法
	public static Integer ToInt2(Object obj1) {
		if (obj1 != null && obj1.toString() != "") {
			try {
				Integer dest = new Integer(obj1.toString());
				return dest;
			} catch (Exception e) {
				return null;
			}

		}
		return null;
	}

	public static double ToDouble(Object obj1) {
		if (obj1 != null && obj1.toString() != "") {
			try {
				double dest = new Double(obj1.toString()).doubleValue();
				return dest;
			} catch (Exception e) {
				return 0.0d;
			}

		}
		return 0.0d;
	}

	// 专门给查询价格用道HotelMFService类中pgsAvailAllowRateList方法
	public static Double ToDouble2(Object obj1) {
		if (obj1 != null && obj1.toString() != "") {
			try {
				double dest = new Double(obj1.toString()).doubleValue();
				return dest;
			} catch (Exception e) {
				return null;
			}

		}
		return null;
	}

	public static Float ToFloat(Object obj1) {
		if (obj1 == null)
			obj1 = "";
		if (obj1.toString() != "") {
			Float dest = Float.parseFloat(obj1.toString());
			return dest;
		}
		return null;
	}

	public static Long ToLong(Object obj1) {
		if (obj1 == null)
			obj1 = "";
		if (obj1.toString() != "") {
			Long dest = Long.parseLong(obj1.toString());
			return dest;
		}
		return null;
	}

	public static String ToString(Object obj1, String defaultvalue) {
		if (ToString(obj1) == null)
			return defaultvalue;
		else
			return ToString(obj1);
	}

	public static String ToCDATAString(Object obj1, String defaultvalue) {
		if (ToString(obj1) == null)
			return "<![CDATA[" + defaultvalue + "]]>";
		else
			return "<![CDATA[" + ToString(obj1) + "]]>";
	}

	public static String ToString(Object obj1) {
		if (obj1 == null)
			obj1 = "";
		if (obj1.toString() != "") {
			return obj1.toString();
		}
		return null;
	}

	// 传入double类型,四舍五入转化为int类型
	public static int DoubleToInt(Object obj1) {
		if (obj1 != null && obj1.toString() != "") {
			try {
				int dest = ToInt(Math.round(ToDouble(obj1)));
				return dest;
			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            待转换编码的字符串
	 * @param oldCharset
	 *            原编码
	 * @param newCharset
	 *            目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String oldCharset, String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			// 用旧的字符编码解码字符串。解码可能会出现异常。
			byte[] bs = str.getBytes(oldCharset);
			// 用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}
}
