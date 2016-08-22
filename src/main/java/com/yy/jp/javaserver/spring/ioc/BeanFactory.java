package com.yy.jp.javaserver.spring.ioc;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yy.jp.javaserver.exceptions.ServerInitException;


public class BeanFactory {
	private static final Logger LOG = Logger.getLogger(BeanFactory.class);
	
	private static ApplicationContext applicationContext;
	
	public static void init(String springConfig){
		LOG.info("加载spring配置："+springConfig);
		applicationContext = new ClassPathXmlApplicationContext(springConfig);
	}
	
	public static <T> T getInstance(Class<T> clazz){
		if(applicationContext == null){
			throw new ServerInitException("IOC uninitialized!");
		}
		return applicationContext.getBean(clazz);
	}
}	
