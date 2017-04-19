package com.consult.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



public interface CommonService {
	
	public String saveArea(String param) throws Exception;
	
	public String deleteArea(String param) throws Exception;
	
	public String updateArea(String param) throws Exception;
	
	public String qryArea(String param) throws Exception;
	
	public String checkIdCard(String param) throws Exception;
	
	public String checkIsNew(String param) throws Exception;
	
	public String saveRecord(String param) throws Exception;
	
	public String queryRecords(String param) throws Exception;
	
	public String createWord(String param,HttpServletRequest request) throws Exception;
	
	public List<Map> queryUserInfo(Map param) throws Exception;
	
	public String createLineUp(String param) throws Exception;
	
	public String doctorQueryUser(String param) throws Exception;
	
	public String printAllYear(String param, HttpServletRequest request) throws Exception;
	
	public String doctorConfirm(String param) throws Exception;
	
	public String saveConstract(List<Map<String, String>> param) throws Exception;
}
