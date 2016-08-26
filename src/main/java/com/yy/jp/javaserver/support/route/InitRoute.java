package com.yy.jp.javaserver.support.route;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yy.jp.javaserver.clazz.ClazzLoader;
import com.yy.jp.javaserver.exceptions.RouteConfigRuntimeException;
import com.yy.jp.javaserver.support.annotation.HAction;
import com.yy.jp.javaserver.support.annotation.HSync;
import com.yy.jp.javaserver.support.annotation.MethodExe;

public class InitRoute {
	private static volatile boolean hasInit = false;
	private static Map<String,Route> routes = new HashMap<String, Route>(); 
	private static Map<String,Route> NOT_MODIFY_ROUTES;
	
	public static synchronized void initRoute(){
		Set<Class<?>> allClasses =  ClazzLoader.getAllClass();
		for(Class<?> clazz : allClasses){
			if(clazz.getAnnotation(HAction.class)!=null){
				Method[] methods = clazz.getMethods();
				for(Method method : methods){
					if(Modifier.isPublic(method.getDeclaringClass().getModifiers())){
						MethodExe me = method.getAnnotation(MethodExe.class);
						HSync sync = method.getAnnotation(HSync.class);
						if(null != me){
							addRoute(me,method,clazz,sync);
						}
					}
				}
			}
		}
		hasInit = true;
		NOT_MODIFY_ROUTES = Collections.unmodifiableMap(routes);
	}
	
	public static void addRoute(MethodExe me,Method m,Class<?> clazz,HSync sync){
		checkExists(me);
		Route  route = new Route(me.name(), clazz, m);
		if(null!=sync){
			route.setAysnc(true);
		}
		routes.put(me.name(),route);
	}
	
	private static void checkExists(MethodExe me){
		if(routes.containsKey(me.name())){
			throw new RouteConfigRuntimeException("重复的路由：name="+me.name());
		}
	}
	
	
	public synchronized static Map<String,Route> getRoutes(){
		if(!hasInit){
			initRoute();
		}
		return NOT_MODIFY_ROUTES;
	}
}
