package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.SetBackgroundImage;
import com.umeng.analytics.MobclickAgent;

public class WebviewActivity extends Activity {

	WebView webview;
	TextView tv;
	ProgressBar progressBar;
	String url,title ;
	RelativeLayout ll;
	GestureDetector detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		ll = (RelativeLayout)findViewById(R.id.weblayout);
		SetBackgroundImage.setTheme(WebviewActivity.this, ll,null);
		detector = new GestureDetector(this, new backGestureListener(this));
		webview = (WebView)findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true); 
		webview.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
		webview.getSettings().setSupportZoom(true); //可以缩放
		webview.getSettings().setDefaultZoom(ZoomDensity.CLOSE);//默认缩放模式 是 ZoomDensity.MEDIUM
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		tv = (TextView)findViewById(R.id.maintitle);
		Bundle extras = getIntent().getExtras();  
		if (extras != null) {	
			url = extras.getString("url");
			title = extras.getString("title");
		}
		System.out.println(title + url);
		tv.setText(title);
	}

	@Override  
    public boolean onTouchEvent(MotionEvent me)  
    {  
		return detector.onTouchEvent(me);  
    }  
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
	
	
	@Override
	public void onResume(){
		super.onResume();
		MobclickAgent.onResume(this);
		webview.setWebViewClient(new MyWebViewClient());
		webview.loadUrl(url);
	
	}
	


	public class MyWebViewClient extends WebViewClient{
		@Override 
		public boolean shouldOverrideUrlLoading(WebView view,String url){    
			view.loadUrl(url);    
			return true;    
		}
		//开始加载   
		@Override  
		public void onPageStarted(WebView view, String url, Bitmap favicon){  
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);

		}  

		//结束加载   
		@Override  
		public void onPageFinished(WebView view, String url){  
			super.onPageFinished(view, url); 
			progressBar.setVisibility(View.GONE);
			
		}  
	}
	@Override 
	public void onPause(){
		super.onPause();
		 MobclickAgent.onPause(this);
		System.out.println("调用webView中的onPause");
		CookieManager.getInstance().removeAllCookie();
		webview.clearCache(true);
		System.gc();
	}

	public boolean onKeyDown(int keyCoder,KeyEvent event){  
		if(webview.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){  
			webview.goBack();   //goBack()表示返回webView的上一页面  
			return true;  
		}
		else if(keyCoder == KeyEvent.KEYCODE_BACK){
			finish();
			return true; 
		}
		return false;  
	}  
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();    
		inflater.inflate(R.menu.webview_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}*/
	public void bt_back(View v){
		finish();
	}

}
