package com.siwe.dutschedule.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;

public class UiWebView extends BaseUi {

	private WebView webview;
	private String url, title;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_webview);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("url");
			title = extras.getString("title");
		}
		this.initActionBar();
		webview = (WebView) findViewById(R.id.webView1);
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true); // 显示放大缩小 controler
		webview.getSettings().setSupportZoom(true); // 可以缩放
		webview.getSettings().setDefaultZoom(ZoomDensity.CLOSE);// 默认缩放模式 是
																// ZoomDensity.MEDIUM
		webview.setWebViewClient(new MyWebViewClient());
		webview.loadUrl(url);
	}

	private void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle(title);
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);
		actionBar.bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doFinish();
			}
		});
		actionBar.bt_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				webview.reload();
			}
		});
		
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		// 开始加载
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			showLoadBar();

		}

		// 结束加载
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			hideLoadBar();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		CookieManager.getInstance().removeAllCookie();
		webview.clearCache(true);
		System.gc();
	}

	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (webview.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			webview.goBack(); // goBack()表示返回webView的上一页面
			return true;
		} else if (keyCoder == KeyEvent.KEYCODE_BACK) {
			doFinish();
			return true;
		}
		return false;
	}
}
