package com.siwe.dutschedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.siwe.dutschedule.model.Schedule;

import android.content.Context;
import android.content.SharedPreferences;

public class TimeUtil {
	
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

	/**获取学期开始结束的日期*/
	private static Calendar[] getTermDate(Context ctx){
		Calendar[] cal = new Calendar[2];
		cal[0] = Calendar.getInstance();
		cal[1] = Calendar.getInstance();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		SharedPreferences sp = AppUtil.getSharedPreferences(ctx);
		String dateBegin = sp.getString("termbegin", "2014-2-24");
		String dateOver  = sp.getString("termover", "2014-6-22");
		System.out.println("#####begin="+dateBegin+"over="+dateOver);
		try {
			cal[0].setTime(sf.parse(dateBegin));
			cal[1].setTime(sf.parse(dateOver));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("#####getTermDate() exception");
			return null;
		}
		return cal;
	}
	/***
	 * 计算当前的周数
	 * 
	 * @param
	 * @return int day
	 */
	public static int getWeekOfTerm(Context context) {

	
		Calendar current = Calendar.getInstance();
		Calendar[] cal = getTermDate(context);
		if(cal == null)
			return 0;
		long b = cal[0].getTimeInMillis();
		long o = cal[1].getTimeInMillis();
		long cur = current.getTimeInMillis();
		if(cur >= b && cur <=o ){		//判断是否在本学期内
			long d = (cur-b)/(1000*3600*24); 
			int weekOfTerm = (int)d/7+1;// 需要加1
			return weekOfTerm;
		}else{
			return 0;
		}
		/*long d = (o-b)/(1000*3600*24); 
		int currentWeek = current.get(Calendar.WEEK_OF_YEAR);
		int beginWeek = cal[0].get(Calendar.WEEK_OF_YEAR);
		int overWeek = cal[1].get(Calendar.WEEK_OF_YEAR);
		System.out.println("#####curr="+currentWeek+"beginWeek="+beginWeek+"overWeek="+overWeek);
		if(currentWeek >= beginWeek && currentWeek <= overWeek)
			return currentWeek - beginWeek + 1;
		else 
			return 0;*/
	}
	
	/**
	 * 获取上课总周数
	 * @return
	 */
	public static int getAllTermWeeks(Context context){
		Calendar[] cal = getTermDate(context);
		if(cal == null)
			return 0;
		long b = cal[0].getTimeInMillis();
		long o = cal[1].getTimeInMillis();
		long d = (o-b)/(1000*3600*24); 
		return (int)d/7+1;
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
	
	// 判断课程是否在上课周内
	public static boolean judgeIsTime(Schedule item,int weekToShow) {
		try {
			String weeks = item.getWeeks();
			Matcher matcher = Pattern.compile("\\d+").matcher(weeks);
			int from,to;
			from = 50;
			to = 0;
			while (matcher.find()){
				String s = matcher.group();
				int x = Integer.valueOf(s);
				if(x < from)
					from = x;
				if(x > to)
					to = x;
			}
			if (weekToShow < from || weekToShow > to)
				return false;
			else
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

}
