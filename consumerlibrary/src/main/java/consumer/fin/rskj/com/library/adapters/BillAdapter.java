package consumer.fin.rskj.com.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.module.BillItem;
import consumer.fin.rskj.com.library.utils.LogUtils;

/**
 * 账单适配器
 **/
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.TViewHolder> {

  private Context context;

  private List<BillItem> list;

  private OnItemClickListener onItemClickListener;
  private OnItemClickListener onChexboxListener;

  private OnItemRemoveListner OnItemRemoveListner;
  private static final int TYPE_NORMAL = 0;
  private static final int TYPE_SOLDOUT = 1;

  boolean isDelay;

  public BillAdapter(Context context, List<BillItem> list, boolean isDelay) {
    this.context = context;
    this.list = list;
    this.isDelay = isDelay;
  }

  @Override
  public int getItemViewType(int position) {
    //		if(list.get(position).getCurrentEndDate().equals("123")){
    //			return TYPE_SOLDOUT;
    //		}

    return TYPE_NORMAL;
  }

  @Override
  public BillAdapter.TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    //		if(viewType == TYPE_SOLDOUT ){
    //
    //		}

    View v = LayoutInflater.from(context).inflate(R.layout.rskj_bill_item, parent, false);
    BillAdapter.TViewHolder th = new BillAdapter.TViewHolder(v);

    return th;
  }

  @Override
  public void onBindViewHolder(final BillAdapter.TViewHolder holder, final int position) {

    BillItem billItem = list.get(position);

    if (getItemViewType(position) == TYPE_SOLDOUT) {
      holder.check.setVisibility(View.INVISIBLE);
      holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
      return;
    }

    holder.check.setChecked(billItem.getCheck());

    if (isDelay) {
      holder.check.setVisibility(View.VISIBLE);
      holder.overInt.setVisibility(View.VISIBLE);
      holder.status.setText("已逾期");
    } else {
      holder.check.setVisibility(View.GONE);
      holder.status.setText("");
      holder.overInt.setVisibility(View.GONE);
    }

    holder.currentPeriod_period.setText(
        billItem.getCurrentPeriod() + "/" + billItem.getPeriod() + "期");

    holder.currentEndDate.setText(billItem.getCurrentEndDate());
    //textView.setText(Html.fromHtml(String.format(getString(R.string.delete_bank),"9999")));
    holder.totalPay.setText(
        Html.fromHtml(String.format(context.getString(R.string.totalPay), billItem.getTotalPay())));
    holder.currentInterest.setText("含利息:" + billItem.getCurrentInterest());
    holder.overInt.setText("罚息:" + billItem.getOverInt());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LogUtils.d(BillAdapter.class.getSimpleName(),
            "------itemView--onClick---------" + position);
        onItemClickListener.OnItemClick(v, holder, position);
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
        LogUtils.d(BillAdapter.class.getSimpleName(), "-----check---onClick---------");
        onChexboxListener.OnItemClick(v, holder, position);
        notifyItemChanged(position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public void checkAll() {
    for (int i = 0; i < this.getItemCount(); i++) {

    }
  }

  public class TViewHolder extends RecyclerView.ViewHolder {
    public CheckBox check;
    public TextView currentPeriod_period;
    public TextView status;

    public TextView currentEndDate;
    public TextView totalPay;
    public TextView currentInterest;
    public TextView overInt;

    public TViewHolder(View itemView) {
      super(itemView);
      check = (CheckBox) itemView.findViewById(R.id.checkbox);
      currentPeriod_period = (TextView) itemView.findViewById(R.id.currentPeriod_period);
      status = (TextView) itemView.findViewById(R.id.status);

      currentEndDate = (TextView) itemView.findViewById(R.id.currentEndDate);
      totalPay = (TextView) itemView.findViewById(R.id.totalPay);
      currentInterest = (TextView) itemView.findViewById(R.id.currentInterest);
      overInt = (TextView) itemView.findViewById(R.id.overInt);
    }
  }

  public interface OnItemClickListener {
    void OnItemClick(View view, BillAdapter.TViewHolder holder, int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.onItemClickListener = listener;
  }

  public void setCheckboxClickListener(OnItemClickListener listener) {
    this.onChexboxListener = listener;
  }

  public interface OnItemRemoveListner {
    void OnItemRemoveListner(View view, int position);
  }

  public void setOnItemRemoveListner(OnItemRemoveListner onItemRemoveListner) {
    this.OnItemRemoveListner = onItemRemoveListner;
  }
}
