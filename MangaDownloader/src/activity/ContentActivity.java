package activity;

import fragment.ContentFragment;
import an.vu.mangadownloader.R;
import android.app.Activity;
import android.os.Bundle;

public class ContentActivity extends Activity{
	public static final String MANGA_ID = "manga_id";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chapter_list_activity);
		Bundle extras = getIntent().getExtras();
		if (extras!=null){
			long mangaId = extras.getLong(MANGA_ID);
			ContentFragment cf = (ContentFragment) getFragmentManager().findFragmentById(R.id.content_chapter_frag);
			cf.updateContent(mangaId);
		}
	}
}
