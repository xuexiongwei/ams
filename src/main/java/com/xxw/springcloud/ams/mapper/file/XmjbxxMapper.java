package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.xxw.springcloud.ams.model.Bb001;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.util.UtilValidate;

public interface XmjbxxMapper {

	// 保存项目基本信息
	@Insert("insert into ams_bus_xmjbxx (prjSN, prjUnit, prjAdr, prjName, prjType, contacts, contactInf, prjTemSN, specialNotifi,"
			+ " noticeTime, effectiveTime, remark,delaySN, delayCountDay,correctionSN, correctionDate,prjSNType,prjYear) values"
			+ "(#{prjSN}, #{prjUnit}, #{prjAdr}, #{prjName}, #{prjType}, #{contacts}, #{contactInf}, #{prjTemSN}, #{specialNotifi},"
			+ " #{noticeTime}, #{effectiveTime}, #{remark},#{delaySN}, #{delayCountDay}, #{correctionSN},  #{correctionDate}, #{prjSNType},#{prjYear})")
	void saveXmjbxx(Xmjbxx jbxx);

	// 保存项目基本信息
	@Insert("insert into ams_bus_xmjbxx (prjSN, prjUnit, prjAdr, prjName, prjType, contacts, contactInf, prjTemSN, specialNotifi,"
			+ " noticeTime, effectiveTime, remark,delaySN, delayCountDay,correctionSN, correctionDate,prjSNType,prjYear) values"
			+ "(#{prjSN}, #{prjUnit}, #{prjAdr}, #{prjName}, #{prjType}, #{contacts}, #{contactInf}, #{prjTemSN}, #{specialNotifi},"
			+ " #{noticeTime}, #{effectiveTime}, #{remark},#{delaySN}, #{delayCountDay}, #{correctionSN},  #{correctionDate}, #{prjSNType},#{prjYear})")
	void saveXmjbxx2(Map<String, Object> params);

	// 查询项目基本信息
	@Select("select * from ams_bus_xmjbxx where prjSN=#{prjSN}")
	Xmjbxx queryXmjbxxByPrjSN(String prjSN);

	@Select("select * from ams_bus_xmjbxx where id=#{id}")
	Xmjbxx queryXmjbxxByID(Map<String, Object> params);

	// 更新项目状态
	@Select("update ams_bus_xmjbxx set prjStatus=#{prjStatus} where prjSN=#{prjSN}")
	void updatePrjStatusByPrjSN(Map<String, Object> params);

	// 删除项目基本信息
	@Delete("delete from ams_bus_xmjbxx where prjSN=#{prjSN}")
	void delXmjbxxByPrjSN(Map<String, Object> params);

	@Select("update ams_bus_xmjbxx set prjUnit=#{prjUnit},prjAdr=#{prjAdr}, prjName=#{prjName}, prjType=#{prjType}, contacts=#{contacts},"
			+ "contactInf=#{contactInf}, prjTemSN=#{prjTemSN}, specialNotifi=#{specialNotifi},noticeTime=#{noticeTime},"
			+ "effectiveTime=#{effectiveTime}, remark=#{remark},prjSNType=#{prjSNType},correctionSN=#{correctionSN},correctionDate=#{correctionDate},"
			+ "delaySN=#{delaySN}, delayCountDay=#{delayCountDay} where prjSN=#{prjSN}")
	void updateXmjbxx(Xmjbxx jbxx);

	@Select("update ams_bus_xmjbxx set prjUnit=#{prjUnit},prjAdr=#{prjAdr}, prjName=#{prjName}, prjType=#{prjType}, contacts=#{contacts},"
			+ "contactInf=#{contactInf}, prjTemSN=#{prjTemSN}, specialNotifi=#{specialNotifi},noticeTime=#{noticeTime},"
			+ "effectiveTime=#{effectiveTime}, remark=#{remark},prjSNType=#{prjSNType},correctionSN=#{correctionSN},  correctionDate=#{correctionDate},"
			+ "delaySN=#{delaySN}, delayCountDay=#{delayCountDay} where prjSN=#{prjSN}")
	void updateXmjbxx2(Map<String, Object> params);

	// 查询项目基本信息
	@SelectProvider(type = XmjbxxProvider.class, method = "findXmjbxxByAttr")
	public List<Xmjbxx> findXmjbxxByAttr(Map<String, Object> params);

	@SelectProvider(type = XmjbxxProvider.class, method = "findXmjbxxByAttrCount")
	public int findXmjbxxByAttrCount(Map<String, Object> params);

	// 视图表一
	@SelectProvider(type = XmjbxxProvider.class, method = "findbb001")
	public List<Bb001> findbb001(Map<String, Object> params);

	@SelectProvider(type = XmjbxxProvider.class, method = "findbb001Detail")
	public List<Bb001> findbb001Detail(Map<String, Object> params);

	@SelectProvider(type = XmjbxxProvider.class, method = "findbb001Count")
	public int findbb001Count(Map<String, Object> params);

	class XmjbxxProvider {
		public String findXmjbxxByAttr(Map<String, Object> params) {
			String sql = "SELECT * " + createSql(params);
			if (UtilValidate.isNotEmpty(params.get("pageSize")) && UtilValidate.isNotEmpty(params.get("pageIndex"))) {
				sql += " limit #{pageSize} offset #{pageIndex}";
			}
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

		// 用于统计表001
		public String findbb001(Map<String, Object> params) {
			String sql = createSqlbb001(params, false);
			if (UtilValidate.isNotEmpty(params.get("pageSize")) && UtilValidate.isNotEmpty(params.get("pageIndex"))) {
				sql += " limit #{pageSize} offset #{pageIndex}";
			}
			return sql;
		}

		public String findbb001Detail(Map<String, Object> params) {

			String sql = " from bb001v where 1=1";
			String kes = " ";

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if ("pageSize|pageIndex".indexOf(key) != -1)
					continue;
				kes += key + ",";
				sql += " AND " + key + " = #{" + key + "}";
			}
			kes = kes.substring(0, kes.length() - 1);
			sql = "select distinct sxid,mxid," + kes + sql;
			return sql;
		}

		public String findbb001Count(Map<String, Object> params) {
			String sql = createSqlbb001(params, true);
			return sql;
		}

		private String createSqlbb001(Map<String, Object> params, boolean isCount) {

			String sql = " from bb001v where 1=1";
			String kes = " distinct ";

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if ("pageSize|pageIndex".indexOf(key) != -1)
					continue;
				kes += key + ",";

				// 处理复选框值
				String vals = entry.getValue() + "";
				// 处理字符末尾,号
				if (vals.endsWith(","))
					vals = vals.substring(0, vals.length() - 1);
				if (vals.endsWith("]"))
					vals = vals.substring(0, vals.length() - 1);
				if (vals.startsWith("["))
					vals = vals.substring(1, vals.length());

				vals = vals.replace("\"", "'");
				// 更改参数
				params.put(key, vals);

				sql += " AND " + key + " in (" + params.get(key) + ")";

			}

			kes = kes.substring(0, kes.length() - 1);

			if (isCount) {
				sql = "select count(" + kes + ")" + sql;
			} else {
				sql = "select " + kes + sql;
			}
			return sql;
		}
	}
}