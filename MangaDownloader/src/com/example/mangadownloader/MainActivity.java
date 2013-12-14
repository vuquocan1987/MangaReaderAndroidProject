package com.example.mangadownloader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mangadownloader.Model.Chapter;
import com.example.mangadownloader.Model.Manga;
import com.example.service.DownloadChapterService;
import com.example.service.ParsingChapterMangaService;
import com.example.service.ParsingMangaLinkService;

import config.Config;
import database.connection.ChapterDataSource;
import database.connection.DatabaseMangaSQLHelper;
import database.connection.MangaDataSource;
import fragment.DDFragment;
import fragment.DFragment;
import fragment.FFragment;
import fragment.ParseFragment;
import fragment.SFragment;
import fragment.FFragment.OnMangaSelectedListener;

public class MainActivity extends Activity implements OnMangaSelectedListener {
	public MangaDataSource mds;
	public ChapterDataSource cds;

	private BroadcastReceiver parsingMangaReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			handleParseMangaResult(intent.getExtras());
		}

	};
	private BroadcastReceiver downloadServiceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			handleDownloadResult(intent.getExtras());
		}

	};
	
	private void handleDownloadResult(Bundle extras) {
		// TODO VIP !!! Update the downloaded state for file, for now i 
		// just make toast
		Toast.makeText(
				this,
				extras.getLong(DownloadChapterService.CHAPTER_ID, 0)
						+ " finish download", Toast.LENGTH_SHORT).show();
		DFragment df  = (DFragment) getFragmentManager().findFragmentByTag(Config.TAG_FRAG_STORAGE);
		long chapterId = extras.getLong(DownloadChapterService.CHAPTER_ID);
//		cds.updateStatusChapterId(chapterId,Chapter.STATUS_CHAPTER_DOWNLOADED);
		if (df!=null){
			df.updateChapterWithIdDownloaded(chapterId);
			
		}
	}

	private void handleParseMangaResult(Bundle extras) {
		ParseFragment mpf = (ParseFragment) getFragmentManager()
				.findFragmentByTag(Config.TAG_FRAG_PARSE);
		if (mpf != null) {
			mpf.updateParsingPage(extras
					.getInt(ParsingMangaLinkService.CURRENT_NOTIFYING_PAGE));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		A cheat to create chapter table
//		DatabaseMangaSQLHelper dms = new DatabaseMangaSQLHelper(this);
//		dms.getWritableDatabase().execSQL(DatabaseMangaSQLHelper.TABLE_CHAPTER_CREATE);
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		// bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		bar.addTab(bar
				.newTab()
				.setTabListener(
						new MainTabListener<SFragment>(this,
								Config.TAG_FRAG_SEARCH, SFragment.class))
				.setIcon(R.drawable.action_search));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.rating_important)
				.setTabListener(
						new MainTabListener<FFragment>(this,
								Config.TAG_FRAG_FAVOURITE,
								FFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.av_download)
				.setTabListener(
						new MainTabListener<DFragment>(this,
								Config.TAG_FRAG_DOWNLOADING,
								DFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.device_access_sd_storage)
				.setTabListener(
						new MainTabListener<DDFragment>(this,
								Config.TAG_FRAG_STORAGE,
								DDFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.device_access_sd_storage)
				.setTabListener(
						new MainTabListener<ParseFragment>(this,
								Config.TAG_FRAG_PARSE,
								ParseFragment.class)));

		mds = new MangaDataSource(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mds.open();
		registerReceiver(parsingMangaReceiver, new IntentFilter(
				ParsingMangaLinkService.NOTIFICATION));
		registerReceiver(downloadServiceReceiver, new IntentFilter(
				DownloadChapterService.NOTIFICATION_SERVICE));

	}

	@Override
	protected void onPause() {
		super.onPause();
		mds.close();
		unregisterReceiver(parsingMangaReceiver);
		unregisterReceiver(downloadServiceReceiver);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.clear:
			mds.clearMangaTable();
			break;
		// case R.id.init:
		// ds.initMangaTable();
		// break;
		case R.id.dl:
			testService();
			break;
		case R.id.testPCM:
			parseMangaChapter("gang_king",
					"http://mangafox.me/manga/gang_king/");
			Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.init:
			Intent intent = new Intent(this, ParsingMangaLinkService.class);
			startService(intent);
			Log.d(Config.TAG_LOG, "Parsing Service start");
			Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
	}

	private void parseMangaChapter(String mangaName, String mangaURL) {

		Intent intent = new Intent(this, ParsingChapterMangaService.class);
		intent.putExtra(ParsingChapterMangaService.MANGA_NAME, mangaName);
		intent.putExtra(ParsingChapterMangaService.URL, mangaURL);
		startService(intent);
		Log.d(Config.TAG_LOG, "Parsing Chapter Manga Service start");

	}

	private void testService() {
		Intent intent = new Intent(this, DownloadChapterService.class);
		intent.putExtra(DownloadChapterService.MANGA_NAME, "Naruto");
		Log.d(Config.TAG_LOG, "Download Chapter Service start");
		intent.putExtra(DownloadChapterService.URL,
				"http://mangafox.me/manga/naruto/v64/c617/1.html");
		startService(intent);
	}

	private void saveChapter(Chapter chapter) {
		String link;
		if (chapter.getLink() == null) {
			// TODO save to db later*** important ****
		}

	}

	@Override
	public void onSelectManga(Manga manga) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ContentActivity.class);
		intent.putExtra(ContentActivity.MANGA_ID,manga.get_id());
		startActivity(intent);

	}
}
