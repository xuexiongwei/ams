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
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.model.UserOperation;
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
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");
			if (UtilValidate.isNotEmpty(prjSN)) {
				// 查询此项目信息是否存在queryXmjbxx
				Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN + "");

				UserOperation uo = new UserOperation(UserOperation.od_jbxx);
				uo.setUserID(header.getReqUserId());
				SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
				uo.setUserName(user.getUserName());

				if (UtilValidate.isNotEmpty(prjSN)) {
					// 乡村建设项目
					if ((prjSN + "").indexOf("乡") != -1) {
						params.put("prjSNType", "乡村建设项目");
					} else if ((prjSN + "").indexOf("临") != -1) {
						params.put("prjSNType", "临时建设项目");
					} else if ((prjSN + "").indexOf("补正") != -1) {
						params.put("prjSNType", "补正项目");
					} else {
						params.put("prjSNType", "城镇建设项目");
					}
				} else {
					params.put("prjSNType", "");
				}

				if (UtilValidate.isNotEmpty(jbxx)) {
					uo.setOperAction(UserOperation.oa_u);
					superMapper.updateXmjbxx2(params);
				} else {
					uo.setOperAction(UserOperation.oa_c);
					superMapper.saveXmjbxx2(params);
				}
				uo.setPrjSN(params.get("prjSN") + "");// 许可证号
				superMapper.saveUserOper(uo);
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

	/**
	 * 删除项目属性
	 */
	@RequestMapping("/api/xmjbxx/del")
	public String del(@RequestBody String inputjson) {

		logger.debug("exc:del params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "删除异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");
			if (UtilValidate.isNotEmpty(prjSN)) {
				Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN + "");
				if (UtilValidate.isNotEmpty(jbxx)) {
					UserOperation uo = new UserOperation(UserOperation.od_jbxx);
					uo.setUserID(header.getReqUserId());
					uo.setOperAction(UserOperation.oa_d);
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());
					superMapper.delXmjbxxByPrjSN(params);

					uo.setPrjSN(jbxx.getPrjSN());// 许可证号
					superMapper.saveUserOper(uo);
					reM = ServiceUtil.returnSuccess("删除成功 ！");
				} else {
					reM = ServiceUtil.returnSuccess("删除数据不存在，或已被删除，无需再次删除 ！prjSN=[" + prjSN + "]");
				}
			} else {
				reM = ServiceUtil.returnError("E", "删除失败，项目许可证号不能为空！");
			}
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "删除异常！" + e.getMessage());
		}

		logger.debug("exc:del return:" + reM);

		return reM;
	}
}