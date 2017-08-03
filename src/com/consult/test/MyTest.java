package com.consult.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.consult.listen.MyEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MyTest implements ApplicationContextAware {
    
    @Autowired
    Student s;
    
    ApplicationContext context;
    
    @Override
	public void setApplicationContext(ApplicationContext paramApplicationContext)
			throws BeansException {
		this.context = paramApplicationContext;
	}

	@Test
    public void test1() {
        System.out.println("==================");
        System.out.println(s.getUsername());
        System.out.println(s.getPassword());
        System.out.println(s.getField1());
    }
	
	@Test
	public void test2(){
		MyEvent event = new MyEvent("source","param1","param2");
		context.publishEvent(event);
	}
}
