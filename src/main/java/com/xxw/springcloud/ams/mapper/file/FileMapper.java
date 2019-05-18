package com.xxw.springcloud.ams.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.BusDxfPosition;
import com.xxw.springcloud.ams.model.BusDxfTips;
import com.xxw.springcloud.ams.model.BusFile;

public interface FileMapper {

	
	@Select("SELECT * FROM ams_bus_file WHERE superId = #{superId}")
	List<BusFile> getBusFileByProjectID(Long superId);

	@Insert("INSERT INTO ams_bus_file(superId,updateTime,fileName,delFlag,fileType,user_create,gmt_create) "
			+ "VALUES(#{superId}, #{updateTime}, #{fileName},#{delFlag},#{fileType},#{userCreate},#{gmtCreate})")
	void insertBusFile(BusFile busFile);

	@Update("UPDATE ams_bus_file SET delFlag=1 WHERE id =#{id}")
	void delBusFileByID(Long id);
	
	//添加图元信息
	@Insert("INSERT INTO ams_bus_file(constrID,fileID,longitude,latitude,user_create,gmt_create) "
			+ "VALUES(#{constrID}, #{fileID}, #{longitude},#{latitude},#{userCreate},#{gmtCreate})")
	void insertBusDxfPosition(BusDxfPosition dxfps);
	
	//通过项目ID查询此项目（dxf）下图像信息
	@Select("SELECT * FROM ams_bus_dxf_position WHERE fileID = #{fileID}")
	List<BusDxfPosition> getDxfPositionsByProjectID(Long fileID);
	
	//添加标点信息
	@Insert("INSERT INTO ams_bus_dxf_tips(dxfID,remark,longitude,latitude,user_create,gmt_create) "
			+ "VALUES(#{dxfID}, #{remark}, #{longitude},#{latitude},#{userCreate},#{gmtCreate})")
	void insertBusDxfTips(BusDxfTips tips);
	
	//更新标点信息
	@Insert("UPDATE ams_bus_dxf_tips SET remark=#{remark},user_modified=#{userModified},gmt_modified=#{gmtModified} WHERE id=#{id}")
	void updata(BusDxfTips tips);
	
	//删除标点信息
	@Insert("DELETE FROM ams_bus_dxf_tips WHERE id=#{id}")
	void delBusDxfTipsByID(Long id);
	
	//查询标点信息
	@Select("SELECT * FROM ams_bus_dxf_tips WHERE dxfID = #{dxfID}")
	List<BusDxfTips> getBusDxfTipsByProjectID(Long dxfID);
}