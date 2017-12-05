package consumer.fin.rskj.com.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.module.BankCardItem;

/**
 * 选择银行适配器
 **/
public class ChooseBankAdapter extends RecyclerView.Adapter<ChooseBankAdapter.TViewHolder>{

	private Context context;

	private List<BankCardItem> list;

	private OnItemClickListener onItemClickListener;
	private OnItemClickListener onChexboxListener;

	private static final int TYPE_NORMAL = 0;
	private static final int TYPE_SOLDOUT = 1;

	String selected;


	public ChooseBankAdapter(Context context, List<BankCardItem> list,String selected) {
		this.context = context;
		this.list = list;
		this.selected = selected;
	}

	@Override
	public int getItemViewType(int position) {
		if(list.get(position).getBankName().equals("123")){
			return TYPE_SOLDOUT;
		}
		return TYPE_NORMAL;
	}

	@Override
	public ChooseBankAdapter.TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(context).inflate(R.layout.choosebank_item,parent,false);
		ChooseBankAdapter.TViewHolder th = new ChooseBankAdapter.TViewHolder(v);

		return th;
	}


	@Override
	public void onBindViewHolder(final ChooseBankAdapter.TViewHolder holder, final int position) {

		Log.d("ChooseBankAdapter","getBankName = " + list.get(position).getBankName());
		Log.d("ChooseBankAdapter","selected = " + selected);

		if(!TextUtils.isEmpty(selected) && selected.equals(list.get(position).getBankCode())){
      holder.check.setImageResource(R.mipmap.rskj_bank_checked);
    }else {
      holder.check.setImageResource(R.mipmap.rskj_bank_unchecked);
    }

//		if(list.get(position).getCheck()){
//			holder.check.setImageResource(R.mipmap.b_checked);
//		}else {
//			holder.check.setImageResource(R.mipmap.b_unchecked);
//		}

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClickListener.OnItemClick(v,holder,position);
				notifyItemChanged(position);
			}
		});

//		holder.check.setOnClickListener(null);
//		holder.check.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LogUtils.d("TTTTAAA","-----check---onClick---------");
//				onChexboxListener.OnItemClick(v,holder,position);
//				notifyItemChanged(position);
//			}
//		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}


	public void disCheckAll(){

		for(BankCardItem bankCardItem : list){
			bankCardItem.setCheck(false);
		}

		notifyDataSetChanged();
	}


	public class TViewHolder extends RecyclerView.ViewHolder{
		public ImageView check;
		public TViewHolder(View itemView) {
			super(itemView);
			check = (ImageView)itemView.findViewById(R.id.checkbox);
//			price = (TextView)itemView.findViewById(R.id.pricea);
//			remove = (Button)itemView.findViewById(R.id.remove);

		}
	}

	public interface OnItemClickListener {
		void OnItemClick(View view, ChooseBankAdapter.TViewHolder holder, int position);
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.onItemClickListener = listener;
	}

	public void setCheckboxClickListener(OnItemClickListener listener){
		this.onChexboxListener = listener;
	}

}
