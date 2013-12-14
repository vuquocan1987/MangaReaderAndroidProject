package com.example.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.htmlcleaner.XPatherException;

import InternetConnection.HtmlHelperPageGetter;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.mangadownloader.Model.Chapter;

import config.Config;
import database.connection.ChapterDataSource;

public class DownloadChapterService extends IntentService {
	public static final String URL = "URL";
	public static final String MANGA_NAME = "mangaName";
	public static final String CHAPTER_NAME = "chapter_name";

	public static final String PAGE_NUMB = "pageNumb";
	public static final String DOWNLOAD_CHAPTER_SERVICE = "DownloadChapterService";
	public static final String CHAPTER_ID = "_id";
	private static final String DOWNLOAD_CHAPTER_SERVICE_NOTIFICATION = "download_chapter_service_notification";
	private static final String RESULT = "result";

	public DownloadChapterService(Chapter chapter) {
		super(DOWNLOAD_CHAPTER_SERVICE);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public DownloadChapterService() {

		super("DownloadChapter");

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		android.os.Debug.waitForDebugger();
		Log.d(Config.TAG_LOG, "DownLoadServiceStart");
		ChapterDataSource cds = new ChapterDataSource(getApplicationContext());
		cds.open();
		long chapterId = intent.getLongExtra(CHAPTER_ID, 0);
		Chapter chapter = cds.getChapterWithId(chapterId);
		String urlPath = chapter.getChapterLink();
		String mangaName = chapter.getMangaName();
		String chapterName = chapter.getChapterName();
		
		String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/data/manga/"
				+ mangaName
				+ "/"
				+ chapterName.replace(' ', '_');
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		Log.d(Config.TAG_LOG, filePath);
		HtmlHelperPageGetter getter;
		try {
			getter = new HtmlHelperPageGetter(new URL(urlPath));
			List<String> bitmapPageLists = getter.getFileList();
			Log.d(Config.TAG_LOG,
					"Inside onHandle Intent DownloadChapterService");
			String currentFileName;
			for (int i = 0; i < bitmapPageLists.size(); i++) {
				currentFileName = String.format("%03d", i) + ".png";
				File file = new File(filePath, currentFileName);
				Log.d(Config.TAG_LOG, file.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(file);
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				getBitmapFromURL(bitmapPageLists.get(i)).compress(
						Bitmap.CompressFormat.PNG, 100, bytes);
				fos.write(bytes.toByteArray());
				fos.close();
			}
			// this bit of code is a bit clumsy but I don't know how to update
			// the database otherwise
			// because the activity could be already closed at this point!!! So
			// there's no way to notify
			// the application to update database -_-....
			cds.updateStatusChapterId(chapterId,
					Chapter.STATUS_CHAPTER_DOWNLOADED);
			cds.close();
			publishResults(Activity.RESULT_OK, chapterId);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void publishResults(int result, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(DOWNLOAD_CHAPTER_SERVICE_NOTIFICATION);
		intent.putExtra(CHAPTER_ID, id);
		intent.putExtra(RESULT, result);
		sendBroadcast(intent);
	}

	public static Bitmap getBitmapFromURL(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);

			connection.connect();

			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(Config.TAG_LOG, e.getMessage().toString());
			return null;
		}
	}
}
