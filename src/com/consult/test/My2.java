package com.consult.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;

public class My2 implements BeanDefinitionRegistryPostProcessor,
        PriorityOrdered {
    
    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("My2 " + 1);
        BeanDefinition bd = registry.getBeanDefinition("myStudent");
        MutablePropertyValues mpv = bd.getPropertyValues();
        mpv.addPropertyValue("field1", "field1-value1");
    }
    
    public int getOrder() {
        return 1;
    }
    
}
