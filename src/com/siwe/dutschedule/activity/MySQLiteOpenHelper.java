package com.siwe.dutschedule.activity;

/** 
 * @author Zhanglinwei
 * @version 2013/3/05
 * 数据库助手
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	// 课程表1数据
	public final static int VERSION = 1;// 版本号
	public final static String TABLE_NAME = "classes";// 表1名
	public final static String ClassId = "classid";// 课程号
	public final static String Name = "name";// 课名
	public final static String Point = "point";//  学分
	public final static String Type = "type"; //修读类型
	public final static String Teacher = "teacher"; //教师名字
	public final static String Weeks = "weeks";// 上课周数
	public final static String Day = "day";// 星期几
	public final static String No = "no";// 第几节课
	public final static String Address = "address";// 上课地点
	public static final String DATABASE_NAME = "schedule.db";// 数据文件名
	// 分数表2数据
	public final static String TABLE_NAME2 = "scores";
	public final static String Name2 = "name2";
	public final static String Xiu = "xiu";
	public final static String Xuefen = "xuefen";
	public final static String Score = "score";
	//公告信息表3
	public final static String TABLE_NAME3 = "notices";
	//公告信息表3
	public final static String TABLE_NAME4 = "tests";


	// 重写方法

	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	public boolean tableis(SQLiteDatabase db, String tableName) {
		Cursor cur = db.rawQuery(
				"select count(*) from sqlite_master where type ='table' and name = '"
						+ tableName + "'", null);
		if (cur.moveToNext()) {
			int count = cur.getInt(0);
			if (count > 0) {
				return true;
			}
		}
		cur.close();
		return false;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!this.tableis(db, TABLE_NAME)) {
			String str_sql = "create table " + TABLE_NAME + "(" + Day
					+ " integer," + No + " integer," + Name + " text,"
					+ ClassId + " text," + Weeks + " text," + Address+" text,"+ Point + " text,"+ Type + " text,"
					+ Teacher + " text);";
			db.execSQL(str_sql);
			// 首次创建时赋值
			for (int i = 1; i < 8; i++)
				for (int j = 1; j < 10; j += 2) {
					db.execSQL("insert into " + TABLE_NAME + " VALUES(" + i
							+ "," + j + ",'无','无','无','无','无','无','无');");

				}
			System.out.println("数据表classes初始化");
		}

		if (!this.tableis(db, TABLE_NAME2)) {
			String str_sql2 = "create table " + TABLE_NAME2 + "(" + Name2
					+ " text," + Xiu + " text," + Xuefen + " text," + Score
					+ " text);";
			db.execSQL(str_sql2);
			// db.execSQL("insert into "+TABLE_NAME2+" VALUES("+1+",null,null,null);");
			System.out.println("数据表scores初次初始化");
		}

		if (!this.tableis(db, TABLE_NAME3)) {
			String str_sql3 = "create table " + TABLE_NAME3 + "(url text,_id text,date text);";
			db.execSQL(str_sql3);
			System.out.println("数据表notices初次初始化");
		}
		if (!this.tableis(db, TABLE_NAME4)) {
			String str_sql4 = "create table " + TABLE_NAME4 + "(week text,_id text,position text,time text);";
			db.execSQL(str_sql4);
			System.out.println("数据表tests初次初始化");
		}

	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v("schedule", "onUpgrade");
	}

	// 以星期节次获取所有课程信息
	/** *infoArry[0]课程名 infoArry[1] 课程号 infoArry[2] 上课周次 infoArry[3]上课地点   infoArry[4]学分
	 * *infoArry[5]修读类型    infoArry[6]教师名字*/
	public String[] selectWithDayAndNo(SQLiteDatabase db, int day, int no) {

		String[] infoArry = new String[7];
		Cursor cur = db.rawQuery("select * from classes where day=" + day
				+ " AND no=" + no + ";", null);
		if (cur != null) {
			while (cur.moveToNext()) {
				infoArry[0] = cur.getString(2); // 课程名
				infoArry[1] = cur.getString(3); // 课程号
				infoArry[2] = cur.getString(4); // 上课周次
				infoArry[3] = cur.getString(5); // 上课地点
				infoArry[4] = cur.getString(6); // 学分
				infoArry[5] = cur.getString(7); // 修读类型
				infoArry[6] = cur.getString(8); // 教师名字
			}
		}
		System.out.println(infoArry[0] + infoArry[1] + infoArry[2]
				+ infoArry[3]);
		cur.close();
		return infoArry;
	}

	//获取整天的课程 
	/** *infoArry[0]课程名 infoArry[1] 课程号 infoArry[2] 上课周次 infoArry[3]上课地点 * */
	public String[] selectWithDay(SQLiteDatabase db, int day) {
		String[] infoArry = { "", "", "", "" };	
		//String judge;
		Cursor cur = db.rawQuery(
				"select * from classes where day=" + day + ";", null);
		if (cur != null) {
			while (cur.moveToNext()) {

			/*	//判断是否在上课周数内
				judge = cur.getString(4);
				System.out.println(judge);
				if(!judge.equalsIgnoreCase("无")){
					String[] spli =  judge.split("-|周");
					if(spli.length==3){
						int from=Integer.parseInt(spli[0]);
						int to=Integer.parseInt(spli[1]);
						if(week<from||week>to){
							continue;
						}
					}
				}*/
				// 注意，此处获取的信息为当天的所有内容
				infoArry[0] += cur.getString(2) + "&"; // 课程名
			//	infoArry[1] += cur.getString(3) + "&"; // 课程号
				infoArry[2] += cur.getString(4) + "&"; // 上课周次
				infoArry[3] += cur.getString(5) + "&"; // 上课地点
			}
		} else {
			System.out.println("读取错误");
		}
		cur.close();
		return infoArry;

	}

	//

}