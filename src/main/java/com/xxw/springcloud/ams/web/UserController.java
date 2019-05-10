package com.xxw.springcloud.ams.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xxw.springcloud.ams.mapper.test1.User1Mapper;
import com.xxw.springcloud.ams.mapper.test2.User2Mapper;
import com.xxw.springcloud.ams.model.User;


@RestController
public class UserController {

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
        return user;
    }
    
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public void save(@RequestBody User user) {
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