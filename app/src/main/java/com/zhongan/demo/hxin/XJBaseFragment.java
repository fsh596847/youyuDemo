package com.zhongan.demo.hxin;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.util.SharedPreferenceUtils;
import com.zhongan.demo.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class XJBaseFragment<T> extends Fragment {

    public static final String TAG = "XJBaseFragment";

    protected SharedPreferenceUtils sharePrefer;
    protected LayoutInflater layoutInflater;

    protected int screenHeight;
    protected int screenWidth;
    Activity mActivity;


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        mActivity = getActivity();

        layoutInflater = context.getLayoutInflater();
        sharePrefer = new SharedPreferenceUtils(context);

        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenHeight = outMetrics.heightPixels;
        screenWidth = outMetrics.widthPixels;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
    }


    /**
     * Post请求 发送请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void httpRequest(String url, RequestParams params, final ResultCallBack callBack) {
        LoggerUtil.debug(TAG, "-----99-url-------" + url);
        MyApplication.instance.dataHttp.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (callBack != null) {
                    //Toast.makeText(getActivity(), "网络通信异常", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("网络通信异常!" ,getActivity());
                    callBack.onFailure(arg0, arg1);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (callBack != null && responseInfo != null && responseInfo.result != null) {
                    LoggerUtil.debug(TAG, "----responseInfo---" + responseInfo.result);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(responseInfo.result);
                        LoggerUtil.debug(TAG, "------responseInfo-------" + responseInfo.result);
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
                            //Toast.makeText(getActivity(), returnMsg, Toast.LENGTH_SHORT).show();
                            ToastUtils.showCenterToast(returnMsg ,getActivity());
                            if (!sharePrefer.iSLogin()) {

                                ToastUtils.showCenterToast("没有登录",getActivity());
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        } else {

                            callBack.onError(returnCode, returnMsg);
                        }

                    } catch (JSONException e) {
                        LoggerUtil.debug(TAG, "数据异常--------------->" + e.toString());
                        callBack.onError("", "数据异常");
                        //Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                        ToastUtils.showCenterToast( "数据异常" ,getActivity());
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

}
