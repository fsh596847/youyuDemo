package com.zhongan.demo.hxin.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.activitys.HXPayMoneyPayDetailActivity;
import com.zhongan.demo.hxin.bean.HXLoanMoneyDetailListItemBean;

import java.util.List;

public class HXLoanMoneyDetailListAdapter extends BaseAdapter {
	private Context context;
	private List<HXLoanMoneyDetailListItemBean> datas;
    private String loanId;
    private String overdueCount;//是否逾期 0：未逾期 1:有逾期
    
	public String getOverdueCount() {
		return overdueCount;
	}
	public void setOverdueCount(String overdueCount) {
		this.overdueCount = overdueCount;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public HXLoanMoneyDetailListAdapter(Context context,
                                        List<HXLoanMoneyDetailListItemBean> datas) {
		super();
		this.context = context;
		this.datas = datas;

	}
    public void updateDatas(List<HXLoanMoneyDetailListItemBean> datas)
    {
    	this.datas = datas;
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
			view = LayoutInflater.from(context).inflate(
					R.layout.hxloan_money_detail_list_item_layout, null);

			holder.mTv1 = (TextView) view
					.findViewById(R.id.pay_money_pay_qishu_num_tv);
			holder.mTv2 = (TextView) view
					.findViewById(R.id.pay_money_pay_date_tv);
			holder.mTv3 = (TextView) view
					.findViewById(R.id.pay_money_pay_money_num_tv);
			holder.mTv4 = (TextView) view
					.findViewById(R.id.pay_money_pay_lixi_tv);
			holder.mTv5 = (TextView) view
					.findViewById(R.id.pay_money_pay_state_btn);
			holder.line1 = view.findViewById(R.id.line_1);
			holder.line2 = view.findViewById(R.id.line_2);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (position == (datas.size() - 1)) {
			holder.line1.setVisibility(View.GONE);
			holder.line2.setVisibility(View.VISIBLE);
		} else {
			holder.line1.setVisibility(View.VISIBLE);
			holder.line2.setVisibility(View.GONE);
		}
		final HXLoanMoneyDetailListItemBean item=datas.get(position);
		final String payState = item.getStatus();//还款状态  0:待还款(未还) 1:未还清  2:已还清 3:已逾期
        final String isCurentTerm=item.getIsCurrentTerm();//是否当前  0：当期 1:不是当期
        final String overdueredFlag=item.getOverdueredFlag();//逾期可还标识 00：可还  01:不可还
		holder.mTv1.setText(item.getCurrentPeriod()+"期");//期数
		holder.mTv2.setText(item.getCurrentEndDate());//应还日期
		holder.mTv3.setText("还款金额：" + item.getCurrentPrincipalInterest()+"(元)");//还款金额
		holder.mTv4.setText("含利息、手续费：" + item.getInterestAddFee());//利息、手续费
		holder.mTv5.setText(payState);
		if(overdueCount.equals("1"))
		{
			//有逾期
			if (payState.equals("3")) {
	            holder.mTv5.setText("已逾期");
	            if("00".equals(overdueredFlag))
	            {
	            	//逾期可还
	            	holder.mTv5.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
			        holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_ffffff));	
	            }else
	            {
	            	holder.mTv5.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg_e5e5e5);
					holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_cccccc));
	            }
				
			}else
			{
				 if(payState.equals("0"))
				{
					 if(isCurentTerm.equals("0"))
					 {
						 holder.mTv5.setText("还当期");
					 }else{
						 holder.mTv5.setText("待还款");
					 }
					
				}else if(payState.equals("2"))
				{
					 holder.mTv5.setText("已还清");
					 
				}
				 holder.mTv5.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg_e5e5e5);
				 holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_cccccc));
			}
		}else{
			//没逾期
			if(payState.equals("0"))
			{
				 if(isCurentTerm.equals("0"))
				 {
					 holder.mTv5.setText("还当期");
					 holder.mTv5.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				     holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_ffffff));
				 }else{
					 holder.mTv5.setText("待还款");
					 holder.mTv5.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg_e5e5e5);
					 holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_cccccc));
				 }
				
			}else if(payState.equals("2"))
			{
				 holder.mTv5.setText("已还清");
				 holder.mTv5.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg_e5e5e5);
				 holder.mTv5.setTextColor(context.getResources().getColor(R.color.color_cccccc));
			}
		}
		final String loanMonths= Integer.toString(datas.size());
		holder.mTv5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(overdueCount.equals("1"))
				{
					//有逾期
					if (payState.equals("3"))
					{
						if("00".equals(overdueredFlag))
			            {
							//逾期可还
							Intent intent = new android.content.Intent(context,
									HXPayMoneyPayDetailActivity.class);
							Bundle bundle=new Bundle();//loanMoneyMonthDetail
							bundle.putString("payType","month");
							bundle.putSerializable("loanMoneyMonthDetail",item);
							bundle.putString("loanMonths",loanMonths);//贷款期限
							bundle.putString("loanId",loanId);//贷款Id
						    intent.putExtra("loanMoneyBundle", bundle);
							context.startActivity(intent);
							((Activity)context).finish();
			            }
					}
				}else if(overdueCount.equals("0"))
				{
					//没有逾期
					if(payState.equals("0"))
					{
						 if(isCurentTerm.equals("0"))
						 {
							 //是当期
							 Intent intent = new android.content.Intent(context,
										HXPayMoneyPayDetailActivity.class);
								Bundle bundle=new Bundle();//loanMoneyMonthDetail
								bundle.putString("payType","month");
								bundle.putSerializable("loanMoneyMonthDetail",item);
								bundle.putString("loanMonths",loanMonths);//贷款期限
								bundle.putString("loanId",loanId);//贷款Id
							    intent.putExtra("loanMoneyBundle", bundle);
								context.startActivity(intent);
								((Activity)context).finish();
							}
						 }
						
					}
				}
				
		});
		return view;
	}

	class ViewHolder {

		TextView mTv1, mTv2, mTv3, mTv4, mTv5;
		View line1, line2;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// return position % advertData.size();
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}
}
