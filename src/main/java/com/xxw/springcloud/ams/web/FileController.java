package com.xxw.springcloud.ams.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xxw.springcloud.ams.enums.UpLoadType;
import com.xxw.springcloud.ams.mapper.file.FileMapper;
import com.xxw.springcloud.ams.model.BusFile;
import com.xxw.springcloud.ams.model.DxfEntity;
import com.xxw.springcloud.ams.util.Excel2003;
import com.xxw.springcloud.ams.util.FileUtils;

@RestController
public class FileController {

	public static Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private FileMapper fileMapper;

	/**
	 * 通过项目ID获取下属dxf或其他文档信息
	 * 
	 * @param superId
	 * @return
	 */
	@RequestMapping("/getBusFileByProjectID")
	public List<BusFile> getBusFileByProjectID(Long superId) {
		return fileMapper.getBusFileByProjectID(superId);
	}

	/**
	 * 通过ID删除文档信息
	 * 
	 * @param superId
	 * @return
	 */
	@RequestMapping("/delBusFileByID")
	public void delBusFileByID(Long id) {
		fileMapper.delBusFileByID(id);
	}

	/**
	 * 获取对应项目的图像信息
	 * 
	 * @param superId
	 * @return
	 */
	@RequestMapping("/getDxfPositionsByProjectID")
	public Map<String, List<String>> getDxfPositionsByProjectID(Long fileID) {

		Map<String, List<String>> rem = new HashMap<String, List<String>>();
		List<DxfEntity> pos = fileMapper.getDxfEntityByProjectID(fileID);
		if (null != pos && pos.size() > 0) {

			for (DxfEntity p : pos) {
				String constrID = p.getConstrID() + "";
				List<String> list = rem.get(constrID);
				if (null == list) {
					list = new ArrayList<String>();
					rem.put(constrID, list);
				}
				list.add(p.getLongitude() + "," + p.getLatitude());
			}
		}
		return rem;
	}

	/**
	 * 上传文件接口
	 * @param uploadfile
	 * @param extraField
	 * @return
	 */
	@RequestMapping("/api/upload")
	public String uploadFile(@RequestParam("files") MultipartFile[] uploadfile,
			@RequestParam("upLoadType") String upLoadType) {

		logger.debug("exc:uploadFile params:upLoadType="+upLoadType);

		if (null == uploadfile||uploadfile.length == 0) {
			return "E";
		}

		try {
			
			if(UpLoadType.ANALYSIS.toString().equals(upLoadType)) {
				for (MultipartFile file : uploadfile) {
					
					if (file.isEmpty()) {
						continue; //next pls
					}
					Excel2003 _2003 = new Excel2003(fileMapper);
					_2003.testExcel2003NoModel(file.getInputStream());
				}
			}else if(UpLoadType.SAVE.toString().equals(upLoadType)){
				FileUtils.saveUploadedFiles(Arrays.asList(uploadfile));
			}else {
				return "未定义的上传类型[upLoadType]，请核实！";
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "E:"+e.getMessage();
		}

		return "S";

	}

}