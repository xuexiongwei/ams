package com.xxw.springcloud.ams.mapper.file;

import org.apache.ibatis.annotations.Insert;

import com.xxw.springcloud.ams.model.UserOperation;

public interface UserOperMapper {

	// 保存操作信息
	@Insert("insert into ams_bus_user_operation (userID, userName, operAction, prjSN) values (#{userID}, #{userName}, #{operAction}, #{prjSN})")
	void saveUserOper(UserOperation uo);

}