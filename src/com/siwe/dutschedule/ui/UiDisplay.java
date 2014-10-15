package com.siwe.dutschedule.ui;


import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseDialog;
import com.siwe.dutschedule.base.BaseUi;

public class UiDisplay extends BaseUi {

	private String path;
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		path = getIntent().getStringExtra("channallist");
		setContentView(R.layout.ui_video_view);
		try{
			mVideoView = (VideoView) findViewById(R.id.surface_view);
			mVideoView.setVideoPath(path);
			mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
			mVideoView.setMediaController(new MediaController(this));
		}
		catch(Exception e){
			new BaseDialog(this,"该频道暂时无法连接，请稍后再试……").show();	
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null)
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
		super.onConfigurationChanged(newConfig);
	}
	
	/*@Override
	public void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
*/
}
