package anvu.bk.fragment.adapter;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import anvu.bk.config.Config;
import anvu.bk.model.Chapter;

import anvu.bk.mangadownloader.R;


public class ChapterAdapter extends ArrayAdapter<Chapter> {
	Context c;
	int res;
	public List<Chapter> chapterList;

	public ChapterAdapter(Context context, int resource,
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
		ImageView imvChapterStatus = (ImageView) v
				.findViewById(R.id.ivChapterStatus);
		tvChapter.setText(chapter.getChapterName());
		imvChapterStatus.setImageResource(Config
				.getResourceForStatus(chapter.getStatus()));
		return v;
	}
}