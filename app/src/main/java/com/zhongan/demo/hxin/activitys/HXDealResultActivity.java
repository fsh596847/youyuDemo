package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.util.SysUtil;


/**
 * 验证结果页面
 */
public class HXDealResultActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn,mSucessRetrunBtn,mFailureRetrunBtn,mCheckThreeBtn;
	private TextView mTitleView,mResultTipTv,mResultTv;
    private ImageView mResultIv;
    private LinearLayout mFailureBtnLL;
    private String checkResult="";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_result_layout);
		checkResult=getIntent().getStringExtra("checkResult");
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
		 mTitleView.setText(R.string.check_bank_title_text);
		 mBackBtn.setOnClickListener(this);
		 mResultIv=(ImageView) findViewById(R.id.deal_result_iv);
		 mResultTipTv=(TextView) findViewById(R.id.deal_result_tip_tv);
		 mResultTv=(TextView) findViewById(R.id.deal_result_tv);
		 mSucessRetrunBtn=(Button) findViewById(R.id.result_sucess_go_shouye_btn);//验证结果成功返回按键
		 mSucessRetrunBtn.setOnClickListener(this);
		 mFailureBtnLL=(LinearLayout) findViewById(R.id.result_failure_ll);
		 mFailureRetrunBtn=(Button) findViewById(R.id.result_failure_go_shouye_btn);//验证结果失败返回按键
		 mFailureRetrunBtn.setOnClickListener(this);
		 mCheckThreeBtn=(Button) findViewById(R.id.result_failure_check_three_btn);//验证结果失败开启验证三
		 mCheckThreeBtn.setOnClickListener(this);
		 if(checkResult.equals("failure"))
		 {
			 String errorTip=getIntent().getStringExtra("retrunMsg");
			 mResultIv.setBackgroundResource(R.drawable.m_icon_deal_failur);
			 mResultTipTv.setText(R.string.reslt_failure_tip_text);
			 mResultTv.setText(errorTip);
			 mFailureBtnLL.setVisibility(View.VISIBLE);
			 mSucessRetrunBtn.setVisibility(View.GONE);
			 mCheckThreeBtn.setVisibility(View.GONE);
		 }else
		 {
			 mResultIv.setBackgroundResource(R.drawable.m_icon_deal_sucess);
			 mResultTipTv.setText(R.string.reslt_success_tip_text);
			 mResultTv.setText(R.string.reslt_success_text);
			 mFailureBtnLL.setVisibility(View.GONE);
			 mSucessRetrunBtn.setVisibility(View.VISIBLE);
		 }
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			if(checkResult.equals("failure"))
			{
				//失败返回银行卡验证页面
				HXDealResultActivity.this.finish();
			}else
			{

			}
			
			break;
		case R.id.result_sucess_go_shouye_btn:
		case R.id.result_failure_go_shouye_btn:
			//返回首页
			break;
		case R.id.result_failure_check_three_btn:
			//开启验三
			Intent checkIntent = new Intent(HXDealResultActivity.this,HXFaceIDCardInfoUploadActivity.class);
			checkIntent.putExtra("option","checkThree");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
			startActivity(checkIntent);
			break;
		default:
			break;
		}
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode== KeyEvent.KEYCODE_BACK)
    	{
    		if(checkResult.equals("failure"))
			{
				//失败返回银行卡验证页面
				HXDealResultActivity.this.finish();
			}else
			{
				//成功返回首页
			}
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
