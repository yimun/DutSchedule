package com.siwe.dutschedule.model;

import com.siwe.dutschedule.base.BaseModel;

public class News extends BaseModel {

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getUptime() {
		return uptime==null?" ":uptime;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}



	private String title;
	private String url;
	private String uptime;
	
	public News() {
		
	}

}
