package com.consult.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Encoder;

public class Test {
	public static void main(String[] args) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name", "罗洋");
		dataMap.put("sex", "男");
		dataMap.put("mingzu", "汉");

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH);// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日

		dataMap.put("year", String.valueOf(year));
		dataMap.put("month", String.valueOf(month));
		dataMap.put("day", String.valueOf(day));
		
		dataMap.put("cardId", "4302256888");
		dataMap.put("addr", "中华人民共和国");

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("index", i);
			map.put("content", "sfsd范德萨发" + i);
			list1.add(map);
		}

		dataMap.put("table1", list1);
		dataMap.put("table2", list1);

		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("activeTime", i);
			// map.put("autograph", "��ʦ˵��" + i);
			map.put("temporary", "temporary" + i);
			map.put("permanent", "permanent" + i);
			map.put("remark", "remark" + i);
			map.put("agree", "agree" + i);
			String im1 = null;
			String im2 = null;
			if (i == 0) {
				im1 = "4glottery_tj.png";
				im2 = "alert_1.png";
			} else if (i == 1) {
				im1 = "cg.png";
				im2 = "alert_2.png";
			} else if (i == 2) {
				im1 = "ios_ewm.png";
				im2 = "closed_ico.png";
			} else if (i == 3) {
				im1 = "shibai.png";
				im2 = "ndl_1.png";
			} else if (i == 4) {
				im1 = "ndl.png";
				im2 = "iphone_xz.png";
			}
			map.put("image1", getImageStr(im1));
			map.put("image2", getImageStr(im2));
			map.put("plan3_index", i);
			// map.put("doc", "docautograph" + i);
			list2.add(map);
		}
		dataMap.put("table3", list2);

		// dataMap.put("image1", getImageStr("4glottery_tj.png"));
		// dataMap.put("image2", getImageStr("alert_1.png"));
		//
		DocumentHandler mdoc = new DocumentHandler();
		try {
			mdoc.createDoc(dataMap, "E:/outFile2.doc","0");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getImageStr(String name) {
		String imgFile = "F:/hallv2/WebRoot/html/images/" + name;
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
}
