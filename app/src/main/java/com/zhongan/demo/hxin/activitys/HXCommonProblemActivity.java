package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.adapters.HXCommonProblemListAdapter;
import com.zhongan.demo.hxin.util.SysUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *常见问题页面
 */
public class HXCommonProblemActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;
	private TextView mTitleView;
    private ListView mProblemListView;
    private RadioGroup mProblemRg;
    private RadioButton mZhenxinRb,mHetongRb;
    private List<Map<String,String>> zhengxinProblem;//征信问题列表
    private List<Map<String,String>> hetongProblem;//合同条款列表
    private HXCommonProblemListAdapter adapter;//征信问题、合同条款适配
    private boolean isZhengxinProblem=true;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_common_problem_layout);
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
		 mTitleView.setText(R.string.common_problem_text);
		 mBackBtn.setOnClickListener(this);
		 mProblemRg=(RadioGroup) findViewById(R.id.common_problem_rg);
		 mZhenxinRb=(RadioButton) findViewById(R.id.common_problem_zhenxin_btn);//征信相关
		 mHetongRb=(RadioButton) findViewById(R.id.common_problem_hetong_btn);//合同条款
		 mProblemListView=(ListView) findViewById(R.id.common_problem_listview);
		 mZhenxinRb.setOnClickListener(this);
		 mHetongRb.setOnClickListener(this);
		 zhengxinProblem=new ArrayList<Map<String,String>>();
		 Map<String,String> map1=new HashMap<String, String>();
		 map1.put("title1", "什么是随心贷？");
		 map1.put("title2", "简介");
	
		 zhengxinProblem.add(map1);
		 Map<String,String> map2=new HashMap<String, String>();
		 map2.put("title1", "额度为什么这么少？");
		 map2.put("title2", "额度");
		
		 zhengxinProblem.add(map2);
		 Map<String,String> map3=new HashMap<String, String>();
		 map3.put("title1", "如何重置密码？");
		 map3.put("title2", "重置密码");
		 
		 zhengxinProblem.add(map3);
		 Map<String,String> map4=new HashMap<String, String>();
		 map4.put("title1", "提现收不收手续费？");
		 map4.put("title2", "不收手续费");
		
		 zhengxinProblem.add(map4);
		
		 adapter=new HXCommonProblemListAdapter(this, zhengxinProblem);
		 mProblemListView.setAdapter(adapter);
		 
		 hetongProblem=new ArrayList<Map<String,String>>();
		 Map<String,String> paymap1=new HashMap<String, String>();
		 paymap1.put("title1", "随心贷使用协议");
		 paymap1.put("title2", "随心贷");
		
		 hetongProblem.add(paymap1);
		 Map<String,String> paymap2=new HashMap<String, String>();
		 paymap2.put("title1", "征信相关协议");
		 paymap2.put("title2", "征信");
		
		
		 hetongProblem.add(paymap2);
		 Map<String,String> paymap3=new HashMap<String, String>();
		 paymap3.put("title1", "用户使用协议");
		 paymap3.put("title2", "使用");
		 
		 hetongProblem.add(paymap3);
		
		 
		 mProblemRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId) {
				// TODO Auto-generated method stub
				switch (checkId) {
				case R.id.common_problem_zhenxin_btn:
					//征信相关
					 isZhengxinProblem=true;
					 adapter=new HXCommonProblemListAdapter(HXCommonProblemActivity.this, zhengxinProblem);
					 mProblemListView.setAdapter(adapter);
					break;
				case R.id.common_problem_hetong_btn:
					//合同条款
					isZhengxinProblem=false;
					adapter=new HXCommonProblemListAdapter(HXCommonProblemActivity.this, hetongProblem);
				    mProblemListView.setAdapter(adapter);
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			HXCommonProblemActivity.this.finish();
			break;
		case R.id.common_problem_zhenxin_btn:
			//征信相关
			mProblemRg.check(R.id.common_problem_zhenxin_btn);
			break;
		case R.id.common_problem_hetong_btn:
			//合同条款
			mProblemRg.check(R.id.common_problem_hetong_btn);
			break;
		default:
			break;
		}
	}

}
