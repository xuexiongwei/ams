package com.xxw.springcloud.ams.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xxw.springcloud.ams.enums.UserSexEnum;
import com.xxw.springcloud.ams.mapper.test1.User1Mapper;
import com.xxw.springcloud.ams.model.User;


@RunWith(SpringRunner.class)
@SpringBootTest
public class User1MapperTest {

	@Autowired
	private User1Mapper userMapper;

	@Test
	public void testInsert() throws Exception {
		userMapper.insert(new User("aa", "a123456", UserSexEnum.MAN));
		userMapper.insert(new User("bb", "b123456", UserSexEnum.WOMAN));
		userMapper.insert(new User("cc", "b123456", UserSexEnum.WOMAN));

		Assert.assertEquals(userMapper.getAll().size(), userMapper.getAll().size());
	}

	@Test
	public void testQuery() throws Exception {
		List<User> users = userMapper.getAll();
		if(users==null || users.size()==0){
			System.out.println("is null");
		}else{
			System.out.println(users.size());
		}
	}
	
	
	@Test
	public void testUpdate() throws Exception {
		User user = userMapper.getOne(28l);
		System.out.println(user.toString());
		user.setNickName("neo");
		userMapper.update(user);
		Assert.assertTrue(("neo".equals(userMapper.getOne(28l).getNickName())));
	}

}