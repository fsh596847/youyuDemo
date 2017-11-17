package com.zhongan.demo.hxin.activitys;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.util.Base64Util;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;


/**
 * 实名验证页面
 */
public class HXTrueNameActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;
	private Button mBackBtn;//返回按钮
	private TextView mTitleView,mResultTipTv,mTrueNameTv,mTrueIdcardTv;
	private ImageView mResultIv,mIdcardfront,mIdcardBack;
	private LinearLayout mAlreadyCheckLl;
    private FrameLayout mNotCheckFl;
    private String isTrueNameChecked;//是否实名认证标识
  
   
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_true_name_layout);
		isTrueNameChecked=getIntent().getStringExtra("isTrueNameChecked");
		initViews();
	}

	@SuppressLint("NewApi") private void initViews() {
		 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);//导航栏标题
		 mTitleView.setText(R.string.app_name);
		 mBackBtn.setOnClickListener(this);
		 mResultIv=(ImageView) findViewById(R.id.true_name_result_iv);//实名认证结果图片
		 mResultTipTv=(TextView) findViewById(R.id.true_name_result_tip_tv);//实名认证结果提示
		 mNotCheckFl=(FrameLayout) findViewById(R.id.true_name_not_check_fl);//未实名认证展示布局
		 mNotCheckFl.setOnClickListener(this);
		 mAlreadyCheckLl=(LinearLayout) findViewById(R.id.true_name_already_check_ll);//已经实名认证展示布局
		 mTrueNameTv=(TextView) findViewById(R.id.true_name_name_tv);//真实姓名
		 mTrueIdcardTv=(TextView) findViewById(R.id.true_name_idcard_tv);
		 mIdcardfront=(ImageView) findViewById(R.id.true_name_idcard_font_iv);//身份证正面照
		 mIdcardBack=(ImageView) findViewById(R.id.true_name_idcard_back_iv);//身份证反面照
		 if(isTrueNameChecked.equals("1"))
		 {
			 //已经实名认证展示
			 Bundle bundle=getIntent().getBundleExtra("idcardInfo");
			 mResultIv.setBackgroundResource(R.drawable.m_icon_alreday_check_true_name);
			 String name=bundle.getString("name");
			 String idcardNum=bundle.getString("id_no");
			 String idCardFrontPic=bundle.getString("custCardFront");
			 String idCardBackPic=bundle.getString("custCardVerso");
			 mAlreadyCheckLl.setVisibility(View.VISIBLE);
			 mResultTipTv.setText(R.string.true_name_success_text);//实名认证结果提示
			 mTrueNameTv.setText(name);//设置姓名
			 mTrueIdcardTv.setText(idcardNum);//设置身份证号
			 LoggerUtil.debug("idCardFrontPic----------------->"+idCardFrontPic);
			 LoggerUtil.debug("idCardBackPic----------------->"+idCardBackPic);
			 if(idCardFrontPic==null||idCardFrontPic.length()==0)
			 {
				 return;
			 }
			 if(idCardBackPic==null||idCardBackPic.length()==0)
			 {
				 return;
			 }
			 Bitmap frontBitmap= Base64Util.base64ToBitmap(idCardFrontPic);
			 mIdcardfront.setImageBitmap(frontBitmap);
			 Bitmap backBitmap= Base64Util.base64ToBitmap(idCardBackPic);
			 mIdcardBack.setImageBitmap(backBitmap);
			 mNotCheckFl.setVisibility(View.GONE);
		 }
		 else  if(isTrueNameChecked.equals("0"))
		 {
			 //未实名认证展示
			 mNotCheckFl.setVisibility(View.GONE);
			 mResultIv.setBackgroundResource(R.drawable.m_icon_not_check_true_name);
			 mResultTipTv.setText(R.string.true_name_failure_text);
			 mAlreadyCheckLl.setVisibility(View.GONE);
		 }
//		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			HXTrueNameActivity.this.finish();
			break;
		case R.id.true_name_not_check_fl:
			//未实名认证展示布局
			//身份证上传
			Intent upoadIntent=new Intent(HXTrueNameActivity.this,HXFaceIDCardInfoUploadActivity.class);
			upoadIntent.putExtra("option","centerUpload");
			startActivity(upoadIntent);
			break;
		default:
			break;
		}
	}

}
