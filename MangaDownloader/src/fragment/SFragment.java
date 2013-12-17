package fragment;

import java.util.ArrayList;
import java.util.List;

import model.Manga;

import an.vu.mangadownloader.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;


import database.connection.MangaDataSource;

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
		mangaListView.setAdapter(new MangaAdapter(getActivity(), R.layout.search_listview_layout,new ArrayList<Manga> ()));
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
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mds.open();
		mangaList = mds.getAllManga();
		MangaAdapter adapter = (MangaAdapter) mangaListView.getAdapter();
		adapter.clear();
		
		
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		
		super.onDetach();
		
		
	}
	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.length()<2) return true;
		newText = newText.toLowerCase();
		ArrayList<Manga> results= new ArrayList<Manga>();
		for (Manga manga : mangaList) {
			if (manga.getMangaName().toLowerCase().contains(newText))
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
