package com.xxw.springcloud.ams.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xxw.springcloud.ams.mapper.common.UserManagerMapper;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.SysUser;
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
	public String getUsersAndPage() {
		int pageSize = 10;
		int pageIndex = 0;
		List<SysUser> users = null;
		int totalSize = 0;
		try {
			users = userManagerMapper.selectUserAndPage(pageSize, pageIndex);
			totalSize = userManagerMapper.selectCount();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServiceUtil.returnError("00001", "用户查询异常");
		}
		Header header = new Header();
		header.setRspPageCount(totalSize);
		return ServiceUtil.returnSuccess(users, "userList", header);
	}
	
	@RequestMapping(value = "/getUserByUsername", method = RequestMethod.POST)
	public String getUser(String username) {
		SysUser user = null;
		List<SysUser> users = null;
		try {
			users = userManagerMapper.selectUserByUsernameAndPage(username, 1, 1);
			if (null != users) {
				user = users.get(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServiceUtil.returnError("00002", "用户查询异常");
		}
		return ServiceUtil.returnSuccess(user, null);
	}
    
}