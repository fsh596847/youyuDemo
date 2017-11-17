package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.views.SwitchButton;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

/**
 * Created by HP on 2017/7/28.
 * 银行卡信息
 */

public class CardDetailActivity extends BaseActivity  {

    private Intent intent;
    private LinearLayout unbound_card;
    private SwitchButton switchButton;
    private TopNavigationView2 topbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankdetail);

        switchButton = (SwitchButton) findViewById(R.id.switch_button);

        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                showToast("isChecked= " + isChecked , Constants.TOAST_SHOW_POSITION);
            }
        });
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
        unbound_card = (LinearLayout) findViewById(R.id.ubound_card);
        unbound_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPWDDialog(new FinishCallBackImpl() {
                    @Override
                    public void finishCallBack(String data) {

                    }
                });
            }
        });

    }



}
