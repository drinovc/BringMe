package DataSources;

import java.util.ArrayList;
import java.util.List;


import DataSources.Request.Status;
import Database.Db;
import Database.DbHelper;
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
		
		statement.bindLong(1, r.getTableID());
		statement.bindLong(2, r.getTypeInt());	
		statement.bindString(3, r.getComment());
		statement.bindString(4, Db.getDate());
		statement.bindLong(5, Request.Status.OPEN.ordinal());
		statement.bindString(6, r.getIpAddr());
		return statement.executeInsert();
	}
	
	public List<Request> getAllEntries() {
		List<Request> list = new ArrayList<Request>();
		Cursor cursor = getAllEntriesCursor();
		while (!cursor.isAfterLast()) {
			list.add(cursorToRequest(cursor));
			cursor.moveToNext();
		}
		return list;
	}

	public Cursor getAllEntriesCursor() {
		String sql = "SELECT " + 
				Db.Request.ID + COMMA +
				Db.Request.TABLE + COMMA + 
				Db.Request.TYPE + COMMA +
				Db.Request.STATUS + COMMA +
				Db.Request.COMMENT + COMMA +
				Db.Request.IP_ADDR + COMMA +
				Db.Request.CREATED +
				" FROM " + Db.Request.TABLE_NAME +
				" WHERE STATUS=" + String.valueOf(Request.Status.OPEN.ordinal()) +
				" ORDER BY ID DESC;";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		return cursor;
	}

	public void deleteEntry(Request r) {
		String sql = "DELETE FROM " + Db.Request.TABLE_NAME + " WHERE id=" + String.valueOf(r.getID()) + ";";
		database.execSQL(sql);
	}

	public void closeEntriesByTableID(long tableID) {
		String sql = "UPDATE " + Db.Request.TABLE_NAME +
				" SET " + Db.Request.STATUS + "=" + String.valueOf(Status.CLOSED.ordinal()) +
				" WHERE " + Db.Request.TABLE + "=" + String.valueOf(tableID) + ";";
		database.execSQL(sql);
	}
	
	private Request cursorToRequest(Cursor cursor) {
		Request entry = new Request();
		entry.setID(cursor.getLong(0));
		entry.setTableID(cursor.getLong(1));
		entry.setType(cursor.getInt(2));
		entry.setStatus(cursor.getInt(3));
		entry.setComment(cursor.getString(4));
		entry.setIpAddr(cursor.getString(5));
		entry.setCreated(cursor.getString(6));
		return entry;
	}

}