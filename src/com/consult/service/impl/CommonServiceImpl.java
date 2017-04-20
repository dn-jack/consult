package com.consult.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;
import com.consult.bean.ConsultConfigArea;
import com.consult.bean.ConsultContent;
import com.consult.bean.ConsultContract;
import com.consult.bean.ConsultIdCardInfo;
import com.consult.bean.ConsultRecord;
import com.consult.bean.ConsultRecordCount;
import com.consult.dao.CommonMapper;
import com.consult.service.CommonService;
import com.consult.util.DocumentHandler;
import com.consult.util.JsonUtil;

@Service
public class CommonServiceImpl implements CommonService {

	Logger logger = Logger.getLogger(CommonServiceImpl.class);

	@Autowired
	CommonMapper mapper;

	@Autowired
	DocumentHandler docHandler;

	public String saveArea(String param) throws Exception {
		JSONObject paramJo = JSONObject.fromObject(param);

		if (!paramJo.containsKey("areas")) {
			returnJo("9999", "areas节点不存在！").toString();
		}
		if (JsonUtil.isBlank(paramJo.get("areas"))) {
			returnJo("9999", "areas节点不能为空！").toString();
		}

		JSONArray areas = paramJo.getJSONArray("areas");

		int allcount = mapper.deleteAreaAll();

		if (allcount <= 0) {
			returnJo("9999", "删除数据失败！").toString();
		}

		for (Object o : areas) {
			JSONObject jo = (JSONObject) o;
			ConsultConfigArea area = new ConsultConfigArea();
			area.setAreaCode(jo.getString("areaCode"));
			area.setAreaName(jo.getString("areaName"));
			area.setState(0);

			Map paramMap = new HashMap();
			paramMap.put("areaCode", jo.getString("areaCode"));
			paramMap.put("state", 0);
			List<ConsultConfigArea> areas1 = mapper.qryArea(paramMap);

			if (areas1.size() <= 0) {
				mapper.saveArea(area);
			}
		}

		return returnJo("0000", "合法区域新增成功！").toString();
	}

	public String deleteArea(String param) throws Exception {

		JSONObject paramJo = JSONObject.fromObject(param);

		if (!paramJo.containsKey("areas")) {
			returnJo("9999", "areas节点不存在！").toString();
		}
		if (JsonUtil.isBlank(paramJo.get("areas"))) {
			returnJo("9999", "areas节点不能为空！").toString();
		}

		JSONArray areas = paramJo.getJSONArray("areas");

		ArrayList areaCodes = new ArrayList<>();
		Map pM = new HashMap();

		for (Object o : areas) {
			JSONObject jo = (JSONObject) o;
			areaCodes.add(jo.getString("areaCode"));
		}
		pM.put("areaCodes", areaCodes.toArray());
		int count = mapper.deleteArea(pM);

		return returnJo("0000", "合法区域删除成功！").toString();

	}

	public String updateArea(String param) throws Exception {
		JSONObject paramJo = JSONObject.fromObject(param);

		if (!paramJo.containsKey("areas")) {
			returnJo("9999", "areas节点不存在！").toString();
		}
		if (JsonUtil.isBlank(paramJo.get("areas"))) {
			returnJo("9999", "areas节点不能为空！").toString();
		}

		JSONArray areas = paramJo.getJSONArray("areas");

		for (Object o : areas) {
			JSONObject jo = (JSONObject) o;
			ConsultConfigArea area = new ConsultConfigArea();
			area.setAreaCode(jo.getString("areaCode"));
			area.setAreaName(jo.getString("areaName"));
			area.setState(Integer.valueOf(jo.getString("state")));
			mapper.updateArea(area);
		}
		return returnJo("0000", "合法区域修改成功！").toString();
	}

	public String qryArea(String param) throws Exception {
		try {
			Map paramMap = null;
			if (!JsonUtil.isBlank(param)) {
				paramMap = JSON.parseObject(param, Map.class);
			} else {
				paramMap = new HashMap();
			}

			logger.info(paramMap);

			List<ConsultConfigArea> areas = mapper.qryArea(paramMap);

			JSONObject reJo = returnJo("0000", "成功");
			reJo.put("result", areas);
			return reJo.toString();
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}
	
	//出生日期字符串转化成Date对象  
    public  Date parse(String strDate) throws ParseException {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        return sdf.parse(strDate);  
    }  
  
    //由出生日期获得年龄  
    public  int getAge(Date birthDay) throws Exception {  
        Calendar cal = Calendar.getInstance();  
  
        if (cal.before(birthDay)) {  
            throw new IllegalArgumentException(  
                    "The birthDay is before Now.It's unbelievable!");  
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(birthDay);  
  
        int yearBirth = cal.get(Calendar.YEAR);  
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
  
        int age = yearNow - yearBirth;  
  
        if (monthNow <= monthBirth) {  
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    }

	public String checkIdCard(String param) throws Exception {

		JSONObject paramJo = JSONObject.fromObject(param);

		String psptId = paramJo.getString("psptId");

		logger.info(psptId);

		String area = psptId.substring(0, 6);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("areaCode", area);
		List<ConsultConfigArea> areaList = mapper.queryAreaByAreaCode(paramMap);

		String addr = paramJo.getString("addr");
		boolean isarea = false;

		if (JsonUtil.isBlank(addr)) {
			Map userMap = new HashMap();
			userMap.put("psptId", psptId);
			List<Map> users = mapper.queryUserByPsptId(userMap);
			addr = String.valueOf(users.get(0).get("ADDRESS"));
		}

//		for (ConsultConfigArea areaeach : areaList) {
//			if (addr.contains(areaeach.getAreaName().split("\\|")[0])
//					&& addr.contains(areaeach.getAreaName().split("\\|")[2])) {
//				isarea = true;
//				break;
//			}
//		}
		
		for (ConsultConfigArea areaeach : areaList) {
			
			if(areaeach.getAreaName().split("\\|")[0].contains("新疆")) {
				if (addr.contains("新疆")
						&& addr.contains(areaeach.getAreaName().split("\\|")[2])) {
					isarea = true;
					break;
				}
			} else if(areaeach.getAreaName().split("\\|")[0].contains("广西")) {
				if (addr.contains("广西")
						&& addr.contains(areaeach.getAreaName().split("\\|")[2])) {
					isarea = true;
					break;
				}
			} else if(areaeach.getAreaName().split("\\|")[0].contains("宁夏")) {
				if (addr.contains("宁夏")
						&& addr.contains(areaeach.getAreaName().split("\\|")[2])) {
					isarea = true;
					break;
				}
			} else if(areaeach.getAreaName().split("\\|")[0].contains("西藏")) {
				if (addr.contains("西藏")
						&& addr.contains(areaeach.getAreaName().split("\\|")[2])) {
					isarea = true;
					break;
				}
			} else {
				if (addr.contains(areaeach.getAreaName().split("\\|")[0])
						&& addr.contains(areaeach.getAreaName().split("\\|")[2])) {
					isarea = true;
					break;
				}
			}
		}

		if (!isarea) {
			logger.info(area);
			logger.info("不在合法征询区域内！");
			return returnJo("9999", "不在合法征询区域内！").toString();
		}
		
		int age = this.getAge(this.parse(psptId.substring(6, 10) + "-"
					+ psptId.substring(10, 12) + "-" + psptId.substring(12, 14)));
		
		logger.info("-------------------->该浆员的征询年龄为:" + age);
		
		if(age < 18 || age > 55) {
			logger.info(age);
			logger.info("年龄 = " + age + "岁，您的年龄不在合法的征询年龄内，合法征询年龄为18-55岁");
			return returnJo("9999", "您的年龄不在合法征询年龄内，合法征询年龄为18-55周岁！").toString();
		}

		// 2����Ա�ϴ���ѯ����14����ǰ
		paramMap.put("psptId", psptId);
//		List<ConsultRecord> records = mapper.queryConsultRecords(paramMap);
//
//		if (records.size() > 0) {
//
//			String activeTime = records.get(0).getActiveTime();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			logger.info(sdf.format(new Date()));
//			int daysBetween = daysBetween(activeTime, sdf.format(new Date()));
//			if (daysBetween <= 14) {
//				logger.info("两次征询的时间差小于14天，不允许征询！" + daysBetween);
//				return returnJo("9999", "两次征询的时间差小于14天，不允许征询！").toString();
//			}
//		}

		// ���潬Ա��Ϣ
		List<Map> users = mapper.queryUserByPsptId(paramMap);

		String isFlag = "1";
		if (users.size() <= 0) {
			isFlag = "0";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ConsultIdCardInfo userInfo = new ConsultIdCardInfo();
			userInfo.setPsptId(psptId);
			userInfo.setAddress(paramJo.getString("addr"));
			userInfo.setName(paramJo.getString("name"));
			userInfo.setNation(paramJo.getString("nation"));
			userInfo.setBirthday(psptId.substring(6, 10) + "-"
					+ psptId.substring(10, 12) + "-" + psptId.substring(12, 14));
			// userInfo.setBirthday(df.format(df.parse(psptId.substring(6,
			// 14))));

			String sex = psptId.substring(16, 17);
			if (Integer.parseInt(sex) % 2 == 0) {
				userInfo.setSex("1");
			} else {
				userInfo.setSex("0");
			}
			userInfo.setActiveTime(df.format(new Date()));
			
			if(JsonUtil.isBlank(paramJo.get("picAddr"))) {
				logger.info("picAddr不能为空！");
				return returnJo("9999", "picAddr不能为空！").toString();
			}
			
			userInfo.setPicture(paramJo.getString("picAddr"));

			mapper.saveUser(userInfo);
		}
		JSONObject reJo = returnJo("0000", "成功");
		reJo.put("isFlag", isFlag);

		return reJo.toString();
	}

	public String saveRecord(String param) throws Exception {
		
		JSONObject paramjo = JSONObject.fromObject(param);
		if(JsonUtil.isBlank(paramjo.get("autopic"))) {
			logger.info("autopic不能为空！");
			return returnJo("9999", "autopic不能为空！").toString();
		}

		ConsultRecord record = (ConsultRecord) JSONObject.toBean(
				JSONObject.fromObject(param), ConsultRecord.class);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		record.setActiveTime(sdf.format(new Date()));
		
		if (JsonUtil.isBlank(record.getPrintFlag())) {
			record.setPrintFlag("N");
		}
		record.setFingerprint(paramjo.getString("autopic"));

		int count = mapper.saveRecord(record);

		if (count <= 0) {
			return returnJo("9999", "保存失败!").toString();
		}

		// 插入征询计数表
		// 更新recordcount记录
		Map conutMap = new HashMap();
		conutMap.put("psptId", record.getPsptId());
		List<ConsultRecordCount> recos = mapper.queryRecordCount(conutMap);

		if (recos.size() <= 0) {
			ConsultRecordCount count1 = new ConsultRecordCount();
			count1.setPsptId(record.getPsptId());
			count1.setIsproduce(0);
			count1.setUnproduce(1);
			mapper.saveRecordCount(count1);
		} else {
			Map paramMap = new HashMap();
			Integer upproduce = recos.get(0).getUnproduce();
			upproduce = upproduce + Integer.valueOf(1);
			paramMap.put("psptId", record.getPsptId());
			paramMap.put("unproduce", upproduce);
			mapper.updateRecordCount(paramMap);
		}

		return returnJo("0000", "保存成功！").toString();
	}

	private JSONObject returnJo(String respCode, String respMsg) {
		JSONObject jo = new JSONObject();
		jo.put("respCode", respCode);
		jo.put("respMsg", respMsg);
		return jo;
	}

	/**
	 * ������������֮����������
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	private int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public String queryRecords(String param) {
		try {
			Map paramMap = JSON.parseObject(param, Map.class);

			logger.info(paramMap);

			paramMap.remove("beginPage");
			paramMap.remove("pageSize");
			List<ConsultRecord> records = mapper.queryRecords(paramMap);

			Map<String, ConsultRecord> repeatMap = new HashMap<String, ConsultRecord>();

			for (ConsultRecord record : records) {
				if (repeatMap.containsKey(record.getPsptId())) {

				} else {
					repeatMap.put(record.getPsptId(), record);
				}
			}

			if (repeatMap.size() > 1) {
				JSONObject reJo = returnJo("0000", "成功");

				JSONArray ja = new JSONArray();
				for (Map.Entry<String, ConsultRecord> entry : repeatMap
						.entrySet()) {
					JSONObject jo = new JSONObject();
					jo.put("psptId", entry.getKey());
					jo.put("name", entry.getValue().getName());
					ja.add(jo);
				}

				reJo.put("result", ja);
				reJo.put("mode", 1);
				reJo.put("sum", repeatMap.size());
				return reJo.toString();
			}

			JSONObject reJo = returnJo("0000", "成功");
			reJo.put("result",
					mapper.queryRecords(JSON.parseObject(param, Map.class)));
			reJo.put("mode", 0);

			// paramMap.remove("beginPage");
			// paramMap.remove("pageSize");
			// List<ConsultRecord> sumRecords = mapper.queryRecords(paramMap);

			reJo.put("sum", records.size());
			return reJo.toString();
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	public String createWord(String param, HttpServletRequest request)
			throws Exception {
		JSONObject paramJo = JSONObject.fromObject(param);

		String wordType = paramJo.getString("wordType");

		if ("0".equals(wordType)) {

			if (!paramJo.containsKey("operType")) {
				logger.info("operType参数不存在！");
				return returnJo("9999", "operType参数不存在！").toString();
			}
			if (JsonUtil.isBlank(paramJo.get("operType"))) {
				logger.info("operType参数不能为空！");
				return returnJo("9999", "operType参数不能为空！").toString();
			}
			String operType = paramJo.getString("operType");

			if ("0".equals(operType)) {
				List<ConsultRecordCount> recos = mapper
						.queryRecordCount(new HashMap());
				if (recos.size() <= 0) {
					logger.info("失败！");
					return returnJo("9999", "失败！").toString();
				}

				List<ConsultRecordCount> canPrint = new ArrayList<ConsultRecordCount>();
				for (ConsultRecordCount count : recos) {
					if (count.getUnproduce() >= 26) {
						canPrint.add(count);
					}
				}

				if (canPrint.size() > 0) {
					List<String> fileNames = new ArrayList<String>();
					for (ConsultRecordCount count : canPrint) {
						String fileName = printAndUpdate(count.getPsptId(),
								request, null, "0",false);
						if (JsonUtil.isJson(fileName)
								&& "9999".equals(JSONObject
										.fromObject(fileName).getString(
												"respCode"))) {
							logger.info(fileName);
							return fileName;
						}
						fileNames.add(request.getScheme() + "://"
								+ request.getServerName() + ":"
								+ request.getServerPort()
								+ request.getContextPath() + "/"
								+ "word/guding/" + fileName);
					}

					logger.info("生成word成功！");
					logger.info("wordAddr" + fileNames);
					JSONObject reJo = returnJo("0000", "生成word成功！");
					reJo.put("wordAddr", fileNames);
					return reJo.toString();

				} else {
					logger.info("浆员中没有征询记录超过26条的！");
					return returnJo("9999", "浆员中没有征询记录超过26条的！").toString();
				}
			} else if ("1".equals(operType)) {
				if (!paramJo.containsKey("psptId")) {
					logger.info("psptId参数不存在！");
					return returnJo("9999", "psptId参数不存在！").toString();
				}
				if (JsonUtil.isBlank(paramJo.get("psptId"))) {
					logger.info("psptId参数不能为空！");
					return returnJo("9999", "psptId参数不能为空！").toString();
				}

				if (!paramJo.containsKey("records")) {
					logger.info("records参数不存在！");
					return returnJo("9999", "records参数不存在！").toString();
				}
				if (JsonUtil.isBlank(paramJo.get("records"))) {
					logger.info("records参数不能为空！");
					return returnJo("9999", "records参数不能为空！").toString();
				}

				for (Object o : paramJo.getJSONArray("records")) {
					JSONObject jo = (JSONObject) o;
					if (!jo.containsKey("id")) {
						logger.info("id必须传且不能为空！");
						return returnJo("9999", "id必须传且不能为空！").toString();
					}
					if (JsonUtil.isBlank(jo.get("id"))) {
						logger.info("id必须传且不能为空！");
						return returnJo("9999", "id必须传且不能为空！").toString();
					}
				}

				String psptId = paramJo.getString("psptId");

				JSONArray recordsJa = paramJo.getJSONArray("records");

				String fileName = printAndUpdate(psptId, request, recordsJa,
						"1",false);

				if (JsonUtil.isJson(fileName)
						&& "9999".equals(JSONObject.fromObject(fileName)
								.getString("respCode"))) {
					logger.info(fileName);
					return fileName;
				}

				logger.info("生成word成功！");
				logger.info("wordAddr:" + request.getScheme() + "://"
						+ request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath()
						+ "/word/guding" + "/" + fileName);
				JSONObject reJo = returnJo("0000", "生成word成功！");
				reJo.put(
						"wordAddr",
						request.getScheme() + "://" + request.getServerName()
								+ ":" + request.getServerPort()
								+ request.getContextPath() + "/word/guding/"
								+ fileName);
				return reJo.toString();
			}

		} else if ("1".equals(wordType)) {
			if (!paramJo.containsKey("psptId")) {
				logger.info("psptId参数不存在！");
				return returnJo("9999", "psptId参数不存在！").toString();
			}
			if (JsonUtil.isBlank(paramJo.get("psptId"))) {
				logger.info("psptId参数不能为空！");
				return returnJo("9999", "psptId参数不能为空！").toString();
			}
			String psptId = paramJo.getString("psptId");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("psptId", psptId);
			List<Map> users = this.queryUserInfo(paramMap);
			if (users.size() <= 0) {
				logger.info("根据身份证号码查询不到该浆员信息！");
				return returnJo("9999", "根据身份证号码查询不到该浆员信息！").toString();
			}
			Map dataMap = newOldDataMap(users.get(0));
			if (dataMap == null) {
				logger.info("生成文档失败");
				return returnJo("9999", "生成文档失败！").toString();
			}

			File newoldWordpath = new File("D:" + File.separator + "static");
			if (!newoldWordpath.exists()) {
				newoldWordpath.mkdir();
			}
			File xinlaopath = new File(newoldWordpath.getAbsoluteFile()
					+ File.separator + "word");
			if (!xinlaopath.exists()) {
				xinlaopath.mkdir();
			}
			File xinlaopath1 = new File(xinlaopath.getAbsoluteFile()
					+ File.separator + "xinlao");
			if (!xinlaopath1.exists()) {
				xinlaopath1.mkdir();
			}
			String wordName = "固定供血浆者采浆前健康征询表-身份证(" + psptId + ")"
					+ "时间(" + new Date().getTime() + ")" + ".doc";
			String fileName = xinlaopath1.getAbsolutePath() + File.separator
					+ wordName;
			docHandler.createDoc(dataMap, fileName, "1");

			logger.info("生成word成功！");
			logger.info("wordAddr:" + request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/word/xinlao/" + wordName);
			JSONObject reJo = returnJo("0000", "生成word成功！");
			reJo.put(
					"wordAddr",
					request.getScheme() + "://" + request.getServerName() + ":"
							+ request.getServerPort()
							+ request.getContextPath() + "/word/xinlao/"
							+ wordName);
			return reJo.toString();
		}
		return null;
	}

	@SuppressWarnings("all")
	private String printAndUpdate(String psptId, HttpServletRequest request,
			JSONArray ja, String operType,boolean isPrintAll) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("psptId", psptId);
		List<Map> users = this.queryUserInfo(paramMap);
		if (users.size() <= 0) {
			logger.info("根据身份证号码查询不到该浆员信息！");
			return returnJo("9999", "根据身份证号码查询不到该浆员信息！").toString();
		}
		Map dataMap = this.gudingDataMap(users.get(0), ja,isPrintAll);

		if (dataMap == null) {
			return returnJo("9999", "生成文档失败！").toString();
		}
		if (dataMap.get("dataMap") == null) {
			return returnJo("9999", "生成文档失败！").toString();
		}

		File gudingWordpath = new File("D:" + File.separator + "static");
		if (!gudingWordpath.exists()) {
			gudingWordpath.mkdir();
		}
		File gudingWordpathpath = new File(gudingWordpath.getAbsoluteFile()
				+ File.separator + "word");
		if (!gudingWordpathpath.exists()) {
			gudingWordpathpath.mkdir();
		}
		File gudingWordpathpath1 = new File(
				gudingWordpathpath.getAbsoluteFile() + File.separator
						+ "guding");
		if (!gudingWordpathpath1.exists()) {
			gudingWordpathpath1.mkdir();
		}

		String wordName = "固定供血浆者采浆前健康征询表-身份证(" + psptId + ")" + "时间("
				+ new Date().getTime() + ")" + ".doc";
		String fileName = gudingWordpathpath1.getAbsolutePath()
				+ File.separator + wordName;

		docHandler.createDoc((Map) dataMap.get("dataMap"), fileName, "0");

		if ("1".equals(operType)) {
			return wordName;
		}

		// 更新records记录
		List<ConsultRecord> records = (List<ConsultRecord>) dataMap
				.get("records");
		if (records.size() > 0) {
			ArrayList ids = new ArrayList<>();
			for (ConsultRecord recird : records) {
				ids.add(recird.getId());
			}
			Map pM = new HashMap();
			pM.put("ids", ids.toArray());
			int count = mapper.updateRecords(pM);
			if (count <= 0) {
				return returnJo("9999", "records更新失败！").toString();
			}
			// 更新recordcount记录
			Map conutMap = new HashMap();
			conutMap.put("psptId", psptId);
			List<ConsultRecordCount> recos = mapper.queryRecordCount(conutMap);
			if (recos.size() <= 0) {
				return returnJo("9999", "recordcount记录为空！").toString();
			}

			Integer isproduce = recos.get(0).getIsproduce();
			Integer unproduce = recos.get(0).getUnproduce();

			if (unproduce > 0) {
				Map countMap = new HashMap();
				countMap.put("isproduce", isproduce + records.size());
				countMap.put("unproduce", (unproduce - records.size()) < 0 ? 0
						: (unproduce - records.size()));
				countMap.put("psptId", psptId);

				int upcount = mapper.updateRecordCount(countMap);
				if (upcount <= 0) {
					return returnJo("9999", "更新recordcount表失败！").toString();
				}
			}
		}
		return wordName;
	}

	private Map newOldDataMap(Map user) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name", user.get("NAME"));

		String sex = null;
		if (user.get("SEX").equals("0")) {
			sex = "男";
		} else if (user.get("SEX").equals("1")) {
			sex = "女";
		} else {
			sex = "不明";
		}

		if (JsonUtil.isBlank(user.get("BIRTHDAY"))) {
			return null;
		}

		String birthDay = String.valueOf(user.get("BIRTHDAY"));

		dataMap.put("sex", sex);
		dataMap.put("mingzu", user.get("NATION"));
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = df.format(new Date());
		dataMap.put("recordNo", date);
		
		

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH);// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日

		dataMap.put("year", birthDay.split("-")[0]);
		dataMap.put("month", birthDay.split("-")[1]);
		dataMap.put("day", birthDay.split("-")[2]);

		dataMap.put("psptId", user.get("PSPTID"));
		dataMap.put("cardId", user.get("PSPTID"));
		dataMap.put("addr", user.get("ADDRESS"));

		List<ConsultContent> contents = mapper.queryContent(new HashMap());

		if (contents.size() <= 0) {
			return null;
		}
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		for (ConsultContent con : contents) {
			if (con.getType().equals("0")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("index", con.getItemIndex());
				map.put("content", con.getContent());
				list1.add(map);
			} else if (con.getType().equals("1")) {
				Map<String, Object> map = new HashMap<String, Object>();
				if(user.get("SEX").equals("0") && (con.getItemIndex() == 2)) {
					continue;
				}
				if(user.get("SEX").equals("0") && (con.getItemIndex() != 1)) {
					con.setItemIndex(con.getItemIndex() - 1);
				}
				map.put("index", con.getItemIndex());
				map.put("content", con.getContent());
				list2.add(map);
			}
		}

		dataMap.put("table1", list1);
		dataMap.put("table2", list2);

		Map paramMap = new HashMap();
		paramMap.put("psptId", user.get("PSPTID"));
		List<ConsultRecord> records = mapper.queryConsultRecords(paramMap);

		if (records.size() <= 0) {
			return null;
		}

		dataMap.put("year1", records.get(0).getActiveTime().split("-")[0]);
		dataMap.put("month1", records.get(0).getActiveTime().split("-")[1]);
		dataMap.put("day1", records.get(0).getActiveTime().split("-")[2]);
		dataMap.put("image1", getImageStr(records.get(0).getFingerprint()));
		dataMap.put("image2", getImageStr(user.get("PICTURE").toString()));
		
		Map paramMap1 = new HashMap();
		paramMap1.put("ids",
				new String[] { String.valueOf(user.get("PSPTID")) });
		List<ConsultContract> contracts = mapper.qryContracts(paramMap1);

		if (contracts.size() > 0) {
			dataMap.put("archives", contracts.get(0).getContractCode());
		} else {
			dataMap.put("archives", "");
		}
		
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activeTime", records.get(0).getActiveTime());

		if(records.get(0).getIspass() != null) {
			if (records.get(0).getIspass().equals("TYGJ")) {
				map.put("agree", "同意");
				map.put("temporary", "");
				map.put("permanent", "");
				
				map.put("recommand", "同意供浆");
			} else if (records.get(0).getIspass().equals("ZSJJ")) {
				map.put("agree", "");
				map.put("temporary", "暂时");
				map.put("permanent", "");
				
				map.put("recommand", "暂时拒绝");
			} else if (records.get(0).getIspass().equals("YJJJ")) {
				map.put("agree", "");
				map.put("temporary", "");
				map.put("permanent", "永久");
				
				map.put("recommand", "永久拒绝");
			}
		} else {
			map.put("agree", "");
			map.put("temporary", "");
			map.put("permanent", "");
			
			map.put("recommand", "");
		}
		//recommand

		map.put("remark", records.get(0).getRemark());
		map.put("image3", getImageStr(records.get(0).getAutograph()));
		if(records.get(0).getDocautograph() != null) {
			map.put("image4", getImageStr(records.get(0).getDocautograph()));
		} else {
			map.put("image4","");
		}
		list3.add(map);
		
		dataMap.put("table3", list3);
		dataMap.put("image3", getImageStr(records.get(0).getAutograph()));
		
		JSONObject lineupJo = new JSONObject();
		lineupJo.put("psptId", user.get("PSPTID"));
		String lineUpRe = createLineUp(lineupJo.toString());
		JSONObject re = JSONObject.fromObject(lineUpRe);
		if(re.getString("respCode").equals("0000")) {
			dataMap.put("orderindex", Integer.parseInt(re.getString("index")) + 1);
		} else {
			dataMap.put("orderindex", "11");
		}
		
		return dataMap;
	}

	private Map gudingDataMap(Map user, JSONArray ja,boolean isPrintAll) throws Exception{

		Map<String, Object> dataMap = new HashMap<String, Object>();
		String sex = null;
		if (user.get("SEX").equals("0")) {
			sex = "男";
		} else if (user.get("SEX").equals("1")) {
			sex = "女";
		} else {
			sex = "不明";
		}

		dataMap.put("name", user.get("NAME"));
		dataMap.put("sex", sex);

		Map paramMap1 = new HashMap();
		paramMap1.put("ids",
				new String[] { String.valueOf(user.get("PSPTID")) });
		List<ConsultContract> contracts = mapper.qryContracts(paramMap1);

		if (contracts.size() > 0) {
			dataMap.put("archives", contracts.get(0).getContractCode());
		} else {
			dataMap.put("archives", "");
		}

		List<ConsultContent> contents = mapper.queryContent(new HashMap());

		if (contents.size() <= 0) {
			return null;
		}

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		for (ConsultContent con : contents) {
			if (con.getType().equals("0")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("index", con.getItemIndex());
				map.put("content", con.getContent());
				list1.add(map);
			} else if (con.getType().equals("1")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("index", con.getItemIndex());
				map.put("content", con.getContent());
				list2.add(map);
			}
		}

		dataMap.put("table1", list1);
		dataMap.put("table2", list2);

		List<ConsultRecord> records = null;
		if (ja != null) {
			records = (List<ConsultRecord>) JSONArray.toList(ja,
					ConsultRecord.class);
		} else {
			// 获取征询记录
			Map paramMap = new HashMap();
			paramMap.put("psptId", user.get("PSPTID"));
			records = mapper.queryConsultRecords(paramMap);

			if (records.size() > 0) {
				Collections.reverse(records);
			}
		}

		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
		if(!isPrintAll) {
		if (records.size() > 26) {
			for (int i = 0; i < 26; i++) {
				// for (ConsultRecord record : records) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("activeTime", records.get(i).getActiveTime());
				if(records.get(i).getIspass() != null) {
					if (records.get(i).getIspass().equals("TYGJ")) {
						map.put("agree", "同意");
						map.put("temporary", "");
						map.put("permanent", "");
					} else if (records.get(i).getIspass().equals("ZSJJ")) {
						map.put("agree", "");
						map.put("temporary", "暂时");
						map.put("permanent", "");
					} else if (records.get(i).getIspass().equals("YJJJ")) {
						map.put("agree", "");
						map.put("temporary", "");
						map.put("permanent", "永久");
					}
				} else {
					map.put("agree", "");
					map.put("temporary", "");
					map.put("permanent", "");
				}

				map.put("remark", records.get(i).getRemark());
				map.put("image1", getImageStr(records.get(i).getAutograph()));
				if(records.get(i).getDocautograph() != null) {
					map.put("image2", getImageStr(records.get(i).getDocautograph()));
				} else {
					map.put("image2", "");
				}
				list3.add(map);
				// }
			}
		} else {
			for (ConsultRecord record : records) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("activeTime", record.getActiveTime());
				if(record.getIspass() != null) {
					if (record.getIspass().equals("TYGJ")) {
						map.put("agree", "同意");
						map.put("temporary", "");
						map.put("permanent", "");
					} else if (record.getIspass().equals("ZSJJ")) {
						map.put("agree", "");
						map.put("temporary", "暂时");
						map.put("permanent", "");
					} else if (record.getIspass().equals("YJJJ")) {
						map.put("agree", "");
						map.put("temporary", "");
						map.put("permanent", "永久");
					}
				} else {
					map.put("agree", "");
					map.put("temporary", "");
					map.put("permanent", "");
				}

				map.put("remark", record.getRemark());
				map.put("image1", getImageStr(record.getAutograph()));
				if(record.getDocautograph() != null) {
					map.put("image2", getImageStr(record.getDocautograph()));
				} else {
					map.put("image2", "");
				}
				list3.add(map);
			}
		}
		} else {
			for (ConsultRecord record : records) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("activeTime", record.getActiveTime());
				if(record.getIspass() != null) {
					if (record.getIspass().equals("TYGJ")) {
						map.put("agree", "同意");
						map.put("temporary", "");
						map.put("permanent", "");
					} else if (record.getIspass().equals("ZSJJ")) {
						map.put("agree", "");
						map.put("temporary", "暂时");
						map.put("permanent", "");
					} else if (record.getIspass().equals("YJJJ")) {
						map.put("agree", "");
						map.put("temporary", "");
						map.put("permanent", "永久");
					}
				} else {
					map.put("agree", "");
					map.put("temporary", "");
					map.put("permanent", "");
				}

				map.put("remark", record.getRemark());
				map.put("image1", getImageStr(record.getAutograph()));
				if(record.getDocautograph() != null) {
					map.put("image2", getImageStr(record.getDocautograph()));
				} else {
					map.put("image2", "");
				}
				list3.add(map);
			}
		}
		dataMap.put("table3", list3);

		Map rePa = new HashMap();
		rePa.put("dataMap", records.size() == 0 ? null : dataMap);
		if(!isPrintAll) {
		if (records.size() > 26) {
			rePa.put("records", records.subList(0, 26));
		} else {
			rePa.put("records", records);
		}
		} else {
			rePa.put("records", records);
		}

		return rePa;
	}

	public List<Map> queryUserInfo(Map param) {
		return mapper.queryUserByPsptId(param);
	}

	private static String getImageStr(String path) {

		path = "D:/static/" + path.substring(path.indexOf("image"));

		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

	public String createLineUp(String param) {

		try {
			JSONObject paramJo = JSONObject.fromObject(param);
			String psptId = paramJo.getString("psptId");

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Map paramMap = new HashMap();
			paramMap.put("activeTime", df.format(new Date()));

			List<ConsultRecord> records = mapper.queryRecordshaveH(paramMap);

			if (records.size() <= 0) {
				return returnJo("9999", "今天没有该浆员的征询记录！").toString();
			}

			Integer index = null;
			String activeTime = null;
			for (ConsultRecord record : records) {
				if (psptId.equals(record.getPsptId())) {
					index = records.indexOf(record);
					activeTime = record.getActiveTime();
					break;
				}
			}

			if (index == null) {
				return returnJo("9999", "今天没有该浆员的征询记录！").toString();
			}

			JSONObject reJo = returnJo("0000", "生成排序成功！");
			reJo.put("index", index);
			reJo.put("activeTime", activeTime);
			return reJo.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	public String checkIsNew(String param) throws Exception {

		Map paramMap = JSON.parseObject(param, Map.class);

		List<Map> users = mapper.queryUserByPsptId(paramMap);

		String isFlag = "1";
		String name = "";
		if (users.size() <= 0) {
			isFlag = "0";
		} else {
			name = String.valueOf(users.get(0).get("NAME"));
		}

		JSONObject reJo = returnJo("0000", "成功");
		reJo.put("isFlag", isFlag);
		reJo.put("name", name);
		return reJo.toString();
	}

	public String saveConstract(List<Map<String, String>> param)
			throws Exception {

		if (param == null || param.size() <= 0) {
			return returnJo("9999", "excel内容为空！").toString();
		}

		List<String> psptIds = new ArrayList<String>();
		for (Map<String, String> map : param) {
			psptIds.add(map.get("psptId"));
		}

		Map paramMap = new HashMap();
		paramMap.put("ids", psptIds.toArray());
		List<ConsultContract> contracts = mapper.qryContracts(paramMap);

		List<Map<String, String>> removeList = new ArrayList<Map<String, String>>();
		if (contracts.size() > 0) {
			for (Map<String, String> map : param) {
				for (ConsultContract contract : contracts) {
					if (contract.getPsptId().equals(map.get("psptId"))) {
						removeList.add(map);
					}
				}
			}
		}

		if (removeList.size() > 0) {
			param.removeAll(removeList);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ConsultContract> cons = new ArrayList<ConsultContract>();
		for (Map<String, String> map : param) {
			ConsultContract contract = new ConsultContract();
			contract.setActiveTime(sdf.format(new Date()));
			contract.setState(0);
			contract.setPsptId(map.get("psptId"));
			contract.setContractCode(map.get("no"));
			cons.add(contract);
		}

		int count = -1;
		if (cons.size() > 0) {
			count = mapper.saveContracts(cons);
		}

		if (count <= 0) {
			return returnJo("0000", "excel data already exist！").toString();
		}

		return returnJo("0000", "succsess").toString();
	}

	public String doctorQueryUser(String param) throws Exception {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map paramMap = new HashMap();
		paramMap.put("activeTime", df.format(new Date()));
		paramMap.put("handState", "N");

		List<ConsultRecord> records = mapper.queryRecordshaveH(paramMap);
		
		if(records.size() <= 0) {
			logger.info("今天没有浆员的征询记录！");
			return returnJo("9999","今天没有浆员的征询记录！").toString();
		}
		
		//修改最早排队的征询记录为正在处理中的状态B
		Map upateParam = new HashMap();
		upateParam.put("handState", "B");
		upateParam.put("id", records.get(0).getId());
		if(mapper.updateConsultRecord(upateParam) <= 0 ) {
			logger.info("cousult_record.hand_state更新失败！");
			return returnJo("9999","cousult_record.hand_state更新失败！").toString();
		}
		
		ConsultRecord record = records.get(0);
		JSONObject consultRecord = JSONObject.fromObject(record);
		
		Map userParam = new HashMap();
		userParam.put("psptId", record.getPsptId());
		List<Map> users = mapper.queryUserByPsptId(userParam);
		
		JSONObject userInfo = new JSONObject();
		userInfo.put("psptId", record.getPsptId());
		userInfo.put("name", users.get(0).get("NAME"));
		userInfo.put("birthday", users.get(0).get("BIRTHDAY1"));
		userInfo.put("sex", users.get(0).get("SEX"));
		userInfo.put("address", users.get(0).get("ADDRESS"));
		userInfo.put("activeTime", users.get(0).get("ACTIVETIME1"));
		userInfo.put("picture", users.get(0).get("PICTURE"));
		userInfo.put("nation", users.get(0).get("NATION"));
		
		JSONObject retJo = returnJo("0000","成功！");
		retJo.put("consultRecord", consultRecord);
		retJo.put("userInfo", userInfo);
		retJo.put("lineupCount", records.size() - 1);
		
		JSONObject lineupJo = new JSONObject();
		lineupJo.put("psptId", record.getPsptId());
		String lineUpRe = createLineUp(lineupJo.toString());
		JSONObject re = JSONObject.fromObject(lineUpRe);
		if(re.getString("respCode").equals("0000")) {
			retJo.put("index", re.getString("index"));
		} else {
			retJo.put("index", "11");
		}
		
		Map paramMap1 = new HashMap();
		paramMap1.put("ids",
				new String[] {record.getPsptId()});
		List<ConsultContract> contracts = mapper.qryContracts(paramMap1);

		if (contracts.size() > 0) {
			retJo.put("archives", contracts.get(0).getContractCode());
		} else {
			retJo.put("archives", "");
		}
		
		retJo.put("picture", record.getFingerprint());
		
		logger.info(retJo);
		return retJo.toString();
	}
	
	private List<String> repeat(List<ConsultRecord> records) {
		
		Map<String,Integer> repeatMap = new HashMap<String,Integer>();
		
		for(ConsultRecord record : records) {
			if(!repeatMap.containsKey(record.getPsptId())) {
				repeatMap.put(record.getPsptId(), 1);
			} else {
				repeatMap.put(record.getPsptId(), repeatMap.get(record.getPsptId()) + 1);
			}
		}
		
		List<String> keys = new ArrayList<String>();
		for(String key : repeatMap.keySet()) {
			keys.add(key);
		}
		
		return keys;
	}

	public String printAllYear(String param, HttpServletRequest request) throws Exception {
		
		Map paramMap = new HashMap();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar a = Calendar.getInstance();
		paramMap.put("activeTime", a.get(Calendar.YEAR) + "-01" + "-01");
		paramMap.put("printFlag", "N");
		List<ConsultRecord> records = mapper.queryRecords(paramMap);
		
		if(records.size() <= 0 ) {
			return returnJo("9999", "本年没有浆员的征询记录！").toString();
		}
		
		List<String> psptIds = repeat(records);
		
		
		List<String> subpsptIds = psptIds.size() >= 10 ? psptIds.subList(0, 9) : psptIds;
		
		List<String> wordAddrList = new ArrayList<String>();
		
		for(String psptId : subpsptIds) {
			String fileName = printAndUpdate(psptId,
					request, null, "0",true);
//			String psptId = record.getPsptId();
//			Map<String, String> paramMap1 = new HashMap<String, String>();
//			paramMap1.put("psptId", psptId);
//			List<Map> users = this.queryUserInfo(paramMap1);
//			if (users.size() <= 0) {
//				logger.info("根据身份证号码查询不到该浆员信息！");
//				return returnJo("9999", "根据身份证号码查询不到该浆员信息！").toString();
//			}
//			Map dataMap = newOldDataMap(users.get(0));
//			if (dataMap == null) {
//				logger.info("生成文档失败");
//				return returnJo("9999", "生成文档失败！").toString();
//			}
//	
//			File newoldWordpath = new File("D:" + File.separator + "static");
//			if (!newoldWordpath.exists()) {
//				newoldWordpath.mkdir();
//			}
//			File xinlaopath = new File(newoldWordpath.getAbsoluteFile()
//					+ File.separator + "word");
//			if (!xinlaopath.exists()) {
//				xinlaopath.mkdir();
//			}
//			File xinlaopath1 = new File(xinlaopath.getAbsoluteFile()
//					+ File.separator + "xinlao");
//			if (!xinlaopath1.exists()) {
//				xinlaopath1.mkdir();
//			}
//			String wordName = "固定供血浆者采浆前健康征询表-身份证(" + psptId + ")"
//					+ "时间(" + new Date().getTime() + ")" + ".doc";
//			String fileName = xinlaopath1.getAbsolutePath() + File.separator
//					+ wordName;
//			docHandler.createDoc(dataMap, fileName, "1");
//	
//			logger.info("生成word成功！");
//			logger.info("wordAddr:" + request.getScheme() + "://"
//					+ request.getServerName() + ":" + request.getServerPort()
//					+ request.getContextPath() + "/word/xinlao/" + wordName);
//			wordAddrList.add(request.getScheme() + "://" + request.getServerName() + ":"
//							+ request.getServerPort()
//							+ request.getContextPath() + "/word/xinlao/"
//							+ wordName);
			logger.info(request.getScheme() + "://"
					+ request.getServerName() + ":"
					+ request.getServerPort()
					+ request.getContextPath() + "/"
					+ "word/guding/" + fileName);
			wordAddrList.add(request.getScheme() + "://"
					+ request.getServerName() + ":"
					+ request.getServerPort()
					+ request.getContextPath() + "/"
					+ "word/guding/" + fileName);
		}
		
		//征询记录更新成Y
		Map pM = new HashMap();
		pM.put("ids", subpsptIds.toArray());
		int count = mapper.updateRecordsByPsptId(pM);
		if (count <= 0) {
			logger.info("records更新失败！");
			return returnJo("9999", "records更新失败！").toString();
		}
		JSONObject reJo = returnJo("0000", "生成word成功！");
		reJo.put("wordAddr", wordAddrList);
		reJo.put("isContinue", psptIds.size() > 10 ? "Y" : "N");
		logger.info(reJo);
		return reJo.toString();
	}
	
	public String doctorConfirm(String param) throws Exception {
		
		if(JsonUtil.isBlank(param)) {
			return returnJo("9999","param不能为空！").toString();
		}
		JSONObject paramJo = JSONObject.fromObject(param);
		if(!paramJo.containsKey("id") || JsonUtil.isBlank(paramJo.get("id"))) {
			return returnJo("9999","param.id不能为空！").toString();
		}
		if(!paramJo.containsKey("ispass") || JsonUtil.isBlank(paramJo.get("ispass"))) {
			return returnJo("9999","param.ispass不能为空！").toString();
		}
		if(!paramJo.containsKey("docautograph") || JsonUtil.isBlank(paramJo.get("docautograph"))) {
			return returnJo("9999","param.docautograph不能为空！").toString();
		}
		
		Map paramMap = new HashMap();
		paramMap.put("ispass", paramJo.getString("ispass"));
		paramMap.put("docautograph", paramJo.getString("docautograph"));
		paramMap.put("id", paramJo.getString("id"));
		//把hand_state状态改为C，已处理完成
		paramMap.put("handState", "C");
		if(mapper.updateConsultRecord(paramMap) <= 0) {
			return returnJo("9999","ConsultRecord更新失败，医生确认失败！").toString();
		}
		
		return returnJo("0000","成功！").toString();
	}

	public static void main(String[] args) {
		Calendar a = Calendar.getInstance();
		System.out.print(a.get(Calendar.YEAR));
	}
}
