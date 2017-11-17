package com.zhongan.demo.hxin.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;


public class HXGuideActivity extends HXBaseActivity {

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {

			switch (msg.what){
			case 0:
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
//		sharePrefer.setDeviceId(szImei);
//		LoggerUtil.debug("deviceId3------>"+szImei);

		handler.sendEmptyMessageDelayed(0, 2000);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		handler=null;
	}

}
