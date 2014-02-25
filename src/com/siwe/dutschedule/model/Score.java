package com.siwe.dutschedule.model;

import java.io.Serializable;

import com.siwe.dutschedule.base.BaseModel;

public class Score extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6413017942220663776L;
	
	public static final String COL_NAME   = "name";
	public static final String COL_CREDIT = "credit";
	public static final String COL_TYPE   = "type";
	public static final String COL_SCORE  = "score";
	
	private String name;
	private String credit;
	private String type;
	private String score;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
}
