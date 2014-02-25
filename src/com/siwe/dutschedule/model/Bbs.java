package com.siwe.dutschedule.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.util.TimeUtil;

public class Bbs extends BaseModel implements Parcelable{

	/**
	 * @author linwei
	 */
	
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	public static final String COL_LASTUPDATE = "lastupdate";
	public static final String COL_UNREAD = "unread";
	
	
	private String id;
	private String name;
	private String lastupdate;
	private String unread;
	
	//
	public String getUnread() {
		return unread;
	}
	
	public int getUnreadInt() {
		return Integer.parseInt(unread);
	}
	
	public boolean isAllRead(){
		int judge = Integer.parseInt(unread);
		return judge == 0;
	}
	
	public void setUnread(String unread) {
		this.unread = unread;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
	
	public String getFormatTime() {
		//TODO
		try {
			return TimeUtil.formatDateString(lastupdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TimeUtil.getCurrent();
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(lastupdate);
		dest.writeString(unread);
	}
	
	
	public static final Parcelable.Creator<Bbs> CREATOR = new Parcelable.Creator<Bbs>() {

		@Override
		public Bbs createFromParcel(Parcel source) {
			Bbs bbs = new Bbs();
			bbs.setId(source.readString());
			bbs.setName(source.readString());
			bbs.setLastupdate(source.readString());
			bbs.setUnread(source.readString());
			return bbs;
		}

		@Override
		public Bbs[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};

}
