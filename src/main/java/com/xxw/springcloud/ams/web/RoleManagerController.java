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
import com.xxw.springcloud.ams.mapper.common.RoleManagerMapper;
import com.xxw.springcloud.ams.mapper.common.UserManagerMapper;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.SysRole;
import com.xxw.springcloud.ams.model.SysRoleMenu;
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.model.SysUserRole;
import com.xxw.springcloud.ams.util.ServiceUtil;


@RestController
public class RoleManagerController {

	public static Logger logger = LoggerFactory.getLogger(RoleManagerController.class);

	@Autowired
	private RoleManagerMapper roleManagerMapper;
	
	/**
	 * 分页获取角色信息
	 * @return
	 */
	@RequestMapping(value="/getRoleAndPage",method = RequestMethod.POST)
	public String getRoleAndPage(@RequestBody String inputjson) {
		List<SysRole> roles = null;
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
			roles = roleManagerMapper.selectRoleAndPage(pageSize, pageIndex);
			totalSize = roleManagerMapper.selectCount();
			header.setRspPageCount(totalSize);
			logger.info("角色查询成功");
			return ServiceUtil.returnSuccess(roles, "roleList", header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("10001", "角色查询异常");
		}
	}
	
	@RequestMapping(value = "/getRoleByRolename", method = RequestMethod.POST)
	public String getRoleByRolename(@RequestBody String inputjson) {
		List<SysRole> roles = null;
		int totalSize = 0;
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			SysRole sysRole = JSONObject.parseObject(bodyStr, new TypeReference<SysRole>() {
			});
			int pageSize = 10;
			int pageIndex = 0;
			if(header!=null) {
				pageSize = header.getReqpageSize();
				pageIndex = header.getReqpageIndex();
			}else {
				header = new Header();
			}
			roles = roleManagerMapper.selectRoleByRoleName(sysRole.getRoleName(),pageSize,pageIndex);
			totalSize = roleManagerMapper.selectCountRoleByRoleName(sysRole.getRoleName());
			header.setRspPageCount(totalSize);
			logger.info("角色查询成功");
			return ServiceUtil.returnSuccess(roles, "roleList",header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("10002", "角色查询异常");
		}
	}
	
	
	@RequestMapping(value="/addRole",method = RequestMethod.POST)
	public String addRole(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysRole sysRole = JSONObject.parseObject(bodyStr, new TypeReference<SysRole>() {
			});
			if(sysRole.getRoleName() == null) {
				return ServiceUtil.returnError("10003", "角色名不能为空");
			}
			sysRole.setUserCreate(header.getReqUserId());
			sysRole.setUserModified(header.getReqUserId());
			roleManagerMapper.insert(sysRole);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("10004", "角色创建异常");
		}
		logger.info("角色添加成功");
		return ServiceUtil.returnSuccess("角色添加成功");
	}
	
	@RequestMapping(value="/updateRole",method = RequestMethod.POST)
	public String updateRole(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysRole sysRole = JSONObject.parseObject(bodyStr, new TypeReference<SysRole>() {
			});
			if(sysRole.getRoleName() == null) {
				return ServiceUtil.returnError("10005", "角色名不能为空");
			}
			sysRole.setUserModified(header.getReqUserId());
			roleManagerMapper.update(sysRole);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("10006", "角色修改异常");
		}
		logger.info("角色修改成功");
		return ServiceUtil.returnSuccess("角色修改成功");
	}
	
	@RequestMapping(value="/deleteRole",method = RequestMethod.POST)
	public String deleteRole(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysRole sysRole = JSONObject.parseObject(bodyStr, new TypeReference<SysRole>() {
			});
			if(sysRole.getId() == null) {
				return ServiceUtil.returnError("10007", "角色ID为空，无法删除");
			}
			roleManagerMapper.delete(sysRole.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("10008", "角色删除异常");
		}
		logger.info("角色删除成功");
		return ServiceUtil.returnSuccess("角色删除成功");
	}
	
	@RequestMapping(value="/addRoleAddMenu",method = RequestMethod.POST)
	public String addRoleAddMenu(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			Map<String,Object> bodyMap = ServiceUtil.jsonStringToMap(bodyStr);
			JSONArray jsonArray = (JSONArray) bodyMap.get("roleMenuList");
			List<SysRoleMenu> sysRoleMenu = JSONObject.parseArray(jsonArray.toJSONString(),SysRoleMenu.class);
			
			if(sysRoleMenu == null || sysRoleMenu.size() == 0) {
				return ServiceUtil.returnError("10009", "数据为空，未分配菜单");
			}
			List<SysRoleMenu> sysRoleMenuList = new ArrayList<SysRoleMenu>();
			Long roleId = 0l;
			for(SysRoleMenu sur:sysRoleMenu) {
				roleId = sur.getRoleId();
				sur.setUserCreate(header.getReqUserId());
				sur.setUserModified(header.getReqUserId());
				sysRoleMenuList.add(sur);
			}
			roleManagerMapper.deleteRoleMenu(roleId);
			roleManagerMapper.insertRoleAddMenu(sysRoleMenuList);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("10011", "角色分配菜单异常");
		}
		logger.info("用户角色分配成功");
		return ServiceUtil.returnSuccess("用户角色分配成功");
	}
}