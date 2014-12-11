package com.banda.bringme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.provider.BaseColumns;


public class Db {
	
	private Db() {}
	
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH);
		return sdf.format(c.getTime());
	}
	
	public abstract class Request implements BaseColumns {
		public static final String TABLE_NAME = "requests";
		public static final String ID = "id";
		public static final String TABLE = "tableName";
		public static final String TYPE = "type";
		public static final String COMMENT = "comment";
		public static final String CREATED = "created";
		public static final String STATUS = "status";
		public static final String IP_ADDR = "ipAddr";
	}

}
