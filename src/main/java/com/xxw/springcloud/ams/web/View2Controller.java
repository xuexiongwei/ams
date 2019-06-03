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
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.model.Xmsx;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.FastMap;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StringUtils;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class View2Controller {

	public static Logger logger = LoggerFactory.getLogger(View2Controller.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 第二个报表 <br>
	 * 查询条件 项目许可证号、建设单位、建设位置、工程名称、项目类型、联系人、联系方式
	 */
	@RequestMapping("/api/view/bb002")
	public String bb002(@RequestBody String inputjson) {

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

			totalSize = superMapper.findXmjbxxByAttrCount(params);

			pageIndex = (pageIndex - 1) * pageSize;
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);
			List<Xmjbxx> xmjbxxL = superMapper.findXmjbxxByAttr(params);
			List<Map<String, String>> viewM = FastList.newInstance();
			for (Xmjbxx jb : xmjbxxL) {
				Map<String, String> item = FastMap.newInstance();
				viewM.add(item);

				String prjSN = jb.getPrjSN();// 许可证号
				String prjUnit = jb.getPrjUnit();// 建设单位
				String prjAdr = jb.getPrjAdr();// 建设位置
				String prjStatus = jb.getPrjStatus();// 项目状态
				String prjSNType = jb.getPrjSNType();// 许可证类型
				String prjMark = jb.getPrjMark();// 项目标识
				String prjName = jb.getPrjName();// 工程名称
				String prjType = jb.getPrjType();// 项目类型
				String contacts = jb.getContacts();// 联系人
				String contactInf = jb.getContactInf();// 联系方式
				String specialNotifi = jb.getSpecialNotifi();// 特别告知事项
				String prjTemSN = jb.getPrjTemSN();// 附带临建批号
				String remark = jb.getRemark();// 备注
				item.put("prjSN", StringUtils.getStr(prjSN));
				item.put("prjUnit", StringUtils.getStr(prjUnit));
				item.put("prjAdr", StringUtils.getStr(prjAdr));
				item.put("prjName", StringUtils.getStr(prjName));
				item.put("prjType", StringUtils.getStr(prjType));
				item.put("contacts", StringUtils.getStr(contacts));
				item.put("contactInf", StringUtils.getStr(contactInf));
				item.put("specialNotifi", StringUtils.getStr(specialNotifi));
				item.put("prjTemSN", StringUtils.getStr(prjTemSN));
				item.put("remark", StringUtils.getStr(remark));
				item.put("prjStatus", StringUtils.getStr(prjStatus));
				item.put("prjMark", StringUtils.getStr(prjMark));
				item.put("prjSNType", StringUtils.getStr(prjSNType));
				item.put("prjXz", "");// 项目性质
				if (UtilValidate.isNotEmpty(prjSN)) {
					List<Xmsx> xmsxl = superMapper.findXmsxByAttr(UtilMisc.toMap("prjSN", prjSN, "serialNumber", 1));
					if (UtilValidate.isNotEmpty(xmsxl)) {
						int max = superMapper.queryXmsxMaxIndexByPrjSN(jb.getPrjSN());
						item.put("prjXz", xmsxl.get(0).getPrjNature() + "等" + max + "项");// 项目性质
					}
				}
			}
			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(viewM, "viewList", header);
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.info("exc:queryUserOperByDate return:" + reM);

		return reM;
	}
}