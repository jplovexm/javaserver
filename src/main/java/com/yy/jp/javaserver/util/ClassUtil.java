package com.yy.jp.javaserver.util;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public class ClassUtil {
	
	private static final Logger LOG = Logger.getLogger(ClassUtil.class);
	
	public static void findAndAddClassInPackageByFile(String packageName,String packagePath,final boolean recursive,Set<Class<?>> classes){
		File dir = new File(packagePath);
		if(!dir.exists()||!dir.isDirectory()){
			return;
		}
		File[] dirFiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				return (recursive&&file.isDirectory())||file.getName().endsWith(".class");
			}
		});
		for(File file:dirFiles){
			if(file.isDirectory()){
				findAndAddClassInPackageByFile(packageName+"."+file.getName(),file.getAbsolutePath(),recursive,classes);
			}else{
				String className = file.getName().substring(0,file.getName().length()-6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName+"."+className));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					LOG.error("找不到此类"+className+".class "+e.getMessage(),e);
				}
			}
		}
	}
	
	public static Set<Class<?>> getClasses(String pack){
		Set<Class<?>> classes =  new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageDirName = pack.replace(".", "/");
		Enumeration<URL> dirs;
		try{
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while(dirs.hasMoreElements()){
				URL  url = dirs.nextElement();
				String protocal = url.getProtocol();
				//以文件的形式保存在服务器上
				if(protocal.equals("file")){
					String filePath = URLDecoder.decode(url.getPath(),"UTF-8");
					findAndAddClassInPackageByFile(pack,filePath,recursive,classes);
				}else if(protocal.equals("jar")){
					JarFile jar;
					jar = ((JarURLConnection) url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()){
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if(name.charAt(0) == '/'){
							name = name.substring(1);
						}
						//如果前半部分和定义的包名相同
						if(name.startsWith(packageDirName)){
							
						}
					}
				}
			}
		}catch(Exception e) {
			
		}
		return  classes;
	}
}
