package com.yy.jp.javaserver.support.handler;


import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.yy.jp.javaserver.spring.ioc.BeanFactory;
import com.yy.jp.javaserver.support.response.ReturnType;
import com.yy.jp.javaserver.support.response.SimpleResponse;
import com.yy.jp.javaserver.support.route.InitRoute;
import com.yy.jp.javaserver.support.route.Route;

public class SimpleServerHandler extends SimpleChannelInboundHandler<FullHttpRequest >{
	
	private static final Logger LOG = Logger.getLogger(SimpleServerHandler.class);
	private static Map<String,Route> routes = InitRoute.getRoutes();
	private String uri;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest  request)
			throws Exception {
			if(null != request){
		            uri = request.getUri();
		            if(StringUtils.isNotBlank(uri)){
		            	QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		            	Map<String, List<String>> params = decoder.parameters();
		            	if(params.size()>0){
		            		uri = uri.substring(0, uri.indexOf('?'));
		            	}
			            if(routes.containsKey(uri)){
			            	Route route = routes.get(uri);
			            	Object obj = BeanFactory.getInstance(route.getClazz());
			            	Object result = route.getMethod().invoke(obj, request);
			            	SimpleResponse res = (SimpleResponse)result;
			            	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK); // 响应
			        		response.headers().set(CONTENT_TYPE, getContentType(res.getRenderType())); 
			        		ByteBuf responseContentByteBuf = Unpooled.copiedBuffer(res.getRederData().getBytes(Charset.forName("utf-8")));  
			        		response.headers().set(CONTENT_LENGTH, responseContentByteBuf.readableBytes());
			        		response.content().writeBytes(responseContentByteBuf);  
			        	    responseContentByteBuf.release();
			            	ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			            }else{
			            	LOG.warn("处理请求uri:"+uri + "404");
			            	ctx.channel().writeAndFlush("404").addListener(ChannelFutureListener.CLOSE);
			            }
		            }else{
		            	LOG.warn("处理请求uri:"+uri + "404");
		            	ctx.channel().writeAndFlush("404").addListener(ChannelFutureListener.CLOSE);
		            }
			}else{
				LOG.warn("处理请求uri:"+uri + "400");
				ctx.channel().writeAndFlush("400").addListener(ChannelFutureListener.CLOSE);
			}
	}
	
	public String getContentType(ReturnType type){
		switch(type){
			case HTML:
				return "text/html; charset=UTF-8";
			case JSON:
				return "text/x-json;charset=UTF-8";
			case TEXT:
				return "text/plain;charset=UTF-8";
			case XML:
				return "text/xml;charset=UTF-8";
			default:
				LOG.error("UNKOWN RENDER TYPE");
				return "text/html; charset=UTF-8";
		}
	}
}
