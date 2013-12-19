package anvu.bk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import anvu.bk.activity.ShowPictureActivity;
import anvu.bk.config.Config;
import anvu.bk.fragment.adapter.ChapterListFragment;
import anvu.bk.model.Chapter;



public class DDFragment extends ChapterListFragment {
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Chapter chapter = chapterList.get(position);
		Intent intent = new Intent (getActivity(),ShowPictureActivity.class);
		intent.putExtra(ShowPictureActivity.CHAPTER_PATH, Config.getPathForChapter(chapter));
		startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initiateDataSource() {
		chapterList = cds.getAllDownloadedChapter();
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
