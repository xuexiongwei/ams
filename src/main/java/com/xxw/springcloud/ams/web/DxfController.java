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
import com.xxw.springcloud.ams.model.DxfEntity;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.FastMap;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilValidate;
import com.xxw.springcloud.ams.util.dxf.DxfUtils;

@RestController
public class DxfController {

	public static Logger logger = LoggerFactory.getLogger(DxfController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询指定项目经纬度信息
	 */
	@RequestMapping("/api/dxf/query")
	public String query(@RequestBody String inputjson) {

		logger.debug("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "请求异常！");
		Header header = ServiceUtil.getContextHeader(inputjson);
		String bodyStr = ServiceUtil.getContextBody(inputjson);
		Map<String, Object> params = JSONObject.parseObject(bodyStr);

		Object prjSN = params.get("prjSN");
		if (UtilValidate.isNotEmpty(prjSN)) {
			List<DxfEntity> items = superMapper.queryDxfEntity(params);
			if (UtilValidate.isEmpty(items)) {
				items = FastList.newInstance();
			}
			reM = ServiceUtil.returnSuccess(items, "points", header);
		} else {
			reM = ServiceUtil.returnError("E", "项目许可证号 必输！");
		}

		logger.debug("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 直角坐标转经纬度
	 */
	@RequestMapping("/api/dxf/convertZB")
	public String convertZB(@RequestBody String inputjson) {

		logger.debug("exc:convertZB params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "请求异常！");
		Header header = ServiceUtil.getContextHeader(inputjson);
		String bodyStr = ServiceUtil.getContextBody(inputjson);
		Map<String, Object> params = JSONObject.parseObject(bodyStr);

		Object jdo = params.get("jd");
		Object wdo = params.get("wd");
		if (UtilValidate.isNotEmpty(jdo)) {
			if (UtilValidate.isNotEmpty(wdo)) {
				Map<String, String> m = FastMap.newInstance();
				Double[] dd = DxfUtils.getAinCoordinate(jdo + "", wdo + "");
				if (UtilValidate.isNotEmpty(dd)) {
					m.put("longlatV", (dd[1] + DxfUtils.jc) + "," + (dd[0] + DxfUtils.wc));
					reM = ServiceUtil.returnSuccess(m, header);
				} else {
					reM = ServiceUtil.returnError("E", "转换异常！");
				}
			} else {
				reM = ServiceUtil.returnError("E", "纬度数据为空！");
			}
		} else {
			reM = ServiceUtil.returnError("E", "经度数据为空！");
		}
		logger.debug("exc:convertZB return:" + reM);

		return reM;
	}

}