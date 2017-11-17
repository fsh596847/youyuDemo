package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.bean.HXPayMoneyRecordListItemBean;

import java.util.List;

public class HXPayRecordPayListAdapter extends BaseAdapter {
	private Context context;
	private List<HXPayMoneyRecordListItemBean> datas;

	public HXPayRecordPayListAdapter(Context context, List<HXPayMoneyRecordListItemBean> datas) {
		super();
		this.context = context;

		this.datas=datas;

	}
    public void updateData(List<HXPayMoneyRecordListItemBean> datas)
    {
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
					R.layout.hxpay_money_pay_list_item_layout, null);
			
			holder.mTv1=(TextView) view.findViewById(R.id.pay_record_pay_type_tv);
			holder.mTv2=(TextView) view.findViewById(R.id.pay_record_pay_date_tv);
			holder.mTv3=(TextView) view.findViewById(R.id.pay_record_pay_num_tv);
			holder.line1= view.findViewById(R.id.line_one);
			holder.line2= view.findViewById(R.id.line_two);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
		HXPayMoneyRecordListItemBean item=(HXPayMoneyRecordListItemBean) getItem(position);
		holder.mTv1.setText(item.getRepayType());//还款方式
		holder.mTv2.setText(item.getRepayDate());//还款日期
		holder.mTv3.setText("¥"+item.getRepayAmt()+"(元)");//还款金额
		
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

		TextView mTv1,mTv2,mTv3;
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

