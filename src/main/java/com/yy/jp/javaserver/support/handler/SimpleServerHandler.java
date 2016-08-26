package com.yy.jp.javaserver.support.handler;


import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yy.jp.javaserver.spring.ioc.BeanFactory;
import com.yy.jp.javaserver.support.async.HandlerCallback;
import com.yy.jp.javaserver.support.request.SimpleRequest;
import com.yy.jp.javaserver.support.response.ReturnType;
import com.yy.jp.javaserver.support.response.SimpleResponse;
import com.yy.jp.javaserver.support.route.InitRoute;
import com.yy.jp.javaserver.support.route.Route;

@Sharable
public class SimpleServerHandler extends SimpleChannelInboundHandler<FullHttpRequest >{
	
	private static final Logger LOG = Logger.getLogger(SimpleServerHandler.class);
	private static Map<String,Route> routes = InitRoute.getRoutes();
	private String uri;
	private EventLoopGroup processGroup;
	
	public SimpleServerHandler(EventLoopGroup processGroup) {
		super();
		this.processGroup = processGroup;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest  request)
			throws Exception {
				final SimpleRequest req = new SimpleRequest();
				uri = request.getUri();
				if(request.getMethod() == HttpMethod.GET){
					QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
	            	Map<String, List<String>> params = decoder.parameters();
	            	if(params.size()>0){
	            		uri = uri.substring(0, uri.indexOf('?'));
	            		for(String key:params.keySet()){
	            			req.setParameter(key, params.get(key).get(0));
	            		}
	            	}
				}else if(request.getMethod() == HttpMethod.POST){
					HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
		            decoder.offer(request);
		            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
		            for(InterfaceHttpData hdata : parmList){
		            	Attribute data = (Attribute) hdata;
		            	req.setParameter(data.getName(), data.getValue());
		            }
				}else{
					return;
				}
	            if(routes.containsKey(uri)){
	            	final Route route = routes.get(uri);
	            	if(route.isAysnc()){
	            		final EventLoopGroup loopGroup = processGroup==null?ctx.channel().eventLoop().parent():processGroup;
		            	loopGroup.execute(new Runnable() {
							public void run() {
								try{
					            	Object obj = BeanFactory.getInstance(route.getClazz());
					            	final HandlerCallback call = new HandlerCallback() {
										public void fail(Throwable e) {
											// TODO Auto-generated method stub
											ctx.fireExceptionCaught(e);
										}
										public void complete(Object result) {
											// TODO Auto-generated method stub
											SimpleResponse res = (SimpleResponse)result;
							            	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK); // 响应
							        		response.headers().set(CONTENT_TYPE, getContentType(res.getRenderType())); 
							        		ByteBuf responseContentByteBuf = Unpooled.copiedBuffer(res.getRederData().getBytes(Charset.forName("utf-8")));  
							        		response.headers().set(CONTENT_LENGTH, responseContentByteBuf.readableBytes());
							        		response.content().writeBytes(responseContentByteBuf);  
							        	    responseContentByteBuf.release();
							            	ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
										}
									};
									Object[] args = new Object[] { req, call };
					            	route.getMethod().invoke(obj, args);
								}catch(Exception e){
									ctx.fireExceptionCaught(e);
								}
							}
						});
					}else{
						Object obj = BeanFactory.getInstance(route.getClazz());
		            	Object result = route.getMethod().invoke(obj, req);
		            	SimpleResponse res = (SimpleResponse)result;
		            	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK); // 响应
		        		response.headers().set(CONTENT_TYPE, getContentType(res.getRenderType())); 
		        		ByteBuf responseContentByteBuf = Unpooled.copiedBuffer(res.getRederData().getBytes(Charset.forName("utf-8")));  
		        		response.headers().set(CONTENT_LENGTH, responseContentByteBuf.readableBytes());
		        		response.content().writeBytes(responseContentByteBuf);  
		        	    responseContentByteBuf.release();
		            	ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
					}
	            }else{
	            	LOG.warn("处理请求uri:"+uri + "404");
	            	ctx.channel().writeAndFlush("404").addListener(ChannelFutureListener.CLOSE);
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

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
}
