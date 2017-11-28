package com.zhongan.demo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.http.OkHttpRequestManager;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.impl.ReqCallBack;
import com.zhongan.demo.module.CommonResponse;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.RegexUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.TopNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import consumer.fin.rskj.com.consumerlibrary.*;
import consumer.fin.rskj.com.consumerlibrary.BuildConfig;
import consumer.fin.rskj.com.library.activitys.WebViewActivity;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.okhttp.callback.Callback;
import consumer.fin.rskj.com.library.utils.Constants;

import static consumer.fin.rskj.com.library.activitys.BaseActivity.sessionId;
import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;

public class LoginActivity extends BaseActivity implements OnClickListener {

  private static final String TAG = "LoginActivity";
  final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

  private TextView forgetPass;//忘记密码

  private Button login_button;//登录
  private TextView register_btn;//注册
  private CheckBox show;

  /**
   * 注册请求码
   */
  public static final int REQUST_REGIST = 100;

  private EditText user_phone;
  private ImageView clear; //清除
  private EditText user_password;

  private Intent intent;
  private Dialog dialog;//提示框
  private String mobilePhone;
  private Dialog mDialog;

  @Override protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    setContentView(R.layout.activity_login);

    mDialog = Util.createLoadingDialog(this, "数据加载中,请稍等...");

    setUI();
  }

  private void setUI() {

    show = (CheckBox) findViewById(R.id.show);
    show.setOnClickListener(this);

    login_button = (Button) findViewById(R.id.activity_login_btn);

    login_button.setOnClickListener(this);

    register_btn = (TextView) findViewById(R.id.register_btn);
    register_btn.setOnClickListener(this);

    forgetPass = (TextView) findViewById(R.id.forgetPass);
    forgetPass.setOnClickListener(this);

    user_phone = (EditText) findViewById(R.id.user_phone);
    if (!TextUtils.isEmpty(MyApplication.getSP(getApplicationContext()).getPhone())) {
      user_phone.setText(MyApplication.getSP(getApplicationContext()).getPhone());
    }
    //		clear = (ImageView) findViewById(R.id.clear);
    //		clear.setOnClickListener(this);
    user_password = (EditText) findViewById(R.id.user_password);
  }

  @Override protected void onActivityResult(int arg0, int arg1, Intent arg2) {
    super.onActivityResult(arg0, arg1, arg2);

    if (arg1 == RESULT_OK && arg0 == REQUST_REGIST) {
      setResult(RESULT_OK);
      finish();
    }
  }

  /**
   * 登录判断
   */
  private void login(final String phone, String password) {

    if (!RegexUtils.isMatchPhoneNum(phone)) {
      ToastUtils.showCenterToast("手机号码有误", this);
      return;
    }
    mDialog.show();
    paramsMap.clear();
    paramsMap.put("mobilePhone", phone);
    paramsMap.put("password", password);
    final String pPhone = phone;
    okHttpRequestManager.requestAsyn(HttpContent.HTTP_LOGIN, OkHttpRequestManager.TYPE_POST_JSON,
        paramsMap, new ReqCallBack<String>() {

          @Override public void onReqSuccess(String result) {
            LogUtils.Log(TAG, "login result = " + result);
            if (null == gson) {
              gson = new GsonBuilder().create();
            }

            try {
              requestResult = gson.fromJson(result, CommonResponse.class);
              if (null != requestResult && "success".equals(requestResult.getCode())) {
                //ToastUtils.showCenterToast("登录成功",LoginActivity.this);
                String memberId = (String) requestResult.getData().get("memberId");
                String token = (String) requestResult.getData().get("token");
                LogUtils.Log(TAG, "onReqSuccess memberId = " + memberId);
                LogUtils.Log(TAG, "onReqSuccess token = " + token);
                MyApplication.getSP(getApplicationContext()).setToken(token);
                MyApplication.getSP(getApplicationContext()).setMemId(memberId);
                MyApplication.getSP(getApplicationContext()).setPhone(pPhone);
                LogUtils.Log(TAG,
                    " gettoken = " + MyApplication.getSP(getApplicationContext()).getToken());
                getMemberProduct();
              } else {
                mDialog.cancel();
                ToastUtils.showCenterToast(requestResult.getMessage(), LoginActivity.this);
              }
            } catch (Exception e) {
              mDialog.cancel();
              ToastUtils.showCenterToast(e.getMessage(), LoginActivity.this);
            }
          }

          @Override public void onReqFailed(String errorMsg) {
            mDialog.cancel();
            LogUtils.Log(TAG, "onReqFailed result = " + errorMsg);
            ToastUtils.showCenterToast(errorMsg, LoginActivity.this);
          }
        });
  }

  /**
   * 获取会员身份
   */
  private void getMemberProduct() {
    paramsMap.clear();
    okHttpRequestManager.requestAsyn("/member/getMemberProduct",
        OkHttpRequestManager.TYPE_RESTFUL_GET, paramsMap, new ReqCallBack<String>() {
          @Override public void onReqSuccess(String result) {
            progressDialogDismiss();
            LogUtils.Log(TAG, "onReqSuccess result = " + result);

            try {
              JSONObject jsonObject = new JSONObject(result);

              if ("success".equals(jsonObject.getString("code"))) {
                MyApplication.getSP(LoginActivity.this)
                    .setIdentity(jsonObject.getJSONObject("data").getString("identity"));

                if (0 == jsonObject.getJSONObject("data").getInt("showTab")) {
                  MyApplication.getSP(LoginActivity.this).setShowTab(false);
                } else {
                  MyApplication.getSP(LoginActivity.this).setShowTab(true);
                }
                //消金2.0 需要打开
                getH5URL();
              } else {
                Toast.makeText(LoginActivity.this, jsonObject.getString("message"),
                    Toast.LENGTH_SHORT).show();
              }
              LogUtils.Log(TAG, "------finishCallBack-------");
            } catch (JSONException e) {
              mDialog.cancel();
              e.printStackTrace();
            }
          }

          @Override public void onReqFailed(String errorMsg) {
            mDialog.cancel();
            LogUtils.Log(TAG, "onReqFailed result = " + errorMsg);

            ToastUtils.showCenterToast(errorMsg, LoginActivity.this);
          }
        });
  }

  protected void getH5URL() {
    paramsMap.clear();
    paramsMap.put("projectType", "suixindai2.0");
    paramsMap.put("transCode", "M107102");//接口标识
    paramsMap.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    paramsMap.put("clientToken", MyApplication.getSP(this).getToken());//登录后token

    LogUtils.Log("getH5URL", "getH5URL data = " + paramsMap.toString());

    sendPostRequest(paramsMap, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        LogUtils.Log("getH5URL", "onSuccess data = " + data);

        try {
          JSONObject jsonObject = new JSONObject(data);

          LogUtils.Log(TAG, "sessionId = " + sessionId);
          MyApplication.getSP(LoginActivity.this).setSessionID(sessionId);

          JSONObject object = new JSONObject(jsonObject.getString("h5Urls"));
          String applyLoan = object.getString("applyLoan");

          String paymentStatus = object.getString("paymentStatus");//还款成功
          String payingStatus = object.getString("payingStatus");//放款中
          String cedStatus = object.getString("cedStatus");//授信审核中

          String helpCenter = object.getString("helpCenter");//帮助中心
          String userFeedbacks = object.getString("userFeedbacks");//用户反馈
          String protocolCenter = object.getString("protocolCenter");//协议
          String loanStatus = object.getString("loanStatus");//借款状态

          MyApplication.getSP(LoginActivity.this).setApplyLoan(applyLoan);
          MyApplication.getSP(LoginActivity.this).setHelpCenter(helpCenter);
          MyApplication.getSP(LoginActivity.this).setUserFeedbacks(userFeedbacks);
          MyApplication.getSP(LoginActivity.this).setProtocolCenter(protocolCenter);
          MyApplication.getSP(LoginActivity.this).setLoanStatus(loanStatus);

          MyApplication.getSP(LoginActivity.this).setPaymentStatus(paymentStatus);
          MyApplication.getSP(LoginActivity.this).setPayingStatus(payingStatus);
          MyApplication.getSP(LoginActivity.this).setCedStatus(cedStatus);

          LogUtils.Log("getH5URL", "applyLoan = " + applyLoan);
          MyApplication.getSP(LoginActivity.this).setFirstUrl(BASE_URL + applyLoan);

          LogUtils.Log(TAG, "sessionId = " + sessionId);
          MyApplication.getSP(LoginActivity.this).setSessionID(sessionId);
          MyApplication.getSP(getApplicationContext()).setLogin(true);

          mDialog.cancel();
          intent = new Intent(LoginActivity.this, MenuListActivity2.class);
          startActivity(intent);

          LoginActivity.this.finish();
        } catch (JSONException e) {
          mDialog.cancel();
          e.printStackTrace();
        }
      }

      @Override public void onFailure(String errorMsg) {
        mDialog.cancel();
        LogUtils.Log("getH5URL", "onFailure errorMsg = " + errorMsg);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        mDialog.cancel();
        LogUtils.Log("getH5URL", "onError errorMsg = " + errorMsg);
      }
    });
  }

  protected void sendPostRequest(Map<String, String> params, final ResultCallBack callBack) {

    params.put("version", BuildConfig.VERSION_NAME);

    LogUtils.Log(TAG, "requestParams--->" + params.toString());
    Log.d("debug", "sharePrefer.getSessionID() = " + MyApplication.getSP(this).getSessionID());
    HttpInfo httpInfo = HttpInfo.Builder().setUrl(Constants.REQUEST_URL)//请求URL
        .addParams(params).addHead("Cookie", MyApplication.getSP(this).getSessionID()).build();

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
            LogUtils.Log("debug", "onSuccess result------------->" + result);
            callBack.onSuccess(result);
          } else if ("E999985".equals(returnCode) || "ES00000303".equals(returnCode)) {
            progressDialogDismiss();
            LogUtils.Log("debug", "--------------token失效，或者用户未登录-------------");
            showToast(returnMsg, Constants.TOAST_SHOW_POSITION);
          } else {
            showToast(returnMsg, Constants.TOAST_SHOW_POSITION);
            callBack.onError(returnCode, returnMsg);
          }
        } catch (JSONException e) {
          LogUtils.Log("error", "数据解析有误" + e.toString());
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

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.show:
        if (show.isChecked()) {
          //显示密码
          user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
          //隐藏密码
          user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        break;
      case R.id.forgetPass://忘记密码 找回密码
        intent = new Intent(LoginActivity.this, GetPasswordActivity.class);
        startActivity(intent);
        break;
      case R.id.activity_login_btn://登录
        Editable phoneNum = user_phone.getText();
        Editable password = user_password.getText();
        if (TextUtils.isEmpty(phoneNum)) {
          ToastUtils.showCenterToast("请输入手机号码", this);
          return;
        }
        LogUtils.Log(TAG, "phoneNum = " + phoneNum.toString());
        LogUtils.Log(TAG, "password = " + password.toString());
        if (!RegexUtils.isMatchPhoneNum(phoneNum.toString())) {
          ToastUtils.showCenterToast("请输入正确的手机号码", this);
          user_phone.setAnimation(Util.shakeAnimation(10));
          return;
        }

        if (TextUtils.isEmpty(password)) {
          ToastUtils.showCenterToast("请输入密码", this);
          user_password.setAnimation(Util.shakeAnimation(10));
          return;
        }
        login(phoneNum.toString(), password.toString());
        break;
      case R.id.register_btn://注册
        intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUST_REGIST);
        break;
    }
  }
}

