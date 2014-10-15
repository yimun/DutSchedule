package com.siwe.dutschedule.service;

/** 
 * @author Zhanglinwei
 * @version 2013/3/14
 * 窗口小部件
 */

import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.model.Schedule;
import com.siwe.dutschedule.sqlite.ScheduleSqlite;
import com.siwe.dutschedule.ui.UiSchedule;
import com.siwe.dutschedule.util.TimeUtil;

public class ScheduleAppWidgetProvider extends AppWidgetProvider {
	public static final String UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
	private int dayOfWeek;
	private ArrayList<Schedule> list = new ArrayList<Schedule>();
	private int tempWeek;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		//list = link.doSelect();
		dayOfWeek = TimeUtil.getDayOfWeek();
		tempWeek = TimeUtil.getWeekOfTerm(context);
		String week = dayOfWeek == 0 ? "周一" : dayOfWeek == 1 ? "周二"
				: dayOfWeek == 2 ? "周三" : dayOfWeek == 3 ? "周四"
						: dayOfWeek == 4 ? "周五" : dayOfWeek == 5 ? "周六" : "周日";
		ScheduleSqlite sqlite = new ScheduleSqlite(context);
		list.clear();
		list.addAll((ArrayList<Schedule>) sqlite.query(null, "weekday=?",
				new String[] { String.valueOf(dayOfWeek + 1) }));
		int[] tvIds = {R.id.textView2,R.id.textView3,R.id.textView4,R.id.textView5,R.id.textView6};
		String[] head = {"1~2:","3~4:","5~6:","7~8:","9~11:",};
		for (int i = 0; i < appWidgetIds.length; i++) {
			Intent intent = new Intent();
			System.out.println("begin fo appwidget");
			// 为Intent对象设置Action
			intent.setAction(UPDATE_ACTION);
			// 使用getBroadcast方法，得到一个PendingIntent对象，当该对象执行时，会发送一个广播
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.schedule_appwidget);

			
			Intent jump = new Intent(context, UiSchedule.class);
			PendingIntent pendingIntent3 = PendingIntent.getActivity(context,
					0, jump, 0);
			remoteViews.setOnClickPendingIntent(R.id.widge, pendingIntent3);
			// appWidgetManager.updateAppWidget(appWidgetId, views);
			remoteViews.setTextViewText(R.id.textView1, "第"+tempWeek+"周\n\n"+week);
			for(int index=0;index<tvIds.length;index++){
				remoteViews.setTextViewText(tvIds[index], head[index] + "无 | 无");
			}
			if(list.isEmpty()){
				// BUG下面这句为刷新界面的View如果没有，操作将不会保存
				appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);	
				continue;
			}
			for (Schedule item : list) {
				if(!TimeUtil.judgeIsTime(item,tempWeek))
					continue;
				int seque = Integer.valueOf(item.getSeque())/2;
				remoteViews.setTextViewText(tvIds[seque], head[seque] + item.getName()
						+ " | " + item.getPosition());
			}
			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Toast.makeText(context, "课表工具刷新成功", Toast.LENGTH_SHORT).show();
		System.out.println("onUpdatep----over");

	}


	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		System.out.println("onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		System.out.println("onDisabled");
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		System.out.println("onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (UPDATE_ACTION.equals(action)) {
			System.out.println("onReceive--action");
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.schedule_appwidget);
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName mComponentName = new ComponentName(context,
					ScheduleAppWidgetProvider.class);
			this.onUpdate(context, appWidgetManager,
					appWidgetManager.getAppWidgetIds(mComponentName));
			ComponentName componentName = new ComponentName(context,
					ScheduleAppWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		} else {
			System.out.println("onReceive--noaction");
			super.onReceive(context, intent);
		}
		super.onReceive(context, intent);
	}

}
