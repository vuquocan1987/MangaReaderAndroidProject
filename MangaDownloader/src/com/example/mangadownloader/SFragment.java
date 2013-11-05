package com.example.mangadownloader;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class SFragment extends Fragment implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.search_fragment,container,false);
		ImageButton searchButton = (ImageButton) v.findViewById(R.id.btnSearch);
		ListView mangaListView = (ListView) v.findViewById(R.id.lvResult);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		mangaListView.setAdapter(adapter);
		searchButton.setOnClickListener(this);
		return v;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
	}
}
