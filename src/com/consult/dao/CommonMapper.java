package com.consult.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.consult.bean.ConsultConfigArea;
import com.consult.bean.ConsultContent;
import com.consult.bean.ConsultContract;
import com.consult.bean.ConsultIdCardInfo;
import com.consult.bean.ConsultRecord;
import com.consult.bean.ConsultRecordCount;

@Repository
public interface CommonMapper {

	int saveArea(ConsultConfigArea area);
	
	int deleteArea(Map param);
	
	int deleteAreaAll();
	
	int updateArea(ConsultConfigArea area);
	
	List<ConsultConfigArea> queryAreaByAreaCode(Map param);
	
	List<ConsultRecord> queryConsultRecords(Map param);
	
	List<Map> queryUserByPsptId(Map param);
	
	int saveUser(ConsultIdCardInfo record);
	
	int saveRecord(ConsultRecord record);
	
	int saveRecordCount(ConsultRecordCount recordCount);
	
	List<ConsultRecord> queryRecords(Map param);
	
	List<ConsultRecord> queryRecordshaveH(Map param);
	
	List<ConsultContent> queryContent(Map param);
	
	int updateRecords(Map param);
	
	int updateRecordsByPsptId(Map param);
	
	List<ConsultRecordCount> queryRecordCount(Map param);
	
	int updateRecordCount(Map param);
	
	List<ConsultConfigArea> qryArea(Map param);
	
	List<ConsultContract> qryContracts(Map param);
	
	int saveContracts(List<ConsultContract> contracts);
	
	int updateConsultRecord(Map param);
}
