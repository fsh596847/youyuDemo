package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.message.MainMessage;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.CountDownTimer;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

/**
 * Created by HP on 2017/9/04.
 * 修改银行卡
 */

public class ChangeBankActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = ChangeBankActivity.class.getSimpleName();

  private TopNavigationView2 topbar;
  private Intent intent;
  /**
   * 姓名
   */
  private EditText name;
  /**
   * 身份证
   */
  private EditText id_num;
  /**
   * 银行卡号
   */
  private EditText bank_num;
  /**
   * 手机号
   */
  private EditText phone;
  /**
   * 验证码
   */
  private TextView mYzmBtn;
  /**
   * 短信验证码
   */
  private EditText validateCode;
  /**
   * 提交
   */
  private Button commit;
  private long surplusTime = 0;
  private long allsurplusTime = 120000;
  private CountDownTimer timer;
  private String url;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rskj_activity_changebank);
    m000139();
  }

  @Override
  public void init() {
    topbar = (TopNavigationView2) findViewById(R.id.topbar);
    topbar.setClickListener(new TopNavigationView2.NavigationViewClickListener() {
      @Override
      public void onLeftClick() {
        finish();
      }

      @Override
      public void onRightClick() {

      }
    });

    name = (EditText) findViewById(R.id.name);
    id_num = (EditText) findViewById(R.id.id_num);
    bank_num = (EditText) findViewById(R.id.bank_num);
    phone = (EditText) findViewById(R.id.phone);

    mYzmBtn = (TextView) findViewById(R.id.mYzmBtn);
    mYzmBtn.setOnClickListener(this);
    validateCode = (EditText) findViewById(R.id.validateCode);

    findViewById(R.id.contractProtocol).setOnClickListener(this);

    commit = (Button) findViewById(R.id.commit);
    commit.setOnClickListener(this);

    if (TextUtils.isEmpty(sharePrefer.getCustName()) || TextUtils.isEmpty(
        sharePrefer.getIdCardNum())) {
      getConsumerMsg(new FinishCallBackImpl() {
        @Override
        public void finishCallBack(String data) {

          if (TextUtils.isEmpty(sharePrefer.getCustName())) {
            name.setText("");
            name.setEnabled(true);
          } else {
            name.setText(sharePrefer.getCustName());
            name.setEnabled(false);
          }

          if (TextUtils.isEmpty(sharePrefer.getIdCardNum())) {
            id_num.setText("");
            id_num.setEnabled(true);
          } else {
            id_num.setText(sharePrefer.getIdCardNum());
            id_num.setEnabled(false);
          }
        }
      });
    } else {
      name.setText(sharePrefer.getCustName());
      name.setEnabled(false);
      id_num.setText(sharePrefer.getIdCardNum());
      id_num.setEnabled(false);
    }
  }

  @Override
  public void onClick(View v) {

    if (v.getId() == R.id.commit) {

      if (TextUtils.isEmpty(validateCode.getText())) {
        showToast("验证码不能为空", Constants.TOAST_SHOW_POSITION);
        return;
      }

      if (TextUtils.isEmpty(phone.getText())) {
        showToast("手机号不能为空", Constants.TOAST_SHOW_POSITION);
        return;
      }

      checkSMSCode(validateCode.getText().toString(),
          phone.getText().toString(), "16", new FinishCallBackImpl() {
            @Override
            public void finishCallBack(String data) {
              changeBankCard();
            }
          });
    }

    if (v.getId() == R.id.mYzmBtn) {
      if (TextUtils.isEmpty(phone.getText())) {
        showToast("请输入手机号", Constants.TOAST_SHOW_POSITION);
        return;
      }
      mYzmBtn.setClickable(false);
      getMsmCode(phone.getText().toString());
    }

    if (v.getId() == R.id.contractProtocol) {
      if (!TextUtils.isEmpty(url)) {
        intent = new Intent(this, HtmlActivity2.class);
        intent.putExtra("url", Constants.BASE_URL + url);
        intent.putExtra("title", "征信授权书");
        startActivity(intent);
      }
    }
  }

  /**
   * 更换银行卡
   */
  private void changeBankCard() {
    if (TextUtils.isEmpty(name.getText())) {
      showToast("姓名不能为空", Constants.TOAST_SHOW_POSITION);
      return;
    }

    if (TextUtils.isEmpty(id_num.getText())) {
      showToast("身份证号不能为空", Constants.TOAST_SHOW_POSITION);
      return;
    }

    if (TextUtils.isEmpty(bank_num.getText())) {
      showToast("银行卡号不能为空", Constants.TOAST_SHOW_POSITION);

      return;
    }

    if (TextUtils.isEmpty(phone.getText())) {
      showToast("手机号不能为空", Constants.TOAST_SHOW_POSITION);
      return;
    }

    //M100141 新增银行卡
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100141);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);

    requestParams.put("bankid", bank_num.getText().toString());
    requestParams.put("cell", phone.getText().toString());
    requestParams.put("idnumber", id_num.getText().toString());
    requestParams.put("name", name.getText().toString());

    showLoading("正在加载...");

    LogUtils.d(TAG, "新增银行卡: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "新增银行卡: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "新增银行卡: errorMsg--->" + errorMsg);
      }

      @Override
      public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "新增银行卡: errorMsg--->" + errorMsg);
      }
    });
  }

  // 获取短信验证码接口 变更银行卡（场景码：16）
  private void getMsmCode(String mobile) {
    //        mYzmBtn.setClickable(false);
    //        showLoading("获取验证码中,请稍后...");
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_YZM);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("isNo", "16");
    requestParams.put("mobile", mobile);//手机号码
    showLoading("正在加载...");

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        allsurplusTime = 120000;
        instantiationTime(allsurplusTime);
        timer.start();
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {

        mYzmBtn.setClickable(true);
        dismissLoading();
      }

      @Override
      public void onFailure(String errorMsg) {

        mYzmBtn.setClickable(true);
        dismissLoading();
      }
    });
  }

  //初始化倒计时控件
  private void instantiationTime(Long time) {
    LogUtils.d("debug", "time------>" + time + "");
    timer = new CountDownTimer(time, 1000) {

      @Override
      public void onTick(long millisUntilFinished) {
        //LogUtils.d("debug", "allsurplusTime------>" + allsurplusTime + "");
        mYzmBtn.setClickable(false);// 防止重复点击
        mYzmBtn.setText(millisUntilFinished / 1000 + "s");
        surplusTime = millisUntilFinished;
        //LogUtils.d("debug", "surplusTime------>" + surplusTime + "");
      }

      @Override
      public void onFinish() {
        surplusTime = 0;
        allsurplusTime = 120000;
        mYzmBtn.setText(R.string.yzm_btn_text);
        mYzmBtn.setClickable(true);
      }
    };
  }

  /**
   * 协议查询
   */
  private void m000139() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M000139);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("protocolType", "3");
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "协议查询: requestParams--->" + requestParams.toString());

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "协议查询: data--->" + data);
        MainMessage mainMessage = new MainMessage();

        try {
          JSONObject jsonObject = new JSONObject(data);
          if ("000000".equals(jsonObject.getString("returnCode"))) {
            url = jsonObject.getString("url");
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "协议查询: errorMsg--->" + errorMsg);
      }

      @Override
      public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "协议查询: errorMsg--->" + errorMsg);
      }
    });
  }
}
