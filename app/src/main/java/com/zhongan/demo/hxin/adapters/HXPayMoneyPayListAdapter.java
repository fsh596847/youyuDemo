package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.activitys.HXLoanMoneyDetailListActivity;
import com.zhongan.demo.hxin.bean.HXLoanMoneyListItemBean;

import java.util.List;

public class HXPayMoneyPayListAdapter extends BaseAdapter {
	private Context context;
	private List<HXLoanMoneyListItemBean> datas;

	public HXPayMoneyPayListAdapter(Context context, List<HXLoanMoneyListItemBean> datas) {
		super();
		this.context = context;

		this.datas=datas;

	}
    public void upateData(List<HXLoanMoneyListItemBean> datas)
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
					R.layout.hxpay_money_loan_list_item_layout, null);
			
			holder.mTv1=(TextView) view.findViewById(R.id.pay_money_pay_num_tv);
			holder.mTv2=(TextView) view.findViewById(R.id.pay_money_pay_date_tv);
			holder.mTv3=(TextView) view.findViewById(R.id.pay_money_qishu_tv);
			holder.mTv4=(TextView) view.findViewById(R.id.pay_money_pay_state_tv);
			holder.mTv5=(TextView) view.findViewById(R.id.pay_money_pay_btn);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
		HXLoanMoneyListItemBean item=datas.get(position);
		final String loanStatus=item.getLoanStatus();//放款状态  3：已还清  7：已逾期
		final String loanId=item.getLoanId();//贷款Id
		holder.mTv1.setText(item.getContractAmt()+"(元)");//借款金额
		holder.mTv2.setText(item.getStartDate()+"借");//借款日期
     	holder.mTv3.setText(datas.get(position).getCurrentPeriod()+"/"+datas.get(position).getContractTerm());//还款期数
		
		if(loanStatus.equals("3"))
		{
			holder.mTv4.setText("已还清");
//			holder.mTv5.setBackgroundColor(context.getResources().getColor(R.color.color_e6e6e6));
//			holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_cccccc));
		}else if(loanStatus.equals("7"))
		{
			holder.mTv4.setText("已逾期");
//			holder.mTv5.setBackgroundColor(context.getResources().getColor(R.color.color_ffffff));
//			holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_ff7920));
		}
		holder.mTv5.setBackgroundColor(context.getResources().getColor(R.color.color_ffffff));
		holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_ff7920));
		holder.mTv5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				  Intent intent=new Intent(context, HXLoanMoneyDetailListActivity.class);
				  intent.putExtra("loanId",loanId);
				  context.startActivity(intent);
				
			}
		});
		return view;
	}

	class ViewHolder {

		TextView mTv1,mTv2,mTv3,mTv4,mTv5;
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

