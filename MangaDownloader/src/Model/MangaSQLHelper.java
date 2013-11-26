package Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MangaSQLHelper extends SQLiteOpenHelper {
	
	
	public static final String TABLE_MANGA = "manga";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MANGANAME = "manganame";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_FAVOURITE = "favourite";
	
	
	private static final String DATABASE_CREATE = "create table "+ TABLE_MANGA + "(" 
													+COLUMN_ID  +" integer primary key autoincrement, "
													+COLUMN_MANGANAME + " text not null, "
													+COLUMN_LINK + " text not null, "
													+COLUMN_FAVOURITE + " integer not null);";
	private static final String DATABASE_NAME = "manga.db";
	private static final int DATABASE_VERSION = 1;
	public MangaSQLHelper(Context context ) {
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
		Log.w(MangaSQLHelper.class.getName(), "Upgrading database from version "+ oldVersion + " to " +newVersion
											+", which will destroy all old data "+TABLE_MANGA);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_MANGA);
		onCreate(db);
	}
}
