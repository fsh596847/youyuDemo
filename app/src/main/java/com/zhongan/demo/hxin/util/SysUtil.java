package com.zhongan.demo.hxin.util;

import android.content.Context;
import android.content.res.Resources;

public class SysUtil {

	/**
	 * 状态栏高度算法
	 * 
	 * @param activity
	 * @return
	 */
//	public static int getStatusHeight(Activity activity) {
//		int statusHeight = 0;
//		Rect localRect = new Rect();
//		activity.getWindow().getDecorView()
//				.getWindowVisibleDisplayFrame(localRect);
//		statusHeight = localRect.top;
//		if (0 == statusHeight) {
//			Class<?> localClass;
//			try {
//				localClass = Class.forName("com.android.internal.R$dimen");
//				Object localObject = localClass.newInstance();
//				int i5 = Integer.parseInt(localClass
//						.getField("status_bar_height").get(localObject)
//						.toString());
//				statusHeight = activity.getResources()
//						.getDimensionPixelSize(i5);
//			} catch (Exception e) {
//				LoggerUtil.debug("tag", "getStatusHeight-------------->>"+e.toString());
//			}
//		}
//		return statusHeight;
//	}
	public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
