package com.xxw.springcloud.ams.mapper.file;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.xxw.springcloud.ams.model.ClassifiDic;

public interface DicMapper {

	// 保存字典信息
	@Insert("insert into ams_bus_classifi_dic (id,parentID, type, code, name, sort, other, user_create) values "
			+ "(#{id},#{parentID}, #{type}, #{code}, #{name}, #{sort}, #{other}, #{userCreate})")
	void saveDic(ClassifiDic dic);

	// 更新字典信息
	@Select("update ams_bus_classifi_dic set code=#{code},name=#{name} where id=#{id}")
	void updateDic(ClassifiDic dic);

	// 根据字典 名称，类型，父ID查询字典
	@Select("select * from ams_bus_classifi_dic where type=#{type} and parentID =#{parentID} and name=#{name}")
	ClassifiDic queryDicByName(Map<String, Object> params);

	// 根据字典码值，类型，父ID查询字典
	@Select("select * from ams_bus_classifi_dic where type=#{type} and parentID =#{parentID} and code=#{code}")
	ClassifiDic queryDicByCode(Map<String, Object> params);

	// 根据字典码值，类型查询字典
	@Select("select * from ams_bus_classifi_dic where type=#{type} and code=#{code}")
	ClassifiDic queryDicByCode2(Map<String, Object> params);

}