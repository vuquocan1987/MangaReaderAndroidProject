package anvu.bk.fragment.adapter;
import java.util.ArrayList;


import anvu.bk.mangadownloader.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import anvu.bk.model.Manga;




public class MangaAdapter extends ArrayAdapter<Manga>{
		public ArrayList<Manga> updatedMangas=new ArrayList<Manga>();
		int layout;
		ArrayList<Manga> mangaList;
		public MangaAdapter(Context context, int layout,
				ArrayList<Manga> mangaList) {
			super(context, layout, mangaList);
			this.layout= layout;
			this.mangaList = mangaList;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v;
			v = View.inflate(getContext(), layout, null);
			TextView tvMangaName = (TextView) v.findViewById(R.id.tvMangaName_slv);
			CheckBox cbFavourite = (CheckBox) v.findViewById(R.id.cbFavourite_slv);
			final Manga currentManga = mangaList.get(position);
			cbFavourite.setChecked((currentManga.getFavourite()==0)?false:true);
			cbFavourite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					updatedMangas.add(currentManga);
					currentManga.setFavourite(isChecked?Manga.FAVOURITE:Manga.NOT_FAVOURITE);
				}
			});
			tvMangaName.setText(currentManga.getMangaName());
			return v;
		}
	}