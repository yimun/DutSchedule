package com.siwe.dutschedule.infoGeter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.siwe.dutschedule.activity.MySQLiteOpenHelper;

public class KaoGeter extends InfoGeter{
	
	private static MySQLiteOpenHelper myOpenHelper;
	private static SQLiteDatabase mysql;

	public KaoGeter(Context cont,String par) {
		super(cont);
		// TODO Auto-generated constructor stub
		FirstUrl = "http://202.118.65.21:8081/loginAction.do?" + par;
		SecondUrl = "http://202.118.65.21:8081/ksApCxAction.do?oper=getKsapXx";
	}
	
	@Override
	public String format(String result) {
		result = Regular.eregi_replace("(\\s)*", "", result);
		result = Regular.eregi_replace("(<html)(.)*(准考证号)", "", result);
		result = Regular.eregi_replace("(<)(.)*?(>)", "/", result);
		result = Regular.eregi_replace("(/&nbsp;/)", "##", result);
		result = Regular.eregi_replace("(&nbsp;)", "/", result);
		result = Regular.eregi_replace("/{2,}", "/", result);
		return result;
	}
	
	@Override
	public void dealStr(String str) {
		myOpenHelper = new MySQLiteOpenHelper(context);
		mysql = myOpenHelper.getReadableDatabase();
		mysql.execSQL("delete from tests");
		String[] re1 = str.split("/##");
		String[] re2;
		for (int i = 0; i < re1.length; i++) {
			re2 = re1[i].split("/");
			if (re2.length == 9)
				mysql.execSQL("insert into tests values('" + re2[1] + "','"
						+ re2[5] + "','" + re2[3] + re2[4] + "','" + "第"
						+ re2[6] + "周  星期" + re2[7] + " " + re2[8] + "')");
		}
		mysql.close();
	}
	

}
