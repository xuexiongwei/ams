package com.xxw.springcloud.ams.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xxw.springcloud.ams.Interceptor.CommonCostInterceptor;
import com.xxw.springcloud.ams.mapper.common.SqlMapper;

@Component 
public class SerialNumberUtil {
	
	@Value("${system.num}")
    private String systemNum;
	
	@Value("${IdWorker.workerId}")
    private Integer wordID;
	
	@Value("${IdWorker.datacenterId}")
    private Integer datacenterID;
	
	@Autowired
	private SqlMapper sqlMapper;
	
	public static SerialNumberUtil serialUtils;
	@PostConstruct
    public void init() {  
        serialUtils = this;
        serialUtils.systemNum = this.systemNum;
        serialUtils.wordID = this.wordID;
        serialUtils.datacenterID = this.datacenterID;
        serialUtils.sqlMapper = this.sqlMapper;
    } 
	
	public static Logger logger = LoggerFactory.getLogger(CommonCostInterceptor.class);
	
	public static String GetSeqID() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		
		String prefix = serialUtils.systemNum + sdf.format(new Date());
		
		IdWorker id = new IdWorker(serialUtils.wordID,serialUtils.datacenterID);
		
		return String.valueOf(id.nextId());
	}
	
	public static String GetSeqNum() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		
		String prefix = serialUtils.systemNum + sdf.format(new Date());
		
		String returnNum = "";
		
		synchronized (SerialNumberUtil.class) {
			returnNum = prefix + String.format("%010d",serialUtils.sqlMapper.GetSeqNum());
		}
		
		return returnNum;
	}
	
    /**
     * 获得一个UUID 
     * @return String UUID 
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号 
        return uuid.replaceAll("-", "");
    }
}
