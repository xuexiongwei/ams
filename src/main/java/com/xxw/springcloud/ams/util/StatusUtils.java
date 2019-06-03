package com.xxw.springcloud.ams.util;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.xxw.springcloud.ams.mapper.file.SuperMapper;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmsx;

public class StatusUtils {

	/**
	 * 更新项目状态
	 * 
	 * 优先判断是否存在 超期、延期、补正，如不存在，则判断其他项
	 * 
	 * @param superMapper
	 * @throws ParseException
	 */
	public static void updatePrjStatus(SuperMapper superMapper, String prjSN) throws ParseException {
		String prjStatus = "";// 项目状态
		List<Xmsx> sxL = superMapper.queryXmsxByPrjSN(prjSN);
		if (UtilValidate.isNotEmpty(sxL)) {
			int count = sxL.size();
			int ys = 0;// 验收个数
			int czx = 0;// 撤（注）销 个数
			for (Xmsx sxt : sxL) {
				String checkSN = sxt.getCheckSN();// 验收文号
				String cancelSN = sxt.getCancelSN();// 撤（注）销证号
				if (UtilValidate.isNotEmpty(checkSN)) {
					ys++;
				} else if (UtilValidate.isNotEmpty(cancelSN)) {
					czx++;
				}
			}
			if (ys + czx > count) {
				prjStatus = "基础数据误!";
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
			superMapper.updatePrjStatusByPrjSN(UtilMisc.toMap("prjStatus", (Object) prjStatus, "prjSN", prjSN));
		}
	}

	/**
	 * 更新项目标识
	 * 
	 * 超期 需要根据【发件日期】和【有效期】【延长期】进行计算 <br>
	 * 延期 存在【延期文号】 <br>
	 * 补正 存在【补正证号】
	 * 
	 * 
	 * @param superMapper
	 * @throws ParseException
	 */
	public static void updatePrjMark(SuperMapper superMapper, String prjSN) throws ParseException {
		String prjMark = "正常";// 项目标识
		// 查询项目基本信息获取有效期字段
		Xmjbxx jb = superMapper.queryXmjbxxByPrjSN(prjSN);
		if (UtilValidate.isNotEmpty(jb)) {
			String noticeTime = jb.getNoticeTime();// 发件日期
			String effectiveTime = jb.getEffectiveTime();// 有效期 X月
			String delayCountDay = jb.getDelayCountDay();// 延长期 X月
			String delaySN = jb.getDelaySN();// 延期文号
			String correctionSN = jb.getCorrectionSN();// 补正号

			boolean checkOther = false;// 是否检查其他项
			if (UtilValidate.isNotEmpty(noticeTime) && UtilValidate.isNotEmpty(effectiveTime)) {
				String nowDate = DateUtils.nowDateString(DateUtils.FORMAT6);
				Date youxiao = DateUtils.parse(noticeTime, DateUtils.FORMAT6);
				String youxiao_ = DateUtils.monthJiaJian(youxiao, Integer.parseInt(effectiveTime));
				if (nowDate.compareTo(youxiao_) > 0) {// 超期
					if (UtilValidate.isNotEmpty(delayCountDay)) {// 判断是否延期
						Date yanqi = DateUtils.parse(youxiao_, DateUtils.FORMAT6);
						String yanqi_ = DateUtils.monthJiaJian(yanqi, Integer.parseInt(delayCountDay));
						if (nowDate.compareTo(yanqi_) > 0) {// 延期后还超期
							prjMark = "超期";
						} else {
							checkOther = true;
						}
					} else {
						prjMark = "超期";
					}
				} else {
					checkOther = true;
				}
			} else {
				checkOther = true;
			}

			if (checkOther) {
				if (UtilValidate.isNotEmpty(delaySN)) {
					prjMark = "延期";
				} else if (UtilValidate.isNotEmpty(correctionSN)) {
					prjMark = "补正";
				}
			}
		} else {
			prjMark = "未查到基本信息,未断!";
		}

		if (UtilValidate.isNotEmpty(prjMark)) {
			superMapper.updatePrjMarkByPrjSN(UtilMisc.toMap("prjMark", (Object) prjMark, "prjSN", prjSN));
		}

	}

	/**
	 * 更新工程状态
	 */
	public static void updateBuldStatus(SuperMapper superMapper, String prjSN, Long serialNumber) {

		// 判断工程状态,并更新
		List<Xmsx> sxl = superMapper.queryXmsxByPrjSNAndSerialNumber2(
				UtilMisc.toMap("prjSN", (Object) prjSN, "serialNumber", serialNumber));
		String buldStatus = "";// 工程状态
		String checkSN = "";
		String cancelSN = "";
		for (Xmsx xmsx : sxl) {// 查找到不为空的行
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
		superMapper.updateBuldStatusByPrjSNAndSerialNumber(
				UtilMisc.toMap("prjSN", (Object) prjSN, "serialNumber", serialNumber, "buldStatus", buldStatus));

	}

}
