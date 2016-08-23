package com.yy.jp.javaserver.support.response;


public class SimpleResponse {
	
	private String rederData;
	private byte[] content;
	private ReturnType renderType;
	
	public SimpleResponse(String rederData, ReturnType renderType) {
		super();
		this.rederData = rederData;
		this.renderType = renderType;
	}

	public String getRederData() {
		return rederData;
	}

	public void setRederData(String rederData) {
		this.rederData = rederData;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public ReturnType getRenderType() {
		return renderType;
	}

	public void setRenderType(ReturnType renderType) {
		this.renderType = renderType;
	}
}
