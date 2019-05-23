package com.xxw.springcloud.ams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xxw.springcloud.ams.web.MenuManagerController;

@SpringBootApplication
public class AmsApplication {

	public static Logger logger = LoggerFactory.getLogger(AmsApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(AmsApplication.class, args);
		logger.info("\t\n 系统启动成功");
	}
	
}
