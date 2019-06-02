package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.xxw.springcloud.ams.model.ClassifiDic;

public interface DicMapper {

	// 保存字典信息
	@Insert("insert into ams_bus_classifi_dic (id,parentID, type, code, name, sort, other, user_create) values "
			+ "(#{id},#{parentID}, #{type}, #{code}, #{name}, #{sort}, #{other}, #{userCreate})")
	void saveDic(ClassifiDic dic);

	// 保存字典信息
	@Insert("insert into ams_bus_classifi_dic (id,parentID, type, code, name, sort, other, user_create) values "
			+ "(#{id},#{parentID}, #{type}, #{code}, #{name}, #{sort}, #{other}, #{userCreate})")
	void saveDic2(Map<String, Object> params);

	// 更新字典信息
	@Select("update ams_bus_classifi_dic set code=#{code},name=#{name} where id=#{id}")
	void updateDic(ClassifiDic dic);

	// 根据字典 名称，类型，父ID查询字典
	@Select("select * from ams_bus_classifi_dic where type=#{type} and parentID =#{parentID} and name=#{name}")
	ClassifiDic queryDicByName(Map<String, Object> params);

	// 根据字典类型，父ID查询字典
	@Select("select * from ams_bus_classifi_dic where type=#{type} and parentID =#{parentID}")
	List<ClassifiDic> queryDicByCode(Map<String, Object> params);

	@Select("select distinct name from ams_bus_classifi_dic where type=#{type} and name like \"%\"#{name}\"%\" and other=#{other}")
	List<String> queryDicNameByType(Map<String, Object> params);

	// 根据字典码值，类型查询字典
	@Select("select * from ams_bus_classifi_dic where type=#{type} and code=#{code}")
	ClassifiDic queryDicByCode2(Map<String, Object> params);

	// 根据字典类型查询所有字典
	@Select("select * from ams_bus_classifi_dic where type=#{type}")
	List<ClassifiDic> queryDicByCode3(Map<String, Object> params);

	@Select("select * from ams_bus_classifi_dic where name like \"%\"#{name}\"%\" and other=#{other}")
	List<ClassifiDic> queryDicByNameLike(Map<String, Object> params);

	// 通过字典类型删除字典
	@Delete("delete from ams_bus_classifi_dic where type=#{type}")
	void delDicByType(String type);

}