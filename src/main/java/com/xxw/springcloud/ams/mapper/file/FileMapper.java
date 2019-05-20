package com.xxw.springcloud.ams.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.xxw.springcloud.ams.model.BusFile;

public interface FileMapper {

	// 保存文档信息
	@Insert("insert into ams_bus_file (prjSN, updateTime, fileName, urlName, delFlag, fileType) values "
			+ "(#{prjSN},#{updateTime},#{fileName},#{urlName},#{delFlag},#{fileType})")
	void saveFile(BusFile file);

	// 查询文档信息
	@Select("select * from ams_bus_file where prjSN=#{prjSN} and delFlag=#{delFlag}")
	List<BusFile> queryBusFiles(Object params);

	// 查询指定ID的文档信息
	@Select("select * from ams_bus_file where id=#{id}")
	BusFile queryBusFileByID(Object params);

	// 刪除文档
	@Select("update ams_bus_file set delFlag='D' where id=#{id}")
	void delFile(Object params);

}