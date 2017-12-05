package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;

/**
 * Created by HP on 2017/7/29.
 * 个人中心
 */

public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = PersonCenterActivity.class.getSimpleName();
  private LinearLayout bank_card;
  private Intent intent;
  private TopNavigationView2 topbar;
  private TextView quotaMax;
  private TextView quotaRemainAmt;
  private TextView bankMsg;

  private String naviState;

  private String productId;
  private String fundId;
  /**
   * 银行卡信息
   */
  private StringBuilder bankCardMsg;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rskj_activity_personcenter);
    naviState = getIntent().getStringExtra("naviState");
    getQuota();
    getBankInfo();
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

    quotaMax = (TextView) findViewById(R.id.quotaMax);
    quotaRemainAmt = (TextView) findViewById(R.id.quotaRemainAmt);

    bank_card = (LinearLayout) findViewById(R.id.bank_card);
    bank_card.setOnClickListener(this);
    bankMsg = (TextView) findViewById(R.id.bankMsg);

    findViewById(R.id.setting).setOnClickListener(this);

    findViewById(R.id.help_center).setOnClickListener(this);
    findViewById(R.id.feedback).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.bank_card) {
      consumerStatus();
    }

    if (v.getId() == R.id.help_center) {
      intent = new Intent(this, HtmlActivity2.class);
      intent.putExtra("title", "帮助中心");
      intent.putExtra("url", BASE_URL + sharePrefer.getHelpCenter());
      startActivity(intent);
    }

    if (v.getId() == R.id.feedback) {
      intent = new Intent(this, HtmlActivity2.class);
      intent.putExtra("title", "用户反馈");
      intent.putExtra("url", BASE_URL + sharePrefer.getUserFeedbacks());
      startActivity(intent);
    }

    if (v.getId() == R.id.setting) {
      //            intent = new Intent(PersonCenterActivity.this,SettingActivity.class);
      //            startActivity(intent);
    }
  }

  /**
   * 获取产品额度
   */
  private void getQuota() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M110701);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("indexNo", "0");
    requestParams.put("pageSize", "1");

    try {
      JSONObject jsonObject = new JSONObject(naviState);
      productId = jsonObject.getString("productId");
      fundId = jsonObject.getString("fundId");
      requestParams.put("productId", productId);
    } catch (JSONException e) {
      showToast("productId 不能为空", Constants.TOAST_SHOW_POSITION);
      return;
    }
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "获取产品额度: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "获取产品额度: data--->" + data);
        try {
          JSONObject object = new JSONObject(data);
          JSONObject jsonObject = object.getJSONArray("rows").getJSONObject(0);
          quotaMax.setText("授信额度:" + jsonObject.getString("quotaMax"));
          quotaRemainAmt.setText("可用额度:" + jsonObject.getString("quotaRemainAmt"));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "获取产品额度: errorMsg--->" + errorMsg);
      }

      @Override
      public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "获取产品额度: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * 客户状态
   */
  private void consumerStatus() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M000812);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);

    requestParams.put("productId", productId);
    requestParams.put("fundId", fundId);
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "客户状态: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "客户状态: data--->" + data);
        try {
          JSONObject object = new JSONObject(data);
          if ("000000".equals(object.getString("returnCode"))) {
            String nextUserState = object.getString("nextUserState");
            if ("aced".equals(nextUserState)) {
              intent = new Intent(PersonCenterActivity.this, ChangeBankActivity.class);
              startActivity(intent);
            } else {
              goPage(nextUserState);
            }
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "客户状态: errorMsg--->" + errorMsg);
      }

      @Override
      public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "获取产品额度: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * 查询银行卡信息 调用接口  M100132
   */
  private void getBankInfo() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_MM100132);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);

    showLoading(getResources().getString(R.string.dialog_loading));

    LogUtils.d(TAG, "银行卡列表查询: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表查询: data--->" + data);
        bankCardMsg = new StringBuilder();
        try {
          JSONObject jsonObject = new JSONObject(data);

          JSONArray jsonArray = jsonObject.getJSONArray("bankList");
          if (null == jsonArray || jsonArray.length() == 0) {
            showToast("银行卡列表查询为空", Constants.TOAST_SHOW_POSITION);
            return;
          }

          for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject object = jsonArray.getJSONObject(a);

            if ("1".equals(object.getString("isMain"))) {
              bankCardMsg.append(object.getString("bankName")).
                  append(object.getString("acctType")).
                  append(object.getString("bankCode"));
            }
          }

          if (null == bankCardMsg || TextUtils.isEmpty(bankCardMsg)) {
            showToast("银行卡信息为空", Constants.TOAST_SHOW_POSITION);
            return;
          }

          bankMsg.setText(bankCardMsg);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表查询: errorMsg--->" + errorMsg);
      }

      @Override
      public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表查询: errorMsg--->" + errorMsg);
      }
    });
  }
}
