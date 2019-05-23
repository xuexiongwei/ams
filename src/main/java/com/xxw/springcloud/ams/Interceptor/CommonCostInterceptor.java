package com.xxw.springcloud.ams.Interceptor;

import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xxw.springcloud.ams.Context.SessionContext;
import com.xxw.springcloud.ams.util.SerialNumberUtil;
import com.xxw.springcloud.ams.util.ServiceUtil;

public class CommonCostInterceptor implements HandlerInterceptor {

	public static Logger logger = LoggerFactory.getLogger(CommonCostInterceptor.class);

	private long start = System.currentTimeMillis();

	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
			start = System.currentTimeMillis();
			SessionContext.cleanAll();
			if(!SessionContext.isInit()){
				SessionContext.init();
			}
			String seqID = SerialNumberUtil.GetSeqID();
			MDC.put(SessionContext.FieldId.serialNumber.toString(), seqID);
			SessionContext.set(SessionContext.FieldId.serialNumber.toString(),seqID);
			//非登录请求，我们做是否登录校验
			if(httpServletRequest.getRequestURI().indexOf("/amsLogin") == -1) {
				String reqSessionId = "";
				Cookie[] cookies =  httpServletRequest.getCookies();
				if(cookies != null){
			        for(Cookie cookie : cookies){
			            if(cookie.getName().equals("sessionId")){
			            	reqSessionId = cookie.getValue();
			            }
			        }
			    }
				HttpSession session = httpServletRequest.getSession();
				if(StringUtils.isEmpty(reqSessionId)) {
					sendJsonData(httpServletResponse,ServiceUtil.returnError("90001", "用户未登录或登录已超时"));
					return false;
				}
				if(reqSessionId.equals(session.getId())) {
					return true;
				}
				sendJsonData(httpServletResponse,ServiceUtil.returnError("90001", "用户未登录或登录已超时"));
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return true;
    }

	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
		logger.info("request times =" + (System.currentTimeMillis() - start));
		SessionContext.cleanAll();
		MDC.clear();
	}

	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
		logger.info("request times 1=" + (System.currentTimeMillis() - start));
	}

	protected void sendJsonData(HttpServletResponse response, String data) throws Exception {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}
}
