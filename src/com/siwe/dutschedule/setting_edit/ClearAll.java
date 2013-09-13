package com.siwe.dutschedule.setting_edit;

import android.database.sqlite.SQLiteDatabase;


public class ClearAll {
	
	public final static String TABLE_NAME = "classes";// 表1名

	
	public static void clear(SQLiteDatabase mysql){
		clearClasses(mysql);
		clearScoreAndNoticeAndTest(mysql);
	}
	
	public static void clearClasses(SQLiteDatabase mysql){

		mysql.delete("classes",null,null);
		for (int i = 1; i < 8; i++)
			for (int j = 1; j < 10; j += 2) {
				mysql.execSQL("insert into classes values(" + i
						+ "," + j + ",'无','无','无','无','无','无','无');");
			}
	}
	
	public static void clearScoreAndNoticeAndTest(SQLiteDatabase mysql){
		mysql.execSQL("delete from scores");
		mysql.delete("notices", null, null);
		mysql.delete("tests", null, null);
		//mysql.delete("scores",null,null);
	}
	

}
