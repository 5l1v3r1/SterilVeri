package com.syagbasan.sterilveri.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmpomedAnadolDB {

	public static final String KEY_ROWID = "_idRow";
	
	public static final String KEY_DATE = "tarih";
	public static final String KEY_PERSON = "hastane_adi";
	public static final String KEY_PRODUCT = "fatura_no";
	public static final String KEY_AMOUNT = "fatura_miktari";
	
	private static final String DATABASE_NAME = "EmpomedAnadolDB";
	private static final String DATABASE_TABLE = "EmpomedAnadolTable";
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
					KEY_PERSON + " TEXT NOT NULL, " +
					KEY_PRODUCT + " TEXT NOT NULL, " +
					KEY_AMOUNT + " TEXT NOT NULL);"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public EmpomedAnadolDB(Context c){
		ourContext = c;
	}
	public EmpomedAnadolDB open() throws SQLException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;	
	}
	public void close(){
		ourHelper.close();
	}
	public long createEntry(String date, String person,String product, String amount) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);
		cv.put(KEY_PERSON, person);
		cv.put(KEY_PRODUCT, product);
		cv.put(KEY_AMOUNT, amount);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	 // Updating single contact
    public long updateEntry(String date, String person,String product, String amount, String id) {

    	ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);
		cv.put(KEY_PERSON, person);
		cv.put(KEY_PRODUCT, product);
		cv.put(KEY_AMOUNT, amount);
 
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
		ourDatabase.delete(DATABASE_TABLE, KEY_PERSON + " = ?",
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
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String result = "";
		
		int iRaw = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_DATE);
		int iUrl = c.getColumnIndex(KEY_PERSON);
		int iGenre = c.getColumnIndex(KEY_PRODUCT);
		int iDrawable = c.getColumnIndex(KEY_AMOUNT);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRaw) + " " + c.getString(iName)+ "" + c.getString(iUrl)+ c.getString(iGenre)+ "" + c.getString(iDrawable) + "\n";
		}
		return result;
	}
	
	public String getAllDataWhereDate(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATE+"=?", new String[] { where }, null, null, KEY_DATE);
		String result = "";
		
		int iKeyDate = c.getColumnIndex(KEY_DATE);
		int iKeyPerson = c.getColumnIndex(KEY_PERSON);
		int iKeyProduct = c.getColumnIndex(KEY_PRODUCT);
		int iKeyAmount = c.getColumnIndex(KEY_AMOUNT);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iKeyDate)+ " - " + c.getString(iKeyPerson)+ " - " +c.getString(iKeyProduct)+ " - " + c.getString(iKeyAmount) + " TL\n";
		}
		return result;
	}

	public String getDataIsPairing(String name) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
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
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
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
	
	public String[] getPersonKeyAllRowId(String where0,String where1,String where2,String where3) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns,KEY_DATE+"=? and "+ KEY_PERSON+"=? and "+ KEY_PRODUCT+"=? and "+ KEY_AMOUNT+"=?", new String[] { where0,where1,where2,where3 }, null, null, KEY_DATE);
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
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
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
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
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
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_PERSON);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getProduct() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE, null);
		String[] result = new String[numberOfRows()] ;

		int iGenres = c.getColumnIndex(KEY_PRODUCT);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iGenres);
			counter++;
		}
		return result;
	}
	
	public String[] getAmount() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
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
	
	public String[] getPersonKeyDate(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_PERSON+"=?", new String[] { where }, null, null, KEY_DATE);
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
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_PERSON+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_PERSON);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyProduct(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_PERSON+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_PRODUCT);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyAmount(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_PERSON+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_AMOUNT);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
	
	public String[] getPersonKeyPersonRowId(String where) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_PERSON,KEY_PRODUCT,KEY_AMOUNT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_PERSON+"=?", new String[] { where }, null, null, KEY_DATE);
		String[] result = new String[numberOfRows()] ;

		int iUrl = c.getColumnIndex(KEY_ROWID);
		
		int counter = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result[counter] = c.getString(iUrl);
			counter++;
		}
		return result;
	}
}