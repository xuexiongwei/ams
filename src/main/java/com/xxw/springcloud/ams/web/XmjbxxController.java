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
import com.xxw.springcloud.ams.util.Check;
import com.xxw.springcloud.ams.util.DateUtils;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StringUtils;
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

		logger.info("exc:query params:inputjson=" + inputjson);

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

		logger.info("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 新增项目基本信息
	 */
	@RequestMapping("/api/xmjbxx/createOrUpdate")
	public String createOrUpdate(@RequestBody String inputjson) {

		logger.info("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "新增异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");
			if (UtilValidate.isNotEmpty(prjSN)) {
				String year = "";
				boolean check = true;
				if ((prjSN + "").length() < 4) {
					check = false;
					reM = ServiceUtil.returnError("E", "项目许可证长度不满足要求，无法提取项目日期！");
				}
				if ((prjSN + "").length() >= 4) {
					year = StringUtils.getYear(prjSN + "");
					if (UtilValidate.isEmpty(year)) {
						check = false;
						reM = ServiceUtil.returnError("E", "项目许可证 中无法提取项目日期！");
					}
				}
				if (UtilValidate.isEmpty(params.get("prjUnit"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "建设单位 为必填项！");
				}
				if (UtilValidate.isEmpty(params.get("prjType"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "建设类型 为必填项！");
				} else {
					if ("新建|改扩建".indexOf(params.get("prjType") + "") == -1) {
						check = false;
						reM = ServiceUtil.returnError("E", "建设类型 为[新建、改扩建]！");
					}
				}
				if (UtilValidate.isNotEmpty(params.get("prjTemSN"))) {
					if ((params.get("prjTemSN") + "").indexOf("临") == -1) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效临建证号！");
					}
				}
				if (UtilValidate.isEmpty(params.get("prjSNType"))) {
					check = false;
					reM = ServiceUtil.returnError("E", "许可证类型 为必填项！");
				} else {
					if ("城镇建设项目|乡村建设项目|临时建设项目|补正项目".indexOf(params.get("prjSNType") + "") == -1) {
						check = false;
						reM = ServiceUtil.returnError("E", "许可证类型 为[城镇建设项目、乡村建设项目、临时建设项目、补正项目]！");
					} else {
						if ("临时建设项目".equals(params.get("prjSNType") + "")) {
							// 发件日期 选填，格式为（年/月/日），*临建项目必填
							if (UtilValidate.isEmpty(params.get("noticeTime"))) {
								check = false;
								reM = ServiceUtil.returnError("E", "发件日期 临建项目必填！");
							}
							// 有效期（月） 选填，数字，整数，*临建项目必填
							if (UtilValidate.isEmpty(params.get("effectiveTime"))) {
								check = false;
								reM = ServiceUtil.returnError("E", "有效期（月） 临建项目必填！");
							}
						}

					}
				}

				if (UtilValidate.isNotEmpty(params.get("noticeTime"))) {
					if (!Check.check(Check.date1, params.get("noticeTime") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [发件日期],格式YYYY/MM/DD！");
					}
				}

				if (UtilValidate.isNotEmpty(params.get("effectiveTime"))) {
					if (!Check.check(Check.zzs, params.get("effectiveTime") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [有效期（月）],整数！");
					}
				}
				if (UtilValidate.isNotEmpty(params.get("delayCountDay"))) {
					if (!Check.check(Check.zzs, params.get("delayCountDay") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [延长期（月）],整数！");
					}
				}
				if (UtilValidate.isNotEmpty(params.get("correctionDate"))) {
					if (!Check.check(Check.date1, params.get("correctionDate") + "")) {
						check = false;
						reM = ServiceUtil.returnError("E", "请输入有效 [补正日期],格式YYYY/MM/DD！");
					} else if (DateUtils.nowDateString(DateUtils.FORMAT6)
							.compareTo(params.get("correctionDate") + "") < 0) {
						check = false;
						reM = ServiceUtil.returnError("E", "补正日期 必须小于当前系统日期!");
					}
				}

				/**
				 * 延期文号 选填，文本，50个字符，为空时【延长期】必为空；填写时【延长期】必填写 <br>
				 * 延长期（月） 选填，数字，整数，为空时【延期文号】必为空；填写时【延期文号】必填写
				 * 
				 */
				if (UtilValidate.isEmpty(params.get("delaySN")) && UtilValidate.isNotEmpty("delayCountDay")
						|| UtilValidate.isEmpty(params.get("delayCountDay")) && UtilValidate.isNotEmpty("delaySN")) {
					check = false;
					reM = ServiceUtil.returnError("E", "[延期文号]&[延长期（月）] 必须同时存在或同时不存在！");
				}
				/**
				 * 补正证号 选填，文本，50个字符，为空时【补正日期】必为空；填写时【补正日期】必填写<br>
				 * 补正日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【补正证号】必为空；填写时【补正证号】必填写
				 * 
				 */
				if (UtilValidate.isEmpty(params.get("correctionSN")) && UtilValidate.isNotEmpty("correctionDate")
						|| UtilValidate.isEmpty(params.get("correctionDate"))
								&& UtilValidate.isNotEmpty("correctionSN")) {
					check = false;
					reM = ServiceUtil.returnError("E", "[补正证号]&[补正日期] 必须同时存在或同时不存在！");
				}

				// 备注 选填，需判断本许可证号，若含有“补正”，这里必填原许可证号，格式如“2004规（朝）建字0190号补正”
				if ((prjSN + "").indexOf("补正") != 1) {
					if (UtilValidate.isEmpty(params.get("remark"))) {
						check = false;
						reM = ServiceUtil.returnError("E", "本许可证号，含有“补正”，[备注]必填原许可证号！");
					} else if ((params.get("remark") + "").indexOf("号补正") == -1) {
						check = false;
						reM = ServiceUtil.returnError("E", "本许可证号，含有“补正”，[备注]必填原许可证号，格式如“2004规（朝）建字0190号补正”");
					}
				}

				if (check) {

					// 查询此项目信息是否存在queryXmjbxx
					Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN + "");

					UserOperation uo = new UserOperation(UserOperation.od_jbxx);
					uo.setUserID(header.getReqUserId());
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());

					// 项目年份
					jbxx.setPrjYear(year);

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
				}
			} else {
				reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "新增异常！" + e.getMessage());
		}

		logger.info("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 删除项目属性
	 */
	@RequestMapping("/api/xmjbxx/del")
	public String del(@RequestBody String inputjson) {

		logger.info("exc:del params:inputjson=" + inputjson);

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

		logger.info("exc:del return:" + reM);

		return reM;
	}
}