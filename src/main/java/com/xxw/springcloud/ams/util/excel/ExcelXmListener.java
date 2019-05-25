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
				if (null == jbxx.getId())
					superMapper.saveXmjbxx(jbxx);
				else
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
				}
			}

			if (no == 3) {// 项目明细
				Xmmx mx = new Xmmx();
				BeanUtils.setProperty(mx, new String[] { "prjSN", "serialNumber", "serialFunct", "aboveGroundArea",
						"underGroundArea", "blendArea", "aboveGroundLen" }, items);
				// 还差分级信息-------------------------------------------------------------------------------------------------
				if (null != mx.getPrjSN()) {

					List<Object> levs = items.subList(7, 11);

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
