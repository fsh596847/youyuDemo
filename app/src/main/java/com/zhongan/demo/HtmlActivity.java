package com.zhongan.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zhongan.demo.util.LogUtils;
import com.zhongan.demo.view.TopNavigationView;


public class HtmlActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = "HtmlActivity";
	private TopNavigationView navigationView;
	private WebView webView;
	private ProgressBar progressBar;
	private String url ;//h5地址
	private String richText;

	private String title;//标题

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_html);

		url = getIntent().getStringExtra("url");

		LogUtils.Log(TAG,"url == " + url);
		richText = getIntent().getStringExtra("richText");
		title = getIntent().getStringExtra("title");

		setUI();
	}


	@Override
	protected void onPause () {//解决webview 退出后 还有播放声音
		webView.reload ();

		super.onPause ();
	}

	protected void setUI() {
		navigationView = (TopNavigationView) findViewById(R.id.topbar);
//		navigationView.setTitle("title");
		navigationView.setClickListener(new TopNavigationView.NavigationViewClickListener() {
			@Override
			public void onLeftClick() {
				if( webView.canGoBack()) {
					webView.goBack();
				}else {
					finish();
				}
			}

			@Override
			public void onRightClick() {

			}
		});

		progressBar = (ProgressBar) findViewById(R.id.activity_load_progressBar);
		webView = (WebView) findViewById(R.id.activity_webView);

		//测试js 调用
		//webView.loadUrl("file:///android_asset/test.html");

		webView.setWebChromeClient(new MyChromeWebViewClient());
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(this, "android");
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("UTF -8");

		webView.getSettings().setUseWideViewPort(true);//设置是当前html界面自适应屏幕
		webView.getSettings().setSupportZoom(true); //设置支持缩放
		webView.getSettings().setBuiltInZoomControls(false);//显示缩放控件
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式

		if(!TextUtils.isEmpty(url)){
			webView.loadUrl(url);
		}

		if(!TextUtils.isEmpty(richText)){
			navigationView.setTitle(title);
			webView.loadDataWithBaseURL("", richText, "text/html", "UTF-8","");
		}

	}


	@JavascriptInterface
	public void showAndroidPdf(String url) {
		//Toast.makeText(HtmlActivity.this, ">>>>" + url, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			default:
				break;
		}
	}

	class MyChromeWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progressBar.setProgress(newProgress);
			if (newProgress == 100) {
				progressBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			navigationView.setTitle(title);
			//Toast.makeText(HtmlActivity.this, "title = " + title, Toast.LENGTH_SHORT).show();
		}
	}

	class MyWebViewClient extends WebViewClient {


		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url != null) {
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
					if (ActivityCompat.checkSelfPermission(HtmlActivity.this,
							Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						startActivity(intent);
					}
				} else if(url.startsWith("http:") || url.startsWith("https:")){
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

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {  //网页加载开始
			super.onPageStarted(view, url, favicon);
			LogUtils.Log(TAG,"------onPageStarted------" + url);

		}

		@Override
		public void onPageFinished(WebView view, String url) {//网页加载完成
			super.onPageFinished(view, url);
			LogUtils.Log(TAG,"------onPageFinished------" + url);
		}


	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
		if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
