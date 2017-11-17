package com.zhongan.demo.hxin;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.ParentActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.util.SharedPreferenceUtils;
import com.zhongan.demo.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class XJBaseActivity extends ParentActivity {

    protected static final String TAG = "HXBaseActivity";

    protected SharedPreferenceUtils sharePrefer;
    protected Editor edit;
    public int screenHeight;
    public int screenWidth;

    protected Dialog mdialogProgress;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActivityStackManagerUtils.getInstance().addActivity(this);
        sharePrefer = new SharedPreferenceUtils(this.getApplicationContext());
        mdialogProgress = Util.createLoadingDialog(this, "数据加载中,请稍等...");
    }





    /**
     * Post请求 发送请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void httpRequest(String url, RequestParams params, final ResultCallBack callBack) {

        MyApplication.instance.dataHttp.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (callBack != null) {
                    Toast.makeText(XJBaseActivity.this, "网络通信异常", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast(  "网络通信异常"  ,XJBaseActivity.this);
                    callBack.onFailure(arg0, arg1);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo != null && responseInfo.result != null) {

                    LoggerUtil.debug(TAG, "----responseInfo---" + responseInfo.result);

                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(responseInfo.result);
                        //数据返回成功
                        String returnCode = jsonObject.getString("returnCode");
                        String returnMsg = jsonObject.getString("returnMsg");
                        LoggerUtil.debug(TAG, "returnCode-------------->" + returnCode + "\nreturnMsg------------->" + returnMsg);
                        if ("000000".equals(returnCode)) {
                            callBack.onSuccess(responseInfo.result);
                        } else if ("E999985".equals(returnCode)) {
                            LoggerUtil.debug(TAG, "--------------token失效，或者用户未登录-------------");
                            sharePrefer.setLogin(false);
//    						callBack.onError(returnCode,"token失效，或者用户未登录");
//                            Toast.makeText(XJBaseActivity.this, returnMsg, Toast.LENGTH_SHORT).show();
                            ToastUtils.showCenterToast(  returnMsg  ,XJBaseActivity.this);
                            if (!sharePrefer.iSLogin()) {

                                Intent intent = new Intent(XJBaseActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            if (!"E1001062".equals(returnCode)) {
//                                Toast.makeText(XJBaseActivity.this, returnMsg, Toast.LENGTH_SHORT).show();
                                ToastUtils.showCenterToast(  returnMsg  ,XJBaseActivity.this);
                            }

                            callBack.onError(returnCode, returnMsg);

                        }

                    } catch (JSONException e) {
                        LoggerUtil.debug(TAG, "数据异常--------------->" + e.toString());
                        callBack.onError("", "数据异常");
//                        Toast.makeText(XJBaseActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        ToastUtils.showCenterToast( "数据异常"  ,XJBaseActivity.this);
                    }

                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (callBack != null) {
                    callBack.onStart();
                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                boolean res = hideKeyboard(v.getWindowToken());
                if (res) {
                    //隐藏了输入法，则不再分发事件
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            LoggerUtil.debug("------EditText--------");
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private boolean hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }


    /**
     * 贷款额度查询
     **/
    protected void getLoanAmountInfo(final Handler handler) {
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type","application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken",sharePrefer.getToken());
        httpRequest( Contants.REQUEST_URL,params,new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();

                Log.d("TTTT","data == " + data);
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

                if("cedbad".equals(userStateInfo)){
                    handler.sendEmptyMessage(2);
                    return;
                }

                if("ced".equals(userStateInfo)){
                    handler.sendEmptyMessage(3);
                    return;
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
            }

            @Override
            public void onError(String returnCode, String msg) {

            }
        });
    }
}
