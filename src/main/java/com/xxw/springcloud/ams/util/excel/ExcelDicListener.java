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
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.SerialNumberUtil;

/**
 * 解析excel 1.判断重复项 2.检查字段是否匹配一致 3.提示数据有错文档及其行数
 * 
 * @author yangwei
 *
 */
public class ExcelDicListener extends AnalysisEventListener<Object> {

	private SuperMapper superMapper = null;
	public static Logger logger = LoggerFactory.getLogger(ExcelDicListener.class);

	public ExcelDicListener(SuperMapper f) {
		superMapper = f;
	}

	// 自定义用于暂时存储data。
	Map<String, String> data = new HashMap<String, String>();
	List<String> dicTypeM = FastList.newInstance();

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
			if (null == items.get(0)) {
				logger.info("解析excel 去除空行：" + items);
				return;
			}

			if (no == 1) {// 分类体系设计

				ClassifiDic dic = null;
				String curentCode = "";
				String dic1ID = "";

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", DicEnum.FJ);

				for (int i = 0; i < 10; i++) {
					if (i % 2 == 0)
						continue;
					String parentCode = "";
					// 一级分类
					for (int j = 0; j < i / 2; j++) {
						parentCode += items.get(j * 2) + "";
					}
					curentCode = parentCode + items.get(i - 1);

					String curentID = data.get(curentCode);
					if (null == curentID) {

						params.put("code", curentCode);
						dic = superMapper.queryDicByCode2(params);
						boolean isNew = false;
						if (null == dic) {
							isNew = true;
							dic = new ClassifiDic();
							dic1ID = SerialNumberUtil.getUUID();
							dic.setId(dic1ID);
							dic.setType(DicEnum.FJ);// 分级字典
							dic.setOther(i / 2 + 1 + "");

							String v = data.get(parentCode);
							if (null != v) {
								dic.setParentID(v);
							} else {
								dic.setParentID("");// 设置为空字符串，方便后续查找
							}
						} else {
							dic1ID = dic.getId();
						}
						dic.setCode(curentCode);// 分级编码
						dic.setName(items.get(i) + "");// 分级名称
						if (isNew)
							superMapper.saveDic(dic);
						else
							superMapper.updateDic(dic);

						data.put(curentCode, dic1ID);
					}
				}
			}

			if (no == 2) {// 朝阳行政区划表
				if (!dicTypeM.contains(DicEnum.CYXZGHB.toString())) {
					superMapper.delDicByType(DicEnum.CYXZGHB + "");
					dicTypeM.add(DicEnum.CYXZGHB + "");
				}
				ClassifiDic dic = new ClassifiDic();
				String dic1ID = SerialNumberUtil.getUUID();
				dic.setId(dic1ID);
				dic.setType(DicEnum.CYXZGHB);// 分级字典
				dic.setCode(items.get(3) + "");
				dic.setName(items.get(2) + "");
				dic.setOther(items.get(1) + "");
				superMapper.saveDic(dic);
			}
		} catch (Exception e) {
			logger.error("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！", e);
			throw new RuntimeException("解析excel 页签序号：" + no + ",行数：" + (row + 1) + " 解析异常！");
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		data.clear();
	}
}
