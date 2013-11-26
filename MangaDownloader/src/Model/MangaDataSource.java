package Model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class MangaDataSource {
	private Context context;
	private SQLiteDatabase database;
	private MangaSQLHelper dbHelper;
	private String[] allColumns = {MangaSQLHelper.COLUMN_ID,MangaSQLHelper.COLUMN_MANGANAME,
			MangaSQLHelper.COLUMN_LINK,MangaSQLHelper.COLUMN_FAVOURITE};
	public MangaDataSource(Context context){
		dbHelper = new MangaSQLHelper(context);
		this.context = context;
	}
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	public void close (){
		dbHelper.close();
	}
	public void createMangaList (List<Manga> mangaList){
		ContentValues values = new ContentValues();
		for (Manga manga : mangaList) {
			values.put(MangaSQLHelper.COLUMN_MANGANAME,manga.getMangaName());
			values.put(MangaSQLHelper.COLUMN_LINK, manga.getLink());
			values.put(MangaSQLHelper.COLUMN_FAVOURITE, manga.getFavourite());
			database.insert(MangaSQLHelper.TABLE_MANGA, null, values);
			values.clear();
		}
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
	public void clearMangaTable(){
		database.delete(MangaSQLHelper.TABLE_MANGA, null, null);
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
		cursor.close();
		return mangas;
	}
	private Manga cursorToManga(Cursor cursor) {
		Manga manga = new Manga(cursor.getLong(0), cursor.getString(1), cursor.getString(2),cursor.getInt(3));
		return manga;
	}
	public void initMangaTable() {
		// TODO Auto-generated method stub
		int numbPage = 1;
		saveOnePage();
		
	}
	public void saveOnePage(){
		try {
			htmlHelper myHelper = new htmlHelper(context, new CustomListener() {
				
				@Override
				public void onFinish(List<Manga> topManga) {
					// TODO Auto-generated method stub
					createMangaList(topManga);
				}
			});
			myHelper.execute();
		} catch (MalformedURLException e) {
			Toast.makeText(context, "Network Failure", Toast.LENGTH_SHORT).show();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "Network Failure", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
