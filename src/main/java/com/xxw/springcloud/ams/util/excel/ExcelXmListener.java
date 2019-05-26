package com.xxw.springcloud.ams.util.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xxw.springcloud.ams.enums.DicEnum;
import com.xxw.springcloud.ams.mapper.file.SuperMapper;
import com.xxw.springcloud.ams.model.ClassifiDic;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.BeanUtils;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.StringUtils;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;

/**
 * 解析excel 1.判断重复项 2.检查字段是否匹配一致 3.提示数据有错文档及其行数
 * 
 * @author yangwei
 *
 */
public class ExcelXmListener extends AnalysisEventListener<Object> {

	private SuperMapper superMapper = null;
	public static Logger logger = LoggerFactory.getLogger(ExcelXmListener.class);

	public ExcelXmListener(SuperMapper f) {
		superMapper = f;
	}

	// 自定义用于暂时存储data。
	List<String> dataxmsxDel = FastList.newInstance();
	List<String> dataxmmxDel = FastList.newInstance();

	// 可以通过实例获取该值
	public void invoke(Object object, AnalysisContext context) {

		int no = context.getCurrentSheet().getSheetNo();
		int row = context.getCurrentRowNum();
		try {
			List<Object> items = (List<Object>) object;

			logger.info("解析excel 页签序号：" + no + ",行数：" + row);

			if (row == 0) {
				logger.info("解析excel 表头信息为：" + items);
				return;
			}

			if (no == 1) {// 项目基本信息
				items.remove(0);

				String prjSN = items.get(0) + "";

				Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN);
				if (null == jbxx)
					jbxx = new Xmjbxx();

				BeanUtils.setProperty(
						jbxx, new String[] { "prjSN", "prjUnit", "prjAdr", "prjName", "prjType", "contacts",
								"contactInf", "prjTemSN", "specialNotifi", "noticeTime", "effectiveTime", "remark" },
						items);
				String prjName = jbxx.getPrjName();
				if (UtilValidate.isNotEmpty(prjName)) {
					// 改扩建、改建、改造、翻建
					if (prjName.indexOf("改扩建") != -1 || prjName.indexOf("改建") != -1 || prjName.indexOf("改造") != -1
							|| prjName.indexOf("翻建") != -1) {
						jbxx.setPrjType("改扩建");
					} else {
						jbxx.setPrjType("新建");
					}
					// 许可证类型
				} else {
					jbxx.setPrjType("");
				}
				// 许可证类型
				if (UtilValidate.isNotEmpty(prjSN)) {
					// 乡村建设项目
					if (prjSN.indexOf("乡") != -1) {
						jbxx.setPrjSNType("乡村建设项目");
					} else if (prjSN.indexOf("临") != -1) {
						jbxx.setPrjSNType("临时建设项目");
					} else if (prjSN.indexOf("补正") != -1) {
						jbxx.setPrjSNType("补正项目");
					} else {
						jbxx.setPrjSNType("城镇建设项目");
					}
				} else {
					jbxx.setPrjSNType("");
				}
				if (null == jbxx.getId()) {
					superMapper.saveXmjbxx(jbxx);
				} else
					superMapper.updateXmjbxx(jbxx);
			}

			if (no == 2) {// 项目属性
				Xmsx sx = new Xmsx();
				BeanUtils.setProperty(sx,
						new String[] { "prjSN", "serialNumber", "prjNature", "prjAttr", "peacetimeUses",
								"aboveGroundLev", "underGroundLev", "aboveGroundHet", "underGroundHet", "buildings",
								"housingStockNum", "strucType", "checkDocSN", "checkDocDate", "checkSN", "checkDate",
								"delaySN", "delayCountDay", "cancelSN", "cancelDate", "correctionSN", "correctionDate",
								"imgJudgeRes", "exproprInfo", "remark" },
						items);
				if (null != sx.getPrjSN()) {
					// 删除所有再更新
					String prjsn = sx.getPrjSN();
					if (!dataxmsxDel.contains(prjsn)) {
						superMapper.delXmsxByPrjSN(sx.getPrjSN());
						dataxmsxDel.add(prjsn);
					}
					superMapper.saveXmsx(sx);

					// 判断工程状态,并更新
					List<Xmsx> sxl = superMapper.queryXmsxByPrjSNAndSerialNumber2(
							UtilMisc.toMap("prjSN", (Object) sx.getPrjSN(), "serialNumber", sx.getSerialNumber()));
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
					superMapper.updateBuldStatusByPrjSNAndSerialNumber(UtilMisc.toMap("prjSN", (Object) sx.getPrjSN(),
							"serialNumber", sx.getSerialNumber(), "buldStatus", buldStatus));

					// 更新项目状态
					List<Xmsx> sxL = superMapper.queryXmsxByPrjSN(sx.getPrjSN());
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
								UtilMisc.toMap("prjStatus", (Object) prjStatus, "prjSN", sx.getPrjSN()));
					}
				}
			}

			if (no == 3) {// 项目明细
				Xmmx mx = new Xmmx();
				BeanUtils.setProperty(mx, new String[] { "prjSN", "serialNumber", "serialFunct", "aboveGroundArea",
						"underGroundArea", "blendArea", "aboveGroundLen" }, items);
				// 还差分级信息-------------------------------------------------------------------------------------------------
				if (null != mx.getPrjSN()) {

					List<Object> levs = items.subList(7, 12);// [居住类项目, 配套公共服务设施, 配套公共服务设施, null, null]

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("type", DicEnum.FJ);

					String code = "";
					String parentID = "";
					ClassifiDic dic = null;

					for (Object obj : levs) {
						params.put("parentID", parentID);
						params.put("name", obj);
						dic = superMapper.queryDicByName(params);
						if (null != dic) {
							parentID = dic.getId();
							code = dic.getCode();
						}
					}
					mx.setPrjClasfiCode(code);

					mx.setPrjClasfiName1(StringUtils.getStr(levs.get(0)));
					mx.setPrjClasfiName2(StringUtils.getStr(levs.get(1)));
					mx.setPrjClasfiName3(StringUtils.getStr(levs.get(2)));
					mx.setPrjClasfiName4(StringUtils.getStr(levs.get(3)));
					mx.setPrjClasfiName5(StringUtils.getStr(levs.get(4)));

					String prjsn = mx.getPrjSN();
					if (!dataxmmxDel.contains(prjsn)) {
						superMapper.delXmmxByPrjSN(mx.getPrjSN());
						dataxmmxDel.add(prjsn);
					}
					superMapper.saveXmmx(mx);
				}
			}
		} catch (Exception e) {
			logger.error("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！", e);
			throw new RuntimeException("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！");
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		dataxmsxDel.clear();
		dataxmmxDel.clear();
	}
}
