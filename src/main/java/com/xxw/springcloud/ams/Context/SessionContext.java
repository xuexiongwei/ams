package com.xxw.springcloud.ams.Context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContext {
	
	private static ThreadLocal<Map<String,Object>> context = new ThreadLocal<Map<String,Object>>();
	
	/**
	 * 初始化
	 */
	public static void init() {
        Map<String,Object> m = context.get();
        if (null == m) {
            m = new ConcurrentHashMap<String,Object>(64);
            context.set(m);
        }
    }
	
	public static boolean isInit() {
        Map<String,Object> m = context.get();
        if(m == null) {
        	return false;
        }
        return true;
    }
	
	/**
	 * 设置值
	 * @param name
	 * @param value
	 */
	public static void set(String name, Object value) {
        if (null == name) {
            throw new java.lang.IllegalArgumentException("name is null");
        }
        Map<String,Object> m = context.get();
        if (null == m) {
            return;
        }
        m.put(name, value);
    }
	
	/**
	 * 获取值
	 * @param name
	 * @return
	 */
	public static Object get(String name) {
        if (null == name) {
            throw new java.lang.IllegalArgumentException("name is null");
        }
        Map<String,Object> m = context.get();
        if (null == m) {
            return null;
        } else {
            Object obj = m.get(name);
            if (obj == null) {
            	return null;
            }
            return obj;
        }
    }
	
	public static void cleanAll() {
		context.remove();
	}
	
	public enum FieldId {
		serialNumber;
	}
}
