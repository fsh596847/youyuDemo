package com.zhongan.demo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.hxin.activitys.HXResultingActivity;
import com.zhongan.demo.util.LogUtils;

/**
 * 首页页面
 */

public class HenXFragment extends HXBaseFragment {

    public static final String TAG = "HenXFragment";

	private View mBaseView;
	MenuListActivity2 activity2;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.Log(TAG, ">>HenXFragment>>onCreateView>>>>>" );
		if (null != mBaseView) {
			ViewGroup parent = (ViewGroup) mBaseView.getParent();
			if (null != parent) {
				parent.removeView(mBaseView);
			}
		} else {
			mBaseView = inflater.inflate(R.layout.fragment_left, null);
			initView();// 控件初始化
		}

		return mBaseView;

	}


	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
		activity2 = (MenuListActivity2) context;
	}

	private void initView() {

		mBaseView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				activity2.s2008Post();

			}
		});
	}

}
