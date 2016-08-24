package com.yy.jp.javaserver.support.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class SimpleRequest {
	
	private Map<String,String> params;

	public SimpleRequest() {
		params = new HashMap<String, String>();
	}
	
	public void setParameter(String key,String value){
		params.put(key, value);
	}
	
	public String getParameter(String key){
		if(StringUtils.isNotBlank(key)){
			if(params.containsKey(key)){
				return params.get(key);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}
