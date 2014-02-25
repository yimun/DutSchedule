package com.siwe.dutschedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
	
	
	private static final String dateBegin = "2014-2-24";
	private static final String dateOver = "2014-8-31";

	/***
	 * 获取星期 周一为0 周日为6
	 * 
	 */
	public static int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
		dayOfWeek = dayOfWeek < 0 || dayOfWeek > 5 ? 6 : dayOfWeek;
		return dayOfWeek;
	}

	/***
	 * 计算当前的周数
	 * 
	 * @param
	 * @return int day
	 */
	public static int getWeekOfTerm() {

		Calendar begin = Calendar.getInstance();
		Calendar over = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);

		try {
			begin.setTime(sf.parse(dateBegin));
			over.setTime(sf.parse(dateOver));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		int currentWeek = current.get(Calendar.WEEK_OF_YEAR);
		int beginWeek = begin.get(Calendar.WEEK_OF_YEAR);
		int overWeek = over.get(Calendar.WEEK_OF_YEAR);
		if(currentWeek >= beginWeek && currentWeek <= overWeek)
			return currentWeek - beginWeek + 1;
		else 
			return 0;
	}

	
	// 获取当前格式化时间
	public static String getCurrent() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
				.format(new Date());
	}

	/**
	 * 格式化时间
	 * @param strdate
	 * @return
	 */
	public static String formatDateString(String strdate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Date current = new Date();
		Date date = null;
		try {
			date = sf.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return strdate;
		}
		StringBuilder sb = new StringBuilder();
		if (date.getYear() == current.getYear()) {
			if (date.getMonth() == current.getMonth()) {
				switch (current.getDate() - date.getDate()) {
				case 1:
					sb.append("昨天");
					break;
				case 2:
					sb.append("前天");
					break;
				case 0:
					if (date.getHours() < 12) {
						sb.append("上午");
					} else {
						sb.append("下午");
					}
					break;
				default:
					sb.append("MM-dd");
					break;
				}
			}else{
				sb.append("MM-dd");
			}
			sb.append(" HH:mm:ss");
		} else {
			return strdate;
		}
		return new SimpleDateFormat(sb.toString(), Locale.CHINA).format(date);

	}

}
