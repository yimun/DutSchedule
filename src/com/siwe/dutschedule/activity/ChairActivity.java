package com.siwe.dutschedule.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.SetBackgroundImage;
import com.umeng.analytics.MobclickAgent;

public class ChairActivity extends Activity {
//讲座信息	
	private final String DATABASE_PATH = "data/data/com.siwe.dutschedule/databases/chair";
	private final String DATABASE_FILENAME = "chair.db";
	SQLiteDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chair);
		
		RelativeLayout re = (RelativeLayout)findViewById(R.id.chairlayout);
		SetBackgroundImage.setTheme(ChairActivity.this, re,null);
		database = openDatabase();
		String sql = "select * from chair ;";
		Cursor cursor = database.rawQuery(sql, null);
		
		ListView lv = (ListView) findViewById(R.id.listview);
		lv.setCacheColorHint(0);       //设置缓存背景的alpha值为0  防止listview滑动时出现黑屏的现象
		
		if (cursor != null && cursor.getCount() >= 0) {

			@SuppressWarnings("deprecation")
			ListAdapter adapter = new SimpleCursorAdapter(this,
					R.layout.item_chair,
					cursor,
					new String[] { "_id", "time", "position", "teacher", "from" },
					new int[] { R.id.title, R.id.time, R.id.position,
					R.id.teacher, R.id.from });
			lv.setAdapter(adapter);
		}
		database.close();
		
	}

	
	private SQLiteDatabase openDatabase() {
		try {
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/dictionary目录中不存在
			// chair.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）
			if (!(new File(databaseFilename)).exists()) {
				// 获得封装dictionary.db文件的InputStream对象
				InputStream is = getResources().openRawResource(R.raw.chair);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的dictionary.db文件
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		} catch (Exception e) {
		}
		return null;
	}
	
	public void bt_back(View v){
		finish();
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
