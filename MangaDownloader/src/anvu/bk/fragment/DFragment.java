package anvu.bk.fragment;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import anvu.bk.fragment.adapter.ChapterListFragment;
import anvu.bk.mangadownloader.R;
import anvu.bk.model.Chapter;


public class DFragment extends ChapterListFragment {
	
	@SuppressWarnings("unchecked")
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

	public void updatePercentageChapterId(long chapterId, int percentage) {
		
		ListView list = getListView();
		int start = list.getFirstVisiblePosition();
		for(int i=start, j=list.getLastVisiblePosition();i<=j;i++)
		    if(chapterId==((Chapter)list.getItemAtPosition(i)).get_id()){
		        View view = list.getChildAt(i-start);
		        TextView tv = (TextView) view.findViewById(R.id.tvPercentage);
		        tv.setVisibility(View.VISIBLE);
		        tv.setText(String.valueOf(percentage));
		        list.getAdapter().getView(i, view, list);
		        break;
		    }

	}

	
}
