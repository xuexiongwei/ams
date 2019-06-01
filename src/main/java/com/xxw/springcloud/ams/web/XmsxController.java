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
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StatusUtils;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class XmsxController {

	public static Logger logger = LoggerFactory.getLogger(XmsxController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询项目基本信息
	 */
	@RequestMapping("/api/xmsx/query")
	public String query(@RequestBody String inputjson) {

		logger.info("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnSuccess("查询成功！");
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

			totalSize = superMapper.findXmsxByAttrCount(params);

			pageIndex = (pageIndex - 1) * pageSize;
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);
			List<Xmsx> items = superMapper.findXmsxByAttr(params);

			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(items, "xmsxList", header);
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.info("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 新增/修改项目属性信息
	 */
	@RequestMapping("/api/xmsx/createOrUpdate")
	public String createOrUpdate(@RequestBody String inputjson) {

		logger.info("exc:createOrUpdate params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "保存异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");
			if (UtilValidate.isNotEmpty(prjSN)) {

				Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN + "");
				if (UtilValidate.isNotEmpty(jbxx)) {
					boolean save = false;
					UserOperation uo = new UserOperation(UserOperation.od_jbsx);
					uo.setUserID(header.getReqUserId());
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());

					Object id = params.get("id");
					if (UtilValidate.isNotEmpty(id)) {
						Xmsx xmsx = superMapper.queryXmsxByID(Long.parseLong(id + ""));
						if (UtilValidate.isNotEmpty(xmsx)) {
							uo.setOperAction(UserOperation.oa_u);
							superMapper.updateXmsx(params);
							save = true;
						} else {
							reM = ServiceUtil.returnError("E", "查询指定id=[" + id + "]，未查询到指定数据，操作失败！");
						}
					} else {
						uo.setOperAction(UserOperation.oa_c);
						superMapper.saveXmsx2(params);
						save = true;
					}
					if (save) {
						uo.setPrjSN(prjSN + "");// 许可证号
						superMapper.saveUserOper(uo);
						reM = ServiceUtil.returnSuccess("保存成功 ！");

						// 判断工程状态,并更新
						StatusUtils.updateBuldStatus(superMapper, prjSN.toString(),
								Long.parseLong(params.get("serialNumber") + ""));
						// 更新项目状态
						StatusUtils.updatePrjStatus(superMapper, prjSN.toString());
					}
				} else {
					reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
				}
			} else {
				reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "保存异常！" + e.getMessage());
		}

		logger.info("exc:createOrUpdate return:" + reM);

		return reM;
	}

	/**
	 * 删除项目属性
	 */
	@RequestMapping("/api/xmsx/del")
	public String del(@RequestBody String inputjson) {

		logger.info("exc:del params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "删除异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object id = params.get("id");
			if (UtilValidate.isNotEmpty(id)) {

				Xmsx xmsx = superMapper.queryXmsxByID(Long.parseLong(id + ""));
				if (UtilValidate.isNotEmpty(xmsx)) {

					UserOperation uo = new UserOperation(UserOperation.od_jbsx);
					uo.setUserID(header.getReqUserId());
					uo.setOperAction(UserOperation.oa_d);
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());
					uo.setPrjSN(xmsx.getPrjSN());// 许可证号
					superMapper.saveUserOper(uo);

					superMapper.delXmsx(params);
					reM = ServiceUtil.returnSuccess("删除成功 ！");
				} else {
					reM = ServiceUtil.returnError("E", "删除失败，未查询到指定数据！id=[" + id + "]");
				}
			}
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "删除异常！" + e.getMessage());
		}

		logger.info("exc:del return:" + reM);

		return reM;
	}
}