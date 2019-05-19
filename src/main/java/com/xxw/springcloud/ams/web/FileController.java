package com.xxw.springcloud.ams.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xxw.springcloud.ams.enums.UpLoadType;
import com.xxw.springcloud.ams.mapper.file.SuperMapper;
import com.xxw.springcloud.ams.model.BusFile;
import com.xxw.springcloud.ams.model.DxfEntity;
import com.xxw.springcloud.ams.util.FileUtils;
import com.xxw.springcloud.ams.util.ServiceUtil;
import com.xxw.springcloud.ams.util.UtilMisc;
import com.xxw.springcloud.ams.util.UtilValidate;
import com.xxw.springcloud.ams.util.dxf.DxfUtils;
import com.xxw.springcloud.ams.util.excel.Excel2003;

@RestController
public class FileController {

	public static Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private SuperMapper superMapper;

	/**
	 * 上传文件接口
	 * 
	 * @param uploadfile
	 * @param extraField
	 * @return
	 */
	@RequestMapping("/api/upload")
	public String uploadFile(@RequestParam("files") MultipartFile[] uploadfile,
			@RequestParam("upLoadType") String upLoadType, @RequestParam("prjSN") String prjSN) {

		logger.debug("exc:uploadFile params:upLoadType=" + upLoadType);

		String reM = ServiceUtil.returnSuccess("上传成功！");

		if (null == uploadfile || uploadfile.length == 0) {
			reM = ServiceUtil.returnError(null, "上传文件为 空！");
		} else if (UtilValidate.isEmpty(upLoadType)) {
			reM = ServiceUtil.returnError(null, "上传类型[upLoadType]必输！");
		} else if (UtilValidate.isEmpty(prjSN)) {
			reM = ServiceUtil.returnError(null, "许可证号[prjSN]必输！");
		} else {
			try {

				if (UpLoadType.ANALYSIS.toString().equals(upLoadType)) {

					for (MultipartFile file : uploadfile) {

						String fname = file.getOriginalFilename().toLowerCase();
						if (fname.endsWith(".xls") || fname.endsWith(".xlsx")) {
							if (file.isEmpty()) {
								continue;
							}
							Excel2003 _2003 = new Excel2003(superMapper);
							_2003.testExcel2003NoModel(file.getInputStream());
						} else if (fname.endsWith(".dxf")) {
							List<DxfEntity> items = DxfUtils.analysis(file, prjSN);
							if (UtilValidate.isNotEmpty(items)) {

								// 删除同许可证号同文件名的图元信息，删除后新增，则为修改
								superMapper.delDxfEntityByPrjSNAndFileName(
										UtilMisc.toMap("prjSN", prjSN, "fileName", fname));

								for (DxfEntity dxf : items) {
									// 判断图元信息是否存在
									List<DxfEntity> dxfs = superMapper.queryDxfEntity2(UtilMisc.toMap("prjSN",
											dxf.getPrjSN(), "longlatV", dxf.getLonglatV(), "fileName", fname));
									if (UtilValidate.isEmpty(dxfs)) {
										dxf.setFileName(fname);
										superMapper.saveDxfEntity(dxf);
									}
								}
							}
						} else {
							reM = ServiceUtil.returnError(null, "不支持解析的文档类型！系统支持excel,dxf解析");
						}
					}
				} else if (UpLoadType.SAVE.toString().equals(upLoadType)) {
					List<BusFile> list = FileUtils.saveUploadedFiles(Arrays.asList(uploadfile), prjSN);
					if (UtilValidate.isNotEmpty(list)) {
						for (BusFile busFile : list) {
							superMapper.saveFile(busFile);
						}
					}
				} else {
					reM = ServiceUtil.returnError(null, "未定义的上传类型[upLoadType]，请核实！");
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				reM = ServiceUtil.returnError(null, "上传失败！" + e.getMessage());
			}
		}

		logger.debug("exc:uploadFile return:" + reM);

		return reM;
	}

	/**
	 * 文件的下载
	 */
	@RequestMapping("/api/download")
	public String download(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {

		logger.debug("exc:download params:id=" + id);

		String reM = ServiceUtil.returnSuccess("下载成功！");

		try {
			BusFile file = superMapper.queryBusFileByID(UtilMisc.toMap("id", id));
			if (UtilValidate.isNotEmpty(file)) {

				String fileName = file.getFileName();

				InputStream inputStream = new FileInputStream(new File(FileUtils.UPLOADED_FOLDER + file.getUrlName()));
				OutputStream outputStream = response.getOutputStream();
				// 指明为下载
				response.setContentType("application/x-download");
				String agent = request.getHeader("User-Agent").toUpperCase(); // 获得浏览器信息并转换为大写
				if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) { // IE浏览器和Edge浏览器
					fileName = URLEncoder.encode(fileName, "UTF-8");
				} else { // 其他浏览器
					fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
				}
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName); // 设置文件名
				// 把输入流copy到输出流
				IOUtils.copy(inputStream, outputStream);
				outputStream.flush();

				inputStream.close();
				outputStream.close();
			} else {
				reM = ServiceUtil.returnSuccess("下载文件为空！");
			}
		} catch (Exception e) {
			logger.error("下载文件失败！", e);
			reM = ServiceUtil.returnSuccess("下载文件失败！" + e.getMessage());
		}

		logger.debug("exc:download return:" + reM);

		return reM;
	}

}