package com.yy.jp.javaserver.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.yy.jp.javaserver.clazz.ClazzLoader;
import com.yy.jp.javaserver.config.ServerConfig;
import com.yy.jp.javaserver.exceptions.FileFormatException;
import com.yy.jp.javaserver.spring.ioc.BeanFactory;
import com.yy.jp.javaserver.support.server.BasicServerBootstrap;
import com.yy.jp.javaserver.support.server.DefaultServerBootstrap;
import com.yy.jp.javaserver.util.Constants;
import com.yy.jp.javaserver.util.IpUtil;
import com.yy.jp.javaserver.util.PathUtil;
import com.yy.jp.javaserver.util.PropertiesUtil;

public class Launcher {
	
	private static final Logger LOG = Logger.getLogger(Launcher.class);
	
	public static void main(String[] args) throws UnknownHostException, SocketException, FileNotFoundException, FileFormatException {
		// TODO Auto-generated method stub
		availableLog4JConfigurationFile();
		LOG.info("log4j.properties exists : "+new File(PathUtil.getRelativePath("log4j.properties")).exists());
		LOG.info("server.properties exists : "+new File(PathUtil.getRelativePath("server.properties")).exists());
		LOG.info("ip:"+IpUtil.getLocalIp());
		String configPath = PathUtil.getRelativePath(Constants.serverProperties);
		LOG.info("开始加载server配置文件 :"+configPath);
		final Properties config = PropertiesUtil.loadProperties(configPath);
		ServerConfig.serverConfig = config;
		LOG.info("完成加载server配置文件 :"+configPath);
		String scanPackage = config.getProperty(Constants.scanPackage);
		LOG.info("scan Package:"+scanPackage);
		if(StringUtils.isNotBlank(scanPackage)){
			ClazzLoader.init(scanPackage);
		}
		String springConfigPath = config.getProperty(Constants.springConfig, "spring/applicationcontext.xml");
		BeanFactory.init(springConfigPath);
		BasicServerBootstrap basicServerBootstrap;
		try{
			basicServerBootstrap = BeanFactory.getInstance(BasicServerBootstrap.class);
		}catch(Exception e){
			basicServerBootstrap = new DefaultServerBootstrap();
		}
		basicServerBootstrap.setConfig(config).start();
	}
	
	public static void availableLog4JConfigurationFile(){
		PropertyConfigurator.configure(PathUtil.getRelativePath("log4j.properties"));
	}

}
