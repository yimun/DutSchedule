package com.siwe.dutschedule.base;

import com.siwe.dutschedule.model.User;


public class BaseAuth {
	
	static public boolean isLogin () {
		User user = User.getInstance();
		if (user.isLogin() == true) {
			return true;
		}
		return false;
	}
	
	static public void setLogin (Boolean status) {
		User user = User.getInstance();
		user.setLogin(status);
	}
	
	static public void setUser (User mc) {
		User user = User.getInstance();
		user.setId(mc.getId());
		user.setSid(mc.getSid());
		user.setStuid(mc.getStuid());
		user.setName(mc.getName());
		user.setSign(mc.getSign());
		user.setFace(mc.getFace());
		user.setFaceurl(mc.getFaceurl());
		user.setGrade(mc.getGrade());
		user.setDepartment(mc.getDepartment());
		user.setMajor(mc.getMajor());
		user.setDoinform(mc.getDoinform());
	}
	
	static public User getUser () {
		return User.getInstance();
	}
}