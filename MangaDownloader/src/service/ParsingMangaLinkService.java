package service;

import internetconnection.HtmlMangaHelper;

import java.io.IOException;

import org.htmlcleaner.XPatherException;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import config.Config;
import database.connection.MangaDataSource;

public class ParsingMangaLinkService extends IntentService {
	public static String PARSING_SERVICE_NAME = "ParsingService";
	public static String SERVICE_PREFERENCE = "Service_Preference";
	public static String CURRENT_PAGE = "current_page";
	public static String CURRENT_NOTIFYING_PAGE = "current_notify_page";
	public static String RESULT = "result";
	public static int PARSE_STEP = 1;
	public static String NOTIFICATION = "com.example.service.ParsingService";

	public ParsingMangaLinkService() {

		super(PARSING_SERVICE_NAME);
		// TODO Auto-generated constructor stub

	}

	public int getCurrentPage() {
		SharedPreferences ms = getApplicationContext().getSharedPreferences(
				SERVICE_PREFERENCE, MODE_PRIVATE);
		currentPage = ms.getInt(CURRENT_PAGE, 0);

		if (currentPage == 0) {
			setCurrentPage(1);
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		SharedPreferences ms = getApplicationContext().getSharedPreferences(
				SERVICE_PREFERENCE, MODE_PRIVATE);
		Editor myEditor = ms.edit().putInt(CURRENT_PAGE, currentPage);
		myEditor.commit();
		this.currentPage = currentPage;
	}

	int currentPage;

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		// android.os.Debug.waitForDebugger();
		Log.d(Config.TAG_LOG, "In onHandleIntent");
		try {

			HtmlMangaHelper htmlHelper = new HtmlMangaHelper();
			MangaDataSource mangaDataSource = new MangaDataSource(
					getApplicationContext());
			mangaDataSource.open();
			while (getCurrentPage() + PARSE_STEP < HtmlMangaHelper.NUMBER_PAGE_MANGA) {
				mangaDataSource.addAllManga(htmlHelper.getAllManga(
						getCurrentPage(), PARSE_STEP));
				setCurrentPage(currentPage + PARSE_STEP);
				publishResults(Activity.RESULT_OK);
			}
			mangaDataSource.close();
			Log.d(Config.TAG_LOG, "Success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(Config.TAG_LOG, "Fail Opening new HtmlMangaHelper");
			return;
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(Config.TAG_LOG, "Fail Parsing MangaFox Page");
		}

	}

	private void publishResults(int result) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(RESULT, result);
		intent.putExtra(CURRENT_NOTIFYING_PAGE, currentPage);
		sendBroadcast(intent);
	}

	/**
	 * @param args
	 */

}
