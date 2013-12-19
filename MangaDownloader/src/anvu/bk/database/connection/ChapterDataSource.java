package anvu.bk.database.connection;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import anvu.bk.config.Config;
import anvu.bk.model.Chapter;
import anvu.bk.model.Manga;

public class ChapterDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_MANGANAME,
			DatabaseHelper.COLUMN_CHAPTER_NO,
			DatabaseHelper.COLUMN_CHAPTER_LINK };

	public ChapterDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
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
			values.put(DatabaseHelper.COLUMN_CHAPTER_LINK,
					chapter.getChapterLink());
			values.put(DatabaseHelper.COLUMN_MANGANAME, mangaName);
			values.put(DatabaseHelper.COLUMN_CHAPTER_NO,
					chapter.getChapterNo());
			values.put(DatabaseHelper.COLUMN_CHAPTER_NAME,
					chapter.getChapterName());
			values.put(DatabaseHelper.COLUMN_CHAPTER_STATUS, chapter.getStatus());
			database.insert(DatabaseHelper.TABLE_CHAPTER, null, values);
			values.clear();
		}
	}

	public Chapter createManga(Chapter chapter) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_CHAPTER_LINK,
				chapter.getChapterLink());
		values.put(DatabaseHelper.COLUMN_MANGANAME,
				chapter.getMangaName());
		values.put(DatabaseHelper.COLUMN_CHAPTER_NO,
				chapter.getChapterNo());

		// long insertID = database.insert(ChapterSQLHelper.TABLE_CHAPTER, null,
		// values);

		Cursor cursor = database.query(DatabaseHelper.TABLE_CHAPTER,
				allColumns, DatabaseHelper.COLUMN_CHAPTER_LINK + " = "
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
		database.delete(DatabaseHelper.TABLE_CHAPTER,
				DatabaseHelper.COLUMN_CHAPTER_LINK + " = " + link, null);
	}

	public List<Chapter> getAllChapter() {
		List<Chapter> chapters = new ArrayList<Chapter>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_CHAPTER,
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
		String selection = DatabaseHelper.COLUMN_MANGANAME + "= '"
				+ manga.getMangaName() + "'";
		Cursor cursor = database.query(DatabaseHelper.TABLE_CHAPTER,
				null, selection, null, null, null, null);
		return cursorToChapterList(cursor);
	}

	private Chapter cursorToChapter(Cursor cursor) {

		Chapter chapter = new Chapter(
				cursor.getLong(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_ID)),
				cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_MANGANAME)),
				cursor.getInt(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_NO)),
				cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_LINK)),
				cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_NAME)),
				cursor.getInt(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_STATUS)));
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
		String selection = DatabaseHelper.COLUMN_CHAPTER_STATUS
				+ " <> " + Chapter.STATUS_CHAPTER_NOTDOWNLOAD;
		Cursor cursor = database.query(DatabaseHelper.TABLE_CHAPTER,
				null, selection, null, null, null, null);
		return cursorToChapterList(cursor);
	}

	public void updateStatusChapterId(long chapterId,
			int statusChapterDownloaded) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_CHAPTER_STATUS,
				statusChapterDownloaded);

		String whereClause = DatabaseHelper.COLUMN_CHAPTER_ID + " = ?";
		long id = database.update(DatabaseHelper.TABLE_CHAPTER, values,
				whereClause, new String[] { String.valueOf(chapterId) });
		System.out.println("id of chapter just updated status" + id);
		Log.d(Config.TAG_LOG, "id of chapter just updated status: " + id);
	}

	public Chapter getChapterWithId(long chapterId) {

		String selection = DatabaseHelper.COLUMN_CHAPTER_ID + " = ?";
		Cursor cursor = database.query(DatabaseHelper.TABLE_CHAPTER,
				null, selection, new String[] { String.valueOf(chapterId) },
				null, null, null);
		if (cursor.moveToFirst()) {
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
		values.put(DatabaseHelper.COLUMN_CHAPTER_STATUS,
				chapter.getStatus());

		String whereClause = DatabaseHelper.COLUMN_CHAPTER_ID + " = ?"
				+ " AND " + DatabaseHelper.COLUMN_CHAPTER_STATUS
				+ " <> " + Chapter.STATUS_CHAPTER_DOWNLOADED;
		int numberAffectedRows = database.update(
				DatabaseHelper.TABLE_CHAPTER, values, whereClause,
				new String[] { String.valueOf(chapter.get_id()) });
		System.out.println("number of rows with status change"
				+ numberAffectedRows);
		Log.d(Config.TAG_LOG, "number of rows with status change"
				+ numberAffectedRows);

	}

	public List<Chapter> getAllDownloadedChapter() {
		// TODO Auto-generated method stub
		String selection = DatabaseHelper.COLUMN_CHAPTER_STATUS + " = "
				+ Chapter.STATUS_CHAPTER_DOWNLOADED;
		Cursor cursor = database.query(DatabaseHelper.TABLE_CHAPTER,
				null, selection, null, null, null, null);
		return cursorToChapterList(cursor);
	}
}
