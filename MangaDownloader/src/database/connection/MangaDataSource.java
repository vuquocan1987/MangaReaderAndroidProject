package database.connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.mangadownloader.Model.CustomListener;
import com.example.mangadownloader.Model.Manga;

import config.Config;


import InternetConnection.htmlHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class MangaDataSource {
	private Context context;
	private SQLiteDatabase database;
	private DatabaseMangaSQLHelper dbHelper;

	public MangaDataSource(Context context) {
		dbHelper = new DatabaseMangaSQLHelper(context);
		this.context = context;
		copyDatabase();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addAllManga(List<Manga> mangaList) {
		ContentValues values = new ContentValues();
		for (Manga manga : mangaList) {
			values.put(DatabaseMangaSQLHelper.COLUMN_MANGANAME,
					manga.getMangaName());
			values.put(DatabaseMangaSQLHelper.COLUMN_LINK, manga.getLink());
			values.put(DatabaseMangaSQLHelper.COLUMN_FAVOURITE,
					manga.getFavourite());
			database.insert(DatabaseMangaSQLHelper.TABLE_MANGA, null, values);
			values.clear();
		}
	}

	public Manga createManga(String manga, String link, int favourite) {
		ContentValues values = new ContentValues();
		values.put(DatabaseMangaSQLHelper.COLUMN_MANGANAME, manga);
		values.put(DatabaseMangaSQLHelper.COLUMN_LINK, link);
		values.put(DatabaseMangaSQLHelper.COLUMN_FAVOURITE, favourite);

		long insertID = database.insert(DatabaseMangaSQLHelper.TABLE_MANGA,
				null, values);

		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_MANGA,
				null,
				DatabaseMangaSQLHelper.COLUMN_MANGA_ID + " = " + insertID, null,
				null, null, null);
		cursor.moveToFirst();
		Manga newManga = cursorToManga(cursor);
		cursor.close();
		return newManga;
	}

	public void clearMangaTable() {
		database.delete(DatabaseMangaSQLHelper.TABLE_MANGA, null, null);
	}

	public void deleteManga(Manga manga) {
		long id = manga.get_id();
		System.out.println("Manga deleted with id: " + id);
		database.delete(DatabaseMangaSQLHelper.TABLE_MANGA,
				DatabaseMangaSQLHelper.COLUMN_MANGA_ID + " = " + id, null);
	}

	private Manga cursorToManga(Cursor cursor) {
		Manga manga = new Manga(cursor.getLong(0), cursor.getString(1),
				cursor.getString(2), cursor.getInt(3));
		return manga;
	}

	public void initMangaTable() {
		// TODO Auto-generated method stub
		int numbPage = 1;
		saveOnePage();

	}

	public void saveOnePage() {
		try {
			htmlHelper myHelper = new htmlHelper(context, new CustomListener() {

				@Override
				public void onFinish(List<Manga> topManga) {
					// TODO Auto-generated method stub
					addAllManga(topManga);
				}
			});
			myHelper.execute();
		} catch (MalformedURLException e) {
			Toast.makeText(context, "Network Failure", Toast.LENGTH_SHORT)
					.show();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "Network Failure", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
	}

	public void copyDatabase() {

		String dBpath = context.getFilesDir().getParent() + "/databases";
		File fileDbpath = new File(dBpath);
		Log.d("KaraokeList", dBpath);
		if (!fileDbpath.exists()) {
			Log.d(Config.TAG_LOG,
					"databases dir is not exist, creating db dir");
			fileDbpath.mkdir();
		} else {
			Log.d(Config.TAG_LOG, "databases dir is exists");

		}

		String songDBPath = dBpath + "/" + DatabaseMangaSQLHelper.DATABASE_NAME;
		File songDbFile = new File(songDBPath);
		if (!songDbFile.exists()) {
			Log.d(Config.TAG_LOG,
					"manga.db is not exist, proceed to copy db.");
			try {
				AssetManager asset = context.getAssets();

				InputStream is = asset
						.open(DatabaseMangaSQLHelper.DATABASE_NAME);

				FileOutputStream fos = new FileOutputStream(songDbFile);
				byte[] buffer = new byte[1024];

				int len;

				while ((len = is.read(buffer)) > 0) {
					fos.write(buffer, 0, len);

				}

				is.close();
				fos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Log.d(Config.TAG_LOG, "manga.db is exist.");
		}

	}

	public List<Manga> getAllMangaWKeyword(String newText) {
		List<Manga> mangas = new ArrayList<Manga>();
		String selection = DatabaseMangaSQLHelper.COLUMN_MANGANAME + " = "
				+ newText;
		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_MANGA,
				null, selection, null, null, null, null);
		Manga manga;
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				manga = cursorToManga(cursor);
				mangas.add(manga);
				cursor.moveToNext();
			}
		}
		return null;
	}

	public List<Manga> getAllManga() {
		List<Manga> mangas = new ArrayList<Manga>();
		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_MANGA,
				null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			Manga manga;
			while (!cursor.isAfterLast()) {
				manga = cursorToManga(cursor);
				mangas.add(manga);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return mangas;
	}

	public ArrayList<Manga> getFavouriteManga() {
		ArrayList<Manga> results = new ArrayList<Manga>();
		String selection = DatabaseMangaSQLHelper.COLUMN_FAVOURITE + "= 1";
		Cursor c = database.query(DatabaseMangaSQLHelper.TABLE_MANGA,
				null, selection, null, null, null, null);
		if (c.moveToFirst()) {
			Manga manga;
			while (!c.isAfterLast()) {
				results.add(cursorToManga(c));
				c.moveToNext();
			}
		}
		return results;
	}

	private ContentValues mangaToContentValues(Manga manga) {
		ContentValues values = new ContentValues();
		values.put(DatabaseMangaSQLHelper.COLUMN_MANGANAME,
				manga.getMangaName());
		values.put(DatabaseMangaSQLHelper.COLUMN_LINK, manga.getLink());
		values.put(DatabaseMangaSQLHelper.COLUMN_FAVOURITE,
				manga.getFavourite());
		return values;
	}

	public void upDateMangas(ArrayList<Manga> updatedMangas) {
		// TODO Auto-generated method stub
		for (Manga manga : updatedMangas) {
			database.update(DatabaseMangaSQLHelper.TABLE_MANGA,
					mangaToContentValues(manga),
					DatabaseMangaSQLHelper.COLUMN_MANGA_ID + "= ?",
					new String[] { String.valueOf(manga.get_id()) });
		}

	}

	public Manga getMangaWithId(long mangaId) {
		Manga manga;
		String selection = DatabaseMangaSQLHelper.COLUMN_MANGA_ID + "= ?" ;
		Cursor cursor= database.query(DatabaseMangaSQLHelper.TABLE_MANGA, null, selection, new String[]{String.valueOf(mangaId)}, null, null, null);
		if (cursor.moveToFirst()){
			return cursorToManga(cursor);
		}
		else{
			return null;
		}
	}
}
