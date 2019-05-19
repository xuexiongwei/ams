package com.xxw.springcloud.ams.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xxw.springcloud.ams.mapper.file.SuperMapper;
import com.xxw.springcloud.ams.model.DxfEntity;
import com.xxw.springcloud.ams.model.Header;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;

@RestController
public class DxfController {

	public static Logger logger = LoggerFactory.getLogger(DxfController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 查询指定项目经纬度信息
	 */
	@RequestMapping("/api/dxf/query")
	public String query(@RequestParam("prjSN") String prjSN) {

		logger.debug("exc:query params:prjSN=" + prjSN);

		String reM = ServiceUtil.returnSuccess("下载成功！");
		Header header = new Header();
		header.setRspPageCount(0);
		
		List<DxfEntity> items = superMapper.queryDxfEntity(UtilMisc.toMap("prjSN", prjSN));
		if (UtilValidate.isEmpty(items)) {
			items = FastList.newInstance();
		}
		reM = ServiceUtil.returnSuccess(items, "points", header);

		logger.debug("exc:query return:" + reM);

		return reM;
	}

}