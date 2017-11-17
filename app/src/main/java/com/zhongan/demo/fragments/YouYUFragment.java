package com.zhongan.demo.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.zhongan.demo.MainActivity;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.finance.common.FinanceInitor;

import org.json.JSONException;
import org.json.JSONObject;

public class YouYUFragment extends HXBaseFragment {

    private static final String TAG = "YouYUFragment";

    private View mBaseView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null != mBaseView) {
            ViewGroup parent = (ViewGroup) mBaseView.getParent();
            if (null != parent) {
                parent.removeView(mBaseView);
            }
        } else {
            mBaseView = inflater.inflate(R.layout.fragment_youyu, null);
            initView();// 控件初始化
        }

        return mBaseView;

    }

    private void initView() {

        LogUtils.Log(TAG,"------initView---------" );

        mBaseView.findViewById(R.id.apply_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(getActivity(), "click_msh", "xyfq.entrance");
                LogUtils.Log(TAG,"memberId = " + MyApplication.getSP(getActivity()).getMemId());
                send2ZA(MyApplication.getSP(getActivity()).getMemId());
            }
        });

    }


    //参数传递给众安
    private void send2ZA(String data) {

        /**
         * UID = "f9dd3a46-b5cf-445c-9937-e194bf0ee064";
         channel = M0000003;
         partnerNo = 8016112151261010;
         */
        JSONObject json = new JSONObject();
        try {
            json.put("partnerNo", "8016112151261010");
            json.put("channel", "M0000003");
            json.put("UID", data);
            json.put("recommandCode", "");
            json.put("userPhone",MyApplication.getSP(getActivity()).getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FinanceInitor.setBusinessInfo(json.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("zaf://xyfq.entrance"));
        startActivity(intent);
    }


}
