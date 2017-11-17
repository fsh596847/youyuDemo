package com.zhongan.demo.hxin.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.hxin.activitys.HXBankCardManageActivity;
import com.zhongan.demo.hxin.activitys.HXDealBankCardActivity;
import com.zhongan.demo.hxin.activitys.HXFaceIDCardInfoUploadActivity;
import com.zhongan.demo.hxin.activitys.HXFaceStartActivity;
import com.zhongan.demo.hxin.activitys.HXLoanMoneyFirstActivity;
import com.zhongan.demo.hxin.activitys.HXLoanPayRecordListActivity;
import com.zhongan.demo.hxin.activitys.HXPersonCenterActivity;
import com.zhongan.demo.hxin.activitys.HXResultActivity;
import com.zhongan.demo.hxin.activitys.HXUserLineActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CircleImageView;
import com.zhongan.demo.hxin.view.CustomDialog;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * 我的账户界面
 */
@SuppressLint("NewApi")
public class HXZhangHuFragment extends HXBaseFragment implements OnClickListener {

    private View mBaseView;
    private ScrollView mScrollView;
    private TextView mCanUsedNumTv, mTotalUsedNumTv, mLoanPayRecordTipTv, mLoanMoneyBtn, mManagerBankCardBtn;

    private String canUsedSum = "0";//可借额度
    private String totalUsedSum = "0";//总授信额度
    private boolean isFirstLoanMoney;//第一次借款
    private FrameLayout mPersonCenterFl, mLoanPayRecordFl, mCommonProblemFl, mAboutFl;
    private CircleImageView headImageIv;


    private String userStateInfo = "0";//客户状态
    private String selfName = "", selfIdcard = "";
    private String quotaStatus;//有逾期，冻结资金
    private String quotaFreezeAmt;//冻结资金
    //	private boolean isFirst;//第一次进入
//	private boolean isFirstLoanMoney;//第一次借款
    private String isFace;//00：通过验证 01：验证未通过
    private Bundle loanAmountInfo;//额度信息
    private Dialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.hxfragment_zhanghu_layout, null);
        return mBaseView;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE:
                case Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_FAILURE:
                    mDialog.cancel();
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
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
                case Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS:
                    loanAmountInfo = msg.getData();
                    canUsedSum = loanAmountInfo.getString("amountLoan");//可用额度
                    totalUsedSum = loanAmountInfo.getString("totalUsedSum");//总授信额度
                    isFace = loanAmountInfo.getString("isFace");
                    mCanUsedNumTv.setText(canUsedSum);
                    mTotalUsedNumTv.setText("总授信额度:" + totalUsedSum);
                    selfName = loanAmountInfo.getString("userName");
                    selfIdcard = loanAmountInfo.getString("idCard");
                    userStateInfo = loanAmountInfo.getString("userStateInfo");
                    quotaStatus = loanAmountInfo.getString("quotaStatus");
                    quotaFreezeAmt = loanAmountInfo.getString("quotaFreezeAmt");
                    LoggerUtil.debug("1111111111selfName---->" + selfName);
                    LoggerUtil.debug("1111111111selfIdcard---->" + selfIdcard);
                    LoggerUtil.debug("1111111111userStateInfo---->" + userStateInfo);
                    LoggerUtil.debug("1111111111quotaStatus---->" + quotaStatus);
                    LoggerUtil.debug("1111111111quotaFreezeAmt---->" + quotaFreezeAmt);
//    			canUsedSum="30000.00";
//                isFace="00";
                    sharePrefer.setUserStateInfo(userStateInfo);
//				edit.putString("userStateInfo", userStateInfo);
//				edit.commit();
                    if ("ced".equals(userStateInfo) || "loaned".equals(userStateInfo) || "face++".equals(userStateInfo)) {
                        //已授信、 已借款
                        sharePrefer.setCanUsedSum(canUsedSum);
//					edit.putString("canUsedSum", canUsedSum);
//					edit.commit();
                    }
//				if(canUsedSum.equals("0")||canUsedSum.equals("0.00"))
//				{
////					mLoanBtn.setVisibility(View.VISIBLE);
////					mLoanPayMoneyLL.setVisibility(View.GONE);
//					mLoanMoneyBtn.setClickable(false);
//				}else{
//					edit.putString("canUsedSum", canUsedSum);
////					mLoanBtn.setVisibility(View.GONE);
////					mLoanPayMoneyLL.setVisibility(View.VISIBLE);
//					mLoanMoneyBtn.setClickable(true);
//				}
                    queryLoanPayFlag();
                    break;
                case Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_SUCCESS:
                    String code = (String) msg.obj;
                    if ("00".equals(code)) {
                        mLoanPayRecordTipTv.setVisibility(View.GONE);
                    } else if ("01".equals(code)) {
                        mLoanPayRecordTipTv.setVisibility(View.VISIBLE);
                    }
                    mDialog.cancel();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initviews();
        mDialog = Util.createLoadingDialog(getActivity(), "数据加载中,请稍等...");
        //调用额度接口
        //queryLoanAmountInfo();


    }
/*    @Override
    public void onHiddenChanged(boolean hidden) 
    {
    	// TODO Auto-generated method stub
    	super.onHiddenChanged(hidden);
    	if (hidden==false) 
    	{	queryLoanAmountInfo();
		}
    }*/


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        queryLoanAmountInfo();
    }

    private void initviews() {
        mScrollView = (ScrollView) mBaseView.findViewById(R.id.zhanghu_sv);
        mCanUsedNumTv = (TextView) mBaseView.findViewById(R.id.zhanghu_can_used_num);// 可借额度
        mCanUsedNumTv.setText(canUsedSum);
        mTotalUsedNumTv = (TextView) mBaseView.findViewById(R.id.zhanghu_total_used_num);//总授信额度
        mLoanMoneyBtn = (TextView) mBaseView.findViewById(R.id.zhanghu_loan_money_btn);// 我要借款
        mLoanMoneyBtn.setOnClickListener(this);
        mManagerBankCardBtn = (TextView) mBaseView.findViewById(R.id.zhanghu_manager_bank_card_btn);//银行卡管理
        mManagerBankCardBtn.setOnClickListener(this);
        mPersonCenterFl = (FrameLayout) mBaseView.findViewById(R.id.person_center);//个人中心
        mPersonCenterFl.setOnClickListener(this);
        headImageIv = (CircleImageView) mBaseView.findViewById(R.id.person_img_iv);//用户头像
        mLoanPayRecordFl = (FrameLayout) mBaseView.findViewById(R.id.loan_pay_record_fl);//借还款记录
        mLoanPayRecordFl.setOnClickListener(this);
        mLoanPayRecordTipTv = (TextView) mBaseView.findViewById(R.id.loan_pay_record_tip_tv);//已逾期，需还款提示
        mLoanPayRecordTipTv.setVisibility(View.GONE);
        mCommonProblemFl = (FrameLayout) mBaseView.findViewById(R.id.common_problem_fl);//常见问题
        mCommonProblemFl.setOnClickListener(this);
        mAboutFl = (FrameLayout) mBaseView.findViewById(R.id.abount_fl);//关于
        mAboutFl.setOnClickListener(this);
        mScrollView.post(new Runnable() {

            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });

    }

    /**
     * 贷款额度查询
     **/
    private void queryLoanAmountInfo() {

        LoggerUtil.debug("基本信息提交数据:url---->" + Contants.REQUEST_URL

                + "\ntransCode-->" + Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO
                + "\nchannelNo---->"
                + Contants.CHANNEL_NO + "\nclientToken--------->" + sharePrefer.getToken()
                + "\ncustMobile-------->" + sharePrefer.getPhone());
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", sharePrefer.getToken());
        params.addBodyParameter("legalPerNum", "00006");// 法人编号

        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String amountLoan = resultMap.get("amountLoan");//可贷款金额
                String quota_max = resultMap.get("quota_max");//总授信额度
                String userStateInfo = resultMap.get("userStateInfo");//客户状态
                String isFace = resultMap.get("isFace");//人脸识别验证结果   00：通过验证 01：验证未通过
                String userName = resultMap.get("userName");//客户姓名
                String idCard = resultMap.get("idCard");//客户身份证
                String quotaStatus = resultMap.get("quotaStatus");//quotaStatus： 0 是有逾期，有冻结
                String quotaFreezeAmt = resultMap.get("quotaFreezeAmt");//冻结的金额
                sharePrefer.setIdCardNum(idCard);//保存身份证
                Bundle bundle = new Bundle();
                bundle.putString("amountLoan", amountLoan);//可用余额
                bundle.putString("totalUsedSum", quota_max);//总授信额度
                bundle.putString("isFace", isFace);//人脸识别验证结果
                bundle.putString("userStateInfo", userStateInfo);//客户状态
                bundle.putString("userName", userName);//客户姓名
                bundle.putString("idCard", idCard);//客户身份证
                bundle.putString("quotaStatus", quotaStatus);//quotaStatus： 0 是有逾期，有冻结
                bundle.putString("quotaFreezeAmt", quotaFreezeAmt);//冻结的金额
                Message msg = new Message();
                msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
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
                if (mDialog != null) {
                    mDialog.cancel();
                }
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.cancel();
                }
                if ("E10030101".equals(returnCode) || "E00015301".equals(returnCode) || "E10030101".equals(returnCode) || "E10060301".equals(returnCode)) {
                    //限额信息不存在、用户不存在、利率参数不存在
                    Bundle bundle = new Bundle();
                    bundle.putString("amountLoan", "0");//可用余额
                    bundle.putString("isFace", "01");//人脸识别验证结果
                    bundle.putString("userStateInfo", "0");//客户状态
                    bundle.putString("userName", "");//客户姓名
                    bundle.putString("idCard", "");//客户身份证
                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
                    errorMsg.setData(bundle);
                    mHandler.handleMessage(errorMsg);

                } else if (!"E999985".equals(returnCode)) {

                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE;
                    errorMsg.obj = msg;
                    mHandler.handleMessage(errorMsg);

                }
            }
        });
    }

    /**
     * 借还款标志查询
     **/
    private void queryLoanPayFlag() {
        LoggerUtil.debug("借还款标志查询:url---->" + Contants.REQUEST_URL

                + "\ntransCode-->" + Contants.TRANS_CODE_QUERY_LOAN_PAY_MONEY_FLAG
                + "\nchannelNo---->" + Contants.CHANNEL_NO
                + "\nclientToken--------->" + sharePrefer.getToken()
                + "\ncustMobile-------->" + sharePrefer.getPhone());
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode",
                Contants.TRANS_CODE_QUERY_LOAN_PAY_MONEY_FLAG);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", sharePrefer.getToken());
        params.addBodyParameter("legalPerNum", "00006");// 法人编号

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
                Map<String, String> resultMap = gson.fromJson(data, type);
                String code = resultMap.get("code");// 查询标识 00表示无逾期 01表示有逾期
                Message msg = new Message();
                msg.what = Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_SUCCESS;
                msg.obj = code;
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
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.cancel();
                }
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.cancel();
                }
                Message errorMsg = new Message();
                errorMsg.what = Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_FAILURE;
                errorMsg.obj = msg;
                mHandler.handleMessage(errorMsg);
            }
        });
    }

    	/**
	 * 贷款额度查询
	 **/
//	private void queryLoanAmountInfo() {
//		LoggerUtil.debug("基本信息提交数据:url---->" + Contants.REQUEST_URL
//				 
//				+ "\ntransCode-->" + Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO
//				+ "\nchannelNo---->"
//				+ Contants.CHANNEL_NO+"\nclientToken--------->"+sharePrefer.getToken()
//				+"\ncustMobile-------->"+sharePrefer.getPhone());
//		RequestParams params = new RequestParams("utf-8");
//		params.addHeader("Content-Type","application/x-www-form-urlencoded");
//		params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO);
//		params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
//		params.addBodyParameter("clientToken",sharePrefer.getToken());
////		params.addBodyParameter("custNum", custNum);// 客户编号
////		HttpUtils dataHttp = new HttpUtils("60 * 1000");
//		//"accountamt":"1000000.00",	"accountAvailable":1000000.00,	"accountFrozen":0.00,	"des":"",	"acctId":"6000078001"
//		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
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
//						LoggerUtil.debug("查询额度接口error-------------->" + error);
////						String returnMsg = resultMap.get("returnMsg");// 错误提示
//						Message msg = new Message();
//						msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("查询额度接口接口result---->" + responseInfo.result
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
//								
//								String sumLoanAmt=resultMap.get("sumLoanAmt");//贷款总金额
//								String sumProductNum=resultMap.get("sumProductNum");//贷款总产品数
//								String sumRepayedAmt=resultMap.get("sumRepayedAmt");//已还总金额
//								String sumRepayedPricipal=resultMap.get("sumRepayedPricipal");//已还总本金
//								String sumRepayedInterest=resultMap.get("sumRepayedInterest");//已还总利息
//								String sumRepayedOverdueInt=resultMap.get("sumRepayedOverdueInt");//已还总逾期利息
//								String sumNotPayAmt=resultMap.get("sumNotPayAmt");//未还总金额
//								String sumNotPayPricipal=resultMap.get("sumNotPayPricipal");//未还总本金
//								String sumNotPayInterest=resultMap.get("sumNotPayInterest");//未还总利息
//							
//								String sumNotPayOverdueInt=resultMap.get("sumNotPayOverdueInt");//未还总逾期利息
//								String sumOverduePricipal=resultMap.get("sumOverduePricipal");//逾期总本金
//								String sumOverdueInterest=resultMap.get("sumOverdueInterest");//逾期总利息
//								String amountLoan=resultMap.get("amountLoan");//可贷款金额
//								String quota_max=resultMap.get("quota_max");//总授信额度
//								String userStateInfo=resultMap.get("userStateInfo");//客户状态
//								String isFace=resultMap.get("isFace");//人脸识别验证结果   00：通过验证 01：验证未通过
//								String userName=resultMap.get("userName");//客户姓名
//								String idCard=resultMap.get("idCard");//客户身份证
//								
//								sharePrefer.setIdCardNum(idCard);//保存身份证
//								
//								Bundle bundle=new Bundle();
//								bundle.putString("amountLoan", amountLoan);//可用余额
//								bundle.putString("totalUsedSum",quota_max);//总授信额度
//								bundle.putString("isFace", isFace);//人脸识别验证结果	
//								bundle.putString("userStateInfo", userStateInfo);//客户状态
//								bundle.putString("userName", userName);//客户姓名
//								bundle.putString("idCard",idCard);//客户身份证
//							
//												
//								Message msg = new Message();
//								msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
////								msg.obj=accountFrozen;
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//							
//							} else if("E10030101".equals(returnCode)||"E00015301".equals(returnCode)||"E10030101".equals(returnCode)||"E10060301".equals(returnCode))
//							{
//								//限额信息不存在、用户不存在、利率参数不存在
//								Bundle bundle=new Bundle();
//								bundle.putString("amountLoan", "0");//可用余额
//								bundle.putString("totalUsedSum", "0");//总授信额度
//								bundle.putString("isFace", "01");//人脸识别验证结果	
//								bundle.putString("userStateInfo","0");//客户状态
//								bundle.putString("userName", "");//客户姓名
//								bundle.putString("idCard","");//客户身份证
//							
//								Message msg = new Message();
//								msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
////								msg.obj=accountFrozen;
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//							}
//							else
//							{
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//								
//							}
//
//						}
//
//					}
//				});
//	}
//	/**
//	 * 借还款标志查询
//	 **/
//	private void queryLoanPayFlag() {
//		LoggerUtil.debug("借还款标志查询:url---->" + Contants.REQUEST_URL
//				 
//				+ "\ntransCode-->" + Contants.TRANS_CODE_QUERY_LOAN_PAY_MONEY_FLAG
//				+ "\nchannelNo---->"
//				+ Contants.CHANNEL_NO+"\nclientToken--------->"+sharePrefer.getToken()
//				+"\ncustMobile-------->"+sharePrefer.getPhone());
//		RequestParams params = new RequestParams("utf-8");
//		params.addHeader("Content-Type","application/x-www-form-urlencoded");
//		params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LOAN_PAY_MONEY_FLAG);
//		params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
//		params.addBodyParameter("clientToken",sharePrefer.getToken());
////		params.addBodyParameter("custNum", custNum);// 客户编号
////		HttpUtils dataHttp = new HttpUtils("60 * 1000");
//		//"accountamt":"1000000.00",	"accountAvailable":1000000.00,	"accountFrozen":0.00,	"des":"",	"acctId":"6000078001"
//		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("借还款标志查询error-------------->" + error);
////						String returnMsg = resultMap.get("returnMsg");// 错误提示
//						Message msg = new Message();
//						msg.what = Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("借还款标志查询result---->" + responseInfo.result
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
//								
//								String code=resultMap.get("code");//查询标识 00表示无逾期 01表示有逾期
//							   		
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_SUCCESS;
//								msg.obj=code;
//								mHandler.handleMessage(msg);
//							
//							} 
//							else
//							{
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//								
//							}
//
//						}
//
//					}
//				});
//	}
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.zhanghu_loan_money_btn:
                // 我要借款
//			isFirstLoanMoney = sharePrefer.getBoolean("isFirstLoanMoney", true);
//			if (isFirstLoanMoney) 
//			{
                if ("ced".equals(userStateInfo) || "loaned".equals(userStateInfo) || "face++".equals(userStateInfo)) {
                    //已授信、 已借款、做过face++
                    if ("01".equals(isFace)) {
                        LoggerUtil.debug("人脸识别验证未通过");
                        //第一次借款,进入人脸识别界面
                        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());

                        builder.setMessage(R.string.loan_money_first_tip_text);
                        builder.setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        // 设置你的操作事项
                                        Intent loanMoneyIntent = new Intent(getActivity(), HXFaceStartActivity.class);
                                        getActivity().startActivity(loanMoneyIntent);
                                        sharePrefer.setIsFirstLoanMoney(false);
//									edit.putBoolean("isFirstLoanMoney", false);
//									edit.commit();
                                    }
                                });

                        builder.create().show();

                    } else if ("00".equals(isFace)) {
                        if ("face++".equals(userStateInfo) || "loaned".equals(userStateInfo)) {
                            LoggerUtil.debug("人脸识别验证已通过");
                            if ("0".equals(quotaStatus)) {
                                Toast.makeText(getActivity(), "您有逾期，已冻结可用额度:" + quotaFreezeAmt, Toast.LENGTH_SHORT).show();
                            } else {

                                Intent loanMoneyIntent = new Intent(getActivity(), HXLoanMoneyFirstActivity.class);
                                getActivity().startActivity(loanMoneyIntent);
                            }

                        }
                    }
                } else {
                    if ("aced".equals(userStateInfo)) {
                        //授信申请中
                        Toast.makeText(getActivity(), "授信申请中，请稍后...", 1).show();
                    } else if ("cedbad".equals(userStateInfo)) {
                        //授信申请被拒绝
                        Toast.makeText(getActivity(), "授信申请被拒绝，请重新申请...", 1).show();
                        Intent uploadIntent = new Intent(getActivity(), HXFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("0".equals(userStateInfo) || "registered".equals(userStateInfo) || "realfied".equals(userStateInfo)) {
                        //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
                        Intent uploadIntent = new Intent(getActivity(), HXFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("saveinfo".equals(userStateInfo)) {
                        //完成完善客户信息 跳转银行卡验证页面
                        Intent intent = new Intent(getActivity(), HXDealBankCardActivity.class);
                        intent.putExtra("selfName", selfName);
                        intent.putExtra("selfIdcard", selfIdcard);
                        intent.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
                        startActivity(intent);
                    } else if ("verifi4".equals(userStateInfo)) {
                        //完成银行卡验证 跳转结果页面
                        Intent submitIntent = new Intent(getActivity(), HXResultActivity.class);
                        startActivity(submitIntent);
                    }
//				else if("verifi4".equals(userStateInfo))
//				{
//					//完成银行卡验证 跳转设置密码页面
//					Intent submitIntent = new Intent(getActivity(),HXSetBankCardPwdActivity.class);
//					startActivity(submitIntent);
//				}
                }

                break;
            case R.id.zhanghu_manager_bank_card_btn:
                if ("ced".equals(userStateInfo) || "loaned".equals(userStateInfo) || "face++".equals(userStateInfo)) {
                    //已授信、 已借款、做过face++
                    if ("01".equals(isFace)) {
                        LoggerUtil.debug("人脸识别验证未通过");
                        // 银行卡管理
                        Intent bankIntent = new Intent(getActivity(), HXBankCardManageActivity.class);
                        getActivity().startActivity(bankIntent);
//					//第一次借款,进入人脸识别界面
//					CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
//					
//					builder.setMessage(R.string.loan_money_first_tip_text);
//					builder.setPositiveButton(R.string.ok,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									dialog.dismiss();
//									// 设置你的操作事项
//									Intent loanMoneyIntent=new Intent(getActivity(),HXFaceStartActivity.class);
//									getActivity().startActivity(loanMoneyIntent);
//									sharePrefer.setIsFirstLoanMoney(false);
////									edit.putBoolean("isFirstLoanMoney", false);
////									edit.commit();
//								}
//							});
//				
//					builder.create().show();

                    } else if ("00".equals(isFace)) {
                        if ("face++".equals(userStateInfo) || "loaned".equals(userStateInfo)) {
                            LoggerUtil.debug("人脸识别验证已通过");
//						if("0".equals(quotaStatus))
//						{
//							Toast.makeText(getActivity(),"您有逾期，已冻结可用额度:"+quotaFreezeAmt,Toast.LENGTH_SHORT).show();
//						}else
//						{

                            // 银行卡管理
                            Intent bankIntent = new Intent(getActivity(), HXBankCardManageActivity.class);
                            getActivity().startActivity(bankIntent);
//						}

                        }
                    }
                } else {
                    if ("aced".equals(userStateInfo)) {
                        //授信申请中
                        Toast.makeText(getActivity(), "授信申请中，请稍后...", 1).show();
                    } else if ("cedbad".equals(userStateInfo)) {
                        //授信申请被拒绝
                        Toast.makeText(getActivity(), "授信申请被拒绝，请重新申请...", 1).show();
                        Intent uploadIntent = new Intent(getActivity(), HXFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("0".equals(userStateInfo) || "registered".equals(userStateInfo) || "realfied".equals(userStateInfo)) {
                        //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
                        Intent uploadIntent = new Intent(getActivity(), HXFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("saveinfo".equals(userStateInfo)) {
                        //完成完善客户信息 跳转银行卡验证页面
                        Intent intent = new Intent(getActivity(), HXDealBankCardActivity.class);
                        intent.putExtra("selfName", selfName);
                        intent.putExtra("selfIdcard", selfIdcard);
                        intent.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
                        startActivity(intent);
                    } else if ("verifi4".equals(userStateInfo)) {
                        //完成银行卡验证 跳转银行卡管理
                        Intent bankIntent = new Intent(getActivity(), HXBankCardManageActivity.class);
                        getActivity().startActivity(bankIntent);
                    }
//				else if("verifi4".equals(userStateInfo))
//				{
//					//完成银行卡验证 跳转设置密码页面
//					Intent submitIntent = new Intent(getActivity(),HXSetBankCardPwdActivity.class);
//					startActivity(submitIntent);
//				}
                }

                break;
            case R.id.person_center:
                // 个人中心
                Intent personIntent = new Intent(getActivity(), HXPersonCenterActivity.class);
                personIntent.putExtra("userStateInfo", userStateInfo);
                getActivity().startActivity(personIntent);
                break;
            case R.id.loan_pay_record_fl:
                // 借还款记录
                Intent recordIntent = new Intent(getActivity(), HXLoanPayRecordListActivity.class);
                getActivity().startActivity(recordIntent);

                break;
            case R.id.common_problem_fl:
                // 常见问题
//			Intent problemIntent=new Intent(getActivity(),HXCommonProblemActivity.class);
//			getActivity().startActivity(problemIntent);
                String commonURL = Contants.BASE_URL + "/pages/FAQ/FAQ.html";
                Intent problemIntent = new Intent(getActivity(), HXUserLineActivity.class);
                problemIntent.putExtra("title", "常见问题");
                problemIntent.putExtra("url", commonURL);
                startActivity(problemIntent);
                break;
            case R.id.abount_fl:
                // 关于
                String aboutURL = Contants.BASE_URL + "/pages/about/about.html";
//			Intent aboutIntent=new Intent(getActivity(),AboutActivity.class);
//			getActivity().startActivity(aboutIntent);
                Intent aboutIntent = new Intent(getActivity(), HXUserLineActivity.class);
                aboutIntent.putExtra("title", "关于融数");
                aboutIntent.putExtra("url", aboutURL);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }
    }
}
