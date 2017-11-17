package consumer.fin.rskj.com.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SysUtil {

    public static String []stepMap;

//	/**
//	 * 状态栏高度算法
//	 *
//	 * @param activity
//	 * @return
//	 */
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

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 校验email地址的合法性
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {

        String regEx = "\\w+([-+._]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
