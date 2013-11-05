package com.example.mangadownloader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import com.example.mangadownloader.SFragment;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		bar.setDisplayShowTitleEnabled(false);
//		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		bar.addTab(bar.newTab().setText("Search").setTabListener(new MainTabListener<SFragment>(this, Configuration.TAG_FRAG_SEARCH,SFragment.class)));
		bar.addTab(bar.newTab().setText("Favourite").setTabListener(new MainTabListener<FFragment>(this, Configuration.TAG_FRAG_FAVOURITE,FFragment.class)));
		bar.addTab(bar.newTab().setText("Downloading").setTabListener(new MainTabListener<DFragment>(this, Configuration.TAG_FRAG_DOWNLOADING,DFragment.class)));
		bar.addTab(bar.newTab().setText("Download").setTabListener(new MainTabListener<DDFragment>(this, Configuration.TAG_FRAG_DOWNLOAD,DDFragment.class)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
