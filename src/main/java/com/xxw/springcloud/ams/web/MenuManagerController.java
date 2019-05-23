package com.xxw.springcloud.ams.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xxw.springcloud.ams.mapper.common.MenuManagerMapper;
import com.xxw.springcloud.ams.mapper.common.UserManagerMapper;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.SysMenu;
import com.xxw.springcloud.ams.model.SysRole;
import com.xxw.springcloud.ams.model.SysRoleMenu;
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.util.ServiceUtil;


@RestController
public class MenuManagerController {

	public static Logger logger = LoggerFactory.getLogger(MenuManagerController.class);

	@Autowired
	private MenuManagerMapper menuManagerMapper;
	
	@Autowired
	private UserManagerMapper userManagerMapper;
	
	/**
	 * 分页获取角色信息
	 * @return
	 */
	@RequestMapping(value="/getMenuAndPage",method = RequestMethod.POST)
	public String getMenuAndPage(@RequestBody String inputjson) {
		List<SysMenu> menus = null;
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
			menus = menuManagerMapper.selectMenuAndPage(pageSize, pageIndex);
			totalSize = menuManagerMapper.selectCount();
			header.setRspPageCount(totalSize);
			logger.info("角色查询成功");
			return ServiceUtil.returnSuccess(menus, "menuList", header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("11001", "菜单查询异常");
		}
	}
	
	@RequestMapping(value="/addMenu",method = RequestMethod.POST)
	public String addMenu(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysMenu sysMenu = JSONObject.parseObject(bodyStr, new TypeReference<SysMenu>() {
			});
			if(sysMenu.getName() == null) {
				return ServiceUtil.returnError("11002", "菜单名不能为空");
			}
			sysMenu.setUserCreate(header.getReqUserId());
			sysMenu.setUserModified(header.getReqUserId());
			menuManagerMapper.insert(sysMenu);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("11003", "菜单创建异常");
		}
		logger.info("菜单添加成功");
		return ServiceUtil.returnSuccess("菜单添加成功");
	}
	
	@RequestMapping(value="/updateMenu",method = RequestMethod.POST)
	public String updateMenu(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysMenu sysMenu = JSONObject.parseObject(bodyStr, new TypeReference<SysMenu>() {
			});
			if(sysMenu.getName() == null) {
				return ServiceUtil.returnError("11002", "菜单名不能为空");
			}
			sysMenu.setUserModified(header.getReqUserId());
			menuManagerMapper.update(sysMenu);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("11004", "菜单修改异常");
		}
		logger.info("菜单修改成功");
		return ServiceUtil.returnSuccess("菜单修改成功");
	}
	
	@RequestMapping(value="/deleteMenu",method = RequestMethod.POST)
	public String deleteMenu(@RequestBody String inputjson) {
		try {
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			
			SysMenu sysMenu = JSONObject.parseObject(bodyStr, new TypeReference<SysMenu>() {
			});
			if(sysMenu.getId() == null) {
				return ServiceUtil.returnError("11005", "菜单ID为空，无法删除");
			}
			menuManagerMapper.delete(sysMenu.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("11006", "菜单删除异常");
		}
		logger.info("菜单删除成功");
		return ServiceUtil.returnSuccess("菜单删除成功");
	}
	
	@RequestMapping(value = "/selectMenusByRoleId", method = RequestMethod.POST)
	public String selectMenuByRoleId(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);

			SysRoleMenu sysRoleMenu = JSONObject.parseObject(bodyStr, new TypeReference<SysRoleMenu>() {
			});
			if (sysRoleMenu.getId() == null) {
				return ServiceUtil.returnError("11007", "角色id为空");
			}
			// 获取已经分配的菜单ID
			List<SysRoleMenu> sysRoleMenuList = menuManagerMapper.selectMenuIdsByRoleId(sysRoleMenu.getId(),100, 0);
			// 获取所有的菜单ID
			List<SysMenu> sysMenuList = menuManagerMapper.selectMenuAndPage(100, 0);
			for (SysMenu sur : sysMenuList) {
				sur.setIsMark(0);
				for (SysRoleMenu surSelect : sysRoleMenuList) {
					if(sur.getId().equals(surSelect.getMenuId())) {
						sur.setIsMark(1);
						break;
					}
				}
			}
			logger.info("用户菜单查询成功");
			return ServiceUtil.returnSuccess(sysMenuList, "menuList", header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("11008", "用户菜单查询异常");
		}
	}
	
	@RequestMapping(value = "/selectMenusByUserId", method = RequestMethod.POST)
	public String selectMenusByUserId(@RequestBody String inputjson) {
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);

			SysUser sysUser = JSONObject.parseObject(bodyStr, new TypeReference<SysUser>() {
			});
			if(sysUser.getId() == null) {
				return ServiceUtil.returnError("00013", "用户ID为空");
			}
			// 获取已经分配的菜单ID
			List<SysRole> sysRoleList = userManagerMapper.selectRoleByUserId(sysUser.getId(),100, 0);
			List<Long> roleIds = new ArrayList<Long>();
			for (SysRole sur : sysRoleList) {
				roleIds.add(sur.getId());
			}
			List<SysMenu> sysMenuList = new ArrayList<SysMenu>();
			if(roleIds.size()>0) {
				sysMenuList = menuManagerMapper.selectMenuIdsByRoleIds(roleIds,100, 0);
			}
			logger.info("用户菜单查询成功");
			return ServiceUtil.returnSuccess(sysMenuList, "menuList", header);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("11008", "用户菜单查询异常");
		}
	}
}