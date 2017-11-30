package consumer.fin.rskj.com.library.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.activitys.BaseActivity;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.RegexUtils;
import consumer.fin.rskj.com.library.utils.Util;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity implements OnClickListener, TextWatcher {

  public static final String TAG = RegisterActivity.class.getSimpleName();
  private EditText edit_phone;//手机号
  private EditText codeEdit;//验证码
  private EditText passEdit;//密码
  //	private ImageView clear; //清除
  private Button registBtn;//注册按钮
  //	private TextView login_now;//登录
  private CheckBox checkall;

  private EditText recommend_code;
  private Button get_code;//得到验证码

  private TextView link_protocol;//用户协议
  private TextView serviceTel; //客户电话
  private ImageView backBtn;//返回键
  private CheckBox show;
  private Timer timer;
  private final int NUM = 120;//重新得到验证码间隔时间
  private int curNum = 120;//重新得到验证码剩余时间
  private String channelCode; //渠道码

  private int currentTime;
  Intent intent;

  private boolean getCode = false;//是否获取了验证码  并且没有过期

  private Handler handler = new Handler() {
    public void handleMessage(android.os.Message msg) {
      int what = msg.what;
      if (what == 0) {
        if (null != timer) {
          timer.cancel();
        }
        get_code.setText("获取验证码");
        get_code.setTextColor(getResources().getColor(R.color.colorAccent));
        get_code.setEnabled(true);

        getCode = false;
      } else {
        get_code.setText(curNum + " s");
        get_code.setTextColor(getResources().getColor(R.color.colorAccent));
        get_code.setEnabled(false);

        getCode = true;
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    initViews();
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override public void init() {

  }

  private void initViews() {

    show = (CheckBox) findViewById(R.id.show);
    show.setOnClickListener(this);

    recommend_code = (EditText) findViewById(R.id.recommend_code);

    get_code = (Button) findViewById(R.id.get_code);
    get_code.setOnClickListener(this);
    edit_phone = (EditText) findViewById(R.id.edit_phone);
    //		clear = (ImageView) findViewById(R.id.clear);
    //		clear.setOnClickListener(this);

    codeEdit = (EditText) findViewById(R.id.edit_code);
    passEdit = (EditText) findViewById(R.id.register_password);

    registBtn = (Button) findViewById(R.id.register_btn);
    registBtn.setOnClickListener(this);

    link_protocol = (TextView) findViewById(R.id.link_protocol);
    //		link_protocol.setText(Html.fromHtml(getString(R.string.register_protocol)));

    link_protocol.setOnClickListener(this);
    checkall = (CheckBox) findViewById(R.id.checkall);
  }

  /**
   * 设置注册按钮可点击
   */
  private void registEnable() {
    String phone = edit_phone.getText().toString();
    String pass = passEdit.getText().toString();
    String vCode = codeEdit.getText().toString();

    if (!TextUtils.isEmpty(phone)) {
      get_code.setEnabled(true);
    } else {
      get_code.setEnabled(false);
    }

    if ((!TextUtils.isEmpty(phone)) && (!TextUtils.isEmpty(pass)) && (!TextUtils.isEmpty(vCode))) {
      registBtn.setEnabled(true);
      return;
    }
    registBtn.setEnabled(false);
  }

  @Override public void onClick(View v) {

    if (v.getId() == R.id.show) {
      if (show.isChecked()) {
        //显示密码
        passEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      } else {
        //隐藏密码
        passEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
      }
      //passEdit.setSelection(password.length());
    }

    if (v.getId() == R.id.register_btn) {

      doRegistPost();
    }
    if (v.getId() == R.id.link_protocol) {

      //intent = new Intent(this, HtmlActivity.class);
      //intent.putExtra("url", HttpContent.REGISTER_PROTOCOL);
      //startActivity(intent);
    }
    if (v.getId() == R.id.get_code) {
      Editable phoneNum = edit_phone.getText();

      if (TextUtils.isEmpty(phoneNum)) {
        //Toast.makeText(this,"请输入手机号",	Toast.LENGTH_SHORT).show();
        showToast("请输入手机号", Toast.LENGTH_SHORT);
        return;
      }

      if (!RegexUtils.isMatchPhoneNum(phoneNum.toString())) {
        //Toast.makeText(this,"请输入正确的手机号",	Toast.LENGTH_SHORT).show();
        showToast("请输入正确的手机号", Toast.LENGTH_SHORT);
        return;
      }

      getVerifyCode();
    }
  }

  private void startTimer() {
    curNum = NUM;
    timer = null;
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override public void run() {
        handler.sendEmptyMessage(--curNum);
      }
    }, 0, 1000);
  }

  //获取验证码
  private void getVerifyCode() {
    //设置post表单
    requestParams.clear();
    String phone = edit_phone.getText().toString();
    if (!RegexUtils.isMatchPhoneNum(phone)) {
      //Toast.makeText(this,"请输入正确的手机号", Toast.LENGTH_SHORT).show();
      showToast("请输入正确的手机号", Toast.LENGTH_SHORT);
      return;
    }
    showLoading(getResources().getString(R.string.dialog_loading));
    okHttpRequestManager.requestAsyn(Constants.HTTP_REGISTER_CODE + phone,
        OkHttpRequestManager.TYPE_RESTFUL_GET, requestParams, new ReqCallBack<String>() {

          @Override public void onReqSuccess(String result) {
            LogUtils.d(TAG, "getVerifyCode result = " + result);
            dismissLoading();
            Gson gson = new GsonBuilder().create();
            CommonResponse requestResult = gson.fromJson(result, CommonResponse.class);

            if (null != requestResult && "success".equals(requestResult.getCode())) {
              get_code.setText(NUM + " s");
              get_code.setTextColor(getResources().getColor(R.color.colorAccent));

              startTimer();

              handler.sendEmptyMessageDelayed(10, 100);
            } else {
              showToast("失败：" + requestResult.getMessage(), Toast.LENGTH_LONG);
              handler.sendEmptyMessageDelayed(0, 100);
            }
          }

          @Override public void onReqFailed(String errorMsg) {
            getCode = false;
            dismissLoading();
            registBtn.setEnabled(true);
            LogUtils.d(TAG, "getVerifyCode result = " + errorMsg);
            showToast(errorMsg, Toast.LENGTH_SHORT);
          }
        });
  }

  //调用注册接口
  private void doRegistPost() {
    Editable phone = edit_phone.getText();
    Editable code = codeEdit.getText();
    Editable pass = passEdit.getText();

    if (TextUtils.isEmpty(phone) || !RegexUtils.isMatchPhoneNum(phone.toString())) {
      showToast("请输入正确的手机号", Toast.LENGTH_LONG);
      //edit_phone.setAnimation(Util.shakeAnimation(10));
      return;
    }

    if (!getCode) {
      showToast("请先获取验证码", Toast.LENGTH_SHORT);
      return;
    }

    if (TextUtils.isEmpty(code) || code.length() < 4) {
      showToast("请输入正确短信验证码", Toast.LENGTH_SHORT);
      //codeEdit.setAnimation(Util.shakeAnimation(10));
      return;
    }

    if (TextUtils.isEmpty(pass)) {
      showToast("密码不能为空", Toast.LENGTH_SHORT);
      return;
    }

    if (!RegexUtils.passwordMatcher(pass.toString())) {
      showToast("请输入6-20位的数字和密码混合模式", Toast.LENGTH_SHORT);
      return;
    }

    if (!checkall.isChecked()) {
      showToast("请同意注册协议", Toast.LENGTH_SHORT);
      return;
    }

    if (!TextUtils.isEmpty(recommend_code.getText())) {
      if (recommend_code.getText().length() == 10) {
        channelCode = recommend_code.getText().toString();
      } else {
        channelCode = "";
        //Toast.makeText(this,"请输入10位渠道代码",Toast.LENGTH_SHORT).show();
        showToast("请输入10位渠道代码", Toast.LENGTH_SHORT);
        return;
      }
    } else {
      channelCode = "";
      //			channelCode = "1002020001";
    }

    showLoading(getResources().getString(R.string.dialog_loading));

    //先MD5加密,再Base64编码
    //String password = Base64Utils.getDecodeMD5String(pass);

    //密码进行 md5 加密
    //pass = MD5.getMD5(pass);
    LogUtils.d(TAG, "===phone== " + phone);
    LogUtils.d(TAG, "===password== " + pass);
    LogUtils.d(TAG, "===code== " + code);

    /**
     * 设置post表单 参数
     * 验证码	verifyCode
     密码	pwd	密码，长度要求至少6位；要同时包含大写、小写字母和数字长度限制：[6,64]	 MD5加密
     email
     手机号	telNo
     用户来源	userSource	固定值：1  标识用户来源
     描述	extInfo	固定值：""
     */
    requestParams.clear();
    requestParams.put("mobilePhone", phone.toString());
    requestParams.put("verificaCode", code.toString());
    requestParams.put("password", pass.toString());
    requestParams.put("channelCode", channelCode);
    requestParams.put("registSource", "4");

    if (!TextUtils.isEmpty(channelCode)) {
      requestParams.put("channelCode", channelCode);
      requestParams.put("marketProjectCode", "youyuditui");
    }

    //返回值  {"result":"success","data":public_addr,"message":""}
    okHttpRequestManager.requestAsyn(Constants.HTTP_REGIST, OkHttpRequestManager.TYPE_POST_JSON,
        requestParams, new ReqCallBack<String>() {

          @Override public void onReqSuccess(String result) {
            LogUtils.d(TAG, "doRegistPost onReqSuccess = " + result);
            dismissLoading();

            try {
              JSONObject jsonObject = new JSONObject(result);

              if ("success".equals(jsonObject.getString("code"))) {
                showToast("注册成功", Toast.LENGTH_SHORT);
                //注册成功后 回到登录页面
                finish();
              } else {
                showToast(jsonObject.getString("message"), Toast.LENGTH_SHORT);
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override public void onReqFailed(String errorMsg) {
            LogUtils.d(TAG, "doRegistPost onReqFailed = " + errorMsg);
            dismissLoading();
            showToast(errorMsg, Toast.LENGTH_SHORT);
          }
        });
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override public void afterTextChanged(Editable s) {
    registEnable();
  }
}
