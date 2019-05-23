package com.xxw.springcloud.ams.mapper.common;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.SysMenu;
import com.xxw.springcloud.ams.model.SysRoleMenu;
import com.xxw.springcloud.ams.model.SysUser;

public interface MenuManagerMapper {

	/**
	 * 新增用户信息
	 * 
	 * @param user
	 */
	@Insert("INSERT INTO ams_sys_menu(parent_id,name,url,order_num,user_create,user_modified) "
			+ "VALUE (#{parentId},#{name},#{url},#{orderNum},#{userCreate},#{userModified})")
	void insert(SysMenu menu);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 */
	@Update("UPDATE ams_sys_menu SET parent_id=#{parentId},name=#{name},url=#{url},order_num=#{orderNum}"
			+ ",user_modified=#{userModified} WHERE id =#{id}")
	void update(SysMenu menu);

	/**
	 * 删除用户信息
	 * 
	 * @param id
	 */
	@Delete("DELETE FROM ams_sys_menu WHERE id =#{id}")
	void delete(Long id);

	/**
	 * 用户总记录数
	 * 
	 * @param id
	 */
	@Select("SELECT COUNT(*) FROM ams_sys_menu")
	int selectCount();

	/**
	 * 菜单信息
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,parent_id,name,url,order_num,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_menu limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysMenu> selectMenuAndPage(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);

	/**
	 * 通过角色ID获取菜单ID信息
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,role_id,menu_id,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_role_menu where role_id = #{roleId} limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysRoleMenu> selectMenuIdsByRoleId(@Param("roleId") Long roleId,@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);

	@Select({"<script>",
		"SELECT menu.id,menu.parent_id,menu.name,menu.url,menu.order_num,menu.user_create,menu.user_modified,menu.gmt_create,menu.gmt_modified from ams_sys_menu menu left ",
		"join ams_sys_role_menu rolemenu on(menu.id=rolemenu.menu_id) where rolemenu.role_id in",
		"<foreach collection='roleIdList' item='item' open='(' separator=',' close=')'>",
			"#{item}",
		"</foreach>",
		" limit #{pageSize} offset #{pageIndex}",
		"</script>"})
	List<SysMenu> selectMenuIdsByRoleIds(@Param("roleIdList") List<Long> roleIdList,@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);
}
