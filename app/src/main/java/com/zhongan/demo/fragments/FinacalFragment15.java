package com.zhongan.demo.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.BuildConfig;
import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;
import consumer.fin.rskj.com.library.activitys.WebViewActivity;
import consumer.fin.rskj.com.library.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static consumer.fin.rskj.com.library.activitys.BaseActivity.sessionId;
import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;

/**
 * Created by HP on 2017/10/23.
 * 消金1.5
 */

public class FinacalFragment15 extends HXBaseFragment {

    public static final String TAG = "FinacalFragment15";
    private View mBaseView;
    private Dialog mDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        if (null != mBaseView) {
            ViewGroup parent = (ViewGroup) mBaseView.getParent();
            if (null != parent) {
                parent.removeView(mBaseView);
            }
        } else {
            mBaseView = inflater.inflate(R.layout.fragment_xj15, null);
            initView();// 控件初始化
        }

        return mBaseView;

    }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //消金2.0 需要打开
    getH5URL();
  }




    private void initView() {
        mDialog = Util.createLoadingDialog(getActivity(), "数据加载中,请稍等...");
        mBaseView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m100660();
            }
        });
    }



    private void m100660() {
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", "M100660");
        params.addBodyParameter("channelNo", "3");
        params.addBodyParameter("version", BuildConfig.VERSION_NAME);
        params.addBodyParameter("clientToken", sharePrefer.getToken());
        params.addBodyParameter("productType", "1");
        params.addBodyParameter("legalPerNum", "00001");// 法人编号

        httpRequest(Constants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                LogUtils.Log(TAG, "m100660 onSuccess = " + data);
                mDialog.cancel();

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String returnCode = jsonObject.getString("returnCode");
                    if("000000".equals(returnCode)){
                        JSONArray array = jsonObject.getJSONArray("rows");

                        if(null != array && array.length() > 0){
                            String id = array.getJSONObject(0).getString("id");
                            String fundId = array.getJSONObject(0).getString("fundId");
                          //                            sharePrefer.setId15(id);
                          //                            sharePrefer.setFundId15(fundId);

                            Intent  intent = new Intent(getActivity(), WebViewActivity.class);
                            intent.putExtra("url",Constants.BASE_URL + sharePrefer.getApplyLoan());
                            intent.putExtra("title","贷款");
                            intent.putExtra("id",id);
                            intent.putExtra("fundId",fundId);
                            startActivity(intent);
                        }

                    }else {
                        ToastUtils.showCenterToast(jsonObject.getString("returnMsg") ,getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStart() {
                mDialog.show();
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                mDialog.cancel();
                LogUtils.Log(TAG, "onFailure = " + msg);
            }

            @Override
            public void onError(String returnCode, String msg) {
                mDialog.cancel();
                LogUtils.Log(TAG, "onError = " + msg);
            }
        });

    }

  protected void getH5URL() {
    RequestParams paramsMap = new RequestParams("utf-8");
    paramsMap.addBodyParameter("projectType", "suixindai2.0");
    paramsMap.addBodyParameter("transCode", "M107102");//接口标识
    paramsMap.addBodyParameter("channelNo", Constants.CHANNEL_NO);//渠道标识
    paramsMap.addBodyParameter("clientToken", sharePrefer.getToken());//登录后token

    LogUtils.Log("getH5URL", "getH5URL data = " + paramsMap.toString());
    httpRequest(Constants.REQUEST_URL, paramsMap, new ResultCallBack() {
      @Override
      public void onSuccess(String data) {
        LogUtils.Log("getH5URL", "onSuccess data = " + data);
        mDialog.cancel();

        try {
          JSONObject jsonObject = new JSONObject(data);
          LogUtils.Log(TAG, "sessionId = " + sessionId);

          JSONObject object = new JSONObject(jsonObject.getString("h5Urls"));
          String applyLoan = object.getString("applyLoan");

          String paymentStatus = object.getString("paymentStatus");//还款成功
          String payingStatus = object.getString("payingStatus");//放款中
          String cedStatus = object.getString("cedStatus");//授信审核中

          String protocolCenter = object.getString("protocolCenter");//协议
          String loanStatus = object.getString("loanStatus");//借款状态

          sharePrefer.setApplyLoan(applyLoan);
          sharePrefer.setProtocolCenter(protocolCenter);
          sharePrefer.setLoanStatus(loanStatus);

          sharePrefer.setPaymentStatus(paymentStatus);
          sharePrefer.setPayingStatus(payingStatus);
          sharePrefer.setCedStatus(cedStatus);

          LogUtils.Log("getH5URL", "applyLoan = " + applyLoan);
          sharePrefer.setFirstUrl(BASE_URL + applyLoan);
          LogUtils.Log(TAG, "sessionId = " + sessionId);

          Intent intent = new Intent(getActivity(), MenuListActivity2.class);
          startActivity(intent);
        } catch (JSONException e) {
          LogUtils.Log(TAG, "JSONException = " + e.getMessage());
        }
      }

      @Override
      public void onStart() {
        mDialog.show();
      }

      @Override
      public void onFailure(HttpException exception, String msg) {
        mDialog.cancel();
        LogUtils.Log(TAG, "onFailure = " + msg);
      }

      @Override
      public void onError(String returnCode, String msg) {
        mDialog.cancel();
        LogUtils.Log(TAG, "onError = " + msg);
      }
    });
  }

    @Override
    public void onResume() {
        super.onResume();
      //        if(!sharePrefer.iSLogin()){
      //            startActivity(new Intent(getActivity(), MenuListActivity.class));
      //        }
    }

  //    @Override
  //    public void onHiddenChanged(boolean hidden) {
  //        // TODO Auto-generated method stub
  //        super.onHiddenChanged(hidden);
  //
  //        if(!hidden){
  //            if(!sharePrefer.iSLogin()){
  //                startActivity(new Intent(getActivity(), MenuListActivity.class));
  //            }
  //        }
  //    }
}
