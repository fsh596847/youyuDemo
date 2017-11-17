package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.util.SysUtil;


/**
 * 账户安全界面
 **/
public class HXAccountSafeActivity extends HXBaseActivity implements
        OnClickListener {
	private View mStatusView,mLine;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;
	private FrameLayout mZhifuPwdFl, mShoushiPwdFl, mZhiwenPwdFl, mForgetPwdFl;
	private TextView mTitleView, mZhifuPwdTv, mShoushiPwdTv, mZhiwenPwdTv;
	
	private Intent intent;
    private String userStateInfo="0";


	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_account_safe_layout);
		userStateInfo=getIntent().getStringExtra("userStateInfo");
		initViews();
	}

	private void initViews() {
		mStatusView = findViewById(R.id.status_bar_view);
		int statusHeight = SysUtil.getStatusBarHeight(this);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView
				.getLayoutParams();
		params.height = statusHeight;
		mStatusView.setLayoutParams(params);
		mBackBtn = (Button) findViewById(R.id.left_btn);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText(R.string.account_anquan_text);
		mBackBtn.setOnClickListener(this);
		mZhifuPwdFl = (FrameLayout) findViewById(R.id.account_safe_zhifu_pwd_fl);// 支付密码
		mShoushiPwdFl = (FrameLayout) findViewById(R.id.account_safe_shoushi_fl);// 手势密码
        mLine=findViewById(R.id.account_safe_line);
		mZhiwenPwdFl = (FrameLayout) findViewById(R.id.account_safe_zhiwen_fl);// 指纹密码
		mForgetPwdFl = (FrameLayout) findViewById(R.id.accound_safe_forget_password_fl);// 忘记密码
		mZhifuPwdTv = (TextView) findViewById(R.id.account_safe_zhifu_pwd_open_tv);// 支付密码开启提示
		mShoushiPwdTv = (TextView) findViewById(R.id.account_safe_shoushi_open_tip_tv);// 手势密码开启提示
		mZhiwenPwdTv = (TextView) findViewById(R.id.account_safe_zhiwen_open_tip_tv);// 指纹密码开启提示

		mZhifuPwdFl.setOnClickListener(this);
		mShoushiPwdFl.setOnClickListener(this);
		mZhiwenPwdFl.setOnClickListener(this);
		mForgetPwdFl.setOnClickListener(this);
		if("ced".equals(userStateInfo)||"loaned".equals(userStateInfo)||"face++".equals(userStateInfo))
		{
			mZhifuPwdFl.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);
		}else
		{
			mZhifuPwdFl.setVisibility(View.GONE);
			mLine.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			HXAccountSafeActivity.this.finish();
			break;
		case R.id.account_safe_zhifu_pwd_fl:
			intent = new Intent(HXAccountSafeActivity.this,HXPayPassWordActivity.class);
			startActivity(intent);
			// 支付密码
			break;
		case R.id.account_safe_shoushi_fl:
			// 手势密码
			break;
		case R.id.account_safe_zhiwen_fl:
			// 指纹密码
			break;
		case R.id.accound_safe_forget_password_fl:
			intent = new Intent(HXAccountSafeActivity.this,HXForgetPwdActivity.class);
			intent.putExtra("title", "重置密码");
			intent.putExtra("isNo", 2);
			startActivity(intent);
			//重置密码
			break;

		default:
			break;
		}
	}
}
