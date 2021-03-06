package com.xxw.springcloud.ams.util.excel;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.xxw.springcloud.ams.mapper.file.SuperMapper;

public class ExcelDic {

	private SuperMapper superMapper = null;
	public static Logger logger = LoggerFactory.getLogger(ExcelDic.class);

	public ExcelDic(SuperMapper f) {
		superMapper = f;
	}

	public void testExcel2003NoModel(InputStream inputStream) {
		try {
			// 解析每行结果在listener中处理
			ExcelDicListener listener = new ExcelDicListener(superMapper);

			ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, listener);
			excelReader.read();
		} catch (Exception e) {
			logger.error("error Excel2003!", e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
