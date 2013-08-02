package com.siwe.dutschedule.infoGeter;

import com.siwe.dutschedule.activity.MySQLiteOpenHelper;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;

public class SubjectGeter extends InfoGeter {

	public final static String TABLE_NAME = "classes";// 表1名
	public final static String ClassId = "classid";// 课程号
	public final static String Name = "name";// 课名
	public final static String Point = "point";// 学分
	public final static String Type = "type"; // 修读类型
	public final static String Teacher = "teacher"; // 教师名字
	public final static String Weeks = "weeks";// 上课周数
	public final static String Day = "day";// 星期几
	public final static String No = "no";// 第几节课
	public final static String Address = "address";// 上课地点
	private MySQLiteOpenHelper myOpenHelper;
	private SQLiteDatabase mysql;
	String param;

	public SubjectGeter(Context cont,String par) {
		super(cont);
		this.param = par;
		// TODO Auto-generated constructor stub
		FirstUrl = "http://202.118.65.21:8081/loginAction.do?" + param;
		SecondUrl = "http://202.118.65.21:8081/xkAction.do?actionType=6&oper";
	}

	@Override
	public String format(String get) {
		get = Regular.eregi_replace("(\\s)*", "", get);
		get = Regular.eregi_replace("(集中周|非集中周|必修|选修|任选)*", "", get);
		get = Regular.eregi_replace("(<htmllang=)(.)*(教室)", "", get);
		get = Regular.eregi_replace("(function)(.)*?(</html>)", "", get);
		get = Regular.eregi_replace("(<)(.)*?(>)", "/", get);
		get = Regular.eregi_replace("(&nbsp;)", "/", get);
		get = Regular.eregi_replace("(\\d{4})级((.)*?)(/)", "&&", get);
		get = Regular.eregi_replace("/{2,}", "/", get);
		return get;
	}

	/**
	 * 处理数据的总方法
	 */
	@Override
	public void dealStr(String get) {
		String[] result = get.split("/&&/");
		String[] result2;
		myOpenHelper = new MySQLiteOpenHelper(super.context);
		mysql = myOpenHelper.getReadableDatabase();

		// 需要初始化防止课程输入累加！！
		for (int i = 1; i < 8; i++)
			for (int j = 1; j < 10; j += 2) {
				mysql.execSQL("update " + TABLE_NAME + " set " + ClassId + "='"
						+ "无" + "', " + Name + "='" + "无" + "'," + Weeks + "='"
						+ "无" + "'," + Address + "='" + "无" + "'," + Teacher
						+ "='" + "无" + "'," + Type + "='" + "无" + "'," + Point
						+ "='" + "无" + "' where " + Day + "='" + i + "' and "
						+ No + "='" + j + "';");
			}
		System.out.println("数据表classes二次初始化");

		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
			result2 = result[i].split("/");
			for (int j = 0; j < result2.length; j++) {
				if (result2.length == 14) {// 一周一节
					judgeByNum(mysql, result2, 1);
				}

				if (result2.length == 21) { // 一周两节
					judgeByNum(mysql, result2, 2);
				}

				if (result2.length == 28) { // 一周三节
					judgeByNum(mysql, result2, 3);
				}

				if (result2.length == 35) { // 一周四节
					judgeByNum(mysql, result2, 4);
				}
			}
		}
		mysql.close();
		saveParam(); 
	}

	/**
	 * 以每周节次往数据库中插入信息
	 * @param db
	 * @param test
	 * @param bug
	 */
	public void judgeByNum(SQLiteDatabase db, String[] test, int bug) {
		for (int i = 0; i < bug; i++) {
			Integer no = Integer.parseInt(test[9 + i * 7], 10); // 将字符串转化为int
																	// 节次13579
			Integer numbers = Integer.parseInt(test[10 + i * 7], 10); // 节数 2 3
			// 4 6 8
			int flag = numbers / 2;

			// 四节以上连课的情况
			for (int j = 0; j < flag; j++) {
				int temp2 = no + j * 2; // 计算后的节次

				db.execSQL("update " + TABLE_NAME + " set " + ClassId + "='"
						+ test[0] + "', " + Name + "='" + test[1] + "',"
						+ Point + "='" + test[3] + "'," + Type + "='" + test[6]
						+ "'," + Teacher + "='" + test[4] + "'," + Weeks + "='"
						+ test[7 + i * 7] + "'," + Address + "='"
						+ test[12 + i * 7] + test[13 + i * 7] + "' where "
						+ Day + "='" + test[8 + i * 7] + "' and " + No + "='"
						+ temp2 + "';");
			}
		}

	}

	/**
	 * 保存用户名密码参数到sharedPreference
	 */
	private void saveParam(){
		Editor editor;
		//context.getSharedPreferences(FILENAME, context.MODE_PRIVATE);
		editor = context.getSharedPreferences("filename", Context.MODE_PRIVATE).edit();
		editor.putString("usernamepassword", param);
		editor.commit();
	}

}
