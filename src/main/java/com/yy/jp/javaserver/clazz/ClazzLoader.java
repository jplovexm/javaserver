package com.yy.jp.javaserver.clazz;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yy.jp.javaserver.util.ClassUtil;

public class ClazzLoader {
	private static final Logger LOG = Logger.getLogger(ClazzLoader.class);
	private static final Set<Class<?>> allClasses = new HashSet<Class<?>>();
	
	public static void init(String scanPackage){
		allClasses.addAll(ClassUtil.getClasses(scanPackage));
		LOG.info("加载类===============start");
		for(Class<?> c :allClasses ){
			LOG.info(c);
		}
		LOG.info("加载类===============end");
	}
	
	public static Set<Class<?>> getAllClass(){
		return Collections.unmodifiableSet(allClasses);
	}
}
