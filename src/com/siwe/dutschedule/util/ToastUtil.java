package com.siwe.dutschedule.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siwe.dutschedule.R;


/**
 * 
 * @author linwei
 * @version1.0
 */
public abstract class ToastUtil {

	public static void doShowToast(final Context context, String message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View ToastView = inflater.inflate(R.layout.global_toast_view, null);
		Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
		toast.setView(ToastView);
		((TextView) ToastView.findViewById(R.id.toastMessage)).setText(message);
		toast.show();
	}

	public static void doShowSToast(final Context context, int message) {
		adoShowToast(context, R.drawable.icon_success,
				context.getString(message));
	}

	public static void doShowEToast(final Context context, int message) {
		adoShowToast(context, R.drawable.icon_error, context.getString(message));
	}

	public static void doShowSToast(final Context context, String message) {
		adoShowToast(context, R.drawable.abs__ic_cab_done_holo_dark, message);
	}

	public static void doShowEToast(final Context context, String message) {
		adoShowToast(context, R.drawable.abs__ic_clear_normal, message);
	}

	public static void adoShowToast(final Context ui, int icon, String message) {
		LayoutInflater inflater = LayoutInflater.from(ui);
		View ToastView = inflater.inflate(R.layout.global_toast_view, null);
		((ImageView) ToastView.findViewById(R.id.icon))
				.setBackgroundResource(icon);
		// ((TextView)ToastView.findViewById(R.id.ToastMessage)).setCompoundDrawablesWithIntrinsicBounds(icon,
		// 0,0 , 0);
		Toast toast = Toast.makeText(ui, null, Toast.LENGTH_SHORT);
		toast.setView(ToastView);
		((TextView) ToastView.findViewById(R.id.toastMessage)).setText(message);
		toast.show();
	}

	public static void doShowToast(final Context ui, int icon, int message) {
		adoShowToast(ui, icon, ui.getString(message));
	}

}
