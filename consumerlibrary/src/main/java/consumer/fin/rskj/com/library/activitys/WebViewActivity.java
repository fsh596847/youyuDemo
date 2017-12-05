package consumer.fin.rskj.com.library.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.message.MainMessage;
import consumer.fin.rskj.com.library.message.NetMessage;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SysUtil;
import consumer.fin.rskj.com.library.utils.Util;
import consumer.fin.rskj.com.library.views.MyWebView;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;
import static consumer.fin.rskj.com.library.utils.SysUtil.isNetworkConnected;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

  public static final String TAG = WebViewActivity.class.getSimpleName();
  public static Map<String, String> stepParams = new HashMap<>();

  static {
    /**
     0 ocr
     1 验4
     2 face++
     3 基本信息
     */
    stepParams.put("0", "consumer.fin.rskj.com.library.activitys.FaceIDCardInfoUploadActivity");
    stepParams.put("1", "consumer.fin.rskj.com.library.activitys.DealSelfInfoActivity");
    stepParams.put("2", "consumer.fin.rskj.com.library.activitys.FaceStartActivity");
    stepParams.put("3", "consumer.fin.rskj.com.library.activitys.BasicInfoActivity");
  }

  private Button mbackBtn;
  private TextView mTitleTv;
  /**
   * 头部右侧按钮
   */
  private TextView right_btn;
  Drawable rightImage;
  private ProgressBar mProgressbar;
  private MyWebView mWebview;

  private String url = "", title = "", id = "", fundId = "";
  private Intent intent;
  private long curMillions = 0;

  CallBackFunction pwdFunction;
  /**
   * 是否显示个人中心
   */
  private String showUserCenter = "-1";

  private String naviState;

  BackCallBack backCallBack;

  Handler handler = new Handler() {
    @Override public void dispatchMessage(Message msg) {
      if (msg.what == 0) {
        try {
          String data = (String) msg.obj;
          JSONObject js = new JSONObject(data);
          LogUtils.d(TAG, "showUserCenter = " + js.getString("showUserCenter"));
          if ("1".equals(js.getString("showUserCenter"))) {
            right_btn.setText("");

            rightImage = getResources().getDrawable(R.mipmap.rskj_person_center);
            rightImage.setBounds(0, 0, rightImage.getMinimumWidth(), rightImage.getMinimumHeight());
            right_btn.setCompoundDrawables(null, null, rightImage, null);

            right_btn.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                intent = new Intent(WebViewActivity.this, PersonCenterActivity.class);
                startActivity(intent);
              }
            });
          } else {
            right_btn.setText("关闭");
            right_btn.setCompoundDrawables(null, null, null, null);
            right_btn.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                finish();
                //showToast("关闭" , Constants.TOAST_SHOW_POSITION);
              }
            });
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview);
    EventBus.getDefault().register(this);//注册
    url = getIntent().getStringExtra("url");
    title = getIntent().getStringExtra("title");
    id = getIntent().getStringExtra("id");
    fundId = getIntent().getStringExtra("fundId");
    backCallBack = new BackCallBack() {
      @Override public void backCallBack() {
        mWebview.loadUrl(url);
      }
    };
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
  }

  /**
   * 解决webview 退出后 还有播放声音
   */
  @Override protected void onPause() {
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
    if (!isNetworkConnected(this)) {
      //            showNetDialog(backCallBack);
    } else {
      netDismiss();
    }
  }

  /**
   * 打开一个外部链接
   **/
  public static void openWebViewActivity(Context context, String title, String url) {
    Intent intent = new Intent(context, WebViewActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("title", title);
    context.startActivity(intent);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);//解除注册
    destoryDialog();
    mWebview.destroy();
  }

  /**
   * ui主线程中执行
   */
  @Subscribe(threadMode = ThreadMode.MAIN) public void onMainEventBus(MainMessage msg) {
    //提前加载页面，然后跳转
    LogUtils.d(TAG, "======msg = " + msg.toString());
    url = msg.getUrl();
    title = msg.getTitle();
    mWebview.loadUrl(getResources().getString(R.string.dialog_loading));
    mWebview.setVisibility(View.INVISIBLE);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onMainEventBus(NetMessage msg) {
    //提前加载页面，然后跳转
    LogUtils.d(TAG, "======isAvaiable = " + msg.isAvaiable);
    if (msg.isAvaiable) {
      netDismiss();
    } else {
      //showNetDialog(backCallBack);
    }
  }

  @Override public void init() {
    mbackBtn = (Button) findViewById(R.id.left_btn);
    mTitleTv = (TextView) findViewById(R.id.center_title);
    right_btn = (TextView) findViewById(R.id.right_btn);
    mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
    mWebview = (MyWebView) findViewById(R.id.webview);
    mWebview.setTextView(mTitleTv);
    WebSettings settings = mWebview.getSettings();
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }
    settings.setBlockNetworkImage(false);
    settings.setJavaScriptEnabled(true);
    settings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
    settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
    // 设置可以支持缩放
    settings.setSupportZoom(false);
    /**
     * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
     LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
     LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
     LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
     */
    settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
    //扩大比例的缩放
    settings.setUseWideViewPort(false);
    settings.setDomStorageEnabled(true);
    //自适应屏幕
    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    settings.setLoadWithOverviewMode(true);
    mWebview.setDefaultHandler(new DefaultHandler());
    mWebview.clearCache(true);
    url = getIntent().getStringExtra("url");
    title = getIntent().getStringExtra("title");
    mTitleTv.setText(title);
    right_btn.setText("关闭");
    mProgressbar.setMax(100);
    mWebview.setWebChromeClient(new WebChromeClient() {

      @Override public void onProgressChanged(WebView view, int newProgress) {
        LogUtils.d("debug", "onProgressChanged -------->" + newProgress);
        mProgressbar.setProgress(newProgress);
        if (newProgress >= 100) {
          mProgressbar.setVisibility(View.GONE);
          mWebview.setVisibility(View.VISIBLE);
          dismissLoading();
          setShowUserCenter();
          //控制个人中心是否显示
          mWebview.callHandler("naviState", "", new CallBackFunction() {
            @Override public void onCallBack(String data) {
              LogUtils.d(TAG, "data = " + data);
              naviState = data;
              try {
                JSONObject js = new JSONObject(data);
                showUserCenter = js.getString("showUserCenter");
                LogUtils.d(TAG, "showUserCenter2 = " + showUserCenter);
                setShowUserCenter();
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          });
        }
      }

      @Override public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mWebview.addTitle(title);
        LogUtils.d(TAG, "=====onReceivedTitle====title " + title);
        mTitleTv.setText(title);
        showUserCenter = "-1";
        setShowUserCenter();
      }
    });

    setCookie();
    mWebview.loadUrl(url);
    mbackBtn.setOnClickListener(this);
    LogUtils.d(TAG, "getSessionID：" + sharePrefer.getSessionID());
    registerAction();
  }

  private void setShowUserCenter() {

    LogUtils.d(TAG, "showUserCenter = " + showUserCenter);

    if ("1".equals(showUserCenter)) {
      right_btn.setText("");

      rightImage = getResources().getDrawable(R.mipmap.rskj_person_center);
      rightImage.setBounds(0, 0, rightImage.getMinimumWidth(), rightImage.getMinimumHeight());
      right_btn.setCompoundDrawables(null, null, rightImage, null);

      right_btn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          intent = new Intent(WebViewActivity.this, PersonCenterActivity.class);
          intent.putExtra("naviState", naviState);
          startActivity(intent);
        }
      });
    } else {
      right_btn.setText("关闭");
      right_btn.setCompoundDrawables(null, null, null, null);
      right_btn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          //showToast("关闭" , Constants.TOAST_SHOW_POSITION);
          finish();
        }
      });
    }
  }

  private void registerAction() {
    // 第一参数是订阅的java本地函数名字 第二个参数是回调Handler ,
    // 参数返回js请求的resqustData,function.onCallBack（）回调到js，
    // 调用function(responseData)
    mWebview.registerHandler("registerAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        HashMap map = new HashMap<String, String>();
        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("clientToken","")
        map.put("clientToken", sharePrefer.getToken());
        map.put("fundId", /*"5a32c168fc3345ef8bcfd41c6192789e"*//*sharePrefer.getFundId15()*/
            fundId);
        map.put("id", /*"c0472544cb05404f949a414aa222183a"*//*sharePrefer.getId15()*/id);
        //Toast.makeText(WebViewActivity.this,Util.hashMapToJson(map),Toast.LENGTH_SHORT).show();
        function.onCallBack(Util.hashMapToJson(map));
      }
    });

    //第三个页面-- 客户状态
    mWebview.registerHandler("creitAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        LogUtils.d(TAG, "data = " + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          String productId = jsonObject.getString("productId");
          String fundId = jsonObject.getString("fundId");
          String inputPages = jsonObject.getString("inputPages");
          //                    String contractType = jsonObject.getString("contractType");
          String userStateInfo = jsonObject.getString("userStateInfo");

          String contractList = jsonObject.getString("rows");
          LogUtils.d(TAG, "contractList = " + contractList);//验4 相关协议
          sharePrefer.setRows(contractList);

          if (!TextUtils.isEmpty(inputPages)) {
            String[] step = inputPages.split(",");
            LogUtils.d(TAG, "setp = " + step);
            SysUtil.stepMap = new String[step.length];
            for (int i = 0; i < step.length; i++) {
              if (stepParams.containsKey(step[i])) {
                LogUtils.d(TAG, "key = " + step[i]);
                LogUtils.d(TAG, "value = " + stepParams.get(step[i]));
                //stepMap.put(item,stepParams.get(item));
                SysUtil.stepMap[i] = stepParams.get(step[i]);//记录 进件流程步骤
              }
            }

            LogUtils.d(TAG, "初始化后的数组 = " + SysUtil.stepMap);
          }

          sharePrefer.setXJProductId(productId);
          sharePrefer.setXJFundId(fundId);

          String nextUserState = jsonObject.getString("nextUserState");

          LogUtils.d(TAG, "productId = " + productId);
          LogUtils.d(TAG, "fundId = " + fundId);
          LogUtils.d(TAG, "inputPages = " + inputPages);
          LogUtils.d(TAG, "userStateInfo = " + userStateInfo);

          try {
            //补充信息页面
            String replenisherPages = jsonObject.getString("replenisherPages");
            LogUtils.d(TAG, "replenisherPages = " + replenisherPages);
            if (!TextUtils.isEmpty(replenisherPages)) {
              sharePrefer.setReplenisherPages(replenisherPages);
            }
          } catch (Exception e) {

          }

          goPage(nextUserState);
        } catch (JSONException e) {
          LogUtils.d(TAG, "JSONException = " + e.getMessage());
        } catch (Exception e) {
          LogUtils.d(TAG, "Exception = " + e.getMessage());
        }
      }
    });

    //当期
    mWebview.registerHandler("currentAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        //当期，逾期并传入原生状态，1为当前，2为逾期

        intent = new Intent(WebViewActivity.this, MyBillListActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
      }
    });

    //去还款
    mWebview.registerHandler("repaymentAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        intent = new Intent(WebViewActivity.this, LoanListActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
      }
    });

    //查看借还款记录
    mWebview.registerHandler("loanRecordAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        intent = new Intent(WebViewActivity.this, RecordListActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
      }
    });

    //补充信息页面
    mWebview.registerHandler("informationAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        showToast(data, Constants.TOAST_SHOW_POSITION);
      }
    });

    //授信状态
    mWebview.registerHandler("creditApprovalAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        showToast(data, Constants.TOAST_SHOW_POSITION);
      }
    });

    //借款第一个页面 借款借据
    mWebview.registerHandler("loanBillAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        intent = new Intent(WebViewActivity.this, LoanCredentialsActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
      }
    });

    //借款成功
    mWebview.registerHandler("loanStateAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        showToast(data, Constants.TOAST_SHOW_POSITION);
      }
    });

    //还款成功
    mWebview.registerHandler("repayStateAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        showToast(data, Constants.TOAST_SHOW_POSITION);
      }
    });

    //重新登录
    mWebview.registerHandler("reLoginAction", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        showToast("登录失效 = " + data, Constants.TOAST_SHOW_POSITION);
      }
    });

    //马上还
    mWebview.registerHandler("openMaShangHuan", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        intent = new Intent(WebViewActivity.this, MaSHHActivity.class);
        startActivity(intent);
      }
    });

    //设置交易密码
    mWebview.registerHandler("setTradingPassword", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        pwdFunction = function;
        intent = new Intent(WebViewActivity.this, SetPayPWDActivity.class);
        startActivityForResult(intent, 30);
      }
    });

    //获取位置
    mWebview.registerHandler("getLocation", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {

        function.onCallBack(getLocation());
      }
    });

    mWebview.registerHandler("showLoading", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        //showLoading(data);
      }
    });

    mWebview.registerHandler("dismissLoading", new BridgeHandler() {
      @Override public void handler(String data, CallBackFunction function) {
        //dismissLoading();
      }
    });
  }

  private void setCookie() {
    CookieJar cookieJar = OkHttpUtil.getDefault().getDefaultClient().cookieJar();
    HttpUrl url1 = HttpUrl.parse(url);
    List<Cookie> cookies = cookieJar.loadForRequest(url1);
    StringBuilder sb = new StringBuilder();
    for (Cookie cookie : cookies) {
      sb.append(cookie.toString());
      sb.append(";");
    }
    String strCookies = sb.toString();

    Log.d(TAG, "strCookies = " + strCookies);

    android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    cookieManager.removeSessionCookie();//移除
    cookieManager.setCookie(url, strCookies);
    android.webkit.CookieSyncManager.getInstance().sync();
    String CookieStr = cookieManager.getCookie(url);

    Log.d(TAG, "CookieStr = " + CookieStr);
  }

  @Override public void onClick(View view) {
    if (view.getId() == R.id.left_btn) {
      if (mWebview.getUrl().equals(sharePrefer.getFirstUrl())) {
        finish();
      } else {
        if ("1".equals(showUserCenter)) {
          mWebview.loadUrl(BASE_URL + sharePrefer.getApplyLoan());
        } else {

          if (mWebview.canGoBack()) {
            mWebview.goBack();
          } else {
            finish();
          }
        }
      }
    }
  }

  /**
   * 重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
   */
  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
      if (mWebview.getUrl().equals(sharePrefer.getFirstUrl())) {
        finish();
      } else {
        if ("1".equals(showUserCenter)) {
          mWebview.loadUrl(BASE_URL + sharePrefer.getApplyLoan());
        } else {
          if (mWebview.canGoBack()) {
            mWebview.goBack();
          } else {
            finish();
          }
        }
      }
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 30 && resultCode == RESULT_OK) {
      if (data != null) {
        LogUtils.d(TAG, "status = " + data.getStringExtra("status"));
        pwdFunction.onCallBack(data.getStringExtra("status"));
      }
    }
  }

  public interface BackCallBack {
    void backCallBack();
  }
}
