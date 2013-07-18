package com.siwe.dutschedule.setting_edit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.siwe.dutschedule.R;

public class SetBackgroundImage {

	public static void changeBackgroundImage(int i) {
		
	}

	public static void setTheme(Activity activity,View back,View buttom) {
		String bg = getBG(activity);
		View title;
		title = (View)activity.findViewById(R.id.titlelayout);
		if (bg != null) {
			if ("grey".equals(bg)) {
				if(back!=null)
					back.setBackgroundResource(R.drawable.bpush_top_bg);
				if(title!=null)
					title.setBackgroundResource(R.drawable.bpush_top_bg);
				if(buttom!=null)
					buttom.setBackgroundResource(R.drawable.bpush_top_bg);
				return;
			}
			if ("blue".equals(bg)) {
				if(back!=null)
					back.setBackgroundResource(R.drawable.beijing22);
				if(title!=null)
					title.setBackgroundResource(R.drawable.bpush_top_bg);
				if(buttom!=null)
					buttom.setBackgroundResource(R.drawable.bpush_top_bg);
				return;
			}
			if ("green".equals(bg)) {
				if(back!=null)
					back.setBackgroundResource(R.drawable.beijing22);
				if(title!=null)
					title.setBackgroundResource(R.drawable.bpush_top_bg);
				if(buttom!=null)
					buttom.setBackgroundResource(R.drawable.bpush_top_bg);
				return;
			}
			else{
				saveBackground(activity,"grey");
				if(back!=null)
					back.setBackgroundResource(R.drawable.beijing22);
				if(title!=null)
					title.setBackgroundResource(R.drawable.bpush_top_bg);
				if(buttom!=null)
					buttom.setBackgroundResource(R.drawable.bpush_top_bg);
				return;
			}
		}
		else{
			saveBackground(activity,"grey");
			if(back!=null)
				back.setBackgroundResource(R.drawable.beijing22);
			if(title!=null)
				title.setBackgroundResource(R.drawable.bpush_top_bg);
			if(buttom!=null)
				buttom.setBackgroundResource(R.drawable.bpush_top_bg);
			return;
		}

	}

	public static void saveBackground(Activity activity, String imageTag) {
		SharedPreferences preferences = activity.getSharedPreferences("bg",Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("background", imageTag);
		editor.commit();

	}

	private static String getBG(Activity activity) {
		SharedPreferences preferences = activity.getSharedPreferences("bg",Activity.MODE_PRIVATE);
		String bg = preferences.getString("background", null);
		return bg;

	}
}
