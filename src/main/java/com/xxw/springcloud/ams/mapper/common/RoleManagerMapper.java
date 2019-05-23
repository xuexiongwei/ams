package com.xxw.springcloud.ams.mapper.common;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.SysRole;
import com.xxw.springcloud.ams.model.SysRoleMenu;

public interface RoleManagerMapper {

	/**
	 * 新增角色信息
	 * 
	 * @param sysRole
	 */
	@Insert("INSERT INTO ams_sys_role(roleName,roleSign,remark,user_create,user_modified) "
			+ "VALUE (#{roleName},#{roleSign},#{remark},#{userCreate},#{userModified})")
	void insert(SysRole sysRole);

	/**
	 * 更新角色信息
	 * 
	 * @param user
	 */
	@Update("UPDATE ams_sys_role SET roleName=#{roleName},roleSign=#{roleSign},"
			+ "remark=#{remark},user_modified=#{userModified} WHERE id =#{id}")
	void update(SysRole sysRole);

	
	@Delete("DELETE FROM ams_sys_role_menu WHERE role_id =#{roleId}")
	void deleteRoleMenu(@Param("roleId")Long roleId);
	
	/**
	 * 删除角色信息
	 * 
	 * @param id
	 */
	@Delete("DELETE FROM ams_sys_role WHERE id =#{id}")
	void delete(Long id);

	/**
	 * 获取角色统计数
	 * 
	 * @param id
	 */
	@Select("SELECT COUNT(*) FROM ams_sys_role")
	int selectCount();
	
	/**
	 * 根据用户名查询用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,roleName,roleSign,remark,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_role where roleName like '%${roleName}%' limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysRole> selectRoleByRoleName(@Param("roleName") String roleName,@Param("pageSize") int pageSize,
			@Param("pageIndex") int pageIndex);
	
	@Select("SELECT COUNT(*)"
			+ " FROM ams_sys_role where roleName like '%${roleName}%' ")
	int selectCountRoleByRoleName(@Param("roleName") String roleName);

	/**
	 * 分页查询角色信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,roleName,roleSign,remark,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_role limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysRole> selectRoleAndPage(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);
	
	@Insert({"<script>",
		"INSERT INTO ams_sys_role_menu(role_id,menu_id,user_create,user_modified) ",
		"VALUES ",
		"<foreach collection='userrole' item='item' index='index' separator=','>",
		"(#{item.roleId},#{item.menuId},#{item.userCreate},#{item.userModified})",
		"</foreach>",
		"</script>"})
	void insertRoleAddMenu(@Param("userrole")List<SysRoleMenu> userrole);
	
}
