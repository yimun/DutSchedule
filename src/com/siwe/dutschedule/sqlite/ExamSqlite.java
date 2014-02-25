package com.siwe.dutschedule.sqlite;

import android.content.Context;

import com.siwe.dutschedule.base.BaseSqlite;
import com.siwe.dutschedule.model.Exam;

public class ExamSqlite extends BaseSqlite {

	public ExamSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "T_EXAM";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
			Exam.COL_NAME,
			Exam.COL_POSITION,
			Exam.COL_TYPE,
			Exam.COL_TIME
		};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
			Exam.COL_NAME     + " TEXT, " +
			Exam.COL_POSITION + " TEXT, " +
			Exam.COL_TYPE     + " TEXT, " +
			Exam.COL_TIME     + " TEXT " +
			");";
	}

	@Override
	protected String modelName() {
		return "Exam";
	}

}