package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.ClearAll;
import com.umeng.fb.UMFeedbackService;

public class Setting extends Activity {
	ListView lv;
	private ClearAll clearAll = new ClearAll();
	private MySQLiteOpenHelper myOpenHelper;
	private SQLiteDatabase mysql;
	public final String setting_item[]={"编辑课表", "重新登录获取","清除缓存","主题设置","提交反馈","关于我们","敬请关注","退出"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ArrayAdapter<String> madapter = new ArrayAdapter<String>(this,R.layout.setting_item,setting_item);
		lv=(ListView)findViewById(R.id.listView1);
		lv.setAdapter(madapter);
		lv.setOnItemClickListener(new ItemClick());
		}

	class ItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			switch(position){
				case 0: //编辑课表
					startActivity(new Intent(Setting.this, ClassEdit.class));
					break;
				case 1: //重新登录获取
					startActivity(new Intent(Setting.this, LoginActivity.class));
					break;
				case 2: //清除缓存

					new AlertDialog.Builder(Setting.this)
					.setCancelable(false)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("清除缓存")
					.setMessage("您确认要清除所有课表和成绩信息吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							myOpenHelper = new MySQLiteOpenHelper(Setting.this);
							mysql = myOpenHelper.getReadableDatabase();
							clearAll.clear(mysql);

							new AlertDialog.Builder(Setting.this)
							.setCancelable(false)
							.setIcon(R.drawable.ic_launcher)
							.setTitle("清除成功")
							.setMessage("缓存信息已删除！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							})
							.show();
							mysql.close();

						}

					})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.cancel();
						}
					}).show();

					break;
				case 3:  //背景设置
					startActivity(new Intent(Setting.this, ChangeBackgroundActivity.class));
					break;	
				case 4:  //提交反馈
					UMFeedbackService.openUmengFeedbackSDK(Setting.this);
					break;
				case 5:  //关于我们
					startActivity(new Intent(Setting.this, AboutActivity.class));
					break;
				case 6:  //敬请关注
		//			startActivity(new Intent(Setting.this, AdActivity.class));
					break;
				case 7:  //返回
					{
						Intent intent = new Intent(Intent.ACTION_MAIN);  
						intent.addCategory(Intent.CATEGORY_HOME);  
						startActivity(intent);  
						System.exit(0);  
					}
				default:break;

			}
			
		}
		
	}
}
