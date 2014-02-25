package com.siwe.dutschedule.model;

import java.io.Serializable;

import com.siwe.dutschedule.base.BaseModel;

public class User extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5413000072711067814L;
	
	private String id;
	private String sid;
	private String stuid;
	private String name;
	private String sign;
	private String face;
	private String grade;
	private String department;

	private String major;
	private String doinform;
	
	private String faceurl;
	
	public String getDepartment() {
		return department;
	}
	
	public String getMajor() {
		return major;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	// default is no login
	private boolean isLogin = false;
	
	

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getStuid() {
		return stuid;
	}

	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDoinform() {
		return doinform;
	}
	

	public void setDoinform(String doinform) {
		this.doinform = doinform;
	}

	public String getFaceurl() {
		return faceurl;
	}

	public void setFaceurl(String faceurl) {
		this.faceurl = faceurl;
	}

	// single instance for login
	static private User user = null;
	
	static public User getInstance () {
		if (User.user == null) {
			User.user = new User();
		}
		return User.user;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

}
