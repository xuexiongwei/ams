package com.xxw.springcloud.ams.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.BusFile;

public interface FileMapper {

	// 保存文档信息
	@Insert("insert into ams_bus_file (prjSN, updateTime, fileName, urlName, fileType) values "
			+ "(#{prjSN},#{updateTime},#{fileName},#{urlName},#{fileType})")
	void saveFile(BusFile file);

	// 查询文档信息
	@Select("select * from ams_bus_file where prjSN=#{prjSN}")
	List<BusFile> queryBusFiles(Object params);

	// 查询指定ID的文档信息
	@Select("select * from ams_bus_file where id=#{id}")
	BusFile queryBusFileByID(Object params);

	@Select("select * from ams_bus_file where fileName=#{fileName}")
	BusFile queryBusFileByName(Object params);

	// 更新文档
	@Update("delete from ams_bus_file where id=#{id}")
	void delBusFileByID(Object params);

}