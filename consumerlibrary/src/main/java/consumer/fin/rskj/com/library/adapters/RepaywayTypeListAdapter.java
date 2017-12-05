package consumer.fin.rskj.com.library.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import consumer.fin.rskj.com.consumerlibrary.R;


public class RepaywayTypeListAdapter extends BaseAdapter {
	private Context context;
	private int[] picData;
	private String[] titles;

	public RepaywayTypeListAdapter(Context context, String[] titles) {
		super();
		this.context = context;
		this.titles=titles;

	}

	@Override
	public int getCount() {
		// Infinite loop
		return titles.length;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view =  LayoutInflater.from(context).inflate(
					R.layout.rskj_repayway_type_pop_list_item_layout, null);
			holder.mLine1 =  view.findViewById(R.id.line_one);
			holder.mLine2= view.findViewById(R.id.line_two);
			holder.mTitle=(TextView) view.findViewById(R.id.line_name_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
       
		
		holder.mTitle.setText(titles[position]);
		if(position==(titles.length-1))
		{
			holder.mLine1.setVisibility(View.VISIBLE);
			holder.mLine2.setVisibility(View.GONE);
		}else
		{
			holder.mLine1.setVisibility(View.GONE);
			holder.mLine2.setVisibility(View.VISIBLE);
		}
		
		return view;
	}

	class ViewHolder {

        View mLine1,mLine2;
		TextView mTitle;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return position % advertData.size();
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}
}

