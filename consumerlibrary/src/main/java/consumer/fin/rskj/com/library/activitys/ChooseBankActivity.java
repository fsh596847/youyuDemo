package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.ChooseBankAdapter;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.BankCardItem;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.TopNavigationView2;
import me.leefeng.lfrecyclerview.LFRecyclerView;

/**
 * Created by HP on 2017/7/31.
 * 选择银行卡
 */

public class ChooseBankActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = ChooseBankActivity.class.getSimpleName();
  private TopNavigationView2 topbar;
  private LFRecyclerView mRecyclerView;
  private List<BankCardItem> mDatas = new ArrayList<>();
  private boolean status = false;//判断加载成功还是失败
  private ChooseBankAdapter pAdapter;
  private boolean isRefresh = true;
  private int pageNum = 1;

  private String selected;

  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    selected = getIntent().getStringExtra("selected");
    setContentView(R.layout.activity_choosebank);
    Log.d(TAG, "selected = " + selected);
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

    mRecyclerView = (LFRecyclerView) findViewById(R.id.pull_refresh_list);

    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    //        for (int a = 0 ;a < 5 ;a++){
    //            BankCardItem bankCardItem = new BankCardItem();
    //            bankCardItem.setBankLogo("");
    //            bankCardItem.setBankName("BBB" + a);
    //            bankCardItem.setBankNo("1111111"+a);
    //            bankCardItem.setBankTag("自动还款"+a);
    //            bankCardItem.setBankType("建设银行"+a);
    //
    //            mDatas.add(bankCardItem);
    //        }

    pAdapter = new ChooseBankAdapter(this, mDatas, selected);

    pAdapter.setOnItemClickListener(new ChooseBankAdapter.OnItemClickListener() {
      @Override
      public void OnItemClick(View view, ChooseBankAdapter.TViewHolder holder, int position) {
        Log.d(TAG, "position = " + position);
        //                pAdapter.disCheckAll();

        //这样会导致checkbox 与item点击不一致
        holder.check.setImageResource(R.mipmap.rskj_bank_checked);
        Intent intent = new Intent();
        intent.putExtra("item", mDatas.get(position));
        setResult(RESULT_OK, intent);
        finish();
      }
    });

    mRecyclerView.setLoadMore(false);
    mRecyclerView.setRefresh(false);
    mRecyclerView.setNoDateShow();
    mRecyclerView.setAutoLoadMore(false);
    //        recyclerView.setOnItemClickListener(this);
    //        recyclerView.setLFRecyclerViewListener(this);
    //        recyclerView.setScrollChangeListener(this);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    mRecyclerView.setAdapter(pAdapter);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.bank_card) {

      Intent intent = new Intent(ChooseBankActivity.this, BankCardListActivity.class);
      startActivity(intent);
    }
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
            pAdapter.notifyDataSetChanged();
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
