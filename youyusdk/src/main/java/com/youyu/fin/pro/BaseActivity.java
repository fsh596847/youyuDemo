package com.youyu.fin.pro;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.KeyEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;


public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";


    protected int screenHeight;
    protected int screenWidth;


    private Dialog progressDialog;
    protected LayoutInflater inflater;
    protected View dialogView;
    protected LinearLayout layout;
    protected TextView tipTextView;
    protected ImageView img;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = LayoutInflater.from(this);


        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        screenHeight = outMetrics.heightPixels;
        screenWidth = outMetrics.widthPixels;

        sharedPreferences = getSharedPreferences("xm_preference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    protected String getToken(){
        return sharedPreferences.getString("token","");
    }

    /**
     * 显示进度框
     */
    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            initProgressDialog(msg);
        }
        try {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 显示进度框，外面点击不取消
     */
    public void showProgressDialogUnCancle(String msg) {
        if (progressDialog == null) {
            initProgressDialog(msg);
        }
        try {
            progressDialog.setTitle(msg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void progressDialogDismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * 进度框是否显示
     * @return
     */
    public boolean isProgressShowing() {
        return progressDialog.isShowing();
    }




    public void initProgressDialog(String msg) {
        dialogView = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        layout = (LinearLayout) dialogView.findViewById(R.id.dialog_view);// 加载布局
        tipTextView = (TextView) dialogView.findViewById(R.id.tipTextView);
        img = (ImageView) dialogView.findViewById(R.id.img);

        if (TextUtils.isEmpty(msg)) {
            tipTextView.setText("请稍后...");// 设置加载信息
        } else {
            tipTextView.setText(msg);// 设置加载信息
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "rotation", 0.0f, 360F);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setDuration(1500).start();

        progressDialog = new Dialog(this, R.style.myDialogTheme);// 创建自定义样式dialog

        progressDialog.setCancelable(false);// 不可以用“返回键”取消
        progressDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

    }


    /**
     * 授权检查
     */
    protected void network(Handler handler) {
//        contentRel.setVisibility(View.GONE);
//        barLinear.setVisibility(View.VISIBLE);
//        againWarrantyBtn.setVisibility(View.GONE);
//        WarrantyText.setText("正在联网授权中...");
//        WarrantyBar.setVisibility(View.VISIBLE);

        this.handler = handler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(BaseActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
                        BaseActivity.this);
                manager.registerLicenseManager(idCardLicenseManager);
                String uuid = "13213214321424";
                manager.takeLicenseFromNetwork(uuid);
                String contextStr = manager.getContext(uuid);
                Log.w("ceshi", "contextStr====" + contextStr);

                Log.w("ceshi",
                        "idCardLicenseManager.checkCachedLicense()===" + idCardLicenseManager.checkCachedLicense());
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    UIAuthState(true);
                else
                    UIAuthState(false);
            }
        }).start();

    }


    private void UIAuthState(final boolean isSuccess) {
        runOnUiThread(new Runnable() {
            public void run() {
                authState(isSuccess);
            }
        });
    }

    private void authState(boolean isSuccess) {
        if (isSuccess) {
//            barLinear.setVisibility(View.GONE);
//            WarrantyBar.setVisibility(View.GONE);
//            againWarrantyBtn.setVisibility(View.GONE);
//            contentRel.setVisibility(View.VISIBLE);

//            Toast.makeText(this,"授权成功",Toast.LENGTH_SHORT).show();
        } else {
//            barLinear.setVisibility(View.VISIBLE);
//            WarrantyBar.setVisibility(View.GONE);
//            againWarrantyBtn.setVisibility(View.VISIBLE);
//            contentRel.setVisibility(View.GONE);
//            WarrantyText.setText("联网授权失败！请检查网络或找服务商");

//            Toast.makeText(this,"联网授权失败！请检查网络或找服务商",Toast.LENGTH_SHORT).show();
        }

        handler.sendEmptyMessage(10);
    }


}
