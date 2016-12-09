package com.consult.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ReadExcel {

	Logger logger = Logger.getLogger(ReadExcel.class);

	public List<Map<String, String>> readXls(String fileName)
			throws IOException {
		try {
			InputStream is = new FileInputStream(fileName);
			XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
			List<Map<String, String>> noList = new ArrayList<Map<String, String>>();
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 循环行Row
				for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						Map<String, String> map = new HashMap<String, String>();
						String no = getValue(hssfRow.getCell(0));
						String psptId = getValue(hssfRow.getCell(23));
						map.put("no", no);
						map.put("psptId", psptId);
						noList.add(map);
					}
				}
			}
			return noList;
		} catch (Exception e) {
			return readxlsx(fileName);
		}
	}

	public List<Map<String, String>> readxlsx(String fileName)
			throws IOException {
		InputStream is = new FileInputStream(fileName);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<Map<String, String>> noList = new ArrayList<Map<String, String>>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						Map<String, String> map = new HashMap<String, String>();
						String no = getValue(hssfRow.getCell(0));
						String psptId = getValue(hssfRow.getCell(23));
						map.put("no", no);
						map.put("psptId", psptId);
						noList.add(map);
					} catch (Exception e) {
						continue;
					}
				}
			}
		}
		return noList;
	}

	@SuppressWarnings("static-access")
	private String getValue(XSSFCell hssfCell) throws Exception {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) throws Exception {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
