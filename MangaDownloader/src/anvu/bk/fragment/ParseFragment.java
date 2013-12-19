package anvu.bk.fragment;

import anvu.bk.mangadownloader.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ParseFragment extends Fragment {
	TextView txtUpdatePage = null;
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.parse_fragment,container,false);
		txtUpdatePage = (TextView) v.findViewById(R.id.updateParsingPage);
		
		return v;
	}
	public void updateParsingPage (int page){
		txtUpdatePage.setText(String.valueOf(page));
	}
}
