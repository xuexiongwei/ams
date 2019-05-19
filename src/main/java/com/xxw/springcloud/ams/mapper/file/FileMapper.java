package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.BusFile;
import com.xxw.springcloud.ams.model.ClassifiDic;
import com.xxw.springcloud.ams.model.DxfEntity;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.model.Xmsx;

public interface FileMapper {

	
	@Select("SELECT * FROM ams_bus_file WHERE superId = #{superId}")
	List<BusFile> getBusFileByProjectID(Long superId);

	@Insert("INSERT INTO ams_bus_file(superId,updateTime,fileName,delFlag,fileType,user_create,gmt_create) "
			+ "VALUES(#{superId}, #{updateTime}, #{fileName},#{delFlag},#{fileType},#{userCreate},#{gmtCreate})")
	void insertBusFile(BusFile busFile);

	@Update("UPDATE ams_bus_file SET delFlag=1 WHERE id =#{id}")
	void delBusFileByID(Long id);
	
	//添加图元信息
	@Insert("INSERT INTO ams_bus_entity(constrID,fileID,longitude,latitude,user_create,gmt_create) "
			+ "VALUES(#{constrID}, #{fileID}, #{longitude},#{latitude},#{userCreate},#{gmtCreate})")
	void insertBusDxfEntity(DxfEntity dxfps);
	
	//通过项目ID查询此项目（dxf）下图像信息
	@Select("SELECT * FROM ams_bus_dxf_entity WHERE fileID = #{fileID}")
	List<DxfEntity> getDxfEntityByProjectID(Long fileID);
	
	
	/***************************************************************************20190518*********************************************************/
	
	//保存项目基本信息
	@Select("insert into ams_bus_xmjbxx (prjSN, prjUnit, prjAdr, prjName, prjType, contacts, contactInf, prjTemSN, specialNotifi,"
			+ " noticeTime, effectiveTime, remark) values"
			+ "(#{prjSN}, #{prjUnit}, #{prjAdr}, #{prjName}, #{prjType}, #{contacts}, #{contactInf}, #{prjTemSN}, #{specialNotifi},"
			+ " #{noticeTime}, #{effectiveTime}, #{remark})")
	void saveXmjbxx(Xmjbxx jbxx);
	//查询项目基本信息
	@Select("select * from ams_bus_xmjbxx where prjSN=#{prjSN}")
	Xmjbxx queryXmjbxx(String prjSN);
	@Select("update ams_bus_xmjbxx set prjUnit=#{prjUnit},prjAdr=#{prjAdr}, prjName=#{prjName}, prjType=#{prjType}, contacts=#{contacts}," 
			+ "contactInf=#{contactInf}, prjTemSN=#{prjTemSN}, specialNotifi=#{specialNotifi},noticeTime=#{noticeTime},"
			+ "effectiveTime=#{effectiveTime}, remark=#{remark} where prjSN=#{prjSN}")
	void updateXmjbxx(Xmjbxx jbxx);
	//保存项目属性
	@Select("insert into ams_bus_xmsx (prjSN, serialNumber, prjNature, prjAttr, strucType, peacetimeUses, aboveGroundLev, "
			+ "underGroundLev, aboveGroundHet, underGroundHet, buildings, housingStockNum, checkDocSN, checkDocDate, checkSN, "
			+ "checkDate, delaySN, delayCountDay, cancelSN, cancelDate, correctionSN, correctionDate, imgJudgeRes, exproprInfo, "
			+ "remark) "
			+ "values (#{prjSN}, #{serialNumber}, #{prjNature}, #{prjAttr}, #{strucType}, #{peacetimeUses}, #{aboveGroundLev}, "
			+ "#{underGroundLev}, #{aboveGroundHet}, #{underGroundHet}, #{buildings}, #{housingStockNum}, #{checkDocSN}, #{checkDocDate}, #{checkSN},"
			+ "#{checkDate}, #{delaySN}, #{delayCountDay}, #{cancelSN}, #{cancelDate}, #{correctionSN},  #{correctionDate}, #{imgJudgeRes}, #{exproprInfo},"
			+ "#{remark})")
	void saveXmsx(Xmsx sx);
	
	//保存项目属性
	@Select("insert into ams_bus_xmmx (prjSN, serialNumber, serialFunct, aboveGroundArea, underGroundArea, blendArea, aboveGroundLen, prjClasfiCode) "
			+ "values (#{prjSN}, #{serialNumber}, #{serialFunct}, #{aboveGroundArea}, #{underGroundArea}, #{blendArea}, #{aboveGroundLen}, #{prjClasfiCode})")
	void saveXmmx(Xmmx mx);
	
	
	/***************************************************************************20190519*********************************************************/
	//保存字典信息
	@Select("insert into ams_bus_classifi_dic (id,parentID, type, code, name, sort, other, user_create) values "
			+ "(#{id},#{parentID}, #{type}, #{code}, #{name}, #{sort}, #{other}, #{userCreate})" )
	void saveDic(ClassifiDic dic);
	//更新字典信息
	@Select("update ams_bus_classifi_dic set code=#{code},name=#{name} where id=#{id}" )
	void updateDic(ClassifiDic dic);
	
	@Select("select * from ams_bus_classifi_dic where type=#{type} and parentID =#{parentID} and name=#{name}")
	ClassifiDic queryDicByName(Map<String,Object> params);
	@Select("select * from ams_bus_classifi_dic where type=#{type} and parentID =#{parentID} and code=#{code}")
	ClassifiDic queryDicByCode(Map<String,Object> params);
	@Select("select * from ams_bus_classifi_dic where type=#{type} and code=#{code}")
	ClassifiDic queryDicByCode2(Map<String,Object> params);
	
}