package com.siwe.dutschedule.activity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.infoGeter.Regular;
import com.umeng.analytics.MobclickAgent;

public class NoticeActivity extends Activity {

	private MySQLiteOpenHelper myOpenHelper;
	private SQLiteDatabase mysql;
	String url1 = "http://teach.dlut.edu.cn/o2.asp";
	String url2 = "http://tuanwei.dlut.edu.cn/list.php?classid=6";
	String url3 = "http://chuangxin.dlut.edu.cn/SecondPage_News.aspx?Type=6";
	private ListView list;
	private Button refresh;
	public static final int REQUEST_CODE = 1;
	private ProgressDialog progressDialog = null;
	String dizhi = "", biaoti = "";
	private Spinner spinner;
	String[] han = { "所有", "竞赛", "活动讲座" };
	public boolean isContinue;
	GetNoticeTask getTask;
	GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notice);
		detector = new GestureDetector(this, new backGestureListener(this));
		list = (ListView) findViewById(R.id.lv01);
		list.setCacheColorHint(0);
		refresh = (Button) findViewById(R.id.refresh);
		spinner = (Spinner) findViewById(R.id.spinner1);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getTask = new GetNoticeTask();
				getTask.execute((Void) null);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.notice_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent inte = new Intent(NoticeActivity.this,
				WebviewActivity.class);
		switch (item.getItemId()) {
		case R.id.item1:
			inte.putExtra("url", "http://www.dlut.edu.cn/");
			inte.putExtra("title", "大连理工大学");
			startActivity(inte);
			break;
		case R.id.item2:
			inte.putExtra("url", "http://portal.dlut.edu.cn/cas/login?service=http%3A%2F%2Fportal.dlut.edu.cn%2Fcas.jsp");
			inte.putExtra("title", "校园门户");
			startActivity(inte);
			break;
		case R.id.item3:
			inte.putExtra("url", "http://tuanwei.dlut.edu.cn/");
			inte.putExtra("title", "校团委");
			startActivity(inte);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return detector.onTouchEvent(me);
	}

	// 分发手势，将listview等控件获取触摸动作的顺序延后，先执行onTouchEvent()
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		this.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}

	public void onResume() {
		System.out.println("notice onResume");
		MobclickAgent.onResume(this);
		super.onResume();
		BaseAdapter ba = new BaseAdapter() {
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return han.length;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				RelativeLayout ll = (RelativeLayout) LayoutInflater.from(
						NoticeActivity.this)
						.inflate(R.layout.spinneritem, null);
				;
				TextView tv = (TextView) ll.findViewById(R.id.TextViewz);
				tv.setText(han[position]);
				return ll;
			}
		};
		spinner.setAdapter(ba);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// SharedPreferences sharedPrefrences;
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					setListbyKey("");
					break;
				case 1:
					setListbyKey("赛");
					break;
				case 2:
					setListbyKey("讲座");
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		System.gc();

	}

	String url[];
	String title[];

	/** 关键字匹配筛选 */
	private void setListbyKey(String key) {

		myOpenHelper = new MySQLiteOpenHelper(NoticeActivity.this);
		mysql = myOpenHelper.getReadableDatabase();
		String sql1 = "select * from notices where _id like '%" + key + "%';";
		Cursor cursor = mysql.rawQuery(sql1, null);
		System.out.println(cursor.getCount());
		biaoti = "";
		dizhi = ""; // 字符串置零防止累加
		if (cursor.getCount() == 0) {
			list.setBackgroundResource(R.drawable.noinfo);
			return;
		}
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				biaoti += cursor.getString(cursor.getColumnIndex("_id")) + "&";
				dizhi += cursor.getString(cursor.getColumnIndex("url")) + "&";
				// System.out.println(dizhi);
				// System.out.println(biaoti);
			}
		}
		if (cursor != null && cursor.getCount() >= 0) {
			System.out.println("获取成功");

			@SuppressWarnings("deprecation")
			ListAdapter adapter = new SimpleCursorAdapter(NoticeActivity.this,
					R.layout.notice_item, cursor,
					new String[] { "_id", "date" }, new int[] { R.id.tv2,
							R.id.tv1 });
			list.setAdapter(adapter);

		}

		// 设置选项选中的监听器
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {// 重写选项被选中事件的处理方法
				// info[arg0][0];//这是网址。。。传进你们做的哪里就哦了
				url = dizhi.split("&");
				title = biaoti.split("&");
				Intent inte = new Intent(NoticeActivity.this,
						WebviewActivity.class);
				inte.putExtra("url", url[position]);
				inte.putExtra("title", title[position]);
				startActivity(inte);
			}
		});
		// cursor.close();
		mysql.close();
	}

	private String[][] forma() {
		String html1 = getinfo(url1);
		String html2 = getinfo(url2);
		String html3 = getinfo(url3);
		if (html1 == null || html2 == null || html3 == null) {
			return null;
		}
		String infor1 = fliter1(html1);
		String infor2 = fliter2(html2);
		String infor3 = fliter3(html3);

		String[][] suzu1 = format(infor1);
		String[][] suzu2 = format(infor2);
		String[][] suzu3 = format(infor3);
		String[][] info = new String[suzu1.length + suzu2.length + suzu3.length
				- 3][3];
		// 将两个数组整合成一个数组
		for (int i = 0; i < suzu1.length + suzu2.length + suzu3.length - 3; i++) {
			if (i < suzu1.length - 1) {
				info[i][0] = suzu1[i + 1][0];
				// et.setText(info[i][0]);
				// System.out.println(info[i][0]);
				info[i][1] = suzu1[i + 1][1];
				info[i][2] = suzu1[i + 1][4];
				// et.setText(info[i][2]);
			} else if (i < suzu1.length + suzu2.length - 2) {
				info[i][0] = suzu2[i - suzu1.length + 2][0];
				info[i][1] = suzu2[i - suzu1.length + 2][1];
				info[i][2] = suzu2[i - suzu1.length + 2][3];
				// et.setText(info[i][0]);
			} else {
				info[i][0] = suzu3[i - (suzu1.length + suzu2.length) + 3][0];
				info[i][1] = suzu3[i - (suzu1.length + suzu2.length) + 3][1];
				info[i][2] = " ";
				// et.setText(info[i][0]);
			}
		}
		return info;
	}

	// 联网方法
	public String getinfo(String url) {
		HttpGet httpGetRequest = new HttpGet(url);
		String result = new String();
		// 创建HttpPost对象
		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpGetRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) { // 连接成功
				result = EntityUtils.toString(httpResponse.getEntity(),
						"gb2312"); // 获得资源.
			}
		} catch (Exception e) { // 捕获并打印异常
			e.printStackTrace();
			return null;
		}
		return result;

	}

	// 针对创院的正则替换
	private static String fliter3(String result) {
		String text = result;
		// System.out.println(text);
		// System.out.println("替换前：");
		text = Regular.eregi_replace("(\\s)*", "", text);
		text = Regular.eregi_replace("(<!DOCT)(.)*?(末页)", "", text);
		text = Regular.eregi_replace("(</a>)(.)*?(href=\")",
				"##http://chuangxin.dlut.edu.cn/", text);
		text = Regular.eregi_replace("(\"tar)(.)*?(\">)", "%", text);
		text = Regular
				.eregi_replace(
						"(##http://chuangxin.dlut.edu.cn/java)(.)*?(</html>)",
						"", text);
		// System.out.print(text);
		return text;
	}

	// 针对团委的正则替换
	private static String fliter2(String result) {
		String text = result;
		// System.out.println(text);
		// System.out.println("替换前：");
		text = Regular.eregi_replace("(\\s)*", "", text);
		text = Regular.eregi_replace("(<!DOCT)(.)*?(团内通知)", "", text);
		text = Regular.eregi_replace("(</font>)(.)*?(\">)", "%", text);
		text = Regular.eregi_replace("(<ahref=\")", "##", text);
		text = Regular.eregi_replace("(\"targ)(.)*?(\">)", "%", text);
		text = Regular.eregi_replace("(</a>)(.)*?(Font\">)", "%", text);
		text = Regular.eregi_replace("(<font)(.)*?(Font\">)", "", text);
		text = Regular.eregi_replace("(</td>)(.)*?(##)", "##", text);
		text = Regular.eregi_replace("(%<tr>)(.)*?(\">)", "", text);
		text = Regular.eregi_replace("(##/)(.)*?(</html>)", "", text);
		text = Regular.eregi_replace("(##)", "##http://tuanwei.dlut.edu.cn/",
				text);
		// System.out.print(text);
		return text;
	}

	// 数组整理
	private static String[][] format(String result) {
		String[] temp = result.split("##");
		String[][] inform = new String[temp.length][6];
		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < 4; y++) {
				inform[x][y] = "";
			}
		}
		for (int m = 0; m < temp.length; m++) {
			String[] temp2 = temp[m].split("%");
			// System.out.println("");
			for (int n = 0; n < temp2.length; n++) {
				inform[m][n] = temp2[n];
			}
		}
		return inform;

	}

	// 针对教务处通知的正则替换
	private static String fliter1(String result) {
		String text = result;
		text = Regular.eregi_replace("(\\s)*", "", text);
		text = Regular.eregi_replace("(<html>)(.)*?(发布日期)", "", text);
		text = Regular.eregi_replace("(</font)(.)*?(\"left\">)", "", text);
		text = Regular.eregi_replace("(\"targ)(.)*?(\">)", "%", text);
		text = Regular.eregi_replace("(</a>)(.)*?(left\">)", "%", text);
		text = Regular.eregi_replace("(</div>)(.)*?(center\">)", "%", text);
		text = Regular.eregi_replace("(</td><tdalign=\"center\">)", "%", text);
		text = Regular.eregi_replace("(</td>)(.)*?(left\">)", "%", text);
		text = Regular.eregi_replace("(</td>)(.)*?(</html>)", "", text);
		text = Regular.eregi_replace("(<ahref=\")", "##", text);
		text = Regular.eregi_replace("(##../)", "##http://teach.dlut.edu.cn/",
				text);
		// System.out.print(text);
		return text;
	}

	public class GetNoticeTask extends AsyncTask<Void, Void, String[][]> {
		@Override
		protected void onPreExecute() {
			isContinue = true;
			myOpenHelper = new MySQLiteOpenHelper(NoticeActivity.this);
			mysql = myOpenHelper.getReadableDatabase();
			progressDialog = ProgressDialog.show(NoticeActivity.this, "请稍等...",
					"信息获取中...", true);
			progressDialog.setCancelable(true);
			// 人为返回取消后
			progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					System.out.println("progressDialog onCancel");
					isContinue = false;
				}
			});

		}

		@Override
		protected String[][] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Looper.prepare();
			final String[][] info = forma(); // 联网获取
			return info;
		}

		@Override
		public void onCancelled() {
			System.out.println("task onCancelled");
			super.onCancelled();
		}

		@Override
		public void onPostExecute(String[][] info) {

			System.out.println("执行onPostExecute");
			if (info == null) {
				Toast.makeText(NoticeActivity.this, "获取失败", Toast.LENGTH_SHORT)
						.show();
			} else if (isContinue) {

				mysql.delete("notices", null, null);
				for (int i = 0; i < info.length; i++) {
					mysql.execSQL("insert into notices values('" + info[i][0]
							+ "','" + info[i][1] + "','" + info[i][2] + "');");
					System.out.println("插入" + i);
				}
				Toast.makeText(NoticeActivity.this, "获取成功", Toast.LENGTH_SHORT)
						.show();
				setListbyKey("");
			}
			// SetBackgroundImage.setTheme(NoticeActivity.this, list,null);
			progressDialog.dismiss();
			mysql.close();

		}

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
