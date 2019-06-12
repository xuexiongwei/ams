package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.util.UtilValidate;

public interface XmmxMapper {

	// 保存项目明细
	@Insert("insert into ams_bus_xmmx (prjSN, serialNumber, serialFunct, aboveGroundArea, underGroundArea, blendArea, aboveGroundLen, "
			+ "prjClasfiCode,prjClasfiName1,prjClasfiName2,prjClasfiName3,prjClasfiName4,prjClasfiName5) "
			+ "values (#{prjSN}, #{serialNumber}, #{serialFunct}, #{aboveGroundArea}, #{underGroundArea}, #{blendArea}, #{aboveGroundLen}, #{prjClasfiCode},"
			+ "#{prjClasfiName1},#{prjClasfiName2},#{prjClasfiName3},#{prjClasfiName4},#{prjClasfiName5})")
	void saveXmmx(Map<String, Object> params);

	// 更新项目明细
	@Update("update ams_bus_xmmx set serialFunct=#{serialFunct}, aboveGroundArea=#{aboveGroundArea}, underGroundArea=#{underGroundArea}, blendArea=#{blendArea}, "
			+ "aboveGroundLen=#{aboveGroundLen}, prjClasfiCode=#{prjClasfiCode} where id=#{id}")
	void updateXmmx(Map<String, Object> params);

	// 删除项目明细
	@Delete("delete from ams_bus_xmmx where id=#{id}")
	void delXmmx(Map<String, Object> params);

	@Delete("delete from ams_bus_xmmx where prjSN=#{prjSN}")
	void delXmmxByPrjSN(String prjSN);

	@Select("select * from ams_bus_xmmx where id=#{id}")
	Xmmx queryXmmxByID(Long id);

	// 去重查询项目明细，用于分类
	@Select("select distinct serialNumber,prjClasfiName1,prjClasfiName2 from ams_bus_xmmx where prjSN=#{prjSN}")
	List<Xmmx> queryDistinctXmmxByPrjSN(String prjSN);
	// 去重查询项目明细，用于分类
	@Select("select distinct serialNumber,prjClasfiName1,prjClasfiName2 from ams_bus_xmmx where prjSN=#{prjSN} and serialNumber=#{serialNumber}")
	List<Xmmx> queryDistinctXmmxByPrjSNAndSN(Map<String,Object> params);

	// 查询属性信息
	@SelectProvider(type = XmmxProvider.class, method = "findXmmxByAttr")
	public List<Xmmx> findXmmxByAttr(Map<String, Object> params);

	@SelectProvider(type = XmmxProvider.class, method = "findXmmxByAttrCount")
	public int findXmmxByAttrCount(Map<String, Object> params);

	class XmmxProvider {
		public String findXmmxByAttr(Map<String, Object> params) {
			String sql = "SELECT * " + createSql(params);

			if (UtilValidate.isNotEmpty(params.get("pageSize")) && UtilValidate.isNotEmpty(params.get("pageIndex"))) {
				sql += " limit #{pageSize} offset #{pageIndex}";
			}
			return sql;
		}

		public String findXmmxByAttrCount(Map<String, Object> params) {
			String sql = "SELECT count(*) " + createSql(params);
			return sql;
		}

		private String createSql(Map<String, Object> params) {

			String sql = " FROM ams_bus_xmmx where 1=1 ";
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if ("pageSize|pageIndex".indexOf(key) != -1)
					continue;
				if (UtilValidate.isNotEmpty(params.get(key))) {
					if ("prjSN|".indexOf(key + "|") != -1) {// 支持模糊查询字段
						sql += " AND " + key + " like 	\"%\"#{" + key + "}\"%\"";
					} else {
						sql += " AND " + key + " = #{" + key + "}";
					}
				}
			}

			return sql;
		}
	}

}