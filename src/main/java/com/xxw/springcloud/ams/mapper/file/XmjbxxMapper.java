package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.util.UtilValidate;

public interface XmjbxxMapper {

	// 保存项目基本信息
	@Insert("insert into ams_bus_xmjbxx (prjSN, prjUnit, prjAdr, prjName, prjType, contacts, contactInf, prjTemSN, specialNotifi,"
			+ " noticeTime, effectiveTime, remark) values"
			+ "(#{prjSN}, #{prjUnit}, #{prjAdr}, #{prjName}, #{prjType}, #{contacts}, #{contactInf}, #{prjTemSN}, #{specialNotifi},"
			+ " #{noticeTime}, #{effectiveTime}, #{remark})")
	void saveXmjbxx(Xmjbxx jbxx);

	// 保存项目基本信息
	@Insert("insert into ams_bus_xmjbxx (prjSN, prjUnit, prjAdr, prjName, prjType, contacts, contactInf, prjTemSN, specialNotifi,"
			+ " noticeTime, effectiveTime, remark) values"
			+ "(#{prjSN}, #{prjUnit}, #{prjAdr}, #{prjName}, #{prjType}, #{contacts}, #{contactInf}, #{prjTemSN}, #{specialNotifi},"
			+ " #{noticeTime}, #{effectiveTime}, #{remark})")
	void saveXmjbxx2(Map<String, Object> params);

	// 查询项目基本信息
	@Select("select * from ams_bus_xmjbxx where prjSN=#{prjSN}")
	Xmjbxx queryXmjbxxByPrjSN(String prjSN);

	@Select("select * from ams_bus_xmjbxx where id=#{id}")
	Xmjbxx queryXmjbxxByID(Map<String, Object> params);

	// 删除项目基本信息
	@Delete("delete from ams_bus_xmjbxx where prjSN=#{prjSN}")
	void delXmjbxxByPrjSN(Map<String, Object> params);

	@Select("update ams_bus_xmjbxx set prjUnit=#{prjUnit},prjAdr=#{prjAdr}, prjName=#{prjName}, prjType=#{prjType}, contacts=#{contacts},"
			+ "contactInf=#{contactInf}, prjTemSN=#{prjTemSN}, specialNotifi=#{specialNotifi},noticeTime=#{noticeTime},"
			+ "effectiveTime=#{effectiveTime}, remark=#{remark} where prjSN=#{prjSN}")
	void updateXmjbxx(Xmjbxx jbxx);

	@Select("update ams_bus_xmjbxx set prjUnit=#{prjUnit},prjAdr=#{prjAdr}, prjName=#{prjName}, prjType=#{prjType}, contacts=#{contacts},"
			+ "contactInf=#{contactInf}, prjTemSN=#{prjTemSN}, specialNotifi=#{specialNotifi},noticeTime=#{noticeTime},"
			+ "effectiveTime=#{effectiveTime}, remark=#{remark} where prjSN=#{prjSN}")
	void updateXmjbxx2(Map<String, Object> params);

	// 查询项目基本信息
	@SelectProvider(type = XmjbxxProvider.class, method = "findXmjbxxByAttr")
	public List<Xmjbxx> findXmjbxxByAttr(Map<String, Object> params);

	@SelectProvider(type = XmjbxxProvider.class, method = "findXmjbxxByAttrCount")
	public int findXmjbxxByAttrCount(Map<String, Object> params);

	class XmjbxxProvider {
		public String findXmjbxxByAttr(Map<String, Object> params) {
			String sql = "SELECT * " + createSql(params);
			sql += " limit #{pageSize} offset #{pageIndex}";
			return sql;
		}

		public String findXmjbxxByAttrCount(Map<String, Object> params) {
			String sql = "SELECT count(*) " + createSql(params);
			return sql;
		}

		private String createSql(Map<String, Object> params) {

			String sql = " FROM ams_bus_xmjbxx where 1=1 ";

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if ("pageSize|pageIndex".indexOf(key) != -1)
					continue;
				if (UtilValidate.isNotEmpty(params.get(key))) {
					if ("prjSN|prjUnit|prjAdr|specialNotifi|noticeTime|remark|".indexOf(key + "|") != -1) {// 支持模糊查询字段
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