package com.example.mangadownloader.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.service.ParsingMangaLinkService;

public class DatabaseMangaSQLHelper extends SQLiteOpenHelper {
	public static final String TABLE_CHAPTER = "chapter";
	public static final String COLUMN_CHAPTER_LINK = "chapter_link";
	public static final String COLUMN_CHAPTER_NO = "chapter_number";
	public static final String COLUMN_CHAPTER_NAME = "chapter_name";
	
	public static final String TABLE_MANGA = "manga";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MANGANAME = "manganame";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_FAVOURITE = "favourite";
	
	public static final String TABLE_CHAPTER_CREATE = "create table "+ TABLE_CHAPTER + " ( " 
													+COLUMN_MANGANAME + " text not null, "
													+COLUMN_CHAPTER_LINK + " text not null, "
													+COLUMN_CHAPTER_NO + " integer not null, "
													+COLUMN_CHAPTER_NAME + " text not null);";
	
	

	private static final String TABLE_MANGA_CREATE = "create table " + TABLE_MANGA
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_MANGANAME + " text unique, " + COLUMN_LINK
			+ " text not null, " + COLUMN_FAVOURITE + " integer not null);";
	public static final String DATABASE_NAME = "manga.db";
	private static final int DATABASE_VERSION = 1;
	private Context c;

	public DatabaseMangaSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		c = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_MANGA_CREATE);
		db.execSQL(TABLE_CHAPTER_CREATE);

		// if db is reset for first time, set the current Page back to 0
		SharedPreferences ms = c.getSharedPreferences(
				ParsingMangaLinkService.SERVICE_PREFERENCE, 0);
		ms.edit().putInt(ParsingMangaLinkService.CURRENT_PAGE, 0).commit();
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DatabaseMangaSQLHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data "
						+ TABLE_MANGA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANGA);
		onCreate(db);
	}
}