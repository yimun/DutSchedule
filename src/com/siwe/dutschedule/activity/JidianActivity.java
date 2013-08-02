package com.siwe.dutschedule.activity;

/** 
 * @author Yimu
 * @version 2013/3/11
 * 成绩显示界面
 */ 

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.infoGeter.JidianGeter;
import com.umeng.analytics.MobclickAgent;

public class JidianActivity extends Activity{


	private MySQLiteOpenHelper myOpenHelper;
	private SQLiteDatabase mysql;
	TextView tv1,tv2,tv3,tv4,tv6,tv8,tv9,tv10;
	Button refresh;
	ProgressBar progressbar;
	String param;
	GestureDetector detector;

	@Override
	public void onCreate(Bundle savedInstanceState){

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_jidian);		
		progressbar = (ProgressBar)findViewById(R.id.progressBar1);
		progressbar.setVisibility(View.GONE);
		refresh = (Button)findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				GetJidianTask task = new GetJidianTask();
				task.execute((Void) null);
			}
		});
		detector = new GestureDetector(this,new backGestureListener(this));
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent me)  
    {  
        return detector.onTouchEvent(me);  
    }  
	
	//分发手势，将listview等控件获取触摸动作的顺序延后，先执行onTouchEvent()
	@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

	@Override
	public void onResume(){
		System.out.println("成绩数据onResume刷新");
		super.onResume();
		myOpenHelper = new MySQLiteOpenHelper(this);
		tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setHorizontallyScrolling(true);
		tv2 = (TextView)findViewById(R.id.textView2);
		tv3 = (TextView)findViewById(R.id.textView3);
		tv4 = (TextView)findViewById(R.id.textView4);
		tv6 = (TextView)findViewById(R.id.textView6);
		tv8 = (TextView)findViewById(R.id.textView8);
		tv9 = (TextView)findViewById(R.id.textView10);
		tv10 = (TextView)findViewById(R.id.textView12);
		refreshData();
		MobclickAgent.onResume(this);
	}

	public void refreshData(){

		Float[] result = this.calcu();
		String jidian = result[0].toString();
		String average = result[1].toString();
		String jidianBixiu = result[2].toString();
		String averageBixiu = result[3].toString();

		mysql = myOpenHelper.getReadableDatabase();
		Cursor cur = mysql.rawQuery("select * from scores;", null);
		String name = "课程名：\n",xiu = "类型：\n",xuefen = "学分：\n",score = "分数：\n";
		if (cur != null) {
			while (cur.moveToNext()) {
				name += cur.getString(0).toString()+"\n";
				xiu += cur.getString(1).toString()+"\n";
				xuefen += cur.getString(2).toString()+"\n";
				score += cur.getString(3).toString()+"\n";							
			}
		}
		cur.close();
		mysql.close();
		tv1.setText(name);
		tv2.setText(xiu);
		tv3.setText(xuefen);
		tv4.setText(score);
		tv6.setText(jidian);
		tv8.setText(average);
		tv9.setText(jidianBixiu);
		tv10.setText(averageBixiu);	
		System.gc();
	}



	public Float[] calcu(){


		mysql = myOpenHelper.getReadableDatabase();
		//		String[] info = new String[]{"xuefen","score"};
		float xuefen ; float score ;
		float jidianAll = 0;
		float xuefenAll = 0;
		float scoreAll = 0;

		Cursor cur = mysql.rawQuery("select * from scores;", null);
		if (cur != null) {
			while (cur.moveToNext()) {
				xuefen = cur.getFloat(2);
				score = cur.getFloat(3);			
				jidianAll += 0.1*(score-50)*xuefen;
				xuefenAll += xuefen;
				scoreAll += score*xuefen;
			}
		}
		cur.close();

		//必修部分的绩点和平均分计算
		float scoreBixiuAll = 0;
		float jidianBixiuAll = 0;
		float xuefenBixiuAll = 0;
		Cursor cur2 = mysql.rawQuery("select * from scores where xiu like '%必修%';", null);
		if (cur2 != null) {
			while (cur2.moveToNext()) {
				scoreBixiuAll += cur2.getFloat(3)*cur2.getFloat(2);
				jidianBixiuAll+=0.1*(cur2.getFloat(3)-50)*cur2.getFloat(2);
				xuefenBixiuAll+=cur2.getFloat(2);
			}
		}
		cur2.close();
		mysql.close();

		Float result[] = {jidianAll/xuefenAll,scoreAll/xuefenAll,jidianBixiuAll/xuefenBixiuAll,scoreBixiuAll/xuefenBixiuAll};
		return result;

	}

	@Override
	public void onPause(){
		System.out.println("调用onPause方法");
		super.onPause();
		if(mysql.isOpen()){
			mysql.close();
		}
		MobclickAgent.onPause(this);
	}

	/**
	 * 
	 * @author linwei
	 * 后台刷新成绩任务
	 *
	 */
	public class GetJidianTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute(){
			SharedPreferences sharedPrefrences =getSharedPreferences("user", MODE_PRIVATE);
			param = sharedPrefrences.getString("usernamepassword", "zjh=201281084&mm=755213");
			System.out.println("param="+param);	
			progressbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Looper.prepare();
			return new JidianGeter(JidianActivity.this,param).getTwoPageInfo();
			
		}

		@Override
		public void onCancelled(){
			super.onCancelled();
		}

		public void onPostExecute(Boolean success){
			
			progressbar.setVisibility(View.GONE);
			if(success){
				Toast.makeText(JidianActivity.this, "刷新成功",Toast.LENGTH_SHORT).show();
				refreshData();
			}
			else{
				Toast.makeText(JidianActivity.this, "刷新失败",Toast.LENGTH_SHORT).show();
			}
			
		}

	}
	
	public void bt_back(View v){
		this.finish();
	}

}