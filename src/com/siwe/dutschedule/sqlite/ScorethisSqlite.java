package com.siwe.dutschedule.sqlite;

import android.content.Context;

import com.siwe.dutschedule.base.BaseSqlite;
import com.siwe.dutschedule.model.Score;

public class ScorethisSqlite extends BaseSqlite {

	public ScorethisSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "T_SCORETHIS";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
			Score.COL_NAME,
			Score.COL_CREDIT,
			Score.COL_TYPE,
			Score.COL_SCORE
		};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
			Score.COL_NAME     + " TEXT, " +
			Score.COL_CREDIT   + " TEXT, " +
			Score.COL_TYPE     + " TEXT, " +
			Score.COL_SCORE    + " TEXT " +
			");";
	}

	@Override
	protected String modelName() {
		return "Score";
	}
}