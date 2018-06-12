package com.cd.uap.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * 对字符串进行MD5的加密
	 * @param str 需要加密的字符串
	 * @return 加密后的字符串
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5Digest(String str) throws NoSuchAlgorithmException{
		String temp;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		temp = byte2hex(digest);
		return temp;
	}

	/**
	 * 二进制转换为十六进制字符串
	 * @param bytes
	 * @return
	 */
	private static String byte2hex(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int i = 0; i < bytes.length; i++) {
			stmp = (java.lang.Integer.toHexString(bytes[i] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			}else {
				hs = hs + stmp;
			}
		}
		return hs;
	}
}
