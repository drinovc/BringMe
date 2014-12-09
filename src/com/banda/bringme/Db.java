package com.banda.bringme;

import android.provider.BaseColumns;

public class Db {
	
	private Db() {}
	
	public abstract class Request implements BaseColumns {
		public static final String TABLE_NAME = "requests";
		public static final String ID = "id";
		public static final String TABLE = "tableName";
		public static final String TYPE = "type";
	}

}
