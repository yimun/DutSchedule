package com.siwe.dutschedule.sqlite;

import java.util.ArrayList;

import android.content.Context;

import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.base.BaseSqlite;
import com.siwe.dutschedule.model.Bbs;

public class BbsSqlite extends BaseSqlite {

	public BbsSqlite(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String tableName() {
		return "T_BBS";
	}

	@Override
	protected String[] tableColumns() {
		return new String[] { Bbs.COL_ID, Bbs.COL_NAME, Bbs.COL_UNREAD,
				Bbs.COL_LASTUPDATE };

	}

	@Override
	protected String createSql() {
		return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" + Bbs.COL_ID
				+ " TEXT, " + Bbs.COL_NAME + " TEXT, " + Bbs.COL_UNREAD
				+ " TEXT, " + Bbs.COL_LASTUPDATE + " TIMESTAMP" + ");";
	}

	@Override
	protected String modelName() {
		// TODO Auto-generated method stub
		return "Bbs";
	}

	public String getBbsData() {
		StringBuilder sb = new StringBuilder();
		ArrayList<Bbs> bbslist = (ArrayList<Bbs>) this.query(null, null, null);
		if (bbslist.isEmpty())
			return null;
		for (Bbs item : bbslist) {
			sb.append(item.getId());
			sb.append("#");
			sb.append(item.getName());
			sb.append("#");
			sb.append(item.getLastupdate());
			sb.append("%");
		}
		return sb.toString();
	}

	/**
	 * insert the new bbs by subject you learn this term
	 * 
	 * @param datalist
	 */
	public void insertThisTerm(ArrayList<Bbs> datalist) {
		for (Bbs item : datalist) {
			if (!this.exists("id=?", new String[] { item.getId() }))
				this.insert(item);
		}
	}

	/**
	 * list all bbs by time
	 * 
	 * @return ArrayList<Bbs>
	 */
	public ArrayList<Bbs> queryByTime() {
		ArrayList<Bbs> rList = new ArrayList<Bbs>();
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), tableColumns(), null, null, null,
					null, "lastupdate DESC");
			while (cursor.moveToNext()) {
				Bbs rRow = (Bbs) this.getModel(cursor, tableColumns());
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

	/**
	 * updata the unread count 
	 * @param listUnread
	 */
	public void updateUnread(ArrayList<Bbs> listUnread) {
		for (Bbs item : listUnread) {
			this.update(item, new String[] { "unread" }, "id=?",
					new String[] { item.getId() });
		}
	}
	
	/**
	 * update the lastupdate time
	 * @param bbs
	 */
	public void updateTime(Bbs bbs) {
		this.update(bbs, new String[]{"lastupdate"}, "id=?", 
				new String[]{bbs.getId()});
	}
	
	public void clearUnread(Bbs bbs) {
		bbs.setUnread("0");
		this.update(bbs, new String[]{"unread"}, "id=?", 
				new String[]{bbs.getId()});
	}

	public ArrayList<Bbs> getUnreadBbs() {
		// TODO Auto-generated method stub
		ArrayList<Bbs> list = null;
		list = (ArrayList<Bbs>) this.query(null, "unread>?", new String[]{"0"});
		return list;
	}
}
