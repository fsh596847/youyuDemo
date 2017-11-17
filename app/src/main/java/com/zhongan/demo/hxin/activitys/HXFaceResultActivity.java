package com.zhongan.demo.hxin.activitys;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 人脸识别结果页
 */
public class HXFaceResultActivity extends HXBaseActivity implements
		View.OnClickListener {

	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;// 返回按钮
	private TextView mTitleView;// 标题

	private TextView mResultTv;
	private ImageView mResultImageView;
	private Button mResultFinishBtn, mResultAgainBtn;
	private LinearLayout mFaceTipLL;

	boolean isSuccess = false;

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

			isSuccess = result.getString("result").equals(getResources().getString(R.string.verify_success));
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


	//设置工作流接口
	private void setProgress() {
		RequestParams params = new RequestParams("utf-8");
		params.addHeader("Content-Type", "application/x-www-form-urlencoded");
		params.addBodyParameter("transCode", Contants.TRANS_CODE_SET_WORK);
		params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
		params.addBodyParameter("clientToken", getSharePrefer().getToken());
		httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<Map<String, String>>() {
				}.getType();
				Gson gson = new Gson();
				Map<String, String> resultMap = gson.fromJson(data, type);
				String flag = resultMap.get("flag");
				String returnCode = resultMap.get("returnCode");
//                if("00".equals(flag)){
//                    Intent intent = new Intent(FaceResultActivity.this,ResultingActivity.class);
//                    startActivity(intent);
//                }

				if("000000".equals(returnCode)){
					Intent intent = new Intent(HXFaceResultActivity.this,HXResultingActivity.class);
					startActivity(intent);
					return;
				}


				if("M000160".equals(returnCode)){
					Intent intent = new Intent(HXFaceResultActivity.this,HXResultActivity.class);
					intent.putExtra("message","测试人员已达上限");
					intent.putExtra("isSuccess",false);
					startActivity(intent);
					return;
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(HttpException exception, String msg) {
			}

			@Override
			public void onError(String returnCode, String returnMsg) {
				// TODO Auto-generated method stub
				//Toast.makeText(HXFaceResultActivity.this,"贷款工作流失败",Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("贷款工作流失败" ,HXFaceResultActivity.this);
			}
		});
	}


	public static void startActivity(Context context, String status) {
		Intent intent = new Intent(context, HXFaceResultActivity.class);
		intent.putExtra("result", status);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.face_result_finish:
//			Intent loanMoneyIntent=new Intent(HXFaceResultActivity.this,HXLoanMoneyFirstActivity.class);
//			startActivity(loanMoneyIntent);
				if(isSuccess){
//					setProgress();
				}

				break;
			case R.id.face_result_again:
				HXFaceResultActivity.this.finish();
				break;
			case R.id.left_btn:
				HXFaceResultActivity.this.finish();
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