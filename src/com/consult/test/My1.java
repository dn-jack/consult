package com.consult.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;

public class My1 implements BeanDefinitionRegistryPostProcessor,
        PriorityOrdered {
    
    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("My1 " + 0);
        MutablePropertyValues mpv = registry.getBeanDefinition("myStudent").getPropertyValues();
        mpv.addPropertyValue("username", "jack-vip1");
    }
    
    public int getOrder() {
        return 0;
    }
    
}
