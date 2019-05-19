package com.xxw.springcloud.ams.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.xxw.springcloud.ams.enums.FileEnum;
import com.xxw.springcloud.ams.model.BusFile;

public class FileUtils {

	public static String UPLOADED_FOLDER = "D://temp//";

	// save file
	public static List<BusFile> saveUploadedFiles(List<MultipartFile> files, String prjSN) throws IOException {

		List<BusFile> fileL = FastList.newInstance();

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; // next pls
			}
			String fname = file.getOriginalFilename();
			String type = fname.substring(fname.lastIndexOf(".") + 1, fname.length());
			String urlName = SerialNumberUtil.getUUID() + "." + type;

			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + urlName);
			Files.write(path, bytes);

			BusFile fi = new BusFile();
			fi.setDelFlag(FileEnum.C);
			fi.setFileName(fname);
			fi.setFileType(type);
			fi.setUpdateTime(DateUtils.nowDateString(DateUtils.FORMAT1));
			fi.setPrjSN(prjSN);
			fi.setUrlName(urlName);

			fileL.add(fi);
		}
		return fileL;
	}

}
