package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CountDownTimer;

/**
 * 忘记密码页面
 */
public class HXForgetPwdActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;// 返回按钮
	private TextView mTitleView;// 标题
	private EditText mNewPwdEt, mSureNewPwdEt,mMobileEt,mYzmEt;
	private Button mYzmBtn, mSubmitBtn;
	private View mLine;
	private String mobile, pwd1 = "", pwd2 = "", yzm = "";
	private int isNo;
	private CountDownTimer timer = new CountDownTimer(120000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
//			mYzmBtn.setTextColor(getResources().getColor(R.color.color_666666));
			mYzmBtn.setClickable(false);// 防止重复点击
			mYzmBtn.setText(millisUntilFinished / 1000 + "s");
		}

		@Override
		public void onFinish() {
			mYzmBtn.setText(R.string.yzm_btn_text);
//			mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
			mYzmBtn.setClickable(true);
		}
	};
	/**
	 * 用Handler来更新UI
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case 0:
				if(null != timer){
					timer.cancel();
				}
				mYzmBtn.setText("获取验证码");
				mYzmBtn.setClickable(true);
				break;
				

			case Contants.MSG_GET_YZM_CODE_FAILURE:
				mYzmBtn.setClickable(true);
//				mYzmEt.setText("");
//				String errorMsg = (String) msg.obj;
//				CustomDialog.Builder builder = new CustomDialog.Builder(
//						HXForgetPwdActivity.this);
//				// builder.setIcon(R.drawable.ic_launcher);
//				// builder.setTitle(R.string.title_alert);
//				builder.setMessage(errorMsg);
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//								// 设置你的操作事项
//							}
//						});
//				builder.create().show();
				break;
			default:
				break;
			}

		}

	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_forget_pwd_layout);
		isNo=getIntent().getIntExtra("isNo", 0);
		LoggerUtil.debug("isNo-------------->"+isNo);
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
		String title=getIntent().getStringExtra("title");
		mTitleView.setText(title);
		mBackBtn.setOnClickListener(this);
		mNewPwdEt = (EditText) findViewById(R.id.forget_pwd_new_one_et);
		mSureNewPwdEt = (EditText) findViewById(R.id.forget_pwd_new_two_et);
		mYzmEt = (EditText) findViewById(R.id.forget_pwd_yzm_et);
		mMobileEt=(EditText) findViewById(R.id.forget_pwd_moblie_et);
		mYzmBtn = (Button) findViewById(R.id.yzm_btn);
		mYzmBtn.setOnClickListener(this);
		mSubmitBtn = (Button) findViewById(R.id.forget_pwd_submit_btn);
        mLine=findViewById(R.id.forget_pwd_line_one);
		mSubmitBtn.setClickable(false);
		mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		mSubmitBtn.setOnClickListener(this);
		mMobileEt.addTextChangedListener(textWatcher);
		mNewPwdEt.addTextChangedListener(textWatcher);
		mSureNewPwdEt.addTextChangedListener(textWatcher);
		mYzmEt.addTextChangedListener(textWatcher);
		
		findViewById(R.id.yzm_btn).setOnClickListener(this);
		if(isNo==3)
		{
		  mMobileEt.setVisibility(View.VISIBLE);
		  mLine.setVisibility(View.VISIBLE);
		}
		else
		{
		  mMobileEt.setVisibility(View.GONE);
		  mLine.setVisibility(View.GONE);
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable text) {
			if(isNo==3)
			{
				mobile=mMobileEt.getText().toString().trim();
				pwd1 = mNewPwdEt.getText().toString().trim();
				pwd2 = mSureNewPwdEt.getText().toString().trim();
				yzm = mYzmEt.getText().toString().trim();
				if (mobile!=null&&!mobile.equals("")&&mobile.length()==11&&pwd1 != null && !pwd1.equals("") && pwd1.length() >= 6
						&& pwd2 != null && !pwd2.equals("") && pwd2.length() >= 6
						&& yzm != null && !yzm.equals("")) {

					mSubmitBtn
							.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
					mSubmitBtn.setClickable(true);
				} else {

					mSubmitBtn.setClickable(false);
					mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
				}
			}else 
			{
				
				pwd1 = mNewPwdEt.getText().toString().trim();
				pwd2 = mSureNewPwdEt.getText().toString().trim();
				yzm = mYzmEt.getText().toString().trim();
				if (pwd1 != null && !pwd1.equals("") && pwd1.length() >= 6
						&& pwd2 != null && !pwd2.equals("") && pwd2.length() >= 6
						&& yzm != null && !yzm.equals("")) {

					mSubmitBtn
							.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
					mSubmitBtn.setClickable(true);
				} else {

					mSubmitBtn.setClickable(false);
					mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
				}
				
			}
		}
	};

	// 获取短信验证码接口
	private void getMsmCode(int isNo, String mobile) {
		mYzmBtn.setClickable(false);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("transCode", Contants.TRANS_CODE_YZM);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addQueryStringParameter("id", Integer.toString(isNo));// 注册接口返回的cacheKey
		params.addQueryStringParameter("isNo", Integer.toString(isNo));
		params.addQueryStringParameter("mobile", mobile);// 手机号码
		httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				timer.start();
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(HttpException exception, String msg) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(HXForgetPwdActivity.this,msg, Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(0);
			}
		});
		// dataHttp = new HttpUtils("5*60 * 1000");
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//				Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("error-------------->" + error);
//						// String returnMsg = error;// 错误提示
//						Message msg = new Message();
//						msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//						msg.obj = "";
//						Toast.makeText(HXForgetPwdActivity.this,"获取验证码失败", Toast.LENGTH_SHORT).show();
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("获取验证码result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//
//						if (responseInfo.statusCode == 200) {
//							
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(
//									responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								LoggerUtil.debug("获取验证码成功!");
//								timer.start();
//							} else {
//								LoggerUtil.debug("获取验证码失败!");	
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Toast.makeText(HXForgetPwdActivity.this,returnMsg, Toast.LENGTH_SHORT).show();
//								Message msg = new Message();
//								msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//							}
//
//						}
//
//					}
//				});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			HXForgetPwdActivity.this.finish();
			break;
		case R.id.forget_pwd_yzm_btn:
			
			
			break;
		case R.id.forget_pwd_submit_btn:
			if(isNo==3)
			{
			   mobile=mMobileEt.getText().toString().trim();
			}
			else
			{
				mobile=getSharePrefer().getPhone();
			}
			pwd1 = mNewPwdEt.getText().toString().trim();
			pwd2 = mSureNewPwdEt.getText().toString().trim();
			yzm = mYzmEt.getText().toString().trim();
			if (yzm == null || yzm.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//				msg.obj = "验证码不能为空,请输入!";
//				mHandler.handleMessage(msg);
				Toast.makeText(this, "验证码不能为空,请输入!", Toast.LENGTH_SHORT).show();
				return;
			}
			/*if(!pwd1.equals(pwd2))
			{
				Message msg = new Message();
				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
				msg.obj = "两次密码输入不一致";
				mHandler.handleMessage(msg);
				Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
				return;
			}*/
			// 判断验证码是否合法
			if (Util.checkVerCode(yzm)) {
				mSubmitBtn.setEnabled(false);
				chanegLoginPwd();

			} else {

				Toast.makeText(this, "请输入6位合法验证码!", Toast.LENGTH_SHORT).show();
//				Message msg = new Message();
//				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//				msg.obj = "请输入6位合法验证码!";
//				mHandler.handleMessage(msg);
				return;
			}
			break;
			
		case R.id.yzm_btn: //获取验证码
			if(isNo==3)
			{
			   mobile=mMobileEt.getText().toString().trim();
			}
			else
			{
				mobile=getSharePrefer().getPhone();
			}
			//判断手机号是否为空
			if(mobile==null|| mobile.equals(""))
			{	
				/*Message msg = new Message();
				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
				msg.obj = "手机号码不能为空,请输入!";
				mHandler.handleMessage(msg);*/
				
				Toast.makeText(this, "手机号码不能为空,请输入", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!pwd1.equals(pwd2)){
				Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//判断手机号是否合法
			if(Util.isMobile(mobile))	
			{
				mYzmBtn.setClickable(false);// 防止重复点击
				// 获取验证码
				
				getMsmCode(isNo,mobile);
				//timer.start();
			}else
			{
				Toast.makeText(this, "请输入11位合法手机号码", Toast.LENGTH_SHORT).show();
				return;
				/*Message msg = new Message();
				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
				msg.obj = "请输入11位合法手机号码!";
				mHandler.handleMessage(msg);*/
			}
			break;
			
		default:
			break;
		}
	}

	
	//修改登录密码
	private void chanegLoginPwd() {

		if(Util.passwordMatcher(pwd1)){
			Toast.makeText(HXForgetPwdActivity.this, "密码不符合规则", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(Util.passwordMatcher(pwd2)){
			Toast.makeText(HXForgetPwdActivity.this, "密码不符合规则", Toast.LENGTH_SHORT).show();
			return;
		}
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("transCode", Contants.CHANGE_CODE_LOGIN_PWD);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addQueryStringParameter("verifyCode", yzm);
		params.addQueryStringParameter("password", pwd1);
		params.addQueryStringParameter("repassword", pwd2);
		params.addQueryStringParameter("mobilePhone", mobile);// 手机号码
		params.addQueryStringParameter("isNo", String.valueOf(isNo));
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
				mSubmitBtn.setEnabled(true);
				Toast.makeText(HXForgetPwdActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(HXForgetPwdActivity.this,LoginActivity.class));
				HXForgetPwdActivity.this.finish();
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(HttpException exception, String msg) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
				mSubmitBtn.setEnabled(true);
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(HXForgetPwdActivity.this,msg, Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(0);
				mSubmitBtn.setEnabled(true);
			}
		});
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//				Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("error-------------->" + error);
//						// String returnMsg = error;// 错误提示
////						Message msg = new Message();
////						msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
////						msg.obj = "";
////						mHandler.handleMessage(msg);
//						mHandler.sendEmptyMessage(0);
//						mYzmBtn.setClickable(true);
//						mSubmitBtn.setEnabled(true);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("获取验证码result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//
//						if (responseInfo.statusCode == 200) {
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(
//									responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								LoggerUtil.debug("修改密码成功!");
//								Toast.makeText(HXForgetPwdActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
//								startActivity(new Intent(HXForgetPwdActivity.this,HXLoginActivity.class));
//								HXForgetPwdActivity.this.finish();
//								
//							} else {
//								LoggerUtil.debug("修改面码失败!");
//								Toast.makeText(HXForgetPwdActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
////								Message msg = new Message();
////								msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
////								msg.obj = returnMsg;
////								mHandler.handleMessage(msg);
//								mHandler.sendEmptyMessage(0);
//								mYzmBtn.setClickable(true);
//								mSubmitBtn.setEnabled(true);
//							}
//
//						}else{
//							mHandler.sendEmptyMessage(0);
//							mYzmBtn.setClickable(true);
//							mSubmitBtn.setEnabled(true);
//						}
//
//					}
//				});
	
		
		
	}

}
