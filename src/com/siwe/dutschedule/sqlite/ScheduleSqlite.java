package com.siwe.dutschedule.sqlite;

import java.util.ArrayList;

import android.content.Context;

import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.base.BaseSqlite;
import com.siwe.dutschedule.model.Schedule;

public class ScheduleSqlite extends BaseSqlite {

	public ScheduleSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "T_SCHEDULE";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
			Schedule.COL_NAME,
			Schedule.COL_CREDIT,
			Schedule.COL_TYPE,
			Schedule.COL_TEACHER,
			Schedule.COL_WEEKS,
			Schedule.COL_WEEKDAY,
			Schedule.COL_SEQUE,
			Schedule.COL_AMOUNT,
			Schedule.COL_POSITION,
		};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
			Schedule.COL_NAME     + " TEXT, " +
			Schedule.COL_CREDIT   + " TEXT, " +
			Schedule.COL_TYPE     + " TEXT, " +
			Schedule.COL_TEACHER  + " TEXT, " +
			Schedule.COL_WEEKS    + " TEXT, " +
			Schedule.COL_WEEKDAY  + " TEXT, " +
			Schedule.COL_SEQUE    + " TEXT, " +
			Schedule.COL_AMOUNT   + " TEXT, " +
			Schedule.COL_POSITION + " TEXT " +
			");";
	}
	
	public ArrayList<Schedule> getByDay(int weekday){
		
		ArrayList<? extends BaseModel> list;
		list = this.query(null, Schedule.COL_WEEKDAY+"=?", 
				new String[]{String.valueOf(weekday)});
		return (ArrayList<Schedule>) list;
	
	}

	@Override
	protected String modelName() {
		return "Schedule";
	}

	public void updateOne(Schedule schedule) {
		this.update(schedule, null, "weekday=? AND seque=?", 
				new String[]{schedule.getWeekday(),schedule.getSeque()});
		
	}
	
	

}