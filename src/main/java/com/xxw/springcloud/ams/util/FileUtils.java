package com.xxw.springcloud.ams.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.xxw.springcloud.ams.model.BusFile;

public class FileUtils {

	public static String UPLOADED_FOLDER = "D://temp//";

	/**
	 * 根据文件实体保存文件到硬盘
	 * 
	 * @param files
	 * @param items
	 * @return
	 * @throws IOException
	 */
	public static List<BusFile> saveUploadedFiles(List<MultipartFile> files, Map<String, BusFile> items)
			throws IOException {

		List<BusFile> fileL = FastList.newInstance();

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; // next pls
			}
			String fname = file.getOriginalFilename();
			fname = fname.substring(0, fname.lastIndexOf("."));
			BusFile item = items.get(fname);
			if (UtilValidate.isNotEmpty(item)) {
				byte[] bytes = file.getBytes();
				Path path = Paths.get(UPLOADED_FOLDER + item.getUrlName());
				Files.write(path, bytes);
			}
		}
		return fileL;
	}

	/**
	 * 获取上传对象的数据库实体
	 * 
	 * @param files
	 * @param prjSN
	 * @return
	 * @throws IOException
	 */
	public static Map<String, BusFile> getBusFilesEntity(List<MultipartFile> files, String prjSN) throws IOException {

		Map<String, BusFile> fileM = FastMap.newInstance();

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; // next pls
			}
			String fname = file.getOriginalFilename();
			String type = fname.substring(fname.lastIndexOf(".") + 1, fname.length());
			fname = fname.substring(0, fname.lastIndexOf("."));
			String urlName = SerialNumberUtil.getUUID() + "." + type;

			BusFile fi = new BusFile();
			fi.setFileName(fname);
			fi.setFileType(type);
			fi.setUpdateTime(DateUtils.nowDateString(DateUtils.FORMAT1));
			fi.setPrjSN(prjSN);
			fi.setUrlName(urlName);
			fileM.put(fname, fi);
		}
		return fileM;
	}

}
