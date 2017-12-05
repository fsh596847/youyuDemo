package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.BankCardAdapter;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.BankCardItem;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.TopNavigationView2;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 2017/7/24.
 * 银行卡列表
 */

public class BankCardListActivity extends BaseActivity {

  private static final String TAG = BankCardListActivity.class.getSimpleName();

  private TopNavigationView2 topbar;
  private RecyclerView mRecyclerView;
  private List<BankCardItem> mDatas = new ArrayList<>();
  private BankCardAdapter mAdapter;

  private Intent intent;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rskj_activity_banklist);
    getBankList();
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
    mRecyclerView = (RecyclerView) findViewById(R.id.pull_refresh_list);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    mAdapter = new BankCardAdapter(this, mDatas);
    mAdapter.setOnItemClickListener(new BankCardAdapter.OnItemClickListener() {

      @Override
      public void OnItemClick(View view, RecyclerView.ViewHolder holder, int position) {

        showToast("普通点击", Constants.TOAST_SHOW_POSITION);
        intent = new Intent(BankCardListActivity.this, CardDetailActivity.class);
        startActivity(intent);
      }
    });

    mAdapter.setAddListener(new BankCardAdapter.OnItemClickListener() {

      @Override
      public void OnItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        showToast("添加点击", Constants.TOAST_SHOW_POSITION);
        intent = new Intent(BankCardListActivity.this, AddBankCardActivity.class);
        startActivity(intent);
      }
    });

    mRecyclerView.setAdapter(mAdapter);
  }

  private void getBankList() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_MM100132);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("fundId", sharePrefer.getXJFundId());
    requestParams.put("productId", sharePrefer.getXJProductId());
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "银行卡列表: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表: data--->" + data);
        /**
         * bankList	银行列表
         bankCode	银行卡号
         bankName	银行名称
         phone	手机号
         userName	用户名称
         isMain	是否为主还款银行卡
         acctId	银行卡主键
         isAutoRepay	是否允许自动扣款
         */
        try {
          JSONObject jsonObject = new JSONObject(data);

          JSONArray mJsonArr = jsonObject.getJSONArray("bankList");
          for (int a = 0; a < mJsonArr.length(); a++) {

            JSONObject object = mJsonArr.getJSONObject(a);
            BankCardItem bankCardItem = new BankCardItem();
            bankCardItem.setBankName(object.getString("bankName"));
            bankCardItem.setBankCode(object.getString("bankCode"));
            bankCardItem.setPhone(object.getString("phone"));
            bankCardItem.setAcctId(object.getString("acctId"));
            bankCardItem.setIsMain(object.getString("isMain"));
            bankCardItem.setUserName(object.getString("userName"));

            LogUtils.d(TAG, "银行卡列表: bankCardItem--->" + bankCardItem.toString());

            mDatas.add(bankCardItem);
            mAdapter.notifyDataSetChanged();
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表: errorMsg--->" + errorMsg);
      }

      @Override
      public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表: errorMsg--->" + errorMsg);
      }
    });
  }
}
