package com.zhongan.demo.hxin.activitys;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.util.ActivityStackManagerUtils;
import com.zhongan.demo.hxin.util.Util;


/**
 * Created by admin on 2017/5/31.
 */

public class HXResultingActivity extends HXBaseActivity implements View.OnClickListener {
    private Dialog mDialog;
    TextView title, tv_content;
    ImageView imageView;

    ObjectAnimator animator;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 1){
                Log.d("TAG","--------定时刷新-------");
                handler.removeMessages(1);
                getLoanAmountInfo(handler);
                handler.sendEmptyMessageDelayed(1,3500);
                return;
            }

            if(msg.what == 2){
                animator.cancel();
                handler.removeCallbacksAndMessages(null);
                imageView.setRotation(0f);

                title.setText("审批结果");
                tv_content.setText("这不是我要的结果");
                imageView.setImageResource(R.drawable.m_icon_deal_failur);
                return;
            }

            if(msg.what == 3){
                animator.cancel();
                handler.removeCallbacksAndMessages(null);
                imageView.setRotation(0f);

                title.setText("审批结果");
                tv_content.setText("审批通过啦");
                imageView.setImageResource(R.drawable.m_icon_deal_sucess);
                return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hxactivity_resulting);
        findViewById(R.id.left_btn).setOnClickListener(this);
        mDialog= Util.createLoadingDialog(this,"请稍等...");
        title = (TextView) findViewById(R.id.center_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        imageView = (ImageView) findViewById(R.id.imageView);

        title.setText("审批中");

//        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0.0f, 360F);
//        animator.setRepeatCount(ObjectAnimator.INFINITE);
//        //animator.setRepeatMode(ObjectAnimator.REVERSE);
//        animator.setDuration(1000).start();

//        handler.sendEmptyMessageDelayed(1,3500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                Intent intent = new Intent(this, MenuListActivity2.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MenuListActivity2.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != handler){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
