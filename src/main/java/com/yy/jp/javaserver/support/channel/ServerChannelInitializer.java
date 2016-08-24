package com.yy.jp.javaserver.support.channel;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.yy.jp.javaserver.config.ServerConfig;
import com.yy.jp.javaserver.support.handler.DiffRequestHandler;
import com.yy.jp.javaserver.support.handler.in.IdleChannelClosehandler;
import com.yy.jp.javaserver.support.handler.in.IpFilterhandler;
import com.yy.jp.javaserver.support.server.BasicServerBootstrap;
import com.yy.jp.javaserver.util.Constants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {
	
	private static final Logger LOG = Logger.getLogger(ServerChannelInitializer.class);
	private BasicServerBootstrap basicServerBootstrap;
	private int idleTimeout;
	private static IpFilterhandler ipFilterHanlder;
	
	public ServerChannelInitializer(BasicServerBootstrap basicServerBootstrap,
			int idleTimeout, int maxLenght) {
		this.basicServerBootstrap = basicServerBootstrap;
		this.idleTimeout = idleTimeout;
		if (!StringUtils.equals(ServerConfig.serverConfig.getProperty(Constants.ipfilter), "false")) {
			ipFilterHanlder = new IpFilterhandler();
		}
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		// TODO Auto-generated method stub
		LOG.info("initChannel 注册channel:"+ch);
		basicServerBootstrap.registerChannel(ch);
		ch.pipeline().addLast("log",new LoggingHandler(LogLevel.INFO))
						  .addLast("idle",new IdleStateHandler(0, 0, idleTimeout, TimeUnit.SECONDS))
						  .addLast("IdleChannelClosehandler", new IdleChannelClosehandler());
		if(!StringUtils.equals(ServerConfig.serverConfig.getProperty(Constants.ipfilter), "false")){
			ch.pipeline().addLast("IpFilterHanlder",ipFilterHanlder);
		}
        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
        ch.pipeline().addLast(new HttpRequestDecoder());
		// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
        ch.pipeline().addLast(new HttpResponseEncoder());
        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));  
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        
        ch.pipeline().addLast(DiffRequestHandler.NAME_FOR_PIPELINE,new DiffRequestHandler());
	}

}
