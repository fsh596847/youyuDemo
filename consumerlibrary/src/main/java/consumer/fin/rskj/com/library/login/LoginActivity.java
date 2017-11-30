package consumer.fin.rskj.com.library.login;

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
import com.google.gson.Gson;
import consumer.fin.rskj.com.consumerlibrary.BuildConfig;
import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.activitys.BaseActivity;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.okhttp.callback.Callback;
import consumer.fin.rskj.com.library.utils.Constant;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import java.io.IOException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;

public class LoginActivity extends BaseActivity {

  private static final String TAG = LoginActivity.class.getSimpleName();
  final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
  /**
   * 忘记密码
   */
  private TextView forgetPass;
  /**
   * 登录
   */
  private Button login_button;
  /**
   * 注册
   */
  private TextView register_btn;
  private CheckBox show;
  /**
   * 注册请求码
   */
  public static final int REQUST_REGIST = 100;

  private EditText user_phone;
  /**
   * 清除
   */
  private ImageView clear;
  private EditText user_password;

  @Override protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    setContentView(R.layout.activity_login);
    setUI();
  }

  private void setUI() {
    show = (CheckBox) findViewById(R.id.show);
    login_button = (Button) findViewById(R.id.activity_login_btn);
    register_btn = (TextView) findViewById(R.id.register_btn);
    forgetPass = (TextView) findViewById(R.id.forgetPass);
    user_phone = (EditText) findViewById(R.id.user_phone);
    user_password = (EditText) findViewById(R.id.user_password);
    if (!TextUtils.isEmpty(sharePrefer.getPhone())) {
      user_phone.setText(sharePrefer.getPhone());
    }
    login_button.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        Editable phoneNum = user_phone.getText();
        Editable password = user_password.getText();
        Log.d(TAG, "phoneNum = " + phoneNum.toString());
        Log.d(TAG, "password = " + password.toString());
        login(phoneNum.toString(), password.toString());
      }
    });
    register_btn.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REQUST_REGIST);
      }
    });
  }

  @Override protected void onActivityResult(int arg0, int arg1, Intent arg2) {
    super.onActivityResult(arg0, arg1, arg2);
    if (arg1 == RESULT_OK && arg0 == REQUST_REGIST) {
      setResult(RESULT_OK);
      finish();
    }
  }

  @Override public void init() {

  }

  /**
   * 登录判断
   */
  private void login(final String phone, String password) {
    requestParams.clear();
    requestParams.put("mobilePhone", phone);
    requestParams.put("password", password);
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "登录: requestParams--->" + requestParams.toString());
    okHttpRequestManager.requestAsyn(Constants.LOGIN_URL, OkHttpRequestManager.TYPE_POST_JSON,
        requestParams, new ReqCallBack<String>() {
          @Override public void onReqSuccess(String data) {
            try {
              dismissLoading();
              LogUtils.d(TAG, "登录: data--->" + data);
              CommonResponse requestResult = new Gson().fromJson(data, CommonResponse.class);
              if (null != requestResult && "success".equals(requestResult.getCode())) {
                showToast("登录成功", 1);
                String memberId = (String) requestResult.getData().get("memberId");
                String token = (String) requestResult.getData().get("token");
                Log.d(TAG, "onReqSuccess memberId = " + memberId);
                Log.d(TAG, "onReqSuccess token = " + token);
                sharePrefer.setToken(token);
                sharePrefer.setMemId(memberId);
                sharePrefer.setPhone(phone);
                Log.d(TAG, " gettoken = " + sharePrefer.getToken());
                getMemberProduct();
              } else {
                dismissLoading();
                showToast(requestResult.getMessage(), Constants.TOAST_SHOW_POSITION);
              }
            } catch (Exception e) {
              dismissLoading();
              showToast(e.getMessage(), Constants.TOAST_SHOW_POSITION);
            }
          }

          @Override public void onReqFailed(String errorMsg) {
            dismissLoading();
            LogUtils.d(TAG, "登录: errorMsg--->" + errorMsg);
          }
        });
  }

  /**
   * 获取会员身份
   */
  private void getMemberProduct() {
    requestParams.clear();
    okHttpRequestManager.requestAsyn("/member/getMemberProduct",
        OkHttpRequestManager.TYPE_RESTFUL_GET, requestParams, new ReqCallBack<String>() {
          @Override public void onReqSuccess(String result) {
            dismissLoading();
            LogUtils.d(TAG, "onReqSuccess result = " + result);
            try {
              JSONObject jsonObject = new JSONObject(result);
              if ("success".equals(jsonObject.getString("code"))) {
                sharePrefer.setIdentity(jsonObject.getJSONObject("data").getString("identity"));
                if (0 == jsonObject.getJSONObject("data").getInt("showTab")) {
                  sharePrefer.setShowTab(false);
                } else {
                  sharePrefer.setShowTab(true);
                }
                //消金2.0 需要打开
                getH5URL();
              } else {
                Toast.makeText(LoginActivity.this, jsonObject.getString("message"),
                    Toast.LENGTH_SHORT).show();
              }
              LogUtils.d(TAG, "------finishCallBack-------");
            } catch (JSONException e) {
              dismissLoading();
              e.printStackTrace();
            }
          }

          @Override public void onReqFailed(String errorMsg) {
            dismissLoading();
            LogUtils.d(TAG, "onReqFailed result = " + errorMsg);
          }
        });
  }

  protected void getH5URL() {
    requestParams.clear();
    requestParams.put("projectType", "suixindai2.0");
    requestParams.put("transCode", Constants.TRANS_CODE_M107102);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    LogUtils.d("getH5URL", "getH5URL data = " + requestParams.toString());

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        try {
          JSONObject jsonObject = new JSONObject(data);

          LogUtils.d(TAG, "sessionId = " + sessionId);
          sharePrefer.setSessionID(sessionId);

          JSONObject object = new JSONObject(jsonObject.getString("h5Urls"));
          String applyLoan = object.getString("applyLoan");

          String paymentStatus = object.getString("paymentStatus");//还款成功
          String payingStatus = object.getString("payingStatus");//放款中
          String cedStatus = object.getString("cedStatus");//授信审核中

          String helpCenter = object.getString("helpCenter");//帮助中心
          String userFeedbacks = object.getString("userFeedbacks");//用户反馈
          String protocolCenter = object.getString("protocolCenter");//协议
          String loanStatus = object.getString("loanStatus");//借款状态

          sharePrefer.setApplyLoan(applyLoan);
          sharePrefer.setHelpCenter(helpCenter);
          sharePrefer.setUserFeedbacks(userFeedbacks);
          sharePrefer.setProtocolCenter(protocolCenter);
          sharePrefer.setLoanStatus(loanStatus);

          sharePrefer.setPaymentStatus(paymentStatus);
          sharePrefer.setPayingStatus(payingStatus);
          sharePrefer.setCedStatus(cedStatus);

          LogUtils.d("getH5URL", "applyLoan = " + applyLoan);
          sharePrefer.setFirstUrl(BASE_URL + applyLoan);

          LogUtils.d(TAG, "sessionId = " + sessionId);
          sharePrefer.setSessionID(sessionId);
          sharePrefer.setLogin(true);
          dismissLoading();
          //intent = new Intent(LoginActivity.this, MenuListActivity2.class);
          //startActivity(intent);
          //LoginActivity.this.finish();
        } catch (JSONException e) {
          dismissLoading();
          e.printStackTrace();
        }
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d("getH5URL", "onFailure errorMsg = " + errorMsg);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d("getH5URL", "onError errorMsg = " + errorMsg);
      }
    });
  }

  protected void sendPostRequest(Map<String, String> params, final ResultCallBack callBack) {
    params.put("version", BuildConfig.VERSION_NAME);
    LogUtils.d(TAG, "requestParams--->" + params.toString());
    Log.d("debug", "sharePrefer.getSessionID() = " + sharePrefer.getSessionID());
    HttpInfo httpInfo = HttpInfo.Builder().setUrl(Constants.REQUEST_URL)//请求URL
        .addParams(params).addHead("Cookie", sharePrefer.getSessionID()).build();
    OkHttpUtil.getDefault(this).doPostAsync(httpInfo, new Callback() {
      @Override public void onSuccess(HttpInfo info) throws IOException {
        try {
          String result = info.getRetDetail().toString();
          Log.d(TAG, "onSuccess result = " + result);
          JSONObject jsonObject = new JSONObject(result);
          String returnCode = jsonObject.getString("returnCode");
          String returnMsg = jsonObject.getString("returnMsg");
          if ("000000".equals(returnCode)) {
            Log.d(TAG, "onSuccess result------------->" + result);
            callBack.onSuccess(result);
          } else if ("E999985".equals(returnCode) || "ES00000303".equals(returnCode)) {
            dismissLoading();
            Log.d(TAG, "--------------token失效，或者用户未登录-------------");
            showToast(returnMsg, Constants.TOAST_SHOW_POSITION);
          } else {
            showToast(returnMsg, Constants.TOAST_SHOW_POSITION);
            callBack.onError(returnCode, returnMsg);
          }
        } catch (JSONException e) {
          Log.d(TAG, "数据解析有误" + e.toString());
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
}

