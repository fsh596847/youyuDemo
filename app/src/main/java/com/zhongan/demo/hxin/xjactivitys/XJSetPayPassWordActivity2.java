package com.zhongan.demo.hxin.xjactivitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.wknight.keyboard.WKnightKeyboard;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.view.JYPEditText;
import com.zhongan.demo.util.ToastUtils;

public class XJSetPayPassWordActivity2 extends XJBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;//返回按钮
	private TextView mTitleView,mPwdTipTv;
	private JYPEditText mPwdEt;


	private String pwd1,pwd2;
	
	private boolean isChange ;//是否是修改支付密码
	private String cell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_pay_password_layout1);
		
		isChange = getIntent().getBooleanExtra("isChange", false);
		pwd1 = getIntent().getStringExtra("pwd1");
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
		mPwdTipTv=(TextView) findViewById(R.id.set_deal_password_tip_tv);
		
		if(isChange){
			mPwdTipTv.setText("请输入新的支付密码");
		}
		
		final WKnightKeyboard keyboard = new WKnightKeyboard(mPwdEt);
		if (mPwdEt != null) {
			keyboard.setRecvTouchEventActivity(XJSetPayPassWordActivity2.this);
		}
		mPwdEt.setOnEditTextContentListener(new JYPEditText.EditTextContentListener() {

			@Override
			public void onTextChanged(CharSequence text) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(CharSequence text) {
				// TODO Auto-generated method stub
				pwd2=keyboard.getEnctyptText(); 
				keyboard.clearInput();
				mPwdEt.setText("");
				Log.d("PAY", "---------onComplete-------------");
				if(isChange){
					changePayPwd(pwd1,pwd2);
				}else{
					passwordModify(pwd1,pwd2);
				}


			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJSetPayPassWordActivity2.this.finish();
			break;
		default:
			break;
		}
	}
	//忘记支付密码
	private void passwordModify(String password,String password2){

			mdialogProgress.show();
			
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("transCode", Contants.FORGET_CODE_PAY_PWD);
			params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
			params.addQueryStringParameter("clientToken", sharePrefer.getToken());
			params.addQueryStringParameter("passwordModifyType", "1");
			params.addQueryStringParameter("payPwd", password);
			params.addQueryStringParameter("rePayPwd", password2);
			httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					mdialogProgress.dismiss();
					//Toast.makeText(XJSetPayPassWordActivity2.this, "忘记支付密码成功!",Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("忘记支付密码成功!" ,XJSetPayPassWordActivity2.this);
					setResult(RESULT_OK);
					XJSetPayPassWordActivity2.this.finish();
				}
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFailure(HttpException exception, String msg) {
					// TODO Auto-generated method stub
					mdialogProgress.dismiss();
					setResult(RESULT_OK);
					XJSetPayPassWordActivity2.this.finish();
				}
				
				@Override
				public void onError(String returnCode, String msg) {
					// TODO Auto-generated method stub
					mdialogProgress.dismiss();
					setResult(RESULT_OK);
					XJSetPayPassWordActivity2.this.finish();
				}
			});

	}
	 //修改支付密码
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
		params.addQueryStringParameter("cell", "cell");
		httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				mdialogProgress.dismiss();
				//Toast.makeText(XJSetPayPassWordActivity2.this, "修改支付密码成功!", Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("修改支付密码成功!" ,XJSetPayPassWordActivity2.this);
				setResult(RESULT_OK);
				XJSetPayPassWordActivity2.this.finish();
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(HttpException exception, String msg) {
				// TODO Auto-generated method stub
				mdialogProgress.dismiss();
//				Toast.makeText(XJSetPayPassWordActivity2.this, "error = " + error,Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				XJSetPayPassWordActivity2.this.finish();
			}
			
			@Override
			public void onError(String returnCode, String returnMsg) {
				// TODO Auto-generated method stub
				mdialogProgress.dismiss();
//				Toast.makeText(XJSetPayPassWordActivity2.this, returnMsg,Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				XJSetPayPassWordActivity2.this.finish();
			}
		});
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//				Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//
//			@Override
//			public void onFailure(HttpException arg0, String error) {
//				// TODO Auto-generated method stub
//				LoggerUtil.debug("error-------------->" + error);
//				// String returnMsg = error;// 错误提示
//				mdialogProgress.dismiss();
//				Toast.makeText(XJSetPayPassWordActivity2.this, "error = " + error,Toast.LENGTH_SHORT).show();
//				setResult(RESULT_OK);
//				XJSetPayPassWordActivity2.this.finish();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				LoggerUtil.debug("获取验证码result---->"
//						+ responseInfo.result
//						+ "\nresponseInfo.statusCode ===="
//						+ responseInfo.statusCode);
//
//				mdialogProgress.dismiss();
//				
//				if (responseInfo.statusCode == 200) {
//					Type type = new TypeToken<Map<String, String>>() {
//					}.getType();
//					Gson gson = new Gson();
//					Map<String, String> resultMap = gson.fromJson(
//							responseInfo.result, type);
//					String returnCode = resultMap.get("returnCode");
//					if ("000000".equals(returnCode)) {
//						LoggerUtil.debug("设置支付密码成功!");
//						Toast.makeText(XJSetPayPassWordActivity2.this, "设置支付密码成功", Toast.LENGTH_SHORT).show();;
//					} else {
//						LoggerUtil.debug("设置支付密码失败!");
//						String returnMsg = resultMap.get("returnMsg");// 错误提示
//						Toast.makeText(XJSetPayPassWordActivity2.this, returnMsg,Toast.LENGTH_SHORT).show();
//
//					}
//					
//					setResult(RESULT_OK);
//					XJSetPayPassWordActivity2.this.finish();
//
//				}
//
//			}
//		});

	}


}
