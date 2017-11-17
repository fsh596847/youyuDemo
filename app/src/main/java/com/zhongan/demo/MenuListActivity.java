package com.zhongan.demo;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.fragments.PersonCenterFragment;
import com.zhongan.demo.fragments.UnLoginFragment;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.util.DownloadUtil;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.FragmentTabHost;
import com.zhongan.demo.view.Tab;
import com.zhy.base.fileprovider.FileProvider7;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by HP on 2017/8/10.
 * 功能菜单列表
 */

public class MenuListActivity extends HXBaseActivity {

    public static final String TAG = "MenuListActivity";

    private LayoutInflater mInflater;
    private FragmentTabHost mTabHost;
    private ArrayList<Tab> mTabs = new ArrayList<Tab>(2);

    private Dialog updateDialog;//更新提示框
    private String fileUrl;//文件下载路径
    private String uopdateMsg;
    private int isrelease = -1;

    private long curMillions = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getUpdateInfo();
                    break;

                case 3:
                    installApk();//下载完成安装
                    break;

                case 2:
                    String text22 = (String) msg.obj;
                    Log.d(TAG, "text22 = " + text22);

                    JSONObject update_info;
                    String code;
                    try {
                        update_info = new JSONObject(text22);
                        //数据返回成功
                        code = update_info.getString("code");
                        Log.d(TAG, "code = " + code);

                        if (!TextUtils.isEmpty(code) && "success".equals(code)) {
                            JSONObject updateItem = update_info.getJSONObject("data");
                            if (null != updateItem) {

                                uopdateMsg = updateItem.getString("content");//发布内容
                                fileUrl = updateItem.getString("fileUrl");//发布内容
                                isrelease = updateItem.getInt("isrelease");//是否强制更新

                                if (2 == isrelease) {
                                    showVersionDialog(true);//强制更新
                                } else if (1 == isrelease) {
                                    showVersionDialog(false);//普通更新
                                } else {
                                    //没有更新
                                    //ToastUtils.showCenterToast("没有更新",mActivity);
                                    Log.d(TAG, "没有更新");
                                }
                            }

                        } else {
                            Log.d(TAG, "no update info");
                        }

                    } catch (Exception e) {
                        Log.d(TAG, "Exception = " + e.getMessage());
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void installApk() {
        //apk文件的本地路径
        File apkfile = new File(getSharePrefer().getDownLoadPath());
        //会根据用户的数据类型打开android系统相应的Activity。
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 仅需改变这一行
            FileProvider7.setIntentDataAndType(this,
                    intent, "application/vnd.android.package-archive", apkfile, true);
            startActivity(intent);
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }

        //为这个新apk开启一个新的activity栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //开始安装
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManagerUtils.getInstance().addActivity(this);
        setContentView(R.layout.activity_menulist2);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        screenHeight = outMetrics.heightPixels;
        screenWidth = outMetrics.widthPixels;

        initView();

        handler.sendEmptyMessageDelayed(0, 500);

    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getFragmentManager(), R.id.fragment_container);

        initTabHost();

        //去掉纵向分割线 SHOW_DIVIDER_NONE
        //mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //解决侧滑菜单 滑动是底部分割线 绘制
        //mTabHost.getTabWidget().setDividerDrawable(getResources().getDrawable(R.drawable.tab_divider));
        mTabHost.setCurrentTab(0);
    }

    private void initTabHost() {
        Tab num1 = new Tab(R.string.tap_home, R.drawable.m_tab1selector, UnLoginFragment.class);
        Tab num2 = new Tab(R.string.tap_center, R.drawable.m_tab3selector, PersonCenterFragment.class);

        mTabs.add(num1);
        mTabs.add(num2);

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }


    //获取版本信息
    private void getUpdateInfo() {
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/json");
        params.addHeader("deviceType", "android");
        params.addHeader("version", BuildConfig.VERSION_NAME);
        params.addHeader("code", "youyu");
        LoggerUtil.debug(TAG, "----getUpdateInfo----------->" + params.toString());
        MyApplication.instance.dataHttp.send(HttpMethod.POST, HttpContent.VERSION_UPDATE,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LoggerUtil.debug(TAG, "HttpException----getUpdateInfo----->" + arg1 + arg0.getMessage());
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (responseInfo != null && responseInfo.result != null) {
                            LoggerUtil.debug(TAG, "onSuccess--getUpdateInfo-->" + responseInfo.result);
                            handler.obtainMessage(2, responseInfo.result).sendToTarget();
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                });

    }


    protected void showVersionDialog(final boolean option) {
        if (updateDialog == null) {
            updateDialog = new Dialog(this, R.style.myDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.update_dialog_layout, null);
            final ProgressBar mProgressbar = (ProgressBar) view.findViewById(R.id.upload_progress);
            ;
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView content = (TextView) view.findViewById(R.id.content);
            content.setText(uopdateMsg);//更新提示

            TextView sureBtn = (TextView) view.findViewById(R.id.sure);
            sureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //执行下载
                    if (!TextUtils.isEmpty(fileUrl)) {
                        //跳转浏览器下载
//                        Intent intent= new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse(fileUrl);
//                        intent.setData(content_url);
//                        startActivity(intent);

                        DownloadUtil.get().download(fileUrl, getSharePrefer(), "download", new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess() {
                                LogUtils.Log(TAG, "----下载完成------");
                                handler.obtainMessage(3).sendToTarget();
                            }

                            @Override
                            public void onDownloading(int progress) {
                                mProgressbar.setProgress(progress);
                            }

                            @Override
                            public void onDownloadFailed() {
                                LogUtils.Log(TAG, "----下载失败------");
                            }
                        });

                    }

                }
            });

            TextView cancleBtn = (TextView) view.findViewById(R.id.cancel);
            if (option) {
                cancleBtn.setText("退出");
            }
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDialog.dismiss();

                    if (option) {//如果是强制更新 点击返回就退出
                        onBackPressed();
                    }
                }
            });

            updateDialog.setContentView(view);
            updateDialog.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams params = updateDialog.getWindow().getAttributes();
            params.width = (int) (screenWidth * 0.8);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            updateDialog.getWindow().setAttributes(params);
        }
        updateDialog.show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            long millions = System.currentTimeMillis();
            if (millions - curMillions > 2000) {
                curMillions = millions;
                //Toast.makeText(this, "再按一次退出程序",Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("再按一次退出程序", this);
            } else {
                //finish();
                ActivityStackManagerUtils.getInstance().finishAllActivity();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
