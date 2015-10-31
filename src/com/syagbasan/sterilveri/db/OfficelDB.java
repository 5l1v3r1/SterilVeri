package com.syagbasan.sterilveri.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfficelDB {

	public static final String KEY_ROWID = "_idRow";
	
	public static final String KEY_DATE = "tarih";
	public static final String KEY_HOSPITAL = "hastane";
	public static final String KEY_AMOUNT = "satis_miktari";
	public static final String KEY_DATE_MONTH = "tarih_ay";
	
	private static final String DATABASE_NAME = "OfficeDB";
	private static final String DATABASE_TABLE = "OfficeTable";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context,DATABASE_NAME , null , DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + 
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_DATE + " TEXT NOT NULL, " +
					KEY_HOSPITAL + " TEXT NOT NULL, " +
					KEY_AMOUNT + " TEXT NOT NULL, " +
					KEY_DATE_MONTH + " TEXT NOT NULL);"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public OfficelDB(Context c){
		ourContext = c;
	}
	public OfficelDB open() throws SQLException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;	
	}
	public void close(){
		ourHelper.close();
	}
	public long createEntry(String date, String person, String amount, String date_month) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);
		cv.put(KEY_HOSPITAL, person);
		cv.put(KEY_AMOUNT, amount);
		cv.put(KEY_DATE_MONTH, date_month);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	 // Updating single contact
    public long updateEntry(String date, String person,String amount, String date_month, String id) {

    	ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);
		cv.put(KEY_HOSPITAL, person);
		cv.put(KEY_AMOUNT, amount);
		cv.put(KEY_DATE_MONTH, date_month);
 
        // updating row
		return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + " = ?",
	             new String[] { id });
    }
	
	public boolean deleteToDo(long tado_id) {		  
		boolean removed = false;
		int result = ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + " = ?",
	            new String[] { String.valueOf(tado_id) });
		if (result == 1) {
			removed = true;
		}else{
			removed = false;
		}
		
		return removed;
	}
	
	public void deleteSpesific(String key_name) {		  
		ourDatabase.delete(DATABASE_TABLE, KEY_HOSPITAL + " = ?",
	            new String[] { key_name });
	}
	
	public void deleteAllData(){
		ourDatabase.delete(DATABASE_TABLE, null, null);
	}
	
	public int numberOfRows(){
	
		int numRows = (int) DatabaseUtils.queryNumEntries(ourDatabase, DATABASE_TABLE);
		return numRows;
	}
	   
	public String getAllData() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String result = "";
		
		int iRaw = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_DATE);
		int iUrl = c.getColumnIndex(KEY_HOSPITAL);
		int iDrawable = c.getColumnIndex(KEY_AMOUNT);
		int iKeyDateMounth = c.getColumnIndex(KEY_DATE_MONTH);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRaw) + " " + c.getString(iName)+ "" + c.getString(iUrl)+ "" + c.getString(iDrawable) + "" + c.getString(iKeyDateMounth) + "\n";
		}
		return result;
	}
	
	public String getAllDataWhereDate(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATE+"=?", new String[] { where }, null, null, KEY_DATE);
		String result = "";
		
		int iKeyDate = c.getColumnIndex(KEY_DATE);
		int iKeyPerson = c.getColumnIndex(KEY_HOSPITAL);
		int iKeyAmount = c.getColumnIndex(KEY_AMOUNT);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iKeyDate)+ " - " + c.getString(iKeyPerson) + " - " + c.getString(iKeyAmount) + " TL\n";
		}
		return result;
	}

	public String getDataIsPairing(String name) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String result = "";

		int iName = c.getColumnIndex(KEY_DATE);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if (name.equals(c.getString(iName))) {
				result = c.getString(iName);
			}
		}
		return result;
	}
	
	public String[] getRowId() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		
		String[] result = new String[numberOfRows()] ;

		int iName = c.getColumnIndex(KEY_ROWID);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iName);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyAllRowId(String where0,String where1,String where2) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns,KEY_DATE+"=? and "+ KEY_HOSPITAL+"=? and "+ KEY_AMOUNT+"=?", new String[] { where0,where1,where2 }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_ROWID);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getDateKeyRowId(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATE+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_ROWID);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getDate() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		
		String[] result = new String[numberOfRows()] ;

		int iName = c.getColumnIndex(KEY_DATE);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iName);
			counter++;
		}
		return result;
	}
	
	public String[] getPerson() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_HOSPITAL);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getAmount() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String[] result = new String[numberOfRows()] ;

		int iDrawables = c.getColumnIndex(KEY_AMOUNT);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iDrawables);
			counter++;
		}
		return result;
	}
	
	public String[] getDateMonth() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String[] result = new String[numberOfRows()] ;

		int iDrawables = c.getColumnIndex(KEY_DATE_MONTH);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iDrawables);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyDate(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_HOSPITAL+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_DATE);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyPerson(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_HOSPITAL+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_HOSPITAL);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyAmount(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_HOSPITAL+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_AMOUNT);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyDateMonth(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_HOSPITAL+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_DATE_MONTH);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyPersonRowId(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_HOSPITAL+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_ROWID);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	public String[] getPersonKeyAmount(String string, String string2) { 
		// icinde girilen hastahane adýný ve ay olarak girilmis tarihin ayný oldugu verilerden miktar sonucunu getir
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_HOSPITAL,KEY_AMOUNT,KEY_DATE_MONTH};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_HOSPITAL+"=? and "+KEY_DATE_MONTH +"=?", new String[] { string , string2 }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_AMOUNT);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
}