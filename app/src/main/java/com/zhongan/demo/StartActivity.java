package com.zhongan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.bqs.risk.df.android.BqsDF;
import com.umeng.analytics.MobclickAgent;
import com.zhongan.demo.util.Global;
import com.zhongan.demo.util.PermissionUtils;

/**
 * Created by HP on 2017/5/24.
 */

public class StartActivity extends BaseActivity {

    //获取6.0运行时权限列表
    String[] requestPermissions = BqsDF.getRuntimePermissions(true, true, true);

    Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            if(msg.what == 0){
                startActivity(new Intent(StartActivity.this,MenuListActivity.class));
                StartActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getSP(this).setLogin(false);//每次打开都设置为未登录状态
        PermissionUtils.requestMultiPermissions(this, requestPermissions, mPermissionGrant);

        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
                    action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_start);

        handler.sendEmptyMessageDelayed(0,3000);


    }

    /**
     * 授权结果，该回调不管权限是拒绝还是同意都会进入该回调方法
     */
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, String[] requestPermissions) {
            Global.authRuntimePermissions = true;

            /**
             * 因为在启动页进行运行时权限授权，所以要在授权结果回调中出发一次初始化
             */
            initBqsDFSDK();
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(requestCode, requestPermissions, grantResults, requestPermissions, mPermissionGrant);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("rskj.youyu.start");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("rskj.youyu.start");
        MobclickAgent.onPause(this);
    }

}
