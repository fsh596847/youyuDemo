package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.TopNavigationView2;


/**
 * Created by HP on 2017/7/29.
 * 添加银行卡
 */

public class AddBankCardActivity extends  BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddBankCardActivity";

    private RelativeLayout choose_bank;
    private TopNavigationView2 topbar;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbankcard);

    }


    @Override
    public void init() {

        choose_bank = (RelativeLayout) findViewById(R.id.choose_bank);
        choose_bank.setOnClickListener(this);

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


        if(v.getId() == R.id.choose_bank){
            showToast("选择银行" , Constants.TOAST_SHOW_POSITION);
        }

    }

    private void addBankCard(){
        /**
         bankid	银行卡号
         cell	电话号
         idnumber	身份证
         name	姓名
         */
        requestParams.clear();
        requestParams.put("transCode", "M100710");//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
        requestParams.put("legalPerNum", "00001");
        requestParams.put("fundId", sharePrefer.getXJFundId());

        requestParams.put("bankid", "");
        requestParams.put("cell", "");
        requestParams.put("idnumber", "1");
        requestParams.put("name", "10000");

        showLoading("正在加载...");

        LogUtils.d(TAG, "全部还款: requestParams--->" + requestParams.toString());
        sendPostRequest(requestParams, new ResultCallBack() {
            @Override
            public void onSuccess(String data) {
                dismissLoading();
                LogUtils.d(TAG, "全部还款: data--->" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if("000000".equals(jsonObject.getString("returnCode"))){

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String retrunCode, String errorMsg) {
                dismissLoading();
                LogUtils.d(TAG, "全部还款: errorMsg--->" + errorMsg);
            }

            @Override
            public void onFailure(String errorMsg) {
                dismissLoading();
                LogUtils.d(TAG, "全部还款: errorMsg--->" + errorMsg);
            }
        });

    }

}
