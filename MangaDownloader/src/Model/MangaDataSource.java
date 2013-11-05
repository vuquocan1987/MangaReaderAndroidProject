package Model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MangaDataSource {
	private SQLiteDatabase database;
	private MangaSQLHelper dbHelper;
	private String[] allColumns = {MangaSQLHelper.COLUMN_ID,MangaSQLHelper.COLUMN_MANGANAME,
			MangaSQLHelper.COLUMN_LINK,MangaSQLHelper.COLUMN_FAVOURITE};
	public MangaDataSource(Context context){
		dbHelper = new MangaSQLHelper(context);
	}
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	public void close (){
		dbHelper.close();
	}
	public Manga createManga (String manga,String link,int favourite){
		ContentValues values = new ContentValues();
		values.put(MangaSQLHelper.COLUMN_MANGANAME,manga );
		values.put(MangaSQLHelper.COLUMN_LINK,link );
		values.put(MangaSQLHelper.COLUMN_FAVOURITE,favourite );
		
		long insertID = database.insert(MangaSQLHelper.TABLE_MANGA, null, values);
		
		Cursor cursor = database.query(MangaSQLHelper.TABLE_MANGA, allColumns, MangaSQLHelper.COLUMN_ID+ " = "+insertID,
				null, null, null, null);
		cursor.moveToFirst();
		Manga newManga = cursorToManga(cursor);
		cursor.close();
		return newManga;
	}
	public void deleteManga(Manga manga){
		long id = manga.get_id();
		System.out.println("Manga deleted with id: "+id);
		database.delete(MangaSQLHelper.TABLE_MANGA, MangaSQLHelper.COLUMN_ID + " = " + id,null);
	}
	public List<Manga> getAllManga(){
		List<Manga> mangas = new ArrayList<Manga>();
		Cursor cursor = database.query(MangaSQLHelper.TABLE_MANGA, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()){
			Manga manga = cursorToManga(cursor);
			mangas.add(manga);
			cursor.moveToNext();
		}
		return mangas;
	}
	private Manga cursorToManga(Cursor cursor) {
		Manga manga = new Manga(cursor.getLong(0), cursor.getString(1), cursor.getString(2),cursor.getInt(3));
		return manga;
	}
}
