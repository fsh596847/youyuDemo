package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.wknight.keyboard.WKnightKeyboard;

import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.MultiEditText;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

/**
 * Created by HP on 2017/8/1.
 * 设置支付密码
 */

public class SetPayPWDActivity extends BaseActivity   {

    private static final String TAG = "SetPayPWDActivity";

    private TopNavigationView2 topbar;
    private MultiEditText mMultiEditText1;
    private TextView title;
    private boolean isFirst = true;

    private String paymentPassword,repaymentPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setpaypwd);

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

        title = (TextView) findViewById(R.id.title);
        mMultiEditText1 = (MultiEditText) findViewById(R.id.edittext1);
        //    mMultiEditText1.isTextVisible()    mMultiEditText1.setTextVisible(false);

        final WKnightKeyboard keyboard = new WKnightKeyboard(mMultiEditText1);
        if (mMultiEditText1 != null) {
            keyboard.setRecvTouchEventActivity(this);
        }
        mMultiEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 6){
                    if(isFirst){
                        title.setText("请再次输入交易密码");

                        paymentPassword = keyboard.getEnctyptText();
                        keyboard.clearInput();
                        LogUtils.d(TAG,"paymentPassword = " + paymentPassword);
                        isFirst = false;
                        mMultiEditText1.setText("");
                    }else {
                        repaymentPassword = keyboard.getEnctyptText();
                        LogUtils.d(TAG,"repaymentPassword = " + repaymentPassword);

                        setPwd();

                        title.setText("请设置交易密码");
                        keyboard.clearInput();
                        isFirst = true;
                        mMultiEditText1.setText("");
                    }
                }


            }
        });

    }



    //设置支付密码
    private void setPwd() {

        requestParams.clear();
        requestParams.put("transCode", "M000166");//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
      requestParams.put("legalPerNum", Constants.LEGALPER_NUM);

        requestParams.put("paymentPassword", paymentPassword);
        requestParams.put("repaymentPassword", repaymentPassword);

        showLoading("正在加载...");

        LogUtils.d(TAG, "设置支付密码: requestParams--->" + requestParams.toString());
        sendPostRequest(requestParams, new ResultCallBack() {
            @Override
            public void onSuccess(String data) {
                dismissLoading();
                LogUtils.d(TAG, "设置支付密码: data--->" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if("000000".equals(jsonObject.getString("returnCode") )){

                        Intent i = new Intent();
                        i.putExtra("status","success");
                        setResult(RESULT_OK,i);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String retrunCode, String errorMsg) {
                dismissLoading();
                LogUtils.d(TAG, "设置支付密码: errorMsg--->" + errorMsg);
            }

            @Override
            public void onFailure(String errorMsg) {
                dismissLoading();
                LogUtils.d(TAG, "设置支付密码: errorMsg--->" + errorMsg);
            }
        });

    }
}
