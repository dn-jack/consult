package com.consult.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MyTest {
    
    @Autowired
    Student s;
    
    @Test
    public void test1() {
        System.out.println("==================");
        System.out.println(s.getUsername());
        System.out.println(s.getPassword());
        System.out.println(s.getField1());
    }
}
