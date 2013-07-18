package com.siwe.dutschedule.video;


import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.siwe.dutschedule.R;

public class DisplayActivity extends Activity {

	private String path;
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		path = getIntent().getStringExtra("channallist");
		setContentView(R.layout.activity_video_view);
		try{
			mVideoView = (VideoView) findViewById(R.id.surface_view);
			mVideoView.setVideoPath(path);
			mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
			mVideoView.setMediaController(new MediaController(this));
		}
		catch(Exception e){
			new AlertDialog.Builder(DisplayActivity.this).setMessage("该频道暂时无法连接，请稍后再试……").create().show();	
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
