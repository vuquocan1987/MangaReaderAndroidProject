package anvu.bk.fragment;

import java.util.ArrayList;
import java.util.List;



import anvu.bk.mangadownloader.R;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import anvu.bk.config.Config;
import anvu.bk.database.connection.ChapterDataSource;
import anvu.bk.database.connection.MangaDataSource;
import anvu.bk.model.Chapter;
import anvu.bk.model.Manga;
import anvu.bk.service.DownloadChapterService;


public class ContentFragment extends ListFragment {
	public static final String TAG = null;
	Manga manga;
	List<Chapter> chapterList;
	List<Chapter> updatedChapterList = new ArrayList<Chapter>();
	ChapterDataSource cds;

	@SuppressWarnings("unchecked")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Chapter chapter = chapterList.get(position);
		if (chapter.getStatus() == Chapter.STATUS_CHAPTER_DOWNLOADED) {
			Toast.makeText(
					getActivity(),
					"chapter + " + chapter.getChapterName()
							+ " is already downloaded", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Intent intent = new Intent(getActivity(), DownloadChapterService.class);
		intent.putExtra(DownloadChapterService.CHAPTER_ID, chapter.get_id());
		getActivity().startService(intent);
		chapter.setStatus(Chapter.STATUS_CHAPTER_DOWNLOADING);
		((ArrayAdapter<Chapter>) getListAdapter()).notifyDataSetChanged();
		updatedChapterList.add(chapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cds = new ChapterDataSource(getActivity());
		cds.open();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updatedChapterList.clear();
		cds.open();
		if (manga == null)
			return;
		updateContent(manga.get_id());
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cds.updateStatusChapterListNoOverWrite(updatedChapterList);
		cds.close();

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		cds.close();
	}

	public class ChapterListAdapter extends ArrayAdapter<Chapter> {
		int res;
		List<Chapter> chapterList;
		Context c;

		public ChapterListAdapter(Context context, int resource,
				List<Chapter> chapterList) {

			super(context, resource, chapterList);
			this.c = context;
			this.res = resource;
			this.chapterList = chapterList;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = null;
			Chapter chapter = chapterList.get(position);
			v = View.inflate(c, res, null);
			TextView tvChapterName = (TextView) v
					.findViewById(R.id.tvChapterName);
			tvChapterName.setText(chapter.getChapterName());
			ImageView iv = (ImageView) v.findViewById(R.id.ivChapterStatus);
			iv.setImageResource(Config.getResourceForStatus(chapter.getStatus()));
			return v;
		}

	}

	public void updateContent(long mangaId) {
		// TODO Auto-generated method stub
		MangaDataSource mds = new MangaDataSource(getActivity());
		mds.open();
		manga = mds.getMangaWithId(mangaId);
		mds.close();
		chapterList = cds.getAllChapterForManga(manga);
		ChapterListAdapter cda = new ChapterListAdapter(getActivity(),
				R.layout.chapter_row_layout, chapterList);
		setListAdapter(cda);
	}
}
