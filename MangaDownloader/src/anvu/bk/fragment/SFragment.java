package anvu.bk.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anvu.bk.mangadownloader.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import anvu.bk.database.connection.MangaDataSource;
import anvu.bk.fragment.adapter.MangaAdapter;
import anvu.bk.model.Manga;

public class SFragment extends Fragment implements OnQueryTextListener {
	private MangaDataSource mds;
	TextView searchQuery;
	SearchView searchView;
	List<Manga> mangaList;
	ListView mangaListView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mds = new MangaDataSource(getActivity());
		mds.open();
		mangaList = mds.getAllManga();
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.search_fragment, container, false);
		mangaListView = (ListView) v.findViewById(R.id.lvResult);
		//initiate dummy adapter to prevent crash on first launch
		final MangaAdapter adapter = new MangaAdapter(getActivity(), R.layout.search_listview_layout,new ArrayList<Manga> ());
		mangaListView.setAdapter(adapter);
		mangaListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) {
				View v = parent.getChildAt(position);
				v.findViewById(R.id.cbFavourite_slv).performClick();
			}
		});
		searchView = (SearchView) v.findViewById(R.id.searchView);
		searchView.setOnQueryTextListener(this);
		return v;
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MangaAdapter mAdapter =(MangaAdapter) mangaListView.getAdapter();
		mds.upDateMangas(mAdapter.updatedMangas);
		mds.close();
		mAdapter.updatedMangas.clear();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mds.open();
	}
	@Override
	public boolean onQueryTextChange(String queryString) {
		if (queryString.length()<2) return true;
		queryString = queryString.toLowerCase(Locale.getDefault());
		ArrayList<Manga> results= new ArrayList<Manga>();
		for (Manga manga : mangaList) {
			if (manga.getMangaName().toLowerCase(Locale.getDefault()).contains(queryString))
			results.add(manga);
		}
		MangaAdapter mangaAdapter = new MangaAdapter(getActivity(), R.layout.search_listview_layout, results);
		mangaListView.setAdapter(mangaAdapter);
		
		return true;
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		
		return false;
	}
	
}
