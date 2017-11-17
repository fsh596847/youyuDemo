package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zhongan.demo.R;

import java.util.List;
import java.util.Map;

public class HXAboutListAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String,String>> datas;

	public HXAboutListAdapter(Context context, List<Map<String,String>> datas) {
		super();
		this.context = context;

		this.datas=datas;

	}

	@Override
	public int getCount() {
		// Infinite loop
		return datas.size();
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view =  LayoutInflater.from(context).inflate(
					R.layout.hxcommon_problem_list_item_layout, null);
			
			holder.mTv1=(TextView) view.findViewById(R.id.common_problem_tv);
			
			holder.line1= view.findViewById(R.id.line_one);
			holder.line2= view.findViewById(R.id.line_two);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
	
		holder.mTv1.setText(datas.get(position).get("title"));
		
		
		
		if(position==(datas.size()-1))
		{
			
			holder.line1.setVisibility(View.GONE);
			holder.line2.setVisibility(View.VISIBLE);
		}else 
		{
			
			holder.line2.setVisibility(View.GONE);
			holder.line1.setVisibility(View.VISIBLE);
		}
		
		return view;
	}

	class ViewHolder {

		TextView mTv1;
		View line1,line2;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return position % advertData.size();
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}
}

