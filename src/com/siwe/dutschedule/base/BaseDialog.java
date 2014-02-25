package com.siwe.dutschedule.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.siwe.dutschedule.R;

public class BaseDialog {

	private TextView mTextMessage;
	private PopupWindow mDialog;
	private View mDialogView;
	private TextView mTextTitle;
	private Button mClose;
	private Button mConfirm;
	private OnClickListener mOnClickListener = null;
	BaseUi context;

	public void setOnConfirmListener(OnClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public BaseDialog(BaseUi context, Bundle params) {
		this.context = context;
		init();
		mTextTitle.setText(params.getString("title"));
		mTextMessage.setText(params.getString("message"));
		if (params.getBoolean("showCancel")) {
			mClose.setVisibility(View.VISIBLE);
			mClose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mDialog.dismiss();
				}
			});
		}
	}

	public BaseDialog(BaseUi context, String message) {
		this.context = context;
		this.init();
		mTextMessage.setText(message);
	}

	private void init() {
		mDialogView = context.getLayout(R.layout.global_confirm_dialog);
		mTextTitle = (TextView) mDialogView
				.findViewById(R.id.confirm_dialog_title_tv);
		mTextMessage = (TextView) mDialogView
				.findViewById(R.id.confirm_dialog_content_text);
		mClose = (Button) mDialogView.findViewById(R.id.confirm_dialog_btn1);
		mConfirm = (Button) mDialogView.findViewById(R.id.confirm_dialog_btn2);
		mDialog = new PopupWindow(mDialogView,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
		mDialog.setAnimationStyle(-1);
		mDialog.setOutsideTouchable(false);
	}

	public void show() {
		if (mOnClickListener != null)
			mConfirm.setOnClickListener(mOnClickListener);
		else{
			mConfirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mDialog.dismiss();
				}
			});
		}
		mDialog.showAtLocation(context.findViewById(R.id.TITLE),
				Gravity.CENTER, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}
	
	public void dismiss() {
		if(mDialog != null)
			mDialog.dismiss();
	}
}
