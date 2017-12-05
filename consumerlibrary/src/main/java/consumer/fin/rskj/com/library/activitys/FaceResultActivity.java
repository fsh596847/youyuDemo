package consumer.fin.rskj.com.library.activitys;

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

import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SysUtil;
import consumer.fin.rskj.com.library.utils.Util;

/**
 * 人脸识别结果页
 */
public class FaceResultActivity extends BaseActivity implements View.OnClickListener {
  /**
   * 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
   */
  private View mStatusView;
  /**
   * 返回按钮
   */
  private Button mBackBtn;
  /**
   * 标题
   */
  private TextView mTitleView;

  private TextView mResultTv;
  private ImageView mResultImageView;
  private Button mResultFinishBtn;
  private Button mResultAgainBtn;
  private LinearLayout mFaceTipLL;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_face_result);
    init();
  }

  @Override public void init() {
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
    mFaceTipLL = (LinearLayout) findViewById(R.id.face_tip_ll_one);
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

      boolean isSuccess =
          result.getString("result").equals(getResources().getString(R.string.verify_success));
      mResultImageView.setImageResource(
          isSuccess ? R.mipmap.rskj_face_result_success : R.mipmap.rskj_face_result_fail);
      if (isSuccess) {
        mResultFinishBtn.setVisibility(View.VISIBLE);
      } else {
        mResultAgainBtn.setVisibility(View.VISIBLE);
      }
      // doRotate(isSuccess);

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static void startActivity(Context context, String status) {
    Intent intent = new Intent(context, FaceResultActivity.class);
    intent.putExtra("result", status);
    context.startActivity(intent);
  }

  @Override public void onClick(View view) {

    if (view.getId() == R.id.face_result_finish) {
      //String currentPKG = Util.getAppPackageName(FaceResultActivity.this);
      String currentPKG = FaceStartActivity.class.getSimpleName();
      LogUtils.d("currentPKG", "currentPKG->" + currentPKG);
      int currerntIndex = Util.getIndex(SysUtil.stepMap, currentPKG);
      LogUtils.d("currentPKG", "currerntIndex->" + currerntIndex);
      if (currerntIndex == -1) {
        showToast("包名有误：" + currentPKG, Constants.TOAST_SHOW_POSITION);
        return;
      } else {
        currerntIndex = currerntIndex + 1;
        if (currerntIndex > SysUtil.stepMap.length) {
          showToast("超出范围：" + currentPKG, Constants.TOAST_SHOW_POSITION);
          return;
        }
      }
      LogUtils.d("currentPKG", "currerntIndex->" + currerntIndex);
      //			Intent intent = new Intent(FaceResultActivity.this, BasicInfoActivity.class);
      Intent intent = new Intent();
      intent.setAction(SysUtil.stepMap[currerntIndex]);
      startActivity(intent);
      FaceResultActivity.this.finish();
    } else if (view.getId() == R.id.face_result_again) {
      FaceResultActivity.this.finish();
    } else if (view.getId() == R.id.left_btn) {
      this.finish();
    }
  }

  private MediaPlayer mMediaPlayer = null;

  private void doPlay(int rawId) {
    if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();

    mMediaPlayer.reset();
    try {
      AssetFileDescriptor localAssetFileDescriptor = getResources().openRawResourceFd(rawId);
      mMediaPlayer.setDataSource(localAssetFileDescriptor.getFileDescriptor(),
          localAssetFileDescriptor.getStartOffset(), localAssetFileDescriptor.getLength());
      mMediaPlayer.prepare();
      mMediaPlayer.start();
    } catch (Exception localIOException) {
      localIOException.printStackTrace();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mMediaPlayer != null) {
      mMediaPlayer.reset();
      mMediaPlayer.release();
    }
  }
}