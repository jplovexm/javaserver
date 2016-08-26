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
	private boolean isAysnc;
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
	
	public boolean isAysnc() {
		return isAysnc;
	}
	public void setAysnc(boolean isAysnc) {
		this.isAysnc = isAysnc;
	}
	@Override
	public String toString() {
		return "Route [name=" + name + ", clazz=" + clazz + ", method="
				+ method + "]";
	}
}
