package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.bean.HomeProductItem;

import java.util.List;

public class HXShouyeListAdapter extends BaseAdapter {
	private Context context;
	private List<HomeProductItem> titleData;

	public HXShouyeListAdapter(Context context, List<HomeProductItem> titleData) {
		super();
		this.context = context;
		this.titleData=titleData;

	}
	
	public void upateData(List<HomeProductItem> datas)  {
    	this.titleData=datas;
    	
    }

	@Override
	public int getCount() {
		// Infinite loop
		return null == titleData ? 0 : titleData.size();
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view =  LayoutInflater.from(context).inflate(
					R.layout.hxshouye_list_item_layout, null);
			holder.imageView = (ImageView) view.findViewById(R.id.shouye_list_iv);
			holder.mTv1=(TextView) view.findViewById(R.id.shouye_list_tv1);
			holder.mTv2=(TextView) view.findViewById(R.id.shouye_list_tv2);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
		holder.imageView.setBackgroundResource(R.drawable.list_pic_1);
		holder.mTv1.setText(titleData.get(position).getProductName());
		holder.mTv2.setText(titleData.get(position).getExpirationDate());
		return view;
	}

	class ViewHolder {

		ImageView imageView;
		TextView mTv1,mTv2;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return position % advertData.size();
		return position;
	}

	@Override
	public HomeProductItem getItem(int position) {
		// TODO Auto-generated method stub
		return null == titleData ? null : titleData.get(position);
	}
}

