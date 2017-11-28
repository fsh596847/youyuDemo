package com.zhongan.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bqs.risk.df.android.BqsDF;
import com.bqs.risk.df.android.BqsParams;
import com.bqs.risk.df.android.OnBqsDFContactsListener;
import com.bqs.risk.df.android.OnBqsDFListener;
import com.google.gson.Gson;
import com.zhongan.demo.http.OkHttpRequestManager;
import com.zhongan.demo.module.CommonResponse;
import com.zhongan.demo.util.Global;
import com.zhongan.demo.util.LogUtils;

import java.util.HashMap;

/**
 * Created by HP on 2017/6/12.
 */

public class BaseActivity extends ParentActivity implements OnBqsDFListener {

  public static final String TAG = BaseActivity.class.getSimpleName();
  //okhttp网络请求
  protected OkHttpRequestManager okHttpRequestManager;
  //网络请求表单参数
  protected HashMap<String, String> paramsMap;

  protected int screenHeight;
  protected int screenWidth;

  protected Gson gson;
  protected CommonResponse requestResult;//接口返回结果集

  private Dialog progressDialog;
  protected LayoutInflater inflater;
  protected View dialogView;
  protected LinearLayout layout;
  protected TextView tipTextView;
  protected ImageView img;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inflater = LayoutInflater.from(this);

    paramsMap = new HashMap<String, String>();
    if (null == okHttpRequestManager) {
      okHttpRequestManager = OkHttpRequestManager.getInstance(this);
    }
    DisplayMetrics outMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
    screenHeight = outMetrics.heightPixels;
    screenWidth = outMetrics.widthPixels;
  }

  @Override protected void onResume() {
    super.onResume();

    /**
     * Global.authRuntimePermissions:用于判断运行时权限是否授权
     *
     * BqsDF.canInitBqsSDK()：用于控制不被频繁初始化。如果设备指纹采集成功了，app不被kill的情况下30分钟内不会重复提交设备信息
     *
     */
    if (Global.authRuntimePermissions && BqsDF.canInitBqsSDK()) {
      initBqsDFSDK();
    }
  }

  protected void initBqsDFSDK() {
    //1、添加设备信息采集回调
    BqsDF.setOnBqsDFListener(this);
    BqsDF.setOnBqsDFContactsListener(new OnBqsDFContactsListener() {
      @Override public void onGatherResult(boolean gatherStatus) {
        LogUtils.Log("通讯录采集状态 gatherStatus=" + gatherStatus);
      }

      @Override public void onSuccess(String tokenKey) {
        LogUtils.Log("通讯录采集成功");
      }

      @Override public void onFailure(String resultCode, String resultDesc) {
        LogUtils.Log("通讯录采集失败 resultCode=" + resultCode + " resultDesc=" + resultDesc);
      }
    });
    //BqsDF.setOnBqsDFCallRecordListener(...);

    //2、配置初始化参数
    BqsParams params = new BqsParams();
    params.setPartnerId("rskj99");//商户编号
    params.setTestingEnv(false);//false是生产环境 true是测试环境
    params.setGatherGps(true);
    params.setGatherContact(true);
    params.setGatherCallRecord(true);

    //3、执行初始化
    BqsDF.initialize(this, params);
    //采集通讯录
    BqsDF.commitContactsAndCallRecords(true, true);
    BqsDF.commitLocation();
    //BqsDF.commitLocation(longitude, latitude);

    //4、提交tokenkey到贵公司服务器
    String tokenKey = BqsDF.getTokenKey();
    LogUtils.Log("TTTT", "tokenKey:" + tokenKey);
    MyApplication.getSP(this).setTokenKey(tokenKey);

    //注意：上传tokenkey时最好再停留几百毫秒的时间（给SDK预留上传设备信息的时间）
    new CountDownTimer(500, 500) {
      @Override public void onTick(long l) {
      }

      @Override public void onFinish() {
        submitTokenkey();
      }
    }.start();
  }

  //#mark - OnBqsDFListener
  //设备采集成功
  @Override public void onSuccess(String tokenKey) {
    //回调的tokenkey和通过BqsDF.getTokenKey()拿到的值都是一样的
    LogUtils.Log("白骑士SDK采集设备信息成功 tokenkey=" + tokenKey);
  }

  //设备采集失败
  @Override public void onFailure(String resultCode, String resultDesc) {
    LogUtils.Log("白骑士SDK采集设备信息失败 resultCode=" + resultCode + " resultDesc=" + resultDesc);
  }

  /**
   * 提交tokenkey到贵公司服务器
   */
  private void submitTokenkey() {
    String tokenkey = BqsDF.getTokenKey();

    LogUtils.Log("提交tokenkey:" + tokenkey);
    //发起Http请求
    //....

  }

  /**
   * 显示进度框
   */
  public void showProgressDialog(String msg) {
    if (progressDialog == null) {
      initProgressDialog(msg);
    }
    try {
      progressDialog.setCanceledOnTouchOutside(false);
      progressDialog.setCancelable(true);
      progressDialog.show();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  /**
   * 显示进度框，外面点击不取消
   */
  public void showProgressDialogUnCancle(String msg) {
    if (progressDialog == null) {
      initProgressDialog(msg);
    }
    try {
      progressDialog.setTitle(msg);
      progressDialog.setCanceledOnTouchOutside(false);
      progressDialog.setCancelable(false);
      progressDialog.show();
    } catch (Exception e) {
      LogUtils.Log(TAG, e.getMessage());
    }
  }

  public void progressDialogDismiss() {
    try {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
      }
    } catch (IllegalArgumentException e) {
      LogUtils.Log(TAG, e.getMessage());
    }
  }

  /**
   * 进度框是否显示
   */
  public boolean isProgressShowing() {
    return progressDialog.isShowing();
  }

  public void initProgressDialog(String msg) {
    dialogView = inflater.inflate(R.layout.yyloading_dialog, null);// 得到加载view
    layout = (LinearLayout) dialogView.findViewById(R.id.dialog_view);// 加载布局
    tipTextView = (TextView) dialogView.findViewById(R.id.tipTextView);
    img = (ImageView) dialogView.findViewById(R.id.img);

    if (TextUtils.isEmpty(msg)) {
      tipTextView.setText("请稍后...");// 设置加载信息
    } else {
      tipTextView.setText(msg);// 设置加载信息
    }

    //        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "rotation", 0.0f, 360F);
    //        animator.setRepeatCount(ObjectAnimator.INFINITE);
    //        animator.setDuration(1500).start();

    progressDialog = new Dialog(this, R.style.myDialogTheme);// 创建自定义样式dialog

    progressDialog.setCancelable(false);// 不可以用“返回键”取消
    progressDialog.setContentView(layout,
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
  }

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
}
