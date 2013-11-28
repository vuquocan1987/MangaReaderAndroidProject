package com.example.mangadownloader.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChapterSQLHelper extends SQLiteOpenHelper {
	
	
	public static final String TABLE_CHAPTER = "chapter";
	public static final String COLUMN_MANGANAME = "manga_name";
	public static final String COLUMN_CHAPTER_LINK = "chapter_link";
	public static final String COLUMN_CHAPTER_NO = "chapter_number";
	
	
	private static final String DATABASE_CREATE = "create table "+ TABLE_CHAPTER + "(" 
													+COLUMN_MANGANAME + " text not null, "
													+COLUMN_CHAPTER_LINK + " text not null, "
													+COLUMN_CHAPTER_NO + " integer not null);";
	private static final String DATABASE_NAME = "manga.db";
	private static final int DATABASE_VERSION = 1;
	public ChapterSQLHelper(Context context ) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(ChapterSQLHelper.class.getName(), "Upgrading database from version "+ oldVersion + " to " +newVersion
											+", which will destroy all old data "+TABLE_CHAPTER);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_CHAPTER);
		onCreate(db);
	}
}
