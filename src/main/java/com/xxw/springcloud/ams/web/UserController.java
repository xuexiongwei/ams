package com.xxw.springcloud.ams.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xxw.springcloud.ams.Context.SessionContext;
import com.xxw.springcloud.ams.mapper.common.SqlMapper;
import com.xxw.springcloud.ams.mapper.test1.User1Mapper;
import com.xxw.springcloud.ams.mapper.test2.User2Mapper;
import com.xxw.springcloud.ams.model.User;


@RestController
public class UserController {

	public static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private User1Mapper user1Mapper;

	@Autowired
	private User2Mapper user2Mapper;
	
	@RequestMapping("/getUsers")
	public List<User> getUsers() {
		List<User> users=user1Mapper.getAll();
		return users;
	}
	
    @RequestMapping("/getUser")
    public User getUser(Long id) {
    	User user=user2Mapper.getOne(id);
    	String serialNumber = (String) SessionContext.get(SessionContext.FieldId.serialNumber.toString());
    	logger.info("serialNumber="+serialNumber);
    	return user;
    }
    
    @RequestMapping(value="/add",method = RequestMethod.POST,consumes="application/json;charset=utf-8",produces="application/json;charset=utf-8")
    public void save(@RequestBody User user) {
       User u = user;
       System.out.println(u.getNickName());
        user2Mapper.insert(user);
    }
    
    @RequestMapping(value="update")
    public void update(User user) {
        user2Mapper.update(user);
    }
    
    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        user1Mapper.delete(id);
    }
    
}