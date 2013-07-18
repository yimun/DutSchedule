package com.siwe.dutschedule.widgetProvider;

/** 
 * @author Zhanglinwei
 * @version 2013/3/14
 * 窗口小部件
 */ 


import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.activity.MainActivity;

public class ScheduleAppWidgetProvider extends AppWidgetProvider {
	private static final String UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
	Link link = new Link();

	@SuppressLint("NewApi")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {


		System.out.println("WidgetonUpdate");
		//		link.doWork();
		String[] a = link.doSelect();
		link.doClose();
		//	System.out.println(a[0]+a[2]+a[3]);
		String[] name = a[0].split("&");
		//		String[] weeks = a[2].split("&");
		String[] address = a[3].split("&");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		dayOfWeek = dayOfWeek < 1 || dayOfWeek > 6 ? 7 : dayOfWeek;
		String week = dayOfWeek==1?"周一":dayOfWeek==2?"周二":dayOfWeek==3?"周三":dayOfWeek==4?"周四":dayOfWeek==5?"周五":dayOfWeek==6?"周六":"周日";




		for (int i = 0; i < appWidgetIds.length; i++) {
			Intent intent = new Intent();

			// 为Intent对象设置Action
			intent.setAction(UPDATE_ACTION);
			// 使用getBroadcast方法，得到一个PendingIntent对象，当该对象执行时，会发送一个广播
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.schedule_appwidget);  


			Intent intent3 = new Intent(context,MainActivity.class); 
			PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0); 
			remoteViews.setOnClickPendingIntent(R.id.widge, pendingIntent3); 
			//	appWidgetManager.updateAppWidget(appWidgetId, views); 
			remoteViews.setTextViewText(R.id.textView1,week);	
			remoteViews.setTextViewText(R.id.textView2, "   1~2:" + name[0]+"      "+address[0]);
			remoteViews.setTextViewText(R.id.textView3, "   3~4:" + name[1]+"      "+address[1]);
			remoteViews.setTextViewText(R.id.textView4, "   5~6:" + name[2]+"      "+address[2]);
			remoteViews.setTextViewText(R.id.textView5, "   7~8:" + name[3]+"      "+address[3]);
			remoteViews.setTextViewText(R.id.textView6, "   9~11:" + name[4]+"      "+address[4]);

			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Toast.makeText(context, "课程刷新成功", Toast.LENGTH_SHORT).show();
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
		System.out.println("action is "+action);
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
