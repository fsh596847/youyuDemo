package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongan.demo.R;

import java.util.List;
import java.util.Map;


public class HXAllPayBanksListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String,String>> bankData;
	private int selectedPosition=-1;
	
	
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public HXAllPayBanksListAdapter(Context mContext,
                                    List<Map<String, String>> bankData) {
		super();
		this.mContext = mContext;
		this.bankData = bankData;
		
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<Map<String, String>> bankData) {
		this.bankData  = bankData;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bankData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bankData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final int selectPosition=position;
		ViewHolder holder=null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView =  LayoutInflater.from(mContext).inflate(R.layout.hxall_pay_banks_pop_list_item_layout, null);
			holder.mBankLogoIv=(ImageView) convertView.findViewById(R.id.pay_bank_icon);
			holder.mBankNameTv=(TextView) convertView.findViewById(R.id.pay_bank_name_tv);
			holder.mSelectBtn=(ImageView) convertView.findViewById(R.id.pay_bank_selected_icon);
			holder.mLineOne=convertView.findViewById(R.id.pay_bank_line_one);
			holder.mLineTwo=convertView.findViewById(R.id.pay_bank_line_two);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			@SuppressWarnings("unchecked")
            Map<String,String> item =  (Map<String, String>) getItem(position);
			String bankLogo=item.get("bankLogo");//银行卡logo
			String bankName=item.get("bankName");//银行名称
			holder.mBankLogoIv.setBackgroundResource(Integer.valueOf(bankLogo));
			holder.mBankNameTv.setText(bankName);
			if(selectPosition==position)
			{
				holder.mSelectBtn.setVisibility(View.VISIBLE);
			}else
			{
				holder.mSelectBtn.setVisibility(View.GONE);
			}
			if(position==(bankData.size()-1))
			{
				holder.mLineOne.setVisibility(View.GONE);
				holder.mLineTwo.setVisibility(View.VISIBLE);
			}else
			{
				holder.mLineOne.setVisibility(View.VISIBLE);
				holder.mLineTwo.setVisibility(View.GONE);
			}
			
		return convertView;
	}
	
	class ViewHolder {
		ImageView mBankLogoIv,mSelectBtn;
		TextView mBankNameTv;
		View mLineOne,mLineTwo;
	}

}
