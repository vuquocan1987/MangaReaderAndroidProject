package fragment;

import model.Chapter;
import android.widget.ArrayAdapter;


public class DFragment extends ChapterListFragment {
	
	public void updateChapterWithIdDownloaded(long chapterId) {
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
