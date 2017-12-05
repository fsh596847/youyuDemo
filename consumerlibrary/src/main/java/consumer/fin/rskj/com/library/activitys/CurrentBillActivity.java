package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.BSBillAdapter;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.BystatdgeBillItem;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.MyItemDecoration;
import consumer.fin.rskj.com.library.views.TopNavigationView2;
import me.leefeng.lfrecyclerview.LFRecyclerView;

/**
 * Created by HP on 2017/7/25.
 * 当前还款
 */

public class CurrentBillActivity extends BaseActivity {

  private static final String TAG = CurrentBillActivity.class.getSimpleName();

  private CheckBox checkAll;
  private LFRecyclerView recyclerView;
  private List<BystatdgeBillItem> itemList = new ArrayList<>();
  private BSBillAdapter pAdapter;
  private int count = 0;

  private TopNavigationView2 topNavigationView2;
  private TextView payment;
  /**
   * 含利息
   */
  private TextView total_lixi;
  /**
   * 罚息
   */
  private TextView total_faxi;
  /**
   * 总计
   */
  private TextView total_all;

  private String loanId;

  private TextView totalPayAll;
  private TextView loanStartData;
  private RelativeLayout bottom_layout;
  /**
   * 借款
   */
  private TextView loanAmt;
  /**
   * 利息
   */
  private TextView interest;
  /**
   * 借款期数
   */
  private TextView loanTerm;
  /**
   * 到款账户
   */
  private TextView bankCardMsg;

  private String totalAmount;
  private String bankMsg;

  private boolean currentStatus;
  /**
   * 全额还款
   */
  private BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  /**
   * 逾期总额
   */
  private BigDecimal yqtotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  /**
   * 逾期总利息
   */
  private BigDecimal yq_lixi = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  /**
   * 逾期总罚息
   */
  private BigDecimal yq_faxi = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loanId = getIntent().getStringExtra("loanId");
    currentStatus = getIntent().getBooleanExtra("currentStatus", false);
    setContentView(R.layout.activity_currentbill);
    LogUtils.d(TAG, "loanId = " + loanId);
    LogUtils.d(TAG, "currentStatus = " + currentStatus);
    getDetail();
    getBillDetail();
    //非逾期的时候使用
    if (!currentStatus) {
      //计算还款金额,只有全额还款才需要 执行此操作
      M100712();
    }
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
    totalPayAll = (TextView) findViewById(R.id.totalPayAll);
    loanStartData = (TextView) findViewById(R.id.loanStartData);

    loanAmt = (TextView) findViewById(R.id.loanAmt);
    interest = (TextView) findViewById(R.id.interest);
    loanTerm = (TextView) findViewById(R.id.loanTerm);
    bankCardMsg = (TextView) findViewById(R.id.bankCardMsg);

    bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
    checkAll = (CheckBox) findViewById(R.id.checkall);

    payment = (TextView) findViewById(R.id.repayment);

    total_all = (TextView) findViewById(R.id.total_all);
    total_lixi = (TextView) findViewById(R.id.total_lixi);
    total_faxi = (TextView) findViewById(R.id.total_faxi);

    if (currentStatus) {
      checkAll.setVisibility(View.INVISIBLE);
      total_faxi.setVisibility(View.VISIBLE);
      payment.setText("还款");
    } else {
      checkAll.setVisibility(View.INVISIBLE);
      total_faxi.setVisibility(View.GONE);
      payment.setText("全额还款");
    }

    payment.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(CurrentBillActivity.this, PaymentConfirmActivity.class);
        intent.putExtra("loanId", loanId);
        intent.putExtra("bankMsg", bankMsg);
        ArrayList<String> list = new ArrayList();
        if (currentStatus) {
          for (int a = 0; a < itemList.size(); a++) {
            BystatdgeBillItem item = itemList.get(a);
            if (item.getCheck()) {
              list.add(item.getRepayingPlanDetailId());
            }
          }
          intent.putExtra("totalAmount", yqtotal + "");
          intent.putExtra("currentStatus", 1);//逾期还款 1
        } else {
          intent.putExtra("totalAmount", total + "");
          intent.putExtra("currentStatus", 2);//全额还款 2
        }

        if (currentStatus && list.isEmpty()) {
          showToast("请选择需要还款的逾期账单", Constants.TOAST_SHOW_POSITION);
          return;
        }

        LogUtils.d(TAG, "yqtotal =》 " + yqtotal);
        intent.putStringArrayListExtra("repayingList", list);
        startActivity(intent);
      }
    });

    recyclerView = (LFRecyclerView) findViewById(R.id.pull_refresh_list);
    recyclerView.addItemDecoration(new MyItemDecoration());
    pAdapter = new BSBillAdapter(this, itemList, currentStatus);
    pAdapter.setOnItemClickListener(new BSBillAdapter.OnItemClickListener() {
      @Override public void OnItemClick(View view, BSBillAdapter.TViewHolder holder, int position) {
        //逾期还款只能选择逾期先还款，不是逾期的话 只能全额还款
        if (currentStatus) {
          BystatdgeBillItem cb = itemList.get(position);
          Log.d(TAG, "getStatus11 = " + cb.getStatus());
          //如果当前item 没有逾期，不能点击
          if (!"3".equals(cb.getStatus())) {
            holder.check.setChecked(false);
            holder.check.setEnabled(false);
            return;
          }
          //这样会导致checkbox 与item点击不一致
          holder.check.setChecked(!holder.check.isChecked());
          cb.setCheck(holder.check.isChecked());
          LogUtils.d(TAG, "isChecked =》 " + cb.getCheck());
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
          calclatulate();//计算总数
        }
      }
    });

    pAdapter.setCheckboxClickListener(new BSBillAdapter.OnItemClickListener() {
      @Override public void OnItemClick(View view, BSBillAdapter.TViewHolder holder, int position) {

        Log.d(TAG, "position = " + position);
        Log.d(TAG, "isChecked = " + holder.check.isChecked());

        //这样会导致checkbox 与item点击不一致
        //holder.check.setChecked(!holder.check.isChecked());
        if (currentStatus) {
          BystatdgeBillItem cb = itemList.get(position);
          Log.d(TAG, "getStatus22 = " + cb.getStatus());
          if (!"3".equals(cb.getStatus())) {
            holder.check.setChecked(false);
            holder.check.setEnabled(false);
            return;
          }

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
      }
    });

    pAdapter.setOnItemRemoveListner(new BSBillAdapter.OnItemRemoveListner() {
      @Override public void OnItemRemoveListner(View view, int position) {
        itemList.remove(position);
        pAdapter.notifyDataSetChanged();
      }
    });

    recyclerView.setFootText("没有更多");

    recyclerView.setLoadMore(false);
    recyclerView.setRefresh(false);
    recyclerView.setNoDateShow();
    recyclerView.setAutoLoadMore(false);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    recyclerView.setAdapter(pAdapter);

    checkAll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        for (BystatdgeBillItem checkBean : itemList) {
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

  private void setCheckedStatus(int position, BSBillAdapter.TViewHolder holder) {
    BystatdgeBillItem cb = itemList.get(position);
    LogUtils.d(TAG, "222isChecked =》 " + cb.getCheck());
    for (int i = 0; i < itemList.size(); i++) {
      //如果是选中了
      if (cb.getCheck()) {
        if (i < position) {
          if ("3".equals(itemList.get(i).getStatus())) {
            itemList.get(i).setCheck(true);
          }
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

  /**
   * 全额还款试算
   */
  private void M100712() {
    requestParams.clear();
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentData = sDateFormat.format(new java.util.Date());
    requestParams.put("transCode", Constants.TRANS_CODE_M100712);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("prepaymentType", "1");
    requestParams.put("perRepayDate", currentData);
    requestParams.put("loanId", loanId);
    showLoading("正在加载...");
    LogUtils.d(TAG, "全额还款试算: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "全额还款试算: data--->" + data);
        try {
          JSONObject object = new JSONObject(data);
          totalAmount = object.getString("cuTotalAmt");
          total = new BigDecimal(totalAmount).setScale(2);
          total_all.setText("总计" + totalAmount);
          total_lixi.setText("(含利息:" + object.getString("cuInt") + ")");
          LogUtils.d(TAG, "全额还款试算: totalAmount--->" + totalAmount);
          payment.setEnabled(true);
          payment.setBackgroundResource(R.mipmap.rskj_bbg);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "全额还款试算: errorMsg--->" + errorMsg);
        payment.setEnabled(false);
        payment.setBackgroundResource(R.mipmap.rskj_disable_button);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "全额还款试算: onFailure--->" + errorMsg);
        payment.setEnabled(false);
        payment.setBackgroundResource(R.mipmap.rskj_disable_button);
      }
    });
  }

  /**
   * M100705 贷款应还款信息查询
   */
  private void getBillDetail() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100705);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("loanId", loanId);
    showLoading(getResources().getString(R.string.dialog_loading));

    LogUtils.d(TAG, "贷款应还款信息查询: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "贷款应还款信息查询: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          String rowsCount = jsonObject.getString("rowsCount");
          JSONArray jsonArray = jsonObject.getJSONArray("rows");

          for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject object = jsonArray.getJSONObject(a);
            BystatdgeBillItem plItem = new BystatdgeBillItem();
            plItem.setRepayingPlanDetailId(object.getString("repayingPlanDetailId"));
            plItem.setCurrentPeriod(object.getString("currentPeriod"));
            plItem.setCurrentPrincipal(object.getString("currentPrincipal"));
            plItem.setStatus(object.getString("status"));
            plItem.setCurrentEndDate(object.getString("currentEndDate"));
            String currentInterest = object.getString("currentInterest");
            plItem.setCurrentInterest(currentInterest);
            String currentPrincipalInterest = object.getString("currentPrincipalInterest");
            plItem.setCurrentPrincipalInterest(currentPrincipalInterest);
            //0：未还，1：未还清，2：已还，3：已逾期，4：停止计息，5：已冲正
            if (currentStatus) {
              if ("3".equals(plItem.getStatus())) {
                yqtotal = yqtotal.add(new BigDecimal(currentPrincipalInterest));
                //yq_lixi = yq_lixi.add(new BigDecimal(currentInterest));
              }
            } else {
              if ("0".equals(plItem.getStatus())) {
                //通过 100712 获取还款总金额
                //total = total.add(new BigDecimal(currentPrincipalInterest));
                yq_lixi = yq_lixi.add(new BigDecimal(currentInterest));
              }
            }
            plItem.setOverInt(object.getString("overInt"));
            plItem.setRowsCount(rowsCount);
            plItem.setIsCurrentTerm(object.getString("isCurrentTerm"));
            //挑选出当期 然后获取值
            if (!currentStatus && "0".equals(object.getString("isCurrentTerm"))) {
              totalPayAll.setText(object.getString("currentPrincipalInterest"));
            }
            plItem.setOverdueredFlag(object.getString("overdueredFlag"));
            itemList.add(plItem);
          }
          LogUtils.d(TAG, "total = " + total);
          if (currentStatus) {
            totalPayAll.setText("" + yqtotal);
          }
          if (itemList.isEmpty()) {
            bottom_layout.setVisibility(View.GONE);
          } else {
            bottom_layout.setVisibility(View.VISIBLE);
          }
          pAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "贷款应还款信息查询: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "贷款应还款信息查询: errorMsg--->" + errorMsg);
      }
    });
  }

  /**
   * M100721 贷款简略信息 新增
   */
  public void getDetail() {
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100721);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("legalPerNum", Constants.LEGALPER_NUM);
    requestParams.put("fundId", sharePrefer.getXJFundId());
    requestParams.put("loanId", loanId);

    showLoading(getResources().getString(R.string.dialog_loading));
    LogUtils.d(TAG, "贷款简略信息: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        dismissLoading();
        LogUtils.d(TAG, "贷款简略信息: data--->" + data);
        updateDetail(data);
      }

      @Override public void onError(String retrunCode, String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "贷款简略信息: errorMsg--->" + errorMsg);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        LogUtils.d(TAG, "贷款简略信息: errorMsg--->" + errorMsg);
      }
    });
  }

  private void updateDetail(String data) {

    try {
      JSONObject jsonObject = new JSONObject(data);
      totalAmount = jsonObject.getString("loanAmt");
      bankMsg = jsonObject.getString("bankCardMsg");
      loanStartData.setText(jsonObject.getString("loanStartData"));
      loanAmt.setText("借款:" + totalAmount);
      interest.setText("利息:" + jsonObject.getString("interest"));
      loanTerm.setText("借款期数:" + jsonObject.getString("loanTerm"));
      bankCardMsg.setText("到款账户:" + bankMsg);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void calclatulate() {
    yqtotal = new BigDecimal(0);
    yq_lixi = new BigDecimal(0);
    yq_faxi = new BigDecimal(0);

    if (currentStatus) {
      for (int a = 0; a < itemList.size(); a++) {
        BystatdgeBillItem item = itemList.get(a);

        Log.d(TAG, "getCheck = " + item.getCheck());
        Log.d(TAG, "getStatus = " + item.getStatus());
        Log.d(TAG, "getCurrentPeriod = " + item.getCurrentPeriod());

        if (item.getCheck()) {
          Log.d(TAG, "yqtotal111 = " + yqtotal);
          yqtotal = yqtotal.add(new BigDecimal(item.getCurrentPrincipalInterest()));
          Log.d(TAG, "yqtotal222 = " + yqtotal);
          yq_lixi = yq_lixi.add(new BigDecimal(item.getCurrentInterest()));
          yq_faxi = yq_faxi.add(new BigDecimal(item.getOverInt()));
        }
      }

      Log.d(TAG, "yqtotal = " + yqtotal);
      Log.d(TAG, "yq_lixi = " + yq_lixi);
      Log.d(TAG, "yq_faxi = " + yq_faxi);
      total_all.setText("总计:" + yqtotal);
      total_lixi.setText("(含利息:" + yq_lixi);
      total_faxi.setText("罚息:" + yq_faxi + ")");
    }
  }
}
