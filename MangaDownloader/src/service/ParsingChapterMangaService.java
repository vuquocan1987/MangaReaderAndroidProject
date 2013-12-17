package service;

import internetconnection.HtmlChapterHelper;
import internetconnection.HtmlMangaHelper;

import java.io.IOException;
import java.util.List;

import model.Chapter;

import org.htmlcleaner.XPatherException;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import config.Config;
import database.connection.ChapterDataSource;
import database.connection.MangaDataSource;

public class ParsingChapterMangaService extends IntentService {
	public static String PARSING_CHAPTER_SERVICE = "Parsing_Chapter_Service";
	public static String RESULT = "result";
	public static String NOTIFICATION = "com.example.service.ParsingChapterMangaService";
	public static final String URL = "URL";
	public static final String MANGA_NAME = "mangaName";

	public ParsingChapterMangaService() {

		super(PARSING_CHAPTER_SERVICE);
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(Config.TAG_LOG,
				"In onHandleIntent - ParsingChapterMangaService");
		android.os.Debug.waitForDebugger();
		String urlPath = intent.getStringExtra(URL);
		String mangaName = intent.getStringExtra(MANGA_NAME);
		HtmlChapterHelper chapterHelper;

		try {
			chapterHelper = new HtmlChapterHelper(urlPath);
			List<Chapter> chapterList = chapterHelper.getAllChapterLink();
			ChapterDataSource chapterDataSource = new ChapterDataSource(
					getApplicationContext());
			chapterDataSource.open();
			chapterDataSource.addChapterList(chapterList, mangaName);
			chapterDataSource.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void publishResults(int result) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(NOTIFICATION);
		sendBroadcast(intent);
	}

	/**
	 * @param args
	 */

}
