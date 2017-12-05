package com.zhongan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhongan.demo.view.TopNavigationView;

/**
 * Created by HP on 2017/10/17.
 */

public class AboutActivity extends BaseActivity {
    private TopNavigationView navigationView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.rskj_activity_about);

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
                intent.putExtra("url","http://app-web.rskj99.com/aboutus/aboutus.html");
                intent.putExtra("title","关于");
                startActivity(intent);
            }
        });

        TextView version_msg = (TextView) findViewById(R.id.version_msg);
        version_msg.setText("当前版本 " + BuildConfig.VERSION_NAME);

    }


}
