package consumer.fin.rskj.com.library.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.utils.ConUtil;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SysUtil;


/**
 * 人脸识别起始页
 **/
public class FaceStartActivity extends BaseActivity implements OnClickListener {
    private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
    private Button mBackBtn;//返回按钮
    private TextView mTitleView;//标题

    private String uuid;
    private LinearLayout mWarringBarLL, mFaceTipLL;
    private Button mStartFaceBtn;
    private TextView mWarringTv;//网络检测提示
    private ProgressBar mWarrantyBar;//网络检测进度
    private Button againWarringBtn;//网络检测按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_start_layout);

        init();
        netWorkWarranty();

        LogUtils.d("FaceStartActivity","数组 = " + SysUtil.stepMap);
    }

    @Override
    public void init() {
        mStatusView = findViewById(R.id.status_bar_view);
        int statusHeight = SysUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
        params.height = statusHeight;
        mStatusView.setLayoutParams(params);
        mBackBtn = (Button) findViewById(R.id.left_btn);
        mTitleView = (TextView) findViewById(R.id.center_title);
        mTitleView.setText(R.string.face_text);
        mBackBtn.setOnClickListener(this);
        uuid = ConUtil.getUUIDString(this);//设备uuid
        // 设备号
        String deviceId = ConUtil.getDeviceID(this);
        // app版本号
        String version = ConUtil.getVersionName(this);
        Log.v("tag", "uuid-------------->" + uuid);
        mFaceTipLL = (LinearLayout) findViewById(R.id.face_face_tip_ll);
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
                Manager manager = new Manager(FaceStartActivity.this);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(FaceStartActivity.this);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.left_btn) {
            //返回
            this.finish();
        } else if (view.getId() == R.id.face_start_liveness_btn) {
            //开始检测
            startActivityForResult(new Intent(this, FaceActivity.class), PAGE_INTO_LIVENESS);
        } else if (view.getId() == R.id.start_face_again_warring_btn) {
            //网络检查，是否连接face++
            netWorkWarranty();
        }
    }

    private static final int PAGE_INTO_LIVENESS = 100;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取活体检测结果，并将结果发送到结果页
        if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            FaceResultActivity.startActivity(this, result);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mStartFaceBtn.setVisibility(View.VISIBLE);
                mWarringBarLL.setVisibility(View.GONE);
            } else if (msg.what == 2) {
                againWarringBtn.setVisibility(View.VISIBLE);
                mWarringTv.setText(R.string.link_network_error);
                mWarrantyBar.setVisibility(View.GONE);
            }
        }
    };



}
