package com.example.mangadownloader.Model;

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

public class ChapterDataSource {
	private Context context;
	private SQLiteDatabase database;
	private MangaSQLHelper dbHelper;
	private String[] allColumns = {ChapterSQLHelper.COLUMN_MANGANAME,ChapterSQLHelper.COLUMN_CHAPTER_NO,
			ChapterSQLHelper.COLUMN_CHAPTER_LINK};
	public ChapterDataSource(Context context){
		dbHelper = new MangaSQLHelper(context);
		this.context = context;
	}
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	public void close (){
		dbHelper.close();
	}
	public void createChapterList (List<Chapter> chapterList){
		ContentValues values = new ContentValues();
		for (Chapter chapter : chapterList) {
			values.put(ChapterSQLHelper.COLUMN_CHAPTER_LINK,chapter.getChapterLink());
			values.put(ChapterSQLHelper.COLUMN_MANGANAME, chapter.getMangaName());
			values.put(ChapterSQLHelper.COLUMN_CHAPTER_NO, chapter.getChapterNo());
			database.insert(ChapterSQLHelper.TABLE_CHAPTER, null, values);
			values.clear();
		}
	}
	public Chapter createManga (Chapter chapter){
		ContentValues values = new ContentValues();
		values.put(ChapterSQLHelper.COLUMN_CHAPTER_LINK,chapter.getChapterLink());
		values.put(ChapterSQLHelper.COLUMN_MANGANAME, chapter.getMangaName());
		values.put(ChapterSQLHelper.COLUMN_CHAPTER_NO, chapter.getChapterNo());
		
//		long insertID = database.insert(ChapterSQLHelper.TABLE_CHAPTER, null, values);
		
		Cursor cursor = database.query(ChapterSQLHelper.TABLE_CHAPTER, allColumns, ChapterSQLHelper.COLUMN_CHAPTER_LINK+ " = "
		+chapter.getChapterLink(), null, null, null, null);
		cursor.moveToFirst();
		Chapter newChapter = cursorToChapter(cursor);
		cursor.close();
		return newChapter;
	}
//	public void clearMangaTable(){
//		database.delete(MangaSQLHelper.TABLE_MANGA, null, null);
//	}
	public void deleteChapter(Chapter chapter){
		String link = chapter.getChapterLink();
		System.out.println("Manga deleted with link: "+link);
		database.delete(ChapterSQLHelper.TABLE_CHAPTER, ChapterSQLHelper.COLUMN_CHAPTER_LINK + " = " + link,null);
	}
	public List<Chapter> getAllChapter(){
		List<Chapter> chapters = new ArrayList<Chapter>();
		Cursor cursor = database.query(ChapterSQLHelper.TABLE_CHAPTER, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Chapter chapter = cursorToChapter(cursor);
			chapters.add(chapter);
			cursor.moveToNext();
		}
		cursor.close();
		return chapters;
	}
	private Chapter cursorToChapter(Cursor cursor) {
		Chapter chapter = new Chapter(cursor.getString(0), cursor.getInt(1), cursor.getString(2));
		return chapter;
	}
	
}
