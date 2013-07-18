package com.siwe.dutschedule.activity;

/** 
 * @author Zhanglinwei
 @author xukai
 * @version 2013/3/10
 * 课程主界面
 */
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.ClearAll;
import com.siwe.dutschedule.setting_edit.SetBackgroundImage;
import com.umeng.analytics.MobclickAgent;

public class ClassActivity extends Activity {

	private SQLiteDatabase mysql;
	private ImageView cursor;
	private MySQLiteOpenHelper myOpenHelper;
	private int offset = 0; // 偏移量
	private int currIndex = 1; // 当前游标位置
	int dayOfWeek, tmpday = 4;
	private int bmpW;
	GestureDetector detector;
	myGestureListener gesTouch = new myGestureListener();
	RelativeLayout cla1, cla2, cla3, cla4, cla5;
	private Button top_t1, top_t2, top_t3, top_t4, top_t5, top_t6, top_t7,
			temp;
	private TextView tv11, tv12, tv21, tv22, tv31, tv32, tv41, tv42, tv51,
			tv52, calctext;
	// tv1,tv2,tv3,tv4,tv6,tv8,tv9,tv10,
	// ###抽屉效果
	// 抽屉效果
	// ##
	private MyViewGroup myViewGroup;
	private LayoutInflater layoutInflater;
	private View menu_view, content_view;
	private int window_windth;// 按钮宽度
	private ListView lv_menu;
	private boolean hasMeasured = false;
	private static int distance;// 滑动距离

	private ClearAll clearAll = new ClearAll();
	static int week;

	private String title[] = { "编辑课表", "清除缓存", "返回" };
	private ViewTreeObserver viewTreeObserver;

	// ###################################

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("调用onCreate方法");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 创建手势检测器
		setContentView(R.layout.groupmain);
		menuInitView();
		ScrollView jin = (ScrollView) content_view
				.findViewById(R.id.scrollView1);
		jin.setOnTouchListener(this.gesTouch);
		detector = new GestureDetector(this,this.gesTouch);
		this.InitTextView();
		InitImageView();

	}

	/***
	 * 小滑块的的初始化
	 * 
	 * @param 参数
	 * @return 返回值
	 */

	private void InitImageView() {
		cursor = (ImageView) content_view.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; // 获取分辨率宽度
		offset = (screenW / 7 - bmpW) / 2; // 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);

	}

	/***
	 * 顶部文本框以及周数的初始化监听，更新
	 * 
	 * @param
	 * @return
	 */
	private void InitTextView() {

		int Ids[] = new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4,
				R.id.text5, R.id.text6, R.id.text7 };
		// 简化后
		for (int i = 0; i < 7; i++) {
			temp = (i == 0 ? top_t1 : i == 1 ? top_t2 : i == 2 ? top_t3
					: i == 3 ? top_t4 : i == 4 ? top_t5 : i == 5 ? top_t6
							: top_t7);
			temp = (Button) content_view.findViewById(Ids[i]);
			temp.setOnClickListener(new MyOnClickListener(i));
		}

		// 菜单键监听
		Button bt = (Button) content_view.findViewById(R.id.btmenu);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (MyViewGroup.isMenuOpned)
					myViewGroup.closeMenu();
				else
					myViewGroup.showMenu(); // 开启菜单
			}
		});

		calctext = (TextView) findViewById(R.id.calctext);
		week = getweek();
		if (week == 0) {
			calctext.setText("假期");
		} else {
			System.out.println("当前第" + week + "周");
			calctext.setText("第 " + week + " 周");
		}

	}

	// 按钮监听器
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// mPager.setCurrentItem(index);
			System.out.println("onClick");
			insertView(currIndex, index + 1);
			currIndex = index + 1;
		}
	}

	/***
	 * 更新界面上的课程数据
	 * 
	 * @param int current,int gotoDay
	 * @return
	 */
	public void insertView(int current, int gotoDay) {
		tv11 = (TextView) content_view.findViewById(R.id.tv11);
		tv12 = (TextView) content_view.findViewById(R.id.tv12);
		tv21 = (TextView) content_view.findViewById(R.id.tv21);
		tv22 = (TextView) content_view.findViewById(R.id.tv22);
		tv31 = (TextView) content_view.findViewById(R.id.tv31);
		tv32 = (TextView) content_view.findViewById(R.id.tv32);
		tv41 = (TextView) content_view.findViewById(R.id.tv41);
		tv42 = (TextView) content_view.findViewById(R.id.tv42);
		tv51 = (TextView) content_view.findViewById(R.id.tv51);
		tv52 = (TextView) content_view.findViewById(R.id.tv52);

		System.out.println("调用insertView方法");
		myOpenHelper = new MySQLiteOpenHelper(this);
		mysql = myOpenHelper.getReadableDatabase();

		String[] infoArry = myOpenHelper.selectWithDay(mysql, gotoDay);
		String[] classname = infoArry[0].split("&");
		String[] address = infoArry[3].split("&");
		String[] weeks = infoArry[2].split("&");

		tv11.setText(classname[0]);
		tv21.setText(classname[1]);
		tv31.setText(classname[2]);
		tv41.setText(classname[3]);
		tv51.setText(classname[4]);

		tv12.setText("地点:" + address[0] + "  上课周:" + weeks[0]);
		tv22.setText("地点:" + address[1] + "  上课周:" + weeks[1]);
		tv32.setText("地点:" + address[2] + "  上课周:" + weeks[2]);
		tv42.setText("地点:" + address[3] + "  上课周:" + weeks[3]);
		tv52.setText("地点:" + address[4] + "  上课周:" + weeks[4]);

		int one = offset * 2 + bmpW;
		Animation animation = null;
		animation = new TranslateAnimation(one * (current - 1), one
				* (gotoDay - 1), 0, 0);
		// current = tmpdayOfWeek;
		animation.setFillAfter(true);
		animation.setDuration(300);
		cursor.startAnimation(animation);
		System.out.println("insertView:" + current + "——>" + gotoDay);
		// return current;

		mysql.close();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println("调用onStop方法");
		super.onStop();
	}

	@Override
	protected void onResume() {

		System.out.println("调用ClassActivity的onResume方法");
		super.onResume();
		// SetBackgroundImage.setTheme(this, null,null);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		dayOfWeek = dayOfWeek < 1 || dayOfWeek > 6 ? 7 : dayOfWeek;
		System.out.println("dayOfWeekInClassActivity=" + dayOfWeek);
		// currIndex = dayOfWeek;
		this.insertView(currIndex, dayOfWeek);
		currIndex = dayOfWeek;
		MyViewGroup.isMenuOpned = false; // BUG防止菜单打开后未关闭，跳转界面回来后导致界面移动方向相反
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		System.out.println("调用onPause方法");
		super.onPause();
		System.gc();
		if (mysql.isOpen()) {
			mysql.close();
		}
		// 处理Android SQLite - close() was never explicitly called on database异常
		if (myOpenHelper != null) {
			myOpenHelper.close();
		}
		MobclickAgent.onPause(this);
	}

	/***
	 * 页面的滑动手势监听
	 * 
	 * @param
	 */
	public class myGestureListener implements OnGestureListener,
			OnTouchListener {

		// 定义手势动作两点之间的最小距离
		final int FLIP_DISTANCE = 100;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			/*
			 * 如果第一个触点事件的X座标大于第二个触点事件的X座标超过FLIP_DISTANCE 也就是手势从右向左滑。
			 */
			if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
				System.out.println("1");
				insertView(currIndex, (currIndex += 1) == 8 ? 1 : currIndex);
				currIndex = ((currIndex) == 8 ? 1 : currIndex);
				System.out.println("currIndex=" + currIndex);
				return true;
			}

			/* * 如果第二个触点事件的X座标大于第一个触点事件的X座标超过FLIP_DISTANCE 也就是手势从右向左滑。 */

			else if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
				System.out.println("2");

				insertView(currIndex, (currIndex -= 1) == 0 ? 7 : currIndex);
				currIndex = (currIndex == 0 ? 7 : currIndex);
				System.out.println("currIndex=" + currIndex);
				return true;
			} else
				return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub

			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return detector.onTouchEvent(event);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return detector.onTouchEvent(ev);
	}

	/***
	 * 初始化左边的菜单视图，以及绑定子项监听
	 * 
	 * @param
	 * 
	 */
	void menuInitView() {
		// System.out.println("调用menuInitView方法");
		myViewGroup = (MyViewGroup) this.findViewById(R.id.vg_main);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu_view = layoutInflater.inflate(R.layout.menu, null);
		content_view = layoutInflater.inflate(R.layout.activity_main, null);
		myViewGroup.addView(menu_view);
		myViewGroup.addView(content_view);
		lv_menu = (ListView) menu_view.findViewById(R.id.lv_menu);
		lv_menu.setCacheColorHint(0);
		lv_menu.setAdapter(new ArrayAdapter<String>(this, R.layout.menuitem,
				R.id.tv_item, title));
		setListViewHeightBaseOnChildren(lv_menu);
		getMAX_WIDTH();

		content_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myViewGroup.getScrollX() <= -distance) {
					myViewGroup.closeMenu();
				}
			}
		});

		// 菜单被点击时触发的方法

		lv_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 点击后关闭菜单的动画特效

				/*
				 * myViewGroup.setCloseAnimation(new CloseAnimation() {
				 * 
				 * @Override public void closeMenuAnimation() { if
				 * (myViewGroup.getScrollX() == -window_windth)
				 * myViewGroup.closeMenu_2(); } }); myViewGroup.closeMenu_1();
				 */

				myViewGroup.closeMenu();
				switch (position) {
				case 0: // 编辑课表
					startActivity(new Intent(ClassActivity.this,
							ClassEdit.class));
					break;
				case 1: // 清除缓存

					new AlertDialog.Builder(ClassActivity.this)
							.setCancelable(false)
							.setIcon(R.drawable.ic_launcher)
							.setTitle("清除缓存")
							.setMessage("您确认要清除所有课表和成绩信息吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											myOpenHelper = new MySQLiteOpenHelper(
													ClassActivity.this);
											mysql = myOpenHelper
													.getReadableDatabase();
											clearAll.clear(mysql);

											new AlertDialog.Builder(
													ClassActivity.this)
													.setCancelable(false)
													.setIcon(
															R.drawable.ic_launcher)
													.setTitle("清除成功")
													.setMessage("缓存信息已删除！")
													.setPositiveButton(
															"确定",
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	dialog.cancel();

																}
															}).show();
											mysql.close();

										}

									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									}).show();

					break;
				case 2: // 返回
					ClassActivity.this.finish();
					break;
				default:
					break;
				}
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (event.getRepeatCount() == 0 && MyViewGroup.isMenuOpned) {
			if (MyViewGroup.isMenuOpned) {
				myViewGroup.closeMenu();
				return true;
			} else {
				ClassActivity.this.finish();
				// return false;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (MyViewGroup.isMenuOpned)
				myViewGroup.closeMenu();
			else
				myViewGroup.showMenu();
		}
		return true;
	}

	/***
	 * 计算iv_button的宽度
	 * 
	 * @param
	 */
	public void getMAX_WIDTH() {
		System.out.println("调用getMAX_WIDTH()方法");
		viewTreeObserver = menu_view.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_windth = getWindowManager().getDefaultDisplay()
							.getWidth();
					distance = window_windth / 2;
					System.out.println(distance);
					myViewGroup.setDistance(distance);
					// 在这里我们要设置lv_menu的宽度，（因为viewpager和listview
					// 会发送冲突，效果和scrollview一样，怀疑他们私底下有一腿.）
					ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) lv_menu
							.getLayoutParams();
					layoutParams.width = distance;
					lv_menu.setLayoutParams(layoutParams);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	/*
	 * public void onClick(View v) { if (v == iv_button) { if
	 * (myViewGroup.isMenuOpned) myViewGroup.closeMenu(); else
	 * myViewGroup.showMenu(); }
	 * 
	 * }
	 */

	/***
	 * 动态设置listview的高度 注：在listview和scrollview冲突的时候我们可以用下面这个方法动态设置listview的高度，
	 * 但是在这里不行，原因不明确，但是还有一个办法就是把listview设置和屏幕一样的高度，
	 * 这样不管是viewgroup和scrillview肯定都管用.
	 * 
	 * @param listView
	 */
	@SuppressWarnings("deprecation")
	public void setListViewHeightBaseOnChildren(ListView listView) {
		ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
		layoutParams.height = getWindowManager().getDefaultDisplay()
				.getHeight();
		listView.setLayoutParams(layoutParams);
	}

	public class detailListener implements View.OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			int temp = v.getId();
			int no = temp == R.id.cla1 ? 1 : v.getId() == R.id.cla2 ? 3 : v
					.getId() == R.id.cla3 ? 5 : v.getId() == R.id.cla4 ? 7 : 9;
			Intent in = new Intent(ClassActivity.this, DetailActivity.class);
			in.putExtra("no", no);
			in.putExtra("day", currIndex);
			startActivity(in);
			return true;
		}

	}

	/***
	 * 计算当前的周数
	 * 
	 * @param
	 * @return int day
	 */
	public static int getweek() {

		// 获取当前天数
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		int currentDayOfYear = c.get(Calendar.DAY_OF_YEAR);
		System.out.println("xianzai" + currentDayOfYear);

		int distanceday = 0;
		if (currentDayOfYear > 56 && currentDayOfYear < 244) {
			distanceday = currentDayOfYear - 56;
		} else if (currentDayOfYear > 244) {
			distanceday = currentDayOfYear - 244;
		} else {
			return 0;
		}
		int week = distanceday / 7 + 1;
		return week;
	}

}
