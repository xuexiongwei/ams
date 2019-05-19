package com.xxw.springcloud.ams.mapper.file;

import org.apache.ibatis.annotations.Insert;

import com.xxw.springcloud.ams.model.Xmmx;

public interface XmmxMapper {

	// 保存项目明细
	@Insert("insert into ams_bus_xmmx (prjSN, serialNumber, serialFunct, aboveGroundArea, underGroundArea, blendArea, aboveGroundLen, prjClasfiCode) "
			+ "values (#{prjSN}, #{serialNumber}, #{serialFunct}, #{aboveGroundArea}, #{underGroundArea}, #{blendArea}, #{aboveGroundLen}, #{prjClasfiCode})")
	void saveXmmx(Xmmx mx);

}