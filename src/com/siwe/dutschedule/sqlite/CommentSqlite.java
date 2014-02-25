package com.siwe.dutschedule.sqlite;

import java.util.ArrayList;

import android.content.Context;

import com.siwe.dutschedule.base.BaseSqlite;
import com.siwe.dutschedule.model.Comment;

public class CommentSqlite extends BaseSqlite {

	public CommentSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "T_COMMENT";
	}

	@Override
	protected String[] tableColumns() {
		return new String[] { Comment.COL_ID, Comment.COL_COURSEID,
				Comment.COL_USERID, Comment.COL_USERNAME, Comment.COL_CONTENT,
				Comment.COL_UPTIME };
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE IF NOT EXISTS " + tableName() + " ("
				+ Comment.COL_ID + " TEXT, " + Comment.COL_COURSEID + " TEXT, "
				+ Comment.COL_USERID + " TEXT, " + Comment.COL_USERNAME
				+ " TEXT, " + Comment.COL_CONTENT + " TEXT, "
				+ Comment.COL_UPTIME + " TIMESTAMP" + ");";
	}

	@Override
	protected String modelName() {
		return "Comment";
	}

	/**
	 * 
	 * @param courseid
	 * @param page
	 * @return
	 */
	public ArrayList<Comment> getBeforeList(Comment earlier) {
		ArrayList<Comment> rList = new ArrayList<Comment>();
		int pageLimit = 10;
		String[] params = new String[] { earlier.getCourseid(),
				earlier.getUptime(), String.valueOf(pageLimit) };
		try {
			db = dbh.getReadableDatabase();
			cursor = db.rawQuery("SELECT * FROM T_COMMENT "
					+ "WHERE courseid=? AND uptime<? ORDER BY uptime DESC "
					+ "LIMIT ? ;", params);
			if (cursor == null)
				return rList;
			while (cursor.moveToNext()) {
				Comment rRow = (Comment) this.getModel(cursor, tableColumns());
				rList.add(rRow);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return rList;
	}

	public void saveAll(ArrayList<Comment> datalist) {

		for (Comment item : datalist) {
			if (this.exists("id=?", new String[] { item.getId() }))
				continue;
			this.insert(item);
		}
	}

	public boolean existsAndHasBefore(Comment comment) {
		
		if (this.exists("id=?", new String[] { comment.getId() })
				&& this.exists("uptime<?", new String[] { comment.getUptime() })) {
			return true;

		}
		return false;

	}
}
