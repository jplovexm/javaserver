package com.yy.jp.javaserver.support.server;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import com.yy.jp.javaserver.exceptions.ServerInitException;
import com.yy.jp.javaserver.support.channel.ServerChannelInitializer;

public class DefaultServerBootstrap extends BasicServerBootstrap {
	private static final Logger LOG = Logger.getLogger(DefaultServerBootstrap.class);
	
	public DefaultServerBootstrap() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void toStart() throws ServerInitException {
		// TODO Auto-generated method stub
		final String host = getHost();
		final int port = getPort();
		final int idleTimeout = getIdleConnectionTimeout();
		final int maxLength = getMaxLengthRequestBody(); 
		EventLoopGroup acceptGroup = new NioEventLoopGroup(this.getAcceptThreadNumber(), new DefaultThreadFactory("accept-group"));
		EventLoopGroup workGroup = new NioEventLoopGroup(this.getWorkThreadNumber(), new DefaultThreadFactory("work-group"));
		addEventLoopGroup(acceptGroup);
		addEventLoopGroup(workGroup);
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(acceptGroup, workGroup).channel(NioServerSocketChannel.class)
			.childHandler(new ServerChannelInitializer(this,idleTimeout,maxLength)).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture furute = server.bind(host, port);
			furute.addListeners(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					// TODO Auto-generated method stub
					registerChannel(future.channel());
					if(future.isSuccess()){
						LOG.info("server 启动：host "+ host +" ,port "+port + ". enjoy it!");
					}else{
						LOG.error("server 启动失败",future.cause());
					}
				}
			}).sync();
			furute.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			LOG.error("启动失败！"+e.getMessage(),e);
		}finally{
			try {
				acceptGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				LOG.error("关闭失败！"+e.getMessage(),e);
			}
		}
		
	}

}
