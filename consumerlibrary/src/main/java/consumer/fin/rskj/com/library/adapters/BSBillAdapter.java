package consumer.fin.rskj.com.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.module.BillItem;
import consumer.fin.rskj.com.library.module.BystatdgeBillItem;
import consumer.fin.rskj.com.library.utils.LogUtils;

/**
 * 账单适配器
 **/
public class BSBillAdapter extends RecyclerView.Adapter<BSBillAdapter.TViewHolder>{

	private Context context;

	private List<BystatdgeBillItem> list;

	private OnItemClickListener onItemClickListener;
	private OnItemClickListener onChexboxListener;

	private OnItemRemoveListner OnItemRemoveListner;
	private static final int TYPE_NORMAL = 0;
	private static final int TYPE_SOLDOUT = 1;

	private boolean isOutData = false;

	public BSBillAdapter(Context context, List<BystatdgeBillItem> list,boolean isOutData) {
		this.context = context;
		this.list = list;
		this.isOutData = isOutData;
	}


	@Override
	public int getItemViewType(int position) {
//		if(list.get(position).getCurrentEndDate().equals("123")){
//			return TYPE_SOLDOUT;
//		}

		return TYPE_NORMAL;
	}

	@Override
	public BSBillAdapter.TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//		if(viewType == TYPE_SOLDOUT ){
//
//		}
		LogUtils.d("viewType","viewType = " + viewType);
		View v = LayoutInflater.from(context).inflate(R.layout.bill_item,parent,false);
		BSBillAdapter.TViewHolder th = new BSBillAdapter.TViewHolder(v);

		return th;
	}


	@Override
	public void onBindViewHolder(final BSBillAdapter.TViewHolder holder, final int position) {

		BystatdgeBillItem billItem = list.get(position);

//		if(getItemViewType(position) == TYPE_SOLDOUT) {
//			holder.check.setVisibility(View.GONE);
//			holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
//			return;
//		}

		holder.check.setChecked(billItem.getCheck());
		if(isOutData){
			holder.check.setVisibility(View.VISIBLE);
		}else {
			holder.check.setVisibility(View.GONE);
		}

		holder.currentPeriod_period.setText(billItem.getCurrentPeriod() + "/" + billItem.getRowsCount() + "期");

		holder.currentEndDate.setText(billItem.getCurrentEndDate());
		//textView.setText(Html.fromHtml(String.format(getString(R.string.delete_bank),"9999")));
		if("2".equals(billItem.getStatus())){
			holder.totalPay.setText("已还清");
			if(isOutData){
				holder.check.setVisibility(View.INVISIBLE);
				holder.check.setChecked(false);
			}else {
				holder.check.setVisibility(View.GONE);
				holder.check.setChecked(false);
			}
			holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_gray));

			holder.currentInterest.setText("还款金额:" + billItem.getCurrentPrincipalInterest() + "(含利息:" + billItem.getCurrentInterest()+")");

		}else {
			holder.totalPay.setText(Html.fromHtml(String.format(context.getString(R.string.totalPay),billItem.getCurrentPrincipalInterest())));
			holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
			holder.currentInterest.setText("含利息:" + billItem.getCurrentInterest());
		}

//		holder.currentInterest.setText("含利息:" + billItem.getCurrentInterest());
		if(isOutData){
			holder.overInt.setText("罚息:" + billItem.getOverInt());
		}else {
			holder.overInt.setVisibility(View.GONE);
		}

		holder.array.setVisibility(View.INVISIBLE);
		//只有逾期 还有未还的时候 才需要添加
		if("3".equals(billItem.getStatus())){
			holder.status.setText("已逾期");

		} else {
			holder.status.setText("");
		}


		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtils.d("TTTT333","------itemView--onClick---------"+position);
				onItemClickListener.OnItemClick(v,holder,position);
				notifyItemChanged(position);
			}
		});


//		holder.remove.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(OnItemRemoveListner !=null){
//					OnItemRemoveListner.OnItemRemoveListner(v,position);
//				}
//			}
//		});

//		holder.check.setOnClickListener(null);
		holder.check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtils.d("TTTTAAA","-----check---onClick---------");
				onChexboxListener.OnItemClick(v,holder,position);
				notifyItemChanged(position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public void checkAll(){
		for (int i=0;i<this.getItemCount();i++){

		}
	}

	public class TViewHolder extends RecyclerView.ViewHolder{
		public CheckBox check;
		public TextView currentPeriod_period;
		public TextView status;

		public TextView currentEndDate;
		public TextView totalPay;
		public TextView currentInterest;
		public TextView overInt;
		public ImageView array;


		public TViewHolder(View itemView) {
			super(itemView);
			check = (CheckBox)itemView.findViewById(R.id.checkbox);
			currentPeriod_period = (TextView)itemView.findViewById(R.id.currentPeriod_period);
			status = (TextView)itemView.findViewById(R.id.status);

			currentEndDate = (TextView)itemView.findViewById(R.id.currentEndDate);
			totalPay = (TextView)itemView.findViewById(R.id.totalPay);
			currentInterest = (TextView)itemView.findViewById(R.id.currentInterest);
			overInt = (TextView)itemView.findViewById(R.id.overInt);

			array = (ImageView) itemView.findViewById(R.id.array);
		}
	}

	public interface OnItemClickListener {
		void OnItemClick(View view, BSBillAdapter.TViewHolder holder, int position);
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.onItemClickListener = listener;
	}

	public void setCheckboxClickListener(OnItemClickListener listener){
		this.onChexboxListener = listener;
	}

	public interface OnItemRemoveListner {
		void OnItemRemoveListner(View view, int position);
	}

	public void setOnItemRemoveListner(OnItemRemoveListner onItemRemoveListner) {
		this.OnItemRemoveListner = onItemRemoveListner;
	}

}
