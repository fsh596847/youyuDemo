package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.util.SysUtil;


/**
 *借款结果页面
 */
public class XJLoanMoneyResultActivity extends XJBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn,mRetrunBtn;
	private TextView mTitleView,mResultTv,mLoanNumTv,mLoanAccountTv;
    private ImageView mResultIv;
    private String resultCode="",loanMoney="",loanBank="",returnMsg="";
    private LinearLayout mSuccessLL;
    private View failureView;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_loan_money_result_layout);
		Bundle resultData=getIntent().getBundleExtra("loanMoneyResult");
		resultCode=resultData.getString("resultCode");//code
		if("000000".equals(resultCode))
		{
		  loanMoney=resultData.getString("applyAmt");//借款金额
		  loanBank=resultData.getString("bankInfo");//放款至银行
		}else
		{
		  returnMsg=resultData.getString("returnMsg");//错误提示
		}
		initViews();
	}

	private void initViews() {
		 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		 mTitleView.setText(R.string.loan_result_title_text);
		 mBackBtn.setOnClickListener(this);
		 mSuccessLL=(LinearLayout) findViewById(R.id.loan_money_sucess_ll);
		 mResultIv=(ImageView) findViewById(R.id.loan_money_result_iv);
		 mResultTv=(TextView) findViewById(R.id.loan_money_result_tv);
		 mLoanNumTv=(TextView) findViewById(R.id.loan_money_loan_number_tv);
		 mLoanAccountTv=(TextView) findViewById(R.id.loan_money_to_zhanghu_tv);
		 mRetrunBtn=(Button) findViewById(R.id.loan_money_result_back_shouye_btn);
		 failureView=findViewById(R.id.failure_view);
		 mRetrunBtn.setOnClickListener(this);
		 if("000000".equals(resultCode))
		{
			 mSuccessLL.setVisibility(View.VISIBLE);
			 failureView.setVisibility(View.GONE);
			 mLoanNumTv.setText(loanMoney+"(元)");
			 mLoanAccountTv.setText(loanBank);
			 mResultIv.setBackgroundResource(R.drawable.m_icon_loan_money_success);
			 mResultTv.setText("借款成功");
			 mRetrunBtn.setText(R.string.finish_text);
			
			
		}else
		{
			 failureView.setVisibility(View.VISIBLE);
			 mSuccessLL.setVisibility(View.GONE);
			 mResultIv.setBackgroundResource(R.drawable.m_icon_loan_money_failure);
			 mResultTv.setText(returnMsg);
			 mRetrunBtn.setText(R.string.goto_shouye);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
		case R.id.loan_money_result_back_shouye_btn:
			//返回首页
//			ActivityStackManagerUtils.getInstance().finishAllActivity();
			Intent intent = new Intent(XJLoanMoneyResultActivity.this,MenuListActivity2.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(XJLoanMoneyResultActivity.this,
                    MenuListActivity2.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);

	}
}
