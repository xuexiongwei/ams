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
import com.xxw.springcloud.ams.model.UserOperation;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class LogController {

	public static Logger logger = LoggerFactory.getLogger(LogController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询指定日期区间的操作日志
	 */
	@RequestMapping("/api/userOperation/queryUserOperByDate")
	public String query(@RequestBody String inputjson) {

		logger.debug("exc:queryUserOperByDate params:inputjson=" + inputjson);

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

			Object startDate = params.get("startDate");
			Object endDate = params.get("endDate");
			if (UtilValidate.isEmpty(startDate) || UtilValidate.isEmpty(endDate)) {
				reM = ServiceUtil.returnError("E", "查询时，开始日期和结束日期不能为空！");
			} else {

				params.put("startDate", startDate + " 00:00:00");
				params.put("endDate", startDate + " 23:59:59");

				totalSize = superMapper.findUserOperByAttrCount(params);

				pageIndex = (pageIndex - 1) * pageSize;
				params.put("pageSize", pageSize);
				params.put("pageIndex", pageIndex);
				List<UserOperation> items = superMapper.findUserOperByAttr(params);

				header.setRspPageCount(totalSize);
				reM = ServiceUtil.returnSuccess(items, "userOperationList", header);
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.debug("exc:queryUserOperByDate return:" + reM);

		return reM;
	}
}