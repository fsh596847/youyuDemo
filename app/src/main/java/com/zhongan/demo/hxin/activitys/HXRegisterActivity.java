package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CountDownTimer;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 注册页面
 */
public class HXRegisterActivity extends HXBaseActivity implements OnClickListener {
    private EditText mMobileEt, mPasswordEt, mYzmEt;
    private TextView mErrorTv, mUseLinesTv;
    private Button mRegisterBtn, mLoginBtn, mHasProblemBtn, mYzmBtn;
    private String mobile, password, yzm;
    private String cacheKey = "";
    private Dialog mdialog;
    private HttpUtils dataHttp;
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
            mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
            mYzmBtn.setClickable(true);
        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Contants.MSG_GET_YZM_CODE_SUCCESS:
//				String authCode = (String) msg.obj;
//				mYzmEt.setText(authCode);
                    break;
                case Contants.MSG_GET_YZM_CODE_FAILURE:
                    String retMsg = (String) msg.obj;
                    mErrorTv.setText(retMsg);
                    // 设置你的操作事项
//				mYzmEt.setText("");
//				timer.cancel();
//				mYzmBtn.setText(R.string.yzm_btn_text);
//				mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
//				mYzmBtn.setClickable(true);
                    //mYzmEt.setAnimation(Util.shakeAnimation(10));
                    break;
                case Contants.MSG_REGISTER_SUCCESS:
                    mdialog.cancel();
//                    Toast.makeText(HXRegisterActivity.this, "注册成功!", 1).show();
                    ToastUtils.showCenterToast("注册成功" ,HXRegisterActivity.this);
                    getSharePrefer().setPhone(mobile);
                    Intent registerIntent = new Intent(HXRegisterActivity.this, LoginActivity.class);
                    startActivity(registerIntent);
                    HXRegisterActivity.this.finish();
                    break;
                case Contants.MSG_REGISTER_FAILURE:
                    mdialog.cancel();
                    String errorMsg = (String) msg.obj;
                    mErrorTv.setText(errorMsg);
//                    Toast.makeText(HXRegisterActivity.this, "注册失败!", 1).show();
                    ToastUtils.showCenterToast("注册失败" ,HXRegisterActivity.this);
//				mYzmEt.setText("");
//				timer.cancel();
//				mYzmBtn.setText(R.string.yzm_btn_text);
//				mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
//				mYzmBtn.setClickable(true);
                    break;
                case Contants.MSG_M100101_SUCCESS:
                    cacheKey = (String) msg.obj;
                    checkSmscode(cacheKey, yzm);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hxactivity_register_layout);

        initViews();
    }

    private void initViews() {
        mMobileEt = (EditText) findViewById(R.id.register_phone_et);//手机号
        mPasswordEt = (EditText) findViewById(R.id.register_password_et);//密码
        mYzmEt = (EditText) findViewById(R.id.register_yzm_et);//验证码
        mErrorTv = (TextView) findViewById(R.id.register_error_tv);//错误提示
        mRegisterBtn = (Button) findViewById(R.id.register_btn);//注册按钮
        mLoginBtn = (Button) findViewById(R.id.register_go_login_btn);//登录按钮
        mHasProblemBtn = (Button) findViewById(R.id.register_has_problem_btn);//忘记密码
        mYzmBtn = (Button) findViewById(R.id.register_yzm_btn);//获取验证码
        mUseLinesTv = (TextView) findViewById(R.id.register_tip_two);

        mRegisterBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
        mRegisterBtn.setOnClickListener(this);
        mRegisterBtn.setClickable(false);
        mLoginBtn.setOnClickListener(this);
        mHasProblemBtn.setOnClickListener(this);
        mYzmBtn.setOnClickListener(this);
        mUseLinesTv.setOnClickListener(this);
        mMobileEt.addTextChangedListener(textWatcher);
        mYzmEt.addTextChangedListener(textWatcher);
        mPasswordEt.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1,
                                      int arg2, int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable text) {
            mobile = mMobileEt.getText().toString().trim();
            password = mPasswordEt.getText().toString().trim();
            yzm = mYzmEt.getText().toString().trim();
            if (mobile != null && !mobile.equals("") && password != null && !password.equals("") && password.length() >= 6 && yzm != null && !yzm.equals("")) {
                mErrorTv.setText("");
                mRegisterBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                mRegisterBtn.setClickable(true);
            } else {
                mErrorTv.setText("");
                mRegisterBtn.setClickable(false);
                mRegisterBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
            }
        }
    };

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.register_yzm_btn:
                mobile = mMobileEt.getText().toString().trim();
                password = mPasswordEt.getText().toString().trim();
                //判断手机号是否为空
                if (mobile == null || mobile.equals("")) {
                    mErrorTv.setText(R.string.null_mobile_text);
                    mMobileEt.setAnimation(Util.shakeAnimation(10));
                    mRegisterBtn.setClickable(false);
                    mRegisterBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
                    return;
                }
                //判断手机号是否合法
                if (Util.isMobile(mobile)) {

                    mErrorTv.setText("");
//				register(mobile,password);
                    getMsmCode("1", mobile);
                    //获取验证码

                } else {
                    mErrorTv.setText(R.string.error_mobile_text);
                    mMobileEt.setAnimation(Util.shakeAnimation(10));
                }
                break;
            case R.id.register_btn:
                mobile = mMobileEt.getText().toString().trim();
                password = mPasswordEt.getText().toString().trim();
                yzm = mYzmEt.getText().toString().trim();
                LoggerUtil.debug("111111mobile-------------->" + mobile + "\npassword------------>" + password + "\nyzm-------->" + yzm);
                //判断手机号是否合法
                if (Util.isMobile(mobile)) {
                    //判断验证码是否合法
                    if (Util.checkVerCode(yzm)) {
                        //判断密码是否合法
                        if (Util.isPwdNum(password)) {
                            mErrorTv.setText("");
                            register(mobile, password, yzm);
                        } else {
                            mErrorTv.setText(R.string.error_pwd_text);
                            mPasswordEt.setAnimation(Util.shakeAnimation(10));
                        }
                    } else {
                        mErrorTv.setText(R.string.error_yzm_text);
                        mYzmEt.setAnimation(Util.shakeAnimation(10));
                    }

                } else {
                    mErrorTv.setText(R.string.error_mobile_text);
                    mMobileEt.setAnimation(Util.shakeAnimation(10));
                }

                break;
            case R.id.register_go_login_btn:
                mErrorTv.setText("");
                //登录
//			Intent loginIntent=new Intent(HXRegisterActivity.this,HXLoginActivity.class);
//            startActivity(loginIntent);
                HXRegisterActivity.this.finish();
                break;
            case R.id.register_has_problem_btn:
                //遇到问题
                mErrorTv.setText("");
                break;
            case R.id.register_tip_two:
                mErrorTv.setText("");
                //用户使用协议
//			String registerURL=Contants.BASE_URL+"/pages/agreement/registAgreement.html";
                String registerURL = Contants.BASE_URL + "/pages/phonehtml5/userRegistrationProtocol.html";
                Intent useLinesIntent = new Intent(HXRegisterActivity.this, HXUserLineActivity.class);
                useLinesIntent.putExtra("title", "用户协议");
                useLinesIntent.putExtra("url", registerURL);
                startActivity(useLinesIntent);
                break;

            default:
                break;
        }
    }

    //获取短信验证码接口
    private void getMsmCode(String isNo, String mobile) {
        mYzmBtn.setClickable(false);
        RequestParams params = new RequestParams("utf-8");
        params.addQueryStringParameter("transCode", Contants.TRANS_CODE_YZM);
        params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
        params.addQueryStringParameter("id", isNo);//注册接口返回的cacheKey
        params.addQueryStringParameter("isNo", isNo);
        params.addQueryStringParameter("mobile", mobile);//手机号码
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
                mYzmBtn.setClickable(true);
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                mYzmBtn.setClickable(true);
            }
        });
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0,
//							String error) {
//						mYzmBtn.setClickable(true);
//						LoggerUtil.debug("error-------------->" + error);
//						//String returnMsg = error;// 错误提示
//						Message msg = new Message();
//						msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//						msg.obj ="网络问题";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(
//							ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("获取验证码result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						
//						if (responseInfo.statusCode == 200) {
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson
//									.fromJson(responseInfo.result, type);
//							String returnCode = resultMap
//									.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								timer.start();
//								LoggerUtil.debug("获取验证码成功!");
//							} else {
//								mYzmBtn.setClickable(true);
//								LoggerUtil.debug("获取验证码失败!");
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
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

    //验证验证码
    private void checkSmscode(String id, String smsCode) {
        LoggerUtil.debug("验证验证码smsCode-------------->" + smsCode + "\nid------------>" + id);
        RequestParams params = new RequestParams("utf-8");
        params.addQueryStringParameter("transCode", Contants.TRANS_CODE_CHECK_YZM);
        params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
        params.addQueryStringParameter("id", id);//注册接口返回的cacheKey
        params.addQueryStringParameter("smsCode", smsCode);//验证码
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = Contants.MSG_REGISTER_SUCCESS;
                mHandler.handleMessage(msg);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                // TODO Auto-generated method stub
                cacheKey = "";
            }

            @Override
            public void onError(String returnCode, String returnMsg) {
                // TODO Auto-generated method stub
                cacheKey = "";
                Message msg = new Message();
                msg.what = Contants.MSG_REGISTER_FAILURE;
                msg.obj = returnMsg;
                mHandler.handleMessage(msg);
            }
        });
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0,
//							String error) {
//						// TODO Auto-generated method stub
//						cacheKey="";
//						LoggerUtil.debug( "error-------------->" + error);
//						//String returnMsg = error;// 错误提示
//						Message msg = new Message();
//						msg.what = Contants.MSG_REGISTER_FAILURE;
//						msg.obj ="网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(
//							ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug( "验证验证码result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode == 200) {
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson
//									.fromJson(responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								LoggerUtil.debug( "验证验证码成功，注册成功!");
//								Message msg = new Message();
//								msg.what = Contants.MSG_REGISTER_SUCCESS;
//								mHandler.handleMessage(msg);
//							} else
//							{
//								cacheKey="";
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_REGISTER_FAILURE;
//    							msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//							}
//
//						}
//
//					}
//				});
    }

    //用户注册接口
    private void register(String mobile, String password, String verifycode) {
        mdialog = Util.createLoadingDialog(this, "注册中,请稍后...");
        LoggerUtil.debug("222222mobile-------------->" + mobile + "\npassword------------>" + password + "\nyzm-------->" + yzm);
        RequestParams params = new RequestParams("utf-8");
        params.addQueryStringParameter("transCode", Contants.TRANS_CODE_REGISTER);
        params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
        params.addQueryStringParameter("pwd", password);//密码
        params.addQueryStringParameter("mobilePhone", mobile);//电话号码
        params.addQueryStringParameter("verifyCode", verifycode);//验证码
        params.addQueryStringParameter("isNo", "1");
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson
                        .fromJson(data, type);
                String cacheKey = resultMap.get("cacheKey");// 缓存id
                LoggerUtil.debug("用户注册成功" + cacheKey);
                Message msg = new Message();
                msg.what = Contants.MSG_M100101_SUCCESS;
                msg.obj = cacheKey;
                mHandler.handleMessage(msg);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                mdialog.show();
            }

            @Override
            public void onFailure(HttpException exception, String returnMsg) {
                // TODO Auto-generated method stub
                cacheKey = "";
                Message msg = new Message();
                msg.what = Contants.MSG_REGISTER_FAILURE;
                msg.obj = returnMsg;
                mHandler.handleMessage(msg);
            }

            @Override
            public void onError(String returnCode, String returnMsg) {
                // TODO Auto-generated method stub
                cacheKey = "";
                Message msg = new Message();
                msg.what = Contants.MSG_REGISTER_FAILURE;
                msg.obj = returnMsg;
                mHandler.handleMessage(msg);
            }
        });
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//                     @Override
//                    public void onStart() {
//                    	// TODO Auto-generated method stub
//                    	super.onStart();
//                    	mdialog.show();
//                    }
//					@Override
//					public void onFailure(HttpException arg0,
//							String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug( "用户注册：error-------------->" + error);
//						cacheKey="";
//						Message msg = new Message();
//						msg.what = Contants.MSG_REGISTER_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
////						cacheKey="";
////						Message msg = new Message();
////						msg.obj="";
////						msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
////						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(
//							ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("用户注册：result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode == 200) {
//							
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson
//									.fromJson(responseInfo.result, type);
//							String returnCode = resultMap
//									.get("returnCode");
//							if ("000000".equals(returnCode)) {	
//								
//								String cacheKey = resultMap.get("cacheKey");// 缓存id
//								LoggerUtil.debug("用户注册成功"+cacheKey);
////								String snumber = resultMap.get("snumber");// 序号
////								String loginName = resultMap.get("loginName");// 昵称
////								String userName = resultMap.get("userName");// 姓名	
////								String userSex = resultMap.get("userSex");// 性别
////								String idType = resultMap.get("idType");// 证件类型
////								String idNo = resultMap.get("idNo");// 证件号码
////								String mobilePhone = resultMap.get("mobilePhone");// 手机号
////								String userMail = resultMap.get("userMail");// 邮箱
//								Message msg = new Message();
//								msg.what = Contants.MSG_M100101_SUCCESS;
//								msg.obj=cacheKey;
//								mHandler.handleMessage(msg);
//							} else {
//								LoggerUtil.debug("用户注册失败"+cacheKey);
////								cacheKey="";
////								String returnMsg = resultMap.get("returnMsg");// 错误提示
////								Message msg = new Message();
////								msg.what = Contants.MSG_REGISTER_FAILURE;
////								msg.obj = returnMsg;
////								mHandler.handleMessage(msg);
//								cacheKey="";
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_REGISTER_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//							}
//
//						}
//
//					}
//				});
    }
}
