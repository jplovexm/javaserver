package com.yy.jp.javaserver.support.test;

import java.util.HashMap;
import java.util.Map;

import com.yy.jp.javaserver.support.annotation.HAction;
import com.yy.jp.javaserver.support.annotation.HSync;
import com.yy.jp.javaserver.support.annotation.MethodExe;
import com.yy.jp.javaserver.support.async.HandlerCallback;
import com.yy.jp.javaserver.support.request.SimpleRequest;
import com.yy.jp.javaserver.support.response.ReturnType;
import com.yy.jp.javaserver.support.response.SimpleResponse;

@HAction
public class TestHandler {
	
	@MethodExe(name="/test/aa")
	public SimpleResponse test(SimpleRequest request){
		Map<String,String> test = new HashMap<String,String>();
		for(int i=0;i<100;i++){
			test.put(""+i,"你是"+i);
		}
		SimpleResponse r = new SimpleResponse(test.toString(),ReturnType.JSON);
		return r;
	}
	
	@MethodExe(name="/test/ws")
	public SimpleResponse test1(SimpleRequest request){
		Map<String,String> test = new HashMap<String,String>();
		for(int i=0;i<100;i++){
			test.put(""+i,"你是"+i);
		}
		SimpleResponse r = new SimpleResponse(test.toString(),ReturnType.JSON);
		return r;
	}
	
	@HSync
	@MethodExe(name="/test/ee")
	public void  test2(SimpleRequest request,HandlerCallback call){
		SimpleResponse r = new SimpleResponse("这是测试",ReturnType.JSON);
		call.complete(r);
	}
}
