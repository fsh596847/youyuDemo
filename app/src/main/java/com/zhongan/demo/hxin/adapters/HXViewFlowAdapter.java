package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongan.demo.R;


public class HXViewFlowAdapter extends BaseAdapter {
	private Context context;
	private int[] advertData;

	public HXViewFlowAdapter(Context context, int[] advertData) {
		super();
		this.context = context;
		this.advertData = advertData;

	}

	@Override
	public int getCount() {
		// Infinite loop
		return Integer.MAX_VALUE;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.hxshouye_advert_banner_item, null);
			holder.imageView = (ImageView) view.findViewById(R.id.advert_imageview);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		

		// 在这个方法里面设置图片的点击事件
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		return view;
	}

	class ViewHolder {

		ImageView imageView;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return advertData.get(position % advertData.size());
		return advertData[position % advertData.length];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return position % advertData.size();
		return position % advertData.length;
	}
}

