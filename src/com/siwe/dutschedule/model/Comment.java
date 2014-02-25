package com.siwe.dutschedule.model;

import java.io.Serializable;

import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.util.TimeUtil;

public class Comment extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3856943204103439236L;
	
	public final static String COL_ID = "id";
	public final static String COL_COURSEID = "courseid";
	public final static String COL_USERID = "userid";
	public final static String COL_USERNAME = "username";
	public final static String COL_CONTENT = "content";
	public final static String COL_UPTIME = "uptime";
	
	private String id;
	private String courseid;
	private String userid;
	private String username;
	private String content;
	private String uptime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String id) {
		this.courseid = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getFormatTime() {
		return TimeUtil.formatDateString(uptime);
	}

}
