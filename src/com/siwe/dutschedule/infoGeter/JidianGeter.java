package com.siwe.dutschedule.infoGeter;

/** 
 * @author linwei
 * @version 2013/3/14
 * 成绩信息的获取,并插入数据库
 */ 


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.siwe.dutschedule.activity.MySQLiteOpenHelper;

public class JidianGeter extends InfoGeter{
	

	private static MySQLiteOpenHelper myOpenHelper;
	private static SQLiteDatabase mysql;
	
	public JidianGeter(Context cont,String par) {
		super(cont);
		// TODO Auto-generated constructor stub
		FirstUrl = "http://202.118.65.21:8081/loginAction.do?"+par;
		SecondUrl = "http://202.118.65.21:8081/gradeLnAllAction.do?type=ln&oper=qbinfo";
	}

	@Override
	public void dealStr(String result){
		String[] result1 = result.split("/&&/"); 
		System.out.println("成绩条数" + result1.length);
		myOpenHelper = new MySQLiteOpenHelper(context);
		mysql = myOpenHelper.getReadableDatabase();
		
		mysql.execSQL("delete from scores");
		System.out.println("成绩表重置成功");
		for (int i = 0; i < result1.length; i++) {
			String[] temp = result1[i].split("/");
			if (temp.length > 5) {
				mysql.execSQL("insert into scores values('" + temp[2] + "','"
						+ temp[4] + "','" + temp[3] + "','" + temp[5] + "');");
				System.out.println("插入一条成绩");
			}

		}
		System.out.println("所有成绩导入成功！");
		mysql.close();
	}
	
	@Override
	public String format(String get) {
		get = Regular.eregi_replace("(\\s)*", "", get);     //正则算法
		get = Regular.eregi_replace("(<trclass)(.)*?(>)","&&", get);				
		get = Regular.eregi_replace("(<)(.)*?(>)","/", get);
		get = Regular.eregi_replace("&nbsp;","/", get);
		get = Regular.eregi_replace("20\\d{2}-(.)*?成绩","/", get);
		get = Regular.eregi_replace("最低修读学分：(.)*?通过课程门数:/{1,}\\d","/", get); 
		get = Regular.eregi_replace("[A-Z][a-z](.)*?(/)","/", get); 
		get = Regular.eregi_replace("/{2,}","/", get);
		return get;
	}

}
