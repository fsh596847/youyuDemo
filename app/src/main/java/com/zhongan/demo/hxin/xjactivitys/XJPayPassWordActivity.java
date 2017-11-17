package com.zhongan.demo.hxin.xjactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;


//支付密码/登录密码设置
public class XJPayPassWordActivity extends XJBaseActivity implements OnClickListener{
	
	private TextView mTitleView;
	private Button mBackBtn;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_paypassword_layout);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mBackBtn = (Button) findViewById(R.id.left_btn);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText("支付密码");
		mBackBtn.setOnClickListener(this);
		
		findViewById(R.id.change_pay_password).setOnClickListener(this);
		findViewById(R.id.forget_pay_password).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			this.finish();
			break;
		case R.id.change_pay_password:
			// 修改支付密码
			intent = new Intent(this,XJChangePayPassWordActivity.class);
			startActivity(intent);
			break;

		case R.id.forget_pay_password:
			// 忘记支付密码
			intent = new Intent(this,XJForgetPayPassWordActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

}
