package com.zhongan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zhongan.demo.http.OkHttpRequestManager;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.impl.ReqCallBack;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.TopNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.library.activitys.ReSetPayPWDActivity;

/**
 * Created by HP on 2017/10/17.
 */

public class SettingActivity extends BaseActivity {

    private TopNavigationView navigationView;
    String isShouPwd = "0";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_setting);

        isShouPwd = getIntent().getStringExtra("isShouPwd");
        if(TextUtils.isEmpty(isShouPwd)){
            isShouPwd = "0";
        }

        initView();
    }


    private void initView() {

        navigationView = (TopNavigationView) findViewById(R.id.topbar);
        navigationView.setClickListener(new TopNavigationView.NavigationViewClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        findViewById(R.id.chanegpwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });


        if(isShouPwd.equals("0")){
            findViewById(R.id.change_paypassword).setVisibility(View.GONE);
        }else {
            findViewById(R.id.change_paypassword).setVisibility(View.VISIBLE);
            findViewById(R.id.change_paypassword).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingActivity.this, ReSetPayPWDActivity.class);
                    startActivity(intent);
                }
            });
        }

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    //退出登录
    private void logout() {
        showProgressDialog(null);
        paramsMap.clear();

        okHttpRequestManager.requestAsyn("member/logout",
            OkHttpRequestManager.TYPE_POST_JSON, paramsMap,
            new ReqCallBack<String>() {

                @Override
                public void onReqSuccess(String result) {
                    progressDialogDismiss();
                    LogUtils.Log(TAG, "onReqSuccess result = " + result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        if ("success".equals(jsonObject.getString("code"))) {
                            MyApplication.getSP(SettingActivity.this).setLogin(false);
                            Intent intent =
                                new Intent(SettingActivity.this, MenuListActivity.class);
                            startActivity(intent);
                            finish();
                            ActivityStackManagerUtils.getInstance().finishAllActivity();
                        } else {
                            Toast.makeText(SettingActivity.this, jsonObject.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        }

                        LogUtils.Log(TAG, "------finishCallBack-------");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onReqFailed(String errorMsg) {
                    progressDialogDismiss();
                    LogUtils.Log(TAG, "onReqFailed result = " + errorMsg);

                    ToastUtils.showCenterToast(errorMsg, SettingActivity.this);
                }
            });

    }

}
