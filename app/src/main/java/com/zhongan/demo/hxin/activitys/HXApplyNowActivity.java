package com.zhongan.demo.hxin.activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by admin on 2017/5/31.
 */

public class HXApplyNowActivity extends HXBaseActivity implements View.OnClickListener {
    private TextView tv_apply_now;

    private String isFace;//00：通过验证 01：验证未通过
    private String userStateInfo="0";//客户状态
    private String canUsedSum="0";//可用额度
    private String selfName="",selfIdcard="";
    private Bundle loanAmountInfo;//额度信息
    private Dialog mdialog;
    private String quotaStatus;//有逾期，冻结资金
    private String quotaFreezeAmt;//冻结资金
    private int currentPage = 1;// 当前页
    private int pageSize = 10;// 分页条数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hxactivity_apply_now_layout);
        initView();
    }

    private void initView() {
        mdialog= Util.createLoadingDialog(this,"数据加载中,请稍等...");
        Intent intent = getIntent();
        tv_apply_now = (TextView) findViewById(R.id.tv_apply_now);
        tv_apply_now.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        queryLoanAmountInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_apply_now:
                // 我要贷款
                if("aced".equals(userStateInfo))
                {
                    //授信申请中
                    //Toast.makeText(HXApplyNowActivity.this,"授信申请中，请稍后...", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("授信申请中，请稍后..." ,HXApplyNowActivity.this);
                }else if("cedbad".equals(userStateInfo))
                {
                    //授信申请被拒绝
                    //Toast.makeText(HXApplyNowActivity.this,"授信申请被拒绝，请重新申请...", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("授信申请被拒绝，请重新申请..." ,HXApplyNowActivity.this);
                    Intent uploadIntent=new Intent(HXApplyNowActivity.this,HXFaceIDCardInfoUploadActivity.class);
                    HXApplyNowActivity.this.startActivity(uploadIntent);
                }else if("0".equals(userStateInfo)||"registered".equals(userStateInfo)||"realfied".equals(userStateInfo))
                {
                    //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
                    Intent uploadIntent=new Intent(HXApplyNowActivity.this,HXFaceIDCardInfoUploadActivity.class);
                    HXApplyNowActivity.this.startActivity(uploadIntent);
                }else if("saveinfo".equals(userStateInfo))
                {
                    //完成完善客户信息 跳转银行卡验证页面
                    Intent intent = new Intent(HXApplyNowActivity.this,HXDealBankCardActivity.class);
                    intent.putExtra("selfName",selfName);
                    intent.putExtra("selfIdcard",selfIdcard);
                    intent.putExtra("option","check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
                    startActivity(intent);
                }else if("verifi4".equals(userStateInfo))
                {
                    //完成银行卡验证 跳转设置密码页面
                    Intent submitIntent = new Intent(HXApplyNowActivity.this,HXResultActivity.class);
                    startActivity(submitIntent);
                }
                break;
            default:
                break;
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE:
                    LoggerUtil.debug("isLogin---------->"+getSharePrefer().iSLogin());
                    mdialog.cancel();
                    CustomDialog.Builder builder = new CustomDialog.Builder(HXApplyNowActivity.this);
                    builder.setMessage((String) msg.obj);
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

                    builder.create().show();
                    break;
                case Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS:

                    loanAmountInfo=msg.getData();
                    canUsedSum=loanAmountInfo.getString("amountLoan");//可用额度
                    isFace=loanAmountInfo.getString("isFace");
                    userStateInfo=loanAmountInfo.getString("userStateInfo");
                    selfName=loanAmountInfo.getString("userName");
                    selfIdcard=loanAmountInfo.getString("idCard");
                    quotaStatus=loanAmountInfo.getString("quotaStatus");
                    quotaFreezeAmt=loanAmountInfo.getString("quotaFreezeAmt");
                    LoggerUtil.debug("1111111111selfName---->" +selfName);
                    LoggerUtil.debug("1111111111selfIdcard---->" +selfIdcard);
                    LoggerUtil.debug("1111111111userStateInfo---->" +userStateInfo);
                    LoggerUtil.debug("1111111111quotaStatus---->" +quotaStatus);
                    LoggerUtil.debug("1111111111quotaFreezeAmt---->" +quotaFreezeAmt);
                    //    			canUsedSum="30000.00";
                    //                isFace="00";
                    getSharePrefer().setUserStateInfo(userStateInfo);

                    //				edit.putString("userStateInfo", userStateInfo);
                    //				edit.commit();
                    if("ced".equals(userStateInfo)||"loaned".equals(userStateInfo)||"face++".equals(userStateInfo)){
                        //已授信、 已借款、做过face++
                        getSharePrefer().setCanUsedSum(canUsedSum);

                    }else
                    {
                        //已注册、 完善客户信息、 验4  、授信申请中、授信申请被拒绝

                    }
                    //queryProductList();
                    mdialog.cancel();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 贷款额度查询
     **/
    private void queryLoanAmountInfo() {

        LoggerUtil.debug("基本信息提交数据:url---->" + Contants.REQUEST_URL

                + "\ntransCode-->" + Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO
                + "\nchannelNo---->"
                + Contants.CHANNEL_NO+"\nclientToken--------->"+getSharePrefer().getToken()
                +"\ncustMobile-------->"+getSharePrefer().getPhone());
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type","application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken",getSharePrefer().getToken());
        httpRequest( Contants.REQUEST_URL,params,new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String amountLoan=resultMap.get("amountLoan");//可贷款金额
                String userStateInfo=resultMap.get("userStateInfo");//客户状态
                String isFace=resultMap.get("isFace");//人脸识别验证结果   00：通过验证 01：验证未通过
                String userName=resultMap.get("userName");//客户姓名
                String idCard=resultMap.get("idCard");//客户身份证
                String quotaStatus=resultMap.get("quotaStatus");//quotaStatus： 0 是有逾期，有冻结
                String quotaFreezeAmt=resultMap.get("quotaFreezeAmt");//冻结的金额
                Bundle bundle=new Bundle();
                bundle.putString("amountLoan", amountLoan);//可用余额
                bundle.putString("isFace", isFace);//人脸识别验证结果
                bundle.putString("userStateInfo", userStateInfo);//客户状态
                bundle.putString("userName", userName);//客户姓名
                bundle.putString("idCard",idCard);//客户身份证
                bundle.putString("quotaStatus", quotaStatus);//quotaStatus： 0 是有逾期，有冻结
                bundle.putString("quotaFreezeAmt",quotaFreezeAmt);//冻结的金额
                Message msg = new Message();
                msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
                msg.setData(bundle);
                mHandler.handleMessage(msg);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                mdialog.show();
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                // TODO Auto-generated method stub
                if(mdialog!=null)
                {
                    mdialog.cancel();
                }
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                if(mdialog!=null)
                {
                    mdialog.cancel();
                }
                if("E10030101".equals(returnCode)||"E00015301".equals(returnCode)||"E10030101".equals(returnCode)||"E10060301".equals(returnCode))
                {
                    //限额信息不存在、用户不存在、利率参数不存在
                    Bundle bundle=new Bundle();
                    bundle.putString("amountLoan", "0");//可用余额
                    bundle.putString("isFace", "01");//人脸识别验证结果
                    bundle.putString("userStateInfo", "0");//客户状态
                    bundle.putString("userName", "");//客户姓名
                    bundle.putString("idCard","");//客户身份证
                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
                    errorMsg.setData(bundle);
                    mHandler.handleMessage(errorMsg);

                }else if(!"E999985".equals(returnCode))
                {

                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE;
                    errorMsg.obj = msg;
                    mHandler.handleMessage(errorMsg);

                }
            }
        });
    }

}
