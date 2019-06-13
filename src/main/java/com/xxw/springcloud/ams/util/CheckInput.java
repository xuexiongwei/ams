package com.xxw.springcloud.ams.util;

import java.util.List;
import java.util.Map;

/**
 * 格式验证
 * 
 * @author yangwei
 *
 */
public class CheckInput {

	/**
	 * 基本信息录入检查
	 * 
	 * @param params
	 * @return
	 */
	public static String jbxxC(Map<String, Object> params) {

		String reM = "";
		Object prjSN = params.get("prjSN");
		if (UtilValidate.isEmpty(prjSN)) {
			reM = ServiceUtil.returnError("E", "项目许可证号不能为空！");
		}
		if ((prjSN + "").length() < 4) {
			reM = ServiceUtil.returnError("E", "项目许可证长度不满足要求，无法提取项目日期！");
		}
		if ((prjSN + "").length() >= 4) {
			String year = StringUtils.getYear(prjSN + "");
			if (UtilValidate.isEmpty(year)) {
				reM = ServiceUtil.returnError("E", "项目许可证 中无法提取项目日期！");
			}
		}
		if (UtilValidate.isEmpty(params.get("prjUnit"))) {
			reM = ServiceUtil.returnError("E", "建设单位 为必填项！");
		}
		if (UtilValidate.isEmpty(params.get("prjType"))) {
			reM = ServiceUtil.returnError("E", "建设类型 为必填项！");
		} else {
			if ("新建|改扩建".indexOf(params.get("prjType") + "") == -1) {
				reM = ServiceUtil.returnError("E", "建设类型 为[新建、改扩建]！");
			}
		}
		if (UtilValidate.isNotEmpty(params.get("prjTemSN"))) {
			if ((params.get("prjTemSN") + "").indexOf("临") == -1) {
				reM = ServiceUtil.returnError("E", "请输入有效临建证号！");
			}
		}
		if (UtilValidate.isEmpty(params.get("prjSNType"))) {
			reM = ServiceUtil.returnError("E", "许可证类型 为必填项！");
		} else {
			if ("城镇建设项目|乡村建设项目|临时建设项目|补正项目".indexOf(params.get("prjSNType") + "") == -1) {
				reM = ServiceUtil.returnError("E", "许可证类型 为[城镇建设项目、乡村建设项目、临时建设项目、补正项目]！");
			} else {
				if ("临时建设项目".equals(params.get("prjSNType") + "")) {
					// 发件日期 选填，格式为（年/月/日），*临建项目必填
					if (UtilValidate.isEmpty(params.get("noticeTime"))) {
						reM = ServiceUtil.returnError("E", "发件日期 临建项目必填！");
					}
					// 有效期（月） 选填，数字，整数，*临建项目必填
					if (UtilValidate.isEmpty(params.get("effectiveTime"))) {
						reM = ServiceUtil.returnError("E", "有效期（月） 临建项目必填！");
					}
				}

			}
		}

		if (UtilValidate.isNotEmpty(params.get("noticeTime"))) {
			if (!Check.check(Check.date1, params.get("noticeTime") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [发件日期],格式YYYY/MM/DD！");
			}
		}

		if (UtilValidate.isNotEmpty(params.get("effectiveTime"))) {
			if (!Check.check(Check.zzs, params.get("effectiveTime") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [有效期（月）],整数！");
			}
		}
		if (UtilValidate.isNotEmpty(params.get("delayCountDay"))) {
			if (!Check.check(Check.zzs, params.get("delayCountDay") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [延长期（月）],整数！");
			}
		}
		if (UtilValidate.isNotEmpty(params.get("correctionDate"))) {
			if (!Check.check(Check.date1, params.get("correctionDate") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [补正日期],格式YYYY/MM/DD！");
			} else if (DateUtils.nowDateString(DateUtils.FORMAT6).compareTo(params.get("correctionDate") + "") < 0) {
				reM = ServiceUtil.returnError("E", "补正日期 必须小于当前系统日期!");
			}
		}

		/**
		 * 延期文号 选填，文本，50个字符，为空时【延长期】必为空；填写时【延长期】必填写 <br>
		 * 延长期（月） 选填，数字，整数，为空时【延期文号】必为空；填写时【延期文号】必填写
		 * 
		 */
		if (UtilValidate.isEmpty(params.get("delaySN")) && UtilValidate.isNotEmpty(params.get("delayCountDay"))
				|| UtilValidate.isEmpty(params.get("delayCountDay"))
						&& UtilValidate.isNotEmpty(params.get("delaySN"))) {
			reM = ServiceUtil.returnError("E", "[延期文号]&[延长期（月）] 必须同时存在或同时不存在！");
		}
		/**
		 * 补正证号 选填，文本，50个字符，为空时【补正日期】必为空；填写时【补正日期】必填写<br>
		 * 补正日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【补正证号】必为空；填写时【补正证号】必填写
		 * 
		 */
		if (UtilValidate.isEmpty(params.get("correctionSN")) && UtilValidate.isNotEmpty(params.get("correctionDate"))
				|| UtilValidate.isEmpty(params.get("correctionDate"))
						&& UtilValidate.isNotEmpty(params.get("correctionSN"))) {
			reM = ServiceUtil.returnError("E", "[补正证号]&[补正日期] 必须同时存在或同时不存在！");
		}

		// 备注 选填，需判断本许可证号，若含有“补正”，这里必填原许可证号，格式如“2004规（朝）建字0190号补正”
		if ((prjSN + "").indexOf("补正") != -1) {
			if (UtilValidate.isEmpty(params.get("remark"))) {
				reM = ServiceUtil.returnError("E", "本许可证号，含有“补正”，[备注]必填原许可证号！");
			} else if ((params.get("remark") + "").indexOf("号补正") == -1) {
				reM = ServiceUtil.returnError("E", "本许可证号，含有“补正”，[备注]必填原许可证号，格式如“2004规（朝）建字0190号补正”");
			}
		}

		return reM;
	}

	/**
	 * 项目属性输入检查
	 * 
	 * @param params
	 * @return
	 */
	public static String xmsxC(Map<String, Object> params,List<String> outCheck) {

		String reM = "";

		if (UtilValidate.isEmpty(params.get("prjSN"))) {
			reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
		}
		if (UtilValidate.isEmpty(params.get("serialNumber"))) {
			reM = ServiceUtil.returnError("E", "建筑序号必输！");
		} else if (!Check.check(Check.zzs, params.get("serialNumber"))) {
			reM = ServiceUtil.returnError("E", "建筑序号 必须为 正整数！");
		}

		if (UtilValidate.isEmpty(params.get("prjNature"))) {
			reM = ServiceUtil.returnError("E", "项目性质 必输！");
		}
		if(!outCheck.contains("prjAttr")) {
			if (UtilValidate.isEmpty(params.get("prjAttr"))) {
				reM = ServiceUtil.returnError("E", "规划项目/人防 必输！");
			} else if ("规划项目性质：|人防工程情况：".indexOf(params.get("prjAttr") + "") == -1) {
				reM = ServiceUtil.returnError("E", "规划项目/人防 必须为[规划项目性质：、人防工程情况：]！");
			}
		}

		if (UtilValidate.isNotEmpty(params.get("aboveGroundLev"))
				&& !Check.check(Check.zzs, params.get("aboveGroundLev"))) {
			reM = ServiceUtil.returnError("E", "地上层数 必须为正整数！");
		}
		if (UtilValidate.isNotEmpty(params.get("underGroundLev"))
				&& !Check.check(Check.zzs, params.get("underGroundLev"))) {
			reM = ServiceUtil.returnError("E", "地下层数 必须为正整数！");
		}
		if (UtilValidate.isNotEmpty(params.get("aboveGroundHet"))
				&& !Check.check(Check.zfs, params.get("aboveGroundHet"))) {
			reM = ServiceUtil.returnError("E", "地上高度 必须为最多两位小数的浮点数！");
		}
		if (UtilValidate.isNotEmpty(params.get("underGroundHet"))
				&& !Check.check(Check.ffs, params.get("underGroundHet"))) {
			reM = ServiceUtil.returnError("E", "地下高度 必须为最多两位小数的浮点数！");
		}
		if (UtilValidate.isNotEmpty(params.get("buildings")) && !Check.check(Check.zzs, params.get("buildings"))) {
			reM = ServiceUtil.returnError("E", "栋数 必须为正整数！");
		}
		if (UtilValidate.isNotEmpty(params.get("housingStockNum"))
				&& !Check.check(Check.zzs, params.get("housingStockNum"))) {
			reM = ServiceUtil.returnError("E", "住房套数 必须为正整数！");
		}

		if (UtilValidate.isNotEmpty(params.get("checkDocDate"))) {
			if (!Check.check(Check.date1, params.get("checkDocDate") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [验线日期],格式YYYY/MM/DD！");
			} else if (DateUtils.nowDateString(DateUtils.FORMAT6).compareTo(params.get("checkDocDate") + "") < 0) {
				reM = ServiceUtil.returnError("E", "验线日期必须小于当前系统日期!");
			}
		}
		if (UtilValidate.isNotEmpty(params.get("checkDate"))) {
			if (!Check.check(Check.date1, params.get("checkDate") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [验收日期],格式YYYY/MM/DD！");
			} else if (DateUtils.nowDateString(DateUtils.FORMAT6).compareTo(params.get("checkDate") + "") < 0) {
				reM = ServiceUtil.returnError("E", "验收日期 必须小于当前系统日期!");
			}
		}
		if (UtilValidate.isNotEmpty(params.get("cancelDate"))) {
			if (!Check.check(Check.date1, params.get("cancelDate") + "")) {
				reM = ServiceUtil.returnError("E", "请输入有效 [撤（注）销日期],格式YYYY/MM/DD！");
			} else if (DateUtils.nowDateString(DateUtils.FORMAT6).compareTo(params.get("cancelDate") + "") < 0) {
				reM = ServiceUtil.returnError("E", "撤（注）销日期 必须小于当前系统日期!");
			}
		}
		/**
		 * 验线文号 选填，文本，50个字符，为空时【验线日期】必为空；填写时【验线日期】必填写<br>
		 * 验线日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【验线文号】必为空；填写时【验线文号】必填写
		 * 
		 */
		if (UtilValidate.isEmpty(params.get("checkDocSN")) && UtilValidate.isNotEmpty(params.get("checkDocDate"))
				|| UtilValidate.isEmpty(params.get("checkDocDate"))
						&& UtilValidate.isNotEmpty(params.get("checkDocSN"))) {
			reM = ServiceUtil.returnError("E", "[验线文号]&[验线日期] 必须同时存在或同时不存在！");
		}
		/**
		 * 验收文号 选填，文本，50个字符，为空时【验收日期】必为空；填写时【验收日期】必填写<br>
		 * 验收日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【验收文号】必为空；填写时【验收文号】必填写
		 * 
		 */
		if (UtilValidate.isEmpty(params.get("checkSN")) && UtilValidate.isNotEmpty(params.get("checkDate"))
				|| UtilValidate.isEmpty(params.get("checkDate")) && UtilValidate.isNotEmpty(params.get("checkSN"))) {
			reM = ServiceUtil.returnError("E", "[验收文号]&[验收日期] 必须同时存在或同时不存在！");
		}
		// 建设项目的验收日期不应早于临建项目的验收日期

		/**
		 * 撤（注）销证号 选填，文本，50个字符，为空时【撤（注）销日期】必为空；填写时【撤（注）销日期】必填写<br>
		 * 撤（注）销日期 选填，文本，格式必须为（年/月/日），小于当前录入时间，为空时【撤（注）销证号】必为空；填写时【撤（注）销证号】必填写
		 * 
		 */

		if (UtilValidate.isEmpty(params.get("cancelSN")) && UtilValidate.isNotEmpty(params.get("cancelDate"))
				|| UtilValidate.isEmpty(params.get("cancelDate")) && UtilValidate.isNotEmpty(params.get("cancelSN"))) {
			reM = ServiceUtil.returnError("E", "[撤（注）销证号]&[撤（注）销日期] 必须同时存在或同时不存在！");
		}

		return reM;
	}

	/**
	 * 检查项目明细输入项
	 * 
	 * @param params
	 * @return
	 */
	public static String xmmxC(Map<String, Object> params) {

		String reM = "";

		if (UtilValidate.isEmpty(params.get("prjSN"))) {
			reM = ServiceUtil.returnError("E", "项目许可证不可为空！");
		}

		if (UtilValidate.isEmpty(params.get("serialNumber"))) {
			reM = ServiceUtil.returnError("E", "建筑序号 必输！");
		}
		if (UtilValidate.isEmpty(params.get("serialFunct"))) {
			reM = ServiceUtil.returnError("E", "建筑功能 必输！");
		}

		if (UtilValidate.isNotEmpty(params.get("aboveGroundArea"))
				&& !Check.check(Check.zfs, params.get("aboveGroundArea"))) {
			reM = ServiceUtil.returnError("E", "地上建筑面积 必须为最多两位小数的浮点数！");
		}
		if (UtilValidate.isNotEmpty(params.get("underGroundArea"))
				&& !Check.check(Check.zfs, params.get("underGroundArea"))) {
			reM = ServiceUtil.returnError("E", "地下建筑面积 必须为最多两位小数的浮点数！");
		}
		if (UtilValidate.isNotEmpty(params.get("blendArea")) && !Check.check(Check.zfs, params.get("blendArea"))) {
			reM = ServiceUtil.returnError("E", "混合建筑面积 必须为最多两位小数的浮点数！");
		}
		if (UtilValidate.isNotEmpty(params.get("aboveGroundLen"))
				&& !Check.check(Check.zfs, params.get("aboveGroundLen"))) {
			reM = ServiceUtil.returnError("E", "地上建筑长度 必须为最多两位小数的浮点数！");
		}

		Object codeO = params.get("prjClasfiCode");
		if (UtilValidate.isEmpty(codeO)) {
			reM = ServiceUtil.returnError("E", "分级信息 必输！");
		} else if ((codeO + "").length() != 10) {// 0101R14101
			reM = ServiceUtil.returnError("E", "五级分类必须填写完全！");
		}

		return reM;
	}

}
