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
import com.xxw.springcloud.ams.model.SysUser;
import com.xxw.springcloud.ams.model.UserOperation;
import com.xxw.springcloud.ams.model.Xmjbxx;
import com.xxw.springcloud.ams.util.CheckInput;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.StatusUtils;
import com.xxw.springcloud.ams.util.StringUtils;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class XmjbxxController {

	public static Logger logger = LoggerFactory.getLogger(XmjbxxController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询项目基本信息
	 */
	@RequestMapping("/api/xmjbxx/query")
	public String query(@RequestBody String inputjson) {

		logger.info("exc:query params:inputjson=" + inputjson);

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
			params.put("isMH", true);
			totalSize = superMapper.findXmjbxxByAttrCount(params);

			pageIndex = (pageIndex - 1) * pageSize;
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);
			List<Xmjbxx> items = superMapper.findXmjbxxByAttr(params);

			header.setRspPageCount(totalSize);
			reM = ServiceUtil.returnSuccess(items, "xmjbxxList", header);
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "查询异常！" + e.getMessage());
		}

		logger.info("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 新增项目基本信息
	 */
	@RequestMapping("/api/xmjbxx/createOrUpdate")
	public String createOrUpdate(@RequestBody String inputjson) {

		logger.info("exc:query params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "新增异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object prjSN = params.get("prjSN");

			reM = CheckInput.jbxxC(params);

			if (UtilValidate.isEmpty(reM)) {

				String year = StringUtils.getYear(prjSN + "");

				// 查询此项目信息是否存在queryXmjbxx
				Xmjbxx jbxx = superMapper.queryXmjbxxByPrjSN(prjSN + "");

				UserOperation uo = new UserOperation(UserOperation.od_jbxx);
				uo.setUserID(header.getReqUserId());
				SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
				uo.setUserName(user.getUserName());

				Object prjAdrCode = params.get("prjAdrCode");
				if (UtilValidate.isNotEmpty(prjAdrCode)) {
					ClassifiDic dic = superMapper
							.queryDicByCode2(UtilMisc.toMap("type", DicEnum.CYXZGHB, "code", prjAdrCode));
					if (UtilValidate.isNotEmpty(dic)) {
						params.put("prjAdr", dic.getName() + params.get("prjAdrDetail"));
					}
				}

				params.put("prjYear", year);
				if (UtilValidate.isNotEmpty(jbxx)) {
					uo.setOperAction(UserOperation.oa_u);
					superMapper.updateXmjbxx(params);
				} else {
					uo.setOperAction(UserOperation.oa_c);
					superMapper.saveXmjbxx(params);
				}
				uo.setPrjSN(params.get("prjSN") + "");// 许可证号
				superMapper.saveUserOper(uo);

				// 更新项目标识
				StatusUtils.updatePrjMark(superMapper, prjSN + "");

				reM = ServiceUtil.returnSuccess("保存成功！");
			}
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "新增异常！" + e.getMessage());
		}

		logger.info("exc:query return:" + reM);

		return reM;
	}

	/**
	 * 删除项目属性
	 */
	@RequestMapping("/api/xmjbxx/del")
	public String del(@RequestBody String inputjson) {

		logger.info("exc:del params:inputjson=" + inputjson);

		String reM = ServiceUtil.returnError("E", "删除异常！");
		try {
			Header header = ServiceUtil.getContextHeader(inputjson);
			String bodyStr = ServiceUtil.getContextBody(inputjson);
			Map<String, Object> params = JSONObject.parseObject(bodyStr);

			Object id = params.get("id");
			if (UtilValidate.isNotEmpty(id)) {
				Xmjbxx jbxx = superMapper.queryXmjbxxByID(UtilMisc.toMap("id", id));
				if (UtilValidate.isNotEmpty(jbxx)) {
					UserOperation uo = new UserOperation(UserOperation.od_jbxx);
					uo.setUserID(header.getReqUserId());
					uo.setOperAction(UserOperation.oa_d);
					SysUser user = superMapper.selectUserByUserID(Long.parseLong(header.getReqUserId()));
					uo.setUserName(user.getUserName());
					superMapper.delXmjbxxByPrjSN(params);

					uo.setPrjSN(jbxx.getPrjSN());// 许可证号
					superMapper.saveUserOper(uo);
					reM = ServiceUtil.returnSuccess("删除成功 ！");
				} else {
					reM = ServiceUtil.returnSuccess("删除数据不存在，或已被删除，无需再次删除 ！id=[" + id + "]");
				}
			} else {
				reM = ServiceUtil.returnError("E", "删除失败，数据ID不能为空！");
			}
		} catch (Exception e) {
			logger.error("查询异常！", e);
			reM = ServiceUtil.returnError("E", "删除异常！" + e.getMessage());
		}

		logger.info("exc:del return:" + reM);

		return reM;
	}
}