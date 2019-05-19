package com.xxw.springcloud.ams.util;

import java.util.HashMap;
import java.util.Map;

public class FastMap<K,V> {
	
	public static <K, V> Map<K, V> newInstance()
	  {
	    return new HashMap<K,V>();
	  }

}
