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
				if (UtilValidate.isNotEmpty(prjSN)) {
					Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN);
					if (null == jbxx)
						jbxx = new Xmjbxx();

					BeanUtils.setProperty(jbxx,
							new String[] { "prjSN", "prjUnit", "prjAdr", "prjName", "prjType", "contacts", "contactInf",
									"prjTemSN", "specialNotifi", "prjSNType", "noticeTime", "effectiveTime", "delaySN",
									"delayCountDay", "correctionSN", "correctionDate", "remark" },
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
						jbxx.setPrjType("新建");
					}
					// 项目年份
					jbxx.setPrjYear(prjSN.substring(0, 4));

					if (null == jbxx.getId()) {
						superMapper.saveXmjbxx(jbxx);
					} else
						superMapper.updateXmjbxx(jbxx);
				} else {
					logger.error("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 许可证号不能为空 或 文档下方不能留有空行！");
					throw new RuntimeException("解析excel 页签序号：" + no + ",行数：" + (row + 1) + "  许可证号不能为空  或 文档下方不能留有空行！");
				}
			}

			if (no == 2) {// 项目属性

				String prjSN = items.get(0) + "";
				String serialNumber = items.get(1) + "";
				if (UtilValidate.isNotEmpty(prjSN) && UtilValidate.isNotEmpty(serialNumber)) {
					Xmsx sx = new Xmsx();
					BeanUtils.setProperty(sx,
							new String[] { "prjSN", "serialNumber", "prjNature", "prjAttr", "peacetimeUses",
									"aboveGroundLev", "underGroundLev", "aboveGroundHet", "underGroundHet", "buildings",
									"housingStockNum", "strucType", "checkDocSN", "checkDocDate", "checkSN",
									"checkDate", "cancelSN", "cancelDate", "imgJudgeRes", "exproprInfo", "remark" },
							items);
					// 删除所有再更新
					String prjsn = sx.getPrjSN();
					if (!dataxmsxDel.contains(prjsn)) {
						superMapper.delXmsxByPrjSN(sx.getPrjSN());
						dataxmsxDel.add(prjsn);
					}
					superMapper.saveXmsx(sx);

					// 判断工程状态,并更新
					StatusUtils.updateBuldStatus(superMapper, sx.getPrjSN(), sx.getSerialNumber());
					// 更新项目状态
					StatusUtils.updatePrjStatus(superMapper, sx.getPrjSN());
				} else {
					logger.error("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 许可证号/建筑序号不能为空  或 文档下方不能留有空行！");
					throw new RuntimeException(
							"解析excel 页签序号：" + no + ",行数：" + (row + 1) + "  许可证号/建筑序号不能为空  或 文档下方不能留有空行！");
				}
			}

			if (no == 3) {// 项目明细
				String prjSN = items.get(0) + "";
				String serialNumber = items.get(1) + "";
				if (UtilValidate.isNotEmpty(prjSN) && UtilValidate.isNotEmpty(serialNumber)) {
					Xmmx mx = new Xmmx();
					BeanUtils.setProperty(mx, new String[] { "prjSN", "serialNumber", "serialFunct", "aboveGroundArea",
							"underGroundArea", "blendArea", "aboveGroundLen" }, items);
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
				} else {
					logger.error("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 许可证号/建筑序号不能为空  或 文档下方不能留有空行！");
					throw new RuntimeException(
							"解析excel 页签序号：" + no + ",行数：" + (row + 1) + "  许可证号/建筑序号不能为空  或 文档下方不能留有空行！");
				}
			}
		} catch (Exception e) {
			logger.error("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！", e);
			throw new RuntimeException("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！" + e.getMessage());
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		dataxmsxDel.clear();
		dataxmmxDel.clear();
	}
}
