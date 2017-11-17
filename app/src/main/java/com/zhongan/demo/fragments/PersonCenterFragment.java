package com.zhongan.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhongan.demo.AboutActivity;
import com.zhongan.demo.BuildConfig;
import com.zhongan.demo.ChangePasswordActivity;
import com.zhongan.demo.GetPasswordActivity;
import com.zhongan.demo.HtmlActivity;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.MenuListActivity;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.R;
import com.zhongan.demo.SettingActivity;
import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.okhttp.callback.Callback;
import consumer.fin.rskj.com.library.utils.Constants;

/**
 * Created by HP on 2017/10/12.
 */

public class PersonCenterFragment extends HXBaseFragment implements View.OnClickListener{

    private static final String TAG = "PersonCenterFragment";

    private View mBaseView;
    private TextView login_status;
    private Intent intent;
    private TextView bank_card;

    private String isShouPwd = "0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null != mBaseView) {
            ViewGroup parent = (ViewGroup) mBaseView.getParent();
            if (null != parent) {
                parent.removeView(mBaseView);
            }
        } else {
            mBaseView = inflater.inflate(R.layout.fragment_personcenter, null);
            initView();// 控件初始化
        }

        return mBaseView;
    }


    private void initView() {
        login_status = (TextView) mBaseView.findViewById(R.id.login_status);
        if(sharePrefer.iSLogin()){
            String phone = sharePrefer.getPhone();
            if(!TextUtils.isEmpty(phone)){
                login_status.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
                login_status.setTextColor(getResources().getColor(R.color.color_666666));
            }else {
                login_status.setText("--");
                login_status.setTextColor(getResources().getColor(R.color.colorPrimaryDarkTrans));
            }

        }else {
            login_status.setText("立即登录");
            login_status.setTextColor(getResources().getColor(R.color.colorPrimaryDarkTrans));
        }

        login_status.setOnClickListener(this);
        bank_card = (TextView) mBaseView.findViewById(R.id.bank_card);

        mBaseView.findViewById(R.id.abount).setOnClickListener(this);
        mBaseView.findViewById(R.id.help_center).setOnClickListener(this);

        if(sharePrefer.iSLogin()){
            mBaseView.findViewById(R.id.setting).setVisibility(View.VISIBLE);
        }else {
            mBaseView.findViewById(R.id.setting).setVisibility(View.GONE);
            mBaseView.findViewById(R.id.bank_layout).setVisibility(View.GONE);
        }
        mBaseView.findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.login_status:
                if(!sharePrefer.iSLogin()){
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.abount:
                intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.help_center:
                intent = new Intent(getActivity(), HtmlActivity.class);
                intent.putExtra("url","http://app-web.rskj99.com/helpcenter/helpcenter.html");
                intent.putExtra("title","帮助中心");
                startActivity(intent);
                break;

            case R.id.setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("isShouPwd",isShouPwd);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.Log(TAG, ">>>>onResume>>>>>" );

        if(sharePrefer.iSLogin()){
            M100142();
        }
    }

    private void M100142() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("transCode", "M100142");//接口标识
        paramsMap.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        paramsMap.put("clientToken", sharePrefer.getToken());//登录后token

        HttpInfo httpInfo = HttpInfo.Builder()
                .setUrl(Constants.REQUEST_URL)//请求URL
                .addParams(paramsMap)
                .build();

        OkHttpUtil.getDefault(this).doPostAsync(httpInfo, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
                String result = info.getRetDetail().toString();
                Log.d(TAG, "onSuccess result = " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);

                    if (!jsonObject.isNull("isShowAcct")) {
                        String isShowAcct = jsonObject.getString("isShowAcct");
                        isShouPwd = jsonObject.getString("isShouPwd");

                        if(isShowAcct.equals("1")){//是否显示银行卡
                            getBankCardMsg();
                        }else {
                            mBaseView.findViewById(R.id.bank_layout).setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    LogUtils.Log("error", "数据解析有误" + e.toString());
                }
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                String result = info.getRetDetail().toString();
                LogUtils.Log("onFailure = ", "数据解析有误" + result);
            }
        });

    }

    //获取银行卡
    private void getBankCardMsg() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("transCode", "M100132");//接口标识
        paramsMap.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        paramsMap.put("clientToken", sharePrefer.getToken());//登录后token

        HttpInfo httpInfo = HttpInfo.Builder()
                .setUrl(Constants.REQUEST_URL)//请求URL
                .addParams(paramsMap)
                .build();

        OkHttpUtil.getDefault(this).doPostAsync(httpInfo, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
                String result = info.getRetDetail().toString();
                Log.d(TAG, "onSuccess result = " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if("000000".equals(jsonObject.getString("returnCode"))){
                        if(!jsonObject.isNull("bankList")){
                            JSONObject object =  jsonObject.getJSONArray("bankList").getJSONObject(0);

                            Log.d(TAG, "bankCode = " + object.getString("bankCode"));
                            Log.d(TAG, "bankName = " + object.getString("bankName"));

                            String bankCode = object.getString("bankCode");
                            String bankName = object.getString("bankName");

                            String last4 = bankCode.substring(bankCode.length()-5,bankCode.length()).replace(" ","");

                            bank_card.setText("(" + last4 + ")" + " " + bankName);
                        }
                    }

                } catch (JSONException e) {
                    LogUtils.Log("error", "数据解析有误" + e.toString());
                }
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                String result = info.getRetDetail().toString();
                LogUtils.Log("onFailure = ", "数据解析有误" + result);
            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.Log(TAG, ">>>>onHiddenChanged>>>>>" + hidden);
        if(!hidden ){
            if(sharePrefer.iSLogin()){
                String phone = sharePrefer.getPhone();
                if(!TextUtils.isEmpty(phone)){
                    login_status.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
                }else {
                    login_status.setText("--");
                }
            }else {
                login_status.setText("立即登录");
                mBaseView.findViewById(R.id.setting).setVisibility(View.GONE);
                mBaseView.findViewById(R.id.bank_layout).setVisibility(View.GONE);
            }
        }
    }
}
