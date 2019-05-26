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
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilMisc;
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

		logger.debug("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 新增/修改项目属性信息
	 */
	@RequestMapping("/api/xmsx/createOrUpdate")
	public String createOrUpdate(@RequestBody String inputjson) {

		logger.debug("exc:createOrUpdate params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "保存异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");
			if (UtilValidate.isNotEmpty(prjSN)) {

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
					List<Xmsx> sxl = superMapper.queryXmsxByPrjSNAndSerialNumber2(
							UtilMisc.toMap("prjSN", params.get("prjSN"), "serialNumber", params.get("serialNumber")));
					String buldStatus = "";// 工程状态
					String checkSN = "";
					String cancelSN = "";
					for (Xmsx xmsx : sxl) {
						String checkSNT = xmsx.getCheckSN();// 验收文号
						String cancelSNT = xmsx.getCancelSN();// 撤（注）销证号
						if (UtilValidate.isNotEmpty(checkSNT))
							checkSN = checkSNT;
						if (UtilValidate.isNotEmpty(cancelSNT))
							cancelSN = cancelSNT;
					}
					if (UtilValidate.isEmpty(checkSN) && UtilValidate.isEmpty(cancelSN)) {
						buldStatus = "未申报";
					} else if (UtilValidate.isEmpty(checkSN) && UtilValidate.isNotEmpty(cancelSN)) {
						buldStatus = "已撤（注）销";
					} else if (UtilValidate.isNotEmpty(checkSN) && UtilValidate.isEmpty(cancelSN)) {
						buldStatus = "已验收";
					} else {
						buldStatus = "未分析出工程状态！";
					}
					superMapper.updateBuldStatusByPrjSNAndSerialNumber(UtilMisc.toMap("prjSN", params.get("prjSN"),
							"serialNumber", params.get("serialNumber"), "buldStatus", buldStatus));

					// 更新项目状态
					List<Xmsx> sxL = superMapper.queryXmsxByPrjSN(params.get("prjSN") + "");
					if (UtilValidate.isNotEmpty(sxL)) {
						String prjStatus = "";// 项目状态
						int count = sxL.size();
						int ys = 0;// 验收个数
						int czx = 0;// 撤（注）销 个数

						boolean hasDelaySN = false;// 是否存在延期文号
						boolean hasCorrectionSN = false;// 是否存补正证号
						for (Xmsx sxt : sxL) {
							checkSN = sxt.getCheckSN();// 验收文号
							cancelSN = sxt.getCancelSN();// 撤（注）销证号
							String delaySN = sxt.getDelaySN();// 延期文号
							String correctionSN = sxt.getCorrectionSN();// 补正号

							if (UtilValidate.isNotEmpty(checkSN)) {
								ys++;
							} else if (UtilValidate.isNotEmpty(cancelSN)) {
								czx++;
							}
							if (UtilValidate.isNotEmpty(delaySN) && !hasDelaySN) {
								hasDelaySN = true;
							}
							if (UtilValidate.isNotEmpty(correctionSN) && !hasCorrectionSN) {
								hasCorrectionSN = true;
							}
						}
						if (ys + czx > count) {
							prjStatus = "基础数据有问题，请检查！错误：验收文号个数+撤（注）销个数 大于 总项目数";
						} else if (ys == count && czx == 0) {
							prjStatus = "已验收";
						} else if (ys == 0 && czx == 0) {
							prjStatus = "未申报";
						} else if (ys == 0 && czx == count) {
							prjStatus = "已撤（注）销";
						} else if (ys != count && czx != count && ys + czx == count) {
							prjStatus = "已完结";
						} else if (ys == 0 && czx != count && czx != 0) {
							prjStatus = "部分撤（注）销";
						} else if (ys != 0 && ys != count && czx == 0) {
							prjStatus = "部分验收";
						} else if (ys != 0 && ys + czx != count && czx != 0) {
							prjStatus = "未撤（注）销部分、部分验收";
						} else {
							prjStatus = "未分析出项目状态！";
						}
						superMapper.updatePrjStatusByPrjSN(
								UtilMisc.toMap("prjStatus", prjStatus, "prjSN", params.get("prjSN")));
					}
				}
			} else {
				reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "保存异常！" + e.getMessage());
		}

		logger.debug("exc:createOrUpdate return:" + reM);

		return reM;
	}

	/**
	 * 删除项目属性
	 */
	@RequestMapping("/api/xmsx/del")
	public String del(@RequestBody String inputjson) {

		logger.debug("exc:del params:inputjson=" + inputjson);

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

		logger.debug("exc:del return:" + reM);

		return reM;
	}
}