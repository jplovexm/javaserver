package com.yy.jp.javaserver.support.route;

import java.lang.reflect.Method;
/**
 * 
 * @author Administrator
 *
 */
public class Route {	
	
	private String name;
	private Class<?> clazz;
	private Method method;
	public Route(String name, Class<?> clazz, Method method) {
		super();
		this.name = name;
		this.clazz = clazz;
		this.method = method;
	}
	public String getName() {
		return name;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public Method getMethod() {
		return method;
	}
	@Override
	public String toString() {
		return "Route [name=" + name + ", clazz=" + clazz + ", method="
				+ method + "]";
	}
}
