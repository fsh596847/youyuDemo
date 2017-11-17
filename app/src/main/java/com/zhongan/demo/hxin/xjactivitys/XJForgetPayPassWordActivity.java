package com.zhongan.demo.hxin.xjactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.Util;

import java.util.Timer;
import java.util.TimerTask;

public class XJForgetPayPassWordActivity extends XJBaseActivity implements OnClickListener{

	private TextView mTitleView  ;
	private Button mYzmEt;
	private Button mBackBtn;
	private Intent intent;
	private EditText register_yzm_et , id_num_edit;

	private Timer timer;
	private int curNum = 120;//重新得到验证码剩余时间
	private final int NUM = 120;
	
	private String certNo,verifyCode;//身份证号	验证码

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if(what == 0){
				if(null != timer){
					timer.cancel();
				}
				mYzmEt.setText("获取验证码");
				mYzmEt.setEnabled(true);

			}else{
				mYzmEt.setText(curNum+" s");
				mYzmEt.setEnabled(false);
			}
		}
	};

//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//
//			case Contants.MSG_GET_YZM_CODE_FAILURE:
//				mYzmEt.setText("重新获取");
//				String errorMsg = (String) msg.obj;
//				CustomDialog.Builder builder = new CustomDialog.Builder(
//						XJForgetPayPassWordActivity.this);
//				// builder.setIcon(R.drawable.ic_launcher);
//				// builder.setTitle(R.string.title_alert);
//				builder.setMessage(errorMsg);
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,
//							int which) {
//						dialog.dismiss();
//						// 设置你的操作事项
//					}
//				});
//				builder.create().show();
//				break;
//			default:
//				break;
//			}
//
//		}
//
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_forgetpaypwd);

		initView();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		mHandler=null;
	}

	private void initView() {
		// TODO Auto-generated method stub
		id_num_edit = (EditText) findViewById(R.id.id_num_edit);
		register_yzm_et = (EditText) findViewById(R.id.register_yzm_et);
		mBackBtn = (Button) findViewById(R.id.left_btn);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText("忘记支付密码");
		mYzmEt = (Button) findViewById(R.id.yzm_btn);
		mBackBtn.setOnClickListener(this);
		findViewById(R.id.next_step).setOnClickListener(this);
		findViewById(R.id.yzm_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			if(null != timer){
				timer.cancel();
			}
			XJForgetPayPassWordActivity.this.finish();
			break;

		case R.id.yzm_btn:
			// 验证码
			mYzmEt.setEnabled(false);
			getMsmCode("13");

			break;

		case R.id.next_step:
			// 修改支付密码
			checkNum();
//			intent = new Intent(this,XJSetPayPassWordActivity.class);// XJSetBankCardPwdActivity XJSetPayPassWordActivity
//			intent.putExtra("need2", true);
//			startActivity(intent);
			break;


		default:
			break;
		}

	}


	private void checkNum() {
		
		
		if(TextUtils.isEmpty(id_num_edit.getText()) || !Util.isIdCardNum(id_num_edit.getText().toString())){
			Toast.makeText(this, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(register_yzm_et.getText())){
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!sharePrefer.getIdCardNum().equals(id_num_edit.getText().toString())){
			Toast.makeText(this, "身份证号码有误", Toast.LENGTH_SHORT).show();
			return;
		}
		
		certNo = id_num_edit.getText().toString();
		verifyCode = register_yzm_et.getText().toString();
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("transCode", Contants.IDCARD_CODE_PAY_PWD);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addQueryStringParameter("clientToken", sharePrefer.getToken());
		params.addQueryStringParameter("certNo", certNo);
		params.addQueryStringParameter("mobilePhone", sharePrefer.getPhone());// 手机号码
		params.addQueryStringParameter("verifyCode", verifyCode);
		params.addQueryStringParameter("isNo","13");
		httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
				Toast.makeText(XJForgetPayPassWordActivity.this, "身份证验证成功", Toast.LENGTH_SHORT).show();
				intent = new Intent(XJForgetPayPassWordActivity.this,XJSetPayPassWordActivity.class);// XJSetBankCardPwdActivity XJSetPayPassWordActivity
				startActivity(intent);
				XJForgetPayPassWordActivity.this.finish();
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
				mHandler.sendEmptyMessage(0);
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
//				mHandler.sendEmptyMessage(0);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				LoggerUtil.debug("获取验证码result---->"
//						+ responseInfo.result
//						+ "\nresponseInfo.statusCode ===="
//						+ responseInfo.statusCode);
//
//				if (responseInfo.statusCode == 200) {
//					mHandler.sendEmptyMessage(0);
//					Type type = new TypeToken<Map<String, String>>() {
//					}.getType();
//					Gson gson = new Gson();
//					Map<String, String> resultMap = gson.fromJson(
//							responseInfo.result, type);
//					String returnCode = resultMap.get("returnCode");
//					if ("000000".equals(returnCode)) {
//						Toast.makeText(XJForgetPayPassWordActivity.this, "身份证验证成功", Toast.LENGTH_SHORT).show();
//						intent = new Intent(XJForgetPayPassWordActivity.this,XJSetPayPassWordActivity.class);// XJSetBankCardPwdActivity XJSetPayPassWordActivity
//						startActivity(intent);
//						XJForgetPayPassWordActivity.this.finish();
//						
//					} else {
//						Toast.makeText(XJForgetPayPassWordActivity.this, "身份证验证失败", Toast.LENGTH_SHORT).show();
//						LoggerUtil.debug("获取验证码失败!");
//						String returnMsg = resultMap.get("returnMsg");// 错误提示
//					}
//
//				}
//
//			}
//		});
		
		
	}


	// 获取短信验证码接口
	private void getMsmCode(String isNo) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("transCode", Contants.TRANS_CODE_YZM);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addQueryStringParameter("isNo",isNo);
		params.addQueryStringParameter("isNo",isNo);
		params.addQueryStringParameter("mobile", sharePrefer.getPhone());// 手机号码
		httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				startTimer();
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
				mHandler.sendEmptyMessage(0);
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
////				Message msg = new Message();
////				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
////				msg.obj = "";
////				mHandler.handleMessage(msg);
//				mHandler.sendEmptyMessage(0);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				LoggerUtil.debug("获取验证码result---->"
//						+ responseInfo.result
//						+ "\nresponseInfo.statusCode ===="
//						+ responseInfo.statusCode);
//
//				if (responseInfo.statusCode == 200) {
//					startTimer();
//					
//					Type type = new TypeToken<Map<String, String>>() {
//					}.getType();
//					Gson gson = new Gson();
//					Map<String, String> resultMap = gson.fromJson(
//							responseInfo.result, type);
//					String returnCode = resultMap.get("returnCode");
//					if ("000000".equals(returnCode)) {
//						LoggerUtil.debug("获取验证码成功!");
//					} else {
//						LoggerUtil.debug("获取验证码失败!");
//						String returnMsg = resultMap.get("returnMsg");// 错误提示
////						Message msg = new Message();
////						msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
////						msg.obj = returnMsg;
////						mHandler.handleMessage(msg);
//						mHandler.sendEmptyMessage(0);
//					}
//
//				}
//
//			}
//		});
	}

	
	private void startTimer(){
		curNum = NUM;
		timer = null;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(--curNum);
			}
		}, 0,1000);
	}
	
}
