package com.yy.jp.javaserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.yy.jp.javaserver.exceptions.FileFormatException;

public class PropertiesUtil {
	private static final  Logger LOG = Logger.getLogger(PropertiesUtil.class);
	
	public static Properties loadProperties(String path) throws FileNotFoundException, FileFormatException{
		final File file = new File(path);
		Properties pros = new Properties();
		if(!file.exists()){
			throw new FileNotFoundException("文件不存在："+path);
		}
		if(!file.isFile()){
			throw new FileFormatException("文件格式错误，非文件："+path);
		}
		InputStream is = new FileInputStream(file);
		try {
			pros.load(is);
		} catch (IOException e) {
			IOUtils.closeQuietly(is);
			LOG.warn(e.getMessage(),e);
		}
		return pros;
	}
}
