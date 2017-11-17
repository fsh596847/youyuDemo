package com.zhongan.demo.hxin.xjactivitys;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.util.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 人脸识别结果页
 */
public class XJFaceResultActivity extends XJBaseActivity implements
		View.OnClickListener {

	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;// 返回按钮
	private TextView mTitleView;// 标题

	private TextView mResultTv;
	private ImageView mResultImageView;
	private Button mResultFinishBtn, mResultAgainBtn;
	private LinearLayout mFaceTipLL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_face_result);
		init();
	}

	private void init() {
		mStatusView = findViewById(R.id.status_bar_view);
		int statusHeight = SysUtil.getStatusBarHeight(this);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
		params.height = statusHeight;
		mStatusView.setLayoutParams(params);
		mBackBtn = (Button) findViewById(R.id.left_btn);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText(R.string.face_text);
		mBackBtn.setOnClickListener(this);
		mResultImageView = (ImageView) findViewById(R.id.face_tip_icon);
		mResultTv = (TextView) findViewById(R.id.face_tip_result);
		mFaceTipLL=(LinearLayout) findViewById(R.id.face_tip_ll_one);
		mFaceTipLL.setVisibility(View.GONE);
		mResultFinishBtn = (Button) findViewById(R.id.face_result_finish);
		mResultFinishBtn.setVisibility(View.GONE);
		mResultAgainBtn = (Button) findViewById(R.id.face_result_again);
		mResultAgainBtn.setVisibility(View.GONE);
		mResultFinishBtn.setOnClickListener(this);
		mResultAgainBtn.setOnClickListener(this);
		String resultOBJ = getIntent().getStringExtra("result");

		try {
			JSONObject result = new JSONObject(resultOBJ);
			mResultTv.setText(result.getString("result"));

			int resID = result.getInt("resultcode");
			if (resID == R.string.verify_success) {
				//检测成功播放语音
				doPlay(R.raw.meglive_success);
			} 
//			else if (resID == R.string.liveness_detection_failed_not_video) {
//				doPlay(R.raw.meglive_failed);
//			} else if (resID == R.string.liveness_detection_failed_timeout) {
//				doPlay(R.raw.meglive_failed);
//			} else if (resID == R.string.liveness_detection_failed) {
//				doPlay(R.raw.meglive_failed);
//			} 
			else {
				//检测失败播放语音
				doPlay(R.raw.meglive_failed);
			}

			boolean isSuccess = result.getString("result").equals(getResources().getString(R.string.verify_success));
			mResultImageView.setImageResource(isSuccess ? R.drawable.face_result_success: R.drawable.face_result_fail);
			if(isSuccess)
			{
				mResultFinishBtn.setVisibility(View.VISIBLE);
			}else
			{
				mResultAgainBtn.setVisibility(View.VISIBLE);
			}
			// doRotate(isSuccess);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void startActivity(Context context, String status) {
		Intent intent = new Intent(context, XJFaceResultActivity.class);
		intent.putExtra("result", status);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.face_result_finish:
			Intent loanMoneyIntent=new Intent(XJFaceResultActivity.this,XJLoanMoneyFirstActivity.class);
			startActivity(loanMoneyIntent);
			break;
		case R.id.face_result_again:
			XJFaceResultActivity.this.finish();
			break;
		case R.id.left_btn:
			XJFaceResultActivity.this.finish();
			break;
		default:
			break;
		}
	}

	private MediaPlayer mMediaPlayer = null;

	private void doPlay(int rawId) {
		if (mMediaPlayer == null)
			mMediaPlayer = new MediaPlayer();

		mMediaPlayer.reset();
		try {
			AssetFileDescriptor localAssetFileDescriptor = getResources()
					.openRawResourceFd(rawId);
			mMediaPlayer.setDataSource(
					localAssetFileDescriptor.getFileDescriptor(),
					localAssetFileDescriptor.getStartOffset(),
					localAssetFileDescriptor.getLength());
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception localIOException) {
			localIOException.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
		}
	}
}