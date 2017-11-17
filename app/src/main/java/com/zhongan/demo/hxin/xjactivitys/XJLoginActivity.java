package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.security.RSAClient;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 登录页面
 */
public class XJLoginActivity extends XJBaseActivity implements OnClickListener {
	private EditText mMobileEt, mPasswordEt;
	private CheckBox mOpenPwd;
	private TextView mErrorTv;//错误提示
	private Button mRegisterBtn, mLoginBtn, mHasProblemBtn;
    private String mobile="";//手机号码
    private String password="";//密码
    private String deviceId="";
	private long curMillions = 0;
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contants.MSG_LOGIN_SUCCESS:
				mLoginBtn.setClickable(true);
				mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				mErrorTv.setText("");
				sharePrefer.setToken((String)msg.obj);
				sharePrefer.setPhone(mobile);
				sharePrefer.setLogin(true);
				
//				sharePrefer.edit().putString("clientToken",(String)msg.obj);
//				sharePrefer.edit().putString("loginMobile",mobile);
//				sharePrefer.edit().putBoolean("isLogin",true);
//				sharePrefer.edit().commit();
				Toast.makeText(XJLoginActivity.this,"登录成功!",Toast.LENGTH_SHORT).show();
				//登录
				Intent loginIntent=new Intent(XJLoginActivity.this,XJMainActivity.class);
	            startActivity(loginIntent);
				break;
			case Contants.MSG_LOGIN_FAILURE:
				Toast.makeText(XJLoginActivity.this,"登录失败!",Toast.LENGTH_SHORT).show();
				String loginRetMsg = (String) msg.obj;
				mErrorTv.setText(loginRetMsg);
				mLoginBtn.setClickable(true);
				mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				
				break;
			case Contants.MSG_M000003_SUCCESS:
				String mixPassword = (String) msg.obj;
				login(mobile, mixPassword, deviceId);
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
		setContentView(R.layout.hxactivity_login_layout);
		initViews();

		deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		LoggerUtil.debug("deviceId2------>"+deviceId);
//		if(deviceId==null||deviceId.equals(""))
//		{
//			TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//			String szImei = TelephonyMgr.getDeviceId();
//			sharePrefer.setDeviceId(szImei);
//			LoggerUtil.debug("deviceId3------>"+szImei);
//		}
		
	}

	private void initViews() {
		mMobileEt = (EditText) findViewById(R.id.login_phone_et);
		mPasswordEt = (EditText) findViewById(R.id.login_password_et);
		mOpenPwd = (CheckBox) findViewById(R.id.login_open_password);
		mErrorTv = (TextView) findViewById(R.id.login_error_tv);
		mRegisterBtn = (Button) findViewById(R.id.login_go_register_btn);
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mHasProblemBtn = (Button) findViewById(R.id.login_has_problem_btn);
		mOpenPwd.setOnClickListener(this);
		mRegisterBtn.setOnClickListener(this);
		mLoginBtn.setOnClickListener(this);
		mHasProblemBtn.setOnClickListener(this);
		mLoginBtn.setClickable(false);
		mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		mMobileEt.setText(sharePrefer.getPhone());
		mMobileEt.addTextChangedListener(textWatcher);
		mPasswordEt.addTextChangedListener(textWatcher);
		
	}
    private TextWatcher textWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			mobile=mMobileEt.getText().toString().trim();
			password=mPasswordEt.getText().toString().trim();
			if(mobile!=null&&!mobile.equals("")&&password!=null&&!password.equals(""))
			{
				mErrorTv.setText("");
				mLoginBtn.setClickable(true);
				mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				
			}else{
				mErrorTv.setText("");
				mLoginBtn.setClickable(false);
				mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable text) {
			
		}
	};
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.login_open_password:
			password=mPasswordEt.getText().toString().trim();
			if(password==null||password.equals(""))
			{
				mOpenPwd.setChecked(false);
				mErrorTv.setText(R.string.null_pwd_text);
				mPasswordEt.setAnimation(Util.shakeAnimation(10));
				mLoginBtn.setClickable(false);
				mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
				
			}
			if(password!=null&&!password.equals(""))
			{
				mErrorTv.setText("");
				//显示或隐藏密码
	            if(mOpenPwd.isChecked())
	            {
	            	//显示密码
	            	mPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
	            }else
	            {
	            	//隐藏密码
	            	mPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());  
	            }
	            mPasswordEt.setSelection(password.length());
			}
			
			break;
		case R.id.login_go_register_btn:
			//注册
            Intent registerIntent=new Intent(XJLoginActivity.this,XJRegisterActivity.class);
            startActivity(registerIntent);
//            XJLoginActivity.this.finish();
			break;
		case R.id.login_btn:
			mobile=mMobileEt.getText().toString().trim();
			password=mPasswordEt.getText().toString().trim();
			//判断手机号是否合法
			if(Util.isMobile(mobile))
			{
				//判断密码是否合法
				if(Util.isPwdNum(password))
				{
//					String bindingFirmware="db2f5f352d8b4000bb0a068dc425e8c7";
     				login(mobile,password,deviceId);
					//getMixPassWord(password);
//				
//				Intent loginIntent=new Intent(XJLoginActivity.this,XJAllPayBanksActivity.class);
//	            startActivity(loginIntent);
				
//				Intent loginIntent=new Intent(XJLoginActivity.this,XJFaceStartActivity.class);
//	            startActivity(loginIntent);
				}else
				{
					mErrorTv.setText(R.string.error_pwd_text);
					mPasswordEt.setAnimation(Util.shakeAnimation(10));
				}
			}else
			{
				mErrorTv.setText(R.string.error_mobile_text);
				mMobileEt.setAnimation(Util.shakeAnimation(10));
			}
			
			break;
		case R.id.login_has_problem_btn:
			//忘记密码
			Intent forgetPwdIntent=new Intent(XJLoginActivity.this,XJForgetPwdActivity.class);
			forgetPwdIntent.putExtra("title", "忘记密码");
			forgetPwdIntent.putExtra("isNo", 3);
            startActivity(forgetPwdIntent);
			
			break;

		default:
			break;
		}
	}
	//密码加密接口
	private void getMixPassWord(final String passwd)
	{
		 LoggerUtil.debug("密码加密: passwd--->"+passwd);
		 
		 RequestParams params = new RequestParams();
	     params.addQueryStringParameter("transCode",Contants.TRANS_CODE_MIX_PASSWORD);
		 params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
//		 HttpUtils dataHttp = new HttpUtils("60 * 1000");
		 MyApplication.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
		 new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0,
								String error) {
							// TODO Auto-generated method stub
							LoggerUtil.debug("error-------------->" + error);
							Message msg = new Message();
							msg.what = Contants.MSG_LOGIN_FAILURE;
							msg.obj="网络问题!";
							mHandler.handleMessage(msg);
						}

						@Override
						public void onSuccess(
								ResponseInfo<String> responseInfo) {
							LoggerUtil.debug( "密码加密：result---->"
									+ responseInfo.result
									+ "\nresponseInfo.statusCode ===="
									+ responseInfo.statusCode);
							if (responseInfo.statusCode == 200) {
								Type type = new TypeToken<Map<String, String>>() {
								}.getType();
								Gson gson = new Gson();
								Map<String, String> resultMap = gson.fromJson(responseInfo.result, type);
								String returnCode = resultMap.get("returnCode");
								if ("000000".equals(returnCode)) 
								{		
									try {
									   String	mixPassword= RSAClient.encoding(resultMap.get("pm"), resultMap.get("pe"),passwd);
									   Message msg = new Message();
									   msg.what = Contants.MSG_M000003_SUCCESS;
									   msg.obj=mixPassword;
									   mHandler.handleMessage(msg);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										LoggerUtil.exception(e);
										Message msg = new Message();
										msg.what = Contants.MSG_LOGIN_FAILURE;
										msg.obj="";
										mHandler.handleMessage(msg);
									}	
								} else 
								{
									String returnMsg = resultMap.get("returnMsg");// 错误提示
									Message msg = new Message();
									msg.what = Contants.MSG_LOGIN_FAILURE;
									msg.obj=returnMsg;
									mHandler.handleMessage(msg);
									
								}

							}

						}
					});
	}
	//登录接口
	private void login(String mobile,String passwd,String bindingFirmware)
	{
		    final Dialog mdialog =Util.createLoadingDialog(this,"登录中,请稍后...");
		    mLoginBtn.setClickable(false);
		    mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		    LoggerUtil.debug("登录提交数据:url---->"+ Contants.REQUEST_URL+"\nusername--->"+mobile+"\npasswd--->"+passwd+"\ntransCode-->"+Contants.TRANS_CODE_LOGIN+"\nbindingFirmware--->"+bindingFirmware+"\nlegalPerNumber---->00001"+"\nchannelNo---->"+Contants.CHANNEL_NO);
		    RequestParams params = new RequestParams("utf-8");
		    params.addHeader("Content-Type","application/x-www-form-urlencoded");
			params.addBodyParameter("transCode",Contants.TRANS_CODE_LOGIN);
			params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
			params.addBodyParameter("legalPerNumber",Contants.LEGAL_PERNUMBER);//法人编号
			params.addBodyParameter("username", mobile);//用户名称 15105987928
			params.addBodyParameter("passwd", passwd);//用户密码  456
			params.addBodyParameter("bindingFirmware",bindingFirmware);//手机绑定固件（手机渠道必输）db2f5f352d8b4000bb0a068dc425e8c7
			httpRequest( Contants.REQUEST_URL, params, new ResultCallBack() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					Type type = new TypeToken<Map<String, String>>() {
						}.getType();
						Gson gson = new Gson();
						Map<String, String> resultMap = gson.fromJson(data, type);
						String clientToken=resultMap.get("clientToken");//客户令牌
						Message msg = new Message();
						msg.what = Contants.MSG_LOGIN_SUCCESS;
						msg.obj=clientToken;
						mHandler.handleMessage(msg);
						mdialog.cancel();
				}
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					mdialog.show();
				}
				
				@Override
				public void onFailure(HttpException exception, String msg) {
					// TODO Auto-generated method stub
					mdialog.cancel();
					mLoginBtn.setClickable(true);
					mLoginBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				}
				
				@Override
				public void onError(String returnCode, String returnMsg) {
					// TODO Auto-generated method stub
					mdialog.cancel();
					Message msg = new Message();
					msg.what = Contants.MSG_LOGIN_FAILURE;
					msg.obj = returnMsg;
					mHandler.handleMessage(msg);
				}
			});
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//					new RequestCallBack<String>() {
//                        @Override
//                        public void onStart() {
//                        	// TODO Auto-generated method stub
//                        	super.onStart();
//                        	mdialog.show();
//                        }
//                        @Override
//                        public void onLoading(long total, long current,
//                        		boolean isUploading) {
//                        	// TODO Auto-generated method stub
//                        	super.onLoading(total, current, isUploading);
//                        }
//						@Override
//						public void onFailure(HttpException arg0,
//								String error) {
//							// TODO Auto-generated method stub
//							LoggerUtil.debug("登录error-------------->" + error);
//							Message msg = new Message();
//							msg.what = Contants.MSG_LOGIN_FAILURE;
//							msg.obj = "网络问题!";
//							mHandler.handleMessage(msg);
//							mdialog.cancel();
//						}
//
//						@Override
//						public void onSuccess(
//								ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug( "登录result---->"
//									+ responseInfo.result
//									+ "\nresponseInfo.statusCode ===="
//									+ responseInfo.statusCode);
//							if (responseInfo.statusCode == 200) {
//								Type type = new TypeToken<Map<String, String>>() {
//								}.getType();
//								Gson gson = new Gson();
//								Map<String, String> resultMap = gson.fromJson(responseInfo.result, type);
//								String returnCode = resultMap.get("returnCode");
//								if ("000000".equals(returnCode)) 
//								{		
//									// 取得sessionid.........................
////								    DefaultHttpClient dh = (DefaultHttpClient) dataHttp.getHttpClient();
////								    MyCookieStore.cookieStore = dh.getCookieStore();
////								    CookieStore cs = dh.getCookieStore();
////								    List<Cookie> cookies = cs.getCookies();
////									String aa = null;
////								    for (int i = 0; i < cookies.size(); i++)
////								    {
////								    	 if ("JSESSIONID".equals(cookies.get(i).getName())) 
////										 {
////										     aa = cookies.get(i).getValue();
////									         break;
////										 }   
////								    }
//									String clientToken=resultMap.get("clientToken");//客户令牌
//									Message msg = new Message();
//									msg.what = Contants.MSG_LOGIN_SUCCESS;
//									msg.obj=clientToken;
//									mHandler.handleMessage(msg);
//									mdialog.cancel();
//								} else {
//									mdialog.cancel();
//									String returnMsg = resultMap.get("returnMsg");// 错误提示
//									Message msg = new Message();
//									msg.what = Contants.MSG_LOGIN_FAILURE;
//									msg.obj = returnMsg;
//									mHandler.handleMessage(msg);
//									
//								}
//
//							}
//
//						}
//					});
	  }

	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

	            long millions = System.currentTimeMillis();
	            if(millions-curMillions > 2000){
	                curMillions = millions;
	                Toast.makeText(this, "再按一次退出程序",Toast.LENGTH_SHORT).show();
	            }else
	            {
//	            	 android.os.Process.killProcess(android.os.Process.myPid());   //获取PID 
//	            	 //System.exit(0);
	            	ActivityStackManagerUtils.getInstance().ExitApplication(getApplicationContext());
	            }
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	  
}
