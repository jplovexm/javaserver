package com.yy.jp.javaserver.support.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.ReferenceCountUtil;

public class DiffRequestHandler extends SimpleChannelInboundHandler<Object> {
	public static final String NAME_FOR_PIPELINE = "DiffRequestHandler";
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof FullHttpRequest){
			FullHttpRequest req = (FullHttpRequest)msg;
			if(req.getMethod() == HttpMethod.GET && "websocket".equalsIgnoreCase(req.headers().get("Upgrade")) ){
				//创建爱你websocket进行连接握手的工厂类，因为不同版本的连接握手不太一样
		        final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(req.headers().get(HttpHeaders.Names.HOST), null, false);
		        final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
		        if(handshaker == null){
		        	WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		        }else{
		        	handshaker.handshake(ctx.channel(), req);
		        }
			}else{
				ctx.pipeline().addAfter(NAME_FOR_PIPELINE, SimpleServerHandler.class.getName(), new SimpleServerHandler());
				ReferenceCountUtil.retain(msg);
				ctx.fireChannelRead(msg);
			}
		}else if(msg instanceof WebSocketFrame){
			WebsocketServetHandler.channelRead0(ctx, (WebSocketFrame)msg);
		}else{
			return;
		}
	}
}
