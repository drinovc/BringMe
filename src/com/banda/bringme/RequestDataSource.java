package com.banda.bringme;

import java.util.ArrayList;
import java.util.List;

import com.banda.bringme.Db;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RequestDataSource {

	private SQLiteDatabase database;
	private DbHelper dbHelper;

	public RequestDataSource(Context context) {
		dbHelper = DbHelper.getHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addNewRequest(Request r){
		String sql = "INSERT INTO " + Db.Request.TABLE_NAME + 
				" (" + Db.Request.TABLE + ")" +
				" VALUES " + 
				"('" + r.TABLE + "')";
		database.execSQL(sql);
	}
	
	public List<Request> getAllEntries() {
		List<Request> list = new ArrayList<Request>();
		String sql = "SELECT * FROM " + Db.Request.TABLE_NAME + ";";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(cursorToRequest(cursor));
			cursor.moveToNext();
		}
		return list;
	}

	public Cursor getAllEntriesCursor() {
		String sql = "SELECT * FROM " + Db.Request.TABLE_NAME
				+ " ORDER BY ID ASC;";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		return cursor;
	}

	public void deleteEntry(Request s) {
		String sql = "DELETE FROM " + Db.Request.TABLE_NAME + " WHERE id="
				+ String.valueOf(s.ID) + ";";
		database.execSQL(sql);
	}

	private Request cursorToRequest(Cursor cursor) {
		Request entry = new Request();
		entry.ID = cursor.getInt(0);
		entry.TABLE = cursor.getString(1);
		return entry;
	}

}