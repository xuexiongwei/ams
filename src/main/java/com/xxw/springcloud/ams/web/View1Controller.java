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
import com.xxw.springcloud.ams.model.Bb001;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.Xmmx;
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.FastMap;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StringUtils;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class View1Controller {

	public static Logger logger = LoggerFactory.getLogger(View1Controller.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 第一个报表
	 */
	@RequestMapping("/api/view/bb001")
	public String bb001(@RequestBody String inputjson) {

		logger.debug("exc:queryUserOperByDate params:inputjson=" + inputjson);

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

			totalSize = superMapper.findbb001Count(params);

			pageIndex = (pageIndex - 1) * pageSize;
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);
			List<Bb001> bb001s = superMapper.findbb001(params);
			List<Map<String, String>> viewM = FastList.newInstance();
			for (Bb001 b : bb001s) {
				Map<String, String> item = FastMap.newInstance();
				viewM.add(item);

				item.put("prjSN", b.getPrjSN());// 年份
				item.put("year", b.getYear());// 年份
				item.put("prjSNType", b.getPrjSNType());// 许可证类型
				item.put("prjType", b.getPrjType());// 建设类型
				item.put("prjUnit", b.getPrjUnit());// 建设单位
				item.put("prjStatus", b.getPrjStatus());// 项目状态
				item.put("buldStatus", b.getBuldStatus());// 工程状态
				item.put("prjAdr", b.getPrjAdr());// 建设位置
				item.put("prjClasfiName1", b.getPrjClasfiName1());// 一级分类
				item.put("prjClasfiName2", b.getPrjClasfiName2());// 二级分类
				item.put("prjClasfiName3", b.getPrjClasfiName3());// 三级分类
				item.put("prjClasfiName4", b.getPrjClasfiName4());// 四级分类
				item.put("prjClasfiName5", b.getPrjClasfiName5());// 五级分类

				// 统计数据
				List<String> names = FastList.newInstance();
				int count = 0;// 项目个数
				Double sumArea = 0.0d;// 总面积
				Double sumLen = 0.0d;// 总建筑长度
				Double aboveGroundSumArea = 0.0d;// 地上建筑面积（平方米）
				Double underGroundSumArea = 0.0d;// 地下建筑面积（平方米）
				Long buildings = 0l;// 栋数
				Long housingStockNum = 0l;// 住房套数

				Map<String, String> param = FastMap.newInstance();
				param.putAll(item);
				param.put("prjSN", b.getPrjSN());

				List<Bb001> bb001Details = superMapper.findbb001Detail(param);
				for (Bb001 bb001 : bb001Details) {
					String sxid = bb001.getSxid();
					String mxid = bb001.getMxid();

					// 计算项目个数
					Xmsx sx = superMapper.queryXmsxByID(Long.parseLong(sxid));
					if (!names.contains(sx.getSerialNumber() + "")) {
						count++;
						names.add(sx.getSerialNumber() + "");
					}
					Long bds = sx.getBuildings();
					if (UtilValidate.isNotEmpty(bds)) {
						buildings = buildings + bds;
					}
					// 住房套数
					Long hsn = sx.getHousingStockNum();
					if (UtilValidate.isNotEmpty(hsn)) {
						housingStockNum = housingStockNum + hsn;
					}

					// 计算总面积
					Xmmx xmmx = superMapper.queryXmmxByID(Long.parseLong(mxid));
					Double aga = xmmx.getAboveGroundArea();// 总建筑面积（平方米）地上
					Double uga = xmmx.getUnderGroundArea();// 总建筑面积（平方米）地下
					Double agl = xmmx.getAboveGroundLen();// 建筑长度（米）

					String cfc = xmmx.getPrjClasfiCode();

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
							sumArea = StringUtils.sswr(sumArea + aga + uga);
						}
					} else {
						// 总建筑面积（平方米）
						sumArea = StringUtils.sswr(sumArea + aga + uga);
					}
					// 总建筑面积（平方米）地上
					aboveGroundSumArea = StringUtils.sswr(aboveGroundSumArea + aga);
					// 总建筑面积（平方米）地下
					underGroundSumArea = StringUtils.sswr(underGroundSumArea + uga);
					// 建筑长度（米）
					sumLen = StringUtils.sswr(sumLen + agl);
				}

				item.put("count", count + "");// 项目个数
				item.put("sumArea", sumArea + "");// 总面积
				item.put("sumLen", sumLen + "");// 总建筑长度
				item.put("aboveGroundSumArea", aboveGroundSumArea + "");// 地上建筑面积（平方米）
				item.put("underGroundSumArea", underGroundSumArea + "");// 地下建筑面积（平方米）
				item.put("buildings", buildings + "");// 栋数
				item.put("housingStockNum", housingStockNum + "");// 住房套数

			}
			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(viewM, "viewList", header);
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.debug("exc:queryUserOperByDate return:" + reM);

		return reM;
	}
}