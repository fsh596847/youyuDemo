package consumer.fin.rskj.com.library.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.CommonRecycleViewAdapter;
import consumer.fin.rskj.com.library.adapters.RecyclerViewHolder;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.PLItem;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.TopNavigationView2;
import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;

/**
 * Created by HP on 2017/8/18.
 * 借款列表
 */

public class LoanListActivity extends BaseActivity
    implements OnItemClickListener, LFRecyclerView.LFRecyclerViewListener,
    LFRecyclerView.LFRecyclerViewScrollChange {

  private static final String TAG = LoanListActivity.class.getSimpleName();
  private TopNavigationView2 topbar;
  private LFRecyclerView recyclerView;
  private List<PLItem> itemList = new ArrayList<>();
  private CommonRecycleViewAdapter pAdapter;

  private boolean status = false;//判断加载成功还是失败

  private boolean isRefresh = true;
  private int pageNum = 1;

  private String data;

  Handler handler = new Handler() {
    @Override public void dispatchMessage(Message msg) {
      switch (msg.what) {

        case 1: //刷新
          recyclerView.stopRefresh(status);
          pAdapter.notifyDataSetChanged();
          break;
        case 2: //加载
          recyclerView.stopLoadMore();
          pAdapter.notifyDataSetChanged();
          break;
      }
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    data = getIntent().getStringExtra("data");
    LogUtils.d(TAG, "data = " + data);

    setContentView(R.layout.rskj_activity_loanlist);

    requestData();
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

    recyclerView = (LFRecyclerView) findViewById(R.id.pull_refresh_list);
    recyclerView.setLoadMore(true);
    recyclerView.setRefresh(true);
    recyclerView.setNoDateShow();
    recyclerView.setAutoLoadMore(true);
    recyclerView.setOnItemClickListener(this);
    recyclerView.setLFRecyclerViewListener(this);
    recyclerView.setScrollChangeListener(this);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    pAdapter = new CommonRecycleViewAdapter(recyclerView, itemList, R.layout.rskj_plrecord_item) {

      @Override public void bindConvert(RecyclerViewHolder holder, int position, Object item,
          boolean isScrolling) {

        PLItem plItem = (PLItem) item;
        holder.setText(R.id.contractAmt, plItem.getContractAmt());
        holder.setText(R.id.currentPeriod_contractTerm,
            plItem.getCurrentPeriod() + "/" + plItem.getContractTerm());
        //0: 未还,1: 未还清,2: 已还,3: 已逾期,4: 停止计息,5: 已冲正.
        switch (plItem.getStatus()) {

          case "0":
            holder.setText(R.id.status, "未还");
            break;
          case "1":
            holder.setText(R.id.status, "未还清");
            break;
          case "2":
            holder.setText(R.id.status, "已还");
            break;
          case "3":
            holder.setText(R.id.status, "已逾期");
            break;
          case "4":
            holder.setText(R.id.status, "停止计息");
            break;
          case "5":
            holder.setText(R.id.status, "已冲正");
            break;

          case "12":
            holder.setText(R.id.status, "还款中");
            break;
          case "16":
            holder.setText(R.id.status, "放款中");
            break;

          default:
            holder.setText(R.id.contractAmt, "--");
            break;
        }

        holder.setText(R.id.startDate, plItem.getStartDate() + "借");
      }
    };

    recyclerView.setFootText("加载更多");

    recyclerView.setAdapter(pAdapter);
  }

  @Override public void onRefresh() {
    pageNum = 1;
    isRefresh = true;

    requestData();
  }

  @Override public void onLoadMore() {
    pageNum++;
    isRefresh = false;

    requestData();
  }

  @Override public void onRecyclerViewScrollChange(View view, int i, int i1) {

  }

  @Override public void onClick(int position) {

  }

  @Override public void onLongClick(int po) {

  }

  /**
   * M090903 app借款结果接口
   */
  private void requestData() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100602);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("fundId", sharePrefer.getXJFundId());
    requestParams.put("productId", sharePrefer.getXJProductId());
    requestParams.put("indexNo", "0");
    requestParams.put("pageSize", "10");
    try {
      JSONObject jsonObject = new JSONObject(data);
      requestParams.put("productId", jsonObject.getString("productId"));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "借款记录: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "借款记录: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          JSONArray jsonArray = jsonObject.getJSONArray("rows");

          for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject object = jsonArray.getJSONObject(a);
            PLItem plItem = new PLItem();
            plItem.setContractAmt(object.getString("contractAmt"));
            plItem.setContractTerm(object.getString("contractTerm"));
            plItem.setCurrentPeriod(object.getString("currentPeriod"));
            plItem.setCurrentPrincipal(object.getString("currentPrincipal"));
            plItem.setDayRate(object.getString("dayRate"));
            plItem.setProductName(object.getString("productName"));
            plItem.setStatus(object.getString("status"));
            plItem.setStartDate(object.getString("startDate"));
            itemList.add(plItem);
          }

          pAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "借款记录: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "借款记录: errorMsg--->" + errorMsg);
      }
    });
  }

  @Override public void onDestroy() {
    super.onDestroy();

    if (null != handler) {
      handler.removeCallbacksAndMessages(null);
    }
  }
}
