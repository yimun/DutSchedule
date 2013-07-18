package com.siwe.dutschedule.setting_edit;

import android.database.sqlite.SQLiteDatabase;


public class ClearAll {
	
	public final static String TABLE_NAME = "classes";// 表1名
	public final static String Day = "day";//星期几
	public final static String No = "no";//第几节课
	public final static String Name = "name";//课名
	public final static String ClassId = "classid";//课程名
	public final static String Weeks = "weeks";//上课周数
	public final static String Address = "address";//上课地点

	
	public void clear(SQLiteDatabase mysql){
		clearClasses(mysql);
		clearScoreAndNoticeAndTest(mysql);
	}
	
	public void clearClasses(SQLiteDatabase mysql){
		for (int i = 1; i < 8; i++)
			for (int j = 1; j < 10; j += 2) {
				// db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES("+i+","+j+","+i+","+j+");");
				mysql.execSQL("update " + TABLE_NAME +" set " + ClassId + "='"
						+ "无" + "', " + Name + "='" +"无"
						+ "'," + Weeks + "='" +"无"+ "'," + Address
						+ "='" +"无"+ "' where " + Day
						+ "='" + i + "' and " + No + "='"
						+ j + "';");;

			}
	}
	
	public void clearScoreAndNoticeAndTest(SQLiteDatabase mysql){
		mysql.execSQL("delete from scores");
		mysql.delete("notices", null, null);
		mysql.delete("tests", null, null);
		//mysql.delete("scores",null,null);
	}
	

}
