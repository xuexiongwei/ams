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
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.BeanUtils;
import com.xxw.springcloud.ams.util.CheckInput;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StatusUtils;
import com.xxw.springcloud.ams.util.StringUtils;
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

	List<Map<String, Object>> jbxxV = FastList.newInstance();
	List<Map<String, Object>> xmsxV = FastList.newInstance();
	List<Map<String, Object>> xmmxV = FastList.newInstance();

	StringBuffer err = new StringBuffer();

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
				Object prjSN = items.get(0);
				if (UtilValidate.isNotEmpty(prjSN)) {

					Map<String, Object> params = BeanUtils.setProperty(Xmjbxx.class,
							new String[] { "prjSN", "prjUnit", "prjAdr", "prjName", "prjType", "contacts", "contactInf",
									"prjTemSN", "specialNotifi", "prjSNType", "noticeTime", "effectiveTime", "delaySN",
									"delayCountDay", "correctionSN", "correctionDate", "remark" },
							items);

					Object prjName = params.get("prjName");
					if (UtilValidate.isNotEmpty(prjName)) {
						// 改扩建、改建、改造、翻建
						if ((prjName + "").indexOf("改扩建") != -1 || (prjName + "").indexOf("改建") != -1
								|| (prjName + "").indexOf("改造") != -1 || (prjName + "").indexOf("翻建") != -1) {
							params.put("prjType", "改扩建");
						} else {
							params.put("prjType", "新建");
						}
						// 许可证类型
					} else {
						params.put("prjType", "新建");
					}
					// 项目年份
					String year = StringUtils.getYear(params.get("prjSN") + "");
					params.put("prjYear", year);

					String msg = CheckInput.jbxxC(params);
					if (UtilValidate.isEmpty(msg)) {
						jbxxV.add(params);
					} else {
						Header header = ServiceUtil.getContextHeader(msg);
						logger.error("错误信息为：" + header.getRspReturnMsg());
						throw new RuntimeException("错误：" + header.getRspReturnMsg());
					}
				}
			}

			if (no == 2) {// 项目属性

				Object prjSN = items.get(0);
				if (UtilValidate.isNotEmpty(prjSN)) {

					Map<String, Object> params = BeanUtils.setProperty(Xmsx.class,
							new String[] { "prjSN", "serialNumber", "prjNature", "prjAttr", "peacetimeUses",
									"aboveGroundLev", "underGroundLev", "aboveGroundHet", "underGroundHet", "buildings",
									"housingStockNum", "strucType", "checkDocSN", "checkDocDate", "checkSN",
									"checkDate", "cancelSN", "cancelDate", "imgJudgeRes", "exproprInfo", "remark" },
							items);

					String msg = CheckInput.xmsxC(params);

					if (UtilValidate.isEmpty(msg)) {
						xmsxV.add(params);
					} else {
						Header header = ServiceUtil.getContextHeader(msg);
						logger.error("错误：" + header.getRspReturnMsg());
						throw new RuntimeException("错误信息为：" + header.getRspReturnMsg());
					}
				}

			}

			if (no == 3) {// 项目明细
				Object prjSN = items.get(0);
				if (UtilValidate.isNotEmpty(prjSN)) {
					Map<String, Object> paramM = BeanUtils
							.setProperty(
									Xmmx.class, new String[] { "prjSN", "serialNumber", "serialFunct",
											"aboveGroundArea", "underGroundArea", "blendArea", "aboveGroundLen" },
									items);

					// 还差分级信息-------------------------------------------------------------------------------------------------

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
					if (code.length() != 10) {
						logger.error("错误信息为：请检查五级分类是否正确！基础表中未完全匹配该五级分类");
						throw new RuntimeException("错误：请检查五级分类是否正确！基础表中未完全匹配该五级分类");
					} else {
						paramM.put("prjClasfiCode", code);
						paramM.put("prjClasfiName1", levs.get(0));
						paramM.put("prjClasfiName2", levs.get(1));
						paramM.put("prjClasfiName3", levs.get(2));
						paramM.put("prjClasfiName4", levs.get(3));
						paramM.put("prjClasfiName5", levs.get(4));

						String msg = CheckInput.xmmxC(paramM);
						if (UtilValidate.isEmpty(msg)) {
							xmmxV.add(paramM);
						} else {
							Header header = ServiceUtil.getContextHeader(msg);
							logger.error("错误信息为：" + header.getRspReturnMsg());
							throw new RuntimeException("错误：" + header.getRspReturnMsg());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！", e);
			err.append("页签序号：" + no + ",行数：" + (row + 1) + " 异常:" + e.getMessage() + "\n");
			// throw new RuntimeException("页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！" +
			// e.getMessage());
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext arg0) {

		if (err.length() == 0) {
			// 更新或新增基本信息
			int i = 0;
			try {
				if (UtilValidate.isNotEmpty(jbxxV)) {
					for (Map<String, Object> item : jbxxV) {
						i++;
						Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(item.get("prjSN") + "");

						if (UtilValidate.isNotEmpty(jbxx)) {
							superMapper.updateXmjbxx(item);
						} else {
							superMapper.saveXmjbxx(item);
						}
						// 更新项目标识
						StatusUtils.updatePrjMark(superMapper, item.get("prjSN") + "");

					}
				}
				// 更新或新增项目属性
				if (UtilValidate.isNotEmpty(xmsxV)) {
					i = 0;
					for (Map<String, Object> item : xmsxV) {
						i++;
						// 删除所有再更新
						Object prjsn = item.get("prjSN");
						if (!dataxmsxDel.contains(prjsn + "")) {
							superMapper.delXmsxByPrjSN(prjsn + "");
							dataxmsxDel.add(prjsn + "");
						}
						superMapper.saveXmsx(item);

						// 判断工程状态,并更新
						StatusUtils.updateBuldStatus(superMapper, item.get("prjSN") + "",
								Long.parseLong(item.get("serialNumber") + ""));
						// 更新项目状态
						StatusUtils.updatePrjStatus(superMapper, item.get("prjSN") + "");
					}
				}
				// 更新或新增项目明细
				if (UtilValidate.isNotEmpty(xmmxV)) {
					i = 0;
					for (Map<String, Object> item : xmmxV) {
						i++;
						String prjsn = item.get("prjSN") + "";
						if (!dataxmmxDel.contains(prjsn)) {
							superMapper.delXmmxByPrjSN(prjsn);
							dataxmmxDel.add(prjsn);
						}
						superMapper.saveXmmx(item);
					}
				}
			} catch (Exception e) {
				logger.error("页签序号：1,行数：" + i + " 解析异常！", e);
				throw new RuntimeException("页签序号：1,行数：" + i + " 解析异常！" + e.getMessage());
			}
		} else {
			throw new RuntimeException(":" + err.toString());
		}
		err = null;
		jbxxV = null;
		xmsxV = null;
		xmmxV = null;
		dataxmsxDel = null;
		dataxmmxDel = null;
	}

}
