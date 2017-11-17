package com.zhongan.demo.hxin.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongan.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统名称: demo<br />
 * 模块名称: 基础工具类<br />
 * 模块名称: 常用方法<br />
 * 系统版本: 1.0<br />
 * 相关文档: <br />
 * .<br />
 * <b>修订记录</b>
 * <table>
 * <tr>
 * <td>日期</td>
 * <td>编号</td>
 * <td>修改人</td>
 * <td>备注</td>
 * </tr>
 * <tr>
 * <td>2013-10-10</td>
 * <td>0000</td>
 * <td>hj</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * 
 * @author hj
 * @version 1.0
 * @since 1.0
 */

/**
 * @author rdpc0601
 * 
 */
public class Util {
	protected static final int HASACTIVEACCOUNT = 4;
	protected static final int NOHASACTIVEACOUNT = 5;
	protected static final int NOACTIVEACOUNT = 6;

	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null && str.length() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否是4-15.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean CheckUserNameLen(String userName) {

		return (!isEmpty(userName) && userName.length() >= 4 && userName
				.length() <= 15) ? true : false;

	}

	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String... strs) {
		for (String str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去掉字符串前后空格，如果是null，则返回空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 去掉字符串前后空格，如果是null，则返回空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str, String defaultVal) {
		if (isEmpty(str)) {
			return defaultVal;
		}
		return str.trim();
	}

	/**
	 * 去掉字符串前后空格，如果是null，则返回空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(Object str) {
		if (str == null) {
			return "";
		}
		return trim((String) str);
	}

	/**
	 * 去掉字符串前后空格，如果是null，则返回空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String trimAll(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.replaceAll(" ", "");
	}

	/**
	 * 获取随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String random(int length) {
		StringBuffer val = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val.append(random.nextInt(10));
		}
		return val.toString();
	}

	/**
	 * 检查是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		// for (int i = 0; i < str.length(); i++) {
		// if (!Character.isDigit(str.charAt(i))) {
		// return false;
		// }
		// }
		return true;
	}

	/**
	 * 格式化当前日期 </br> 如：yyyyMMddHHmmssSSS
	 * 
	 * @return
	 */
	/*
	 * public static String dateFormat(String formatStr) { SimpleDateFormat
	 * format = new SimpleDateFormat(formatStr); String strDate =
	 * format.format(new Date()); return strDate; }
	 */

	// -------------银行卡-------------------

	/**
	 * 判断卡号是否合法
	 * 
	 * @param cardNumber
	 * @return
	 */
	public static boolean checkCardNumber(String cardNumber) {
		if (cardNumber == null || cardNumber.length() == 0) {
			return false;
		}
		if (cardNumber.length() < 13 || cardNumber.length() > 19) {
			return false;
		}
		return true;
	}

	/**
	 * 格式化卡号，四个一组，中间以空格隔开
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String formatCardNo(String cardNo) {

		cardNo = trimAll(cardNo);
		if (isEmpty(cardNo)) {
			return "";
		}
		if (cardNo.length() > 19) {
			cardNo = cardNo.substring(0, 19);
		}
		char[] chars = cardNo.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (i != 0 && i % 4 == 0) {
				builder.append(" ");
			}
			builder.append(chars[i]);
		}

		return builder.toString();
	}

	/**
	 * 格式化卡号并且遮盖，四个一组，中间以空格隔开
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String hideCardNoFromat(String cardNo) {

		cardNo = trimAll(cardNo);
		if (!checkCardNumber(cardNo)) {
			return "";
		}
		StringBuilder builder = new StringBuilder(formatCardNo(cardNo));
		builder.replace(10, 13, "****");
		return builder.toString();
	}

	/**
	 * 根据登录类型 遮盖 用户名 ，如果没有 登录 类型，用户名不遮盖 。
	 * 
	 * 用户名4-18位，显示前两位+***+后两位； 身份证显示后四位，其余遮挡； 卡号按现有规则，倒数第8位到倒数第4位遮挡。
	 * 
	 * @param 登录类型
	 * @param 用户名
	 * @return
	 */
	public static String shelterUserName(String lgntype, String name) {

		if ("11".equals(lgntype)) {

			// 手机号
			return Util.hideMobile(name);
		} else if ("03".equals(lgntype)) {

			// 身份证
			return Util.trimAll(Util.hideIdentityCard(name));

		} else if ("04".equals(lgntype)) { //

			// 卡号
			return Util.trimAll(Util.hideCardNoNoFromat(name));

		} else {
			return name;
		}
	}

	/**
	 * 遮盖身份证
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String hideIdentityCard(String cardNo) {

		if (isEmpty(cardNo)) {
			return "";
		}
		cardNo = trimAll(cardNo);

		StringBuilder builder = new StringBuilder(cardNo.substring(0, 3));
		for (int i = 3; i < cardNo.length() - 4; i++) {
			builder.append("*");
		}
		builder.append(cardNo.substring(cardNo.length() - 4));
		// 身份证显示后四位，其余遮挡；
		return builder.toString();
	}

	/**
	 * 无格式化遮盖卡号，四个一组，中间以空格隔开
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String hideCardNoNoFromat(String cardNo) {

		cardNo = trimAll(cardNo);
		if (!checkCardNumber(cardNo)) {
			return "";
		}
		StringBuilder builder = new StringBuilder(cardNo);
		// builder.replace(10, 13, "****");
		// 卡号倒数5-8位用*遮盖
		builder.replace(cardNo.length() - 8, cardNo.length() - 4, "****");
		return builder.toString();
	}

	/**
	 * 格式化手机号，4-7位隐藏
	 * 
	 * @param mobile
	 * @return
	 */
	public static String hideMobile(String mobile) {

		mobile = trimAll(mobile);
		if (!isMobile(mobile)) {
			return "";
		}
		StringBuilder builder = new StringBuilder(mobile);
		builder.replace(3, 7, "****");
		return builder.toString();
	}

	/**
	 * 格式化手机号码，344的方式，中间空格
	 * 
	 * @param phone
	 * @return
	 */
	public static String formatPhoneNo(String phone) {

		phone = trimAll(phone);
		if (isEmpty(phone)) {
			return "";
		}
		if (phone.length() > 11) {
			phone = phone.substring(0, 11);
		}
		char[] chars = phone.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (i == 3 || i == 7) {
				builder.append(" ");
			}
			builder.append(chars[i]);
		}
		return builder.toString();
	}

	/**
	 * 金额格式化 1,234.00
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatAmount(String amount) {

		if (isEmpty(amount)) {
			return "0.00";
		}
		String result = "";
		double d = Double.parseDouble(amount);

		if (d == 0) {
			return "0.00";
		}
		NumberFormat format = new DecimalFormat("###,###,##0.00");
		result = format.format(d);
		return result;
	}

	/**
	 * 金额格式化 1,234.00
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatMoney(String amount) {

		if (isEmpty(amount)) {
			return "";
		}
		String result = "";
		double d = Double.parseDouble(amount);

		if (d == 0) {
			return "0.00";
		}
		NumberFormat format = new DecimalFormat("###0.00");
		result = format.format(d);
		return result;
	}

	/**
	 * 金额格式化 1234,不带小数
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatAmount2(String amount, String defaultval) {

		if (isEmpty(amount)) {
			return defaultval;
		}
		if (amount.indexOf(".") == -1) {
			return amount;
		} else {
			return amount.substring(0, amount.indexOf("."));
		}
	}

	/**
	 * 金额转换，元转换为分
	 * 
	 * @param s
	 * @return
	 */
	public static String YuanToCent(String s) {
		s = trim(s);
		int i = s.indexOf(".");
		if (i == -1)
			return s + "00";
		String intStr = s.substring(0, i);
		if (intStr.length() <= 0)
			intStr = "0";
		String decStr = s.substring(i + 1, s.length());
		if (decStr.length() <= 0)
			decStr = "00";
		if (decStr.length() == 1)
			decStr += "0";
		if (decStr.length() > 2)
			decStr = decStr.substring(0, 2);
		int iInt = Integer.parseInt(intStr);
		if (iInt <= 0)
			return decStr;
		else
			return intStr + decStr;
	}

	/**
	 * 检测手机号是否合
	 * 
	 * @param str手机号码
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
	 * 是否是合法的URL
	 * 
	 * @param str
	 * @return
	 */
	public static String isUrl(String str) {
		if (isEmpty(str)) {
			return "";
		}
		if (str.indexOf("http://") != -1) {
			return str.substring(str.indexOf("http://"));
		} else if (str.indexOf("https://") != -1) {
			return str.substring(str.indexOf("https://"));
		} else if (str.indexOf("www.") != -1) {
			return "http://" + str.substring(str.indexOf("www."));
		} else {
			return "";
		}
	}

	/**
	 * 校验email地址的合法性
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		if (isEmpty(str)) {
			return false;
		}
		String regEx = "\\w+([-+._]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否是金额
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAmount(String str) {
		return isAmount(str, false);
	}

	/**
	 * 是否是金额
	 * 
	 * @param str
	 * @param isInteger
	 *            是否整形
	 * @return
	 */
	public static boolean isAmount(String str, boolean isInteger) {
		if (isEmpty(str)) {
			return false;
		}
		// String regEx = "^[0-9]+(.[0-9]{1,2})?$";
		String regEx1 = "^(([1-9]\\d{0,9})|0)(\\.\\d{1,5})?$";

		// if (isInteger) {
		// regEx1 = "^[0-9]*$";
		// }
		Pattern pattern = Pattern.compile(regEx1);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            sp数值
	 * @param fontScale
	 *            DisplayMetrics类中属性scaledDensity
	 * @return 返回像素值
	 */
	public static int sp2px(float spValue, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}

	/**
	 * 将px值转换sp为值，保证文字大小不变
	 * 
	 * @param spValue
	 *            sp数值
	 * @param fontScale
	 *            DisplayMetrics类中属性scaledDensity
	 * @return 返回像素值
	 */
	public static int px2sp(float pxValue, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 从jsonbject中获取值
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static String getjsonObj(JSONObject jsonObject, String key) {
		try {
			if (jsonObject != null && !isEmpty(key) && jsonObject.has(key)) {
				return jsonObject.getString(key);
			}
		} catch (JSONException e) {
		}
		return "";
	}

	/**
	 * 从jsonbject中获取值,如果没有key或者value为空，返回defaultVal
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static String getjsonObj(JSONObject jsonObject, String key,
                                    String defaultVal) {
		if (jsonObject != null) {
			String value = jsonObject.optString(key, defaultVal);
			if (isEmpty(value)) {
				value = defaultVal;
			}
			return value;
		}
		return defaultVal;
	}

	/**
	 * 判断输入 验证码是否合法
	 * 
	 * @param psd
	 * @return
	 */
	public static boolean checkVerCode(String code) {

		if (TextUtils.isEmpty(code) || code.length() != 6) {
			return false;
		}
		// :必须 是 字母 + 数字 的组合 。
		// return checkNickName(code);
		return true;
	}

	/**
	 * 是否全是数字
	 * 
	 * @param smsCode
	 * @return true=是
	 */
	public static boolean checkSMSCodeValid(String smsCode) {
		if (TextUtils.isEmpty(smsCode)) {
			return false;
		}
		String number = "^[0-9]*$";
		Pattern pattern = Pattern.compile(number);
		Matcher matcher = pattern.matcher(smsCode);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断登录名 是否 是 昵称或 邮箱 或手机号
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isLoginName(String name) {
		if (TextUtils.isEmpty(name)) {
			return false;
		}
		if (Util.isEmail(name) || Util.isMobile(name) || Util.isNickName(name)) {
			return true;
		}
		return false;
	}

	/**
	 * 昵称是否合法 ，
	 * 
	 * @param str
	 * @return 空或者有特殊字符返回true
	 */
	public static boolean nickNameHasSpecialChar(String str) {
		if (isEmpty(str)) {
			return true;
		}
		String[] special_char = { "@", "(CM)", "<", ">", "%", "'", "&", "#",
				";" };
		for (String sc : special_char) {
			if (str.indexOf(sc) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 昵称是否合法 ，
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNickName(String str) {
		if (isNumber(str)) {
			return false;
		}
		String[] special_char = { "@", "(CM)", "<", ">", "%", "'", "&", "#",
				";" };
		for (String sc : special_char) {
			if (str.indexOf(sc) != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 合法用户名 ，
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLegaluserName(String str) {
		if (isNumber(str)) {
			return false;
		}
		String[] special_char = { "@", "(CM)", "<", ">", "%", "'", "&", "#",
				";" };
		for (String sc : special_char) {
			if (str.indexOf(sc) != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 返回msg的bigdecimal类型
	 * 
	 * @param msg
	 * @return
	 */
	public static BigDecimal getBigDecimal(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(msg);
	}

	/**
	 * 获取银行卡号
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String getCardNo(String cardNo) {
		if (isEmpty(cardNo)) {
			return "";
		}
		// 找到分隔符 6226 9014 **** 4190|b80757c6
		if (cardNo.indexOf("|") == -1) {
			return cardNo;

		}
		return cardNo.substring(0, cardNo.indexOf("|"));
	}

	/**
	 * 获取银行卡号Id
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String getCardNoTag(String cardNo) {
		if (isEmpty(cardNo)) {
			return "";
		}
		// 找到分隔符 6226 9014 **** 4190|b80757c6
		if (cardNo.indexOf("|") == -1) {
			return "";

		}
		return cardNo.substring(cardNo.indexOf("|") + 1);
	}

	/**
	 * 获取银行卡类型
	 * 
	 * @param cardType
	 * @return
	 */
	public static String getCardType(String cardType) {
		if (isEmpty(cardType)) {
			return "";
		}
		// 找到分隔符 21|借记卡
		if (cardType.indexOf("|") == -1) {
			return cardType;

		}
		return cardType.substring(0, cardType.indexOf("|"));
	}

	/**
	 * 获取银行卡类型
	 * 
	 * @param cardType
	 * @return
	 */
	public static String getCardTypeName(String cardType) {
		if (isEmpty(cardType)) {
			return "";
		}
		// 找到分隔符 21|借记卡
		if (cardType.indexOf("|") == -1) {
			return cardType;

		}
		return cardType.substring(cardType.indexOf("|") + 1);
	}

	/**
	 * 判断两个字符串是否相等
	 * 
	 * @param str1
	 * @param str2
	 * @return 相等返回0;str1大于str2,返回1;str1小于str2,返回-1,数据错误，返回9
	 */
	public static int stringCompare(String str1, String str2) {
		if (isEmpty(str1, str2)) {
			return 9;
		}
		BigDecimal bigDecimal1 = new BigDecimal(str1);
		BigDecimal bigDecimal2 = new BigDecimal(str2);
		// 相等
		return bigDecimal1.compareTo(bigDecimal2);
	}

	/**
	 * 按钮不可用
	 * 
	 * @param btn
	 */
	public static void disableBtn(Button... btns) {
		if (btns != null) {
			for (Button btn : btns) {
				btn.getBackground().setAlpha(100);
				btn.setEnabled(false);
			}
		}
	}

	/**
	 * 按钮可用
	 * 
	 * @param btn
	 */
	public static void enableBtn(Button... btns) {
		if (btns != null) {
			for (Button btn : btns) {
				btn.getBackground().setAlpha(255);
				btn.setEnabled(true);
			}
		}
	}

	/**
	 * 给文字增加下划线，蓝色
	 * 
	 * @param msg
	 * @return
	 */
	public static Spanned getBlueString(String msg) {
		return Html.fromHtml("<font color='blue'>" + msg + "</font>");
	}

	/**
	 * 给文字增加蓝色
	 * 
	 * @param prefix
	 * @param blueText
	 * @param postfix
	 * @return
	 */
	public static Spanned getBlueString(String prefix, String blueText,
                                        String postfix) {
		return Html.fromHtml(prefix + "<br>" + "请在" + "<font color='#6DB3F8'>"
				+ blueText + "</font>" + postfix);
	}

	/**
	 * 给选定文字改变颜色
	 * 
	 * @param color
	 *            如：#FE7609
	 * @param msg
	 * @return
	 */
	public static String getColorString(String color, String msg) {
		return "<font color='" + color + "'>" + msg + "</font>";
	}

	/**
	 * 给选定文字改变颜色
	 * 
	 * @param color
	 *            如：#FE7609
	 * @param msg
	 * @return
	 */
	public static String getColorString(int color, String msg) {
		return "<font color='" + color + "'>" + msg + "</font>";
	}

	/**
	 * 创建一个按钮的警告窗口
	 * 
	 * @param mContext
	 * @param msg
	 */
	// public static void createWarnDialog(Context mContext, String msg) {
	// DialogCreater.showOneBtnDialogForWranWithImg(mContext, msg);
	// }

	/**
	 * 创建一个按钮的错误窗口
	 * 
	 * @param mContext
	 * @param msg
	 */
	// public static void createErrorDialog(Context mContext, String msg) {
	// DialogCreater.showOneBtnDialogForErrorWithImg(mContext, msg);
	// }

	/**
	 * 获取分割字符串
	 * 
	 * @param str
	 *            源字符串
	 * @param split
	 *            分割字符串
	 * @param index
	 *            索引
	 * @return
	 */
	public static String getSplitValue(String str, String split, int index) {
		if (str == null) {
			return null;
		}
		String[] s = str.split(split);
		if (index < 0 || index >= s.length) {
			return null;
		}
		return s[index];
	}

	/**
	 * 获取数据字段值
	 * 
	 * @param str
	 *            源字符串
	 * @return
	 */
	public static String getFieldValue(String str) {
		return getSplitValue(str, "\\|", 0);
	}

	/**
	 * 获取数据字段文字
	 * 
	 * @param str
	 *            源字符串
	 * @return
	 */
	public static String getFieldText(String str) {
		return getSplitValue(str, "\\|", 1);
	}

	/**
	 * 调换有效期年和月的位置
	 * 
	 * @param valid
	 * @return
	 */
	public static String changeYearMonth(String oldValid) {
		byte[] oldValidByte = oldValid.getBytes();
		int length = oldValidByte.length;
		byte[] newValidByte = new byte[length];
		System.arraycopy(oldValidByte, 0, newValidByte, length / 2, length / 2);
		System.arraycopy(oldValidByte, length / 2, newValidByte, 0, length / 2);
		return new String(newValidByte);
	}

	/**
	 * 四位一组，格式化银行卡号
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String getFormatCardNo(String cardNo) {
		int length = cardNo.length();
		StringBuilder sb = new StringBuilder();
		String tmpString = "";
		int index = 0;
		while (true) {
			tmpString = cardNo.substring(index, index + 4);
			sb.append(tmpString);
			index += 4;
			length -= tmpString.length();
			if (length < 4) {
				sb.append(" ").append(cardNo.substring(index));
				break;
			}
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 格式化显示11位电话号码
	 * 
	 * @param phone
	 *            原始电话号码，例如13311112222
	 * @return 默认说出格式：133 1111 2222
	 */
	public static String getFormatPhone(String phone) {
		return getFormatLengthPhone(phone, 3, 4);
	}

	/**
	 * 格式化显示11位电话号码
	 * 
	 * @param phone
	 *            原始电话号码，例如13311112222
	 * @param firstSp
	 *            第一个间隔的長度
	 * @param secondSp
	 *            第二个间隔的長度
	 * @return
	 */
	public static String getFormatLengthPhone(String phone, int firstSp,
                                              int secondSp) {
		if (TextUtils.isEmpty(phone)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(phone.substring(0, firstSp)).append(" ")
				.append(phone.substring(firstSp, firstSp + secondSp))
				.append(" ").append(phone.substring(firstSp + secondSp));
		return sb.toString();
	}

	/**
	 * 判断是否是合法 身份证 身份证 中 可有 非数字。
	 * 
	 * @param identitycard
	 * @return boolean
	 */
	public static boolean isIdentityCard(String identitycard) {
		if (Util.isEmpty(identitycard)) {
			return false;
		}
		//
		// String regx = "[0-9]{17}x";
		// String reg1 = "[0-9]{15}";
		// String regex = "[0-9]{18}";

		Pattern pattern = Pattern
				.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
		Matcher matcher = pattern.matcher(identitycard);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
		// return identitycard.matches(regx) || identitycard.matches(reg1) ||
		// identitycard.matches(regex);

	}

	/**
	 * 将json中的key转化为小写
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject getLowercJsonObject(JSONObject json) {
		JSONObject response = new JSONObject();
		try {
			Iterator<String> it = (Iterator<String>) json.keys();
			while (it.hasNext()) {
				String key = it.next();
				response.put(key.toLowerCase(), json.opt(key));
			}
		} catch (JSONException e) {

		}
		return response;
	}

	/**
	 * 根据资源名称返回资源在R文件中的ID
	 * 
	 * @return
	 */
	public static int getResourceIdByName(Context context, String name,
                                          String type) {
		int resId = 0;
		try {
			resId = context.getResources().getIdentifier(name, type,
					context.getPackageName());
		} catch (Exception e) {
			LoggerUtil.exception(e);
		}
		return resId;
	}

	/**
	 * 字符串相减
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String stringSub(String str1, String str2) {
		BigDecimal bigDecimal1 = getBigDecimal(str1);
		BigDecimal bigDecimal2 = getBigDecimal(str2);
		// 相减
		return bigDecimal1.subtract(bigDecimal2).toString();
	}

	/**
	 * 字符串除法
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String stringDiv(String str1, String str2) {
		BigDecimal bigDecimal1 = getBigDecimal(str1);
		BigDecimal bigDecimal2 = getBigDecimal(str2);
		if (bigDecimal2.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		}
		return bigDecimal1.divide(bigDecimal2, 2, BigDecimal.ROUND_HALF_UP)
				.toString();
	}

	/**
	 * 字符串除法,计算百分比
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String stringDivPer(String str1, String str2) {
		BigDecimal bigDecimal = getBigDecimal(str1);
		bigDecimal = bigDecimal.multiply(new BigDecimal("100"));

		return stringDiv(bigDecimal.toString(), str2);
	}

	/**
	 * 判断是不是合法的别名 别名：最多5个汉字 或 10个字符（数字和字母），不包括特殊符号
	 * 
	 * @param alias
	 *            别名
	 * @return
	 */
	public static boolean isCorrectAlias(String alias) {

		if (alias.equals("") || alias == null)
			return false;

		int chinese = 0;
		int chars = 0;

		// 汉字区间
		int i = 19968;
		int e = 40869;
		int maxlenght = 10;

		for (int t = 0; t < alias.length(); t++) {

			char cur = alias.charAt(t);

			if (cur >= i && cur <= e)
				chinese++;
			else if (cur >= 'A' && cur <= 'Z' || cur >= 'a' && cur <= 'z'
					|| cur >= '0' && cur <= '9')
				chars++;
			else
				return false;

			if (chinese * 2 + chars > maxlenght)
				return false;
		}

		return true;
	}

	/**
	 * 为指定Edittext设置一个合法别名长度的过滤器，注意只过滤长度是不是合法，不过滤内容。 别名长度规则（最多5个汉字 或
	 * 10个字符）请参加开发需求文档
	 * 
	 * @param editText
	 *            要设置过滤器的控件
	 */
	public static void setAliasLengthInputFilter(EditText editText) {
		InputFilter fl = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

				LoggerUtil.warn("source", source.toString());
				LoggerUtil.warn("start", start + "");
				LoggerUtil.warn("end", end + "");
				LoggerUtil.warn("dest", dest.toString());
				LoggerUtil.warn("dstart", dstart + "");
				LoggerUtil.warn("dend", dend + "");

				int s = 19968;
				int e = 40869;
				int maxlenght = 10;

				int destlen = 0;
				int sourcelen = 0;

				for (int i = 0; i < dest.length() && dest != null; i++) {

					char c = dest.charAt(i);

					if (c >= s && c <= e)
						destlen += 2;
					else
						destlen++;
				}

				for (int j = 0; j < source.length() && source != null; j++) {

					char c = source.charAt(j);

					if (c >= s && c <= e)
						sourcelen += 2;
					else
						sourcelen++;
				}

				if (destlen + sourcelen <= maxlenght) {

					return source;

				} else {

					int tmplen = destlen;

					int k = 0;
					for (k = 0; k < source.length() && source != null; k++) {

						char c = source.charAt(k);

						if (c >= s && c <= e)
							tmplen += 2;
						else
							tmplen++;

						if (tmplen > 10)
							break;
					}

					return source.subSequence(0, k);
				}
			}
		};

		InputFilter is[] = new InputFilter[1];
		is[0] = fl;
		editText.setFilters(is);
	}

	/**
	 * 假设日期格式为yy-mm-dd 分隔符根据情况设定，这里用的是"-"
	 */
	public static int compareDate(String pdate, String ndate) {

		String local[] = pdate.split("\\-");
		String server[] = ndate.split("\\-");

		int[] perDateNums = new int[local.length];
		for (int i = 0; i < local.length; i++) {
			perDateNums[i] = Integer.parseInt(local[i]);
		}

		int[] nextDateNums = new int[server.length];
		for (int i = 0; i < local.length; i++) {
			nextDateNums[i] = Integer.parseInt(server[i]);
		}

		if (perDateNums[0] == nextDateNums[0]) {
			if (perDateNums[1] == nextDateNums[1]) {
				if (perDateNums[2] == nextDateNums[2]) {
					return 0;
				} else {
					return perDateNums[2] < nextDateNums[2] ? -1 : 1;
				}
			} else {
				return perDateNums[1] < nextDateNums[1] ? -1 : 1;
			}
		} else {
			return perDateNums[0] < nextDateNums[0] ? -1 : 1;
		}
	}

	/**
	 * is Background return true;
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/** 取虚拟键盘高度 */
	public static int getVmenuHeight(Context ctx) {

		Resources resources = ctx.getResources();

		int rid = resources.getIdentifier("config_showNavigationBar", "bool",
				"android");
		if (rid > 0) {
			Log.d("sam test", resources.getBoolean(rid) + ""); // 获取导航栏是否显示true
																// or false
		}

		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			Log.d("sam test", resources.getDimensionPixelSize(resourceId) + ""); // 获取高度
		}
		return resources.getDimensionPixelSize(resourceId);
	}

	public static char convertDigit(int value) {
		value &= 0x0f;
		if (value >= 10)
			return ((char) (value - 10 + 'a'));
		else
			return ((char) (value + '0'));
	}

	public static boolean hasNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static String convert(final byte bytes[], int pos, int len) {

		StringBuffer sb = new StringBuffer(len * 2);
		for (int i = pos; i < pos + len; i++) {
			sb.append(convertDigit((int) (bytes[i] >> 4)));
			sb.append(convertDigit((int) (bytes[i] & 0x0f)));
		}
		return (sb.toString());

	}

	public static byte[] md5Byte(String encryptStr) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(encryptStr.getBytes("UTF-8"));
		return md.digest();
	}

	/**
	 * 
	 * 判断邮编格式是否正确
	 */
	public static boolean isZipCode(String code) {
		if (isEmpty(code) || code.length() != 6) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[0-9][0-9]{5}$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断姓名格式是否正确 姓名2-15位汉字
	 */
	public static boolean isCorrectName(String code) {
		if (isEmpty(code) || code.length() > 8 || code.length() < 2) {
			return false;
		}
		Pattern pattern = Pattern.compile("^([\u4e00-\u9fa5]){2,15}$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断姓名拼音格式是否正确 详细住址只能包括：大写英文字母 空格
	 * 
	 */
	public static boolean isCorrectNamePinyin(String code) {
		if (isEmpty(code)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[A-Z]+ +[A-z ]+[A-Z]+$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断详细住址格式是否正确 详细住址只能包括：汉字、英文字母、-、#、，
	 * 
	 */
	public static boolean isCorrectDetailAddress(String code) {
		if (isEmpty(code)) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^[\u4E00-\u9FA5A-Za-z0-9-，,# ­]{5,80}$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断日期格式
	 * 
	 */
	public static boolean isCorrectData(String code) {
		if (isEmpty(code)) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断公司名格式
	 */
	public static boolean isCompanyName(String code) {

		if (code.equals("") || code == null)
			return false;

		int chinese = 0;
		int chars = 0;

		// 汉字区间
		int i = 19968;
		int e = 40869;
		// int maxlenght = 10;

		for (int t = 0; t < code.length(); t++) {

			char cur = code.charAt(t);

			if (cur >= i && cur <= e)
				chinese++;
			else if (cur >= 'A' && cur <= 'Z' || cur >= 'a' && cur <= 'z'
					|| cur >= '0' && cur <= '9')
				chars++;
			else
				return false;

			// if (chinese * 2 + chars > maxlenght)
			// return false;
		}

		return true;
	}

	/**
	 * 
	 * 判断单位电话格式 如1234-12345678-1234
	 */
	public static boolean isAreaCode(String code) {
		if (isEmpty(code)) {
			return false;
		}
		// String
		// reg="((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
		Pattern pattern = Pattern
				.compile("(^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断车牌号格式
	 * 
	 */
	public static boolean isCarNum(String code) {
		if (isEmpty(code)) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断身份证格式
	 * 
	 */
	public static boolean isiCarNum(String code) {
		if (isEmpty(code)) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 判断密码 长度6-20位 只能有数字、字母
	 * 
	 */
	public static boolean isPwdNum(String pwd) {
		if (isEmpty(pwd) || pwd.length() < 6) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[0-9A-Za-z]{6,20}$");
		Matcher matcher = pattern.matcher(pwd);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * 动态设置ListView组建的高度，解决scrollview 嵌套listview，listview只显示一条数据问题
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView,
			Context context) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ px2dip(context, 80);
		listView.setLayoutParams(params);

	}

	/**
	 * 根据图片地址将图片转化成bitmap格式
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			LoggerUtil.exception(e);
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			LoggerUtil.exception(e);
		}
		return bitmap;
	}

	public static void hideKeyBoard(Context context, View view) {

		// 隐藏软键盘
		((InputMethodManager) context
				.getSystemService(context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(view.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

	}

	// 是否联网网络
	public static boolean IsHaveInternet(Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = manger.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate() {
		String str = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			str = formatter.format(curDate);
		} catch (Exception e) {
			LoggerUtil.debug("获取当前时间失败" + e.toString());
		}
		return str;
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
		//Pattern p_idcard = Pattern.compile(reg_idcard);
		//Matcher m_idcard = p_idcard.matcher(idCards);
		//flg_idcard = m_idcard.matches();
	}

	//密码合法性验证
	private final static String PASSWORD_MATCHER = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
	public static boolean passwordMatcher(String passWord){

		if(passWord.length() < 6 || passWord.length() > 20){
			return false;
		}

		return passWord.matches(PASSWORD_MATCHER);

	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.hxloading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.loding_dialog);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loding_img);
		TextView tipTextView = (TextView) v.findViewById(R.id.loding_tip_msg);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
//		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}
	//根据身份证号输出年龄
    public static int IdNOToAge(String IdNO){
        int leh = IdNO.length();
        int age=0;
        if (leh == 18) {
            String dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year=df.format(new Date());
            age= Integer.parseInt(year)- Integer.parseInt(dates);
            return age;
        }else
        {    
            return age;
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

  @SuppressLint("SimpleDateFormat")
 public static int getCurrentYear()
  {
	  int year=0;
	  SimpleDateFormat df = new SimpleDateFormat("yyyy");
      String yearStr=df.format(new Date());
      year= Integer.parseInt(yearStr);
      return year;
  }
}
