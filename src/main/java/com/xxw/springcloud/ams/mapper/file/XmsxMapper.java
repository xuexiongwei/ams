package com.xxw.springcloud.ams.mapper.file;

import org.apache.ibatis.annotations.Insert;

import com.xxw.springcloud.ams.model.Xmsx;

public interface XmsxMapper {

	// 保存项目属性
	@Insert("insert into ams_bus_xmsx (prjSN, serialNumber, prjNature, prjAttr, strucType, peacetimeUses, aboveGroundLev, "
			+ "underGroundLev, aboveGroundHet, underGroundHet, buildings, housingStockNum, checkDocSN, checkDocDate, checkSN, "
			+ "checkDate, delaySN, delayCountDay, cancelSN, cancelDate, correctionSN, correctionDate, imgJudgeRes, exproprInfo, "
			+ "remark) "
			+ "values (#{prjSN}, #{serialNumber}, #{prjNature}, #{prjAttr}, #{strucType}, #{peacetimeUses}, #{aboveGroundLev}, "
			+ "#{underGroundLev}, #{aboveGroundHet}, #{underGroundHet}, #{buildings}, #{housingStockNum}, #{checkDocSN}, #{checkDocDate}, #{checkSN},"
			+ "#{checkDate}, #{delaySN}, #{delayCountDay}, #{cancelSN}, #{cancelDate}, #{correctionSN},  #{correctionDate}, #{imgJudgeRes}, #{exproprInfo},"
			+ "#{remark})")
	void saveXmsx(Xmsx sx);

}