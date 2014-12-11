package com.banda.bringme;

import java.util.ArrayList;
import java.util.List;

import com.banda.bringme.Db;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class RequestDataSource {

	private SQLiteDatabase database;
	private DbHelper dbHelper;

	private static final String COMMA = ", ";
	
	public RequestDataSource(Context context) {
		dbHelper = DbHelper.getHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long addNewRequest(Request r){
		SQLiteStatement statement = database.compileStatement(
				"INSERT INTO " + Db.Request.TABLE_NAME + "(" +
				Db.Request.TABLE + COMMA + 
				Db.Request.TYPE + COMMA +
				Db.Request.COMMENT + COMMA +
				Db.Request.CREATED + COMMA +
				Db.Request.STATUS + COMMA +
				Db.Request.IP_ADDR +
				") VALUES (?, ?, ?, ?, ?, ?)"); 
		
		statement.bindString(1, r.table);
		statement.bindString(2, r.type);	
		statement.bindString(3, r.comment);
		statement.bindString(4, Db.getDate());
		statement.bindLong(5, Request.STATUS_NEW);
		statement.bindString(6, r.ipAddr);
		return statement.executeInsert();
	}
	
	public List<Request> getAllEntries() {
		List<Request> list = new ArrayList<Request>();
		String sql = "SELECT * FROM " + Db.Request.TABLE_NAME + " ORDER BY ID DESC;";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(cursorToRequest(cursor));
			cursor.moveToNext();
		}
		return list;
	}

	public Cursor getAllEntriesCursor() {
		String sql = "SELECT * FROM " + Db.Request.TABLE_NAME + " ORDER BY ID DESC;";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		return cursor;
	}

	public void deleteEntry(Request r) {
		String sql = "DELETE FROM " + Db.Request.TABLE_NAME + " WHERE id=" + String.valueOf(r.ID) + ";";
		database.execSQL(sql);
	}

	private Request cursorToRequest(Cursor cursor) {
		Request entry = new Request();
		entry.ID = cursor.getInt(0);
		entry.table = cursor.getString(1);
		entry.type = cursor.getString(2);
		entry.comment = cursor.getString(3);
		entry.created = cursor.getString(4);
		entry.status = cursor.getLong(5);
		entry.ipAddr = cursor.getString(6);
		return entry;
	}

}