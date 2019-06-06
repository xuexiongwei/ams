package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.UtilValidate;

public interface XmsxMapper {

	// 保存项目属性
	@Insert("insert into ams_bus_xmsx (prjSN, serialNumber, prjNature, prjAttr, strucType, peacetimeUses, aboveGroundLev, "
			+ "underGroundLev, aboveGroundHet, underGroundHet, buildings, housingStockNum, checkDocSN, checkDocDate, checkSN, "
			+ "checkDate, cancelSN, cancelDate, imgJudgeRes, exproprInfo, remark) "
			+ "values (#{prjSN}, #{serialNumber}, #{prjNature}, #{prjAttr}, #{strucType}, #{peacetimeUses}, #{aboveGroundLev}, "
			+ "#{underGroundLev}, #{aboveGroundHet}, #{underGroundHet}, #{buildings}, #{housingStockNum}, #{checkDocSN}, #{checkDocDate}, #{checkSN},"
			+ "#{checkDate}, #{cancelSN}, #{cancelDate}, #{imgJudgeRes}, #{exproprInfo},#{remark})")
	void saveXmsx(Map<String, Object> params);

	// 更新项目属性
	@Update("update ams_bus_xmsx set prjNature=#{prjNature}, prjAttr=#{prjAttr}, strucType=#{strucType}, "
			+ "peacetimeUses=#{peacetimeUses}, aboveGroundLev=#{aboveGroundLev}, underGroundLev=#{underGroundLev}, aboveGroundHet=#{aboveGroundHet}, "
			+ "underGroundHet=#{underGroundHet}, buildings=#{buildings}, housingStockNum=#{housingStockNum}, checkDocSN=#{checkDocSN}, checkDocDate=#{checkDocDate},"
			+ "checkSN=#{checkSN},checkDate=#{checkDate}, cancelSN=#{cancelSN}, cancelDate=#{cancelDate}, "
			+ "imgJudgeRes=#{imgJudgeRes}, exproprInfo=#{exproprInfo},remark=#{remark} where id=#{id}")
	void updateXmsx(Map<String, Object> params);

	// 删除项目属性
	@Delete("delete from ams_bus_xmsx where id=#{id}")
	void delXmsx(Map<String, Object> params);

	@Delete("delete from ams_bus_xmsx where prjSN=#{prjSN}")
	void delXmsxByPrjSN(String prjSN);

	@Select("select * from ams_bus_xmsx where id=#{id}")
	Xmsx queryXmsxByID(Long id);

	// 用于判断项目及工程状态
	@Select("select distinct prjSN,serialNumber,prjNature,checkSN,checkDate,cancelSN,cancelDate from ams_bus_xmsx where prjSN=#{prjSN}")
	List<Xmsx> queryXmsxByPrjSN(String prjSN);

	@Select("select MAX(serialNumber) from ams_bus_xmsx where prjSN=#{prjSN}")
	int queryXmsxMaxIndexByPrjSN(String prjSN);

	@Select("select distinct prjSN,serialNumber,prjAttr from ams_bus_xmsx where prjSN=#{prjSN} and serialNumber=#{serialNumber}")
	List<Xmsx> queryXmsxByPrjSNAndSerialNumber(Map<String, Object> params);

	// 用于判断工程状态
	@Select("select distinct prjSN,checkSN,checkDate,cancelSN,cancelDate from ams_bus_xmsx where prjSN=#{prjSN} and serialNumber=#{serialNumber}")
	List<Xmsx> queryXmsxByPrjSNAndSerialNumber2(Map<String, Object> params);

	// 更新工程状态
	@Select("update ams_bus_xmsx set buldStatus=#{buldStatus} where prjSN=#{prjSN} and serialNumber=#{serialNumber}")
	void updateBuldStatusByPrjSNAndSerialNumber(Map<String, Object> params);

	// 查询属性信息
	@SelectProvider(type = XmsxProvider.class, method = "findXmsxByAttr")
	public List<Xmsx> findXmsxByAttr(Map<String, Object> params);

	@SelectProvider(type = XmsxProvider.class, method = "findXmsxByAttrCount")
	public int findXmsxByAttrCount(Map<String, Object> params);

	class XmsxProvider {
		public String findXmsxByAttr(Map<String, Object> params) {
			String sql = "SELECT *  " + createSql(params);
			if (UtilValidate.isNotEmpty(params.get("pageSize")) && UtilValidate.isNotEmpty(params.get("pageIndex"))) {
				sql += " limit #{pageSize} offset #{pageIndex}";
			}
			return sql;
		}

		public String findXmsxByAttrCount(Map<String, Object> params) {
			String sql = "SELECT count(*) " + createSql(params);
			return sql;
		}

		private String createSql(Map<String, Object> params) {

			String sql = " FROM ams_bus_xmsx where 1=1 ";
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if ("pageSize|pageIndex".indexOf(key) != -1)
					continue;
				if (UtilValidate.isNotEmpty(params.get(key))) {
					sql += " AND " + key + " = #{" + key + "}";
				}
			}
			return sql;
		}
	}

}