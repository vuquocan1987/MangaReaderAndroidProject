package anvu.bk.database.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import anvu.bk.service.ParsingMangaLinkService;


public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String TABLE_CHAPTER = "chapter";
	public static final String COLUMN_CHAPTER_LINK = "chapter_link";
	public static final String COLUMN_CHAPTER_NO = "chapter_no";
	public static final String COLUMN_CHAPTER_NAME = "chapter_name";
	public static final String COLUMN_CHAPTER_STATUS = "status";
	public static final String COLUMN_CHAPTER_CURRENT_PAGE = "current_page";
	public static final String COLUMN_CHAPTER_ID = "_id";
	public static final String COLUMN_CHAPTER_LOCALPATH = "local_path";
	
	public static final String TABLE_MANGA = "manga";
	public static final String COLUMN_MANGA_ID = "_id";
	public static final String COLUMN_MANGANAME = "manganame";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_FAVOURITE = "favourite";
			
	public static final String TABLE_CHAPTER_CREATE = "create table "+ TABLE_CHAPTER + " ( "
													+COLUMN_CHAPTER_ID +" integer primary key autoincrement, " 
													+COLUMN_MANGANAME + " text not null, "
													+COLUMN_CHAPTER_LINK + " text not null, "
													+COLUMN_CHAPTER_NO + " integer not null, "
													+COLUMN_CHAPTER_NAME + " text not null, "
													+COLUMN_CHAPTER_STATUS + " int not null);";

	private static final String TABLE_MANGA_CREATE = "create table " + TABLE_MANGA
			+ "(" + COLUMN_MANGA_ID + " integer primary key autoincrement, "
			+ COLUMN_MANGANAME + " text unique, " + COLUMN_LINK
			+ " text not null, " + COLUMN_FAVOURITE + " integer not null);";
	public static final String DATABASE_NAME = "manga.db";
	private static final int DATABASE_VERSION = 2;
	
	private Context c;

	public DatabaseHelper(Context context) {
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
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data "
						+ TABLE_MANGA);
		db.execSQL(TABLE_CHAPTER_CREATE);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANGA);
//		onCreate(db);
//		db.execSQL("alter table "+TABLE_CHAPTER+ " add column " + COLUMN_CHAPTER_STATUS + " integer default 0 " );
	}
}
