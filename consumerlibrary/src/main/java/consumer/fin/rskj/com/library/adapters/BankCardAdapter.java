package consumer.fin.rskj.com.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.module.BankCardItem;
import consumer.fin.rskj.com.library.utils.LogUtils;

public class BankCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final String TAG = BankCardAdapter.class.getSimpleName();
  private Context context;
  private List<BankCardItem> list;
  private OnItemClickListener onItemClickListener;
  private OnItemClickListener addListener;

  private static final int TYPE_NORMAL = 0;
  private static final int TYPE_OTHER = 1;

  public BankCardAdapter(Context context, List<BankCardItem> list) {
    this.context = context;
    this.list = list;
    LogUtils.d(TAG, "------position--------" + list.size());
  }

  @Override
  public int getItemViewType(int position) {
    LogUtils.d(TAG, "------position--------" + position);
    LogUtils.d(TAG, "------size--------" + list.size());

    if (position == list.size() - 1) {
      return TYPE_OTHER;
    }

    return TYPE_NORMAL;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    LogUtils.d(TAG, "------onCreateViewHolder-------" + viewType);

    if (viewType == TYPE_OTHER) {
      View v0 = LayoutInflater.from(context).inflate(R.layout.bankcard_add, parent, false);
      BankCardAdapter.AddViewHolder th0 = new BankCardAdapter.AddViewHolder(v0);
      return th0;
    }

    View v = LayoutInflater.from(context).inflate(R.layout.bank_item, parent, false);
    BankCardAdapter.TViewHolder th = new BankCardAdapter.TViewHolder(v);

    return th;
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

    LogUtils.d("TTTT", "------itemView--onClick---------" + position);
    LogUtils.d("TTTT", "-------type------" + getItemViewType(position));
    if (holder instanceof AddViewHolder) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          addListener.OnItemClick(v, holder, position);
          notifyItemChanged(position);
        }
      });
      return;
    }

    //加载网络图片
    //Picasso.with(mContext).load(datas.get(position).getUrl()).into(((TViewHolder) holder).iv);
    ((TViewHolder) holder).bank_name.setText(list.get(position).getBankName());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.OnItemClick(v, holder, position);
        notifyItemChanged(position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public class TViewHolder extends RecyclerView.ViewHolder {
    public ImageView logo;
    public TextView bank_name;

    public TViewHolder(View itemView) {
      super(itemView);
      logo = (ImageView) itemView.findViewById(R.id.bankLogo);
      bank_name = (TextView) itemView.findViewById(R.id.bank_name);
    }
  }

  public class AddViewHolder extends RecyclerView.ViewHolder {
    public AddViewHolder(View itemView) {
      super(itemView);
    }
  }

  public interface OnItemClickListener {
    void OnItemClick(View view, RecyclerView.ViewHolder holder, int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.onItemClickListener = listener;
  }

  public void setAddListener(OnItemClickListener listener) {
    this.addListener = listener;
  }
}
