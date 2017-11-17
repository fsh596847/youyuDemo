package com.zhongan.demo.hxin.activitys;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.adapters.HXAllPayBanksListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HXAllPayBanksActivity extends HXBaseActivity implements OnClickListener {
	private ImageView mCloseBtn;
	private ListView mListView;
	private List<Map<String,String>> banksData;
	private HXAllPayBanksListAdapter adapter;
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxall_pay_banks_pop_layout);
		initViews();
	}
   private void initViews()
   {
	   banksData=new ArrayList<Map<String,String>>();
	   Map<String,String> map1=new HashMap<String, String>();
		 String bankLogo= String.valueOf(R.drawable.m_icon_jianhang_logo);
		 map1.put("bankLogo",bankLogo);//银行logo
		 map1.put("bankName","中国建设银行储蓄卡 (8101)");//银行名称
		 
		 banksData.add(map1);
		 Map<String,String> map2=new HashMap<String, String>();
		
		 map2.put("bankLogo",bankLogo);//银行logo
		 map2.put("bankName","中信银行储蓄卡 (7533)");//银行名称
		 banksData.add(map2);
		
	   mCloseBtn=(ImageView) findViewById(R.id.basic_all_banks_close);
	   mCloseBtn.setOnClickListener(this);
	   mListView=(ListView) findViewById(R.id.basic_all_banks_listview);
	   adapter=new HXAllPayBanksListAdapter(this, banksData);
	   adapter.setSelectedPosition(0);
	   adapter.notifyDataSetInvalidated();
	   mListView.setAdapter(adapter);
	   mListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
			// TODO Auto-generated method stub
			adapter.setSelectedPosition(position);
			adapter.notifyDataSetInvalidated();
		}
	   });
		
   }
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.basic_all_banks_close:
			HXAllPayBanksActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
