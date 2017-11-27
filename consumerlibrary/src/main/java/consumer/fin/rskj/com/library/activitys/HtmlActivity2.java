package consumer.fin.rskj.com.library.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.views.TopNavigationView2;

public class HtmlActivity2 extends BaseActivity implements OnClickListener {

  public static final String TAG = HtmlActivity2.class.getSimpleName();
  private TopNavigationView2 navigationView;
  private WebView webView;
  private ProgressBar progressBar;
  /**
   * h5地址
   */
  private String url;
  private String richText;
  /**
   * 标题
   */
  private String title;

  @Override protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    url = getIntent().getStringExtra("url");
    richText = getIntent().getStringExtra("richText");
    title = getIntent().getStringExtra("title");
    setContentView(R.layout.activity_html2);
  }

  /**
   * 解决webview 退出后 还有播放声音
   */
  @Override protected void onPause() {
    webView.reload();
    super.onPause();
  }

  @Override public void init() {
    navigationView = (TopNavigationView2) findViewById(R.id.topbar);
    navigationView.setClickListener(new TopNavigationView2.NavigationViewClickListener() {
      @Override public void onLeftClick() {
        finish();
      }

      @Override public void onRightClick() {

      }
    });

    progressBar = (ProgressBar) findViewById(R.id.activity_load_progressBar);
    webView = (WebView) findViewById(R.id.activity_webView);

    webView.setWebChromeClient(new MyChromeWebViewClient());
    webView.setWebViewClient(new MyWebViewClient());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.addJavascriptInterface(this, "android");
    webView.getSettings().setDefaultTextEncodingName("UTF -8");
    webView.getSettings().setUseWideViewPort(true);//设置是当前html界面自适应屏幕
    webView.getSettings().setSupportZoom(true); //设置支持缩放
    webView.getSettings().setBuiltInZoomControls(false);//显示缩放控件
    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式

    if (!TextUtils.isEmpty(url)) {
      webView.loadUrl(url);
    }

    if (!TextUtils.isEmpty(richText)) {
      navigationView.setTitle(title);
      webView.loadDataWithBaseURL("", richText, "text/html", "UTF-8", "");
    }
  }

  @JavascriptInterface public void showAndroidPdf(String url) {
    //Toast.makeText(HtmlActivity2.this, ">>>>" + url, Toast.LENGTH_SHORT).show();
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      default:
        break;
    }
  }

  class MyChromeWebViewClient extends WebChromeClient {
    @Override public void onProgressChanged(WebView view, int newProgress) {
      progressBar.setProgress(newProgress);
      if (newProgress == 100) {
        progressBar.setVisibility(View.GONE);
      }
      super.onProgressChanged(view, newProgress);
    }

    @Override public void onReceivedTitle(WebView view, String title) {
      super.onReceivedTitle(view, title);
      navigationView.setTitle(title);
      //Toast.makeText(HtmlActivity2.this, "title = " + title, Toast.LENGTH_SHORT).show();
    }
  }

  class MyWebViewClient extends WebViewClient {

    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if (url != null) {
        if (url.startsWith("tel:")) {
          Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
          if (ActivityCompat.checkSelfPermission(HtmlActivity2.this, Manifest.permission.CALL_PHONE)
              != PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
          }
        } else if (url.startsWith("http:") || url.startsWith("https:")) {
          view.loadUrl(url);
        }

        // 如果想继续加载目标页面则调用下面的语句
        //		 view.loadUrl(url);
        //		 ProductCenterH5.this.url = url;
        //		 LogUtils.Log("AAA", "after" + url);
        //		 handler.removeMessages(0);
        //handler.sendEmptyMessage(0);
        // 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
      }
      // 返回true表示停留在本WebView（不跳转到系统的浏览器）
      return true;
    }

    @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {  //网页加载开始
      super.onPageStarted(view, url, favicon);
    }

    @Override public void onPageFinished(WebView view, String url) {//网页加载完成
      super.onPageFinished(view, url);
    }
  }
}
