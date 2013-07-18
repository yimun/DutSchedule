package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.SetBackgroundImage;
import com.umeng.analytics.MobclickAgent;
//课程的详细信息
public class DetailActivity extends Activity {
	int no , dayOfWeek;
	private MySQLiteOpenHelper myHelper;
	private SQLiteDatabase mysql;
	TextView t1,t2,t3,t4,t5,t6,t7,title;
	TextView[] textview ={t1,t2,t3,t4,t5,t6,t7};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail);
		SetBackgroundImage.setTheme(this, null, null);
		title = (TextView)findViewById(R.id.maintitle);
		t1 = (TextView)findViewById(R.id.detail1);
		t2 = (TextView)findViewById(R.id.detail2);
		t3 = (TextView)findViewById(R.id.detail3);
		t4 = (TextView)findViewById(R.id.detail4);
		t5 = (TextView)findViewById(R.id.detail5);
		t6 = (TextView)findViewById(R.id.detail6);
		t7 = (TextView)findViewById(R.id.detail7);
		getDetail();
		
	}

	public void getDetail(){
		myHelper = new MySQLiteOpenHelper(this);
		mysql = myHelper.getReadableDatabase();
		no = getIntent().getIntExtra("no", 1);
		dayOfWeek = getIntent().getIntExtra("day", 1);
		String titText = "";
		titText = (dayOfWeek==1?"周一":dayOfWeek==2?"周二":dayOfWeek==3?"周三":dayOfWeek==4?"周四":dayOfWeek==5?"周五":dayOfWeek==6?"周六":"周日")
			+"  "+(no==1?"第一大节":no==3?"第二大节":no==5?"第三大节":no==7?"第四大节":"第五大节");
		title.setText(titText);
	/*	String[] infoArry = myHelper.selectWithDayAndNo(mysql, dayOfWeek, no);
		for(int i=0;i<textview.length;i++){
			textview[i].setText(infoArry[i]);		
		}*/
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
	
	
	
	

	public void bt_back(View v){
		this.finish();	
	}

}
