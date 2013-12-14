package database.connection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.mangadownloader.Model.Chapter;
import com.example.mangadownloader.Model.Manga;

import config.Config;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class ChapterDataSource {
	private Context context;
	private SQLiteDatabase database;
	private DatabaseMangaSQLHelper dbHelper;
	private String[] allColumns = { DatabaseMangaSQLHelper.COLUMN_MANGANAME,
			DatabaseMangaSQLHelper.COLUMN_CHAPTER_NO,
			DatabaseMangaSQLHelper.COLUMN_CHAPTER_LINK };

	public ChapterDataSource(Context context) {
		dbHelper = new DatabaseMangaSQLHelper(context);
		this.context = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addChapterList(List<Chapter> chapterList, String mangaName) {
		ContentValues values = new ContentValues();
		for (Chapter chapter : chapterList) {
			values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_LINK,
					chapter.getChapterLink());
			values.put(DatabaseMangaSQLHelper.COLUMN_MANGANAME, mangaName);
			values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_NO,
					chapter.getChapterNo());
			values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_NAME,
					chapter.getChapterName());
			database.insert(DatabaseMangaSQLHelper.TABLE_CHAPTER, null, values);
			values.clear();
		}
	}

	public Chapter createManga(Chapter chapter) {
		ContentValues values = new ContentValues();
		values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_LINK,
				chapter.getChapterLink());
		values.put(DatabaseMangaSQLHelper.COLUMN_MANGANAME,
				chapter.getMangaName());
		values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_NO,
				chapter.getChapterNo());

		// long insertID = database.insert(ChapterSQLHelper.TABLE_CHAPTER, null,
		// values);

		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_CHAPTER,
				allColumns, DatabaseMangaSQLHelper.COLUMN_CHAPTER_LINK + " = "
						+ chapter.getChapterLink(), null, null, null, null);
		cursor.moveToFirst();
		Chapter newChapter = cursorToChapter(cursor);
		cursor.close();
		return newChapter;
	}

	// public void clearMangaTable(){
	// database.delete(MangaSQLHelper.TABLE_MANGA, null, null);
	// }
	public void deleteChapter(Chapter chapter) {
		String link = chapter.getChapterLink();
		System.out.println("Manga deleted with link: " + link);
		database.delete(DatabaseMangaSQLHelper.TABLE_CHAPTER,
				DatabaseMangaSQLHelper.COLUMN_CHAPTER_LINK + " = " + link, null);
	}

	public List<Chapter> getAllChapter() {
		List<Chapter> chapters = new ArrayList<Chapter>();
		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_CHAPTER,
				allColumns, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			Chapter chapter;
			while (!cursor.isAfterLast()) {
				chapter = cursorToChapter(cursor);
				chapters.add(chapter);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return chapters;
	}

	public List<Chapter> getAllChapterForManga(Manga manga) {
		// TODO Auto-generated method stub
		ArrayList<Chapter> chapterList = new ArrayList<Chapter>();
		String selection = DatabaseMangaSQLHelper.COLUMN_MANGANAME + "= '"
				+ manga.getMangaName() + "'";
		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_CHAPTER,
				null, selection, null, null, null, null);
		return cursorToChapterList(cursor);
	}

	private Chapter cursorToChapter(Cursor cursor) {

		Chapter chapter = new Chapter(
				cursor.getLong(cursor
						.getColumnIndex(DatabaseMangaSQLHelper.COLUMN_CHAPTER_ID)),
				cursor.getString(cursor
						.getColumnIndex(DatabaseMangaSQLHelper.COLUMN_MANGANAME)),
				cursor.getInt(cursor
						.getColumnIndex(DatabaseMangaSQLHelper.COLUMN_CHAPTER_NO)),
				cursor.getString(cursor
						.getColumnIndex(DatabaseMangaSQLHelper.COLUMN_CHAPTER_LINK)),
				cursor.getString(cursor
						.getColumnIndex(DatabaseMangaSQLHelper.COLUMN_CHAPTER_NAME)));
		return chapter;
	}

	private List<Chapter> cursorToChapterList(Cursor cursor) {
		List<Chapter> chapterList = new ArrayList<Chapter>();
		if (cursor.moveToFirst()) {
			Chapter chapter;
			while (!cursor.isAfterLast()) {
				chapter = cursorToChapter(cursor);
				chapterList.add(chapter);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return chapterList;
	}

	public List<Chapter> getAllChapterNotDownloaded() {
		String selection = DatabaseMangaSQLHelper.COLUMN_CHAPTER_STATUS
				+ " <> " + Chapter.STATUS_CHAPTER_NOTDOWNLOAD;
		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_CHAPTER,
				null, selection, null, null, null, null);
		return cursorToChapterList(cursor);
	}

	public void updateStatusChapterId(long chapterId,
			int statusChapterDownloaded) {
		ContentValues values = new ContentValues();
		values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_STATUS,
				statusChapterDownloaded);

		String whereClause = DatabaseMangaSQLHelper.COLUMN_CHAPTER_ID + " = ?";
		long id = database.update(DatabaseMangaSQLHelper.TABLE_CHAPTER, values,
				whereClause, new String[] { String.valueOf(chapterId) });
		 System.out.println("id of chapter just updated status" + id);
		 Log.d(Config.TAG_LOG, "id of chapter just updated status: " + id);
	}

	public Chapter getChapterWithId(long chapterId) {
		
		String selection = DatabaseMangaSQLHelper.COLUMN_CHAPTER_ID + " = ?";
		Cursor cursor = database.query(DatabaseMangaSQLHelper.TABLE_CHAPTER, null, selection,
				new String[] { String.valueOf(chapterId) }, null, null, null);
		if (cursor.moveToFirst()){
			return cursorToChapter(cursor);
		}
		return null;

	}

	public void updateStatusChapterListNoOverWrite(
			List<Chapter> updatedChapterList) {
		// this method will update status of a chapter with status <
		// Downloading.
		for (Chapter chapter : updatedChapterList) {
			updateStatusChapterNoOverWrite(chapter);
		}
	}

	private void updateStatusChapterNoOverWrite(Chapter chapter) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DatabaseMangaSQLHelper.COLUMN_CHAPTER_STATUS,
				chapter.getStatus());

		String whereClause = DatabaseMangaSQLHelper.COLUMN_CHAPTER_ID + " = ?"
				+ " AND " + DatabaseMangaSQLHelper.COLUMN_CHAPTER_STATUS + " <> " + Chapter.STATUS_CHAPTER_DOWNLOADED;
		long id = database.update(DatabaseMangaSQLHelper.TABLE_CHAPTER, values,
				whereClause, new String[] { String.valueOf(chapter.get_id()) });
		System.out.println("id of chapter just updated status" + id);
		 Log.d(Config.TAG_LOG, "id of chapter just updated status" + id);

	}
}
