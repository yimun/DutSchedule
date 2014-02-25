package com.siwe.dutschedule.model;

import java.io.Serializable;

import com.siwe.dutschedule.base.BaseModel;

public class Exam extends BaseModel implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -4920223765800002209L;
	
	public static final String COL_NAME     = "name"; 
	public static final String COL_POSITION = "position"; 
	public static final String COL_TYPE     = "type"; 
	public static final String COL_TIME     = "time";
	
	private String name;
	private String position;
	private String type;
	private String time;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	

}
