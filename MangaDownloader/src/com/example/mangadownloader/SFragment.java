package com.example.mangadownloader;

import java.util.ArrayList;
import java.util.List;

import Model.Manga;
import Model.MangaDataSource;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SFragment extends Fragment implements OnClickListener{
	private MangaDataSource ds;
	ArrayAdapter<String> adapter;
	TextView searchQuery;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ds = ((MainActivity)getActivity()).ds;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.search_fragment,container,false);
		ImageButton searchButton = (ImageButton) v.findViewById(R.id.btnSearch);
		ListView mangaListView = (ListView) v.findViewById(R.id.lvResult);
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		mangaListView.setAdapter(adapter);
		searchButton.setOnClickListener(this);
		searchQuery = (TextView) v.findViewById(R.id.txtSearch);
		return v;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
		adapter.clear();
		List<Manga> lm = ds.getAllManga();
		List<Manga> mResults = new ArrayList<Manga>();
		
		List<String> results = new ArrayList<String>();
		
		String txtQuery = searchQuery.getText().toString().toLowerCase();
		for (Manga manga : lm) {
			
			if (manga.getMangaName().toLowerCase().contains(txtQuery)){
				mResults.add(manga);
			}
		}
		for (Manga manga : mResults) {
			adapter.add(manga.getLink());
		}
		
		
	}
}
