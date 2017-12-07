package com.zhongan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.http.OkHttpRequestManager;
import com.zhongan.demo.impl.ReqCallBack;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.util.ToastUtils;
import com.zhongan.demo.view.TopNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 2017/10/17.
 */

public class AboutActivity extends BaseActivity {
    private TopNavigationView navigationView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_about);

        initView();
    }


    private void initView() {

        navigationView = (TopNavigationView) findViewById(R.id.topbar);
        navigationView.setClickListener(new TopNavigationView.NavigationViewClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        findViewById(R.id.about_youyu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, HtmlActivity.class);
                intent.putExtra("url", MyApplication.getSP(getApplicationContext()).getAboutUrl());
                intent.putExtra("title","关于");
                startActivity(intent);
            }
        });

        TextView version_msg = (TextView) findViewById(R.id.version_msg);
        version_msg.setText("当前版本 " + BuildConfig.VERSION_NAME);

    }


}
