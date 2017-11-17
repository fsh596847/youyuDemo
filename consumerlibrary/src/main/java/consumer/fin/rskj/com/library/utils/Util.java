package consumer.fin.rskj.com.library.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.megvii.idcardquality.IDCardQualityResult;
import com.megvii.idcardquality.bean.IDCardAttr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import consumer.fin.rskj.com.consumerlibrary.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class Util {

	public static Toast toast;

	/**
	 * 输出toast
	 */
	public static void showToast(Context context, String str) {
		if (context != null) {
			if (toast != null) {
				toast.cancel();
			}
			toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
			// 可以控制toast显示的位置
			toast.setGravity(Gravity.TOP, 0, 30);
			toast.show();
		}
	}

	/**
	 * 取消弹出toast
	 */
	public static void cancleToast(Context context) {
		if (context != null) {
			if (toast != null) {
				toast.cancel();
			}
		}
	}

	public static String getUUIDString(Context mContext) {
		String KEY_UUID = "key_uuid";
		SharedUtil sharedUtil = new SharedUtil(mContext);
		String uuid = sharedUtil.getStringValueByKey(KEY_UUID);
		if (uuid != null)
			return uuid;

		uuid = getPhoneNumber(mContext);
		Log.w("ceshi", "uuid====" + uuid);
		if (uuid == null || uuid.trim().length() == 0) {
			uuid = getMacAddress(mContext);
			if (uuid == null || uuid.trim().length() == 0) {
				uuid = getDeviceID(mContext);
				if (uuid == null || uuid.trim().length() == 0) {
					uuid = UUID.randomUUID().toString();
					uuid = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
				}
			}
		}
		sharedUtil.saveStringValue(KEY_UUID, uuid);
		return uuid;
	}

	public static String getPhoneNumber(Context mContext) {
		TelephonyManager phoneMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return "";
		}
		return phoneMgr.getLine1Number();
	}

	public static String getDeviceID(Context mContext) {
//		TelephonyManager tm = (TelephonyManager) mContext
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		return tm.getDeviceId();
		return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	public static String getMacAddress(Context mContext) {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String address = info.getMacAddress();
		if(address != null && address.length() > 0){
			address = address.replace(":", "");
		}
		return address;
	}
	
	public static Camera.Size getNearestRatioSize(Camera.Parameters para,
			final int screenWidth, final int screenHeight) {
		List<Camera.Size> supportedSize = para.getSupportedPreviewSizes();
		for (Camera.Size tmp : supportedSize) {
			if (tmp.width == 1280 && tmp.height == 720) {
				return tmp;
			}
		}
		Collections.sort(supportedSize, new Comparator<Camera.Size>() {
			@Override
			public int compare(Camera.Size lhs, Camera.Size rhs) {
				int diff1 = (((int) ((1000 * (Math.abs(lhs.width
						/ (float) lhs.height - screenWidth
						/ (float) screenHeight))))) << 16)
						- lhs.width;
				int diff2 = (((int) (1000 * (Math.abs(rhs.width
						/ (float) rhs.height - screenWidth
						/ (float) screenHeight)))) << 16)
						- rhs.width;

				return diff1 - diff2;
			}
		});

		return supportedSize.get(0);
	}
	
	
//    public static Camera.Size getNearestRatioSize(Camera.Parameters para, final int screenWidth, final int screenHeight) {
//        List<Camera.Size> supportedSize = para.getSupportedPreviewSizes();
//        Collections.sort(supportedSize, new Comparator<Camera.Size>() {
//            @Override
//            public int compare(Camera.Size lhs, Camera.Size rhs) {
//                int diff1 = (((int) ((1000 * (Math.abs(lhs.width / (float) lhs.height -
//                        screenWidth / (float) screenHeight))))) << 16) + lhs.width;
//                int diff2 = (((int) (1000 * (Math.abs(rhs.width / (float) rhs.height -
//                        screenWidth / (float) screenHeight)))) << 16) + rhs.width;
//
//                return diff1 - diff2;
//            }
//        });
//
//        return supportedSize.get(0);
//    }

    public static String getTimeStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return simpleDateFormat.format(new Date());
    }

    public static void closeStreamSilently(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {

            }
        }
    }

    public static byte[] bmp2byteArr(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled())
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        Util.closeStreamSilently(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String errorType2HumanStr(IDCardQualityResult.IDCardFailedType type, IDCardAttr.IDCardSide side) {
        String result = null;
        switch (type) {
            case QUALITY_FAILED_TYPE_NOIDCARD:
                result = "请将身份证置于提示框内";
                break;
            case QUALITY_FAILED_TYPE_BLUR:
                result = "请点击屏幕对焦";
                break;
            case QUALITY_FAILED_TYPE_BRIGHTNESSTOOHIGH:
                result = "太亮";
                break;
            case QUALITY_FAILED_TYPE_BRIGHTNESSTOOLOW:
                result = "太暗";
                break;
            case QUALITY_FAILED_TYPE_OUTSIDETHEROI:
                result = "请将身份证与提示框对齐";
                break;
            case QUALITY_FAILED_TYPE_SIZETOOLARGE:
                result = "请将身份证与提示框对齐";
                break;
            case QUALITY_FAILED_TYPE_SIZETOOSMALL:
                result = "请将身份证与提示框对齐";
                break;
            case QUALITY_FAILED_TYPE_SPECULARHIGHLIGHT:
                result = "请调整拍摄位置，以去除光斑";
                break;
            case QUALITY_FAILED_TYPE_TILT:
                result = "请将身份证摆正";
                break;
            case QUALITY_FAILED_TYPE_SHADOW:
    			result = "请调整拍摄位置，以去除阴影";
    			break;
            case QUALITY_FAILED_TYPE_WRONGSIDE:
                if (side == IDCardAttr.IDCardSide.IDCARD_SIDE_BACK)
                    result = "请翻到国徽面";
                else {
                    result = "请翻到人像面";
                }
                break;
        }
        return result;
    }

    public static byte[] readModel(Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = -1;
        try {
            inputStream = context.getResources().openRawResource(R.raw.idcardmodel);
            while ((count = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

	public static void hideKeyBoard(Context context, View view) {

		// 隐藏软键盘
		((InputMethodManager) context
				.getSystemService(context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(view.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

	}


	/**
	 * 检测手机号是否合
	 *
	 * @param str 手机号码
	 * @return
	 */
	public static boolean isMobile(String str) {
		if (isEmpty(str) || str.length() != 11) {
			return false;
		}
		// String temp = "^((13[0-9])|(15[^4,\\d])|(18[0,5-9]))\\d{8}$";
//		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		String regExp="^1[3|4|5|7|8][0-9]{9}$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 将15位身份证号转化为18位返回，非15位身份证号原值返回
	 *
	 * @param identityCard
	 * @return
	 */
	public static String get18Ic(String identityCard) {
		String retId = "";
		String id17 = "";
		int sum = 0;
		int y = 0;
		// 定义数组存放加权因子（weight factor）
		int[] wf = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		// 定义数组存放校验码（check code）
		String[] cc = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		if (identityCard.length() != 15) {
			return identityCard;
		}
		// 加上两位年19
		id17 = identityCard.substring(0, 6) + "19" + identityCard.substring(6);
		// 十七位数字本体码加权求和
		for (int i = 0; i < 17; i++) {
			sum = sum + Integer.valueOf(id17.substring(i, i + 1)) * wf[i];
		}
		// 计算模
		y = sum % 11;
		// 通过模得到对应的校验码 cc[y]
		retId = id17 + cc[y];
		return retId;
	}


	/**
	 * 身份证合法性
	 */
	private final static String reg_idcard =
			"^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

	public static boolean isIdCardNum(String idcard){
		if(TextUtils.isEmpty(idcard)){
			return false;
		}

		if(idcard.length() != 18){
			return false;
		}

		return idcard.matches(reg_idcard);
	}


	//map转换为json字符串
	public static String hashMapToJson(HashMap map) {
		String string = "{";
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			string += "\"" + e.getKey() + "\":";
			string += "\"" + e.getValue() + "\",";
		}
		string = string.substring(0, string.lastIndexOf(","));
		string += "}";

		return string;
	}

	//获取数组索引
	public static int getIndex(String[] array,String value){
		for(int i = 0;i<array.length;i++){
			if((array[i]).contains(value)){
				return i;
			}
		}
		return -1;//当if条件不成立时，默认返回一个负数值-1
	}



    //获得当前activity的名字
    public static String getAppPackageName(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d("lixx", "当前应用:" + componentInfo.getPackageName());
        return componentInfo.getPackageName();
    }


	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
}
