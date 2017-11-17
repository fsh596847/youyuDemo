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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.adapters.HXBankListAdapter;
import com.zhongan.demo.hxin.adapters.HXBasicLinesListAdapter;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.AssetsBankInfo;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CardInputEditText;
import com.zhongan.demo.hxin.view.CountDownTimer;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行卡验证、添加页面
 */
public class HXDealBankCardActivity extends HXBaseActivity implements
        OnClickListener {
    private View mStatusView;
    private Button mBackBtn, mYzmBtn, mSubmitBtn;
    private TextView mTitleView, mOpenNameTv, mOpenIdCardTipTv, mOpenIdCardTv, mBankNameTv, mAllLinesBtn;
    private EditText mMoblieEt, mYzmEt;
    private CardInputEditText mOpenCardIdET;
    private ImageView mWarnBtn;
    private String openName, openIdcard, openCardId, openMobile, openYzm;
    private PopupWindow mBanksPopWindow;
    private View mLineOne, mLineThree;
    private RelativeLayout mOpenPhoneRl;
    private LinearLayout mBankNameLL;
    private String option = "";
    private HttpUtils dataHttp;
    private Dialog mDialog;
    private PopupWindow mLinesPopWindow;
    private CheckBox mAllLinesCb;
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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            mDialog.cancel();
            switch (msg.what) {
                case Contants.MSG_GET_YZM_CODE_SUCCESS:
                    // 获取验证码
                    timer.start();
                    break;
                case Contants.MSG_CHECK_BNAK_CARD_INFO_SUCCESS:
                    Intent submitIntent = new Intent(HXDealBankCardActivity.this,
                            HXResultActivity.class);
                    submitIntent.putExtra("message","审批通过啦");
                    submitIntent.putExtra("isSuccess",true);
                    startActivity(submitIntent);
                    mSubmitBtn
                            .setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    mSubmitBtn.setClickable(true);
//				mYzmEt.setText("");
                    timer.cancel();
                    mYzmBtn.setText(R.string.yzm_btn_text);
                    mYzmBtn.setTextColor(getResources().getColor(R.color.color_ff7920));
                    mYzmBtn.setClickable(true);
                    finish();
                    break;
                case Contants.MSG_CHECK_BNAK_CARD_INFO_FAILURE:
                    mSubmitBtn
                            .setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    mSubmitBtn.setClickable(true);
//                    String errorMsg = (String) msg.obj;
//                    Intent intent = new Intent(HXDealBankCardActivity.this,
//                            HXDealResultActivity.class);
//                    intent.putExtra("checkResult", "failure");// 验证结果：成功 success // 失败failure
//                    intent.putExtra("retrunMsg", errorMsg);// 验证结果提示
//                    startActivity(intent);
                    mYzmEt.setText("");
                    timer.cancel();
                    mYzmBtn.setText(R.string.yzm_btn_text);
                    mYzmBtn.setTextColor(getResources().getColor(
                            R.color.color_ff7920));
                    mYzmBtn.setClickable(true);
                    break;
                case Contants.MSG_UPLOAD_CUST_INFO_FAILURE:
                case Contants.MSG_GET_YZM_CODE_FAILURE:

                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            HXDealBankCardActivity.this);
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
        setContentView(R.layout.hxactivity_bank_card_deal_layout);
        option = getIntent().getStringExtra("option");
//        openName = getIntent().getStringExtra("selfName");// 姓名
//        openIdcard = getIntent().getStringExtra("selfIdcard");// 身份证号

        openName = getSharePrefer().getUserName();
        openIdcard = getSharePrefer().getIdCardNum();

        mDialog = Util.createLoadingDialog(this, "请稍等...");
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

        mBackBtn.setOnClickListener(this);
        mOpenNameTv = (TextView) findViewById(R.id.open_card_name_value_tv);// 姓名
        mOpenIdCardTipTv = (TextView) findViewById(R.id.open_card_idcard_tip_tv);// 身份证提示
        mOpenIdCardTv = (TextView) findViewById(R.id.open_card_idcard_value_tv);// 身份证号
        mAllLinesCb = (CheckBox) findViewById(R.id.allow_the_lines_cb);
        mAllLinesBtn = (TextView) findViewById(R.id.all_the_lines_tv);
        mAllLinesBtn.setOnClickListener(this);
        mLineOne = findViewById(R.id.open_line_two);
        mBankNameLL = (LinearLayout) findViewById(R.id.open_card_bankname_ll);
        mBankNameTv = (TextView) findViewById(R.id.open_card_bankname);//银行卡名称
        mLineThree = findViewById(R.id.open_line_seven);
        mOpenCardIdET = (CardInputEditText) findViewById(R.id.open_card_no_value_et);// 卡号
        mWarnBtn = (ImageView) findViewById(R.id.open_card_warn_btn);
        mMoblieEt = (EditText) findViewById(R.id.open_card_phonenum_value_et);// 手机号
        mYzmEt = (EditText) findViewById(R.id.open_card_yzm_value_et);// 验证码
        mYzmBtn = (Button) findViewById(R.id.open_yzm_btn);
        mSubmitBtn = (Button) findViewById(R.id.open_submit_btn);// 提交按钮
        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
        mSubmitBtn.setOnClickListener(this);
        mWarnBtn.setOnClickListener(this);
        mYzmBtn.setOnClickListener(this);
        mOpenPhoneRl = (RelativeLayout) findViewById(R.id.open_card_two_rl);
        mOpenCardIdET.addTextChangedListener(textWacher);
        mMoblieEt.addTextChangedListener(textWacher);
        mYzmEt.addTextChangedListener(textWacher);
        if (option.equals("checkThree")) {
            // 验证三流程
            mOpenPhoneRl.setVisibility(View.GONE);
            mOpenIdCardTipTv.setVisibility(View.VISIBLE);
            mOpenIdCardTv.setVisibility(View.VISIBLE);
            mLineOne.setVisibility(View.VISIBLE);
            mBankNameLL.setVisibility(View.GONE);
            mLineThree.setVisibility(View.GONE);
            mTitleView.setText(R.string.check_shenfen_text);// 身份验证
        } else {
            if (option.equals("add")) {

                mTitleView.setText(R.string.add_bank_card_text);// 添加银行卡

            } else if (option.equals("check")) {
                mTitleView.setText(R.string.bank_card_yz_text);// 验证银行卡
                mOpenNameTv.setText(openName);
                mOpenIdCardTv.setText(openIdcard);
            }
            mOpenPhoneRl.setVisibility(View.VISIBLE);
            mOpenIdCardTipTv.setVisibility(View.GONE);
            mOpenIdCardTv.setVisibility(View.GONE);
            mLineOne.setVisibility(View.GONE);
            mBankNameLL.setVisibility(View.VISIBLE);
            mLineThree.setVisibility(View.VISIBLE);
        }
    }

    private TextWatcher textWacher = new TextWatcher() {

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
            // TODO Auto-generated method stub
            openName = mOpenNameTv.getText().toString().trim();
            openIdcard = mOpenIdCardTv.getText().toString().trim();
            openCardId = mOpenCardIdET.getTextWithoutSpace().trim();
            openMobile = mMoblieEt.getText().toString().trim();
            openYzm = mYzmEt.getText().toString().trim();
            if (option.equals("checkThree")) {
                if (openName != null && !openName.equals("")
                        && openCardId != null && !openCardId.equals("")
                        && openIdcard != null && !openIdcard.equals("")) {
                    mSubmitBtn
                            .setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    mSubmitBtn.setClickable(true);
                } else {
                    mSubmitBtn.setClickable(false);
                    mSubmitBtn
                            .setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
                }

            } else {
                if (openName != null && !openName.equals("") && openCardId != null && !openCardId.equals("") && openCardId.length() > 10 && openCardId.length() <= 19) {
                    String openCardId6Num = openCardId.substring(0, 6);
                    int sixNum = Integer.parseInt(openCardId6Num);
                    LoggerUtil.debug("sixNum" + sixNum);
                    String nameOfBank = AssetsBankInfo.getNameOfBank(HXDealBankCardActivity.this, sixNum);//获取银行卡的信息
                    String[] bankInfo = nameOfBank.split("-");
                    String bankName = bankInfo[0];
                    LoggerUtil.debug("bankName" + bankName);
                    mBankNameTv.setText(bankName);
                }
                if (openName != null && !openName.equals("")
                        && openCardId != null && !openCardId.equals("") && openCardId.length() > 10 && openCardId.length() <= 19
                        && openMobile != null && !openMobile.equals("")
                        && openYzm != null && !openYzm.equals("") && openYzm.length() == 6) {

                    mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    mSubmitBtn.setClickable(true);
                } else {
                    mSubmitBtn.setClickable(false);
                    mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
                }

            }

        }
    };

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_btn:
                // 返回
////			HXDealBankCardActivity.this.finish();
//			Intent intent = new Intent(HXDealBankCardActivity.this,HXMainActivity.class);
////			intent.putExtra("flag",0);//0：返回首页 2：返回我的账户
//			startActivity(intent);
                Intent intent = new Intent(this, MenuListActivity2.class);
                startActivity(intent);

                break;
            case R.id.open_card_warn_btn:
                // 提示
                initAllBanksPopWindow();
                mBanksPopWindow.showAtLocation(view, 0, 0, 0);
                break;
            case R.id.open_yzm_btn:
                if (option.equals("checkThree")) {
                    openMobile = "";

                } else {
                    openMobile = mMoblieEt.getText().toString().trim();

                }
                //判断手机号是否合法
                if (!Util.isMobile(openMobile)) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "手机号码不能为空!";
//				mHandler.sendMessage(msg);
                    //Toast.makeText(HXDealBankCardActivity.this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("手机号码不能为空!",HXDealBankCardActivity.this);
                    mMoblieEt.setAnimation(Util.shakeAnimation(10));
                    return;

                }
                // //获取验证码
                getMsmCode("4", openMobile);


                break;
            case R.id.open_submit_btn:
                openName = mOpenNameTv.getText().toString().trim();
                openIdcard = mOpenIdCardTv.getText().toString().trim();
                openCardId = mOpenCardIdET.getTextWithoutSpace().trim();
                openMobile = mMoblieEt.getText().toString().trim();
                openYzm = mYzmEt.getText().toString().trim();

                // 提交
                if (option.equals("checkThree")) {
                    // 验三
                    Intent submitIntent = new Intent(HXDealBankCardActivity.this,
                            HXFaceStartActivity.class);
                    startActivity(submitIntent);
                } else {
                    // 验证四
                    //判断手机号是否合法
                    if (Util.isMobile(openMobile)) {
                        //判断验证码是否合法
                        if (Util.checkVerCode(openYzm)) {
                            if (!mAllLinesCb.isChecked()) {
                                Message msg = new Message();
                                msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
                                msg.obj = "请先勾选我已同意并阅读《相关协议》";
                                mHandler.sendMessage(msg);
                                return;
                            } else {
                                checkBankCard(openCardId, openMobile, openIdcard, openName, openYzm);
                            }
                        } else {
//						Message msg = new Message();
//						msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//						msg.obj = "请输入6位合法验证码!";
//						mHandler.sendMessage(msg);
                            Toast.makeText(HXDealBankCardActivity.this, "请输入6位合法验证码!", Toast.LENGTH_SHORT).show();
                            mYzmEt.setAnimation(Util.shakeAnimation(10));
                        }

                    } else {
//					Message msg = new Message();
//					msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//					msg.obj = "请输入11位合法手机号码!";
//					mHandler.sendMessage(msg);
                        Toast.makeText(HXDealBankCardActivity.this, "请输入11位合法手机号码!", Toast.LENGTH_SHORT).show();
                        mMoblieEt.setAnimation(Util.shakeAnimation(10));
                        return;
                    }


                }
                break;
            case R.id.all_the_lines_tv:
                initAllLinesPopWindow();
                mLinesPopWindow.showAtLocation(view, 0, 0, 0);
                break;
            default:
                break;
        }
    }

    // 获取短信验证码接口
    private void getMsmCode(String isNo, String mobile) {
        mYzmBtn.setClickable(false);
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addQueryStringParameter("transCode", Contants.TRANS_CODE_YZM);
        params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken",
                getSharePrefer().getToken());
        params.addQueryStringParameter("isNo", isNo);
        params.addQueryStringParameter("id", isNo);
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
                mYzmBtn.setClickable(true);
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                mYzmBtn.setClickable(true);
//				Message errorMsg = new Message();
//				errorMsg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//				errorMsg.obj = msg;
//				mHandler.handleMessage(errorMsg);
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
//						mYzmBtn.setClickable(true);
//						Message msg = new Message();
//						msg.what = Contants.MSG_GET_YZM_CODE_FAILURE;
//						msg.obj = "网络问题!";
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
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(
//									responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								LoggerUtil.debug("获取验证码成功!");
//							
//								// String authCode = resultMap.get("snumber");//
//								// 验证码
//								 Message msg = new Message();
//								 msg.what = Contants.MSG_GET_YZM_CODE_SUCCESS;
//								
//								 mHandler.handleMessage(msg);
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

    // 银行卡验证四接口
    private void checkBankCard(final String bankCardid, String mobile,
                               String idnumber, String name, String yzm) {

        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
        LoggerUtil.debug("银行卡验证四: bankCardid--->" + bankCardid
                + "\nmobile---------->" + mobile + "\nidnumber---------->"
                + idnumber + "\nname---->" + name);
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode",
                Contants.TRANS_CODE_BANK_CARD_CHECK);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        // params.addBodyParameter("branchNo", Contants.BRANCH_NO);//机构id(法人编号)
        params.addBodyParameter("bankid", bankCardid);// 银行卡号
        params.addBodyParameter("cell", mobile);// 电话号
        params.addBodyParameter("idnumber", idnumber);// 身份证
        params.addBodyParameter("name", name);// 姓名
        params.addBodyParameter("smsCode", yzm);// 验证码
        params.addBodyParameter("isNo", "4");
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.cancel();
                }
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(
                        data, type);
                String code = resultMap.get("code");
                if ("00".equals(code)) {
                    LoggerUtil.debug("银行卡验证四成功");
                    Intent intent = new Intent(HXDealBankCardActivity.this,
                            HXFaceStartActivity.class);
                    startActivity(intent);
                    HXDealBankCardActivity.this.finish();
//                    Message msg = new Message();
//                    msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_SUCCESS;
//                    mHandler.handleMessage(msg);
                } else if ("02".equals(code)) {
                    LoggerUtil.debug("银行卡验证四失败");
                    Toast.makeText(HXDealBankCardActivity.this,"银行卡验证四失败",Toast.LENGTH_SHORT).show();
//                    Message msg = new Message();
//                    msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_FAILURE;
//                    msg.obj = "这不是我要的结果";
//                    mHandler.handleMessage(msg);
                }
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                mDialog.show();
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                mSubmitBtn
                        .setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                mSubmitBtn.setClickable(true);
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.cancel();
                }
            }

            @Override
            public void onError(String returnCode, String msg) {
                if (mDialog != null) {
                    mDialog.cancel();
                }
                // TODO Auto-generated method stub
                mSubmitBtn
                        .setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                mSubmitBtn.setClickable(true);
                if ("E1001065".equals(returnCode)) {
                    // 已经做过验四，直接进入设置密码页面
                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_SUCCESS;
                    mHandler.handleMessage(errorMsg);
                } else {
                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_FAILURE;
                    errorMsg.obj = msg;
                    mHandler.handleMessage(errorMsg);

                }
            }
        });
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//				Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                    	// TODO Auto-generated method stub
//                    	super.onStart();
//                    	mDialog.show();
//                    }
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("error-------------->" + error);
//						Message msg = new Message();
//						msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("银行卡验证四：result---->"
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
//								String code = resultMap.get("code");
//								if ("00".equals(code)) {
//									LoggerUtil.debug("银行卡验证四成功");
//									Message msg = new Message();
//									msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_SUCCESS;
//									mHandler.handleMessage(msg);
//								} else if ("02".equals(code)) {
//									LoggerUtil.debug("银行卡验证四失败");
//									Message msg = new Message();
//									msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_FAILURE;
//									msg.obj = "这不是我要的结果";
//									mHandler.handleMessage(msg);
//								}
//
//							}else if("E1001065".equals(returnCode))
//							{
//								//已经做过验四，直接进入设置密码页面
//								Message msg = new Message();
//								msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_SUCCESS;
//								mHandler.handleMessage(msg);
//							}
//							else {
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_CHECK_BNAK_CARD_INFO_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//
//							}
//
//						}
//
//					}
//				});
    }

    private void initAllBanksPopWindow() {
        // 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
        View contentView = inflater.inflate(
                R.layout.hxall_support_bank_pop_layout, null);
        mBanksPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        mBanksPopWindow.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体可点击
        // mPopWindow.setFocusable(true);
        // mPopWindow.setOutsideTouchable(true);
        // 刷新状态
        mBanksPopWindow.update();
        ListView mListView = (ListView) contentView
                .findViewById(R.id.support_bank_listview);
        ImageView mCloseBtn = (ImageView) contentView
                .findViewById(R.id.support_bank_close_btn);
        mCloseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mBanksPopWindow != null && mBanksPopWindow.isShowing()) {
                    mBanksPopWindow.dismiss();
                    mBanksPopWindow = null;
                }
            }
        });
        List<Map<String, String>> banks = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("bankName", "厦门银行");
        map1.put("bankNameBreif", "福建农信");
        banks.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("bankName", "福建海峡");
        map2.put("bankNameBreif", "泉州银行");
        banks.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("bankName", "工商银行");
        map3.put("bankNameBreif", "农业银行");
        banks.add(map3);
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("bankName", "中国银行");
        map4.put("bankNameBreif", "建设银行");
        banks.add(map4);
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("bankName", "交通银行");
        map5.put("bankNameBreif", "邮蓄银行");
        banks.add(map5);
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("bankName", "招商银行");
        map6.put("bankNameBreif", "光大银行");
        banks.add(map6);

        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("bankName", "平安银行");
        map7.put("bankNameBreif", "中信银行");
        banks.add(map7);
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("bankName", "民生银行");
        map8.put("bankNameBreif", "兴业银行");
        banks.add(map8);
        Map<String, String> map9 = new HashMap<String, String>();
        map9.put("bankName", "华夏银行");
        map9.put("bankNameBreif", "浦发银行");
        banks.add(map9);
        Map<String, String> map10 = new HashMap<String, String>();
        map10.put("bankName", "广发银行");
        map10.put("bankNameBreif", "上海银行");
        banks.add(map10);
        Map<String, String> map11 = new HashMap<String, String>();
        map11.put("bankName", "北京银行");
        map11.put("bankNameBreif", "");
        banks.add(map11);
        HXBankListAdapter adapter = new HXBankListAdapter(this, banks);
        mListView.setAdapter(adapter);

    }

    private void initAllLinesPopWindow() {
        // 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
        View contentView = inflater
                .inflate(R.layout.hxall_lines_pop_layout, null);
        mLinesPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        mLinesPopWindow.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体可点击
        // mPopWindow.setFocusable(true);
        // mPopWindow.setOutsideTouchable(true);
        // 刷新状态
        mLinesPopWindow.update();
        ListView mListView = (ListView) contentView
                .findViewById(R.id.basic_all_lines_listview);
        final String[] titles = new String[]{"个人征信授权书", "信息使用授权书", "芝麻信用服务协议", "取消"};
        HXBasicLinesListAdapter adapter = new HXBasicLinesListAdapter(this, titles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (position == (titles.length - 1)) {
                    if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
                        mLinesPopWindow.dismiss();
                        mLinesPopWindow = null;
                    }
                } else {
//					getLinesURL(titles,position);
                    if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
                        mLinesPopWindow.dismiss();
                        mLinesPopWindow = null;
                    }
                    Intent intent = new Intent(HXDealBankCardActivity.this, HXUserLineActivity.class);
                    intent.putExtra("title", titles[position]);
                    intent.putExtra("url", getUrl(position));//协议url
                    startActivity(intent);
                }
            }
        });

    }

    private String getUrl(int position) {
        String url = /*Contants.BASE_URL*/"";
        if (position == 0) {
            url = Contants.BASE_URL + "/pages/phonehtml5/letterOfInquiry.html" + "?registerAction=" + openName;//个人征信
        } else if (position == 1) {
            url = Contants.BASE_URL + "/pages/phonehtml5/personalInformation.html";//信息使用
        } else if (position == 2) {
            url = Contants.BASE_URL + "/pages/phonehtml5/sesameService.html";//芝麻信用
        }

        LogUtils.Log("TTTT","协议 === url = " + url);
        return url;
    }

    // 获取协议接口
    private void getLinesURL(final String[] titles, final int position) {


        LoggerUtil.debug("获取协议数据:url---->" + Contants.REQUEST_URL
                + "\nprotocolType--->" + position + "\nchannelNo---->"
                + Contants.CHANNEL_NO + "\nclientToken" + getSharePrefer().getToken());
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LINES_URL);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("protocolType", Integer.toString(position));//协议类型

        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(
                        data, type);
                if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
                    mLinesPopWindow.dismiss();
                    mLinesPopWindow = null;
                }
                String url = resultMap.get("url"); //协议路径
                Intent intent = new Intent(HXDealBankCardActivity.this, HXUserLineActivity.class);
                intent.putExtra("title", titles[position]);
                intent.putExtra("url", Contants.BASE_URL + url);//协议url
                startActivity(intent);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub

            }
        });
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//					new RequestCallBack<String>() {
//	                    @Override
//	                    public void onStart() {
//	                    	// TODO Auto-generated method stub
//	                    	super.onStart();
//	                    }
//						@Override
//						public void onFailure(HttpException arg0, String error) {
//							// TODO Auto-generated method stub
//							LoggerUtil.debug("获取协议数据error-------------->" + error);
//						
//						}
//
//						@Override
//						public void onSuccess(ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug("获取协议数据result---->" + responseInfo.result
//									+ "\nresponseInfo.statusCode ===="
//									+ responseInfo.statusCode);
//							if (responseInfo.statusCode == 200) {
//								Type type = new TypeToken<Map<String, String>>() {
//								}.getType();
//								Gson gson = new Gson();
//								Map<String, String> resultMap = gson.fromJson(
//										responseInfo.result, type);
//								String returnCode = resultMap.get("returnCode");
//								if ("000000".equals(returnCode)) {
//									if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
//										mLinesPopWindow.dismiss();
//										mLinesPopWindow = null;
//									}
//									String url=resultMap.get("url"); //协议路径
//									Intent intent = new Intent(HXDealBankCardActivity.this,HXUserLineActivity.class);
//									intent.putExtra("title",titles[position]);
//									intent.putExtra("url", Contants.BASE_URL+url);//协议url
//									startActivity(intent);
//								} else if("E999985".equals(returnCode))
//								{
//									//登录超时
//									String returnMsg = resultMap.get("returnMsg");// 错误提示
//									Message msg = new Message();
//									msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//									msg.obj = returnMsg;
//									mHandler.handleMessage(msg);
//								}
//							}
//
//						}
//					});
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MenuListActivity2.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
