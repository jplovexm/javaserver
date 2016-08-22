package com.yy.jp.javaserver.support.handler.in;

import java.net.InetAddress;
import java.net.InetSocketAddress;



import java.util.Set;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IpFilterhandler extends ChannelInboundHandlerAdapter{
	private static final Logger LOG = Logger.getLogger(IpFilterhandler.class);
	private Set<String> ips;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress socketAdress =  (InetSocketAddress) ctx.channel().remoteAddress();
		InetAddress  inetAddress = socketAdress.getAddress();
		String ipAddress = inetAddress.getHostAddress();
		if(ips.contains(ipAddress)){
			super.channelActive(ctx);
		}else{
			ctx.channel().close().addListener(new ChannelFutureListener() {
				
				public void operationComplete(ChannelFuture future) throws Exception {
					// TODO Auto-generated method stub
					if (future.isSuccess()) {
						LOG.info("过滤ip请求，关闭连接成功:" + future.channel().remoteAddress());
					} else {
						LOG.warn("过滤ip请求，关闭连接失败:" + future.channel().remoteAddress());
					}
				}
			});
		}
	}
	
}
