package com.zhongan.demo.hxin.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by admin on 2017/5/31.
 */

public class HXResultActivity extends HXBaseActivity implements View.OnClickListener {
    private Dialog mDialog;

    TextView title, tv_content;
    ImageView img_in_audit;
    boolean isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hxactivity_validate_layout);
        findViewById(R.id.left_btn).setOnClickListener(this);
        mDialog = Util.createLoadingDialog(this, "请稍等...");


        img_in_audit = (ImageView) findViewById(R.id.img_in_audit);

        title = (TextView) findViewById(R.id.center_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        title.setText("审批结果");

        /**
         * intent1.putExtra("message","这不是我要的结果");
         intent1.putExtra("isSuccess",false);
         */
        isSuccess = getIntent().getBooleanExtra("isSuccess",false);
        String content = getIntent().getStringExtra("message");
        if(!TextUtils.isEmpty(content)){
            tv_content.setText(content);
        }


        if(isSuccess){
            img_in_audit.setImageResource(R.drawable.m_icon_deal_sucess);
        }else {
            img_in_audit.setImageResource(R.drawable.m_icon_deal_failur);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
//                Intent intent = new Intent(HXResultActivity.this, HXMainActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            mDialog.cancel();
            super.handleMessage(msg);
            switch (msg.what) {
                case Contants.MSG_SET_JIAOYI_PWD_SUCCESS:
//                    Intent intent = new Intent(HXResultActivity.this,HXDealResultActivity.class);
//                    intent.putExtra("checkResult","success");//验证结果：成功 success 失败failure
//                    startActivity(intent);
//                    HXResultActivity.this.finish();
                    break;
                case Contants.MSG_SET_JIAOYI_PWD_FAILURE:
                    String errorMsg = (String) msg.obj;
                    Intent errorIntent = new Intent(HXResultActivity.this, HXDealResultActivity.class);
                    errorIntent.putExtra("checkResult", "failure");//验证结果：成功 success 失败failure
                    errorIntent.putExtra("retrunMsg", errorMsg);//验证结果提示
                    startActivity(errorIntent);
                    HXResultActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    //设置工作流接口
    private void setBankCardPwd(final String pwd1, final String pwd2) {
//		 String applyAmt=sharePrefer.getString("selfRepaySum","");
        String applyAmt = "60000";
        LoggerUtil.debug("pwd1------->" + pwd1 + "\npwd2----------->" + pwd2 + "\ntoken----------->" +
                getSharePrefer().getToken() + "\napplyAmt---------->" + applyAmt);
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_SET_WORK);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("repaymentPassword", pwd1);//支付密码
        params.addBodyParameter("paymentPassword", pwd2);//支付密码
        params.addBodyParameter("applyAmt", applyAmt);// 额度
        params.addBodyParameter("productId", "1");//  测试9090产品ID
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String flag = resultMap.get("flag");
                if ("00".equals(flag)) {
                    LoggerUtil.debug("设置支付密码成功!");
                    Message msg = new Message();
                    msg.what = Contants.MSG_SET_JIAOYI_PWD_SUCCESS;
//						msg.obj=returnMsg;
                    mHandler.handleMessage(msg);
                } else if ("11".equals(flag)) {
                    LoggerUtil.debug("设置支付密码失败!");
                    String returnMsg = resultMap.get("returnMsg");// 错误提示
                    Message msg = new Message();
                    msg.what = Contants.MSG_SET_JIAOYI_PWD_FAILURE;
                    msg.obj = returnMsg;
                    mHandler.handleMessage(msg);
                } else if ("22".equals(flag)) {
                    Message msg = new Message();
                    msg.what = Contants.MSG_DO_CHECK_PWD_DIFFRIENT_FAILURE;
                    msg.obj = "两次密码输入不一致!";
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
                msg.obj = returnMsg;
                mHandler.handleMessage(msg);
            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
////            Intent intent = new Intent(HXResultActivity.this, HXMainActivity.class);
////            startActivity(intent);
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
}
