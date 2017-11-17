package com.zhongan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.http.OkHttpRequestManager;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.impl.ReqCallBack;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.RegexUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.TopNavigationView;

import org.json.JSONObject;

import java.util.Timer;


/**
 * Created by HP on 2017/6/21.
 * <p>
 * 修改密码
 */

public class ChangePasswordActivity extends BaseActivity {

    public static final String TAG = "ChangePasswordActivity";

    private TopNavigationView topbar;

    private EditText origin_pwd;
    private EditText new_pwd;
    private EditText new_pwd2;
    private Button changePWD;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_changepassword);

        initView();
    }

    private void initView() {
        topbar = (TopNavigationView) findViewById(R.id.topbar);
        topbar.setClickListener(new TopNavigationView.NavigationViewClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });

        origin_pwd = (EditText) findViewById(R.id.origin_pwd);
        new_pwd = (EditText) findViewById(R.id.new_pwd);
        new_pwd2 = (EditText) findViewById(R.id.new_pwd2);

        changePWD = (Button) findViewById(R.id.change_pwd);
        changePWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(origin_pwd.getText()) || !RegexUtils.passwordMatcher(origin_pwd.getText().toString())) {
                    ToastUtils.showCenterToast("原始密码格式不对", ChangePasswordActivity.this);
                    origin_pwd.setAnimation(Util.shakeAnimation(10));
                    return;
                }


                if (TextUtils.isEmpty(new_pwd.getText()) || !RegexUtils.passwordMatcher(new_pwd.getText().toString())) {
                    ToastUtils.showCenterToast("新始密码格式不对", ChangePasswordActivity.this);
                    new_pwd.setAnimation(Util.shakeAnimation(10));
                    return;
                }

                if (TextUtils.isEmpty(new_pwd2.getText()) || !RegexUtils.passwordMatcher(new_pwd2.getText().toString())) {
                    ToastUtils.showCenterToast("新始密码格式不对", ChangePasswordActivity.this);
                    new_pwd2.setAnimation(Util.shakeAnimation(10));
                    return;
                }

                changPWD();

            }
        });

    }

    private void changPWD() {
        showProgressDialog(null);
        paramsMap.clear();
        final String password1 = origin_pwd.getText().toString();
        final String password2 = new_pwd.getText().toString();
        final String password3 = new_pwd2.getText().toString();

        paramsMap.put("oldPassword", password1);
        paramsMap.put("password", password2);
        paramsMap.put("confirmPassword", password3);
        paramsMap.put("token", MyApplication.getSP(this).getToken());

        LogUtils.Log(TAG, "paramsMap = " + paramsMap.toString());

        okHttpRequestManager.requestAsyn("member/modifyPassWord",
                OkHttpRequestManager.TYPE_POST_JSON, paramsMap,
                new ReqCallBack<String>() {
                    @Override
                    public void onReqSuccess(String result) {
                        LogUtils.Log(TAG, "HTTP_PASSWORD_CODE result = " + result);
                        progressDialogDismiss();

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(result);
                            //修改密码成功
                            LogUtils.Log(TAG, "code = " + jsonObject.get("code"));
                            if ("success".equals(jsonObject.get("code"))) {
                                showToast("密码修改成功",1);
                                //MyApplication.getSP(ChangePasswordActivity.this).setLogin(false);
                                Intent intent = new Intent(ChangePasswordActivity.this, MenuListActivity2.class);
                                startActivity(intent);
                                ChangePasswordActivity.this.finish();
                            } else {
                                ToastUtils.showCenterToast(jsonObject.get("message") + "", ChangePasswordActivity.this);
                            }
                        } catch (Exception e) {
                            LogUtils.Log(TAG, "Exception = " + e.getMessage());
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        progressDialogDismiss();
                        ToastUtils.showCenterToast(errorMsg, ChangePasswordActivity.this);
                    }
                });
    }


}
