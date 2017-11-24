package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import consumer.fin.rskj.com.library.utils.Constant;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.PurposeListAdapter;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.message.MainMessage;
import consumer.fin.rskj.com.library.module.BankCardItem;
import consumer.fin.rskj.com.library.module.LoanPurpose;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.CountDownTimer;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

/**
 * Created by HP on 2017/7/28.
 * 借款借据
 */

public class LoanCredentialsActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = LoanCredentialsActivity.class.getSimpleName();
  private TopNavigationView2 topNavigationView2;
  private Button next_step;
  private Intent intent;

  private String selected = "";
  private String data;
  private String applyAmt_String;
  /**
   * 姓名
   */
  private TextView customerName;
  /**
   * 贷款金额
   */
  private TextView applyAmt;
  /**
   * 贷款期限
   */
  private TextView period;
  /**
   * 起止时间
   */
  private TextView s_data;
  /**
   * 日利率
   */
  private TextView dayRate;
  /**
   * 还款方式
   */
  private TextView repayName;
  /**
   * 还款银行
   */
  private TextView repayBankName;
  /**
   * 放款银行
   */
  private TextView payLoanBankName;
  /**
   * 收款银行卡
   */
  private TextView shoukuan_bank;
  /**
   * 短信验证码
   */
  private EditText sms_code;
  /**
   * 获取验证码
   */
  private TextView mYzmBtn;
  /**
   * 合同url
   */
  private String contractUrl;
  private TextView check_contact;
  private CheckBox checkbox;

  private String repayWayId;
  private String loanterm;

  String bankMsg;// bang

  private long surplusTime = 0;
  private long allsurplusTime = 120000;
  private CountDownTimer timer;

  private String fundId;
  private String productId;
  /**
   * 合同协议h5地址
   */
  private String contractProtocolUrl;
  /**
   * 验证交易密码  1：需要，0：不需要
   */
  private String validatePwdFlag = "";
  /**
   * 贷款申请表
   */
  private String contractUrl1;
  /**
   * CFCA项目编码
   */
  private String projectCode;
  /**
   * CFCA开户用户Id
   */
  private String userId;

  private PopupWindow mLinesPopWindow;
  private ArrayList<LoanPurpose> arrayList = new ArrayList();
  private TextView loan_purpose;
  private String purpose;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loancredit);
    data = getIntent().getStringExtra("data");
    LogUtils.d(TAG, "data = " + data);
    if (TextUtils.isEmpty(data)) {
      return;
    }
    generateCredit();
  }

  @Override public void init() {
    topNavigationView2 = (TopNavigationView2) findViewById(R.id.topbar);
    topNavigationView2.setClickListener(new TopNavigationView2.NavigationViewClickListener() {
      @Override public void onLeftClick() {
        finish();
      }

      @Override public void onRightClick() {

      }
    });
    customerName = (TextView) findViewById(R.id.customerName);
    applyAmt = (TextView) findViewById(R.id.applyAmt);
    period = (TextView) findViewById(R.id.period);
    s_data = (TextView) findViewById(R.id.s_data);
    dayRate = (TextView) findViewById(R.id.dayRate);
    repayName = (TextView) findViewById(R.id.repayName);
    repayBankName = (TextView) findViewById(R.id.repayBankName);
    payLoanBankName = (TextView) findViewById(R.id.payLoanBankName);
    shoukuan_bank = (TextView) findViewById(R.id.shoukuan_bank);
    mYzmBtn = (TextView) findViewById(R.id.mYzmBtn);
    mYzmBtn.setOnClickListener(this);
    check_contact = (TextView) findViewById(R.id.check_contact);
    check_contact.setOnClickListener(this);
    checkbox = (CheckBox) findViewById(R.id.checkbox);
    sms_code = (EditText) findViewById(R.id.sms_code);
    next_step = (Button) findViewById(R.id.next_step);
    next_step.setOnClickListener(this);
    findViewById(R.id.choose_bank).setOnClickListener(this);
    findViewById(R.id.contractProtocol).setOnClickListener(this);
    findViewById(R.id.applytable).setOnClickListener(this);
    loan_purpose = (TextView) findViewById(R.id.loan_purpose);
  }

  @Override public void onClick(View v) {

    if (v.getId() == R.id.next_step) {
      if (TextUtils.isEmpty(sms_code.getText())) {
        showToast("请输入短信验证码", Constants.TOAST_SHOW_POSITION);
        return;
      }
      if (!checkbox.isChecked()) {
        showToast("请同意借款协议", Constants.TOAST_SHOW_POSITION);
        return;
      }
      //短信验证接口
      if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(projectCode)) {
        showToast("请先获取验证码", 1);
        return;
      }
      checkSMSCode(sms_code.getText().toString());
    }
    if (v.getId() == R.id.mYzmBtn) {
      mYzmBtn.setClickable(false);
      getMsmCode();
    }
    if (v.getId() == R.id.check_contact) {
      if (TextUtils.isEmpty(contractUrl)) {
        showToast("合同为空", Constants.TOAST_SHOW_POSITION);
        return;
      }
      LogUtils.d(TAG, "-----contractUrl--------" + contractUrl);

      intent = new Intent(this, HtmlActivity2.class);
      intent.putExtra("url", Constants.BASE_URL + contractUrl);
      intent.putExtra("title", "查看合同");
      startActivity(intent);
    }

    if (v.getId() == R.id.contractProtocol) {
      intent = new Intent(this, HtmlActivity2.class);
      intent.putExtra("url", Constants.BASE_URL + contractUrl);
      intent.putExtra("title", "借款合同");
      startActivity(intent);
    }

    if (v.getId() == R.id.applytable) {

      if (TextUtils.isEmpty(contractUrl1)) {
        showToast("借款申请表为空", Constants.TOAST_SHOW_POSITION);
        return;
      }
      intent = new Intent(this, HtmlActivity2.class);
      intent.putExtra("url", Constants.BASE_URL + contractUrl1);
      intent.putExtra("title", "贷款申请表");
      startActivity(intent);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 10 && resultCode == RESULT_OK) {

      BankCardItem item = data.getParcelableExtra("item");

      selected = item.getBankCode();
      shoukuan_bank.setText(item.getBankName() + item.getBankCode());
      LogUtils.d(TAG, "item = " + item.getBankName());
    }
  }

  /**
   * 获取短信验证码接口
   */
  private void getMsmCode() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M090904);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("cell", sharePrefer.getPhone());//手机号码

    LogUtils.d(TAG, "验证码: requestParams--->" + requestParams.toString());
    showLoading("正在加载...");

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        allsurplusTime = 120000;
        instantiationTime(allsurplusTime);
        timer.start();

        try {
          JSONObject jsonObject = new JSONObject(data);
          if ("000000".equals(jsonObject.getString("returnCode"))) {
            projectCode = jsonObject.getString("projectCode");
            userId = jsonObject.getString("userId");
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {

        mYzmBtn.setClickable(true);
        dismissLoading();
      }

      @Override public void onFailure(String errorMsg) {

        mYzmBtn.setClickable(true);
        dismissLoading();
      }
    });
  }

  /**
   * 初始化倒计时控件
   */
  private void instantiationTime(Long time) {
    LogUtils.d("debug", "time------>" + time + "");
    timer = new CountDownTimer(time, 1000) {

      @Override public void onTick(long millisUntilFinished) {
        //LogUtils.d("debug", "allsurplusTime------>" + allsurplusTime + "");
        mYzmBtn.setClickable(false);// 防止重复点击
        mYzmBtn.setText(millisUntilFinished / 1000 + "s");
        surplusTime = millisUntilFinished;
        //LogUtils.d("debug", "surplusTime------>" + surplusTime + "");
      }

      @Override public void onFinish() {
        surplusTime = 0;
        allsurplusTime = 120000;
        mYzmBtn.setText(R.string.yzm_btn_text);
        mYzmBtn.setClickable(true);
      }
    };
  }

  /**
   * 生产借款借据
   */
  private void generateCredit() {
    try {
      requestParams.clear();
      requestParams.put("transCode", Constants.TRANS_CODE_M100300);//接口标识
      requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
      requestParams.put("clientToken", sharePrefer.getToken());//登录后token
      JSONObject jsonObject = new JSONObject(data);
      requestParams.put("prodNum", jsonObject.getString("prodNum"));
      requestParams.put("applyAmt", jsonObject.getString("applyAmt"));
      loanterm = jsonObject.getInt("period") + "";
      requestParams.put("period", loanterm);
      requestParams.put("repayMode", jsonObject.getString("repayMode"));
      requestParams.put("repayName", jsonObject.getString("repayName"));
      repayWayId = jsonObject.getString("repayWayId");
      requestParams.put("repayWayId", repayWayId);
      applyAmt_String = jsonObject.getString("applyAmt");
      fundId = jsonObject.getString("fundId");
      productId = jsonObject.getString("productId");
      purpose = jsonObject.getString("purpose");
      loan_purpose.setText(purpose);
      requestParams.put("purpose", purpose);//借款用途
    } catch (Exception e) {
      e.printStackTrace();
    }
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "生成借据: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "生成借据: data--->" + data);
        updateUi(data);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "生成借据: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "生成借据: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * 借款确认接口
   */
  private void confirmLoan() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M090902);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");

    requestParams.put("productId", productId);
    requestParams.put("fundId", fundId);
    requestParams.put("applyAmt", applyAmt_String);
    requestParams.put("cell", sharePrefer.getPhone());
    requestParams.put("purpose", /*loan_purpose.getText().toString()*/purpose);
    requestParams.put("repayWayId", repayWayId);
    requestParams.put("smsCode", sms_code.getText().toString());
    requestParams.put("loanterm", loanterm);
    requestParams.put("projectCode", projectCode);
    requestParams.put("cfcaUId", userId);

    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "借款确认: requestParams--->" + requestParams.toString());

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "借款确认: data--->" + data);
        MainMessage mainMessage = new MainMessage();

        try {
          JSONObject jsonObject = new JSONObject(data);
          if ("000000".equals(jsonObject.getString("returnCode")) && "00".equals(
              jsonObject.getString("flag"))) {
            mainMessage.setUrl(Constants.BASE_URL
                + sharePrefer.getPayingStatus()
                + "?projectId="
                + jsonObject.getString("projectId"));
            mainMessage.setTitle("借款中");
            EventBus.getDefault().post(mainMessage);
            Intent intent = new Intent(LoanCredentialsActivity.this, WebViewActivity.class);
            startActivity(intent);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "借款确认: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "借款确认: errorMsg--->" + errorMsg);
      }
    });
  }

  private void updateUi(String data) {
    /**
     * customerName	客户名称      identityCardNo	身份证号
     applyAmt	贷款金额         period	贷款期限
     startDate	起始日期         endDate	结束日期
     dayRate	日利率         repayMode	还款方式
     repayBankName	还款银行名称      repayBankId	还款银行卡号末四位
     payLoanBankName	放款银行     phone	手机号码
     contractUrl	合同url
     */
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(data);
      customerName.setText(jsonObject.getString("customerName"));

      applyAmt.setText(applyAmt_String);
      period.setText(jsonObject.getString("period") + "个月");
      s_data.setText(jsonObject.getString("startDate") + " - " + jsonObject.getString("endDate"));
      dayRate.setText(jsonObject.getString("dayRate") + "%");
      repayName.setText(jsonObject.getString("repayName"));

      bankMsg = jsonObject.getString("repayBankName") + jsonObject.getString("repayBankId");
      repayBankName.setText(bankMsg);
      payLoanBankName.setText(jsonObject.getString("payLoanBankName"));

      contractUrl = jsonObject.getString("contractUrl");
      LogUtils.d(TAG, "-----contractUrl == " + contractUrl);
      shoukuan_bank.setText(
          jsonObject.getString("repayBankName") + jsonObject.getString("repayBankId"));
      //contractProtocolUrl = jsonObject.getString("contractUrl");
      contractUrl1 = jsonObject.getString("contractUrl1");
      LogUtils.d(TAG, "contractUrl1 == " + contractUrl1);
    } catch (JSONException e) {
      LogUtils.e("error", "数据解析有误" + e.toString());
    }
  }

  /**
   * 资金方产品进件信息  进件页面
   */
  private void m000191() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M000191);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");
    requestParams.put("productId", productId);
    requestParams.put("fundId", fundId);

    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "m000191: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "m000191: onSuccess--->" + data);

        try {
          JSONObject jsonObject = new JSONObject(data);
          validatePwdFlag = jsonObject.getString("validatePwdFlag");
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "m000191: onError--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "m000191: onFailure--->" + errorMsg);
      }
    });
  }

  /**
   * 验证码检查
   */
  private void checkSMSCode(String smsCode) {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_CFCA0003);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");
    requestParams.put("checkCode", smsCode);
    requestParams.put("cfcauid", userId);
    requestParams.put("projectCode", projectCode);

    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "CFCA0003: requestParams--->" + requestParams.toString());

    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        try {
          dismissLoading();
          LogUtils.d(TAG, "CFCA0003: onSuccess--->" + data);
          JSONObject jsonObject = new JSONObject(data);
          if ("000000".equals(jsonObject.getString("returnCode"))) {
            showPWDDialog(new FinishCallBackImpl() {
              @Override public void finishCallBack(String data) {
                //校验支付密码
                checkPayPwd(data);
              }
            });
          } else {
            showToast(jsonObject.getString("returnMsg"), Constants.TOAST_SHOW_POSITION);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "CFCA0003: onError--->" + errorMsg);
        //Toast.makeText(BaseActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "CFCA0003: onFailure--->" + errorMsg);
        //Toast.makeText(BaseActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * M000192 支付密码验证
   */
  public void checkPayPwd(String paymentPassword) {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M000192);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");

    requestParams.put("paymentPassword", paymentPassword);

    showLoading("正在加载...");

    LogUtils.d(TAG, "支付密码验证: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissPWDDialog();
        dismissLoading();
        LogUtils.d(TAG, "支付密码验证: data--->" + data);
        //{"transCode":"M000192","channelNo":"3","returnCode":"000000","returnMsg":"OK","flag":"0"}

        try {
          JSONObject jsonObject = new JSONObject(data);
          if ("1".equals(jsonObject.getString("flag"))) {
            //验证密码正确 确认借款
            confirmLoan();
          } else {
            showToast("交易密码错误", 1);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissPWDDialog();
        dismissLoading();
        LogUtils.d(TAG, "支付密码验证: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissPWDDialog();
        dismissLoading();
        LogUtils.d(TAG, "支付密码验证: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * 借款用途
   */
  private void M000171() {
    requestParams.clear();
    requestParams.put("transCode", "M000171");//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");
    requestParams.put("classIds", "AppLoanUseType");
    requestParams.put("indexNo", "0");
    requestParams.put("pageSize", "500");

    showLoading("正在加载...");

    LogUtils.d(TAG, "借款用途: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "借款用途: data--->" + data);

        try {
          JSONObject jsonObject = new JSONObject(data);
          JSONArray jsonArray = jsonObject.getJSONArray("rows");
          for (int i = 0; i < jsonArray.length(); i++) {
            LoanPurpose loanPurpose = new LoanPurpose();
            loanPurpose.setEnumName(jsonArray.getJSONObject(i).getString("enumName"));
            loanPurpose.setEnumId(jsonArray.getJSONObject(i).getString("enumId"));
            loanPurpose.setEnumValue(jsonArray.getJSONObject(i).getString("enumValue"));
            loanPurpose.setEnumOrder(jsonArray.getJSONObject(i).getInt("enumOrder"));
            arrayList.add(loanPurpose);
          }

          LoanPurpose purpose = new LoanPurpose();
          purpose.setEnumOrder(arrayList.size() + 1);
          purpose.setEnumName("取消");
          purpose.setEnumId("1111111111");
          purpose.setEnumValue("取消");
          arrayList.add(purpose);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "借款用途: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "借款用途: errorMsg--->" + errorMsg);
      }
    });
  }

  private void initAllLinesPopWindow() {
    // 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
    LayoutInflater inflater = LayoutInflater.from(this);
    View contentView = inflater.inflate(R.layout.popwindow_layout, null);
    mLinesPopWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT, false);
    // 实例化一个ColorDrawable颜色为半透明
    // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
    mLinesPopWindow.setBackgroundDrawable(new ColorDrawable(0x60000000));
    // 设置SelectPicPopupWindow弹出窗体可点击
    mLinesPopWindow.setFocusable(false);
    // 刷新状态
    mLinesPopWindow.update();
    ListView mListView = (ListView) contentView.findViewById(R.id.basic_all_lines_listview);
    PurposeListAdapter adapter = new PurposeListAdapter(this, arrayList);
    mListView.setAdapter(adapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if (position == (arrayList.size() - 1)) {
          if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
            mLinesPopWindow.dismiss();
          }
        } else {
          if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
            mLinesPopWindow.dismiss();
            loan_purpose.setText(arrayList.get(position).getEnumName());
            LogUtils.d("LoanCredentialsActivity",
                "getEnumName = " + arrayList.get(position).getEnumName());
          }
        }
      }
    });
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
      mLinesPopWindow.dismiss();
      mLinesPopWindow = null;
    }
    return super.onTouchEvent(event);
  }
}
