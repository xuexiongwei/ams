package com.xxw.springcloud.ams.util;

import java.util.ArrayList;
import java.util.List;

public class FastList<T>{

	public static <T> List<T> newInstance()
	  {
	    return new ArrayList<T>();
	  }
}
