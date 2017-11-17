package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.adapters.HXAboutListAdapter;
import com.zhongan.demo.hxin.util.ConUtil;
import com.zhongan.demo.hxin.util.SysUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *关于页面
 */
public class XJAboutActivity extends XJBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;
	private TextView mTitleView,mVersionNameTv;
    private ListView mAboutListView;
   
    private List<Map<String,String>> aboutList;//关于列表
   
    private HXAboutListAdapter adapter;//关于适配
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_about_layout);
		initViews();
	}

	private void initViews() {
		 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		 mTitleView.setText(R.string.about_suixindai_text);
		 mBackBtn.setOnClickListener(this);
		 mAboutListView=(ListView) findViewById(R.id.about_listview);
		 String versionName= ConUtil.getVersionName(getApplicationContext());//应用版本号
		 mVersionNameTv=(TextView) findViewById(R.id.version_tv);
		 mVersionNameTv.setText("随心贷  V"+versionName);
		 aboutList=new ArrayList<Map<String,String>>();
		 Map<String,String> map1=new HashMap<String, String>();
		 map1.put("title", "去评分");
		
		 aboutList.add(map1);
		 Map<String,String> map2=new HashMap<String, String>();
		 map2.put("title", "功能介绍");
		
		
		 aboutList.add(map2);
		 Map<String,String> map3=new HashMap<String, String>();
		 map3.put("title", "系统通知");
	
		 aboutList.add(map3);
		 Map<String,String> map4=new HashMap<String, String>();
		 map4.put("title", "投诉");
		
		 aboutList.add(map4);
		
		 adapter=new HXAboutListAdapter(this, aboutList);
		 mAboutListView.setAdapter(adapter);
		 mAboutListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(XJAboutActivity.this, aboutList.get(position).get("title"), Toast.LENGTH_SHORT).show();
			}
		});
		
		
		 
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJAboutActivity.this.finish();
			break;
		
		default:
			break;
		}
	}

}
