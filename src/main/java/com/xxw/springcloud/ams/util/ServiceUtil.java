package com.xxw.springcloud.ams.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xxw.springcloud.ams.Context.SessionContext;
import com.xxw.springcloud.ams.model.Header;

public class ServiceUtil {

	public static final String successCode = "000000";
	public static final String successMsg = "交易成功";
	/**
	 * 通过入参获取头对象
	 * @param inputjson
	 * @return
	 */
	public static Header getContextHeader(String inputjson) {
		Map<String, Object> inputMap = jsonStringToMap(inputjson);
		Map<String, Object> headerMap = null;
		if(inputMap.get("header") != null) {
			headerMap = (Map<String, Object>) inputMap.remove("header");
		}
		String header = mapToJsonString(headerMap);
		
		if(StringUtils.isNotEmpty(header)) {
			Header headerObj = JSON.parseObject(header, new TypeReference<Header>() {});
			return headerObj;
		}
		return null;
	}
	
	/**
	 * 通过入参获取body
	 * @param inputjson
	 * @return
	 */
	public static String getContextBody(String inputjson) {
		Map<String, Object> inputMap = jsonStringToMap(inputjson);
		inputMap.remove("header");
		return mapToJsonString(inputMap);
	}
	
	/**
	 * 返回成功信息，可传递成功消息，也可以不传递
	 * @param msg
	 * @return
	 */
	public static String returnSuccess(String... msg) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Header headerObj = new Header();
		headerObj.setRspReturnCode(successCode);
		String msg0 = "【"+SessionContext.get(SessionContext.FieldId.serialNumber.toString())+"】";
		if(msg.length>0) {
			msg0 += msg[0];
		}else {
			msg0 += successMsg;
		}
		headerObj.setRspReturnMsg(msg0);
		returnMap.put("header", headerObj);
		return JSON.toJSONString(returnMap);
	}
	
	/**
	 * 返回失败信息
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String returnError(String code,String msg) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Header headerObj = new Header();
		headerObj.setRspReturnCode(code);
		headerObj.setRspReturnMsg(msg);
		returnMap.put("header", headerObj);
		return JSON.toJSONString(returnMap);
	}
	
	/**
	 * 根据beanlist返回标准成功信息
	 * @param bean
	 * @param header
	 * @return
	 */
	public static String returnSuccess(List beanList,String listKey,Header header) {
		Map<String, Object> returnMap =  new HashMap<String, Object>(); 
		if(header == null) {
			header = new Header();
		}
		header.setRspReturnCode(successCode);
		String msg0 = "【"+SessionContext.get(SessionContext.FieldId.serialNumber.toString())+"】"+successMsg;
		header.setRspReturnMsg(msg0);
		String headerStr = JSON.toJSONString(header);
		Map<String, Object> headerMap = jsonStringToMap(headerStr);
		returnMap.put("header", headerMap);
		returnMap.put(listKey, beanList);
		return mapToJsonString(returnMap);
	}
	
	/**
	 * 根据bean返回标准成功信息
	 * @param bean
	 * @param header
	 * @return
	 */
	public static String returnSuccess(Object bean,Header header) {
		Map<String, Object> returnMap =  new HashMap<String, Object>(); 
		if(header == null) {
			header = new Header();
		}
		header.setRspReturnCode(successCode);
		String msg0 = "【"+SessionContext.get(SessionContext.FieldId.serialNumber.toString())+"】"+successMsg;
		header.setRspReturnMsg(msg0);
		String headerStr = JSON.toJSONString(header);
		Map<String, Object> headerMap = jsonStringToMap(headerStr);
		returnMap.put("header", headerMap);
		String beanStr = JSON.toJSONString(bean);
		Map<String, Object> beanMap = jsonStringToMap(beanStr);
		returnMap.putAll(beanMap);
		return mapToJsonString(returnMap);
	}
	
	public static Map<String, Object> jsonStringToMap(String jsonString, int... length) {
		SerializeConfig config = SerializeConfig.getGlobalInstance();  
    	config.put(BigDecimal.class, BigDecimalCodecDefined.instance);
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		jsonString = replace(jsonString);
		Map<String, Object> inputMap = new HashMap<String, Object>();
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		inputMap = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, Object>>() {
		});
		return inputMap;
	}
	
	public static String mapToJsonString(Map<String, Object> inputMap) {
		if (null == inputMap) {
			return null;
		}
		String jsonString = JSON.toJSONString(inputMap,SerializerFeature.WriteBigDecimalAsPlain);
		return jsonString;
	}
	
	public static String replace(String str) {
	    String destination = "";
	    if (str!=null) {
	        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	        Matcher m = p.matcher(str);
	        destination = m.replaceAll("");
	    }
	    return destination;
	}
}
