package com.yy.jp.javaserver.support.test;

import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;


import com.yy.jp.javaserver.support.annotation.HAction;
import com.yy.jp.javaserver.support.annotation.MethodExe;
import com.yy.jp.javaserver.support.response.ReturnType;
import com.yy.jp.javaserver.support.response.SimpleResponse;

@HAction
public class TestHandler {
	
	@MethodExe(name="/test/aa")
	public SimpleResponse test(HttpRequest request){
		Map<String,String> test = new HashMap<String,String>();
		for(int i=0;i<100;i++){
			test.put(""+i,"你是"+i);
		}
		SimpleResponse r = new SimpleResponse(test.toString(),ReturnType.JSON);
		return r;
	}
	
}