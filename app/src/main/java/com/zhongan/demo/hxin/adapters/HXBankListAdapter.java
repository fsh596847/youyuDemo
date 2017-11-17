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

public class HXBankListAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String,String>> banks;

	public HXBankListAdapter(Context context, List<Map<String,String>> banks) {
		super();
		this.context = context;
		this.banks=banks;

	}

	@Override
	public int getCount() {
		// Infinite loop
		return banks.size();
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view =  LayoutInflater.from(context).inflate(
					R.layout.hxall_support_bank_pop_list_item_layout, null);
			holder.mBankNameBreif= (TextView) view.findViewById(R.id.support_bank_name_breif_tv);
			holder.mBankName=(TextView) view.findViewById(R.id.support_bank_name_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
		
		holder.mBankName.setText(banks.get(position).get("bankName"));
		holder.mBankNameBreif.setText(banks.get(position).get("bankNameBreif"));
		
		return view;
	}

	class ViewHolder {

      
		TextView mBankName,mBankNameBreif;
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
		return banks.get(position);
	}
}

