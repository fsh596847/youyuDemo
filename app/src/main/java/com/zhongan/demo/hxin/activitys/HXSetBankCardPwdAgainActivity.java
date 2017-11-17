package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.wknight.keyboard.WKnightKeyboard;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.JYPEditText;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 设置交易密码页面
 */
public class HXSetBankCardPwdAgainActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;//返回按钮
	private TextView mTitleView,mPwdTipTv;
    private JYPEditText mPwdEt;
    private String password1;
	private Dialog mDialog;
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mDialog.cancel();
			super.handleMessage(msg);
			switch (msg.what) {
			case Contants.MSG_SET_JIAOYI_PWD_SUCCESS:
				Intent intent = new Intent(HXSetBankCardPwdAgainActivity.this,HXDealResultActivity.class);
				intent.putExtra("checkResult","success");//验证结果：成功 success 失败failure
				startActivity(intent);
				HXSetBankCardPwdAgainActivity.this.finish();
				break;
			case Contants.MSG_SET_JIAOYI_PWD_FAILURE:
				String errorMsg=(String) msg.obj;
				Intent errorIntent = new Intent(HXSetBankCardPwdAgainActivity.this,HXDealResultActivity.class);
				errorIntent.putExtra("checkResult","failure");//验证结果：成功 success 失败failure
				errorIntent.putExtra("retrunMsg",errorMsg);//验证结果提示
				startActivity(errorIntent);
				HXSetBankCardPwdAgainActivity.this.finish();
				
				break;
			case Contants.MSG_DO_CHECK_PWD_DIFFRIENT_FAILURE:
				CustomDialog.Builder builder = new CustomDialog.Builder(
						HXSetBankCardPwdAgainActivity.this);
				// builder.setIcon(R.drawable.ic_launcher);
				// builder.setTitle(R.string.title_alert);
				builder.setMessage((String) msg.obj);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
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
		setContentView(R.layout.hxactivity_bank_card_deal_password_layout);
		password1=getIntent().getStringExtra("password");
		mDialog= Util.createLoadingDialog(this,"请稍等...");
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
		 mPwdTipTv.setText(R.string.set_deal_password_text2);
//		 mPwdEt.setFirstComplete(false);
		 final WKnightKeyboard keyboard = new WKnightKeyboard(mPwdEt);
		 if (mPwdEt != null) {
	            keyboard.setRecvTouchEventActivity(HXSetBankCardPwdAgainActivity.this);
	     }
		 mPwdEt.setOnEditTextContentListener(new JYPEditText.EditTextContentListener() {
			
			@Override
			public void onTextChanged(CharSequence text) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(CharSequence text) {
				// TODO Auto-generated method stub
//				mPwdEt.setFirstComplete(false);
				String password2=keyboard.getEnctyptText();
				keyboard.clearInput();
				mPwdEt.setText("");
				setBankCardPwd(password1,password2);
				
				
			}
		});
	}
	
	//设置交易密码接口
	private void setBankCardPwd(final String pwd1, final String pwd2)
	{   
//		 String applyAmt=sharePrefer.getString("selfRepaySum","");
		 String applyAmt="60000";
		 LoggerUtil.debug("pwd1------->"+pwd1+"\npwd2----------->"+pwd2+"\ntoken----------->"+getSharePrefer().getToken()+"\napplyAmt---------->"+applyAmt);
		 RequestParams params = new RequestParams("utf-8");
		 params.addHeader("Content-Type","application/x-www-form-urlencoded");
	     params.addBodyParameter("transCode",Contants.TRANS_CODE_SET_PAY_PASSWORD);
		 params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
		 params.addBodyParameter("clientToken",getSharePrefer().getToken());
		 params.addBodyParameter("repaymentPassword",pwd1);//支付密码
		 params.addBodyParameter("paymentPassword",pwd2);//支付密码
		 params.addBodyParameter("applyAmt",applyAmt);// 额度
		 params.addBodyParameter("productId","1");//  测试9090产品ID
		 httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Gson gson = new Gson();
					Map<String, String> resultMap = gson.fromJson(data, type);
					String flag=resultMap.get("flag");
					if("00".equals(flag))
					{
						LoggerUtil.debug( "设置支付密码成功!");
						Message msg = new Message();
						msg.what = Contants.MSG_SET_JIAOYI_PWD_SUCCESS;
//						msg.obj=returnMsg;
						mHandler.handleMessage(msg);
					}else if("11".equals(flag))
					{
						LoggerUtil.debug( "设置支付密码失败!");
						String returnMsg = resultMap.get("returnMsg");// 错误提示
						Message msg = new Message();
						msg.what = Contants.MSG_SET_JIAOYI_PWD_FAILURE;
						msg.obj=returnMsg;
						mHandler.handleMessage(msg);
					}else if("22".equals(flag))
					{
						Message msg = new Message();
						msg.what = Contants.MSG_DO_CHECK_PWD_DIFFRIENT_FAILURE;
						msg.obj="两次密码输入不一致!";
						mHandler.handleMessage(msg);
					}	
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				 mDialog.show();
			}
			
			@Override
			public void onFailure(HttpException exception, String msg) {
				// TODO Auto-generated method stub
				mDialog.cancel();
			}
			
			@Override
			public void onError(String returnCode, String returnMsg) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = Contants.MSG_SET_JIAOYI_PWD_FAILURE;
				msg.obj=returnMsg;
				mHandler.handleMessage(msg);
			}
		});
//		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//		 new RequestCallBack<String>() {
//                        @Override
//                        public void onStart() {
//                        // TODO Auto-generated method stub
//                         super.onStart();
//                         mDialog.show();
//                        }
//						@Override
//						public void onFailure(HttpException arg0,
//								String error) {
//							// TODO Auto-generated method stub
//							LoggerUtil.debug("设置交易密码:error-------------->" + error);
//							Message msg = new Message();
//							msg.what = Contants.MSG_SET_JIAOYI_PWD_FAILURE;
//							msg.obj="网络问题!";
//							mHandler.handleMessage(msg);
//						}
//
//						@Override
//						public void onSuccess(
//								ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug( "设置交易密码：result---->"
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
//									String flag=resultMap.get("flag");
//									if("00".equals(flag))
//									{
//										LoggerUtil.debug( "设置支付密码成功!");
//										Message msg = new Message();
//										msg.what = Contants.MSG_SET_JIAOYI_PWD_SUCCESS;
////										msg.obj=returnMsg;
//										mHandler.handleMessage(msg);
//									}else if("11".equals(flag))
//									{
//										LoggerUtil.debug( "设置支付密码失败!");
//										String returnMsg = resultMap.get("returnMsg");// 错误提示
//										Message msg = new Message();
//										msg.what = Contants.MSG_SET_JIAOYI_PWD_FAILURE;
//										msg.obj=returnMsg;
//										mHandler.handleMessage(msg);
//									}else if("22".equals(flag))
//									{
//										Message msg = new Message();
//										msg.what = Contants.MSG_DO_CHECK_PWD_DIFFRIENT_FAILURE;
//										msg.obj="两次密码输入不一致!";
//										mHandler.handleMessage(msg);
//									}	
//								} else 
//								{
//									String returnMsg = resultMap.get("returnMsg");// 错误提示
//									Message msg = new Message();
//									msg.what = Contants.MSG_SET_JIAOYI_PWD_FAILURE;
//									msg.obj=returnMsg;
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
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			HXSetBankCardPwdAgainActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
