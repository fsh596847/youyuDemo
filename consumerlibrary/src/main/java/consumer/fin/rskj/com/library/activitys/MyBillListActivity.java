package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.BillAdapter;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.BillItem;
import consumer.fin.rskj.com.library.module.Rows;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.MyItemDecoration;
import consumer.fin.rskj.com.library.views.TopNavigationView2;
import me.leefeng.lfrecyclerview.LFRecyclerView;

/**
 * Created by HP on 2017/7/25.
 * 账单 列表
 */

public class MyBillListActivity extends BaseActivity {

  private static final String TAG = MyBillListActivity.class.getSimpleName();

  private TopNavigationView2 topbar;
  private CheckBox checkAll;
  private LFRecyclerView recyclerView;
  //    private View headerView; //头部view
  private List<BillItem> itemList = new ArrayList<>();
  private BillAdapter pAdapter;
  private int count = 0;

  private TextView payment;
  private String data;

  private TextView current;
  /**
   * 账单总金额
   */
  private TextView totalPayAll;
  /**
   * 最后还款日
   */
  private TextView endRepayingDate;
  /**
   * 含利息
   */
  private TextView total_lixi;//总利息
  /**
   * 罚息
   */
  private TextView total_faxi;//总罚息
  /**
   * 总计
   */
  private TextView total_all;//总计

  public String status;
  private String endRepayingDate_str;

  private Intent intent;
  private RelativeLayout bottom_layout;

  private StringBuilder bankCardMsg;//银行卡信息
  private ArrayList<Rows> rows = new ArrayList<>();//贷款Id

  /**
   * 总计
   */
  private BigDecimal total;//还款总额
  private BigDecimal yq_lixi = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);//逾期总利息
  private BigDecimal yq_faxi = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);//逾期总罚息

  private boolean isDelay;
  private String productId;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    data = getIntent().getStringExtra("data");

    try {
      JSONObject jsonObject = new JSONObject(data);
      isDelay = "2".equals(jsonObject.getString("states"));
      productId = jsonObject.getString("prodId");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    setContentView(R.layout.activity_billlist);
    LogUtils.d(TAG, "data = " + data);

    getBill();
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

    total_all = (TextView) findViewById(R.id.total_all);
    total_lixi = (TextView) findViewById(R.id.total_lixi);
    total_faxi = (TextView) findViewById(R.id.total_faxi);

    checkAll = (CheckBox) findViewById(R.id.checkall);
    checkAll.setVisibility(View.INVISIBLE);
    payment = (TextView) findViewById(R.id.repayment);
    payment.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        if (isDelay) {

          if (count <= 0) {
            showToast("请选择需要还款的账单", Constants.TOAST_SHOW_POSITION);
          } else {
            getBankInfo();
          }
        }
      }
    });

    bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
    recyclerView = (LFRecyclerView) findViewById(R.id.pull_refresh_list);
    current = (TextView) findViewById(R.id.current);
    totalPayAll = (TextView) findViewById(R.id.totalPayAll);
    endRepayingDate = (TextView) findViewById(R.id.endRepayingDate);
    recyclerView.addItemDecoration(new MyItemDecoration());
    pAdapter = new BillAdapter(this, itemList, isDelay);
    pAdapter.setOnItemClickListener(new BillAdapter.OnItemClickListener() {
      @Override public void OnItemClick(View view, BillAdapter.TViewHolder holder, int position) {
        Log.d(TAG, "position = " + position);
        Log.d(TAG, "isChecked = " + holder.check.isChecked());

        intent = new Intent(MyBillListActivity.this, CurrentBillActivity.class);
        intent.putExtra("loanId", itemList.get(position).getLoanId());
        if ("2".equals(status)) {//0 未借款 1 当期 2 逾期 3 本月已还
          intent.putExtra("currentStatus", true);
        } else {
          intent.putExtra("currentStatus", false);
        }
        startActivity(intent);
      }
    });

    pAdapter.setCheckboxClickListener(new BillAdapter.OnItemClickListener() {
      @Override public void OnItemClick(View view, BillAdapter.TViewHolder holder, int position) {

        //这样会导致checkbox 与item点击不一致
        //holder.check.setChecked(!holder.check.isChecked());
        BillItem cb = itemList.get(position);

        cb.setCheck(holder.check.isChecked());

        if (holder.check.isChecked() && (count < itemList.size())) {
          count++;
        } else if (!holder.check.isChecked() && (count > 0)) {
          count--;
        }

        if (count == itemList.size()) {
          checkAll.setChecked(true);
        } else {
          checkAll.setChecked(false);
        }

        setCheckedStatus(position, holder);
        calclatulate();
      }
    });

    pAdapter.setOnItemRemoveListner(new BillAdapter.OnItemRemoveListner() {
      @Override public void OnItemRemoveListner(View view, int position) {
        itemList.remove(position);
        pAdapter.notifyDataSetChanged();
      }
    });

    //        recyclerView.setHeaderView(headerView);
    recyclerView.setFootText("没有更多");

    recyclerView.setLoadMore(false);
    recyclerView.setRefresh(false);
    recyclerView.setNoDateShow();
    recyclerView.setAutoLoadMore(false);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    recyclerView.setAdapter(pAdapter);

    checkAll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        for (BillItem checkBean : itemList) {
          checkBean.setCheck(checkAll.isChecked());
        }

        if (checkAll.isChecked()) {
          count = itemList.size();
        } else {
          count = 0;
        }

        pAdapter.notifyDataSetChanged();
      }
    });
  }

  /**
   * 选中逻辑判断
   */

  private void setCheckedStatus(int position, BillAdapter.TViewHolder holder) {
    BillItem cb = itemList.get(position);
    LogUtils.d(TAG, "222isChecked =》 " + cb.getCheck());
    for (int i = 0; i < itemList.size(); i++) {
      //如果是选中了
      if (cb.getCheck()) {
        if (i < position) {
          holder.check.setChecked(true);
          holder.check.setEnabled(true);
          itemList.get(i).setCheck(true);
        } else if (i == position) {
          continue;
        }
      } else {
        //如果是取消了
        if (i > position) {
          holder.check.setChecked(false);
          itemList.get(i).setCheck(false);
        }
      }
    }

    pAdapter.notifyDataSetChanged();
  }

  private void calclatulate() {
    Log.d(TAG, "size = " + itemList.size());
    total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    yq_lixi = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);//逾期总利息
    yq_faxi = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);//逾期总罚息
    rows.clear();
    if (isDelay) {
      for (int a = 0; a < itemList.size(); a++) {
        BillItem item = itemList.get(a);

        Log.d(TAG, "getCheck = " + item.getCheck());
        Log.d(TAG, "getCurrentPeriod = " + item.getCurrentPeriod());

        if (item.getCheck()) {
          Log.d(TAG, "----- = ");
          total = total.add(new BigDecimal(item.getTotalPay()));
          yq_lixi = yq_lixi.add(new BigDecimal(item.getCurrentInterest()));
          yq_faxi = yq_faxi.add(new BigDecimal(item.getOverInt()));

          Rows row = new Rows();
          row.setLoanId(item.getLoanId());
          row.setRepayAmt(item.getTotalPay());
          rows.add(row);
        }
      }

      total_all.setText("总计:" + total);
      total_lixi.setText("(含利息:" + yq_lixi);
      total_faxi.setText("罚息:" + yq_faxi + ")");

      Log.d(TAG, "total = " + total);
    }
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

    showLoading("正在加载...");

    LogUtils.d(TAG, "银行卡列表查询: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
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

          Intent intent = new Intent(MyBillListActivity.this, PaymentConfirmActivity.class);
          intent.putExtra("bankMsg", bankCardMsg.toString());
          intent.putParcelableArrayListExtra("rows", rows);
          intent.putExtra("currentStatus", true);
          intent.putExtra("totalAmount", /*total + */"");
          intent.putExtra("currentStatus", 3);//账单还款 3

          startActivity(intent);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表查询: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "银行卡列表查询: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * M100611 本月账单接口
   */
  private void getBill() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100611);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("fundId", sharePrefer.getXJFundId());

    requestParams.put("productId", productId);

    showLoading("正在加载...");

    LogUtils.d(TAG, "本月账单: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "本月账单: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          JSONArray jsonArray = jsonObject.getJSONArray("rows");
          if (null == jsonArray || jsonArray.length() == 0) {
            showToast("账单列表为空", Constants.TOAST_SHOW_POSITION);
            return;
          }

          status = jsonObject.getString("status");//0 未借款 1 当期 2 逾期
          current.setText(jsonObject.getString("thisMonth") + "月未还金额");
          endRepayingDate_str = jsonObject.getString("endRepayingDate");
          totalPayAll.setText(jsonObject.getString("totalPayAll"));//账单总金额
          endRepayingDate.setText("最后还款日:" + endRepayingDate_str);//本期还款日

          for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject object = jsonArray.getJSONObject(a);
            BillItem plItem = new BillItem();
            try {
              plItem.setLoanId(object.getString("loanId"));//贷款Id
              plItem.setRepayingPlanId(object.getString("repayingPlanId"));//还款计划ID
              plItem.setPeriod(object.getString("period"));//期数
              plItem.setTotalPay(object.getString("totalPay"));//还款总金额

              plItem.setCurrentPrincipal(object.getString("currentPrincipal"));//应还本金
              plItem.setCurrentInterest(object.getString("currentInterest"));//应还利息
              plItem.setCurrentFee(object.getString("currentFee"));//应还费用
              plItem.setTotalPay(object.getString("totalPay"));
              plItem.setLoanId(object.getString("loanId"));
              plItem.setOverInt(object.getString("overInt"));
              plItem.setCurrentEndDate(object.getString("currentEndDate"));//计划还款日
              plItem.setCurrentPeriod(object.getString("currentPeriod"));//当期期数
              plItem.setOverInt(object.getString("overInt"));
            } catch (Exception e) {
              e.fillInStackTrace();
            }
            itemList.add(plItem);
          }

          pAdapter.notifyDataSetChanged();

          if (/*status.equals("2")*/isDelay) {
            bottom_layout.setVisibility(View.VISIBLE);
          } else {
            bottom_layout.setVisibility(View.GONE);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "本月账单: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "本月账单: errorMsg--->" + errorMsg);
      }
    });
  }
}
