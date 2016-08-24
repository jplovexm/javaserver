package com.yy.jp.javaserver.support.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebsocketServetHandler{


	public static void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame)
			throws Exception {
		// TODO Auto-generated method stub
		if(frame instanceof CloseWebSocketFrame){
			ctx.channel().close();
		}else if(frame instanceof TextWebSocketFrame){
			ctx.channel().writeAndFlush(new TextWebSocketFrame("nihao "));
		}
	}

}
