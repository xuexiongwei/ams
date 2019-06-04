package com.xxw.springcloud.ams.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class BeanUtils {

	// 该方法的参数列表是一个类的 类名、成员变量、给变量的赋值
	public static Map<String, Object> setProperty(Class c, String[] PropertyNames, List<Object> values)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Map<String, Object> valM = FastMap.newInstance();

		// 获取obj类的字节文件对象
		for (int i = 0; i < PropertyNames.length; i++) {
			// 获取该类的成员变量
			Object v = values.get(i);

			if (null == v) {
				continue;
			}

			Field f = c.getDeclaredField(PropertyNames[i]);

			Class cls = (Class) f.getGenericType();
			if (cls.equals(Long.class)) {
				v = Long.parseLong(v + "");
			} else if (cls.equals(Double.class)) {
				v = Double.valueOf(v + "");
			} else if ("noticeTime|correctionDate|checkDocDate|checkDate|cancelDate".indexOf(PropertyNames[i]) != -1) {// 日期转换

				if (Check.check(Check.zzs, v + "")) {
					v = DateUtils.getPOIDate(false, Double.valueOf(v + ""));
				}
			}
			// 给变量赋值
			valM.put(PropertyNames[i], v);
		}

		return valM;
	}
}
