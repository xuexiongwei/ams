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
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.model.SysUserRole;

public interface UserManagerMapper {

	/**
	 * 新增用户信息
	 * 
	 * @param user
	 */
	@Insert("INSERT INTO ams_sys_user(username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified) "
			+ "VALUE (#{userName},#{name},#{password},#{deptId},#{email},#{mobile},#{status}, #{sex}, #{birth},#{userCreate},#{userModified})")
	void insert(SysUser user);

	@Insert({ "<script>", "INSERT INTO ams_sys_user_role(userId,roleId,user_create,user_modified) ", "VALUES ",
			"<foreach collection='userrole' item='item' index='index' separator=','>",
			"(#{item.userId},#{item.roleId},#{item.userCreate},#{item.userModified})", "</foreach>", "</script>" })
	void insertUserAddRole(@Param("userrole") List<SysUserRole> userrole);

	@Delete("DELETE FROM ams_sys_user_role WHERE userId =#{userId}")
	void deleteUserRole(@Param("userId") Long userId);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 */
	@Update("UPDATE ams_sys_user SET username=#{userName},name=#{name},password=#{password},deptId=#{deptId},email=#{email},"
			+ "mobile=#{mobile},status=#{status},sex=#{sex},birth=#{birth},user_modified=#{userModified} WHERE id =#{id}")
	void update(SysUser user);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 */
	@Update("UPDATE ams_sys_user SET password=#{password}" + " WHERE id =#{id}")
	void updatePassword(SysUser user);

	/**
	 * 删除用户信息
	 * 
	 * @param id
	 */
	@Delete("DELETE FROM ams_sys_user WHERE id =#{id}")
	void delete(Long id);

	/**
	 * 用户总记录数
	 * 
	 * @param id
	 */
	@Select("SELECT COUNT(*) FROM ams_sys_user")
	int selectCount();

	/**
	 * 根据姓名模糊匹配用户名查询用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user where name like '%${name}%' limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysUser> selectUserByname(@Param("name") String name, @Param("pageSize") int pageSize,
			@Param("pageIndex") int pageIndex);

	/**
	 * 根据用户名查询用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user where username = #{username}  limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysUser> selectUserByUsernameAndPage(@Param("username") String username, @Param("pageSize") int pageSize,
			@Param("pageIndex") int pageIndex);

	/**
	 * 根据用户名查询用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user where username = #{username} and password = #{password}  limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysUser> selectUserByUsernamePasswordAndPage(@Param("username") String username,
			@Param("password") String password, @Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);

	/**
	 * 根据用户ID查询用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT * FROM ams_sys_user where id = #{id}")
	SysUser selectUserByUserID(String id);

	/**
	 * 根据姓名统计用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT count(*)" + " FROM ams_sys_user where name like '%${name}%'")
	int selectCountUserByNameAndPage(@Param("name") String name);

	/**
	 * 查询用户信息，
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysUser> selectUserAndPage(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);

	@Select("SELECT role.id,role.roleName,role.roleSign,role.remark,role.user_create,role.user_modified,role.gmt_create,"
			+ "role.gmt_modified from ams_sys_role as role LEFT JOIN ams_sys_user_role as userrole on (role.id=userrole.roleId) "
			+ "where userrole.userId=#{userId} limit #{pageSize} offset #{pageIndex}")
	@Results({ @Result(property = "userCreate", column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified") })
	List<SysRole> selectRoleByUserId(@Param("userId") Long userId, @Param("pageSize") int pageSize,
			@Param("pageIndex") int pageIndex);
}
