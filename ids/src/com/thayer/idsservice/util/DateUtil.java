package com.thayer.idsservice.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlException;

import com.thayer.idsservice.exception.BizException;

/**
 * <B>Function :</B> Date Util<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : Jun 2, 2010<br>
 * @version : v1.0
 */
public class DateUtil {

    private static final Log log = LogFactory.getLog(DateUtil.class);

    public final static String DATE_FORMAT = "yyyy-MM-dd";

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String FORMAT_DEFAULT = "default";

    public final static String FORMAT_FULL = "full";

    public final static String FORMAT_LONG = "long";

    public final static String FORMAT_MEDIUM = "medium";

    public final static String FORMAT_SHORT = "short";

    public final static String TIME_FORMAT = "HH:mm:ss";

    public final static String TIME_UNIT_S = "s";

    public final static String TIME_UNIT_M = "m";

    public final static String TIME_UNIT_H = "H";

    public final static String TIME_UNIT_D = "d";

    public final static String TIME_UNIT_W = "w";

    public final static String TIME_UNIT_MON = "M";

    public final static String TIME_UNIT_Y = "y";

    public final static String TIME_UNIT_Q = "q";

    public static long current() {
        return System.currentTimeMillis();
    }

    public static Date dateAdd(String interval, int number, Date date) {
        if (date == null) {
            log.error("Parameter data is NULL!");
            throw new NullPointerException();
        }

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);

        if (interval.equals(TIME_UNIT_Y)) {
            rightNow.add(Calendar.YEAR, number);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_Q)) {

            rightNow.add(Calendar.MONTH, number * 3);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_MON)) {

            rightNow.add(Calendar.MONTH, number);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_D)) {

            rightNow.add(Calendar.DAY_OF_YEAR, number);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_W)) {

            rightNow.add(Calendar.DAY_OF_YEAR, number * 7);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_H)) {

            rightNow.add(Calendar.HOUR_OF_DAY, number);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_M)) {

            rightNow.add(Calendar.MINUTE, number);
            return rightNow.getTime();
        }

        if (interval.equals(TIME_UNIT_S)) {

            rightNow.add(Calendar.SECOND, number);
            return rightNow.getTime();
        }

        return null;
    }

    public static int dateDiff(String interval, Date beginDate, Date endDate) throws Exception {
        if (beginDate == null || endDate == null) {
            log.error("Parameter Is NULL!");
            throw new NullPointerException();
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(beginDate);
        calendar2.setTime(endDate);

        // interval
        if (interval.equals(TIME_UNIT_Y)) {

            return (calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR));
        }

        if (interval.equals(TIME_UNIT_Q)) {
            int increaseYear = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
            int increaseMonth = calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);
            return (increaseMonth + increaseYear * 12) / 3;
        }

        if (interval.equals(TIME_UNIT_MON)) {
            int increaseYear = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
            int increaseMonth = calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);
            return increaseMonth + increaseYear * 12;
        }

        if (interval.equals(TIME_UNIT_D)) {
            long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000 / 60 / 60 / 24;
            return (int) increaseDate;
        }

        if (interval.equals(TIME_UNIT_W)) {
            long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 7;
            return (int) increaseDate;

        }

        if (interval.equalsIgnoreCase(TIME_UNIT_H)) {
            long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000 / 60 / 60;
            return (int) increaseDate;

        }

        if (interval.equals(TIME_UNIT_M)) {
            long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000 / 60;
            return (int) increaseDate;
        }

        if (interval.equals(TIME_UNIT_S)) {
            long increaseDate = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000;
            return (int) increaseDate;
        }
        throw new Exception("interval error!");

    }

    public static int datePart(String interval, Date date) throws Exception {
        if (date == null) {
            log.error("Parameter is NULL!");
            throw new NullPointerException();
        }

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        // interval
        if (interval.equals(TIME_UNIT_Y)) {
            // return date.getYear() + 1900;
            return rightNow.get(Calendar.YEAR);
        }
        // interval
        if (interval.equals(TIME_UNIT_MON)) {
            // return date.getMonth() + 1;
            return rightNow.get(Calendar.MONTH) + 1;

        }
        // interval
        if (interval.equals(TIME_UNIT_D)) {
            // return date.getDate();
            return rightNow.get(Calendar.DAY_OF_MONTH);
        }
        // interval
        if (interval.equals(TIME_UNIT_H)) {
            // return date.getHours();
            return rightNow.get(Calendar.HOUR_OF_DAY);
        }
        // interval
        if (interval.equals(TIME_UNIT_M)) {
            // return date.getMinutes();
            return rightNow.get(Calendar.MINUTE);
        }
        // interval
        if (interval.equals(TIME_UNIT_S)) {
            // return date.getSeconds();
            return rightNow.get(Calendar.SECOND);
        }
        throw new Exception("interval error!");
    }

    public static Date dateSerial(int year, int month, int day) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(year, month - 1, day);
        return rightNow.getTime();
    }

    public static Date dateSerial(int year, int month, int day, int hour, int minute, int second) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(year, month - 1, day, hour, minute, second);
        return rightNow.getTime();
    }

    public static Date dateValue(String date) {
        String date_format;
        Date fDate = null;

        try {

            if (date.indexOf(":") > 0) {
                date_format = DATE_TIME_FORMAT;
            } else {
                date_format = DATE_FORMAT;
            }
            SimpleDateFormat formatter = new SimpleDateFormat(date_format);
            fDate = formatter.parse(date);

        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }

        return fDate;
    }

    public static Date dateValue(long millis) {
        Date fDate = null;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);

            fDate = calendar.getTime();

        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }

        return fDate;
    }

    public static String format(Date date, String pattern) {

        if (date == null) {
            log.warn("Parameter is NULL!");
            return null;
        }
        if (pattern == null) {
            return DateFormat.getDateInstance().format(date);
        } else if (pattern.trim().length() == 0) {
            return DateFormat.getDateInstance().format(date);
        } else if (pattern.equalsIgnoreCase(FORMAT_DEFAULT)) {
            return DateFormat.getDateInstance().format(date);
        } else if (pattern.equalsIgnoreCase(FORMAT_LONG)) {
            return DateFormat.getDateInstance(DateFormat.LONG).format(date);
        } else if (pattern.equalsIgnoreCase(FORMAT_MEDIUM)) {
            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        } else if (pattern.equalsIgnoreCase(FORMAT_SHORT)) {
            return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
        } else if (pattern.equalsIgnoreCase(FORMAT_FULL)) {
            return DateFormat.getDateInstance(DateFormat.FULL).format(date);
        } else if (pattern.equalsIgnoreCase(FORMAT_LONG)) {
            return DateFormat.getInstance().format(date);
        } else {
            return new SimpleDateFormat(pattern).format(date);
        }

    }

    public static String formatDate(Date date) {
        if (date == null) {
            log.warn("Parameter is NULL!");
            return null;
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);

    }

    public static String formatDate(String date) {
        String sdate = "";

        if (date == null) {
            sdate = "";
        } else if (date.equals("0000-00-00 00:00:00")) {
            sdate = "";
        } else {
            int index = date.indexOf(" ");
            if (index > 1) {
                sdate = date.substring(0, index);
            } else {
                sdate = date;
            }
        }

        return sdate;

    }

    public static String formatDateTime(String date) {
        String sdate = date;

        if (date == null) {
            sdate = "";
        } else if (date.equals("0000-00-00 00:00:00")) {
            sdate = "";
        } else {
            int pos = date.lastIndexOf(".");
            if (pos > -1) {
                sdate = date.substring(0, pos);
            }
        }

        return sdate;

    }

    public static String formatDateTime(Date date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_TIME_FORMAT);
        if (date == null) {
            return "";
        } else {
            return sdf.format(date);
        }
    }

    public static String formatNow() {
        java.text.DateFormat sdf = new java.text.SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(new Date());
    }

    public static String formatTime(Date date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(TIME_FORMAT);
        return sdf.format(date);
    }

    public static String formatTime(String date) {
        String stime = "";

        if (date == null) {
            stime = "";
        } else if (date.endsWith("00:00:00")) {
            stime = "";
        } else {
            int index = date.indexOf(" ");
            if (index > 1) {
                stime = date.substring(index + 1, date.length());
            } else {
                stime = date;
            }
        }

        return stime;

    }

    public static int hour(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        return rightNow.get(Calendar.HOUR_OF_DAY);
    }

    public static Date maxDateOfMonth(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.MONTH, 11);
        // date.setMonth(11);
        return rightNow.getTime();
    }

    public static Date minDateOfMonth(Date date) {
        // date.setMonth(0);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.MONTH, 0);
        return rightNow.getTime();
    }

    public static int minute(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        return rightNow.get(Calendar.MINUTE);
    }

    public static int month(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        return rightNow.get(Calendar.MONTH) + 1;
    }

    public static String monthName(int month, String lang) {
        if (lang.equals("zh")) {
            return month + "月";
        }
        if (lang.equals("en")) {
            return month + "month";
        }
        return "";
    }

    public static Date now() {
        return new Date();
    }

    public static Date timeSerial(int hour, int minute, int second) {
        /*
         * Date tempDate = new Date(); tempDate.setHours(hour); tempDate.setMinutes(minute);
         * tempDate.setSeconds(second); return tempDate;
         */
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Calendar.HOUR_OF_DAY, hour);
        rightNow.set(Calendar.MINUTE, minute);
        rightNow.set(Calendar.SECOND, second);
        return rightNow.getTime();

    }

    public static Date timeValue(String time) {
        try {

            // Date tempDate = new Date();
            Calendar rightNow = Calendar.getInstance();
            String newHourString;
            String newMinuteString;
            String newSecondString;
            newHourString = time.substring(0, time.indexOf(":"));
            newMinuteString = time.substring(time.indexOf(":") + 1, time.lastIndexOf(":"));
            newSecondString = time.substring(time.lastIndexOf(":") + 1, time.length());
            Integer newHourInt = new Integer(newHourString);
            Integer newMinuteInt = new Integer(newMinuteString);
            Integer newSecondInt = new Integer(newSecondString);
            /*
             * tempDate.setHours(newHourInt.intValue()); tempDate.setMinutes(newMinuteInt.intValue());
             * tempDate.setSeconds(newSecondInt.intValue()); return tempDate;
             */
            rightNow.set(Calendar.HOUR_OF_DAY, newHourInt.intValue());
            rightNow.set(Calendar.MINUTE, newMinuteInt.intValue());
            rightNow.set(Calendar.SECOND, newSecondInt.intValue());
            return rightNow.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int weekday(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        int sun = 7;
        /*
         * if (date.getDay() == 0) { return sun; } return date.getDay();
         */
        if (rightNow.get(Calendar.DAY_OF_WEEK) == 1) {
            return sun;
        }
        return rightNow.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String weekdayName(int weekday, String lang) {
        if (lang.equals("zh")) {
            switch (weekday) {
                case 1:
                    return "星期一";
                case 2:
                    return "星期二";
                case 3:
                    return "星期三";
                case 4:
                    return "星期四";
                case 5:
                    return "星期五";
                case 6:
                    return "星期六";
                case 7:
                    return "星期日";
            }
        }
        if (lang.equals("en")) {
            switch (weekday) {
                case 1:
                    return "Monthday";
                case 2:
                    return "Tuesday";
                case 3:
                    return "Wednesday";
                case 4:
                    return "Tuesday";
                case 5:
                    return "Friday";
                case 6:
                    return "Saturday";
                case 7:
                    return "Sunday";
            }
        }
        return "";
    }

    public static int year(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        return rightNow.get(Calendar.YEAR);
    }

    String lang;

    protected DateUtil() {
    }

    protected DateUtil(String lang) {
        this.lang = lang;
    }

    public static String getWeekday(String date, String lang) {
        String val = formatDate(date);
        String strDate = val + " " + weekdayName(weekday(dateValue(val)), lang);
        return strDate;
    }

    public static boolean isDate(String date) {
        Date tmpDate;
        tmpDate = dateValue(date);
        if (tmpDate == null) {
            return false;
        }
        return true;

    }

    public static long daysBetween(Calendar start, Calendar end) throws BizException {
        if (start == null || end == null) {
            throw new BizException("日期参数为null");
        }
        // return date1.getTime() / (24*60*60*1000) - date2.getTime() / (24*60*60*1000);
        return end.getTimeInMillis() / 86400000 - start.getTimeInMillis() / 86400000; // 用立即数，减少乘法计算的开销
    }

    /**
     * 根据传入的字符串，得到holdTime，得到的时间为1970年的HH:mm:ss 如果格式不对(不为例如:23:12:23或着23:56或着23)，抛出异常
     * 
     * @param str
     * @return
     */
    public static Date getHoldTimeDate(String str) throws Exception {
        boolean flag = false;
        Pattern p1 = Pattern.compile("(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})");
        Matcher m1 = p1.matcher(str);
        Pattern p2 = Pattern.compile("(0\\d{1}|1\\d{1}|2[0-3])");
        Matcher m2 = p2.matcher(str);
        Pattern p3 = Pattern.compile("(0\\d{1}|1\\d{1}|2[0-4]):[0-5]\\d{1}");
        Matcher m3 = p3.matcher(str);
        if (m1.matches() || m2.matches() || m3.matches()) {
            flag = true;
            if (m3.matches()) {
                return string2Date(str, "HH:mm");
            }
            if (m1.matches()) {
                return string2Date(str, "HH:mm:ss");
            }

            if (m2.matches()) {
                return string2Date(str, "HH");
            }
        }

        if (!flag) {
            throw new XmlException("holdTime format is invalid!");
        }
        return null;
    }
    /**
     * 把日期字符串格式化为日期类型
     * 
     * @param datetext 日期字符串
     * @param format 日期格式，如果不传则使用"yyyy-MM-dd HH:mm:ss"格式
     * @return
     */
    public static Date string2Date(String datetext, String format) {
        try {
            SimpleDateFormat df;
            if (datetext == null || "".equals(datetext.trim())) {
                return new Date();
            }
            if (format != null) {
                df = new SimpleDateFormat(format);
            } else {
                datetext = datetext.replaceAll("/", "-");
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }

            Date date = df.parse(datetext);

            return date;

        }

        catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }
    public static String formatTimeHHmm(Date d) {
        if (d == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            return sdf.format(d);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
    
    /**
     * 把一段日期 按月拆分
     * @author Denile.Zhang
     * @date 2012-8-30
     * @param startDate
     * @param endDate
     * @return 日期分段的List<String> 每个String用'#'分割起始日期和接触日期
     */
    public static List<String> splitDateToMonth(Date startDate,Date endDate){
    	
    	List<String> splitDate = new ArrayList<String>();//每一对日期 用 #区分
    	Calendar startCa = Calendar.getInstance();
    	startCa.setTime(startDate);
    	Calendar endCa = Calendar.getInstance();
    	endCa.setTime(endDate);
    	
    	//如果在同一个月内 就直接返回
    	if(startCa.get(Calendar.YEAR) == endCa.get(Calendar.YEAR) && startCa.get(Calendar.MONTH) == endCa.get(Calendar.MONTH)){//如果是同年同学的话 就直接返回
    		splitDate.add(DateUtil.formatDate(startCa.getTime()) + "#" + DateUtil.formatDate(endCa.getTime()));
    		return splitDate;
    	}
    	
    	
    	//求起始时间到月底
    	Calendar lastDayOfMonthCa = Calendar.getInstance();
    	lastDayOfMonthCa.setTime(startDate);
    	
    	
    	while(lastDayOfMonthCa.getTimeInMillis() < endCa.getTimeInMillis()){
    		
    		Date firstDayOfMonthCa = null;//月首
    		//1、求月底
    		lastDayOfMonthCa.add(Calendar.MONTH, 1);//
    		lastDayOfMonthCa.set(Calendar.DAY_OF_MONTH, 1);
    		lastDayOfMonthCa.add(Calendar.DAY_OF_MONTH, -1);
    		
    		//2、放入集合
        	splitDate.add(DateUtil.formatDate(startCa.getTime()) + "#" + DateUtil.formatDate(lastDayOfMonthCa.getTime()));
        	
        	
        	//3、月首变成下个月1号
        	startCa.add(Calendar.MONTH, 1);
        	startCa.set(Calendar.DAY_OF_MONTH, 1);
        	
        	//4、加一天 变成下月初
        	lastDayOfMonthCa.add(Calendar.DAY_OF_MONTH, 1);
        	//如果下一个月 就是结束日期的月份 就直接保存 然后退出
        	if(lastDayOfMonthCa.get(Calendar.YEAR) == endCa.get(Calendar.YEAR) && lastDayOfMonthCa.get(Calendar.MONTH) == endCa.get(Calendar.MONTH)){
        		splitDate.add(DateUtil.formatDate(lastDayOfMonthCa.getTime()) + "#" + DateUtil.formatDate(endCa.getTime()));
        		break;
        	}
    	}
    	
    	return splitDate;
    }
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        try {
            int days = DateUtil.dateDiff("d", calendar.getTime(), new Date());
            System.out.println(days);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
