package consumer.fin.rskj.com.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.module.PayTypeItemBean;


public class LoanMoneyPaytypeListAdapter extends BaseAdapter {
	private Context context;
	private List<PayTypeItemBean> datas;

	public LoanMoneyPaytypeListAdapter(Context context,List<PayTypeItemBean> datas) {
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
					R.layout.rskj_loan_money_pay_type_list_item_layout, null);
			
			holder.mTv1=(TextView) view.findViewById(R.id.loan_money_pay_type_pay_date_tv);
			holder.mTv2=(TextView) view.findViewById(R.id.loan_money_pay_type_benjin_tv);
			holder.mTv3=(TextView) view.findViewById(R.id.loan_money_pay_type_lixi_tv);
			holder.mTv4=(TextView) view.findViewById(R.id.loan_money_pay_type_shouxufei_tv);
			holder.mTv5=(TextView) view.findViewById(R.id.loan_money_pay_type_huankuaie_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
		PayTypeItemBean item=datas.get(position);
		
		holder.mTv1.setText(item.getCurrentEndDate());//还款日期
		holder.mTv2.setText(item.getCurrentPrincipal());//本金(应还本金)
		holder.mTv3.setText(item.getCurrentInterest());//利息(应还利息)
		holder.mTv4.setText(item.getCurrentFee());//手续费
		holder.mTv5.setText(item.getCurrentPrincipalInterest());//还款额(应还款总额)
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

