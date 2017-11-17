package com.zhongan.demo.hxin.xjactivitys;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.bean.HXLoanMoneyListItemBean;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.SysUtil;


/**
 * 借款详情界面
 */
public class XJLoanMoneyDetailActivity extends XJBaseActivity implements OnClickListener
{
	
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;
	private TextView mTitleView,mLoanYinhangTv,mPayYinhangTv,mDayLilvTv,
	mStartEndDateTv,mLoanNumTv,mNotPayLilvTv,mPayStateTv,mNotPayNumTv,mNotPayLixiTv,mNotPayShouxufeiTv,mCurrentPaydateTv,mLookHetongBtn;
    private HXLoanMoneyListItemBean loanItem;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_loan_money_detail_layout);
		Bundle bundle=getIntent().getBundleExtra("loanData");
		loanItem=(HXLoanMoneyListItemBean) bundle.getSerializable("loanMeneyListItem");
		initViews();
	}
    private void initViews()
    {
    	 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		 mTitleView.setText(R.string.loan_money_detail_text);
		 mBackBtn.setOnClickListener(this);
		 mLoanNumTv=(TextView) findViewById(R.id.pay_money_loan_num_tv);//贷款金额
		 mLoanNumTv.setText(loanItem.getContractAmt()+"(元)");
		 mStartEndDateTv=(TextView) findViewById(R.id.pay_money_start_end_date_tv);//起止时间
		 mStartEndDateTv.setText(loanItem.getStartDate()+"~"+loanItem.getExpirationDate());
		 mDayLilvTv=(TextView) findViewById(R.id.pay_money_lilv_day_tv);//日利率
		 mDayLilvTv.setText(loanItem.getDayRate()+"%");
		 mPayYinhangTv=(TextView) findViewById(R.id.pay_money_pay_yanhang_tv);//还款银行
		 mPayYinhangTv.setText(loanItem.getPayBankName()+" "+loanItem.getPayBankType()+" ("+loanItem.getPayBankAcct()+")");
		 mLoanYinhangTv=(TextView) findViewById(R.id.pay_money_loan_yinhang_tv);//放款银行
		 mLoanYinhangTv.setText(loanItem.getLoanBank());
		 mNotPayLilvTv=(TextView) findViewById(R.id.pay_money_yuqililv_tv);//逾期利率
		 mNotPayLilvTv.setText(loanItem.getContractOverdueRate()+"%");
		 mPayStateTv=(TextView) findViewById(R.id.pay_money_loan_state_tv);//借款状态
		 mLookHetongBtn=(TextView) findViewById(R.id.pay_money_look_hetong_tv);
		 mLookHetongBtn.setOnClickListener(this);
		 String str="";
		 if(loanItem.getLoanStatus().equals("3"))
		 {
			str="已还清";
		 }
		 else if(loanItem.getLoanStatus().equals("7"))
		 {
			str="已逾期";
		 }else
		 {
			str="未还清";
		 }
		 str=str+ " ("+loanItem.getCurrentPeriod()+"/"+loanItem.getContractTerm()+")";
		 mPayStateTv.setText(str);
		 mNotPayNumTv=(TextView) findViewById(R.id.pay_money_not_pay_num_tv);//未还余额
		 mNotPayNumTv.setText(loanItem.getNotPayPrincipal()+"(元)");
		 mNotPayLixiTv=(TextView) findViewById(R.id.pay_money_not_pay_lixi_tv);//未还利息
		 mNotPayLixiTv.setText(loanItem.getNotPayInterest()+"(元)");
		 mNotPayShouxufeiTv=(TextView) findViewById(R.id.pay_money_not_pay_shouxufei_tv);//未还手续费
		 mNotPayShouxufeiTv.setText(loanItem.getNotPayFee()+"(元)");
		 mCurrentPaydateTv=(TextView) findViewById(R.id.pay_money_current_pay_date_tv);//当前还款时间
		 mCurrentPaydateTv.setText(loanItem.getCurrentEndDate());
		 
		
    }
   
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJLoanMoneyDetailActivity.this.finish();
			break;
		case R.id.pay_money_look_hetong_tv:
			String contractURL=loanItem.getContractUrl();
			Intent useLinesIntent=new Intent(XJLoanMoneyDetailActivity.this,XJUserLineActivity.class);
			useLinesIntent.putExtra("title", "查看合同");
			useLinesIntent.putExtra("url", Contants.BASE_URL+contractURL);
            startActivity(useLinesIntent);
			break;
		default:
			break;
		}
	}
	
}
