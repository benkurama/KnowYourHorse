package com.kyh.objects;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class KYHDatabase {
// =========================================================================
 // TODO Variables
 // =========================================================================
	public static String MY_DATABASE_NAME = "KYHDatabase";
	public static int MY_DATABASE_VERSION = 1;

	public SQLiteDatabase sqLiteDatabase;
	public SQLiteHelper sqLiteHelper;
	public Context context;
 // =========================================================================
 // TODO Class Object for SQLiteHelper
 // =========================================================================
	public class SQLiteHelper extends SQLiteOpenHelper {
		public SQLiteHelper(Context context, String name,CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Record Report Table
		db.execSQL("create table Record (id integer primary key autoincrement, userid integer, horseid integer, record_name text, description text, duration text, type text, status text, date text, path text)");
		// Report  Table
		db.execSQL("create table Report (id integer primary key autoincrement, userid integer, horseid integer, type text, subtype text, status text, date text, report text)");
		//Login Table
//		db.execSQL("create table Login (id integer primary key autoincrement,username text,password text,name text,email text)");
//		db.execSQL("insert into Login (username,password,name,email) values ('benkurama','pass123','Alvin','benkurama@gmail.com')");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}	
 // =========================================================================
 // TODO Main Functions
 // =========================================================================	
	public KYHDatabase(Context c){
		context = c;
	}
	/// Default Functions
	public KYHDatabase openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MY_DATABASE_NAME, null, MY_DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;	
	}
	public KYHDatabase openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MY_DATABASE_NAME, null,MY_DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;	
	}
	public void close(){
		sqLiteHelper.close();
	}
 // =========================================================================
 // TODO Implementation
 // =========================================================================
	public void insertRecord(int userid, int horseid, String recordname, String recorddesc, String duration, String type, String status, String date, String path ){
		
		ContentValues addRecords = new ContentValues();
		
		addRecords.put("userid", userid);
		addRecords.put("horseid", horseid);
		addRecords.put("record_name", recordname);
		addRecords.put("description", recorddesc);
		addRecords.put("duration", duration);
		addRecords.put("type", type);
		addRecords.put("status", status);
		addRecords.put("date", date);
		addRecords.put("path", path);
		
		sqLiteDatabase.insert("Record", null, addRecords);
	}
	// =========================================================================
	public ArrayList<RecordObj> gelAllRecords(int userid, int horseid){
		
		ArrayList<RecordObj> RecordList = new ArrayList<RecordObj>();
		Cursor cur = sqLiteDatabase.rawQuery("select * from Record where horseid =" +horseid+ " and userid = " +userid, null);
		
		if(cur.getCount() != 0){
			 
			for(cur.moveToFirst(); !(cur.isAfterLast()); cur.moveToNext()){
				
				RecordObj record = new RecordObj();
				// --
				record.ID = cur.getInt(0);
				record.USERID = cur.getInt(1);
				record.HORSEID = cur.getInt(2);
				record.RECORDNAME =  cur.getString(3);
				record.DESCRIPTION = cur.getString(4);
				record.DURATION = cur.getString(5);
				record.TYPE = cur.getString(6);
				record.STATUS = cur.getString(7);
				record.DATE = cur.getString(8);
				record.PATH = cur.getString(9);
				// --
				RecordList.add(record);
			}
		} else{
			
			RecordObj record = new RecordObj();
			RecordList.add(record);
		}

		return RecordList;
	}
	// =========================================================================
	public void insertReport(int userid, int horseid, String type, String subtype, String status, String date, String reports){
		
		ContentValues addReports = new ContentValues();
		
		addReports.put("userid", userid);
		addReports.put("horseid", horseid);
		addReports.put("type", type);
		addReports.put("subtype", subtype);
		addReports.put("status", status);
		addReports.put("date", date);
		addReports.put("report", reports);
		
		sqLiteDatabase.insert("Report", null, addReports);
	}
	// =========================================================================
	public ArrayList<ReportListObj> getAllReports(int userid, int horseid){
		
		ArrayList<ReportListObj> ReportList = new ArrayList<ReportListObj>();
		Cursor cur = sqLiteDatabase.rawQuery("select * from Report where userid = " +userid+ " and horseid = " +horseid, null);
		
		if(cur.getCount() != 0){
			
			for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
				
				ReportListObj reports = new ReportListObj();
				// --
				reports.ID = cur.getInt(0);
				reports.USERID = cur.getInt(1);
				reports.HORSEID = cur.getInt(2);
				reports.TYPE = cur.getString(3);
				reports.SUBTYPE = cur.getString(4);
				reports.STATUS = cur.getString(5);
				reports.DATE = cur.getString(6);
				reports.REPORT = cur.getString(7);
				
				ReportList.add(reports);
				// --
			}
		} else {
			
			ReportListObj reports = new ReportListObj();
			ReportList.add(reports);
		}
		
		return ReportList;
	}
 // =========================================================================
 // TODO Final
}
