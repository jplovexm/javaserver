package com.yy.jp.javaserver.support.channel;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.yy.jp.javaserver.config.ServerConfig;
import com.yy.jp.javaserver.support.handler.in.IdleChannelClosehandler;
import com.yy.jp.javaserver.support.handler.in.IpFilterhandler;
import com.yy.jp.javaserver.support.server.BasicServerBootstrap;
import com.yy.jp.javaserver.util.Constants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {
	private static final Logger LOG = Logger.getLogger(ServerChannelInitializer.class);
	private BasicServerBootstrap basicServerBootstrap;
	private int idleTimeout;
	private int maxLenght;
	private static IpFilterhandler ipFilterHanlder;
	
	public ServerChannelInitializer(BasicServerBootstrap basicServerBootstrap,
			int idleTimeout, int maxLenght) {
		this.basicServerBootstrap = basicServerBootstrap;
		this.idleTimeout = idleTimeout;
		this.maxLenght = maxLenght;
		if (!StringUtils.equals(ServerConfig.serverConfig.getProperty(Constants.ipfilter), "false")) {
			ipFilterHanlder = new IpFilterhandler();
		}
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		// TODO Auto-generated method stub
		basicServerBootstrap.registerChannel(ch);
		ch.pipeline().addLast("log",new LoggingHandler(LogLevel.INFO))
						  .addLast("idle",new IdleStateHandler(0, 0, idleTimeout, TimeUnit.SECONDS))
						  .addLast("IdleChannelClosehandler", new IdleChannelClosehandler());
		if(!StringUtils.equals(ServerConfig.serverConfig.getProperty(Constants.ipfilter), "false")){
			ch.pipeline().addLast("IpFilterHanlder",ipFilterHanlder);
		}
		
	}

}
