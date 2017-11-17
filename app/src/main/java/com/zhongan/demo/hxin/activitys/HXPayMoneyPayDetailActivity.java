package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.wknight.keyboard.WKnightKeyboard;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.bean.HXBankListBean;
import com.zhongan.demo.hxin.bean.HXBankListItemBean;
import com.zhongan.demo.hxin.bean.HXLoanMoneyDetailListItemBean;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.JYPEditText;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 全额、分期还款详情界面
 */
public class HXPayMoneyPayDetailActivity extends HXBaseActivity implements
        OnClickListener {

    private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
    private Button mBackBtn, mSubmitBtn;
    private TextView mTitleView, mPayBenjinTv, mPayLixiTv, mPayFaxiTv,
            mPayShouxufeiTv, mPayTotalSumTv, mPayTypeTv, mWeiyuejinTv;
    private PopupWindow mZhiwenPwdPopWindow;
    private PopupWindow mPwdPopWindow;
    private View line1, line2;
    private LinearLayout mPayFaxiLl, mPayWeiyuejinLl;
    private String payType = "";
    private String payState = "";
    private String loanMonths = "";//贷款期限
    private String repayingPlanDetailId = "";//还款计划明细ID
    private String loanId = "";//贷款Id
    private String actualRepayAmt = "";//还款金额
    private String currentFee = "";//手续费
    private HXLoanMoneyDetailListItemBean loanMoneyMonthDetail;
    private Bundle loanMoneyBundle;
    private String perRepayDate = "";//还款日期
    private String penalty = "";//违约金
    private Dialog mDialog;
    /**
     * 用Handler来更新UI
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_SUCCESS:
                case Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_SUCCESS:
                case Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_SUCCESS:
                    mDialog.cancel();
                    mSubmitBtn.setClickable(true);
                    mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    Intent intent = new Intent(HXPayMoneyPayDetailActivity.this, HXPayMoneyResultActivity.class);
                    intent.putExtra("flag", "true");
                    intent.putExtra("payType", payType);
                    intent.putExtra("loanId", loanId);
                    startActivity(intent);
                    HXPayMoneyPayDetailActivity.this.finish();

                    break;
                case Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_FAILURE:
                case Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_FAILURE:
                case Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_FAILURE:
                    mDialog.cancel();
                    String retrunMsg = (String) msg.obj;
                    mSubmitBtn.setClickable(true);
                    mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    Intent fauilerIntent = new Intent(HXPayMoneyPayDetailActivity.this, HXPayMoneyResultActivity.class);
                    fauilerIntent.putExtra("flag", "false");
                    fauilerIntent.putExtra("payType", payType);
                    fauilerIntent.putExtra("loanId", loanId);
                    fauilerIntent.putExtra("retrunMsg", retrunMsg);
                    startActivity(fauilerIntent);
                    HXPayMoneyPayDetailActivity.this.finish();
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
        setContentView(R.layout.hxactivity_pay_money_by_qishu_layout);
        loanMoneyBundle = getIntent().getBundleExtra("loanMoneyBundle");
        payType = loanMoneyBundle.getString("payType");
        loanId = loanMoneyBundle.getString("loanId");
        perRepayDate = Util.getCurrentDate();
        mDialog = Util.createLoadingDialog(this, "数据加载中,请稍等...");
        initViews();
        getBankList();

    }

    //查询卡列表接口
    private void getBankList() {
        LoggerUtil.debug("卡列表查询: clientToken--->" + getSharePrefer().getToken());

        RequestParams params = new RequestParams("utf-8");
        params.addQueryStringParameter("transCode", Contants.TRANS_CODE_QUERY_BANK_LIST_INFO);
        params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Gson gson = new Gson();
                HXBankListBean bankListData = gson.fromJson(data, HXBankListBean.class);//还款计划数据
                List<HXBankListItemBean> bankList = bankListData.getBankList();
                LoggerUtil.debug("卡列表---->" + bankList);
                String bankNoStr = bankList.get(0).getBankCode();
                String bankNo = bankNoStr.substring(bankNoStr.length() - 4, bankNoStr.length());
                mPayTypeTv.setText(bankList.get(0).getBankName() + "  储蓄卡   " + bankNo);
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
//  		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//  		 new RequestCallBack<String>() {
//
//  						@Override
//  						public void onFailure(HttpException arg0,
//  								String error) {
//  							// TODO Auto-generated method stub
//  							LoggerUtil.debug("卡列表查询 error-------------->" + error);
////  							Message msg = new Message();
////  							msg.what = Contants.MSG_DO_QUERY_BANK_LIST_INFO_FAILURE;
////  							msg.obj="网络问题";
////  							mHandler.handleMessage(msg);
//  						}
//
//  						@Override
//  						public void onSuccess(
//  								ResponseInfo<String> responseInfo) {
//  							LoggerUtil.debug( "卡列表查询：result---->"
//  									+ responseInfo.result
//  									+ "\nresponseInfo.statusCode ===="
//  									+ responseInfo.statusCode);
//  							if (responseInfo.statusCode == 200) {
//				
//  								Gson gson = new Gson();
//  								HXBankListBean bankListData= gson.fromJson(responseInfo.result, HXBankListBean.class);//还款计划数据
//                              
//								String returnCode = bankListData.getReturnCode();//返回交易吗
//  								if ("000000".equals(returnCode)) 
//  								{
//  									List<HXBankListItemBean> bankList=bankListData.getBankList();
//  									LoggerUtil.debug("卡列表---->"+bankList);
//  									String bankNoStr=bankList.get(0).getBankCode();
//  									String bankNo=bankNoStr.substring( bankNoStr.length()-4, bankNoStr.length());
//  									mPayTypeTv.setText(bankList.get(0).getBankName()+"  储蓄卡   "+bankNo);
//  									
//  								} else 
//  								{
//  									String returnMsg = bankListData.getReturnMsg();// 交易提示
//  									LoggerUtil.debug("查询卡列表失败---->"+returnMsg);
//  									Message msg=new Message();
//  									msg.obj=returnMsg;
//  									msg.what=Contants.MSG_DO_QUERY_BANK_LIST_INFO_FAILURE;
//  									mHandler.sendMessage(msg);
//  									
//  								}
//
//  							}
//
//  						}
//  					});
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
        mPayBenjinTv = (TextView) findViewById(R.id.pay_money_pay_sum_tv);// 还款本金
        mPayLixiTv = (TextView) findViewById(R.id.pay_money_pay_lixi_num_tv);// 利息
        mPayFaxiTv = (TextView) findViewById(R.id.pay_money_pay_faxi_tv);// 逾期罚息
        mPayShouxufeiTv = (TextView) findViewById(R.id.pay_money_pay_shouxufei_tv);// 手续费
        mPayTotalSumTv = (TextView) findViewById(R.id.pay_money_pay_total_sum_tv);// 还款总额
        mPayTypeTv = (TextView) findViewById(R.id.pay_money_yinhang_cardid_tv);// 还款方式
        mPayTypeTv.setOnClickListener(this);
        mPayFaxiLl = (LinearLayout) findViewById(R.id.pay_money_pay_faxi_ll);
        line1 = findViewById(R.id.pay_money_pay_faxi_line);
        mPayWeiyuejinLl = (LinearLayout) findViewById(R.id.pay_money_pay_weiyuejin_ll);
        line2 = findViewById(R.id.pay_money_pay_weiyuejin_line);
        mWeiyuejinTv = (TextView) findViewById(R.id.pay_money_pay_weiyuejin_tv);
        mSubmitBtn = (Button) findViewById(R.id.pay_money_ok_pay_btn);
        mSubmitBtn.setOnClickListener(this);

        if (payType.equals("all")) {
            mTitleView.setText(R.string.pay_money_all_tip_text);
            mPayWeiyuejinLl.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            String repayPricipal = loanMoneyBundle.getString("repayPricipal");
            String repayInerest = loanMoneyBundle.getString("repayInerest");
            String overdueInt = loanMoneyBundle.getString("overdueInt");
            String totalAmt = loanMoneyBundle.getString("totalAmt");
            String repayFee = loanMoneyBundle.getString("repayFee");
            penalty = loanMoneyBundle.getString("penalty");
            mWeiyuejinTv.setText(penalty);
            mPayBenjinTv.setText(repayPricipal);
            mPayLixiTv.setText(repayInerest);
            mPayFaxiTv.setText(overdueInt);
            mPayShouxufeiTv.setText(repayFee);
            mPayTotalSumTv.setText(totalAmt);
        } else {

            mPayWeiyuejinLl.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            loanMoneyMonthDetail = (HXLoanMoneyDetailListItemBean) loanMoneyBundle.getSerializable("loanMoneyMonthDetail");
            loanMonths = loanMoneyBundle.getString("loanMonths");
            String period = loanMoneyMonthDetail.getCurrentPeriod();
            mTitleView.setText(period + "/" + loanMonths + "期还款");
            payState = loanMoneyMonthDetail.getStatus();
            repayingPlanDetailId = loanMoneyMonthDetail.getRepayingPlanDetailId();//还款计划明细ID
            actualRepayAmt = loanMoneyMonthDetail.getCurrentPrincipalInterest();
            currentFee = loanMoneyMonthDetail.getCurrentFee();
            if (payState.equals("0") || payState.equals("3")) {
                mPayBenjinTv.setText(loanMoneyMonthDetail.getCurrentPrincipal());
                mPayLixiTv.setText(loanMoneyMonthDetail.getCurrentInterest());
                mPayFaxiTv.setText(loanMoneyMonthDetail.getOverInt());
                mPayShouxufeiTv.setText(currentFee);
                mPayTotalSumTv.setText(loanMoneyMonthDetail.getCurrentPrincipalInterest());
            }
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_btn:
                // 返回
                HXPayMoneyPayDetailActivity.this.finish();
                break;
            case R.id.pay_money_ok_pay_btn:
                // 确认还款
//          指纹识别弹出
//			initZhiwenPasswordPopWindow();
//			mZhiwenPwdPopWindow.showAtLocation(view, 0, 0, 0);
                initPasswordPopWindow();
                mPwdPopWindow.showAtLocation(view, 0, 0, 0);
                break;
            case R.id.pay_money_yinhang_cardid_tv:
                //还款方式
                break;
            default:
                break;
        }
    }


    //提前还款申请
    private void submitAheadPayMoney(String loanId, String prepaymentType, String perRepayDate, String penalty, String payPassword) {

        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
        LoggerUtil.debug("提前还款申请: 贷款id-->" + loanId + "\n提前还款日期--->" + perRepayDate + "\n提前还款类型" + prepaymentType);
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_SUBMIT_AHEAD_PAY_MONEY);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("prepaymentType", prepaymentType);//提前还款类型  1:全额还款
        params.addBodyParameter("loanId", loanId);//贷款id
        params.addBodyParameter("perRepayDate", perRepayDate);//提前还款日期
        params.addBodyParameter("penalty", penalty);//违约金
        params.addBodyParameter("perRepaymentReasons", "1");//违约金
        params.addBodyParameter("payPassword", payPassword);//支付密码
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub

                Message msg = new Message();
                msg.what = Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_SUCCESS;
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
            public void onError(String returnCode, String errorMsg) {
                // TODO Auto-generated method stub
                mDialog.cancel();
                Message msg = new Message();
                msg.what = Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_FAILURE;
                msg.obj = errorMsg;
                mHandler.handleMessage(msg);
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
//					public void onFailure(HttpException arg0, String arg1) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.what = Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_FAILURE;
//						msg.obj="网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("提前还款申请result---->" + responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode==200) {	
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) 
//							{				
//							
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_SUCCESS;
//								mHandler.handleMessage(msg);
//							}else
//							{
//								String errorMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_SUBMIT_AHEAD_PAY_MONEY_FAILURE;
//								msg.obj=errorMsg;
//								mHandler.handleMessage(msg);
//							}
//						}
//					}
//				});
    }

    //正常还款提交
    private void submitCommonLoanMoneyData(String loanId, String repayingLoanDetailId, String actualRepayAmt, String perRepayDate, String payPassword) {

        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
        LoggerUtil.debug("正常还款试算: 还款计划明细ID-->" + repayingLoanDetailId + "\n贷款ID--->" + loanId + "\n实际还款日期---->" + perRepayDate);
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_SUBMIT_COMMON_PAY_MONEY);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("loanId", loanId);//贷款ID
        params.addBodyParameter("repayingLoanDetailId", repayingLoanDetailId);//还款计划明细ID
        params.addBodyParameter("actualRepayAmt", actualRepayAmt);
        params.addBodyParameter("actualRepayDate", perRepayDate);//实还日期
        params.addBodyParameter("fundsSource", "1");//资金来源
        params.addBodyParameter("note", "");//备注
        params.addBodyParameter("interestReliefAmt", "0");//利息减免
        params.addBodyParameter("feeReliefAmt", "0");//费用减免
        params.addBodyParameter("payPassword", payPassword);//支付密码
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String repayPricipal = resultMap.get("repayPricipal");//本金金额
                String repayInerest = resultMap.get("repayInerest");//利息金额
                String overdueInt = resultMap.get("overdueInt");//逾期利息金额
                String totalAmt = resultMap.get("totalAmt");//实还金额
                String repayFee = resultMap.get("repayFee");//费用金额
                Message msg = new Message();
                msg.what = Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("repayPricipal", repayPricipal);
                bundle.putString("repayInerest", repayInerest);
                bundle.putString("overdueInt", overdueInt);
                bundle.putString("totalAmt", totalAmt);
                bundle.putString("repayFee", repayFee);
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
            public void onError(String returnCode, String errorMsg) {
                // TODO Auto-generated method stub
                mDialog.cancel();
                Message msg = new Message();
                msg.what = Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_FAILURE;
                msg.obj = errorMsg;
                mHandler.handleMessage(msg);
            }
        });
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//                   @Override
//                public void onStart() {
//                	// TODO Auto-generated method stub
//                	super.onStart();
//                	mDialog.show();
//                }
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.what = Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_FAILURE;
//						msg.obj="网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("正常还款试算result---->" + responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode==200) {	
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) 
//							{				
//								String repayPricipal=resultMap.get("repayPricipal");//本金金额
//								String repayInerest=resultMap.get("repayInerest");//利息金额
//								String overdueInt=resultMap.get("overdueInt");//逾期利息金额
//								String totalAmt=resultMap.get("totalAmt");//实还金额
//								String repayFee=resultMap.get("repayFee");//费用金额
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_SUCCESS;
//								Bundle bundle = new Bundle();
//								bundle.putString("repayPricipal", repayPricipal);
//								bundle.putString("repayInerest", repayInerest);
//								bundle.putString("overdueInt", overdueInt);
//								bundle.putString("totalAmt", totalAmt);
//								bundle.putString("repayFee", repayFee);
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//							}else
//							{
//								String errorMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_SUBMIT_COMMON_PAY_MONEY_FAILURE;
//								msg.obj=errorMsg;
//								mHandler.handleMessage(msg);
//							}
//						}
//					}
//				});
    }

    //逾期还款提交
    private void submitDelayLoanMoneyData(String loanId, String repayingLoanDetailId, String actualRepayAmt, String payPassword) {

        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
        LoggerUtil.debug("逾期还款提交: 还款计划明细ID-->" + repayingLoanDetailId + "\n贷款ID--->" + loanId + "\n实际还款日期---->" + perRepayDate);
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_SUBMIT_DELAY_PAY_MONEY);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("loanId", loanId);//贷款ID
        params.addBodyParameter("repayingDetailId", repayingLoanDetailId);//还款计划明细ID
        params.addBodyParameter("actualRepayAmt", actualRepayAmt);//实际还款金额
        params.addBodyParameter("payPassword", payPassword);//支付密码
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String repayPricipal = resultMap.get("repayPricipal");//本金金额
                String repayInerest = resultMap.get("repayInerest");//利息金额
                String overdueInt = resultMap.get("overdueInt");//逾期利息金额
                String totalAmt = resultMap.get("totalAmt");//实还金额
                String repayFee = resultMap.get("repayFee");//费用金额
                Message msg = new Message();
                msg.what = Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("repayPricipal", repayPricipal);
                bundle.putString("repayInerest", repayInerest);
                bundle.putString("overdueInt", overdueInt);
                bundle.putString("totalAmt", totalAmt);
                bundle.putString("repayFee", repayFee);
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
            public void onError(String returnCode, String errorMsg) {
                // TODO Auto-generated method stub
                mDialog.cancel();
                Message msg = new Message();
                msg.what = Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_FAILURE;
                msg.obj = errorMsg;
                mHandler.handleMessage(msg);
            }
        });
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//                   @Override
//                public void onStart() {
//                	// TODO Auto-generated method stub
//                	super.onStart();
//                	mDialog.show();
//                }
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.what = Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_FAILURE;
//						msg.obj="网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("正常还款试算result---->" + responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode==200) {	
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) 
//							{				
//								String repayPricipal=resultMap.get("repayPricipal");//本金金额
//								String repayInerest=resultMap.get("repayInerest");//利息金额
//								String overdueInt=resultMap.get("overdueInt");//逾期利息金额
//								String totalAmt=resultMap.get("totalAmt");//实还金额
//								String repayFee=resultMap.get("repayFee");//费用金额
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_SUCCESS;
//								Bundle bundle = new Bundle();
//								bundle.putString("repayPricipal", repayPricipal);
//								bundle.putString("repayInerest", repayInerest);
//								bundle.putString("overdueInt", overdueInt);
//								bundle.putString("totalAmt", totalAmt);
//								bundle.putString("repayFee", repayFee);
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//							}else
//							{
//								String errorMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_SUBMIT_DELAY_PAY_MONEY_FAILURE;
//								msg.obj=errorMsg;
//								mHandler.handleMessage(msg);
//							}
//						}
//					}
//				});
    }
//	private void initZhiwenPasswordPopWindow() {
//		// 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
//		LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
//		View contentView = inflater.inflate(
//				R.layout.hxzhiwen_password_pop_layout, null);
//		mZhiwenPwdPopWindow = new PopupWindow(contentView,
//				WindowManager.LayoutParams.MATCH_PARENT,
//				WindowManager.LayoutParams.MATCH_PARENT, false);
//		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0x60000000);
//		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//		mZhiwenPwdPopWindow.setBackgroundDrawable(dw);
//		// 设置SelectPicPopupWindow弹出窗体可点击
//		// mPopWindow.setFocusable(true);
//		// mPopWindow.setOutsideTouchable(true);
//		// 刷新状态
//		mZhiwenPwdPopWindow.update();
//		ImageView zhiwenIv = (ImageView) contentView
//				.findViewById(R.id.zhiwen_iv);
//		Button cancleBtn = (Button) contentView
//				.findViewById(R.id.zhiwen_cancle_btn);
//		cancleBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(final View view) {
//				// TODO Auto-generated method stub
//				if (mZhiwenPwdPopWindow.isShowing()) {
//					mZhiwenPwdPopWindow.dismiss();
//					mZhiwenPwdPopWindow = null;
//				}
//				CustomDialog.Builder builder = new CustomDialog.Builder(
//						HXPayMoneyPayDetailActivity.this);
//				// builder.setIcon(R.drawable.ic_launcher);
//				// builder.setTitle(R.string.title_alert);
//				builder.setMessage(R.string.zhiwen_exit_tip);
//				builder.setPositiveButton(R.string.ok,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//								// 设置你的操作事项
//								Intent intent = new Intent(
//										HXPayMoneyPayDetailActivity.this,
//										HXPayMoneyResultActivity.class);
//								intent.putExtra("flag", "false");
//								startActivity(intent);
//							}
//						});
//				builder.setNegativeButton(R.string.input_pwd_text,
//						new android.content.DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//
//								initPasswordPopWindow();
//								mPwdPopWindow.showAtLocation(view, 0, 0, 0);
//							}
//						});
//				builder.create().show();
//			}
//		});
//	}

    private void initPasswordPopWindow() {
        // 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
        View contentView = inflater.inflate(R.layout.hxpassword_pop_layout, null);
        mPwdPopWindow = new PopupWindow(contentView,
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
        final JYPEditText mPwdEt = (JYPEditText) contentView
                .findViewById(R.id.loan_money_deal_passwordnum_et);
        final WKnightKeyboard keyboard = new WKnightKeyboard(mPwdEt);
        if (mPwdEt != null) {
            keyboard.setRecvTouchEventActivity(HXPayMoneyPayDetailActivity.this);
        }
        mPwdEt.setOnEditTextContentListener(new JYPEditText.EditTextContentListener() {

            @Override
            public void onTextChanged(CharSequence text) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onComplete(CharSequence text) {
                String password = keyboard.getEnctyptText();
                if (payType.equals("all")) {
                    //提前全额还款
                    submitAheadPayMoney(loanId, "1", perRepayDate, penalty, password);
                } else {
                    if (payState.equals("0")) {
                        //正常还款
                        submitCommonLoanMoneyData(loanId, repayingPlanDetailId, actualRepayAmt, perRepayDate, password);

                    } else if (payState.equals("3")) {
                        //逾期还款
                        submitDelayLoanMoneyData(loanId, repayingPlanDetailId, actualRepayAmt, password);
                    }
                }

                mPwdEt.setText("");

                if (mPwdPopWindow.isShowing()) {
                    mPwdPopWindow.dismiss();
                    mPwdPopWindow = null;
                    mPwdEt.setFocusable(false);

                }
            }
        });
    }

}
