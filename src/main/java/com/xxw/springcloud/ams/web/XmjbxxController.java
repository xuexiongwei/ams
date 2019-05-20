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
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class XmjbxxController {

	public static Logger logger = LoggerFactory.getLogger(XmjbxxController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询项目基本信息
	 */
	@RequestMapping("/api/xmjbxx/query")
	public String query(@RequestBody String inputjson) {

		logger.debug("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "查询异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);
			int totalSize = 0;
			int pageSize = 10;
			int pageIndex = 0;
			if (header != null) {
				pageSize = header.getReqpageSize();
				pageIndex = header.getReqpageIndex();
			} else {
				header = new Header();
			}

			totalSize = superMapper.findXmjbxxByAttrCount(params);

			pageIndex = (pageIndex - 1) * pageSize;
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);
			List<Xmjbxx> items = superMapper.findXmjbxxByAttr(params);

			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(items, "xmjbxxList", header);
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.debug("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 新增项目基本信息
	 */
	@RequestMapping("/api/xmjbxx/createOrUpdate")
	public String createOrUpdate(@RequestBody String inputjson) {

		logger.debug("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "新增异常！");
		try {
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");
			if (UtilValidate.isNotEmpty(prjSN)) {
				// 查询此项目信息是否存在queryXmjbxx
				Xmjbxx jbxx = superMapper.queryXmjbxx(prjSN + "");
				if (UtilValidate.isNotEmpty(jbxx)) {
					superMapper.updateXmjbxx2(params);
				} else {
					superMapper.saveXmjbxx2(params);
				}
				reM = ServiceUtil.returnSuccess("保存成功！");
			} else {
				reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "新增异常！" + e.getMessage());
		}

		logger.debug("exc:query return:" + reM);

		return reM;
	}
}