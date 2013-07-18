package com.siwe.dutschedule.infoGeter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class DoOnCookie {
	
	private static HashMap<String, String> CookieContiner = new HashMap<String, String>();

	/** * ±£´æCookie * @param resp */
	public static void SaveCookies(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getHeaders("Set-Cookie");
		//	String headerstr = headers.toString();
		//		if (headers == null)
		//			return;
		for (int i = 0; i < headers.length; i++) {
			String cookie = headers[i].getValue();
			String[] cookievalues = cookie.split(";");
			for (int j = 0; j < cookievalues.length; j++) {
				String[] keyPair = cookievalues[j].split("=");
				String key = keyPair[0].trim();
				String value = keyPair.length > 1 ? keyPair[1].trim() : "";
				CookieContiner.put(key, value);
			}
		}
	}

	/** * Ôö¼ÓCookie * @param request */
	@SuppressWarnings("rawtypes")
	public static void AddCookies(HttpGet httpRequest2) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			String val = entry.getValue().toString();
			sb.append(key);
			sb.append("=");
			sb.append(val);
			sb.append(";");
		}
		httpRequest2.addHeader("cookie", sb.toString());
	}

}
