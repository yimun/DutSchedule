package com.siwe.dutschedule.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseFragment;

public class FragMain extends BaseFragment{
	
	private LinearLayout rl;
	
	private ImageButton btn_schedule;
	private ImageButton btn_score;
	private ImageButton btn_exam;
	private ImageButton btn_news;
	private ImageButton btn_video;
	private ImageButton btn_notice;
	
	public FragMain(){
		
	}

	public FragMain(Context context) {
		super(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.ui_frag_main, container, false);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getWidget();
		setEvent();
	}

	void getWidget(){
		rl = (LinearLayout) mView.findViewById(R.id.parent);
		btn_schedule = (ImageButton) mView.findViewById(R.id.imageButton1);
		btn_score    = (ImageButton) mView.findViewById(R.id.imageButton2);
		btn_exam     = (ImageButton) mView.findViewById(R.id.imageButton3);
		btn_news     = (ImageButton) mView.findViewById(R.id.imageButton4);
		btn_video    = (ImageButton) mView.findViewById(R.id.imageButton5);
		btn_notice   = (ImageButton) mView.findViewById(R.id.imageButton6);
	}
	
	void setEvent(){
		((UiHome)this.baseUi).bidirSldingLayout.setScrollEvent(mView);
		rl.startAnimation(AnimationUtils.loadAnimation(
				this.baseUi,R.anim.home_main));
		Mlistener mListener = new Mlistener();
		btn_schedule.setOnClickListener(mListener);		
		btn_score.setOnClickListener(mListener);
		btn_exam.setOnClickListener(mListener);
		btn_news.setOnClickListener(mListener);
		btn_video.setOnClickListener(mListener);
		btn_notice.setOnClickListener(mListener);
	}
	
	class Mlistener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.imageButton1:
				baseUi.forward(UiSchedule.class);
				break;
			case R.id.imageButton2:
				baseUi.forward(UiScore.class);
				break;
			case R.id.imageButton3:
				baseUi.forward(UiExam.class); 
				break;
			case R.id.imageButton4:
				baseUi.forward(UiNews.class); 
				break;
			case R.id.imageButton5:
				baseUi.forward(UiVideo.class);
				break;
			case R.id.imageButton6:
				Intent sendIntent = new Intent();
				sendIntent.setClassName(baseUi, "com.baidu.android.pushservice.richmedia.MediaListActivity");
				sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(sendIntent);
				break;
			
			}
		}
	}
	

}
