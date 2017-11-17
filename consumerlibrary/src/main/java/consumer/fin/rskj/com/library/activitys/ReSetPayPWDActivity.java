package consumer.fin.rskj.com.library.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wknight.keyboard.WKnightKeyboard;

import org.json.JSONException;
import org.json.JSONObject;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.Util;
import consumer.fin.rskj.com.library.views.CountDownTimer;
import consumer.fin.rskj.com.library.views.MultiEditText;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

/**
 * Created by HP on 2017/8/18.
 * 重置交易密码
 * 重置交易密码（场景码：13）
 */

public class ReSetPayPWDActivity extends BaseActivity   {

    private static final String TAG = "ReSetPayPWDActivity";

    private TopNavigationView2 topbar;
    private AppCompatEditText password1 , password2;
    private TextView getCode;
    private WKnightKeyboard keyboard1 ,keyboard2;

    private TextView phone ;
    private EditText smsCode , userName,certNo;

    private long surplusTime = 0;
    private long allsurplusTime = 120000;
    private CountDownTimer timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resetpaypwd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置短信验证码时间
        Long thisTime = System.currentTimeMillis();//当前系统时间
        Long idCardSysTime = sharePrefer.getTimeIdcardSys();//存储倒计时时间
        Long idCardTime = sharePrefer.getTimeIdcard();
        Long surplusTime = idCardTime - (thisTime - idCardSysTime);//计算倒计时显示时间
        if (idCardSysTime != 0 && idCardTime != 0 && surplusTime > 0) {
            allsurplusTime = surplusTime;
            instantiationTime(allsurplusTime);
            timer.start();
        }
    }


    //初始化倒计时控件
    private void instantiationTime(Long time) {
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCode.setClickable(false);// 防止重复点击
                getCode.setText(millisUntilFinished / 1000 + "s");
                surplusTime = millisUntilFinished;
                //LogUtils.d("debug", "surplusTime------>" + surplusTime + "");
            }

            @Override
            public void onFinish() {
                surplusTime = 0;
                allsurplusTime = 120000;
                getCode.setText(R.string.yzm_btn_text);
                getCode.setClickable(true);
            }
        };
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

        phone = (TextView) findViewById(R.id.phone);
        phone.setText(sharePrefer.getPhone());
        String phoneNo = sharePrefer.getPhone();
        if(!TextUtils.isEmpty(phoneNo)) {
            phone.setText(phoneNo.substring(0, 3) + "****" + phoneNo.substring(7, 11));
        }

        smsCode = (EditText)findViewById(R.id.smsCode);
        userName = (EditText)findViewById(R.id.userName);
        certNo = (EditText)findViewById(R.id.certNo);

        password1 = (AppCompatEditText) findViewById(R.id.password1);
        password2 = (AppCompatEditText) findViewById(R.id.password2);

        keyboard1 = new WKnightKeyboard(password1);
        if (password1 != null) {
            keyboard1.setRecvTouchEventActivity(this);
        }

        keyboard2 = new WKnightKeyboard(password2);
        if (password2 != null) {
            keyboard2.setRecvTouchEventActivity(this);
        }

        getCode = (TextView) findViewById(R.id.getCode);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(sharePrefer.getPhone()) && Util.isMobile(sharePrefer.getPhone())){
                    getMsmCode("13", sharePrefer.getPhone() , new ResultCallBack() {
                        @Override
                        public void onSuccess(String data) {
                            dismissLoading();
                            allsurplusTime = 120000;
                            instantiationTime(allsurplusTime);
                            timer.start();
                        }

                        @Override
                        public void onFailure(String errorMsg) {
                            getCode.setClickable(true);
                            dismissLoading();
                        }

                        @Override
                        public void onError(String retrunCode, String errorMsg) {
                            getCode.setClickable(true);
                            dismissLoading();
                        }
                    });
                }else {
                    showToast("请输入正确的手机号",1);
                }

            }
        });

        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(smsCode.getText())){
                    showToast("请输入短信验证码",1);
                    return;
                }


                if(TextUtils.isEmpty(userName.getText())){
                    showToast("请输入联系人姓名",1);
                    return;
                }

                if(TextUtils.isEmpty(certNo.getText())){
                    showToast("请输入身份证号",1);
                    return;
                }

                if (TextUtils.isEmpty(password1.getText())) {
                    showToast("请输入交易密码",1);
                    return;
                }

                if (TextUtils.isEmpty(password2.getText())) {
                    showToast("请再次输入交易密码",1);
                    return;
                }

                checkSMSCode(smsCode.getText().toString(), phone.getText().toString(),
                        "13", new FinishCallBackImpl() {
                    @Override
                    public void finishCallBack(String data) {
                        reSetPwd();
                    }
                });

            }
        });
    }




    //重置支付密码
    private void reSetPwd() {
        requestParams.clear();
        requestParams.put("transCode", "M000167");//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
        requestParams.put("legalPerNum", "00001");
        requestParams.put("isNo", "13");
        requestParams.put("verifyCode", smsCode.getText().toString());

        requestParams.put("certNo", certNo.getText().toString());
        requestParams.put("userName", userName.getText().toString());
        requestParams.put("payPassword", keyboard1.getEnctyptText());
        requestParams.put("rePayPassword", keyboard2.getEnctyptText());
        requestParams.put("phone", phone.getText().toString());

        //keyboard.clearInput();
        showLoading("正在加载...");

        LogUtils.d(TAG, "重置支付密码: requestParams--->" + requestParams.toString());
        sendPostRequest(requestParams, new ResultCallBack() {
            @Override
            public void onSuccess(String data) {
                dismissLoading();
                LogUtils.d(TAG, "重置支付密码: data--->" + data);
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
                LogUtils.d(TAG, "重置支付密码: errorMsg--->" + errorMsg);
            }

            @Override
            public void onFailure(String errorMsg) {
                dismissLoading();
                LogUtils.d(TAG, "重置支付密码: errorMsg--->" + errorMsg);
            }
        });

    }
}
