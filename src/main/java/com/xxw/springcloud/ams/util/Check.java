package com.xxw.springcloud.ams.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式验证
 * 
 * @author yangwei
 *
 */
public class Check {
	// 验证日期格式 yyyy/mm/dd
	public static String date1 = "^\\d{4}\\/\\d{2}\\/\\d{2}$";
	// 验证正整数
	public static String zzs = "^[0-9]*$";
	// 非零开头的最多带两位小数的数字
	public static String zfs = "^([0-9]*)+(.[0-9]{1,2})?$";

	public static boolean check(String eL, Object data) {
		if (UtilValidate.isEmpty(data)) {
			return false;
		}
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(data + "");
		boolean b = m.matches();
		if (b) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		System.out.println(check(zfs, "0.00"));
	}

}
