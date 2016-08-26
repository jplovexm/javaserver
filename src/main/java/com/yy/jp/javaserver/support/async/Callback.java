package com.yy.jp.javaserver.support.async;

public interface Callback<T> {
	/**
	 * 执行完成
	 * @param result
	 */
	public void complete(T result);
	/**
	 * 执行失败
	 * @param result
	 */
	public void fail(Throwable result);
}
