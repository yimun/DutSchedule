package com.siwe.dutschedule.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.siwe.dutschedule.base.BaseModel;

public class Schedule extends BaseModel implements Parcelable {

	/**
	 * 
	 */
	public static final String COL_NAME = "name";
	public static final String COL_CREDIT = "credit";
	public static final String COL_TYPE = "type";
	public static final String COL_TEACHER = "teacher";
	public static final String COL_WEEKS = "weeks";
	public static final String COL_WEEKDAY = "weekday";
	public static final String COL_SEQUE = "seque";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_POSITION = "position";

	private String name;
	private String credit;
	private String type;
	private String teacher;
	private String weeks;
	private String weekday;
	private String seque;
	private String amount;
	private String position;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(credit);
		dest.writeString(type);
		dest.writeString(teacher);
		dest.writeString(weeks);
		dest.writeString(weekday);
		dest.writeString(seque);
		dest.writeString(amount);
		dest.writeString(position);
	}

	public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

		@Override
		public Schedule createFromParcel(Parcel source) {
			Schedule schedule = new Schedule();
			schedule.setName(source.readString());
			schedule.setCredit(source.readString());
			schedule.setType(source.readString());
			schedule.setTeacher(source.readString());
			schedule.setWeeks(source.readString());
			schedule.setWeekday(source.readString());
			schedule.setSeque(source.readString());
			schedule.setAmount(source.readString());
			schedule.setPosition(source.readString());
			return schedule;
		}

		@Override
		public Schedule[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}

	};

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

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getSeque() {
		return seque;
	}

	public void setSeque(String seque) {
		this.seque = seque;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
