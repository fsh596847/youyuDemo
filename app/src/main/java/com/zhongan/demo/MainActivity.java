package com.zhongan.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.umeng.analytics.MobclickAgent;
import com.zhongan.demo.util.LogUtils;
import com.zhongan.finance.common.FinanceInitor;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    private static final String TAG = "YYYUUU";
    private EditText editText;

    private  static final String BASE_URL = "http://test.xmqq99.com/msh/device/active";
//    private  static final String BASE_URL = "http://10.2.11.241:8080/qqjf-api-supplementary/device/active";

    private  String deviceCode ;
    private String recommandP;


    //参数传递给众安
    private void send2ZA(String data) {

        /**
         * UID = "f9dd3a46-b5cf-445c-9937-e194bf0ee064";
         channel = M0000003;
         partnerNo = 8016112151261010;
         */
        JSONObject json = new JSONObject();
        try {
            json.put("partnerNo", "8016112151261010");
            json.put("channel", "M0000003");
            json.put("UID", data);
            json.put("recommandCode", recommandP+"");
            json.put("userPhone",MyApplication.getSP(getApplicationContext()).getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FinanceInitor.setBusinessInfo(json.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("zaf://xyfq.entrance"));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        deviceCode = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG,"deviceCode = " + deviceCode);
        Log.d(TAG,"BRAND = " + android.os.Build.BRAND);
        Log.d(TAG,"MODEL = " + android.os.Build.MODEL);

        editText = (EditText) findViewById(R.id.edit_recommend);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(MainActivity.this, "click_msh", "xyfq.entrance");
                //getCode();
                LogUtils.Log(TAG,"memberId = " + MyApplication.getSP(getApplicationContext()).getMemId());
                send2ZA(MyApplication.getSP(getApplicationContext()).getMemId());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("zaf://xyfq.entrance"));
//                startActivity(intent);
            }
        });


        Log.d(TAG,"获取版本信息" );
        //getUpdateInfo();
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(null != handler){
//            handler.removeCallbacksAndMessages(null);
//        }
//    }
}
