package com.banda.bringme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "RequestDatabase.db";
	
	private static final String COMMA = ", ";
	
	private static DbHelper instance;
	
	public static synchronized DbHelper getHelper(Context context) {
		if(instance == null) {
			instance = new DbHelper(context);
		}
		return instance;
	}
	
	private DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		createRequestsTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Db.Request.TABLE_NAME + ";");
		onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
	private void createRequestsTable(SQLiteDatabase db) {
		db.execSQL(
			"CREATE TABLE " + Db.Request.TABLE_NAME + " (" +
			Db.Request.ID + " INTEGER PRIMARY KEY, " +
			Db.Request.TABLE + " TEXT" + COMMA +
			Db.Request.TYPE + " TEXT" + COMMA +
			Db.Request.COMMENT  + " TEXT" + COMMA +
			Db.Request.CREATED  + " TEXT" + COMMA +
			Db.Request.STATUS  + " INTEGER" + COMMA +
			Db.Request.IP_ADDR + " TEXT" +
			");"
		);
	}
	
}
