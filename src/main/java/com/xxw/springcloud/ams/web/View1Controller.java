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

		logger.info("exc:queryUserOperByDate params:inputjson=" + inputjson);

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
			List<Bb001> bb001s = superMapper.findbb001(params);// 查询出数据只为分页
			List<Map<String, Object>> viewM = FastList.newInstance();
			for (Bb001 b : bb001s) {

				Map<String, Object> item = FastMap.newInstance();
				viewM.add(item);

				params.clear();
				String prjYear = b.getPrjYear();
				if (UtilValidate.isNotEmpty(prjYear)) {
					params.put("prjYear", prjYear);
				}
				String prjSNType = b.getPrjSNType();
				if (UtilValidate.isNotEmpty(prjSNType)) {
					params.put("prjSNType", prjSNType);
				}
				String prjType = b.getPrjType();
				if (UtilValidate.isNotEmpty(prjType)) {
					params.put("prjType", prjType);
				}
				String prjUnit = b.getPrjUnit();
				if (UtilValidate.isNotEmpty(prjUnit)) {
					params.put("prjUnit", prjUnit);
				}
				String prjStatus = b.getPrjStatus();
				if (UtilValidate.isNotEmpty(prjStatus)) {
					params.put("prjStatus", prjStatus);
				}
				String buldStatus = b.getBuldStatus();
				if (UtilValidate.isNotEmpty(buldStatus)) {
					params.put("buldStatus", buldStatus);
				}
				String prjAdr = b.getPrjAdr();
				if (UtilValidate.isNotEmpty(prjAdr)) {
					params.put("prjAdr", prjAdr);
				}
				String prjClasfiName1 = b.getPrjClasfiName1();
				if (UtilValidate.isNotEmpty(prjClasfiName1)) {
					params.put("prjClasfiName1", prjClasfiName1);
				}
				String prjClasfiName2 = b.getPrjClasfiName2();
				if (UtilValidate.isNotEmpty(prjClasfiName2)) {
					params.put("prjClasfiName2", prjClasfiName2);
				}
				String prjClasfiName3 = b.getPrjClasfiName3();
				if (UtilValidate.isNotEmpty(prjClasfiName3)) {
					params.put("prjClasfiName3", prjClasfiName3);
				}
				String prjClasfiName4 = b.getPrjClasfiName4();
				if (UtilValidate.isNotEmpty(prjClasfiName4)) {
					params.put("prjClasfiName4", prjClasfiName1);
				}
				String prjClasfiName5 = b.getPrjClasfiName5();
				if (UtilValidate.isNotEmpty(prjClasfiName5)) {
					params.put("prjClasfiName5", prjClasfiName5);
				}
				item.put("prjYear", StringUtils.getStr(prjYear));
				item.put("prjType", StringUtils.getStr(prjType));
				item.put("prjSNType", StringUtils.getStr(prjSNType));
				item.put("prjUnit", StringUtils.getStr(prjUnit));
				item.put("prjStatus", StringUtils.getStr(prjStatus));
				item.put("buldStatus", StringUtils.getStr(buldStatus));
				item.put("prjAdr", StringUtils.getStr(prjAdr));
				item.put("prjClasfiName1", StringUtils.getStr(prjClasfiName1));
				item.put("prjClasfiName2", StringUtils.getStr(prjClasfiName2));
				item.put("prjClasfiName3", StringUtils.getStr(prjClasfiName3));
				item.put("prjClasfiName4", StringUtils.getStr(prjClasfiName1));
				item.put("prjClasfiName5", StringUtils.getStr(prjClasfiName5));

				// 查询属性id 及 明细 id 信息
				List<Bb001> bb001L = superMapper.findbb001Detail(params);
				// // 统计数据
				int count = 0;// 项目个数
				Double sumArea = 0.0d;// 总面积
				Double sumLen = 0.0d;// 总建筑长度
				Double aboveGroundSumArea = 0.0d;// 地上建筑面积（平方米）
				Double underGroundSumArea = 0.0d;// 地下建筑面积（平方米）
				Long buildings = 0l;// 栋数
				Long housingStockNum = 0l;// 住房套数

				// 去重复
				Map<String, String> sxidM = FastMap.newInstance();
				Map<String, String> mxidM = FastMap.newInstance();
				for (Bb001 bb001 : bb001L) {
					String sxid = bb001.getSxid();
					String mxid = bb001.getMxid();
					sxidM.put(sxid, "1");
					mxidM.put(mxid, "1");
				}
				count = sxidM.size();
				// 计算属性表统计数据
				for (Map.Entry<String, String> entry : sxidM.entrySet()) {
					String sxid = entry.getKey();
					// 计算项目个数
					Xmsx sx = superMapper.queryXmsxByID(Long.parseLong(sxid));
					Long bds = sx.getBuildings();
					if (UtilValidate.isNotEmpty(bds)) {
						buildings = buildings + bds;
					}
					// 住房套数
					Long hsn = sx.getHousingStockNum();
					if (UtilValidate.isNotEmpty(hsn)) {
						housingStockNum = housingStockNum + hsn;
					}
				}
				// 计算明细表统计数据
				for (Map.Entry<String, String> entry : mxidM.entrySet()) {
					String mxid = entry.getKey();
					// 计算总面积
					Xmmx xmmx = superMapper.queryXmmxByID(Long.parseLong(mxid));
					Double aga = xmmx.getAboveGroundArea();// 总建筑面积（平方米）地上
					Double uga = xmmx.getUnderGroundArea();// 总建筑面积（平方米）地下
					Double agl = xmmx.getAboveGroundLen();// 建筑长度（米）

					if (null == aga)
						aga = 0.0d;

					if (null == uga)
						uga = 0.0d;

					if (null == agl)
						agl = 0.0d;

					// 总建筑面积（平方米）
					sumArea = StringUtils.sswr(sumArea + aga + uga);
					// 总建筑面积（平方米）地上
					aboveGroundSumArea = StringUtils.sswr(aboveGroundSumArea + aga);
					// 总建筑面积（平方米）地下
					underGroundSumArea = StringUtils.sswr(underGroundSumArea + uga);
					// 建筑长度（米）
					sumLen = StringUtils.sswr(sumLen + agl);
				}
				item.put("count", count);// 项目个数
				item.put("sumArea", sumArea);// 总面积
				item.put("sumLen", sumLen);// 总建筑长度
				item.put("aboveGroundSumArea", aboveGroundSumArea);// 地上建筑面积（平方米）
				item.put("underGroundSumArea", underGroundSumArea);// 地下建筑面积（平方米）
				item.put("buildings", buildings);// 栋数
				item.put("housingStockNum", housingStockNum);// 住房套数
			}
			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(viewM, "viewList", header);
		} catch (

		Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.info("exc:queryUserOperByDate return:" + reM);

		return reM;
	}
}