package anvu.bk.fragment.adapter;

import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import anvu.bk.database.connection.ChapterDataSource;
import anvu.bk.mangadownloader.R;
import anvu.bk.model.Chapter;


public class ChapterListFragment extends ListFragment {
	protected List<Chapter> chapterList;
	protected ChapterDataSource cds;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cds = new ChapterDataSource(getActivity());
		cds.open();
		initiateDataSource();
		setListAdapter(new ChapterAdapter(getActivity(),
				R.layout.chapter_row_layout, chapterList));
	}

	public void initiateDataSource() {
		chapterList = cds.getAllChapterNotDownloaded();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cds.open();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cds.close();
	}

	}
