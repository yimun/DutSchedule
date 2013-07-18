package com.siwe.dutschedule.widgetProvider;
/** 
 * @author Zhanglinwei
 * @version 2013/3/09
 * 小部件的数据连接
 */ 


import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.siwe.dutschedule.activity.MySQLiteOpenHelper;
import com.umeng.analytics.MobclickAgent;

public class Link extends Activity {
	private MySQLiteOpenHelper myOpenHelper;
	private SQLiteDatabase mysql ;


	/***
	 * 获取小部件上当天的课程
	 * @param 
	 * @return infoArry[0]课程名
	 *     	   infoArry[1]课程号 
	 *         infoArry[2]上课周次 
	 *         infoArry[3]上课地点 
	 */
	public String[] doSelect() {


		try {
			System.out.println("开始实例化数据库2-----1");
			myOpenHelper = new MySQLiteOpenHelper(this);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("1实例化shibai");
		}


		try {
			//#########################    BUG
			//###    此处因不在程序的进程之中必须使用绝对路径否则将无法打开数据库
			
			//仍有空指针异常但是数据库能够正常打开？？？？？
			mysql = SQLiteDatabase.openOrCreateDatabase("data/data/com.siwe.dutschedule/databases/schedule.db", null);
			System.out.println("开始实例化数据库2----2");
			//		mysql = this.openOrCreateDatabase("schedule.db",MODE_PRIVATE, null);
			mysql = myOpenHelper.getReadableDatabase();
			//		myOpenHelper.onCreate(mysql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("2实例化shibai");
		}   


		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		dayOfWeek = dayOfWeek < 1 || dayOfWeek > 6 ? 7 : dayOfWeek;
		System.out.println(dayOfWeek);

		String[] infoArry = myOpenHelper.selectWithDay(mysql, dayOfWeek);
		System.out.println("get successful");
		return infoArry;
	}

	public void doClose() {
		System.out.println("doClose");
		if (mysql.isOpen())
			mysql.close();
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
}
