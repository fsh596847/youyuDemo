package com.zhongan.demo.hxin.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.umeng.analytics.MobclickAgent;
import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.util.ConUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.util.LogUtils;

import org.json.JSONObject;

/**
 * 人脸识别起始页
 **/
public class HXFaceStartActivity extends HXBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;//返回按钮
	private TextView mTitleView;//标题

	private String uuid;
	private LinearLayout mWarringBarLL,mFaceTipLL;
	private Button mStartFaceBtn;
	private TextView mWarringTv;//网络检测提示
	private ProgressBar mWarrantyBar;//网络检测进度
	private Button againWarringBtn;//网络检测按钮

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_face_start_layout);

		init();
		netWorkWarranty();
	}

	private void init() {
		mStatusView=findViewById(R.id.status_bar_view);
		int statusHeight= SysUtil.getStatusBarHeight(this);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
		params.height=statusHeight;
		mStatusView.setLayoutParams(params);
		mBackBtn=(Button) findViewById(R.id.left_btn);
		mTitleView=(TextView) findViewById(R.id.center_title);
		mTitleView.setText(R.string.face_text);
		mBackBtn.setOnClickListener(this);
		uuid = ConUtil.getUUIDString(this);//设备uuid
		// 设备号
		String deviceId = ConUtil.getDeviceID(this);
		// app版本号
		String version = ConUtil.getVersionName(this);
		Log.v("tag", "uuid-------------->"+uuid);
		mFaceTipLL=(LinearLayout) findViewById(R.id.face_face_tip_ll);
		mWarringBarLL = (LinearLayout) findViewById(R.id.start_face_warring_bar_ll);
		mWarringTv = (TextView) findViewById(R.id.start_face_warring_tv);
		mWarrantyBar = (ProgressBar) findViewById(R.id.start_face_warring_bar);
		againWarringBtn = (Button) findViewById(R.id.start_face_again_warring_btn);
		againWarringBtn.setOnClickListener(this);
		mStartFaceBtn = (Button) findViewById(R.id.face_start_liveness_btn);//开始检测按钮
		mStartFaceBtn.setOnClickListener(this);

	}

	/**
	 * 联网授权
	 */
	private void netWorkWarranty() {

		mStartFaceBtn.setVisibility(View.GONE);
		mWarringBarLL.setVisibility(View.VISIBLE);
		againWarringBtn.setVisibility(View.GONE);
		mWarringTv.setText("正在联网授权中...");
		mWarrantyBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Manager manager = new Manager(HXFaceStartActivity.this);
				LivenessLicenseManager licenseManager = new LivenessLicenseManager(HXFaceStartActivity.this);
				manager.registerLicenseManager(licenseManager);
				manager.takeLicenseFromNetwork(uuid);
				if (licenseManager.checkCachedLicense() > 0)
					//可以开始检测
					mHandler.sendEmptyMessage(1);
				else
					//网络问题，需重新联网
					mHandler.sendEmptyMessage(2);
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.left_btn:
				//返回
				Intent intent = new Intent(this, MenuListActivity2.class);
				startActivity(intent);
				break;
			case R.id.face_start_liveness_btn:
				//开始检测
				startActivityForResult(new Intent(this, HXFaceActivity.class), PAGE_INTO_LIVENESS);
				break;
			case R.id.start_face_again_warring_btn:
				//网络检查，是否连接face++
				netWorkWarranty();
			default:
				break;
		}
	}

	private static final int PAGE_INTO_LIVENESS = 100;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//获取活体检测结果，并将结果发送到结果页
		if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
			String result = data.getStringExtra("result");

			LogUtils.Log("TTTT","result = " + result);
//			HXFaceResultActivity.startActivity(this, result);

			try {
				JSONObject object = new JSONObject(result);
				int resID = object.getInt("resultcode");
				if (resID == R.string.verify_success) {
					Intent uploadIntent = new Intent(this,   HXBasicSelfInfoActivity.class);
//				uploadIntent.putExtra("name", "");
//				uploadIntent.putExtra("idnumber", "");
					startActivity(uploadIntent);
				}else {
					HXFaceResultActivity.startActivity(this, result);
				}

			}catch (Exception e){

				HXFaceResultActivity.startActivity(this, result);
			}

			boolean isSuccess = result.equals(getResources().getString(R.string.verify_success));
			//成功直接跳转到基本信息，失败跳转失败页面 可以重试


		}

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					mStartFaceBtn.setVisibility(View.VISIBLE);
					mWarringBarLL.setVisibility(View.GONE);
					break;
				case 2:
					againWarringBtn.setVisibility(View.VISIBLE);
					mWarringTv.setText(R.string.link_network_error);
					mWarrantyBar.setVisibility(View.GONE);
					break;
			}
		}
	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this, MenuListActivity2.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
