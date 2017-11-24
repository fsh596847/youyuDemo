package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import consumer.fin.rskj.com.consumerlibrary.BuildConfig;
import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.message.MainMessage;
import consumer.fin.rskj.com.library.module.PayModule;
import consumer.fin.rskj.com.library.module.Rows;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.CountDownTimer;
import consumer.fin.rskj.com.library.views.TopNavigationView2;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static consumer.fin.rskj.com.library.utils.Constants.REQUEST_URL;

/**
 * Created by HP on 2017/7/26.
 * 还款确认页面
 */

public class PaymentConfirmActivity extends BaseActivity {

  private static final String TAG = "PaymentConfirmActivity";
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private long surplusTime = 0;
  private long allsurplusTime = 120000;
  private CountDownTimer timer;
  private TopNavigationView2 topbar;
  private TextView getCode;
  private TextView repayAmt;
  private TextView card_type;
  private EditText sms_code;
  /**
   * 贷款id
   */
  private String loanId;
  /**
   * 实还金额
   */
  private String totalAmount;
  private String bankMsg;
  private String currentData;
  Intent intent;
  /**
   * 逾期还款 1 ，全额还款 2，账单还款 3
   */
  private int currentStatus;
  private ArrayList<String> list;
  private ArrayList<Rows> rows;
  private PayModule payModule;

  MainMessage mainMessage;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loanId = getIntent().getStringExtra("loanId");
    totalAmount = getIntent().getStringExtra("totalAmount");
    bankMsg = getIntent().getStringExtra("bankMsg");
    currentStatus = getIntent().getIntExtra("currentStatus", 0);
    list = getIntent().getStringArrayListExtra("repayingList");
    rows = getIntent().getParcelableArrayListExtra("rows");
    setContentView(R.layout.activity_paymentconfirm);
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    currentData = sDateFormat.format(new java.util.Date());
    LogUtils.d(TAG, "currentData =》 " + currentData);

    LogUtils.d(TAG, "rows =》 " + rows);
    LogUtils.d(TAG, "list =》 " + list);
    LogUtils.d(TAG, "currentStatus =》 " + currentStatus);
    LogUtils.d(TAG, "totalAmount =》 " + totalAmount);
  }

  @Override public void init() {
    topbar = (TopNavigationView2) findViewById(R.id.topbar);
    topbar.setClickListener(new TopNavigationView2.NavigationViewClickListener() {
      @Override public void onLeftClick() {
        finish();
      }

      @Override public void onRightClick() {

      }
    });
    getCode = (TextView) findViewById(R.id.getCode);
    getCode.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getCode.setClickable(false);
        getMsmCode();
      }
    });

    sms_code = (EditText) findViewById(R.id.sms_code);
    repayAmt = (TextView) findViewById(R.id.repayAmt);
    repayAmt.setText(totalAmount);
    card_type = (TextView) findViewById(R.id.card_type);
    card_type.setText(bankMsg);

    findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        if (TextUtils.isEmpty(sms_code.getText())) {
          showToast("请输入短信验证码", Constants.TOAST_SHOW_POSITION);
          return;
        }
        //先进行验证码校验
        checkSMSCode(sms_code.getText().toString(), "", "15", new FinishCallBackImpl() {
          @Override public void finishCallBack(String data) {
            doPayMethod();
          }
        });
      }
    });
  }

  /**
   * 执行还款 操作
   */
  private void doPayMethod() {
    if (currentStatus > 3 || currentStatus < 1) {
      showToast("状态异常", Constants.TOAST_SHOW_POSITION);
      return;
    }
    if (currentStatus == 1) {
      payDelay();//逾期还款
    }
    if (currentStatus == 2) {
      payAll();//全额还款接口
    }
    if (currentStatus == 3) {
      //            payBill();//全额还款接口
      new Thread() {
        @Override public void run() {
          payBill("");//全额还款接口
        }
      }.start();
    }
  }

  /**
   * 获取短信验证码接口 还款确认（场景码：15）
   */
  private void getMsmCode() {
    requestParams.clear();
    requestParams.put("transCode", /*Constants.TRANS_CODE_YZM*/"M000010");//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("isNo", "15");
    requestParams.put("mobile", sharePrefer.getPhone());//手机号码
    showLoading("正在加载...");
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        allsurplusTime = 120000;
        instantiationTime(allsurplusTime);
        timer.start();
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        getCode.setClickable(true);
        dismissLoading();
      }

      @Override public void onFailure(String errorMsg) {
        getCode.setClickable(true);
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
        getCode.setClickable(false);// 防止重复点击
        getCode.setText(millisUntilFinished / 1000 + "s");
        surplusTime = millisUntilFinished;
      }

      @Override public void onFinish() {
        surplusTime = 0;
        allsurplusTime = 120000;
        getCode.setText(R.string.yzm_btn_text);
        getCode.setClickable(true);
      }
    };
  }

  private void payBill(String a) {
    payModule = new PayModule();
    payModule.setRows(rows);
    payModule.setChannelNo(Constants.CHANNEL_NO);
    payModule.setClientToken(sharePrefer.getToken());
    payModule.setTransCode("M100800");
    payModule.setLegalPerNum("00001");
    payModule.setVersion(BuildConfig.VERSION_NAME);

    String json = new Gson().toJson(payModule);
    LogUtils.d(TAG, "json = " + json);

    //申明给服务端传递一个json串
    //创建一个OkHttpClient对象
    OkHttpClient okHttpClient = new OkHttpClient();
    //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
    RequestBody requestBody = RequestBody.create(JSON, json);
    //创建一个请求对象
    Request request = new Request.Builder().url(REQUEST_URL)
        .post(requestBody)
        .addHeader("Cookie", sharePrefer.getSessionID())
        .build();
    //发送请求获取响应
    try {
      Response response = okHttpClient.newCall(request).execute();
      //判断请求是否成功
      if (response.isSuccessful()) {
        //打印服务端返回结果
        String data = response.body().string();
        Log.i(TAG, "data  => " + data);
        goResultActivity(data);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //跳转结果页面
  private void goResultActivity(String data) {
    mainMessage = new MainMessage();
    intent = new Intent(PaymentConfirmActivity.this, WebViewActivity.class);
    try {
      JSONObject jsonObject = new JSONObject(data);
      if ("000000".equals(jsonObject.getString("returnCode"))) {
        mainMessage.setTitle("还款成功");
        mainMessage.setUrl(Constants.BASE_URL + sharePrefer.getPaymentStatus() + "?result=success");
        //intent.putExtra("title","还款成功");
        //intent.putExtra("url", Constants.BASE_URL + sharePrefer.getPaymentStatus()+ "?result=success");
      } else {
        mainMessage.setTitle("还款失败");
        mainMessage.setUrl(Constants.BASE_URL + sharePrefer.getPaymentStatus() + "?result=fail");
        //intent.putExtra("title","还款失败");
        //intent.putExtra("url",Constants.BASE_URL + sharePrefer.getPaymentStatus()+ "?result=failure");
      }
      EventBus.getDefault().post(mainMessage);
      startActivity(intent);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  //账单还款
  private void payBill() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100800);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");
    requestParams.put("rows", rows.toString());
    showLoading("正在加载...");

    LogUtils.d(TAG, "账单还款: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "账单还款: data--->" + data);
        goResultActivity(data);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "账单还款: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "账单还款: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * 全额还款
   * M100710 全部还款
   * loanId	贷款id
   * prepaymentType	提前还款类型
   * repayAmt	提前还款金额
   * perRepayDate	提前还款日期
   * penalty	违约金
   * perRepaymentReasons	提前还款原因
   * payPassword	支付密码
   * feeType	费用类型
   */

  private void payAll() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100710);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");
    requestParams.put("loanId", loanId);
    requestParams.put("perRepayDate", currentData);
    requestParams.put("prepaymentType", "1");
    requestParams.put("repayAmt", totalAmount);
    requestParams.put("perRepaymentReasons", "app");
    showLoading("正在加载...");
    LogUtils.d(TAG, "全部还款: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "全部还款: data--->" + data);
        goResultActivity(data);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "全部还款: errorMsg--->" + errorMsg);
        mainMessage.setTitle("还款失败");
        mainMessage.setUrl(Constants.BASE_URL + sharePrefer.getPaymentStatus() + "?result=fail");
        EventBus.getDefault().post(mainMessage);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "全部还款: onFailure--->" + errorMsg);
      }
    });
  }

  /**
   * 逾期还款
   * loanId	贷款ID
   * rows
   * repayingDetailId	还款计划明细ID
   * actualRepayAmt	实还金额
   * payPassword	支付密码
   */
  private void payDelay() {
    StringBuilder arrayList = new StringBuilder();
    for (int a = 0; a < list.size(); a++) {
      arrayList.append(list.get(a));
      if (a < list.size() - 1) {
        arrayList.append(",");
      }
    }

    LogUtils.d(TAG, "arrayList = " + arrayList);
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100724);//M100724 手机逾期还款
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", "00001");
    requestParams.put("loanId", loanId);
    requestParams.put("actualRepayAmt", totalAmount);
    requestParams.put("repayingDetailIds", arrayList.toString());
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "逾期还款: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        mainMessage = new MainMessage();
        intent = new Intent(PaymentConfirmActivity.this, WebViewActivity.class);
        LogUtils.d(TAG, "逾期还款: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          if ("000000".equals(jsonObject.getString("returnCode"))) {
            mainMessage.setTitle("还款成功");
            mainMessage.setUrl(
                Constants.BASE_URL + sharePrefer.getPaymentStatus() + "?result=success");
          } else {
            mainMessage.setTitle("还款失败");
            mainMessage.setUrl(
                Constants.BASE_URL + sharePrefer.getPaymentStatus() + "?result=fail");
          }
          EventBus.getDefault().post(mainMessage);
          startActivity(intent);
          finish();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "逾期还款: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "逾期还款: errorMsg--->" + errorMsg);
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
    requestParams.put("paymentPassword", "0");
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "支付密码验证: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        try {
          dismissPWDDialog();
          dismissLoading();
          LogUtils.d(TAG, "支付密码验证: data--->" + data);
          //{"transCode":"M000192","channelNo":"3","returnCode":"000000","returnMsg":"OK","flag":"0"}
          JSONObject jsonObject = new JSONObject(data);
          if ("0".equals(jsonObject.getString("flag"))) {
            doPayMethod();
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
}
