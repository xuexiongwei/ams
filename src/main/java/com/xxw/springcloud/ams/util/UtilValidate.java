package com.xxw.springcloud.ams.util;

import java.util.Collection;

public class UtilValidate {
	
	/**
	 * 判断对象是否 空 或 null
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		if(obj == null){
			return true;
		}
		//字符串
		if(obj instanceof String){
			return obj.equals("");
		}
		if(obj instanceof StringBuffer){
			return obj.toString().equals("");
		}
		//集合
		if(obj instanceof Collection){
			return (((Collection)obj).size() == 0);
		}
		return false;
	}
	/**
	 * 判断是否非空 null
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}

}
