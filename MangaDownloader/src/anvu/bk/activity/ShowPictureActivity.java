package anvu.bk.activity;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import anvu.bk.fragment.ShowPicturePagerFragment;
import anvu.bk.mangadownloader.R;

public class ShowPictureActivity extends FragmentActivity {
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
//	private int mNumPage;
	String chapter_path;
	private File[] fileList;;
	public static final String CHAPTER_PATH = "chapter_path";
	@Override
	protected void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		chapter_path= getIntent().getExtras().getString(CHAPTER_PATH);
		setContentView(R.layout.show_picture_activity);
		File file = new File(chapter_path);
		fileList = file.listFiles();
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ShowPicturePagerAdapter (getSupportFragmentManager(),chapter_path);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new SimpleOnPageChangeListener(){
			
			@Override
			public void onPageSelected(int position) {
				invalidateOptionsMenu();
			}
		});
		
	}
	
	private class ShowPicturePagerAdapter extends FragmentStatePagerAdapter{
		//private String chapterPath;
		public ShowPicturePagerAdapter (FragmentManager fm,String chapterPath)
		{
			super(fm);
//			this.chapterPath = chapterPath;
		}

		@Override
		public Fragment getItem(int position) {
			return ShowPicturePagerFragment.create(fileList[position].getAbsolutePath());
		}

		@Override
		public int getCount() {
			return fileList.length;
		}
		
	}

}
