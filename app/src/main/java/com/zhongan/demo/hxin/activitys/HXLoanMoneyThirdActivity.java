package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.wknight.keyboard.WKnightKeyboard;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.bean.HXLoanRecordDetailBean;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CountDownTimer;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.JYPEditText;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 借款借据界面
 */
public class HXLoanMoneyThirdActivity extends HXBaseActivity implements
        OnClickListener {

	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn, mSubmitBtn, mYzmBtn;
	private TextView mTitleView, mLookhetongBtn, mLoanYinhangTv, mPayYinhangTv,
			mPayTypeTv, mDayLilvTv, mStartEndDateTv, mLoanMonthTv, mLoanNumTv,
			mLoanIdcardTv, mLoanNameTv;
	private EditText mYzmEt;
	private String yzm = "";
	private String applyAmount="";//贷款金额
	private String loanTerm="";//贷款期限
	private String repayMode="";//还款方式
	private String repayModeName="";//还款方式名称
	private String repayWayId="";//还款方式id
	private String loanRate="";//贷款利率
	private String custName="";//姓名
	private String idCardNum="";//身份证号
	private String startDate="";//起始日期
	private String endDate="";//结束日期
	private String payBankName="";//还款银行名称
	private String payBankId="";//还款银行卡号
	private String loanBank="";//放款银行
	private String productId="";//产品id
	private String contractURL="";
	private Dialog mDialog;
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

	/**
	 * 用Handler来更新UI
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mDialog.cancel();
			switch (msg.what) {
			case Contants.MSG_DO_LOAN_APPLY_SUCCESS:
				Bundle successBundle = msg.getData();
				Intent successIntent = new Intent(HXLoanMoneyThirdActivity.this,
						HXLoanMoneyResultActivity.class);
				successIntent.putExtra("loanMoneyResult", successBundle);
				startActivity(successIntent);
				mYzmEt.setText("");
				timer.cancel();
				mYzmBtn.setText(R.string.yzm_btn_text);
				mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
				mYzmBtn.setClickable(true);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
	        	mSubmitBtn.setClickable(true);
				break;
			case Contants.MSG_DO_LOAN_APPLY_FAILURE:
				mYzmEt.setText("");
				timer.cancel();
				mYzmBtn.setText(R.string.yzm_btn_text);
				mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
				mYzmBtn.setClickable(true);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
	        	mSubmitBtn.setClickable(true);
				Bundle failureBundle = msg.getData();
				Intent failureIntent = new Intent(HXLoanMoneyThirdActivity.this,
						HXLoanMoneyResultActivity.class);
				failureIntent.putExtra("loanMoneyResult", failureBundle);
				startActivity(failureIntent);
				break;
			case Contants.MSG_GET_YZM_CODE_FAILURE:
				mYzmEt.setText("");
			String errorMsg=(String) msg.obj;
			CustomDialog.Builder builder = new CustomDialog.Builder(HXLoanMoneyThirdActivity.this);
			// builder.setIcon(R.drawable.ic_launcher);
			// builder.setTitle(R.string.title_alert);
			builder.setMessage(errorMsg);
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

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_loan_money_third_layout);
//		Bundle bundle=getIntent().getBundleExtra("loanDetail");
//		applyAmount=bundle.getString("applyAmount");
//		loanRate=bundle.getString("loanRate");
//		loanTerm=bundle.getString("loanTerm");
//		repayMode=bundle.getString("repayMode");
		Bundle bundle=getIntent().getBundleExtra("loanDetail");
		HXLoanRecordDetailBean loanDetail=(HXLoanRecordDetailBean) bundle.getSerializable("loanDetailInfo");
		productId=bundle.getString("productId");
//		applyAmount=loanDetail.getApplyAmt();//贷款金额
		loanRate=loanDetail.getDayRate();//日利率
//		loanTerm=loanDetail.getPeriod();//贷款期限
//		repayMode=loanDetail.getRepayMode();//还款方式
		applyAmount=bundle.getString("applyAmount");
//		loanRate=bundle.getString("loanRate");
		loanTerm=bundle.getString("loanTerm");
		repayMode=bundle.getString("repayMode");
		repayModeName=bundle.getString("repayModeName");
		repayWayId=bundle.getString("repayWayId");
		custName=loanDetail.getCustomerName();
		idCardNum=loanDetail.getIdentityCardNo();
		startDate=loanDetail.getStartDate();
		endDate=loanDetail.getEndDate();
		payBankName=loanDetail.getRepayBankName();
		payBankId=loanDetail.getRepayBankId();
		loanBank=loanDetail.getPayLoanBankName();
		contractURL=loanDetail.getContractUrl();
		mDialog=Util.createLoadingDialog(this,"数据加载中,请稍等...");
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
		mTitleView.setText(R.string.loan_money_jieju_text);
		mBackBtn.setOnClickListener(this);
		mLoanNameTv = (TextView) findViewById(R.id.loan_money_name_tv);// 姓名
		mLoanNameTv.setText(custName);
		mLoanIdcardTv = (TextView) findViewById(R.id.loan_money_idcard_tv);// 身份证号
		String hideIdCard=Util.hideIdentityCard(idCardNum);
		mLoanIdcardTv.setText(hideIdCard);
		mLoanNumTv = (TextView) findViewById(R.id.loan_money_loan_num_tv);// 贷款金额
		mLoanNumTv.setText(applyAmount+"(元)");
		mLoanMonthTv = (TextView) findViewById(R.id.loan_money_daikuanqixian_tv);// 贷款期限
		mLoanMonthTv.setText(loanTerm+"个月");
		mStartEndDateTv = (TextView) findViewById(R.id.loan_money_start_end_date_tv);// 起止时间
		mStartEndDateTv.setText(startDate+"~"+endDate);
		mDayLilvTv = (TextView) findViewById(R.id.loan_money_lilv_day_tv);// 日利率
		mDayLilvTv.setText(loanRate+"%");
		mPayTypeTv = (TextView) findViewById(R.id.loan_money_loan_pay_type_tv);// 还款方式
		mPayTypeTv.setText(repayModeName);
		mPayYinhangTv = (TextView) findViewById(R.id.loan_money_pay_yanhang_tv);// 还款银行
		mPayYinhangTv.setText(payBankName+" ("+payBankId+")");
		mLoanYinhangTv = (TextView) findViewById(R.id.loan_money_loan_yinhang_tv);// 放款银行
		mLoanYinhangTv.setText(loanBank);
		mLookhetongBtn = (TextView) findViewById(R.id.loan_money_look_hetong_tv);// 查看合同
		mLookhetongBtn.setOnClickListener(this);
		mYzmEt = (EditText) findViewById(R.id.loan_money_yzm_et);
		mYzmBtn = (Button) findViewById(R.id.loan_money_yzm_btn);
		mYzmBtn.setOnClickListener(this);
		mSubmitBtn = (Button) findViewById(R.id.loan_money_third_submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mSubmitBtn.setClickable(false);
		mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		mYzmEt.addTextChangedListener(textWatcher);
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
			yzm = mYzmEt.getText().toString().trim();
			if (yzm != null && !yzm.equals("") && yzm.length() == 6) {

				mSubmitBtn
						.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				mSubmitBtn.setClickable(true);
			} else {

				mSubmitBtn.setClickable(false);
				mSubmitBtn
						.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
			}
		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			HXLoanMoneyThirdActivity.this.finish();
			break;
		case R.id.loan_money_yzm_btn:
			// 获取验证码
			getMsmCode("6",getSharePrefer().getPhone());
			
			break;
		case R.id.loan_money_third_submit_btn:
			// 提交
			yzm = mYzmEt.getText().toString().trim();
			if(yzm==null||yzm.equals(""))
			{
//				Message msg = new Message();
//				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//				msg.obj = "验证码不能为空,请输入!";
//				mHandler.handleMessage(msg);
				//Toast.makeText(HXLoanMoneyThirdActivity.this, "验证码不能为空,请输入!", Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("验证码不能为空,请输入!" ,HXLoanMoneyThirdActivity.this);
				return;
			}
			//判断验证码是否合法
			if(Util.checkVerCode(yzm))
			{
				initPasswordPopWindow(view);
			}else
			{
				
//				Message msg = new Message();
//				msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//				msg.obj = "请输入6位合法验证码!";
//				mHandler.handleMessage(msg);
				//Toast.makeText(HXLoanMoneyThirdActivity.this, "请输入6位合法验证码!", Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("请输入6位合法验证码" ,HXLoanMoneyThirdActivity.this);
				return;
			}
						
			break;
		case R.id.loan_money_look_hetong_tv:
			// 查看合同
			Intent useLinesIntent=new Intent(HXLoanMoneyThirdActivity.this,HXUserLineActivity.class);
			useLinesIntent.putExtra("title", "查看合同");
			useLinesIntent.putExtra("url",Contants.BASE_URL+contractURL);
            startActivity(useLinesIntent);
			break;
		default:
			break;
		}
	}

	private void initPasswordPopWindow(View view) {
		// 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
		LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
		View contentView = inflater.inflate(R.layout.hxpassword_pop_layout, null);
		final PopupWindow mPwdPopWindow = new PopupWindow(contentView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x60000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		mPwdPopWindow.setBackgroundDrawable(dw);
		// 设置SelectPicPopupWindow弹出窗体可点击
		// mPopWindow.setFocusable(true);
		// mPopWindow.setOutsideTouchable(true);
		// 刷新状态
		mPwdPopWindow.update();
		LinearLayout warpLL = (LinearLayout) contentView
				.findViewById(R.id.jiaoyi_pwd_ll);
		warpLL.setFocusableInTouchMode(false);
		final JYPEditText mPwdEt = (JYPEditText) contentView
				.findViewById(R.id.loan_money_deal_passwordnum_et);
		final WKnightKeyboard keyboard = new WKnightKeyboard(mPwdEt);
		if (mPwdEt != null) {
            keyboard.setRecvTouchEventActivity(HXLoanMoneyThirdActivity.this);
        }
//		mPwdEt.setFocusable(false);
		mPwdEt.setOnEditTextContentListener(new JYPEditText.EditTextContentListener() {

			@Override
			public void onTextChanged(CharSequence text) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(CharSequence text) {
				// TODO Auto-generated method stub
				
				String password = keyboard.getEnctyptText();
				doLoanApply(applyAmount,loanTerm,password,getSharePrefer().getPhone(),yzm,productId,repayWayId);
//				 Bundle bundle=new Bundle();
//				 bundle.putString("returnMsg", "借款失败");
//				 bundle.putString("resultCode","111111111111111");
//				
//				 Message msg = new Message();
//				 msg.what = Contants.MSG_DO_LOAN_APPLY_FAILURE;
//				 msg.setData(bundle);
//				 mHandler.handleMessage(msg);
	
				if (mPwdPopWindow.isShowing()) {
					mPwdEt.setFocusable(false);
					mPwdEt.clearFocus();
					mPwdPopWindow.dismiss();	

				}
			}
		});
		mPwdPopWindow.showAtLocation(view, 0, 0, 0);
	}
	 //获取短信验证码接口
	   private void getMsmCode(String isNo, String mobile)
	   {
		     mYzmBtn.setClickable(false);// 防止重复点击
		    RequestParams params = new RequestParams("utf-8");
			params.addQueryStringParameter("transCode",Contants.TRANS_CODE_YZM);
			params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
			params.addBodyParameter("clientToken",getSharePrefer().getToken());
			params.addQueryStringParameter("id", isNo);//注册接口返回的cacheKey
			params.addQueryStringParameter("isNo",isNo);
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
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//					new RequestCallBack<String>() {
//
//						@Override
//						public void onFailure(HttpException arg0,
//								String error) {
//							// TODO Auto-generated method stub
//							LoggerUtil.debug("error-------------->" + error);
//							//String returnMsg = error;// 错误提示
////							Message msg = new Message();
////							msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
////							msg.obj ="网络问题";
////							mHandler.handleMessage(msg);
//							mYzmBtn.setClickable(true);
//						}
//
//						@Override
//						public void onSuccess(
//								ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug("获取验证码result---->"
//									+ responseInfo.result
//									+ "\nresponseInfo.statusCode ===="
//									+ responseInfo.statusCode);
//							
//							if (responseInfo.statusCode == 200) {
//								Type type = new TypeToken<Map<String, String>>() {
//								}.getType();
//								Gson gson = new Gson();
//								Map<String, String> resultMap = gson
//										.fromJson(responseInfo.result, type);
//								String returnCode = resultMap
//										.get("returnCode");
//								if ("000000".equals(returnCode)) {
//									timer.start();
//									LoggerUtil.debug("获取验证码成功!");
//								} else {
//									LoggerUtil.debug("获取验证码失败!");
//									String returnMsg = resultMap.get("returnMsg");// 错误提示
//									Message msg = new Message();
//									msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//									msg.obj = returnMsg;
//									mHandler.handleMessage(msg);
//									mYzmBtn.setClickable(true);
//								}
//
//							}
//
//						}
//					});
	   }
	// 贷款业务申请接口
	private void doLoanApply(String applyAmt, String loanTerm, String pwd, String moblile, String yzm, String productId, String repayWayId) {
		
		mSubmitBtn.setClickable(false);
		mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		LoggerUtil.debug("贷款业务申请 : clientToken--->"+getSharePrefer().getToken()+"\napplyAmt----->"+applyAmt+"\npwd------------>"+pwd);
		RequestParams params = new RequestParams("utf-8");
		params.addQueryStringParameter("transCode",Contants.TRANS_CODE_DO_LOAN_APPLY);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addBodyParameter("clientToken",getSharePrefer().getToken());
		params.addBodyParameter("productId",productId);//  测试9090产品ID
		params.addQueryStringParameter("applyAmt",applyAmt);// 申请金额
		params.addQueryStringParameter("payPwd",pwd);// 支付密码   	
		params.addQueryStringParameter("loanterm",loanTerm);// 贷款期限	
		params.addQueryStringParameter("cell",moblile);//手机号
		params.addQueryStringParameter("smsCode",yzm);//验证码
		params.addQueryStringParameter("repayWayId",repayWayId);//还款方式id
		params.addQueryStringParameter("isNo","6");
		httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Gson gson = new Gson();
					
					Map<String, String> resultMap = gson.fromJson(
							data, type);
					String returnCode = resultMap.get("returnCode");
					String applyAmt = resultMap.get("applyAmt");// 贷款金额
					String bankInfo = resultMap.get("bankInfo");// 放款至银行
					Bundle bundle = new Bundle();
					bundle.putString("applyAmt", applyAmt);
					bundle.putString("bankInfo", bankInfo);
					bundle.putString("resultCode", returnCode);
					LoggerUtil
							.debug("贷款业务申请---->applyAmt---------->"
									+ applyAmt
									+ "\nbankInfo----------->"
									+ bankInfo);
					Message msg = new Message();
					msg.what = Contants.MSG_DO_LOAN_APPLY_SUCCESS;
					msg.setData(bundle);
					mHandler.handleMessage(msg);
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
            mSubmitBtn.setClickable(true);
		    mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				mDialog.cancel();
				mSubmitBtn.setClickable(true);
		    	mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				if("E1001061".equals(returnCode))
				{
						//验证码输入错误
//						Message errorMsg = new Message();
//						errorMsg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//						errorMsg.obj=msg;
//						mHandler.handleMessage(errorMsg);
						
				}else
				{
						Bundle bundle = new Bundle();
						bundle.putString("returnMsg", msg);
						bundle.putString("resultCode", returnCode);
						LoggerUtil.debug("贷款业务申请---->" + msg);
						Message errorMsg = new Message();
						errorMsg.what = Contants.MSG_DO_LOAN_APPLY_FAILURE;
						errorMsg.setData(bundle);
						mHandler.handleMessage(errorMsg);

				}
			}
		});
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                    	// TODO Auto-generated method stub
//                    	super.onStart();
//                    	mDialog.show();
//                    }
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("贷款业务申请 error-------------->" + error);
//						 Message msg = new Message();
//						 msg.what = Contants.MSG_DO_LOAN_APPLY_FAILURE;
//						 msg.obj="网络问题";
//						 mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("贷款业务申请：result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode == 200) {
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(
//									responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								String applyAmt = resultMap.get("applyAmt");// 贷款金额
//								String bankInfo = resultMap.get("bankInfo");// 放款至银行
//								Bundle bundle = new Bundle();
//								bundle.putString("applyAmt", applyAmt);
//								bundle.putString("bankInfo", bankInfo);
//								bundle.putString("resultCode", returnCode);
//								LoggerUtil
//										.debug("贷款业务申请---->applyAmt---------->"
//												+ applyAmt
//												+ "\nbankInfo----------->"
//												+ bankInfo);
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_LOAN_APPLY_SUCCESS;
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//
//							}else if("E1001061".equals(returnCode))
//							{
//								//验证码输入错误
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//								msg.obj=returnMsg;
//								mHandler.handleMessage(msg);
//								
//							}else {
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Bundle bundle = new Bundle();
//								bundle.putString("returnMsg", returnMsg);
//								bundle.putString("resultCode", returnCode);
//								LoggerUtil.debug("贷款业务申请---->" + returnMsg);
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_LOAN_APPLY_FAILURE;
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//
//							}
//
//						}
//
//					}
//				});
	}
}
