package com.zhongan.demo.hxin.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;


@SuppressLint("NewApi")
public class HXShopFragment extends HXBaseFragment {
   
	 private View mBaseView;
	

	   @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
		mBaseView=inflater.inflate(R.layout.hxfragment_shop_layout, null);
		return mBaseView;
	  }
	   @Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			initviews();
		}
	  private void initviews()  {
		  

	  }
}
