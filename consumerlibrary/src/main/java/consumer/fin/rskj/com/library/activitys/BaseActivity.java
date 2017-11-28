package consumer.fin.rskj.com.library.activitys;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wknight.keyboard.WKnightKeyboard;

import consumer.fin.rskj.com.library.login.OkHttpRequestManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import consumer.fin.rskj.com.consumerlibrary.BuildConfig;
import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.BillItem;
import consumer.fin.rskj.com.library.module.Rows;
import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.okhttp.callback.Callback;
import consumer.fin.rskj.com.library.utils.BusinessParams;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.DensityUtil;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SharedPreferenceUtils;
import consumer.fin.rskj.com.library.views.MultiEditText;
import consumer.fin.rskj.com.library.views.MyDialog;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

/**
 * 公共Activity
 */
public abstract class BaseActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback {

  private static final String TAG = BaseActivity.class.getSimpleName();
  protected Map<String, String> requestParams = new HashMap<>();

  protected View dialogView;
  private Dialog mDialog;
  private MultiEditText editPwd;
  protected LayoutInflater inflater;
  FinishCallBackImpl callBack;
  protected OkHttpRequestManager okHttpRequestManager;
  /**
   * 需要进行检测的权限数组
   */
  protected String[] needPermissions = {
      Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
      Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
  };

  //权限检测请求码
  private static final int PERMISSION_REQUEST_CODE = 1234;

  /**
   * 判断是否需要检测，防止不停的弹框
   */
  private boolean isNeedCheck = true;
  /**
   * 判断应用是否在前台
   */
  public static boolean isForeground = false;

  private static boolean isFrist = true;//第一次进入
  protected int screenHeight;
  protected int screenWidth;

  protected SharedPreferenceUtils sharePrefer;
  public static String sessionId;



  protected LocationManager locationManager;
  protected static Location location;
  protected String locationProvider;
  protected boolean flag = false;

  @Override public void onStart() {
    super.onStart();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inflater = LayoutInflater.from(this);

    DisplayMetrics outMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
    screenHeight = outMetrics.heightPixels;
    screenWidth = outMetrics.widthPixels;
    if (null == okHttpRequestManager) {
      okHttpRequestManager = OkHttpRequestManager.getInstance(this);
    }

    //隐藏标题栏
    // getSupportActionBar().hide();
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    sharePrefer = new SharedPreferenceUtils(this.getApplicationContext());

  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    init();
  }

  @Override public void setContentView(View view) {
    super.setContentView(view);
    init();
  }

  @Override public void setContentView(View view, ViewGroup.LayoutParams params) {
    super.setContentView(view, params);
    init();
  }

  @Override protected void onResume() {
    super.onResume();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (isNeedCheck) {
        checkPermissions(needPermissions);
      }
    }
  }

  @Override protected void onPause() {
    super.onPause();
  }

  public void initPWDDialog() {
    dialogView = inflater.inflate(R.layout.ubound_dialog, null);// 得到加载view
    mDialog = new Dialog(this, R.style.myDialogTheme);// 创建自定义样式dialog
    TextView textView = (TextView) dialogView.findViewById(R.id.bankTail);
    textView.setText(Html.fromHtml(String.format(getString(R.string.delete_bank), "9999")));
    dialogView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mDialog.dismiss();
      }
    });

    editPwd = (MultiEditText) dialogView.findViewById(R.id.editPwd);
    final WKnightKeyboard keyboard = new WKnightKeyboard(editPwd);
    if (editPwd != null) {
      keyboard.setRecvTouchEventActivity(this);
    }

    editPwd.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length() == 6) {
          String paymentPassword = keyboard.getEnctyptText();
          keyboard.clearInput();
          callBack.finishCallBack(paymentPassword);
        }
      }
    });

    TextView textView3 = (TextView) dialogView.findViewById(R.id.textView3);//忘记密码
    textView3.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        intent2 = new Intent(BaseActivity.this, ReSetPayPWDActivity.class);
        startActivity(intent2);
      }
    });

    //mDialog.setCancelable(false);// 不可以用“返回键”取消
    mDialog.setContentView(dialogView,
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

    mDialog.setCanceledOnTouchOutside(false);
    WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
    params.width = (int) (screenWidth * 0.8);
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.gravity = Gravity.TOP;

    params.x = 0; // 新位置X坐标
    params.y = DensityUtil.dip2px(this, 80); // 新位置Y坐标

    mDialog.getWindow()
        .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    mDialog.getWindow()
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    mDialog.getWindow().setAttributes(params);
  }

  public void showPWDDialog(FinishCallBackImpl callBack) {
    this.callBack = callBack;
    if (mDialog == null) {
      initPWDDialog();
    }

    if (mDialog.isShowing()) {
      return;
    }

    try {
      mDialog.setCanceledOnTouchOutside(false);
      //            mDialog.setCancelable(false);
      mDialog.show();
    } catch (Exception e) {
    }
  }

  public void dismissPWDDialog() {
    try {
      if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
      }
    } catch (Exception e) {
    }
  }

  /**
   * 检测权限
   */
  private void checkPermissions(String... permissions) {
    List<String> needRequestPermissionList = findDeniedPermissions(permissions);
    if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
      ActivityCompat.requestPermissions(this,
          needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
          PERMISSION_REQUEST_CODE);
    }
  }

  /**
   * 获取权限集中需要申请权限的列表
   */
  private List<String> findDeniedPermissions(String[] permissions) {
    List<String> needRequestPermissionList = new ArrayList<>();
    for (String perm : permissions) {
      if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
          || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
        needRequestPermissionList.add(perm);
      }
    }
    return needRequestPermissionList;
  }

  /**
   * 检测是否所有的权限都已经授权
   */
  private boolean verifyPermissions(int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  /**
   * 显示提示信息
   */
  private void showMissingPermissionDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.map_notifyTitle);
    builder.setMessage(R.string.map_notifyMsg);
    // 拒绝, 退出应用
    builder.setNegativeButton(R.string.map_cancel, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });

    builder.setPositiveButton(R.string.map_setting, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        startAppSettings();
      }
    });
    builder.setCancelable(false);
    builder.show();
  }

  /**
   * 启动应用的设置
   */
  private void startAppSettings() {
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivity(intent);
    //      startActivityForResult(intent,PERMISSION_REQUEST_CODE);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (isNeedCheck) {
          checkPermissions(needPermissions);
        }
      }
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (null != dialog) {
      dialog.onDestory();
    }
  }

  /**
   * 初始化操作
   **/
  public abstract void init();

  /**
   * showToast 简单显示错误提示
   *
   * @param msg Toast提示话术
   * @param position Toast显示位置 0：顶部 1：中间 2：底部
   */
  public void showToast(String msg, int position) {
    Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    //toast显示位置
    if (position == 0) {
      toast.setGravity(Gravity.TOP, 0, 0);
    } else if (position == 1) {
      toast.setGravity(Gravity.CENTER, 0, 0);
    } else if (position == 2) {
      toast.setGravity(Gravity.BOTTOM, 0, 0);
    }
    toast.show();
  }

  /**
   * 子类可以重写决定是否使用透明状态栏
   */
  protected boolean translucentStatusBar() {
    return false;
  }

  /**
   * 子类可以重写改变状态栏颜色
   */
  protected int setStatusBarColor() {
    return getColorPrimary();
  }

  /**
   * 获取主题色
   */
  public int getColorPrimary() {
    TypedValue typedValue = new TypedValue();
    getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
    return typedValue.data;
  }

  private MyDialog dialog;

  /**
   * showLoading 显示加载框
   *
   * @param msg 加载框提示话术
   */
  public void showLoading(String msg) {
    if (dialog != null && dialog.isShowing()) return;
    dialog = new MyDialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCanceledOnTouchOutside(false);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setMessage(msg);
    dialog.show();
  }

  /**
   * dismissLoading 隐藏加载框
   */
  public void dismissLoading() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  protected void destoryDialog() {
    if (dialog != null) {
      dialog.onDestory();
      dialog = null;
    }

    if (netDialog != null) {
      netDialog = null;
    }
  }

  //检测用户是否开启权限
  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] paramArrayOfInt) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (!verifyPermissions(paramArrayOfInt)) {
        showMissingPermissionDialog();
        isNeedCheck = false;
      }
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      View v = getCurrentFocus();
      if (isShouldHideKeyboard(v, ev)) {
        boolean res = hideKeyboard(v.getWindowToken());
        if (res) {
          //隐藏了输入法，则不再分发事件
          return true;
        }
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  /**
   * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
   */
  private boolean isShouldHideKeyboard(View v, MotionEvent event) {
    if (v != null && (v instanceof EditText)) {
      int[] l = { 0, 0 };
      v.getLocationInWindow(l);
      int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
      return !(event.getX() > left
          && event.getX() < right
          && event.getY() > top
          && event.getY() < bottom);
    }
    // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
    return false;
  }

  /**
   * 获取InputMethodManager，隐藏软键盘
   */
  private boolean hideKeyboard(IBinder token) {
    if (token != null) {
      InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    return false;
  }

  /**
   * 公共请求接口
   *
   * @param params 请求参数
   * @param callBack 请求回调
   **/
  protected void sendPostRequest(Map<String, String> params, final ResultCallBack callBack) {

    params.put("version", BuildConfig.VERSION_NAME);

    LogUtils.d(TAG, "requestParams--->" + params.toString());
    Log.d("debug", "sharePrefer.getSessionID() = " + sharePrefer.getSessionID());
    HttpInfo httpInfo = HttpInfo.Builder().setUrl(Constants.REQUEST_URL)//请求URL
        .addParams(params).addHead("Cookie", sharePrefer.getSessionID()).build();

    OkHttpUtil.getDefault(this).doPostAsync(httpInfo, new Callback() {
      @Override public void onSuccess(HttpInfo info) throws IOException {
        String result = info.getRetDetail().toString();
        Log.d(TAG, "onSuccess result = " + result);
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(result);
          String returnCode = jsonObject.getString("returnCode");
          String returnMsg = jsonObject.getString("returnMsg");
          if ("000000".equals(returnCode)) {
            LogUtils.d("debug", "onSuccess result------------->" + result);
            callBack.onSuccess(result);
          } else if ("E999985".equals(returnCode) || "ES00000303".equals(returnCode)) {
            dismissLoading();
            LogUtils.d("debug", "--------------token失效，或者用户未登录-------------");
            sharePrefer.setLogin(false);
            showToast(returnMsg, Constants.TOAST_SHOW_POSITION);
          } else {
            showToast(returnMsg, Constants.TOAST_SHOW_POSITION);
            callBack.onError(returnCode, returnMsg);
          }
        } catch (JSONException e) {
          LogUtils.e("error", "数据解析有误" + e.toString());
          showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
        }
      }

      @Override public void onFailure(HttpInfo info) throws IOException {
        String result = info.getRetDetail().toString();
        Log.d(TAG, "onFailure result = " + result);
        showToast(result, Constants.TOAST_SHOW_POSITION);
        callBack.onFailure(result);
      }
    });
  }

  /**
   * 登录请求接口
   *
   * @param params 请求参数
   * @param callBack 请求回调
   **/
  protected void loginPostRequest(Map<String, String> params, final ResultCallBack callBack) {

    params.put("version", BuildConfig.VERSION_NAME);

    LogUtils.d(TAG, "requestParams--->" + params.toString());
    Log.d(TAG, "sharePrefer.getSessionID() = " + sharePrefer.getSessionID());
    HttpInfo httpInfo = HttpInfo.Builder().setUrl(Constants.LOGIN_URL)//请求URL
        .addParams(params).build();

    OkHttpUtil.getDefault(this).doPostAsync(httpInfo, new Callback() {
      @Override public void onSuccess(HttpInfo info) throws IOException {
        String result = info.getRetDetail().toString();
        callBack.onSuccess(result);
      }

      @Override public void onFailure(HttpInfo info) throws IOException {
        String result = info.getRetDetail().toString();
        Log.d(TAG, "onFailure result = " + result);
        showToast(result, Constants.TOAST_SHOW_POSITION);
        callBack.onFailure(result);
      }
    });
  }

  /** 定位 */
  protected String getLocation() {
    //获取地理位置管理器
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //获取所有可用的位置提供器
    List<String> providers = locationManager.getProviders(true);
    if (providers.contains(LocationManager.GPS_PROVIDER)) {
      //如果是GPS
      locationProvider = LocationManager.GPS_PROVIDER;
    } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
      //如果是Network
      locationProvider = LocationManager.NETWORK_PROVIDER;
    } else {
      //没有可用
      return "";
    }

    //获取Location
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      showToast("请打开gps以获取位置", Constants.TOAST_SHOW_POSITION);
      return "";
    }
    location = locationManager.getLastKnownLocation(locationProvider);

    //可查看是否授权获取定位信息
    flag =
        Settings.Secure.getInt(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
    //在更新检测后，才去获取地理位置
    if (location != null) {
      //不为空,获取地理位置经纬度
      return getCity(location);
    } else {
      return "";
    }
  }

  /**
   * 显示地理位置经度和纬度信息
   */
  private String getCity(Location location) {
    String cit = "";
    String street = "";
    //当location对象不为空-获取位置之后--获取页面数据
    if (location != null) {

      String latLongString = null;
      String locationStr = "维度：" + location.getLatitude() + "\n" + "经度：" + location.getLongitude();
      System.out.println("经纬度 " + locationStr);
      List<Address> addList = null;
      Geocoder ge = new Geocoder(this);
      try {
        //根据经纬度-获取地理位置
        addList = ge.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (addList != null && addList.size() > 0) {
        for (int i = 0; i < addList.size(); i++) {
          Address ad = addList.get(i);
          //获取城市
          latLongString = ad.getLocality();
          street = ad.getAddressLine(0);
          LogUtils.d(TAG, "当前位置 street = " + street);
        }
      }
      if (latLongString != null) {
        //截取城市名
        String city = latLongString.substring(0, latLongString.indexOf("市"));
        LogUtils.d(TAG, "当前位置 city = " + city);
      }

      return street;
    } else {
      //当用户拒绝定位权限时进行其他操作
      return "";
    }
  }

  // 获取短信验证码接口
  protected void getMsmCode(String isNo, String mobile, final ResultCallBack callBack2) {
    //        mYzmBtn.setClickable(false);
    //        showLoading("获取验证码中,请稍后...");
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_YZM);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    //        requestParams.put("id", isNo);
    requestParams.put("isNo", isNo);
    requestParams.put("mobile", mobile);//手机号码
    showLoading("正在加载...");
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        callBack2.onSuccess(data);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        callBack2.onError(retrunCode, errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        callBack2.onFailure(errorMsg);
      }
    });
  }

  //验证码检查
  protected void checkSMSCode(String smsCode, String phone, String checkCode,
      final FinishCallBackImpl fCallBack) {
    //M000015 短信验证码验证
    requestParams.clear();
    requestParams.put("transCode", "M000015");//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");

    requestParams.put("smsCode", smsCode);
    requestParams.put("isNo", checkCode);

    requestParams.put("cell", TextUtils.isEmpty(phone) ? sharePrefer.getPhone() : phone);

    showLoading("正在加载...");
    LogUtils.d(TAG, "M000015: requestParams--->" + requestParams.toString());

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "M000015: onSuccess--->" + data);

        try {
          JSONObject jsonObject = new JSONObject(data);

          if ("000000".equals(jsonObject.getString("returnCode")) && "1".equals(
              jsonObject.getString("code"))) {
            fCallBack.finishCallBack(data);
          } else {
            showToast(jsonObject.getString("returnMsg"), Constants.TOAST_SHOW_POSITION);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "M000015: onError--->" + errorMsg);
        //Toast.makeText(BaseActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "M000015: onFailure--->" + errorMsg);
        //Toast.makeText(BaseActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
      }
    });
  }

  //获取客户信息
  protected void getConsumerMsg(final FinishCallBackImpl finishCallBack) {
    //M100123
    requestParams.clear();
    requestParams.put("transCode", "M100123");//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");

    showLoading("正在加载...");

    LogUtils.d(TAG, "本月账单: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "本月账单: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          if ("000000".equals(jsonObject.getString("returnCode"))) {
            sharePrefer.setCustName(jsonObject.getString("custName"));
            sharePrefer.setIdCardNum(jsonObject.getString("idnumber"));
            finishCallBack.finishCallBack("");
            return;
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "本月账单: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "本月账单: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * registered:已注册     saveinfo  :完善客户信息
   * realfied  :实名认证(OCR)     verifi4   :验4
   * face++    :人脸识别     baseInfo  :基础信息
   * suppleInfo:补充信息     aced      :授信申请中
   * cedbad    :授信申请被拒绝     ced       :已授信
   * 0          ：未有状态
   */
  private Intent intent2;

  protected void goPage(String userStateInfo) {

    LogUtils.d(TAG, "nextUserState = " + userStateInfo);

    switch (userStateInfo) {
      case BusinessParams.realfied:
        //实名认证 OCR
        intent2 = new Intent(this, FaceIDCardInfoUploadActivity.class);
        startActivity(intent2);
        break;

      case BusinessParams.verifi4:
        //绑卡 验4
        intent2 = new Intent(this, DealSelfInfoActivity.class);
        startActivity(intent2);
        break;

      case BusinessParams.facepp:
        //人脸识别
        intent2 = new Intent(this, FaceStartActivity.class);
        startActivity(intent2);
        break;

      case BusinessParams.baseInfo:
        //基本信息
        intent2 = new Intent(this, BasicInfoActivity.class);
        startActivity(intent2);
        break;
    }
  }

  private Dialog netDialog;
  protected LinearLayout layout;
  protected View netView;
  private WebViewActivity.BackCallBack backCallBack;

  private void initNetDialog() {
    netView = inflater.inflate(R.layout.net_dialog, null);// 得到加载view
    layout = (LinearLayout) netView.findViewById(R.id.dialog_view);// 加载布局

    layout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        backCallBack.backCallBack();
      }
    });

    //        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "rotation", 0.0f, 360F);
    //        animator.setRepeatCount(ObjectAnimator.INFINITE);
    //        animator.setDuration(1500).start();

    netDialog = new Dialog(this, R.style.myDialogTheme);// 创建自定义样式dialog

    netDialog.setCancelable(false);// 不可以用“返回键”取消
    netDialog.setContentView(layout,
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
  }

  public void netDismiss() {
    try {
      if (netDialog != null && netDialog.isShowing()) {
        netDialog.dismiss();
      }
    } catch (IllegalArgumentException e) {
      LogUtils.d(TAG, e.getMessage());
    }
  }
}
