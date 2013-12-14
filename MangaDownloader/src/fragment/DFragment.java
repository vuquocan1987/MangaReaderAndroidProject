package fragment;

import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mangadownloader.R;
import com.example.mangadownloader.Model.Chapter;
import com.example.mangadownloader.R.id;
import com.example.mangadownloader.R.layout;

import config.Config;
import database.connection.ChapterDataSource;

public class DFragment extends ListFragment {
	List<Chapter> chapterList ;
	ChapterDataSource cds;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cds = new ChapterDataSource(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cds.open();
		chapterList = cds.getAllChapterNotDownloaded();
		setListAdapter(new ChapterAdapterDFragment(getActivity(), R.layout.download_fragment_row_layout, chapterList));
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cds.close();
	}
	public class ChapterAdapterDFragment extends ArrayAdapter<Chapter>{
		Context c;
		int res;
		List<Chapter> chapterList;
		public ChapterAdapterDFragment(Context context, int resource,
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
			final Chapter chapter = chapterList.get(position);
			v = View.inflate(c, res, null);
			TextView tvChapter = (TextView) v.findViewById(R.id.tvChapterName);
			ImageView imvChapterStatus = (ImageView) v.findViewById(R.id.imvChapterStatus);
			tvChapter.setText(chapter.getChapterName());
			imvChapterStatus.setImageResource( Config.getResourceForStatus(chapter.getStatus()));
			return v;
		}
	}

	public void updateChapterWithIdDownloaded(long chapterId) {
		// I can definitely search the chapter with chapterId in O(1) using hashmap...
		Chapter chapter ;
		Chapter replaceChapter = cds.getChapterWithId(chapterId);
		for (int i = 0; i < chapterList.size(); i++) {
			chapter = chapterList.get(i);
			if (chapter.get_id() == chapterId){
				chapter.setStatus(replaceChapter.getStatus());
			}
		}
		((ArrayAdapter<Chapter>)getListAdapter()).notifyDataSetChanged();
	}
}
