package com.yy.jp.javaserver.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;


public class PathUtil {
	
	private static final Logger LOG = Logger.getLogger(PathUtil.class);
	/**
	 * 获取文件相对路径
	 * @param fileName
	 * @return
	 */
	public static String getRelativePath(String fileName){
		if(PathUtil.class.getResource("/"+fileName) != null){
			return PathUtil.class.getResource("/"+fileName).getPath();
		}else if(Thread.currentThread().getContextClassLoader().getResource("/"+fileName)!=null){
			return Thread.currentThread().getContextClassLoader().getResource("/"+fileName).getPath();
		}else{
			return fileName;
		}
	}
	/**
	 * 获取文件的相对URL
	 * @param fileName
	 * @return
	 */
	public static URL getRelativeURL(String fileName){
		String path = "";
		try {
			path =PathUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			LOG.error(e.getMessage(),e);
		}
		if(path.endsWith(".jar")){
			return null;
		}else{
			return PathUtil.class.getResource("/"+fileName);
		}
	}
	/**
	 * 获取文件绝对路径
	 * @param fileName
	 * @return
	 */
	public static String getAbsolutePath(String fileName){
		String path = "";
		try {
			path = PathUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			LOG.error(e.getMessage(),e);
		}
		return new File(path).getParentFile().getPath()+File.separator+fileName;
	}
}
