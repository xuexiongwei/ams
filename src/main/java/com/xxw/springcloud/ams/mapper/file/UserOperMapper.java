package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;

import com.xxw.springcloud.ams.model.UserOperation;
import com.xxw.springcloud.ams.util.UtilValidate;

public interface UserOperMapper {

	// 保存操作信息
	@Insert("insert into ams_bus_user_operation (userID, userName, operAction,operDesc, prjSN) values (#{userID}, #{userName}, #{operAction}, #{operDesc}, #{prjSN})")
	void saveUserOper(UserOperation uo);

	// 查询操作日志
	@SelectProvider(type = UserOperProvider.class, method = "findUserOperByAttr")
	public List<UserOperation> findUserOperByAttr(Map<String, Object> params);

	@SelectProvider(type = UserOperProvider.class, method = "findUserOperByAttrCount")
	public int findUserOperByAttrCount(Map<String, Object> params);

	class UserOperProvider {
		public String findUserOperByAttr(Map<String, Object> params) {
			String sql = "SELECT * " + createSql(params);
			sql += " limit #{pageSize} offset #{pageIndex}";
			return sql;
		}

		public String findUserOperByAttrCount(Map<String, Object> params) {
			String sql = "SELECT count(*) " + createSql(params);
			return sql;
		}

		private String createSql(Map<String, Object> params) {

			String sql = " FROM ams_bus_user_operation where 1=1 ";

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if ("pageSize|pageIndex".indexOf(key) != -1)
					continue;
				else if ("startDate".equals(key)) {// 查询指定时间区间
					sql += " and gmt_create >=#{startDate}";
				} else if ("endDate".equals(key)) {// 查询指定时间区间
					sql += " and gmt_create <=#{endDate}";
				} else if (UtilValidate.isNotEmpty(params.get(key))) {
					sql += " AND " + key + " = #{" + key + "}";
				}
			}
			return sql;
		}
	}

}