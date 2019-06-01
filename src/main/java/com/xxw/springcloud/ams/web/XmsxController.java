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
import com.xxw.springcloud.ams.util.Check;
import com.xxw.springcloud.ams.util.DateUtils;
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
				boolean check = true;

				// 建筑序号 必填，数字
				Object serialNumber = params.get("serialNumber");
				if (UtilValidate.isEmpty(serialNumber)) {
					check = false;
					reM = ServiceUtil.returnError("E", "建筑序号必输！");
				} else if (!Check.check(Check.zzs, serialNumber)) {
					check = false;
					reM = ServiceUtil.returnError("E", "建筑序号 必须为 正整数！");
				}

				if (UtilValidate.isEmpty(params.get("prjNature"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "项目性质 必输！");
				}
				if (UtilValidate.isEmpty(params.get("prjAttr"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "规划项目/人防 必输！");
				} else if ("规划项目性质：|人防工程情况：".indexOf(params.get("prjAttr") + "") == -1) {
					check = false;
					reM = ServiceUtil.returnError("E", "规划项目/人防 必须为[规划项目性质：、人防工程情况：]！");
				}

				if (UtilValidate.isNotEmpty(params.get("aboveGroundLev"))
						&& !Check.check(Check.zzs, params.get("aboveGroundLev"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "地上层数 必须为正整数！");
				}
				if (UtilValidate.isNotEmpty(params.get("underGroundLev"))
						&& !Check.check(Check.zzs, params.get("underGroundLev"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "地下层数 必须为正整数！");
				}
				if (UtilValidate.isNotEmpty(params.get("aboveGroundHet"))
						&& !Check.check(Check.zfs, params.get("aboveGroundHet"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "地上高度 必须为最多两位小数的浮点数！");
				}
				if (UtilValidate.isNotEmpty(params.get("underGroundHet"))
						&& !Check.check(Check.zfs, params.get("underGroundHet"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "地下高度 必须为最多两位小数的浮点数！");
				}
				if (UtilValidate.isNotEmpty(params.get("buildings"))
						&& !Check.check(Check.zzs, params.get("buildings"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "栋数 必须为正整数！");
				}
				if (UtilValidate.isNotEmpty(params.get("housingStockNum"))
						&& !Check.check(Check.zzs, params.get("housingStockNum"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "住房套数 必须为正整数！");
				}

				if (UtilValidate.isNotEmpty(params.get("checkDocDate"))) {
					if (!Check.check(Check.date1, params.get("checkDocDate") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [验线日期],格式YYYY/MM/DD！");
					} else if (DateUtils.nowDateString(DateUtils.FORMAT6)
							.compareTo(params.get("checkDocDate") + "") < 0) {
						check = false;
						reM = ServiceUtil.returnError("E", "验线日期必须小于当前系统日期!");
					}
				}
				if (UtilValidate.isNotEmpty(params.get("checkDate"))) {
					if (!Check.check(Check.date1, params.get("checkDate") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [验收日期],格式YYYY/MM/DD！");
					} else if (DateUtils.nowDateString(DateUtils.FORMAT6).compareTo(params.get("checkDate") + "") < 0) {
						check = false;
						reM = ServiceUtil.returnError("E", "验收日期 必须小于当前系统日期!");
					}
				}
				if (UtilValidate.isNotEmpty(params.get("cancelDate"))) {
					if (!Check.check(Check.date1, params.get("cancelDate") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [撤（注）销日期],格式YYYY/MM/DD！");
					} else if (DateUtils.nowDateString(DateUtils.FORMAT6)
							.compareTo(params.get("cancelDate") + "") < 0) {
						check = false;
						reM = ServiceUtil.returnError("E", "撤（注）销日期 必须小于当前系统日期!");
					}
				}
				/**
				 * 验线文号 选填，文本，50个字符，为空时【验线日期】必为空；填写时【验线日期】必填写<br>
				 * 验线日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【验线文号】必为空；填写时【验线文号】必填写
				 * 
				 */
				if (UtilValidate.isEmpty(params.get("checkDocSN")) && UtilValidate.isNotEmpty("checkDocDate")
						|| UtilValidate.isEmpty(params.get("checkDocDate")) && UtilValidate.isNotEmpty("checkDocSN")) {
					check = false;
					reM = ServiceUtil.returnError("E", "[验线文号]&[验线日期] 必须同时存在或同时不存在！");
				}
				/**
				 * 验收文号 选填，文本，50个字符，为空时【验收日期】必为空；填写时【验收日期】必填写<br>
				 * 验收日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【验收文号】必为空；填写时【验收文号】必填写
				 * 
				 */
				if (UtilValidate.isEmpty(params.get("checkSN")) && UtilValidate.isNotEmpty("checkDate")
						|| UtilValidate.isEmpty(params.get("checkDate")) && UtilValidate.isNotEmpty("checkSN")) {
					check = false;
					reM = ServiceUtil.returnError("E", "[验收文号]&[验收日期] 必须同时存在或同时不存在！");
				}
				// 建设项目的验收日期不应早于临建项目的验收日期

				/**
				 * 撤（注）销证号 选填，文本，50个字符，为空时【撤（注）销日期】必为空；填写时【撤（注）销日期】必填写<br>
				 * 撤（注）销日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【撤（注）销证号】必为空；填写时【撤（注）销证号】必填写
				 * 
				 */

				if (UtilValidate.isEmpty(params.get("cancelSN")) && UtilValidate.isNotEmpty("cancelDate")
						|| UtilValidate.isEmpty(params.get("cancelDate")) && UtilValidate.isNotEmpty("cancelSN")) {
					check = false;
					reM = ServiceUtil.returnError("E", "[撤（注）销证号]&[撤（注）销日期] 必须同时存在或同时不存在！");
				}

				if (check) {
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
						reM = ServiceUtil.returnError("E", "根据项目许可证号[" + prjSN + "]，未查询到项目基本信息！");
					}
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