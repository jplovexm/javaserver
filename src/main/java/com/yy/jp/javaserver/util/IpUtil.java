package com.yy.jp.javaserver.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class IpUtil {
	
	private static final  Logger LOG = Logger.getLogger(IpUtil.class);
	/**
	 * 获取本地ip地址
	 * @return
	 */
	public static String getLocalIpStr(){
		String localIpStr = "127.0.0.1";
		Enumeration<NetworkInterface> netInterface;
		try {
			netInterface = NetworkInterface.getNetworkInterfaces();
			while(netInterface.hasMoreElements()){
				NetworkInterface ni = netInterface.nextElement();
				if(ni.equals(Constants.NETWORKNAME)){
					Enumeration<InetAddress>  ia= ni.getInetAddresses();
					while(ia.hasMoreElements()){
						InetAddress add = ia.nextElement();
						if(!(add instanceof Inet6Address)){
							localIpStr = add.getHostAddress();
							break;
						}
					}
					break;
				}
			}
		} catch (SocketException e) {
			LOG.warn(e.getMessage(),e);
		}
		return localIpStr;
	}
	/**
	 * 获取ip地址
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static InetAddress getLocalIp() throws UnknownHostException, SocketException{
		if(System.getProperty("os.name").toLowerCase().indexOf("windows") > -1){
			return  InetAddress.getLocalHost();
		}else{
			Enumeration<NetworkInterface> netInterface = NetworkInterface.getNetworkInterfaces();
			while(netInterface.hasMoreElements()){
				NetworkInterface ni = netInterface.nextElement();
				if(ni.equals(Constants.NETWORKNAME)){
					Enumeration<InetAddress>  ia= ni.getInetAddresses();
					while(ia.hasMoreElements()){
						InetAddress add = ia.nextElement();
						if(!(add instanceof Inet6Address)){
							return add;
						}
					}
					break;
				}
			}
			return null;
		}
	}
	
	/**
	 * 获取mac地址
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getMacAddress() throws UnknownHostException, SocketException{
		InetAddress ip = getLocalIp();
		LOG.info("current ip:"+ip.getHostAddress());
		NetworkInterface ne = NetworkInterface.getByInetAddress(ip);
		byte[] mac = ne.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}
		LOG.info("Current MAC address : " + sb);
		return (sb.toString());
	}
	public static void main(String[] args) throws UnknownHostException, SocketException {
		// TODO Auto-generated method stub
		System.out.println(InetAddress.getLocalHost());
		System.out.println(getMacAddress());
	}

}
