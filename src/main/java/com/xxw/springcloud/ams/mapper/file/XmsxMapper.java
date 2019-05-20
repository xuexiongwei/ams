package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;

import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.UtilValidate;

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

	// 保存项目属性
	@Insert("insert into ams_bus_xmsx (prjSN, serialNumber, prjNature, prjAttr, strucType, peacetimeUses, aboveGroundLev, "
			+ "underGroundLev, aboveGroundHet, underGroundHet, buildings, housingStockNum, checkDocSN, checkDocDate, checkSN, "
			+ "checkDate, delaySN, delayCountDay, cancelSN, cancelDate, correctionSN, correctionDate, imgJudgeRes, exproprInfo, "
			+ "remark) "
			+ "values (#{prjSN}, #{serialNumber}, #{prjNature}, #{prjAttr}, #{strucType}, #{peacetimeUses}, #{aboveGroundLev}, "
			+ "#{underGroundLev}, #{aboveGroundHet}, #{underGroundHet}, #{buildings}, #{housingStockNum}, #{checkDocSN}, #{checkDocDate}, #{checkSN},"
			+ "#{checkDate}, #{delaySN}, #{delayCountDay}, #{cancelSN}, #{cancelDate}, #{correctionSN},  #{correctionDate}, #{imgJudgeRes}, #{exproprInfo},"
			+ "#{remark})")
	void saveXmsx2(Map<String, Object> params);

	// 查询属性信息
	@SelectProvider(type = XmsxProvider.class, method = "findXmsxByAttr")
	public List<Xmsx> findXmsxByAttr(Map<String, Object> params);

	@SelectProvider(type = XmsxProvider.class, method = "findXmsxByAttrCount")
	public int findXmsxByAttrCount(Map<String, Object> params);

	class XmsxProvider {
		public String findXmsxByAttr(Map<String, Object> params) {
			String sql = "SELECT *  " + createSql(params);
			sql += " limit #{pageSize} offset #{pageIndex}";
			return sql;
		}

		public String findXmsxByAttrCount(Map<String, Object> params) {
			String sql = "SELECT count(*) " + createSql(params);
			return sql;
		}

		private String createSql(Map<String, Object> params) {

			String sql = " FROM ams_bus_xmsx where 1=1 ";
			if (UtilValidate.isNotEmpty(params.get("id"))) {
				sql += " AND id = #{id}";
			}
			if (UtilValidate.isNotEmpty(params.get("prjSN"))) {
				sql += " AND prjSN = #{prjSN}";
			}

			return sql;
		}
	}

}