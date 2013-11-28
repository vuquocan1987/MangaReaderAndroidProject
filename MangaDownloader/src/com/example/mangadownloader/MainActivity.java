package com.example.mangadownloader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mangadownloader.Model.DatabaseMangaSQLHelper;
import com.example.mangadownloader.Model.MangaDataSource;
import com.example.service.DownloadChapterService;
import com.example.service.ParsingChapterMangaService;
import com.example.service.ParsingMangaLinkService;

public class MainActivity extends Activity {
	public MangaDataSource ds;
	public static String[] mangaList = { "Naruto", "Fairy Tail", "Noblesse" };
	public static String[] mangaLinks = { "lNaruto", "lFairy Tail", "lNoblesse" };
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			handleResult(intent.getExtras());
		}

	};

	private void handleResult(Bundle extras) {
		// TODO Auto-generated method stub
		ParseFragment mpf =(ParseFragment) getFragmentManager().findFragmentByTag(Configuration.TAG_FRAG_PARSE);
		if (mpf!=null){
			mpf.UpdateParsingPage(extras.getInt(ParsingMangaLinkService.CURRENT_NOTIFYING_PAGE));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((new DatabaseMangaSQLHelper(this)).getWritableDatabase()).execSQL(DatabaseMangaSQLHelper.TABLE_CHAPTER_CREATE); 
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		// bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		bar.addTab(bar
				.newTab()
				.setTabListener(
						new MainTabListener<SFragment>(this,
								Configuration.TAG_FRAG_SEARCH, SFragment.class))
				.setIcon(R.drawable.action_search));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.rating_important)
				.setTabListener(
						new MainTabListener<FFragment>(this,
								Configuration.TAG_FRAG_FAVOURITE,
								FFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.av_download)
				.setTabListener(
						new MainTabListener<DFragment>(this,
								Configuration.TAG_FRAG_DOWNLOADING,
								DFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.device_access_sd_storage)
				.setTabListener(
						new MainTabListener<DDFragment>(this,
								Configuration.TAG_FRAG_DOWNLOAD,
								DDFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.device_access_sd_storage)
				.setTabListener(
						new MainTabListener<ParseFragment>(this,
								Configuration.TAG_FRAG_PARSE,
								ParseFragment.class)));
		ds = new MangaDataSource(this);
		

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ds.open();
		registerReceiver(receiver, new IntentFilter(ParsingMangaLinkService.NOTIFICATION));

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ds.close();
		unregisterReceiver(receiver);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.clear:
			ds.clearMangaTable();
			break;
		// case R.id.init:
		// ds.initMangaTable();
		// break;
		case R.id.dl:
			testService();
			break;
		case R.id.testPCM:
			parseMangaChapter("gang_king", "http://mangafox.me/manga/gang_king/" );
			Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.init:
			Intent intent = new Intent(this, ParsingMangaLinkService.class);
			startService(intent);
			Log.d(Configuration.TAG_LOG, "Parsing Service start");
			Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
	}
	private void parseMangaChapter(String mangaName,String mangaURL){
		
		Intent intent = new Intent(this, ParsingChapterMangaService.class);
		intent.putExtra(ParsingChapterMangaService.MANGA_NAME, mangaName);
		intent.putExtra(ParsingChapterMangaService.URL, mangaURL);
		startService(intent);
		Log.d(Configuration.TAG_LOG, "Parsing Chapter Manga Service start");
		
	}
	private void testService() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, DownloadChapterService.class);
		intent.putExtra(DownloadChapterService.MANGA_NAME, "Naruto");
		Log.d(Configuration.TAG_LOG, "Download Chapter Service start");
		intent.putExtra(DownloadChapterService.URL,
				"http://mangafox.me/manga/naruto/v64/c617/1.html");
		startService(intent);
	}
}
