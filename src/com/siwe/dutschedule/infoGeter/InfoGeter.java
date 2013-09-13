package com.siwe.dutschedule.infoGeter;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;

public abstract class InfoGeter {

	protected String FirstUrl;
	protected String SecondUrl;
	protected Context context;
	private String result1;
	private String result2;
	
	public InfoGeter(Context cont){
		this.context = cont;
	}

	public boolean getTwoPageInfo() {

		// 第一次登陆/////////////////////////
		HttpGet httpRequest = new HttpGet(FirstUrl);
		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			DoOnCookie.SaveCookies(httpResponse);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result1 = EntityUtils.toString(httpResponse.getEntity());
			}
			if(isLogined(result1)){  //判别是否登录成功
				System.out.println("登陆成功");
				//二次定向/////////////////////////////////////////////////////
				HttpGet httpRequest2 = new HttpGet(SecondUrl);
				DoOnCookie.AddCookies(httpRequest2);
				HttpResponse httpResponse2 = new DefaultHttpClient().execute(httpRequest2);
				if (httpResponse2.getStatusLine().getStatusCode() == 200) {
					result2 = EntityUtils.toString(httpResponse2.getEntity());
					result2 = format(result2);
					dealStr(result2);
					return true;
				}
			}
			else{
				System.out.println("用户名密码错误");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("ClientProtocolException e");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException e");
			e.printStackTrace();
		}
		return false;

	}
	
	public boolean getOnePageInfo(){
		HttpGet httpRequest = new HttpGet(FirstUrl);
		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result2 = EntityUtils.toString(httpResponse.getEntity());
				result2 = format(result2);
				dealStr(result2);
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("ClientProtocolException e");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException e");
			e.printStackTrace();
		}
		return false;
	}

	
	
	/**
	 * 是否登录成功
	 * @param result
	 * @return 
	 */
	private boolean isLogined(String result) {
		
		String flag = "学分制综合教务"; //登陆成功的标志
		result = Regular.eregi_replace("(\\s)*", "", result);
		result = Regular.eregi_replace("<html>(.)*?<title>", "", result);
		result = Regular.eregi_replace("</title>(.)*?</html>", "", result);
		return result.equalsIgnoreCase(flag);
		
	}

	/**
	 * 对获取字符串进行处理
	 * @param str
	 */
	public abstract void dealStr(String str);
	
	
	/**
	 * 网页字符串的格式化方法
	 * @param origStr
	 * @return 
	 */
	public abstract String format(String origStr);


	

}
