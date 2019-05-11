package com.xxw.springcloud.ams.mapper.common;

import org.apache.ibatis.annotations.Select;

public interface SqlMapper {
	
	@Select(value = "select _nextval('seq')")
	Integer GetSeqNum();
	
}
