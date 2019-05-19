package com.xxw.springcloud.ams.util;

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

}
