package consumer.fin.rskj.com.library.activitys;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

import consumer.fin.rskj.com.consumerlibrary.R;

public class MaSHHActivity extends BaseActivity {

    private static final String TAG = "YYYUUU";
    private EditText editText;

    private  static final String BASE_URL = "http://test.xmqq99.com/msh/device/active";
//    private  static final String BASE_URL = "http://10.2.11.241:8080/qqjf-api-supplementary/device/active";

    private  String deviceCode ;
    private String recommandP;

    private Dialog updateDialog;//更新提示框
    private String fileUrl;//文件下载路径
    private String uopdateMsg;
    private int isrelease = -1;


    //参数传递给众安
    private void send2ZA(String data) {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("partnerNo", "8016112151261010");
//            json.put("channel", "M0000003");
//            json.put("UID", data);
//            json.put("recommandCode", recommandP+"");
//            json.put("userPhone",MyAppLication.getSP(getApplicationContext()).getPhone());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        FinanceInitor.setBusinessInfo(json.toString());
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("zaf://xyfq.entrance"));
//        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mashh);

        deviceCode = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG,"deviceCode = " + deviceCode);
        Log.d(TAG,"BRAND = " + Build.BRAND);
        Log.d(TAG,"MODEL = " + Build.MODEL);

//        editText = (EditText) findViewById(R.id.edit_recommend);
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LogUtils.d(TAG,"memberId = " + MyApplication.getSP(getApplicationContext()).getMemId());
//                send2ZA(MyApplication.getSP(getApplicationContext()).getMemId());
//            }
//        });

    }


    @Override
    public void init() {

    }
}
