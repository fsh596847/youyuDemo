package com.zhongan.demo;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.GsonBuilder;
import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.http.OkHttpRequestManager;
import consumer.fin.rskj.com.library.login.ReqCallBack;
import com.zhongan.demo.module.CommonResponse;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.RegexUtils;
import com.zhongan.demo.util.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HP on 2017/8/10.
 */

public class GetPasswordActivity extends BaseActivity {

    private EditText user_phone;
    private EditText edit_code;
    private EditText password;

    private Button get_code;
    private Button finish;

    private Timer timer;
    private final int NUM = 120;//重新得到验证码间隔时间
    private int curNum = 120;//重新得到验证码剩余时间

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if(what == 0){
                if(null != timer){
                    timer.cancel();
                }
                get_code.setText("获取验证码");
                //get_code.setTextColor(getResources().getColor(R.color.colorAccent));
                get_code.setEnabled(true);

            }else{
                get_code.setText(curNum+" s");
                //get_code.setTextColor(getResources().getColor(R.color.colorAccent));
                get_code.setEnabled(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.rskj_activity_getpassword);

        initView();
    }


    private void initView() {

        user_phone = (EditText) findViewById(R.id.user_phone);
        edit_code = (EditText) findViewById(R.id.edit_code);
        password = (EditText) findViewById(R.id.password);
        get_code = (Button) findViewById(R.id.get_code);

        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable phone = user_phone.getText();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showCenterToast("手机号不能为空", GetPasswordActivity.this);
                    return;
                }

                if (!RegexUtils.isMatchPhoneNum(phone.toString())) {
                    ToastUtils.showCenterToast("请输入正确的手机号", GetPasswordActivity.this);
                    return;
                }

                getCode(phone.toString());


            }
        });


        finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePWd();

            }
        });

    }


    private void startTimer(){
        curNum = NUM;
        timer = null;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(--curNum);
            }
        }, 0,1000);
    }


    private void getCode(String phone){

        showProgressDialog(null);
        paramsMap.clear();

        okHttpRequestManager.requestAsyn(HttpContent.HTTP_PASSWORD_CODE + phone,
                OkHttpRequestManager.TYPE_RESTFUL_GET,paramsMap,
                new ReqCallBack<String>(){

                    @Override
                    public void onReqSuccess(String result) {
                        LogUtils.Log(TAG,"HTTP_PASSWORD_CODE result = " + result);
                        progressDialogDismiss();
                        if(null == gson){
                            gson = new GsonBuilder().create();
                        }

                        requestResult = gson.fromJson(result,CommonResponse.class);

                        if(null != requestResult && "success".equals(requestResult.getCode() )){
                            get_code.setText(NUM + " s");
                            startTimer();
                            handler.sendEmptyMessageDelayed(10,100);
                            ToastUtils.showCenterToast("验证码获取成功",GetPasswordActivity.this);
                        }else {
                            handler.sendEmptyMessageDelayed(0,100);
                            ToastUtils.showCenterToast(requestResult.getMessage(),GetPasswordActivity.this);
                        }


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                        progressDialogDismiss();
                        ToastUtils.showCenterToast(errorMsg,GetPasswordActivity.this);
                        LogUtils.Log(TAG,"getVerifyCode result = " + errorMsg);
                        get_code.setEnabled(true);
                        ToastUtils.showCenterToast(errorMsg  ,GetPasswordActivity.this);
                    }
                });

    }



    private void changePWd(){


        if(TextUtils.isEmpty(user_phone.getText())){
            ToastUtils.showCenterToast("手机号不能为空",GetPasswordActivity.this);
            return;
        }

        if(!RegexUtils.isMatchPhoneNum(user_phone.getText().toString())){
            ToastUtils.showCenterToast("手机号不合法",GetPasswordActivity.this);
            return;
        }

        if(TextUtils.isEmpty(edit_code.getText()) || edit_code.getText().length() < 4){
            ToastUtils.showCenterToast("请输入正确的验证码",GetPasswordActivity.this);
            return;
        }


        if(TextUtils.isEmpty(password.getText()) ){
            ToastUtils.showCenterToast("密码不能为空",this);
            return;
        }


        if(!RegexUtils.passwordMatcher(password.getText().toString() )){
            ToastUtils.showCenterToast("密码不符合规则",this);
            return;
        }

        showProgressDialog(null);

        paramsMap.clear();
        paramsMap.put("mobilePhone", user_phone.getText().toString());
        paramsMap.put("verificaCode",edit_code.getText().toString());
        paramsMap.put("password",password.getText().toString());

        okHttpRequestManager.requestAsyn(HttpContent.HTTP_FORGET_PASSWORD,
                OkHttpRequestManager.TYPE_POST_JSON,paramsMap,
                new ReqCallBack<String>(){

                    @Override
                    public void onReqSuccess(String result) {
                        LogUtils.Log(TAG,"getVerifyCode result = " + result);
                        progressDialogDismiss();

                        if(null == gson){
                            gson = new GsonBuilder().create();
                        }

                        requestResult = gson.fromJson(result,CommonResponse.class);

                        if(null != requestResult && "success".equals(requestResult.getCode() )){
                            ToastUtils.showCenterToast("重置密码成功",GetPasswordActivity.this);
                            GetPasswordActivity.this.finish();
                        }else {
                            ToastUtils.showCenterToast(requestResult.getMessage(),GetPasswordActivity.this);
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.Log(TAG,"getVerifyCode result = " + errorMsg);
                        progressDialogDismiss();

                        ToastUtils.showCenterToast(errorMsg  ,GetPasswordActivity.this);
                    }
                });

    }
}
