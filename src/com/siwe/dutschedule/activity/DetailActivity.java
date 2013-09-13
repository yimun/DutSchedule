package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.siwe.dutschedule.R;
import com.umeng.analytics.MobclickAgent;
//课程的详细信息
public class DetailActivity extends Activity {
	int no , dayOfWeek;
	private MySQLiteOpenHelper myHelper;
	private SQLiteDatabase mysql;
	Button bt_save;
	TextView title;
	EditText[] textview =new EditText[7];
	int[] tvId = {R.id.detail1,R.id.detail2,R.id.detail3,R.id.detail4,R.id.detail5,R.id.detail6,R.id.detail7};
	private GestureDetector detector;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail);
		title = (TextView)findViewById(R.id.maintitle);
		title.requestFocus();
		bt_save = (Button)findViewById(R.id.save);
		bt_save.setOnClickListener(new saveListener());
		getDetail();
		detector = new GestureDetector(this,new backGestureListener(this));
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
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
		String[] infoArry = myHelper.selectWithDayAndNo(mysql, dayOfWeek, no);
		for(int i=0;i<textview.length;i++){
			textview[i] = (EditText)findViewById(tvId[i]);
			textview[i].setText(infoArry[i]);		
		}
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
	
	class saveListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mysql = myHelper.getReadableDatabase();
			mysql.execSQL("update classes set classid='"
				 	+ textview[1].getText() + "', name='" + textview[0].getText()
					+ "',weeks='" + textview[2].getText() + "',address='" + textview[3].getText()
					+"',point='"+textview[4].getText()+"',type='"+textview[5].getText()
					+"',teacher='"+ textview[6].getText()
					+ "' where day='" + dayOfWeek + "' and no='"+ no + "';");
			mysql.close();
			Toast.makeText(DetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
		}
		
	}

}
