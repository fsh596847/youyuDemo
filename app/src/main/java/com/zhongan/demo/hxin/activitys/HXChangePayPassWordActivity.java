package com.zhongan.demo.hxin.activitys;

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
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.util.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

public class HXChangePayPassWordActivity extends HXBaseActivity implements OnClickListener {
	
	private EditText id_numl ,register_yzm_et;
	private TextView mTitleView;
	private Button mBackBtn;
	private Intent intent;
	private Button mYzmEt;
	
	private Timer timer;
	private int curNum = 120;//重新得到验证码剩余时间
	private final int NUM = 120;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_changepaypwd);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mBackBtn = (Button) findViewById(R.id.left_btn);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText("修改支付密码");
		mBackBtn.setOnClickListener(this);
		id_numl = (EditText) findViewById(R.id.id_num);
		findViewById(R.id.next_step).setOnClickListener(this);
		
		register_yzm_et = (EditText) findViewById(R.id.register_yzm_et);
		mYzmEt = (Button) findViewById(R.id.yzm_btn);
		mYzmEt.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			this.finish();
			break;
			
		case R.id.yzm_btn:
			// 验证码
			mYzmEt.setEnabled(false);
			getMsmCode();

			break;
			
		case R.id.next_step:
			if(TextUtils.isEmpty(id_numl.getText()) || !Util.isIdCardNum(id_numl.getText().toString())){
				//Toast.makeText(this, "请输入正确的身份证信息", Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("请输入正确的身份证信息" ,this);
                return;
			}
			
			
			/*if(TextUtils.isEmpty(register_yzm_et.getText())){
				Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}*/
			
			if(!getSharePrefer().getIdCardNum().equals(id_numl.getText().toString())){
				//Toast.makeText(this, "身份证号码有误", Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("身份证号码有误" ,this);
				return;
			}
			
			mHandler.sendEmptyMessage(0);
			
			intent = new Intent(this,HXSetPayPassWordActivity.class);// HXSetBankCardPwdActivity HXSetPayPassWordActivity
			intent.putExtra("isChange", true);
			//intent.putExtra("cell", register_yzm_et.getText().toString());
			startActivity(intent);
			this.finish();
			break;


		default:
			break;
		}
		
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
	
	
	//获取短信验证码接口
	   private void getMsmCode( ){
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("transCode", Contants.TRANS_CODE_YZM);
			params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
			params.addQueryStringParameter("isNo", "1");
			params.addQueryStringParameter("mobile", getSharePrefer().getPhone());// 手机号码
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
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//					Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//
//				@Override
//				public void onFailure(HttpException arg0, String error) {
//					// TODO Auto-generated method stub
//					LoggerUtil.debug("error-------------->" + error);
//					// String returnMsg = error;// 错误提示
//					mHandler.sendEmptyMessage(0);
//				}
//
//				@Override
//				public void onSuccess(ResponseInfo<String> responseInfo) {
//					LoggerUtil.debug("获取验证码result---->"
//							+ responseInfo.result
//							+ "\nresponseInfo.statusCode ===="
//							+ responseInfo.statusCode);
//
//					if (responseInfo.statusCode == 200) {
//						startTimer();
//						
//						Type type = new TypeToken<Map<String, String>>() {
//						}.getType();
//						Gson gson = new Gson();
//						Map<String, String> resultMap = gson.fromJson(
//								responseInfo.result, type);
//						String returnCode = resultMap.get("returnCode");
//						if ("000000".equals(returnCode)) {
//							LoggerUtil.debug("获取验证码成功!");
//						} else {
//							LoggerUtil.debug("获取验证码失败!");
//							String returnMsg = resultMap.get("returnMsg");// 错误提示
//							mHandler.sendEmptyMessage(0);
//						}
//					}
//
//				}
//			});
		}
	   
	   
	   

}
