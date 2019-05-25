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
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilValidate;

/**
 * 模糊查询功能，页面用于自动补全
 * 
 * @author yangwei
 *
 */
@RestController
public class QueryMhController {

	public static Logger logger = LoggerFactory.getLogger(QueryMhController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询字典
	 */
	@RequestMapping("/api/mh/queryDicByNameLike")
	public String queryByType(@RequestBody String inputjson) {

		logger.debug("exc:queryByType params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "请求异常！");
		Header header = ServiceUtil.getContextHeader(inputjson);
		String bodyStr = ServiceUtil.getContextBody(inputjson);
		Map<String, Object> params = JSONObject.parseObject(bodyStr);

		Object name = params.get("name");
		Object other = params.get("other");
		if (UtilValidate.isNotEmpty(name)) {

			if (null == other)
				params.put("other", "");

			List<ClassifiDic> items = superMapper.queryDicByNameLike(params);
			if (UtilValidate.isEmpty(items)) {
				items = FastList.newInstance();
			}
			reM = ServiceUtil.returnSuccess(items, "classifiDicList", header);
		} else {
			reM = ServiceUtil.returnError("E", "字典名称 必输！");
		}

		logger.debug("exc:queryByType return:" + reM);

		return reM;
	}

}