package com.consult.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.consult.bean.ConsultConfigArea;
import com.consult.dao.CommonMapper;
import com.consult.service.TransationService;

@Service
public class TransationServiceImpl implements TransationService {
	
	@Autowired
	CommonMapper mapper;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void test() {
		
			System.out.print("fjdlsajflkdsglkfdgewopriop");
			ConsultConfigArea area = new ConsultConfigArea();
			area.setAreaCode("ww");
			area.setAreaName("ww");
			area.setState(0);
			mapper.saveArea(area);
			throw new RuntimeException("回滚！");
	}

}
