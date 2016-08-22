package com.yy.jp.javaserver;

import java.net.URL;

import org.junit.Test;

import com.yy.jp.javaserver.util.PathUtil;

public class PathUtilTest {

	@Test
	public void testGetRelativePath() {
		String s =PathUtil.getRelativePath("log4j.properties");
		System.out.println(s);
	}

	@Test
	public void testGetRelativeURL() {
		URL s = PathUtil.getRelativeURL("log4j.properties");
		System.out.println(s.toString());
	}

	@Test
	public void testGetAbsolutePath() {
		String s = PathUtil.getAbsolutePath("log4j.properties");
		System.out.println(s);
	}

}
