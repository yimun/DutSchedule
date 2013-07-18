package com.siwe.dutschedule.infoGeter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular {
	//ÕıÔòËã·¨
		public static String eregi_replace(String string, String string2,String strresult) {
			// TODO Auto-generated method stub
		//	String strPattern = "(?i)" + string;
			String strPattern = string;
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(strresult);
			if (m.find()) {
				return strresult.replaceAll(string, string2);
			} else {
				return strresult;
			}
		}

}
