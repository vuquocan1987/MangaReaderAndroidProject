package com.example.mangadownloader;

import com.example.service.ParsingService;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

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
								Configuration.TAG_FRAG_SEARCH, SFragment.class)).setIcon(R.drawable.action_search));
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
		Tab t = bar.newTab();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.init:
			Intent intent = new Intent(this,ParsingService.class);
			startService(intent);
			Log.d(Configuration.TAG_LOG, "Service start");
			Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
