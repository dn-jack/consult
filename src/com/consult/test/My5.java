package com.consult.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

public class My5 implements BeanFactoryPostProcessor, PriorityOrdered {
    
    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("My5 " + 0);
        BeanDefinition bd = beanFactory.getBeanDefinition("myStudent");
        MutablePropertyValues mpv = bd.getPropertyValues();
        mpv.addPropertyValue("field1", "field1-value2");
    }
    
    public int getOrder() {
        return 0;
    }
    
}
