package com.siwe.dutschedule.base;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseSqlite {

	private static final String DB_NAME = "duthelper.db";
	private static final int DB_VERSION = 3;

	public DbHelper dbh = null;
	public SQLiteDatabase db = null;
	public Cursor cursor = null;
	protected Context context;

	public BaseSqlite(Context context) {
		this.context = context;
		dbh = new DbHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public void insert(BaseModel values) {
		try {
			db = dbh.getWritableDatabase();
			db.insert(tableName(), null, getValues(values, null));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public void insertList(ArrayList<? extends BaseModel> lists) {
		try {
			db = dbh.getWritableDatabase();
			for (BaseModel item : lists) {
				// TODO whether use null or tableColumns()
				db.insert(tableName(), null, getValues(item, null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/**
	 * 
	 * @param values
	 *            BaseModel
	 * @param columns
	 *            String[]
	 * @param where
	 *            String
	 * @param params
	 *            String[]
	 */
	public void update(BaseModel values, String[] columns, String where,
			String[] params) {
		try {
			db = dbh.getWritableDatabase();
			db.update(tableName(), getValues(values, columns), where, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/**
	 * delete all and insert list
	 * 
	 * @param datalist
	 *            ArrayList<? extends BaseModel>
	 */
	public void updateAll(ArrayList<? extends BaseModel> datalist) {
		this.delete(null, null);
		this.insertList(datalist);
	}

	/**
	 * delete all as {delete(null,null)}
	 */
	public void delete(String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			db.delete(tableName(), where, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/**
	 * 
	 * @param columns
	 *            {null if all}
	 * @param where
	 * @param params
	 * @return
	 */
	public ArrayList<? extends BaseModel> query(String[] columns, String where,
			String[] params) {
		ArrayList<BaseModel> rList = new ArrayList<BaseModel>();
		if (columns == null)
			columns = tableColumns();
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), columns, where, params, null, null,
					null);
			while (cursor.moveToNext()) {
				BaseModel rRow = this.getModel(cursor, columns);
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

	public int count(String where, String[] params) {
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), tableColumns(), where, params, null,
					null, null);
			return cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return 0;
	}

	public boolean exists(String where, String[] params) {
		boolean result = false;
		int count = this.count(where, params);
		if (count > 0) {
			result = true;
		}

		return result;
	}

	abstract protected String tableName();

	abstract protected String[] tableColumns();

	abstract protected String createSql();

	abstract protected String modelName();

	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	protected class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			db.execSQL(createSql());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(upgradeSql());
			onCreate(db);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}
	}

	/**
	 * Get basemodel from db cursor
	 * 
	 * @param cur
	 * @param fieldsName
	 * @return ? extends BaseModel
	 * @throws Exception
	 */
	protected BaseModel getModel(Cursor cur, String[] fieldsName)
			throws Exception {

		String modelClassName = C.packageName.model + "." + modelName();
		BaseModel modelObj = (BaseModel) Class.forName(modelClassName)
				.newInstance();
		Class<? extends BaseModel> modelClass = modelObj.getClass();
		// auto-setting model fields
		for (String fieldname : fieldsName) {
			Field field = modelClass.getDeclaredField(fieldname);
			field.setAccessible(true); // have private to be accessable
			field.set(modelObj, cur.getString(cur.getColumnIndex(fieldname)));
		}
		return modelObj;
	}

	/**
	 * Get ContentValues from basemodel
	 * 
	 * @param model
	 * @param fieldsName
	 * @return ContentValues
	 */
	protected ContentValues getValues(BaseModel model, String[] fieldsName) {
		ContentValues cv = new ContentValues();
		if (fieldsName == null)
			fieldsName = tableColumns();
		try {
			for (String fieldname : fieldsName) {
				Field field = model.getClass().getDeclaredField(fieldname);
				field.setAccessible(true);
				String get = (String) field.get(model);
				cv.put(fieldname, get);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return cv;
	}



}