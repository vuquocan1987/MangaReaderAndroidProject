package com.example.service;

import java.io.IOException;

import org.htmlcleaner.XPatherException;

import com.example.mangadownloader.Configuration;
import com.example.mangadownloader.Model.HtmlMangaHelper;
import com.example.mangadownloader.Model.MangaDataSource;
import com.example.mangadownloader.Model.htmlHelper;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ParsingService extends IntentService{
public static String PARSING_SERVICE_NAME = "ParsingService";
	public ParsingService() {
		super(PARSING_SERVICE_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		android.os.Debug.waitForDebugger();
		Log.d(Configuration.TAG_LOG,"In onHandleIntent");
		try {
			HtmlMangaHelper htmlHelper = new HtmlMangaHelper();
			MangaDataSource mangaDataSource = new MangaDataSource(getApplicationContext());
			mangaDataSource.addAllManga(htmlHelper.getAllManga(1, 10));
			Log.d(Configuration.TAG_LOG,"Success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(Configuration.TAG_LOG, "Fail Opening new HtmlMangaHelper");
			return;
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(Configuration.TAG_LOG, "Fail Parsing MangaFox Page");
		}
		
	}

	/**
	 * @param args
	 */
	
}
