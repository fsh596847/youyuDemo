package consumer.fin.rskj.com.library.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.views.TopNavigationView2;


/**
 * Created by HP on 2017/7/29.
 * 个人中心
 */

public class AboutActivity extends  BaseActivity implements View.OnClickListener {

    private LinearLayout bank_card;
    private TopNavigationView2 topbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rskj_activity_about);

        bank_card = (LinearLayout) findViewById(R.id.bank_card);
        bank_card.setOnClickListener(this);

    }

    @Override
    public void init() {

        topbar = (TopNavigationView2) findViewById(R.id.topbar);
        topbar.setClickListener(new TopNavigationView2.NavigationViewClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}
