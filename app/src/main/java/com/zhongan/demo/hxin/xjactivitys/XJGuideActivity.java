package com.zhongan.demo.hxin.xjactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.util.LoggerUtil;


public class XJGuideActivity extends XJBaseActivity {

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {

			switch (msg.what){
			case 0:
				
				Intent intent = new Intent(XJGuideActivity.this,XJMainActivity.class);
				startActivity(intent);
				
				finish();
				
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.hxactivity_guide);
		
//		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//		String szImei = TelephonyMgr.getDeviceId();
		String szImei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		sharePrefer.setDeviceId(szImei);
		LoggerUtil.debug("deviceId3------>"+szImei);

		handler.sendEmptyMessageDelayed(0, 2000);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		handler=null;
	}

}
