package com.siwe.dutschedule.view;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;

public class ProgressDialog {

	private TextView mTextMessage;
	private PopupWindow mDialog;
	private View mDialogView;
	private BaseUi context;
	private String message;

	public void setMessage(String message) {
		this.message = message;
		mTextMessage.setText(message);
	}
	public ProgressDialog(BaseUi context) {
		this.context = context;
		this.init();
	}

	public ProgressDialog(BaseUi context, String message) {
		this.context = context;
		this.init();
		this.message = message;
		mTextMessage.setText(message);
	}

	private void init() {
		mDialogView = context.getLayout(R.layout.global_progress_dialog);
		mTextMessage = (TextView) mDialogView
				.findViewById(R.id.mm_progress_dialog_msg);
		mDialog = new PopupWindow(mDialogView,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
		mDialog.setAnimationStyle(-1);
		mDialog.setOutsideTouchable(false);
	}

	public void show() {
		if(!mDialog.isShowing())
			mDialog.showAtLocation(context.findViewById(R.id.TITLE),
				Gravity.CENTER, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	public void dismiss() {
		if(mDialog.isShowing())
			mDialog.dismiss();
	}
}
