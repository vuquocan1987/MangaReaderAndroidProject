package com.example.mangadownloader;

import com.example.service.ParsingService;

import java.util.ArrayList;
import java.util.List;

import com.example.mangadownloader.Controller.DownloadChapterService;

import Model.Manga;
import Model.MangaDataSource;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
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
			mpf.UpdateParsingPage(extras.getInt(ParsingService.CURRENT_NOTIFYING_PAGE));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		registerReceiver(receiver, new IntentFilter(ParsingService.NOTIFICATION));

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ds.close();
		unregisterReceiver(receiver);
	}

	public List<Manga> initiateDummyManga() {
		List<Manga> results = new ArrayList<Manga>();
		for (int i = 0; i < 3; i++) {
			results.add(new Manga(mangaList[i], mangaLinks[i]));
		}
		return results;
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
		case R.id.add:
			ds.createMangaList(initiateDummyManga());
			break;
		case R.id.load:
			// List<Manga> lm = ds.getAllManga();
			break;
		case R.id.clear:
			ds.clearMangaTable();
			break;
		// case R.id.init:
		// ds.initMangaTable();
		// break;
		case R.id.dl:
			testService();
		case R.id.init:
			Intent intent = new Intent(this, ParsingService.class);
			startService(intent);
			Log.d(Configuration.TAG_LOG, "Service start");
			Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
	}

	private void testService() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, DownloadChapterService.class);
		intent.putExtra(DownloadChapterService.MANGA_NAME, "Naruto");
		intent.putExtra(DownloadChapterService.URL,
				"http://mangafox.me/manga/naruto/v64/c617/1.html");
		startService(intent);
	}
}
