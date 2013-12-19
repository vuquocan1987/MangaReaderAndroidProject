package anvu.bk.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import anvu.bk.config.Config;
import anvu.bk.database.connection.ChapterDataSource;
import anvu.bk.database.connection.MangaDataSource;
import anvu.bk.fragment.DDFragment;
import anvu.bk.fragment.DFragment;
import anvu.bk.fragment.FFragment;
import anvu.bk.fragment.FFragment.OnMangaSelectedListener;
import anvu.bk.fragment.ParseFragment;
import anvu.bk.fragment.SFragment;
import anvu.bk.mangadownloader.R;
import anvu.bk.model.Manga;
import anvu.bk.service.DownloadChapterService;
import anvu.bk.service.ParsingChapterMangaService;
import anvu.bk.service.ParsingMangaLinkService;

public class MainActivity extends Activity implements OnMangaSelectedListener {
	public MangaDataSource mds;
	public ChapterDataSource cds;


	private BroadcastReceiver parsingMangaChapterReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			handleParseChapterMangaResult(intent.getExtras());

		}

	};
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

	private void handleParseChapterMangaResult(Bundle extras) {
		FFragment ff = (FFragment) getFragmentManager().findFragmentByTag(
				Config.TAG_FRAG_FAVOURITE);
		System.out.println("waiter");
		if (ff != null) {
			if (extras.getInt(ParsingChapterMangaService.RESULT, -1) == ParsingChapterMangaService.RESULT_OK) {
				int position = extras
						.getInt(ParsingChapterMangaService.POSITION);
				ff.updatePositionFinishParsing(position);
			}
		}
	}

	private void handleDownloadResult(Bundle extras) {

		DFragment df = (DFragment) getFragmentManager().findFragmentByTag(
				Config.TAG_FRAG_STORAGE);
		if (df == null)
			return;
		long chapterId = extras.getLong(DownloadChapterService.CHAPTER_ID);
		switch (extras.getInt(DownloadChapterService.RESULT)) {
		case Activity.RESULT_OK:
			Toast.makeText(
					this,
					extras.getLong(DownloadChapterService.CHAPTER_ID, 0)
							+ " finish download", Toast.LENGTH_SHORT).show();

			df.updateChapterWithIdDownloaded(chapterId);

			break;
		case DownloadChapterService.RESULT_DOWNLOADING:
			int percentage = extras.getInt(DownloadChapterService.PERCENTAGE);
			df.updatePercentageChapterId(chapterId, percentage);
			break;
		default:
			break;
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

	private void initiateFilesLocation() {
		Config.setchapterMangaRootLocation(getExternalFilesDir(
				Environment.DIRECTORY_PICTURES).getAbsolutePath());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initiateFilesLocation();
		// A cheat to create chapter table
		// DatabaseMangaSQLHelper dms = new DatabaseMangaSQLHelper(this);
		// dms.getWritableDatabase().execSQL(DatabaseMangaSQLHelper.TABLE_CHAPTER_CREATE);
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
								Config.TAG_FRAG_FAVOURITE, FFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.av_download)
				.setTabListener(
						new MainTabListener<DFragment>(this,
								Config.TAG_FRAG_DOWNLOADING, DFragment.class)));
		bar.addTab(bar
				.newTab()
				.setIcon(R.drawable.device_access_sd_storage)
				.setTabListener(
						new MainTabListener<DDFragment>(this,
								Config.TAG_FRAG_STORAGE, DDFragment.class)));
		// bar.addTab(bar
		// .newTab()
		// .setIcon(R.drawable.device_access_sd_storage)
		// .setTabListener(
		// new MainTabListener<ParseFragment>(this,
		// Config.TAG_FRAG_PARSE,
		// ParseFragment.class)));

		mds = new MangaDataSource(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mds.open();

		registerReceiver(downloadServiceReceiver, new IntentFilter(
				DownloadChapterService.NOTIFICATION));

		registerReceiver(parsingMangaReceiver, new IntentFilter(
				ParsingMangaLinkService.NOTIFICATION));

		registerReceiver(parsingMangaChapterReceiver, new IntentFilter(
				ParsingChapterMangaService.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		mds.close();
		unregisterReceiver(parsingMangaReceiver);
		unregisterReceiver(downloadServiceReceiver);
		unregisterReceiver(parsingMangaChapterReceiver);
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
		case R.id.action_settings:
		
		default:
			break;
		}
		return true;
	}

	@Override
	public void onSelectManga(Manga manga) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ContentActivity.class);
		intent.putExtra(ContentActivity.MANGA_ID, manga.get_id());
		startActivity(intent);

	}
}
