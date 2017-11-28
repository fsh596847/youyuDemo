package com.zhongan.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.MotionEvent;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.util.LogUtils;
import consumer.fin.rskj.com.library.login.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017/8/24.
 */

public class ParentActivity extends AppCompatActivity {

  /**
   * 需要进行检测的权限数组
   */
  protected String[] needPermissions = {
      Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
      Manifest.permission.READ_CONTACTS
  };

  /**
   * 判断是否需要检测，防止不停的弹框
   */
  private boolean isNeedCheck = true;
  //权限检测请求码
  private static final int PERMISSION_REQUEST_CODE = 120;

  @Override public void onCreate(@Nullable Bundle savedInstanceState,
      @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
  }

  @Override protected void onResume() {
    super.onResume();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (isNeedCheck) {
        //                checkPermissions(needPermissions);
      }
    }
  }

  /**
   * 检测权限
   */
  protected void checkPermissions(String... permissions) {
    List<String> needRequestPermissionList = findDeniedPermissions(permissions);
    if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
      ActivityCompat.requestPermissions(this,
          needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
          PERMISSION_REQUEST_CODE);
    }
  }

  /**
   * 获取权限集中需要申请权限的列表
   */
  protected List<String> findDeniedPermissions(String[] permissions) {
    List<String> needRequestPermissionList = new ArrayList<>();
    for (String perm : permissions) {

      if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
          || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
        Log.d("TTTT = ", "perm = " + perm);
        needRequestPermissionList.add(perm);
      }
    }
    return needRequestPermissionList;
  }

  /**
   * 检测是否所有的权限都已经授权
   */
  private boolean verifyPermissions(int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  /**
   * 显示提示信息
   */
  private void showMissingPermissionDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.map_notifyTitle);
    builder.setMessage(R.string.map_notifyMsg);
    // 拒绝, 退出应用
    builder.setNegativeButton(R.string.map_cancel, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        //finish();
      }
    });

    builder.setPositiveButton(R.string.map_setting, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        startAppSettings();
      }
    });
    builder.setCancelable(false);
    builder.show();
  }

  /**
   * 启动应用的设置
   */
  private void startAppSettings() {
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivity(intent);
    //      startActivityForResult(intent,PERMISSION_REQUEST_CODE);
  }

  //检测用户是否开启权限
  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] paramArrayOfInt) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (!verifyPermissions(paramArrayOfInt)) {
        showMissingPermissionDialog();
        isNeedCheck = false;
      }
    }
  }

  /**
   * 定时操作
   */
  private Handler handler = new Handler();
  private long time = 1000 * 10 * 1;

  //@Override public boolean onTouchEvent(MotionEvent event) {
  //  LogUtils.Log(ParentActivity.class.getSimpleName(), "---------onTouchEvent-------");
  //  switch (event.getAction()) {
  //    case MotionEvent.ACTION_DOWN:
  //      LogUtils.Log(ParentActivity.class.getSimpleName(), "---------ACTION_DOWN-------");
  //      handler.removeCallbacks(runnable);
  //      break;
  //    case MotionEvent.ACTION_UP:
  //      LogUtils.Log(ParentActivity.class.getSimpleName(), "---------ACTION_UP-------");
  //      startRun();
  //      break;
  //  }
  //  return super.onTouchEvent(event);
  //}

  private Runnable runnable = new Runnable() {
    @Override public void run() {
      MyApplication.getSP(getApplicationContext()).setLogin(false);
      Intent intent = new Intent(ParentActivity.this, LoginActivity.class);
      intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
    }
  };

  /**
   * 定时操作开始
   */
  public void startRun() {
    handler.removeCallbacks(runnable);
    handler.postDelayed(runnable, time);
  }
}
