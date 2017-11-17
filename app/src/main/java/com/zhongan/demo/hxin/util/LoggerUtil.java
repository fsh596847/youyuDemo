package com.zhongan.demo.hxin.util;


import android.util.Log;

public class LoggerUtil {

	private static final String Tag = "mobilebank";
	/**
	 * LOG输出开关，true=输出，false=禁止
	 */
	public static boolean isDebug = true;
	
	public static void debug(String msg) {
		if (isDebug && !Util.isEmpty(msg)) {
			debug(Tag, msg);
		}
	}

	public static void debug(String tag, String msg) {
		if (isDebug && !Util.isEmpty(msg)) {
			Log.d(tag, "debug------>> " + msg);
		}
	}

	public static void warn(String tag, String msg) {
		if (isDebug && !Util.isEmpty(msg)) {
			
			Log.w(tag, "warn------>> " + msg);
		}
	}

	public static void exception(Exception ex) {
		if (isDebug) {
			debug(Tag, "error------>>"+ex.toString());
		}
	}

	public static void systemOutPrintln(String msg) {
		if (isDebug && !Util.isEmpty(msg)) {
			System.out.println("console------>> " + msg);
		}
	}

}