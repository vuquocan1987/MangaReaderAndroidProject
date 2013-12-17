package fragment;

import java.util.ArrayList;
import java.util.List;

import service.ParsingChapterMangaService;

import model.Manga;

import an.vu.mangadownloader.R;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import database.connection.MangaDataSource;

public class FFragment extends ListFragment {
	MangaDataSource mds;
	private OnMangaSelectedListener mangaSelectedListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mds = new MangaDataSource(getActivity());

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mangaSelectedListener = (OnMangaSelectedListener) activity;
		} catch (ClassCastException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Manga manga = (Manga) l.getItemAtPosition(position);
		mangaSelectedListener.onSelectManga(manga);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mds.open();
		ArrayList<Manga> favouriteManga = mds.getFavouriteManga();
		setListAdapter(new MasterMangaAdapter(getActivity(),
				R.layout.master_manga_row_layout, favouriteManga));

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(getActivity(), "onpause", Toast.LENGTH_LONG).show();
		MasterMangaAdapter mAdapter = (MasterMangaAdapter) getListAdapter();
		// mds.upDateMangas(mAdapter.updatedMangas);
		mds.close();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater
				.inflate(R.layout.favourite_fragment, container, false);

		return v;
	}

	public interface OnMangaSelectedListener {
		public void onSelectManga(Manga manga);
	}

	public class MasterMangaAdapter extends ArrayAdapter<Manga> {
		Context c;
		int res;
		List<Manga> mangaList;

		public MasterMangaAdapter(Context context, int resource,
				List<Manga> mangaList) {
			super(context, resource, mangaList);
			this.c = context;
			this.res = resource;
			this.mangaList = mangaList;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = null;

			v = View.inflate(c, res, null);
			final Manga manga = mangaList.get(position);
			ImageButton btnRefresh = (ImageButton) v
					.findViewById(R.id.btnRefresh);
			TextView tvMangaName = (TextView) v
					.findViewById(R.id.tvMangaName_masterManga);
			tvMangaName.setText(manga.getMangaName());
			btnRefresh.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// call service to parse the chapter of the manga;

					Intent intent = new Intent(getActivity(),
							ParsingChapterMangaService.class);
					intent.putExtra(ParsingChapterMangaService.URL,
							manga.getLink());
					intent.putExtra(ParsingChapterMangaService.MANGA_NAME,
							manga.getMangaName());
					c.startService(intent);
				}
			});
			btnRefresh.setFocusable(false);

			return v;
		}
	}
}
