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
import com.xxw.springcloud.ams.enums.DicEnum;
import com.xxw.springcloud.ams.mapper.file.SuperMapper;
import com.xxw.springcloud.ams.model.ClassifiDic;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.model.UserOperation;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class XmmxController {

	public static Logger logger = LoggerFactory.getLogger(XmmxController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询项目基本信息
	 */
	@RequestMapping("/api/xmmx/query")
	public String query(@RequestBody String inputjson) {

		logger.debug("exc:query params:inputjson=" + inputjson);

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

			totalSize = superMapper.findXmmxByAttrCount(params);

			pageIndex = (pageIndex - 1) * pageSize;
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);

			List<Xmmx> items = superMapper.findXmmxByAttr(params);

			pageIndex = (pageIndex - 1) * pageSize;
			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(items, "xmmxList", header);
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.debug("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 新增/更新项目明细信息
	 */
	@RequestMapping("/api/xmmx/createOrUpdate")
	public String createOrUpdate(@RequestBody String inputjson) {

		logger.debug("exc:query params:inputjson=" + inputjson);

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
					UserOperation uo = new UserOperation(UserOperation.od_jbmx);
					uo.setUserID(header.getReqUserId());
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());

					// 分析五级分类
					Object codeO = params.get("prjClasfiCode");
					if (UtilValidate.isNotEmpty(codeO)) {
						String code = codeO.toString();
						String codeT = code.substring(0, 2);
						ClassifiDic dic = superMapper
								.queryDicByCode2(UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", codeT));
						String name = dic.getName();
						params.put("prjClasfiName1", name);

						if (code.length() >= 4) {
							codeT = code.substring(0, 4);
							dic = superMapper
									.queryDicByCode2(UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", codeT));
							name = dic.getName();
							params.put("prjClasfiName2", name);

							if (code.length() >= 5) {
								codeT = code.substring(0, 5);
								dic = superMapper
										.queryDicByCode2(UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", codeT));
								name = dic.getName();
								params.put("prjClasfiName3", name);

								if (code.length() >= 8) {
									codeT = code.substring(0, 8);
									dic = superMapper.queryDicByCode2(
											UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", codeT));
									name = dic.getName();
									params.put("prjClasfiName4", name);

									if (code.length() >= 10) {
										codeT = code.substring(0, 10);
										dic = superMapper.queryDicByCode2(
												UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", codeT));
										name = dic.getName();
										params.put("prjClasfiName5", name);
									}
								}
							}
						}
					}

					Object id = params.get("id");
					if (UtilValidate.isNotEmpty(id)) {

						Xmmx xmmx = superMapper.queryXmmxByID(Long.parseLong(id + ""));
						if (UtilValidate.isNotEmpty(xmmx)) {
							uo.setOperAction(UserOperation.oa_u);
							superMapper.updateXmmx(params);
							save = true;
						} else {
							reM = ServiceUtil.returnError("E", "查询指定id=[" + id + "]，未查询到指定数据，操作失败！");
						}
					} else {
						uo.setOperAction(UserOperation.oa_c);
						superMapper.saveXmmx2(params);
						save = true;
					}

					if (save) {
						uo.setPrjSN(prjSN + "");// 许可证号
						superMapper.saveUserOper(uo);
						reM = ServiceUtil.returnSuccess("保存成功 ！");
					}
				} else {
					reM = ServiceUtil.returnError("E", "通过项目许可证未查询到项目信息！");
				}
			} else {
				reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "保存异常！" + e.getMessage());
		}

		logger.debug("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 删除项目明细信息
	 */
	@RequestMapping("/api/xmmx/del")
	public String del(@RequestBody String inputjson) {

		logger.debug("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "删除异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object id = params.get("id");
			if (UtilValidate.isNotEmpty(id)) {
				Xmmx xmmx = superMapper.queryXmmxByID(Long.parseLong(id + ""));
				if (UtilValidate.isNotEmpty(xmmx)) {
					UserOperation uo = new UserOperation(UserOperation.od_jbmx);
					uo.setUserID(header.getReqUserId());
					uo.setOperAction(UserOperation.oa_d);
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());
					uo.setPrjSN(xmmx.getPrjSN());// 许可证号
					superMapper.saveUserOper(uo);
					superMapper.delXmmx(params);
					reM = ServiceUtil.returnSuccess("删除成功 ！");
				} else {
					reM = ServiceUtil.returnError("E", "删除失败，未查询到指定数据！id=[" + id + "]");
				}

			} else {
				reM = ServiceUtil.returnError("E", "删除时，ID 必传！");
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "删除异常！" + e.getMessage());
		}

		logger.debug("exc:query return:" + reM);

		return reM;
	}
}