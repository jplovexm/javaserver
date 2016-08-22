package com.yy.jp.javaserver.support.handler.in;

import java.net.SocketAddress;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class IdleChannelClosehandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOG = Logger.getLogger(IdleChannelClosehandler.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if(evt instanceof IdleStateEvent){
			final IdleStateEvent e= (IdleStateEvent)evt;
			final SocketAddress  add = ctx.channel().remoteAddress();
			ctx.channel().close().addListener(new ChannelFutureListener() {
				
				public void operationComplete(ChannelFuture future) throws Exception {
					// TODO Auto-generated method stub
					if(future.isSuccess()){
						LOG.info("空闲连接关闭:" + add + " for " + e.state() + " 成功!");
					} else {
						LOG.info("空闲连接关闭:" + add + " for " + e.state() + " 失败!");
					}
				}
			});
		}
		super.userEventTriggered(ctx, evt);
	}
	
}
