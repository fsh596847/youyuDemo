package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.fragments.HXShopFragment;
import com.zhongan.demo.hxin.fragments.HXShouYeFragment;
import com.zhongan.demo.hxin.fragments.HXZhangHuFragment;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.util.ToastUtils;


/**
 * 控制页
 */
@SuppressLint("NewApi")
public class HXMainActivity extends HXBaseActivity implements OnClickListener {
	private FrameLayout mTabFrameLayout;
	public RadioGroup mTabRadioGroup;
	private RadioButton mTabShouYe;// 首页
	private RadioButton mTabShop;// 分期商店
	private RadioButton mTabZhangHu;// 我的账户
	private Fragment mShouYeFragment;
	private Fragment mShopFragment;
	private Fragment mZhangHuFragment;
	
	private long curMillions = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_main);
		initViews();
		initEvents();
		mTabRadioGroup.check(R.id.fragment_bottom_bar_tab1);
		select(0);
		
		
		/**
		 * 获取设备的The IMEI
		 * */
//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//		String deviceId = TelephonyMgr.getDeviceId();
//		LoggerUtil.debug("deviceId1------>"+deviceId);
//		sharePrefer.setDeviceId(deviceId);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_bottom_bar_tab1:
			//首页
			mTabRadioGroup.check(R.id.fragment_bottom_bar_tab1);
			break;
		case R.id.fragment_bottom_bar_tab2:
			//分期商城
			mTabRadioGroup.check(R.id.fragment_bottom_bar_tab2);
			break;
		case R.id.fragment_bottom_bar_tab3:
			//我的账户
			mTabRadioGroup.check(R.id.fragment_bottom_bar_tab3);
			break;
		default:
			break;
		}

	}
    @Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	int flag=intent.getIntExtra("flag", 0);
    	if(flag > 2 || flag < 0)
    	{
    		select(0);
    	}/*else {
    		select(flag);
    	}*/
    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if(!getSharePrefer().iSLogin()){
			ToastUtils.showCenterToast("没有登录",this);
//			Intent intent = new Intent(this,HXLoginActivity.class);
//			startActivity(intent);

			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
		}
    }
    
	// 初始化控件
	private void initViews() {
		mTabFrameLayout = (FrameLayout) findViewById(R.id.fragment_bottom_bar);
		mTabRadioGroup = (RadioGroup) findViewById(R.id.fragment_bottom_bar_rg);
		mTabShouYe = (RadioButton) findViewById(R.id.fragment_bottom_bar_tab1);
		mTabShop = (RadioButton) findViewById(R.id.fragment_bottom_bar_tab2);
		mTabZhangHu = (RadioButton) findViewById(R.id.fragment_bottom_bar_tab3);

	}

	// 初始化 监听事件
	private void initEvents() {
		mTabRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup rg, int chechedId) {
						switch (chechedId) {
						case R.id.fragment_bottom_bar_tab1:
							//首页
							select(0);
							break;
						case R.id.fragment_bottom_bar_tab2:
							//分期商城
							select(1);
							break;
						case R.id.fragment_bottom_bar_tab3:
							//我的账户
							select(2);
							break;
						default:
							break;
						}
					}
				});
		mTabShouYe.setOnClickListener(this);
		mTabShop.setOnClickListener(this);
		mTabZhangHu.setOnClickListener(this);
	}

	//
	private void select(int i) {
		FragmentManager fm = this.getFragmentManager(); // 获得Fragment管理器
		FragmentTransaction ft = fm.beginTransaction(); // 开启一个事务
		hidtFragment(ft); // 先隐藏 Fragment

		switch (i) {
		case 0:

			if (mShouYeFragment == null) {
				mShouYeFragment = new HXShouYeFragment();
				ft.add(R.id.fragment_container, mShouYeFragment);
			} else {
				ft.show(mShouYeFragment);
			}
			break;
		case 1:

			if (mShopFragment == null) {
				mShopFragment = new HXShopFragment();
				ft.add(R.id.fragment_container, mShopFragment);
			} else {
				ft.show(mShopFragment);
			}
			break;
		case 2:

			if (mZhangHuFragment == null) {
				mZhangHuFragment = new HXZhangHuFragment();
				ft.add(R.id.fragment_container, mZhangHuFragment);
			} else {
				ft.show(mZhangHuFragment);
			}
			break;
		}
		ft.commit(); // 提交事务
	}

	// 隐藏所有Fragment
	private void hidtFragment(FragmentTransaction fragmentTransaction) {
		if (mShouYeFragment != null) {
			fragmentTransaction.hide(mShouYeFragment);
		}
		if (mShopFragment != null) {
			fragmentTransaction.hide(mShopFragment);
		}
		if (mZhangHuFragment != null) {
			fragmentTransaction.hide(mZhangHuFragment);
		}
	}

	
	  @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

	            long millions = System.currentTimeMillis();
	            if(millions-curMillions > 2000){
	                curMillions = millions;
	                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	            }else{
	            	ActivityStackManagerUtils.getInstance().ExitApplication(getApplicationContext());
	            }
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	  
}
