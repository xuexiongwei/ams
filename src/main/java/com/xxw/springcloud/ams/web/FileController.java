package com.xxw.springcloud.ams.web;

import java.util.ArrayList;
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

import com.xxw.springcloud.ams.mapper.file.FileMapper;
import com.xxw.springcloud.ams.model.BusDxfPosition;
import com.xxw.springcloud.ams.model.BusDxfTips;
import com.xxw.springcloud.ams.model.BusFile;
import com.xxw.springcloud.ams.util.Excel2003;

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
		List<BusDxfPosition> pos = fileMapper.getDxfPositionsByProjectID(fileID);
		if (null != pos && pos.size() > 0) {

			for (BusDxfPosition p : pos) {
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
	 * 获取对应项目的图像信息的标点信息
	 * 
	 * @return
	 */
	@RequestMapping("/getBusDxfTipsByProjectID")
	public List<BusDxfTips> getBusDxfTipsByProjectID(Long dxfID) {
		return fileMapper.getBusDxfTipsByProjectID(dxfID);
	}

	/**
	 * 修改对应项目的图像信息的标点信息
	 * 
	 * @return
	 */
	@RequestMapping("/updataBusDxfTips")
	public void updataBusDxfTips(BusDxfTips tips) {
		fileMapper.updata(tips);
	}

	/**
	 * 删除对应项目的图像信息的标点信息
	 * 
	 * @return
	 */
	@RequestMapping("/delBusDxfTipsByID")
	public void delBusDxfTipsByID(Long id) {
		fileMapper.delBusDxfTipsByID(id);
	}

	/**
	 * 新增对应项目的图像信息的标点信息
	 * 
	 * @return
	 */
	@RequestMapping("/saveBusDxfTips")
	public void saveBusDxfTips(BusDxfTips tips) {
		fileMapper.insertBusDxfTips(tips);
	}

	/**
	 * 上传文件接口
	 * @param uploadfile
	 * @param extraField
	 * @return
	 */
	@RequestMapping("/api/upload")
	public String uploadFile(@RequestParam("files") MultipartFile[] uploadfile,
			@RequestParam("extraField") String extraField) {

		logger.debug("Single file upload!");

		if (null == uploadfile||uploadfile.length == 0) {
			return "E";
		}

		try {
			
			 for (MultipartFile file : uploadfile) {
				 
		            if (file.isEmpty()) {
		                continue; //next pls
		            }
		            Excel2003 _2003 = new Excel2003();
		            _2003.testExcel2003NoModel(file.getInputStream());
		        }

//			FileUtils.saveUploadedFiles(Arrays.asList(uploadfile));

		} catch (Exception e) {
			e.printStackTrace();
			return "E:"+e.getMessage();
		}

		return "S";

	}

}