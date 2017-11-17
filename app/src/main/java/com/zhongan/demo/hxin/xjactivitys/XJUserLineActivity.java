package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;


/**
 * 用户协议页面
 */
public class XJUserLineActivity extends XJBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;//返回按钮
	private TextView mTitleView;//标题
	private WebView mWebView;
    private String url="",title="";
    private WebSettings mWebSettings;;
 
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_lines_layout);
		initViews();
	}

	@SuppressLint("NewApi") private void initViews() {
		 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		
		 mBackBtn.setOnClickListener(this);
		 mWebView=(WebView) findViewById(R.id.out_url_webview);
		mWebSettings = mWebView.getSettings();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) 
		{
			mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			 
		}
		mWebSettings.setBlockNetworkImage(false);	
		//支持javascript
		mWebSettings.setJavaScriptEnabled(true); 
		// 设置可以支持缩放 
		mWebSettings.setSupportZoom(true); 
		// 设置出现缩放工具 
		mWebSettings.setBuiltInZoomControls(true);
		//扩大比例的缩放
		mWebSettings.setUseWideViewPort(true);
		//自适应屏幕
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebSettings.setLoadWithOverviewMode(true);
		url = getIntent().getStringExtra("url");
		title=getIntent().getStringExtra("title");
		mTitleView.setText(title);
		if(url != null&&!url.isEmpty()){
			mWebView.loadUrl(url);
		}else{
			LoggerUtil.debug("url is null,please check the url!");
			
		}
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url) {
				view.loadUrl(url);
				return true;
			};
            @Override
            public void onReceivedSslError(WebView view,
            		SslErrorHandler handler, SslError error) {
            	// TODO Auto-generated method stub
            	super.onReceivedSslError(view, handler, error);
            	handler.proceed();
            }
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJUserLineActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
