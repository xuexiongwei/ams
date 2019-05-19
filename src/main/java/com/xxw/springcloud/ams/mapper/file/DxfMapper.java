package com.xxw.springcloud.ams.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.xxw.springcloud.ams.model.DxfEntity;

public interface DxfMapper {

	// 保存dxf信息
	@Insert("insert into ams_bus_dxf_entity (constrID, fileName,prjSN, longlatV) values"
			+ " (#{constrID}, #{fileName}, #{prjSN}, #{longlatV})")
	void saveDxfEntity(DxfEntity dxf);

	// 查询图元信息
	@Select("select * from ams_bus_dxf_entity where prjSN=#{prjSN}")
	List<DxfEntity> queryDxfEntity(Object params);

	// 查询图元信息
	@Select("select * from ams_bus_dxf_entity where prjSN=#{prjSN} and longlatV=#{longlatV} and fileName=#{fileName}")
	List<DxfEntity> queryDxfEntity2(Object params);

	// 删除图元信息
	@Delete("delete from ams_bus_dxf_entity where prjSN=#{prjSN} and fileName=#{fileName}")
	void delDxfEntityByPrjSNAndFileName(Object params);

}