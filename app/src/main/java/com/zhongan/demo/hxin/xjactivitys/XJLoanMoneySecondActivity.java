package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.adapters.HXLoanMoneyPaytypeListAdapter;
import com.zhongan.demo.hxin.bean.HXPayTypeBean;
import com.zhongan.demo.hxin.bean.HXPayTypeItemBean;
import com.zhongan.demo.hxin.util.SysUtil;

import java.util.List;

/**
 * 还款方式
 */
public class XJLoanMoneySecondActivity extends XJBaseActivity implements OnClickListener {

    private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
    private Button mBackBtn;
    private TextView mTitleView, mLoanTypeTv, mTotalHuankuaneTv, mTotalDaikuaneTv, mTotalLixiTv, mTotalShouxufeiTv, mTotalQishuTv;

    private ListView mListView;

    private List<HXPayTypeItemBean> payTypeDataList;//还款计划列表
    private HXLoanMoneyPaytypeListAdapter adapter;
    private HXPayTypeBean paytypeData;
    private String repayModeName = "";//还款方式名称

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hxactivity_loan_money_second_layout);
        Bundle data = getIntent().getBundleExtra("payTypeInfo");
        paytypeData = (HXPayTypeBean) data.getSerializable("payTypeData");
        repayModeName = getIntent().getStringExtra("repayModeName");
        payTypeDataList = paytypeData.getRepayPlanList();
        initViews();
    }

    private void initViews() {
        mStatusView = findViewById(R.id.status_bar_view);
        int statusHeight = SysUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
        params.height = statusHeight;
        mStatusView.setLayoutParams(params);
        mBackBtn = (Button) findViewById(R.id.left_btn);
        mTitleView = (TextView) findViewById(R.id.center_title);
        mTitleView.setText(R.string.loan_money_pay_type_text);
        mBackBtn.setOnClickListener(this);
        mLoanTypeTv = (TextView) findViewById(R.id.loan_money_pay_type_tv);//还款方式
        mLoanTypeTv.setText(repayModeName);

        mTotalHuankuaneTv = (TextView) findViewById(R.id.loan_money_total_huankuaie_tv);//总还款额
        mTotalDaikuaneTv = (TextView) findViewById(R.id.loan_money_total_daikuaie_tv);//总贷款额
        mTotalLixiTv = (TextView) findViewById(R.id.loan_money_total_lixi_tv);//总利息
        mTotalShouxufeiTv = (TextView) findViewById(R.id.loan_money_total_shouxufei_tv);//总手续费
        mTotalQishuTv = (TextView) findViewById(R.id.loan_money_total_qishu_tv);//总期数
        mListView = (ListView) findViewById(R.id.loan_money_pay_type_listview);
        mTotalHuankuaneTv.setText(paytypeData.getTotalAmt());//总还款额
        mTotalDaikuaneTv.setText(paytypeData.getTotalLoanAmt());//总贷款额
        mTotalLixiTv.setText(paytypeData.getTotalIntAmt());//总利息
        mTotalShouxufeiTv.setText(paytypeData.getTotalFeeAmt());//总手续费
        mTotalQishuTv.setText(paytypeData.getTotalMonths());//总期数

        adapter = new HXLoanMoneyPaytypeListAdapter(this, payTypeDataList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_btn:
                //返回
                XJLoanMoneySecondActivity.this.finish();
                break;

            default:
                break;
        }
    }
}
