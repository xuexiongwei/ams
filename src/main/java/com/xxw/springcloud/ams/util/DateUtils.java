package com.xxw.springcloud.ams.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期处理
 * 
 * @author uisftech
 *
 */
public class DateUtils {

	public static String FORMAT0 = "yyyy-MM-dd HH:mm:ss:SSS";
	public static String FORMAT1 = "yyyy-MM-dd HH:mm:ss";
	public static String FORMAT2 = "yyyy-MM-dd";
	public static String FORMAT3 = "yyyyMMdd";
	public static String FORMAT4 = "HH:mm:ss";
	public static String FORMAT5 = "HHmmss";
	public static String FORMAT6 = "yyyy/MM/dd";

	/** 锁对象 */
	private static final Object lockObj = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	/**
	 * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDataStr(String pattern, Date date) {
		return getSdf(pattern).format(date);
	}

	public static Date parse(String dateStr, String pattern) throws ParseException {
		return getSdf(pattern).parse(dateStr);
	}

	public static String getTimeStr(String pattern, Date date) {
		return getSdf(pattern).format(date);
	}

	/**
	 * Return a string formatted as format
	 *
	 * @return String formatted for right now
	 */
	public static String nowDateString(String format) {
		return getDataStr(format, new Date());
	}

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 * 
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					tl = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}

	/**
	 * 增加或减少天数，重新计算日期
	 * 
	 * @param day
	 * @return
	 */
	public static String dayJiaJian(Date date, int day) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		return sf.format(c.getTime());
	}

	/**
	 * 增加或减少月数，重新计算日期
	 * 
	 * @param month
	 * @return
	 */
	public static String monthJiaJian(Date date, int month) {
		Calendar curr = Calendar.getInstance();
		curr.setTime(date);
		curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) + month); // 月
		Date d = curr.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(d);
	}

	/**
	 * excel 日期转换
	 * 
	 * @param use1904windowing
	 * @param value
	 * @return
	 */
	public static String getPOIDate(boolean use1904windowing, double value) {
		int wholeDays = (int) Math.floor(value);
		int millisecondsInDay = (int) ((value - (double) wholeDays) * 8.64E7D + 0.5D);
		Calendar calendar = new GregorianCalendar();
		short startYear = 1900;
		byte dayAdjust = -1;
		if (use1904windowing) {
			startYear = 1904;
			dayAdjust = 1;
		} else if (wholeDays < 61) {
			dayAdjust = 0;
		}
		calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, millisecondsInDay);
		if (calendar.get(Calendar.MILLISECOND) == 0) {
			calendar.clear(Calendar.MILLISECOND);
		}
		Date date = calendar.getTime();
		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd");
		return s.format(date);
	}
}
