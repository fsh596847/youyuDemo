package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wknight.keyboard.WKnightKeyboard;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.view.JYPEditText;

/**
 * 设置交易密码页面
 */
public class XJSetBankCardPwdActivity extends XJBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;//返回按钮
	private TextView mTitleView,mPwdTipTv;
    private JYPEditText mPwdEt;
    private String  password="";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_bank_card_deal_password_layout);
		initViews();
	}
	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			
			if(msg.what == 0){
				Intent intent = new Intent(XJSetBankCardPwdActivity.this,XJSetBankCardPwdAgainActivity.class);
				intent.putExtra("password",password);
				startActivity(intent);
			}
			
		};
	};
	private void initViews() {
		 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		 mTitleView.setText(R.string.set_deal_password_text);
		 mBackBtn.setOnClickListener(this);
		 mPwdEt=(JYPEditText) findViewById(R.id.deal_passwordnum_et);
//		 mPwdEt.setFirstComplete(false);
		 mPwdTipTv=(TextView) findViewById(R.id.set_deal_password_tip_tv);
		 final WKnightKeyboard keyboard = new WKnightKeyboard(mPwdEt);
		 if (mPwdEt != null) {
	            keyboard.setRecvTouchEventActivity(XJSetBankCardPwdActivity.this);
	     }
		 mPwdEt.setOnEditTextContentListener(new JYPEditText.EditTextContentListener() {
			
			@Override
			public void onTextChanged(CharSequence text) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(CharSequence text) {
				// TODO Auto-generated method stub
				password=keyboard.getEnctyptText(); 
				handler.sendEmptyMessageDelayed(0, 300);
				keyboard.clearInput();
//				mPwdEt.setFirstComplete(false);
//				XJSetBankCardPwdActivity.this.finish();
//				
			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJSetBankCardPwdActivity.this.finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		handler = null;
	}
}
