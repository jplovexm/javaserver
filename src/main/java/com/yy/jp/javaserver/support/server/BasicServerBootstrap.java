package com.yy.jp.javaserver.support.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.yy.jp.javaserver.exceptions.ServerInitException;
import com.yy.jp.javaserver.util.Constants;
import com.yy.jp.javaserver.util.IpUtil;

public abstract class BasicServerBootstrap {
	private static final Logger LOG = Logger.getLogger(BasicServerBootstrap.class);
	private Properties config;
	private final ChannelGroup allChannels;
	private List<EventLoopGroup> allEventLoopGroups = new ArrayList<EventLoopGroup>();
	private volatile boolean stopped = false;
	
	public BasicServerBootstrap(Properties config) {
		this.config = config;
		allChannels = new DefaultChannelGroup("Server-all-channels", GlobalEventExecutor.INSTANCE);
	}
	
	public BasicServerBootstrap() {
		this(new Properties());
	}
	
	public BasicServerBootstrap setConfig(Properties config){
		this.config = config;
		return this;
	}
	
	public Properties getConfig() {
		return config;
	}
	
	private final Thread serverGroupShutdownHook = new Thread(new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			stop();
		}
	});
	
	public void stop(){
		LOG.info("Shutting down server");
		if(stopped){
			LOG.info("Already stopped");return;
		}
		LOG.info("Closing all channels..");
		final ChannelGroupFuture future = allChannels.close();
		future.awaitUninterruptibly(10*1000);
		if(!future.isSuccess()){
			final Iterator<ChannelFuture>  it = future.iterator();
			while(it.hasNext()){
				final ChannelFuture cf = it.next();
				if(cf.isSuccess()){
					LOG.info("Unable to close channel. Cause of failure for "+cf.channel()+" is "+cf.cause());
				}
			}
		}
		LOG.info("Shutting down event loops");
		for(EventLoopGroup group : allEventLoopGroups){
			group.shutdownGracefully();
		}
		for(EventLoopGroup group : allEventLoopGroups){
			try {
				group.awaitTermination(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOG.warn("Interrupted while shutting down event loop");
			}
		}
		stopped = true;
		LOG.info("Done shutting server");
	}
	
	public void start(){
		if(getConfig() == null){
			LOG.warn("配置为空，请检查配置文件");
		}else{
			Runtime.getRuntime().addShutdownHook(serverGroupShutdownHook);
			toStart();
		}
	}
	
	protected abstract void toStart() throws ServerInitException;
	
	public String getHost(){
		String host = config.getProperty(Constants.serverHost);
		if(null == host || host.trim().equals("")){
			return IpUtil.getLocalIpStr();
		}
		return host;
	}
	
	public int getPort(){
		String port =  config.getProperty(Constants.serverPort,"9000");
		try{
			return Integer.parseInt(port);
		}catch(NumberFormatException e){
			LOG.warn("加载配置"+Constants.serverPort+"失败"+e.getMessage(),e);
		}
		return 9000;
	}
	
	public int getIdleConnectionTimeout(){
		String timeout = config.getProperty(Constants.idleConnTimeout,"60");
		try{
			return Integer.parseInt(timeout);
		}catch(NumberFormatException e){
			LOG.warn("加载配置"+Constants.idleConnTimeout+"失败"+e.getMessage(),e);
		}
		return 60;
	}
	
	public int getAcceptThreadNumber() {
		String nThread = config.getProperty(Constants.acceptThreadNum);
		try {
				return Integer.parseInt(nThread);
		} catch (NumberFormatException e) {
			LOG.warn("加载配置 "+Constants.acceptThreadNum+" 失败:" + nThread, e);
		}
		return 0;
	}

	public int getWorkThreadNumber() {
		String nThread = config.getProperty(Constants.workThreadNum);
		try {
				return Integer.parseInt(nThread);
		} catch (NumberFormatException e) {
			LOG.warn("加载配置 "+Constants.workThreadNum+" 失败:" + nThread, e);
		}
		return 0;
	}
	
	public int getMaxLengthRequestBody() {
		String maxLengthStr = config.getProperty(Constants.maxLength);
		if (maxLengthStr != null && !"".equals(maxLengthStr)) {
			try {
				return Integer.parseInt(maxLengthStr);
			} catch (NumberFormatException e) {
				LOG.warn("加载配置 "+Constants.maxLength+" 失败:" + maxLengthStr, e);
			}
		}
		return 60 * 1024 * 1024;
	}
	
	/**
	 * 维护所有的EventLoopGroup 用来在关掉服务时候，停掉服务
	 * 
	 * @param group
	 */
	protected void addEventLoopGroup(EventLoopGroup group) {
		this.allEventLoopGroups.add(group);
	}
	
	public void registerChannel(Channel channel) {
		this.allChannels.add(channel);
	}
}
