package com.siwe.dutschedule.sqlite;

import android.content.Context;

public class ScoreallSqlite extends ScorethisSqlite {

	public ScoreallSqlite(Context context) {
		super(context);
	}
	
	@Override
	protected String tableName() {
		return "T_SCOREALL";
	}

}
