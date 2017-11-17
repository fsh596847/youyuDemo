package com.zhongan.demo.hxin.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 图片压缩 gzip base64
 **/
public class EncryptUtil {

	private static final int BUFFER_SIZE = 1024;

	/**
	 * BASE64 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 加密
			return new String(Base64.encode(encode, 0, encode.length,
					Base64.DEFAULT), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			LoggerUtil.debug(e.toString());
		}

		return "";
	}

	/**
	 * BASE64 解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 解密
			return new String(Base64.decode(encode, 0, encode.length,
					Base64.DEFAULT), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			LoggerUtil.debug(e.toString());
		}

		return "";
	}

	/**
	 * GZIP 加密
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] encryptGZIP(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream baos=null;
		GZIPOutputStream gzip=null;
		try {
			// gzip压缩
			baos = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(baos);
			gzip.write(str.getBytes("UTF-8"));
			gzip.close();

			byte[] encode = baos.toByteArray();

			baos.flush();
			baos.close();

			// base64 加密
			return encode;
			// return new String(encode, "UTF-8");

		}catch (Exception e) {
			LoggerUtil.debug(e.toString());
		} finally {

			try {
				if (gzip != null) {
					gzip.close();
				}
				if (baos != null) {
					baos.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				LoggerUtil.debug(e.toString());
			}
		}

		return null;
	}

	/**
	 * GZIP 解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decryptGZIP(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		ByteArrayInputStream bais = null;
		GZIPInputStream gzip = null;
		try {

			byte[] decode = str.getBytes("UTF-8");

			// gzip 解压缩
			bais = new ByteArrayInputStream(decode);
			gzip = new GZIPInputStream(bais);

			byte[] buf = new byte[BUFFER_SIZE];
			int len = 0;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ((len = gzip.read(buf, 0, BUFFER_SIZE)) != -1) {
				baos.write(buf, 0, len);
			}
			gzip.close();
			baos.flush();

			decode = baos.toByteArray();

			baos.close();

			return new String(decode, "UTF-8");

		} catch (Exception e) {
			LoggerUtil.debug(e.toString());
		} finally {

			try {
				if (gzip != null) {
					gzip.close();
				}
				if (bais != null) {
					bais.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				LoggerUtil.debug(e.toString());
			}
		}

		return "";
	}

	/**
	 * 十六进制字符串 转换为 byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
		// return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * byte[] 转换为 十六进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");

		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
