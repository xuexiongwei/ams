package com.xxw.springcloud.ams.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xxw.springcloud.ams.Context.SessionContext;
import com.xxw.springcloud.ams.util.SerialNumberUtil;


public class CommonCostInterceptor implements HandlerInterceptor {
	
	public static Logger logger = LoggerFactory.getLogger(CommonCostInterceptor.class);
	
	private long start = System.currentTimeMillis();
	
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        start = System.currentTimeMillis();
        SessionContext.cleanAll();
        if(!SessionContext.isInit()){
        	SessionContext.init();
        }
        SessionContext.set(SessionContext.FieldId.serialNumber.toString(), SerialNumberUtil.GetSeqID());
        return true;
    }
	
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		logger.debug("request times ="+(System.currentTimeMillis()-start));
    }
	
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		
	}
}
