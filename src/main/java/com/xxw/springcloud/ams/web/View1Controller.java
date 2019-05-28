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
			List<Bb001> bb001s = superMapper.findbb001(params);// 查询出数据只为分页
			Map<String, String> viewM = FastMap.newInstance();
			for (int i = 0; i < bb001s.size(); i++) {
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
				viewM.put("count", count + "");// 项目个数
				viewM.put("sumArea", sumArea + "");// 总面积
				viewM.put("sumLen", sumLen + "");// 总建筑长度
				viewM.put("aboveGroundSumArea", aboveGroundSumArea + "");// 地上建筑面积（平方米）
				viewM.put("underGroundSumArea", underGroundSumArea + "");// 地下建筑面积（平方米）
				viewM.put("buildings", buildings + "");// 栋数
				viewM.put("housingStockNum", housingStockNum + "");// 住房套数
			}
			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(viewM, "viewList", header);
		} catch (

		Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.debug("exc:queryUserOperByDate return:" + reM);

		return reM;
	}
}