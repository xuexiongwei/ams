package com.xxw.springcloud.ams.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用此方法，防止字符串内存泄漏
 * 
 * @author uisftech
 *
 */
public class StringUtils {

	/**
	 * 获取部分字符串
	 * 
	 * @param source
	 * @param start
	 * @param end
	 * @return
	 */
	public static String substring(String source, int start, int end) {
		if (source == null || source.equals("")) {
			return "";
		} else {
			return new String(source.substring(start, end));
		}
	}

	/**
	 * 获取部分字符串
	 * 
	 * @param source
	 * @param start
	 * @return
	 */
	public static String substring(String source, int start) {
		if (source == null || source.equals("")) {
			return "";
		} else {
			return new String(source.substring(start));
		}
	}

	/**
	 * 四舍五入
	 * 
	 * @param d
	 * @return
	 */
	public static Double sswr(Double d) {
		if (null == d)
			return 0.0d;
		BigDecimal b = new BigDecimal(d);
		double f1 = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * 获取非空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getStr(Object obj) {
		if (null == obj) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 提取许可证中年份
	 * 
	 * @param str
	 * @return
	 */
	public static String getYear(String str) {
		Pattern p = Pattern.compile("[0-9\\.]+");
		Matcher m = p.matcher(str);
		while (m.find()) {
			String date = m.group();
			if ("1900".compareTo(date) < 0 && "2999".compareTo(date) > 0) {
				return date;
			}
		}
		return "";
	}
}
