package com.xxw.springcloud.ams.mapper.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.xxw.springcloud.ams.mapper.common.UserManagerMapper;

public interface SuperMapper extends FileMapper, DxfMapper, DicMapper, XmjbxxMapper, XmmxMapper, XmsxMapper,
		UserOperMapper, UserManagerMapper {

	/**
	 * 查询某表的某个字段模糊查询
	 * 
	 * @param params
	 * @return
	 */
	@SelectProvider(type = Provider.class, method = "queryJbxxLike")
	public List<String> queryJbxxLike(Map<String, Object> params);

	class Provider {

		// 查询指定字段的模糊匹配
		public String queryJbxxLike(Map<String, Object> params) {
			String sql = "select distinct " + params.get("key") + " from ams_bus_" + params.get("tab") + " where "
					+ params.get("key") + " like '%" + params.get("val") + "%'";
			return sql;
		}

	}

}