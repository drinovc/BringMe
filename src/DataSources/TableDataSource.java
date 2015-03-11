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

public class TableDataSource {

	private SQLiteDatabase database;
	private DbHelper dbHelper;

	private static final String COMMA = ", ";
	private static final String SETTER = "=?";
	
	public TableDataSource(Context context) {
		dbHelper = DbHelper.getHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long insertTable(Table t){
		SQLiteStatement statement = database.compileStatement(
				"INSERT INTO " + Db.Table.TABLE_NAME + "(" +
				Db.Table.TABLE + COMMA +
				Db.Table.DESCRIPTION + COMMA +
				Db.Table.TYPE + COMMA +
				Db.Table.SHAPE + COMMA +
				Db.Table.COUNT + COMMA +
				Db.Table.NUMBER + COMMA +
				Db.Table.COLOR + COMMA +
				Db.Table.ASIZE + COMMA +
				Db.Table.BSIZE + COMMA + 
				Db.Table.XPOSITION + COMMA +
				Db.Table.YPOSITION +
				") VALUES (?,?,?,?,?,?,?,?,?,?,?)"
				);
		statement.bindString(1, t.getTableName());
		statement.bindString(2, t.getDescription());
		statement.bindLong(3, t.getTypeInt());
		statement.bindLong(4, t.getShapeInt());
		statement.bindLong(5, t.getCount());
		statement.bindLong(6, t.getNumber());
		statement.bindLong(7, t.getColor());
		statement.bindLong(8, t.getAsize());
		statement.bindLong(9, t.getBsize());
		statement.bindLong(10, t.getXposition());
		statement.bindLong(11, t.getYposition());
		return statement.executeInsert();
	}

	public int updateTable(Table t){
		SQLiteStatement statement = database.compileStatement(
				"UPDATE " + Db.Table.TABLE_NAME +
				" SET " +
				Db.Table.TABLE + SETTER + COMMA +
				Db.Table.DESCRIPTION + SETTER + COMMA +
				Db.Table.TYPE + SETTER + COMMA +
				Db.Table.SHAPE + SETTER + COMMA +
				Db.Table.COUNT + SETTER + COMMA +
				Db.Table.NUMBER + SETTER + COMMA +
				Db.Table.COLOR + SETTER + COMMA +
				Db.Table.ASIZE + SETTER + COMMA +
				Db.Table.BSIZE + SETTER + COMMA + 
				Db.Table.XPOSITION + SETTER + COMMA +
				Db.Table.YPOSITION + SETTER +
				" WHERE ID" + SETTER + ";");
		statement.bindString(1, t.getTableName());
		statement.bindString(2, t.getDescription());
		statement.bindLong(3, t.getTypeInt());
		statement.bindLong(4, t.getShapeInt());
		statement.bindLong(5, t.getCount());
		statement.bindLong(6, t.getNumber());
		statement.bindLong(7, t.getColor());
		statement.bindLong(8, t.getAsize());
		statement.bindLong(9, t.getBsize());
		statement.bindLong(10, t.getXposition());
		statement.bindLong(11, t.getYposition());
		statement.bindLong(12, t.getID());
		return statement.executeUpdateDelete();
	}
	
	public List<Table> getAllEntries() {
		List<Table> list = new ArrayList<Table>();
		Cursor cursor = getAllEntriesCursor();
		while (!cursor.isAfterLast()) {
			list.add(cursorToTable(cursor));
			cursor.moveToNext();
		}
		return list;
	}

	public Cursor getAllEntriesCursor() {
		String sql = "SELECT " + 
				"A." + Db.Table.ID + COMMA +
				"A." + Db.Table.TABLE + COMMA +
				"A." + Db.Table.DESCRIPTION + COMMA +
				"A." + Db.Table.TYPE + COMMA +
				"A." + Db.Table.SHAPE + COMMA +
				"A." + Db.Table.NUMBER + COMMA +
				"A." + Db.Table.COLOR + COMMA +
				"A." + Db.Table.ASIZE + COMMA +
				"A." + Db.Table.BSIZE + COMMA +
				"A." + Db.Table.XPOSITION + COMMA +
				"A." + Db.Table.YPOSITION + COMMA +
				"A." + Db.Table.COUNT + COMMA +
				" CASE WHEN B." + Db.Request.ID +" IS NULL THEN 0 ELSE " + "COUNT(A." + Db.Table.ID + ") END AS REQUESTS" +
				" FROM " + Db.Table.TABLE_NAME + " A" +
				" LEFT JOIN " + Db.Request.TABLE_NAME + " B ON A." + Db.Table.ID + "=B." + Db.Request.TABLE +
				" AND B." + Db.Request.STATUS + "=" + String.valueOf(Request.Status.OPEN.ordinal()) +
				" GROUP BY A." + Db.Table.ID + COMMA +
				"A." + Db.Table.TABLE + COMMA +
				"A." + Db.Table.DESCRIPTION + COMMA +
				"A." + Db.Table.TYPE + COMMA +
				"A." + Db.Table.SHAPE + COMMA +
				"A." + Db.Table.NUMBER + COMMA +
				"A." + Db.Table.COLOR + COMMA +
				"A." + Db.Table.ASIZE + COMMA +
				"A." + Db.Table.BSIZE + COMMA +
				"A." + Db.Table.XPOSITION + COMMA +
				"A." + Db.Table.YPOSITION + ";";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		return cursor;
	}

	public void deleteEntry(Table t) {
		String sql = "DELETE FROM " + Db.Table.TABLE_NAME + " WHERE id=" + String.valueOf(t.getID()) + ";";
		database.execSQL(sql);
	}

	private Table cursorToTable(Cursor cursor) {
		Table entry = new Table();
		entry.setID(cursor.getInt(0));
		entry.setTableName(cursor.getString(1));
		entry.setDescription(cursor.getString(2));
		entry.setType(cursor.getInt(3));
		entry.setShape(cursor.getInt(4));
		entry.setNumber(cursor.getInt(5));
		entry.setColor(cursor.getInt(6));
		entry.setAsize(cursor.getInt(7));
		entry.setBsize(cursor.getInt(8));
		entry.setXposition(cursor.getInt(9));
		entry.setYposition(cursor.getInt(10));
		entry.setCount(cursor.getInt(11));
		entry.setRequests(cursor.getInt(12));
		return entry;
	}
}