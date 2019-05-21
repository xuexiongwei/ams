package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.util.UtilValidate;

public interface XmmxMapper {

	// 保存项目明细
	@Insert("insert into ams_bus_xmmx (prjSN, serialNumber, serialFunct, aboveGroundArea, underGroundArea, blendArea, aboveGroundLen, prjClasfiCode) "
			+ "values (#{prjSN}, #{serialNumber}, #{serialFunct}, #{aboveGroundArea}, #{underGroundArea}, #{blendArea}, #{aboveGroundLen}, #{prjClasfiCode})")
	void saveXmmx(Xmmx mx);

	// 保存项目明细
	@Insert("insert into ams_bus_xmmx (prjSN, serialNumber, serialFunct, aboveGroundArea, underGroundArea, blendArea, aboveGroundLen, prjClasfiCode) "
			+ "values (#{prjSN}, #{serialNumber}, #{serialFunct}, #{aboveGroundArea}, #{underGroundArea}, #{blendArea}, #{aboveGroundLen}, #{prjClasfiCode})")
	void saveXmmx2(Map<String, Object> params);

	// 更新项目明细
	@Update("update ams_bus_xmmx set serialFunct=#{serialFunct}, aboveGroundArea=#{aboveGroundArea}, underGroundArea=#{underGroundArea}, blendArea=#{blendArea}, "
			+ "aboveGroundLen=#{aboveGroundLen}, prjClasfiCode=#{prjClasfiCode} where id=#{id}")
	void updateXmmx(Map<String, Object> params);

	// 删除项目明细
	@Delete("delete from ams_bus_xmmx where id=#{id}")
	void delXmmx(Map<String, Object> params);

	// 查询属性信息
	@SelectProvider(type = XmmxProvider.class, method = "findXmmxByAttr")
	public List<Xmmx> findXmmxByAttr(Map<String, Object> params);

	@SelectProvider(type = XmmxProvider.class, method = "findXmmxByAttrCount")
	public int findXmmxByAttrCount(Map<String, Object> params);

	class XmmxProvider {
		public String findXmmxByAttr(Map<String, Object> params) {
			String sql = "SELECT * " + createSql(params);
			sql += " limit #{pageSize} offset #{pageIndex}";
			return sql;
		}

		public String findXmmxByAttrCount(Map<String, Object> params) {
			String sql = "SELECT count(*) " + createSql(params);
			return sql;
		}

		private String createSql(Map<String, Object> params) {

			String sql = " FROM ams_bus_xmmx where 1=1 ";
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