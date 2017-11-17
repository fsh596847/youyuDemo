package com.zhongan.demo.hxin.activitys;

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
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.view.JYPEditText;

public class HXSetPayPassWordActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;//返回按钮
	private TextView mTitleView,mPwdTipTv;
	private JYPEditText mPwdEt;


	private String pwd1;
	private Intent intent;
	private boolean isChange = false;//是否是修改支付密码
//	private String cell;
	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			
			if(msg.what == 0){
				intent = new Intent(HXSetPayPassWordActivity.this,HXSetPayPassWordActivity2.class);
				intent.putExtra("isChange", isChange);
//				intent.putExtra("cell", cell);
				intent.putExtra("pwd1", pwd1);
				startActivityForResult(intent, 10);
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_pay_password_layout1);
		isChange = getIntent().getBooleanExtra("isChange", false);
//		if(isChange){
//			cell = getIntent().getStringExtra("cell");
//		}
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
		mTitleView.setText(R.string.set_deal_password_text);
		mBackBtn.setOnClickListener(this);
		mPwdEt=(JYPEditText) findViewById(R.id.deal_passwordnum_et);
		mPwdEt.setCursorVisible(false);
		mPwdTipTv=(TextView) findViewById(R.id.set_deal_password_tip_tv);
		
		if(isChange){
			mPwdTipTv.setText("请输入原支付密码");
		}
		
		final WKnightKeyboard keyboard = new WKnightKeyboard(mPwdEt);
		if (mPwdEt != null) {
			keyboard.setRecvTouchEventActivity(HXSetPayPassWordActivity.this);
		}
		mPwdEt.setOnEditTextContentListener(new JYPEditText.EditTextContentListener() {

			@Override
			public void onTextChanged(CharSequence text) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(CharSequence text) {
				// TODO Auto-generated method stub
				pwd1=keyboard.getEnctyptText(); 
				keyboard.clearInput();
				
				handler.sendEmptyMessageDelayed(0, 300);

				//				Intent intent = new Intent(HXSetPayPassWordActivity.this,HXSetBankCardPwdAgainActivity.class);
				//				intent.putExtra("password",password);
				//				startActivity(intent);
				//				HXSetPayPassWordActivity.this.finish();


			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			HXSetPayPassWordActivity.this.finish();
			break;
		default:
			break;
		}
	}


	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK){
			if(requestCode == 10){

				this.finish();
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		handler = null;
	}
	
/*	 //修改密码
	protected void changePayPwd(String password,String password2){

		mdialogProgress.show();
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("transCode", Contants.CHANGE_CODE_PWD);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addQueryStringParameter("clientToken", sharePrefer.getToken());
		params.addQueryStringParameter("passwordModifyType", "1");
		params.addQueryStringParameter("loginpwd", password);
		params.addQueryStringParameter("updateloginpwd", password2);
		params.addQueryStringParameter("telphone", sharePrefer.getPhone());
		params.addQueryStringParameter("cell", cell);
		
		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
				Contants.REQUEST_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String error) {
				// TODO Auto-generated method stub
				LoggerUtil.debug("error-------------->" + error);
				// String returnMsg = error;// 错误提示
				mdialogProgress.dismiss();
				Toast.makeText(HXSetPayPassWordActivity.this, "error = " + error,Toast.LENGTH_SHORT).show();
				HXSetPayPassWordActivity.this.finish();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoggerUtil.debug("获取验证码result---->"
						+ responseInfo.result
						+ "\nresponseInfo.statusCode ===="
						+ responseInfo.statusCode);

				mdialogProgress.dismiss();
				
				if (responseInfo.statusCode == 200) {
					Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Gson gson = new Gson();
					Map<String, String> resultMap = gson.fromJson(
							responseInfo.result, type);
					String returnCode = resultMap.get("returnCode");
					if ("000000".equals(returnCode)) {
						LoggerUtil.debug("设置支付密码成功!");
						Toast.makeText(HXSetPayPassWordActivity.this, "设置支付密码成功", Toast.LENGTH_SHORT).show();;
					} else {
						LoggerUtil.debug("设置支付密码失败!");
						String returnMsg = resultMap.get("returnMsg");// 错误提示
						Toast.makeText(HXSetPayPassWordActivity.this, returnMsg,Toast.LENGTH_SHORT).show();

					}
					
					HXSetPayPassWordActivity.this.finish();

				}

			}
		});

	}*/


}
