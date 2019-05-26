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
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.FastMap;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StringUtils;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class View3Controller {

	public static Logger logger = LoggerFactory.getLogger(View3Controller.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 第三个报表 <br>
	 * 查询条件 项目许可证号
	 */
	@RequestMapping("/api/view/bb003")
	public String bb002(@RequestBody String inputjson) {

		logger.debug("exc:queryUserOperByDate params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "查询异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSNO = params.get("prjSN");
			if (UtilValidate.isEmpty(prjSNO)) {
				reM = ServiceUtil.returnError("E", "报表003必须输入项目许可证号进行统计查询！");
			} else {
				List<Xmjbxx> xmjbxxL = superMapper.findXmjbxxByAttr(params);
				Map<String, Map<String, Object>> viewM = FastMap.newInstance();
				for (Xmjbxx jb : xmjbxxL) {
					String prjSN = jb.getPrjSN();// 许可证号

					// 查找去重项目明细，返回serialNumber，substring(prjClasfiCode,1,4)
					List<Xmmx> mxls = superMapper.queryXmmxByPrjSN(prjSN);
					// 分析一级分类
					for (Xmmx mxTem : mxls) {
						String cfc = mxTem.getPrjClasfiCode();
						if (UtilValidate.isNotEmpty(cfc)) {
							Map<String, Object> f01m = null;
							// 一级分类
							String fl01 = cfc.substring(0, 2);
							ClassifiDic dic = superMapper
									.queryDicByCode2(UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", fl01));
							fl01 = dic.getName();
							if (!viewM.containsKey(fl01)) {
								f01m = FastMap.newInstance();
								viewM.put(fl01, f01m);
							} else {
								f01m = viewM.get(fl01);
							}

							// 二级分类
							String fl02 = cfc.substring(0, 4);
							dic = superMapper
									.queryDicByCode2(UtilMisc.toMap("type", (Object) DicEnum.FJ, "code", fl02));
							fl02 = dic.getName();
							if (UtilValidate.isNotEmpty(fl02)) {
								Map<String, Map<String, Object>> f02m = null;
								if (!f01m.containsKey(fl02)) {
									f02m = FastMap.newInstance();
									f01m.put(fl02, f02m);
								} else {
									f02m = (Map<String, Map<String, Object>>) f01m.get(fl02);
								}
								Long serialNumber = mxTem.getSerialNumber();

								Map<String, Object> index = null;
								if (f02m.containsKey(serialNumber + "")) {
									index = f02m.get(serialNumber + "");
								} else {
									index = FastMap.newInstance();
									f02m.put(serialNumber + "", index);
								}

								// 第一行统计信息
								Map<String, Object> tongj = null;
								if (index.containsKey("tongj")) {
									tongj = (Map<String, Object>) index.get("tongj");
								} else {
									tongj = FastMap.newInstance();
									// 初始化
									tongj.put("sumArea", 0.0d);// 总建筑面积（平方米）
									tongj.put("aboveGroundSumArea", 0.0d);// 总建筑面积（平方米）地上
									tongj.put("underGroundSumArea", 0.0d);// 总建筑面积（平方米）地下
									tongj.put("sumLen", 0.0d);// 建筑长度（米）

									tongj.put("aboveGroundLev", 0l);// 地上层数
									tongj.put("underGroundLev", 0l);// 地下层数
									tongj.put("aboveGroundHet", 0d);// 地上高度
									tongj.put("underGroundHet", 0d);// 地下高度
									tongj.put("buildings", 0l);// 栋数
									tongj.put("housingStockNum", 0l);// 住房套数

									tongj.put("prjNature", "");// 项目性质
									tongj.put("peacetimeUses", "");// 平时用途
									tongj.put("strucType", "");// 结构类型
									tongj.put("checkDocSN", "");// 验线文号
									tongj.put("checkDocDate", "");// 验线日期
									tongj.put("checkSN", "");// 验收文号
									tongj.put("checkDate", "");// 验收日期
									tongj.put("delaySN", "");// 延期文号
									tongj.put("delayCountDay", "");// 延长期
									tongj.put("cancelSN", "");// 撤（注）销证号
									tongj.put("cancelDate", "");// 撤（注）销日期
									tongj.put("correctionSN", "");// 补正证号
									tongj.put("correctionDate", "");// 补正日期
									tongj.put("imgJudgeRes", "");// 影像判读结果
									tongj.put("exproprInfo", "");// 代征用地情况
									tongj.put("remark", "");// 备注

									index.put("tongj", tongj);
								}

								// 统计面积
								List<Xmmx> mxl = superMapper.findXmmxByAttr(
										UtilMisc.toMap("prjSN", (Object) prjSN, "serialNumber", serialNumber));
								for (Xmmx mx : mxl) {

									cfc = mx.getPrjClasfiCode();
									Double aga = mx.getAboveGroundArea();// 总建筑面积（平方米）地上
									Double uga = mx.getUnderGroundArea();// 总建筑面积（平方米）地下
									Double agl = mx.getAboveGroundLen();// 建筑长度（米）
									if (null == aga)
										aga = 0.0d;

									if (null == uga)
										uga = 0.0d;

									if (null == agl)
										agl = 0.0d;

									if (cfc.length() == 10) {
										String fl04 = cfc.substring(5, 8);
										String fl05 = cfc.substring(8, 10);
										if (!"141".equals(fl04) && "01|02".indexOf(fl05) != -1) {// 总建筑面积不计算人防工程
											// 总建筑面积（平方米）
											Double sumArea = (Double) tongj.get("sumArea");
											tongj.put("sumArea", StringUtils.sswr(sumArea + aga + uga));
										}
									} else {
										// 总建筑面积（平方米）
										Double sumArea = (Double) tongj.get("sumArea");
										tongj.put("sumArea", StringUtils.sswr(sumArea + aga + uga));
									}

									// 总建筑面积（平方米）地上
									Double aboveGroundSumArea = (Double) tongj.get("aboveGroundSumArea");
									tongj.put("aboveGroundSumArea", StringUtils.sswr(aboveGroundSumArea + aga));
									// 总建筑面积（平方米）地下
									Double underGroundSumArea = (Double) tongj.get("underGroundSumArea");
									tongj.put("underGroundSumArea", StringUtils.sswr(underGroundSumArea + uga));
									// 建筑长度（米）
									Double sumLen = (Double) tongj.get("sumLen");
									tongj.put("sumLen", StringUtils.sswr(sumLen + agl));
								}
								// 查找项目属性
								List<Xmsx> sxl = superMapper.findXmsxByAttr(
										UtilMisc.toMap("prjSN", (Object) prjSN, "serialNumber", serialNumber));
								// 统计属性中高度等信息
								for (Xmsx xmsx : sxl) {

									String buldStatus = xmsx.getBuldStatus();
									if (UtilValidate.isNotEmpty(buldStatus)) {
										tongj.put("buldStatus", buldStatus);// 工程状态
									}
									// 计算地上层数
									Long aboveGroundLev = (Long) tongj.get("aboveGroundLev");
									Long abgl = xmsx.getAboveGroundLev();
									if (UtilValidate.isNotEmpty(abgl)) {
										tongj.put("aboveGroundLev", aboveGroundLev + abgl);
									}
									// 计算地下层数
									Long underGroundLev = (Long) tongj.get("underGroundLev");
									Long ugl = xmsx.getUnderGroundLev();
									if (UtilValidate.isNotEmpty(ugl)) {
										tongj.put("underGroundLev", underGroundLev + ugl);
									}
									// 地上高度
									Double aboveGroundHet = (Double) tongj.get("aboveGroundHet");
									Double agh = xmsx.getAboveGroundHet();
									if (UtilValidate.isNotEmpty(agh)) {
										tongj.put("aboveGroundHet", aboveGroundHet + agh);
									}
									// 地下高度
									Double underGroundHet = (Double) tongj.get("underGroundHet");
									Double ugh = xmsx.getUnderGroundHet();
									if (UtilValidate.isNotEmpty(ugh)) {
										tongj.put("underGroundHet", underGroundHet + ugh);
									}
									// 栋数
									Long buildings = (Long) tongj.get("buildings");
									Long bds = xmsx.getBuildings();
									if (UtilValidate.isNotEmpty(bds)) {
										tongj.put("buildings", buildings + bds);
									}
									// 住房套数
									Long housingStockNum = (Long) tongj.get("housingStockNum");
									Long hsn = xmsx.getHousingStockNum();
									if (UtilValidate.isNotEmpty(hsn)) {
										tongj.put("housingStockNum", housingStockNum + hsn);
									}

									String prjNature = xmsx.getPrjNature();// 项目性质
									if (UtilValidate.isNotEmpty(prjNature)) {
										tongj.put("prjNature", prjNature);
									}
									String peacetimeUses = xmsx.getPeacetimeUses();// 平时用途
									if (UtilValidate.isNotEmpty(peacetimeUses)) {
										tongj.put("peacetimeUses", peacetimeUses);
									}
									String strucType = xmsx.getStrucType();// 结构类型
									if (UtilValidate.isNotEmpty(strucType)) {
										tongj.put("strucType", strucType);
									}
									String checkDocSN = xmsx.getCheckDocSN();// 验线文号
									if (UtilValidate.isNotEmpty(checkDocSN)) {
										tongj.put("checkDocSN", checkDocSN);
									}
									String checkDocDate = xmsx.getCheckDocDate();// 验线文号
									if (UtilValidate.isNotEmpty(checkDocDate)) {
										tongj.put("checkDocDate", checkDocDate);
									}
									String checkSN = xmsx.getCheckSN();// 验收文号
									if (UtilValidate.isNotEmpty(checkSN)) {
										tongj.put("checkSN", checkSN);
									}
									String checkDate = xmsx.getCheckDate();// 验收日期
									if (UtilValidate.isNotEmpty(checkDate)) {
										tongj.put("checkDate", checkDate);
									}
									String delaySN = xmsx.getDelaySN();// 延期文号
									if (UtilValidate.isNotEmpty(delaySN)) {
										tongj.put("delaySN", delaySN);
									}
									String delayCountDay = xmsx.getDelayCountDay();// 延长期
									if (UtilValidate.isNotEmpty(delayCountDay)) {
										tongj.put("delayCountDay", delayCountDay);
									}
									String cancelSN = xmsx.getCancelSN();// 撤（注）销证号
									if (UtilValidate.isNotEmpty(cancelSN)) {
										tongj.put("cancelSN", cancelSN);
									}
									String cancelDate = xmsx.getCancelDate();// 撤（注）销日期
									if (UtilValidate.isNotEmpty(cancelDate)) {
										tongj.put("cancelDate", cancelDate);
									}
									String correctionSN = xmsx.getCorrectionSN();// 补正证号
									if (UtilValidate.isNotEmpty(correctionSN)) {
										tongj.put("correctionSN", correctionSN);
									}
									String correctionDate = xmsx.getCorrectionDate();// 补正日期
									if (UtilValidate.isNotEmpty(correctionDate)) {
										tongj.put("correctionDate", correctionDate);
									}
									String imgJudgeRes = xmsx.getImgJudgeRes();// 影像判读结果
									if (UtilValidate.isNotEmpty(imgJudgeRes)) {
										tongj.put("imgJudgeRes", imgJudgeRes);
									}
									String exproprInfo = xmsx.getExproprInfo();// 代征用地情况
									if (UtilValidate.isNotEmpty(exproprInfo)) {
										tongj.put("exproprInfo", exproprInfo);
									}
									String remark = xmsx.getRemark();// 备注
									if (UtilValidate.isNotEmpty(remark)) {
										tongj.put("remark", remark);
									}
								}

								// 分析附属属性
								sxl = superMapper.queryXmsxByPrjSNAndSerialNumber(
										UtilMisc.toMap("prjSN", (Object) prjSN, "serialNumber", serialNumber));
								for (Xmsx xmsx : sxl) {
									String prjAttr = xmsx.getPrjAttr();// 规划项目/人防
									if (UtilValidate.isNotEmpty(prjAttr)) {// 如果此字段为空，则无需显示明细信息
										// 第二行表格信息
										mxl = superMapper.findXmmxByAttr(
												UtilMisc.toMap("prjSN", (Object) prjSN, "serialNumber", serialNumber));
										for (Xmmx mx : mxl) {
											// 第五级分类
											cfc = mx.getPrjClasfiCode();
											if (cfc.length() == 10) {
												String fl04 = cfc.substring(5, 8);
												String fl05 = cfc.substring(8, 10);
												List<Map<String, Object>> items02 = null;
												if ("人防工程情况：".equals(prjAttr)) {
													// [规划项目性质包括：]或 [人防工程情况：]等
													Map<String, Object> its = FastMap.newInstance();
													// 人防工程
													if ("141".equals(fl04) && "01|02".indexOf(fl05) != -1) {
														its.put("serialFunct", mx.getSerialFunct());// 建筑功能
														// 建筑面积（平方米）
														Double f1 = mx.getAboveGroundArea();
														its.put("aboveGroundArea", StringUtils.sswr(f1));// 地上建筑面积（平方米）
														Double f2 = mx.getUnderGroundArea();
														its.put("underGroundArea", StringUtils.sswr(f2));// 地下建筑面积（平方米）
														Double f3 = mx.getBlendArea();
														its.put("blendArea", StringUtils.sswr(f3));// 混合建筑面积（平方米）
														Double f4 = mx.getAboveGroundLen();
														its.put("aboveGroundLen", StringUtils.sswr(f4));// 地上建筑长度（米）

														dic = superMapper.queryDicByCode2(UtilMisc.toMap("type",
																(Object) DicEnum.FJ, "code", cfc.substring(0, 8)));
														String c3 = dic.getName();
														dic = superMapper.queryDicByCode2(UtilMisc.toMap("type",
																(Object) DicEnum.FJ, "code", cfc));
														String c4 = dic.getName();

														its.put("buldType", c3 + "/" + c4);// 建筑类型

														Map<String, Object> xxl = null;
														if (index.containsKey("detail")) {
															xxl = (Map<String, Object>) index.get("detail");
														} else {
															xxl = FastMap.newInstance();
															index.put("detail", xxl);
														}
														if (xxl.containsKey(prjAttr)) {
															items02 = (List<Map<String, Object>>) xxl.get(prjAttr);
														} else {
															items02 = FastList.newInstance();
															xxl.put(prjAttr, items02);
														}
														items02.add(its);
													}

												} else if ("规划项目性质：".equals(prjAttr)) {
													Map<String, Object> its = FastMap.newInstance();
													// 非人防工程
													if (!"141".equals(fl04)) {
														its.put("serialFunct", mx.getSerialFunct());// 建筑功能
														// 建筑面积（平方米）
														Double f1 = mx.getAboveGroundArea();
														its.put("aboveGroundArea", StringUtils.sswr(f1));// 地上建筑面积（平方米）
														Double f2 = mx.getUnderGroundArea();
														its.put("underGroundArea", StringUtils.sswr(f2));// 地下建筑面积（平方米）
														Double f3 = mx.getBlendArea();
														its.put("blendArea", StringUtils.sswr(f3));// 混合建筑面积（平方米）
														Double f4 = mx.getAboveGroundLen();
														its.put("aboveGroundLen", StringUtils.sswr(f4));// 地上建筑长度（米）

														dic = superMapper.queryDicByCode2(UtilMisc.toMap("type",
																(Object) DicEnum.FJ, "code", cfc.substring(0, 8)));
														String c3 = dic.getName();
														dic = superMapper.queryDicByCode2(UtilMisc.toMap("type",
																(Object) DicEnum.FJ, "code", cfc));
														String c4 = dic.getName();

														its.put("buldType", c3 + "/" + c4);// 建筑类型

														Map<String, Object> xxl = null;
														if (index.containsKey("detail")) {
															xxl = (Map<String, Object>) index.get("detail");
														} else {
															xxl = FastMap.newInstance();
															index.put("detail", xxl);
														}

														if (xxl.containsKey(prjAttr)) {
															items02 = (List<Map<String, Object>>) xxl.get(prjAttr);
														} else {
															items02 = FastList.newInstance();
															xxl.put(prjAttr, items02);
														}

														items02.add(its);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				reM = ServiceUtil.returnSuccess(viewM, "viewList", header);
			}

		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.debug("exc:queryUserOperByDate return:" + reM);

		return reM;
	}
}