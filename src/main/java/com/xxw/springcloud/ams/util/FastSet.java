package com.xxw.springcloud.ams.util;

import java.util.HashSet;
import java.util.Set;

public class FastSet<T> {

	public static <T> Set<T> newInstance() {
		return new HashSet<T>();
	}
}
