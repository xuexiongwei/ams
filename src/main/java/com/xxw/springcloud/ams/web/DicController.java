package com.xxw.springcloud.ams.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xxw.springcloud.ams.mapper.file.SuperMapper;
import com.xxw.springcloud.ams.model.ClassifiDic;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.SerialNumberUtil;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class DicController {

	public static Logger logger = LoggerFactory.getLogger(DicController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询指定项目经纬度信息
	 */
	@RequestMapping("/api/dic/queryByType")
	public String queryByType(@RequestBody String inputjson) {

		logger.debug("exc:queryByType params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "请求异常！");
		Header header = ServiceUtil.getContextHeader(inputjson);
		String bodyStr = ServiceUtil.getContextBody(inputjson);
		Map<String, Object> params = JSONObject.parseObject(bodyStr);

		Object type = params.get("type");
		if (UtilValidate.isNotEmpty(type)) {
			List<ClassifiDic> items = superMapper.queryDicByCode3(params);
			if (UtilValidate.isEmpty(items)) {
				items = FastList.newInstance();
			}
			reM = ServiceUtil.returnSuccess(items, "classifiDicList", header);
		} else {
			reM = ServiceUtil.returnError("E", "字典类型 必输！");
		}

		logger.debug("exc:queryByType return:" + reM);

		return reM;
	}

	/**
	 * 新增字典信息
	 */
	@RequestMapping("/api/dic/create")
	public String create(@RequestBody String inputjson) {

		logger.debug("exc:queryByType params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "请求异常！");
		String bodyStr = ServiceUtil.getContextBody(inputjson);
		Map<String, Object> params = JSONObject.parseObject(bodyStr);

		Object type = params.get("type");

		if (UtilValidate.isNotEmpty(type)) {
			params.put("id", SerialNumberUtil.getUUID());
			superMapper.saveDic2(params);
			reM = ServiceUtil.returnSuccess("保存成功 ！");
		} else {
			reM = ServiceUtil.returnError("E", "字典类型 必输！");
		}

		logger.debug("exc:queryByType return:" + reM);

		return reM;
	}
}