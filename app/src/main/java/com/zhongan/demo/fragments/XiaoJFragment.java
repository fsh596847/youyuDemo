package com.zhongan.demo.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
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
import com.zhongan.demo.hxin.XJBaseFragment;
import com.zhongan.demo.hxin.activitys.HXResultActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CircleImageView;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.xjactivitys.XJBankCardManageActivity;
import com.zhongan.demo.hxin.xjactivitys.XJDealBankCardActivity;
import com.zhongan.demo.hxin.xjactivitys.XJFaceIDCardInfoUploadActivity;
import com.zhongan.demo.hxin.xjactivitys.XJFaceStartActivity;
import com.zhongan.demo.hxin.xjactivitys.XJLoanMoneyFirstActivity;
import com.zhongan.demo.hxin.xjactivitys.XJLoanPayRecordListActivity;
import com.zhongan.demo.hxin.xjactivitys.XJPersonCenterActivity;
import com.zhongan.demo.hxin.xjactivitys.XJUserLineActivity;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.Map;

public class XiaoJFragment extends XJBaseFragment implements View.OnClickListener {

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
                    sharePrefer.setUserStateInfo(userStateInfo);
                    if ("ced".equals(userStateInfo) || "loaned".equals(userStateInfo) || "face++".equals(userStateInfo)) {
                        //已授信、 已借款
                        sharePrefer.setCanUsedSum(canUsedSum);
                    }
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


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //调用额度接口
        queryLoanAmountInfo();

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
                sharePrefer.setUserName(userName);
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

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.zhanghu_loan_money_btn:
                // 我要借款
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
                                        Intent loanMoneyIntent = new Intent(getActivity(), XJFaceStartActivity.class);
                                        getActivity().startActivity(loanMoneyIntent);
                                        sharePrefer.setIsFirstLoanMoney(false);
                                    }
                                });

                        builder.create().show();

                    } else if ("00".equals(isFace)) {
                        if ("face++".equals(userStateInfo) || "loaned".equals(userStateInfo)) {
                            LoggerUtil.debug("人脸识别验证已通过");
                            if ("0".equals(quotaStatus)) {
                                //Toast.makeText(getActivity(), "您有逾期，已冻结可用额度:" + quotaFreezeAmt, Toast.LENGTH_SHORT).show();
                                ToastUtils.showCenterToast(  "您有逾期，已冻结可用额度:" + quotaFreezeAmt ,getActivity());
                            } else {

                                Intent loanMoneyIntent = new Intent(getActivity(), XJLoanMoneyFirstActivity.class);
                                getActivity().startActivity(loanMoneyIntent);
                            }

                        }
                    }
                } else {
                    if ("aced".equals(userStateInfo)) {
                        //授信申请中
                        //Toast.makeText(getActivity(), "授信申请中，请稍后...", Toast.LENGTH_SHORT).show();
                        ToastUtils.showCenterToast(  "授信申请中，请稍后.."  ,getActivity());
                    } else if ("cedbad".equals(userStateInfo)) {
                        //授信申请被拒绝
                        //Toast.makeText(getActivity(), "授信申请被拒绝，请重新申请...", Toast.LENGTH_SHORT).show();
                        ToastUtils.showCenterToast(  "授信申请被拒绝，请重新申请..."  ,getActivity());
                        Intent uploadIntent = new Intent(getActivity(), XJFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("0".equals(userStateInfo) || "registered".equals(userStateInfo) || "realfied".equals(userStateInfo)) {
                        //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
                        Intent uploadIntent = new Intent(getActivity(), XJFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("saveinfo".equals(userStateInfo)) {
                        //完成完善客户信息 跳转银行卡验证页面
                        Intent intent = new Intent(getActivity(), XJDealBankCardActivity.class);
                        intent.putExtra("selfName", selfName);
                        intent.putExtra("selfIdcard", selfIdcard);
                        intent.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
                        startActivity(intent);
                    } else if ("verifi4".equals(userStateInfo)) {
                        //完成银行卡验证 跳转结果页面
                        Intent submitIntent = new Intent(getActivity(), HXResultActivity.class);
                        startActivity(submitIntent);
                    }
                }

                break;
            case R.id.zhanghu_manager_bank_card_btn:
                if ("ced".equals(userStateInfo) || "loaned".equals(userStateInfo) || "face++".equals(userStateInfo)) {
                    //已授信、 已借款、做过face++
                    if ("01".equals(isFace)) {
                        LoggerUtil.debug("人脸识别验证未通过");
                        // 银行卡管理
                        Intent bankIntent = new Intent(getActivity(), XJBankCardManageActivity.class);
                        getActivity().startActivity(bankIntent);

                    } else if ("00".equals(isFace)) {
                        if ("face++".equals(userStateInfo) || "loaned".equals(userStateInfo)) {
                            LoggerUtil.debug("人脸识别验证已通过");
                            Intent bankIntent = new Intent(getActivity(), XJBankCardManageActivity.class);
                            getActivity().startActivity(bankIntent);
                        }
                    }
                } else {
                    if ("aced".equals(userStateInfo)) {
                        //授信申请中
                        //Toast.makeText(getActivity(), "授信申请中，请稍后...", Toast.LENGTH_SHORT).show();
                        ToastUtils.showCenterToast(  "授信申请中，请稍后..."  ,getActivity());
                    } else if ("cedbad".equals(userStateInfo)) {
                        //授信申请被拒绝
                        //Toast.makeText(getActivity(), "授信申请被拒绝，请重新申请...", Toast.LENGTH_SHORT).show();
                        ToastUtils.showCenterToast(  "授信申请被拒绝，请重新申请..."  ,getActivity());
                        Intent uploadIntent = new Intent(getActivity(), XJFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("0".equals(userStateInfo) || "registered".equals(userStateInfo) || "realfied".equals(userStateInfo)) {
                        //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
                        Intent uploadIntent = new Intent(getActivity(), XJFaceIDCardInfoUploadActivity.class);
                        getActivity().startActivity(uploadIntent);
                    } else if ("saveinfo".equals(userStateInfo)) {
                        //完成完善客户信息 跳转银行卡验证页面
                        Intent intent = new Intent(getActivity(), XJDealBankCardActivity.class);
                        intent.putExtra("selfName", selfName);
                        intent.putExtra("selfIdcard", selfIdcard);
                        intent.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
                        startActivity(intent);
                    } else if ("verifi4".equals(userStateInfo)) {
                        //完成银行卡验证 跳转银行卡管理
                        Intent bankIntent = new Intent(getActivity(), XJBankCardManageActivity.class);
                        getActivity().startActivity(bankIntent);
                    }
                }

                break;
            case R.id.person_center:
                // 个人中心
                Intent personIntent = new Intent(getActivity(), XJPersonCenterActivity.class);
                personIntent.putExtra("userStateInfo", userStateInfo);
                getActivity().startActivity(personIntent);
                break;
            case R.id.loan_pay_record_fl:
                // 借还款记录
                Intent recordIntent = new Intent(getActivity(), XJLoanPayRecordListActivity.class);
                getActivity().startActivity(recordIntent);

                break;
            case R.id.common_problem_fl:
                // 常见问题
//			Intent problemIntent=new Intent(getActivity(),XJCommonProblemActivity.class);
//			getActivity().startActivity(problemIntent);
                String commonURL = Contants.BASE_URL + "/pages/FAQ/FAQ.html";
                Intent problemIntent = new Intent(getActivity(), XJUserLineActivity.class);
                problemIntent.putExtra("title", "常见问题");
                problemIntent.putExtra("url", commonURL);
                startActivity(problemIntent);
                break;
            case R.id.abount_fl:
                // 关于
                String aboutURL = Contants.BASE_URL + "/pages/about/about.html";
//			Intent aboutIntent=new Intent(getActivity(),AboutActivity.class);
//			getActivity().startActivity(aboutIntent);
                Intent aboutIntent = new Intent(getActivity(), XJUserLineActivity.class);
                aboutIntent.putExtra("title", "关于融数");
                aboutIntent.putExtra("url", aboutURL);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }
    }

}
