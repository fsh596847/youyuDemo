package com.zhongan.demo.hxin.activitys;

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
import com.zhongan.demo.hxin.util.SysUtil;


/**
 *还款结果页面
 */
public class HXPayMoneyResultActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;
	private TextView mTitleView,mResultTv;
    private ImageView mResultIv;
    private String resultFlag="true";
    private String retrunMsg;
    private String loanId;
    private String payType;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_pay_money_result_layout);
		resultFlag=getIntent().getStringExtra("flag");
		payType=getIntent().getStringExtra("payType");
		loanId=getIntent().getStringExtra("loanId");
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
		 mTitleView.setText(R.string.pay_result_title_text);
		 mBackBtn.setOnClickListener(this);
		 mResultIv=(ImageView) findViewById(R.id.pay_money_result_iv);
		 mResultTv=(TextView) findViewById(R.id.pay_money_result_tv);
		if(resultFlag.equals("false"))
		{
			retrunMsg=getIntent().getStringExtra("retrunMsg");
			mResultIv.setBackgroundResource(R.drawable.m_icon_pay_money_failure);
			mResultTv.setText(retrunMsg);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			if(payType.equals("all")&&resultFlag.equals("true"))
			{
//				ActivityStackManagerUtils.getInstance().finishAllActivity();
				Intent intent = new Intent(HXPayMoneyResultActivity.this,MenuListActivity2.class);
				startActivity(intent);
			}else
			{
			   HXPayMoneyResultActivity.this.finish();
			}
			break;
		
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (payType.equals("all") && resultFlag.equals("true")) {

			} else {
				HXPayMoneyResultActivity.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
