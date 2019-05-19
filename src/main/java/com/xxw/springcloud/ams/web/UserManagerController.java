package com.xxw.springcloud.ams.web;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xxw.springcloud.ams.mapper.common.UserManagerMapper;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.model.SysUserRole;
import com.xxw.springcloud.ams.util.ServiceUtil;


@RestController
public class UserManagerController {

	public static Logger logger = LoggerFactory.getLogger(UserManagerController.class);

	@Autowired
	private UserManagerMapper userManagerMapper;
	
	/**
	 * 分页获取用户信息
	 * @return
	 */
	@RequestMapping(value="/getUserAndPage",method = RequestMethod.POST)
	public String getUsersAndPage(@RequestBody String inputjson) {
		List<SysUser> users = null;
		int totalSize = 0;
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			int pageSize = 10;
			int pageIndex = 0;
			if(header!=null) {
				pageSize = header.getReqpageSize();
				pageIndex = header.getReqpageIndex();
			}else {
				header = new Header();
			}
			//计算起止记录数
			pageIndex = (pageIndex-1)*pageSize;
			users = userManagerMapper.selectUserAndPage(pageSize, pageIndex);
			totalSize = userManagerMapper.selectCount();
			header.setRspPageCount(totalSize);
			logger.info("用户查询成功");
			return ServiceUtil.returnSuccess(users, "userList", header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00001", "用户查询异常");
		}
	}
	
	@RequestMapping(value = "/getUserByUsername", method = RequestMethod.POST)
	public String getUserByUsername(@RequestBody String inputjson) {
		
		SysUser user = null;
		List<SysUser> users = null;
		try {
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			users = userManagerMapper.selectUserByUsernameAndPage(sysUser.getUserName(), 10,0);
			if (null != users) {
				user = users.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00002", "用户查询异常");
		}
		logger.info("用户查询成功");
		return ServiceUtil.returnSuccess(user, null);
	}
	
	@RequestMapping(value="/addUser",method = RequestMethod.POST)
	public String addUsers(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			if(sysUser.getUserName() == null) {
				return ServiceUtil.returnError("00003", "用户名不能为空，此信息用于登录");
			}
			sysUser.setPassword(new String(Base64.getEncoder().encode("123456".getBytes())));
			sysUser.setUserCreate(header.getReqUserId());
			sysUser.setUserModified(header.getReqUserId());
			userManagerMapper.insert(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00004", "用户创建异常");
		}
		logger.info("用户添加成功");
		return ServiceUtil.returnSuccess("用户添加成功");
	}
	
	@RequestMapping(value="/updateUser",method = RequestMethod.POST)
	public String updateUsers(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			if(sysUser.getUserName() == null) {
				return ServiceUtil.returnError("00005", "用户名不能为空，此信息用于登录");
			}
			sysUser.setUserModified(header.getReqUserId());
			userManagerMapper.update(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00006", "用户修改异常");
		}
		logger.info("用户修改成功");
		return ServiceUtil.returnSuccess("用户修改成功");
	}
	@RequestMapping(value="/updateUserPassword",method = RequestMethod.POST)
	public String updateUsersPassword(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			if(sysUser.getUserName() == null) {
				return ServiceUtil.returnError("00005", "用户名不能为空，此信息用于登录");
			}
			if(sysUser.getPassword() == null) {
				return ServiceUtil.returnError("00007", "用户密码不能为空");
			}
			sysUser.setPassword(new String(Base64.getEncoder().encode(sysUser.getPassword().getBytes())));
			sysUser.setUserModified(header.getReqUserId());
			userManagerMapper.updatePassword(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00008", "用户修改密码异常");
		}
		logger.info("用户修改密码成功");
		return ServiceUtil.returnSuccess("用户修改密码成功");
	}
	
	@RequestMapping(value="/deleteUser",method = RequestMethod.POST)
	public String deleteUsers(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			if(sysUser.getId() == null) {
				return ServiceUtil.returnError("00005", "用户ID不能为空");
			}
			userManagerMapper.delete(sysUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00009", "用户修改异常");
		}
		logger.info("用户删除成功");
		return ServiceUtil.returnSuccess("用户删除成功");
	}
	
	@RequestMapping(value="/addUserAddRole",method = RequestMethod.POST)
	public String addUserAddRole(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			Map<String,Object> bodyMap = ServiceUtil.jsonStringToMap(bodyStr);
			JSONArray jsonArray = (JSONArray) bodyMap.get("userRoleList");
			List<SysUserRole> sysUserRole = JSONObject.parseArray(jsonArray.toJSONString(),SysUserRole.class);
			
			if(sysUserRole == null || sysUserRole.size() == 0) {
				return ServiceUtil.returnError("00010", "数据为空，未分配角色");
			}
			List<SysUserRole> sysUserRoleList = new ArrayList<SysUserRole>();
			Long userId = 0l;
			for(SysUserRole sur:sysUserRole) {
				sur.setUserCreate(header.getReqUserId());
				sur.setUserModified(header.getReqUserId());
				if(sur.getUserId() == null || sur.getRoleId() == null) {
					return ServiceUtil.returnError("00013", "用户ID或者角色ID为空，分配失败");
				}
				userId = sur.getUserId();
				sysUserRoleList.add(sur);
			}
			//先清空，再增加
			userManagerMapper.deleteUserRole(userId);
			userManagerMapper.insertUserAddRole(sysUserRoleList);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00011", "用户角色分配异常");
		}
		logger.info("用户角色分配成功");
		return ServiceUtil.returnSuccess("用户角色分配成功");
	}
	
	@RequestMapping(value = "/getUserByname", method = RequestMethod.POST)
	public String getUserByName(@RequestBody String inputjson) {
		List<SysUser> users = null;
		int totalSize = 0;
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			int pageSize = 10;
			int pageIndex = 0;
			if(header!=null) {
				pageSize = header.getReqpageSize();
				pageIndex = header.getReqpageIndex();
			}else {
				header = new Header();
			}
			//计算起止记录数
			pageIndex = (pageIndex-1)*pageSize;
			users = userManagerMapper.selectUserByUsername(sysUser.getName(), pageSize,pageIndex);
			totalSize = userManagerMapper.selectCountUserByNameAndPage(sysUser.getName());
			header.setRspPageCount(totalSize);
			logger.info("用户查询成功");
			return ServiceUtil.returnSuccess(users, "userList", header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("00012", "用户查询异常");
		}
	}
    
}