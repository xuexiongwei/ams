package com.xxw.springcloud.ams.mapper.common;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xxw.springcloud.ams.model.SysUser;

public interface UserManagerMapper {
	
	/**
	 * 新增用户信息
	 * @param user
	 */
	@Insert("INSERT INTO ams_sys_user(username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified) "
			+ "VALUES(#{userName},#{name},#{password},#{deptId},#{email},#{mobile},#{status}, #{sex}, #{birth},#{user_create},#{user_modified})")
	void insert(SysUser user);

	/**
	 * 更新用户信息
	 * @param user
	 */
	@Update("UPDATE ams_sys_user SET username=#{userName},name=#{name},password=#{password},deptId=#{deptId},email=#{email},"
			+ "mobile=#{mobile},status=#{status},sex=#{sex},birth=#{birth},user_modified=#{user_modified}, WHERE id =#{id}")
	void update(SysUser user);

	/**
	 * 删除用户信息
	 * @param id
	 */
	@Delete("DELETE FROM ams_sys_user WHERE id =#{id}")
	void delete(Long id);
	
	/**
	 * 根据姓名模糊匹配用户名查询用户信息，
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user where name like '%#{name}%'")
	@Results({
			@Result(property = "userCreate",  column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified")
	})
	SysUser selectUserByUsername(String name,int pageSize,int pageIndex);
	
	/**
	 * 根据用户名查询用户信息，
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user where username = #{username}  limit #{pageSize} offset (#{pageIndex}-1)*#{pageSize}")
	@Results({
			@Result(property = "userCreate",  column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified")
	})
	SysUser selectUserByUsernameAndPage(String username,int pageSize,int pageIndex);
	
	/**
	 * 查询用户信息，
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@Select("SELECT id,username,name,password,deptId,email,mobile,status,sex,birth,user_create,user_modified,gmt_create,gmt_modified"
			+ " FROM ams_sys_user limit #{pageSize} offset (#{pageIndex}-1)*#{pageSize}")
	@Results({
			@Result(property = "userCreate",  column = "user_create"),
			@Result(property = "userModified", column = "user_modified"),
			@Result(property = "gmtCreate", column = "gmt_create"),
			@Result(property = "gmtModified", column = "gmt_modified")
	})
	SysUser selectUserAndPage(int pageSize,int pageIndex);
}
