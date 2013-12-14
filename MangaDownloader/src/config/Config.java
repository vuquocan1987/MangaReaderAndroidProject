package config;

import com.example.mangadownloader.R;
import com.example.mangadownloader.Model.Chapter;
import com.example.mangadownloader.R.drawable;

public class Config {
	public static final String TAG_FRAG_SEARCH = "frag_search";
	public static final String TAG_FRAG_FAVOURITE = "frag_favourite";
	public static final String TAG_FRAG_DOWNLOADING = "frag_downloading" ;
	public static final String TAG_FRAG_STORAGE = "frag_storage";
	public static final String TAG_FRAG_PARSE = "frag_parse";
	public static final String MANGA_FOX_SITE = "http://mangafox.me/directory/";
	public static final String TAG_LOG = "MangaDownloader";
	public static int  getResourceForStatus(int status) {
		// TODO Auto-generated method stub
		
		switch (status) {
		case Chapter.STATUS_CHAPTER_DOWNLOADING:
			return R.drawable.av_download;
			
		case Chapter.STATUS_CHAPTER_DOWNLOADED:
			
			return R.drawable.device_access_sd_storage; 
			
		case Chapter.STATUS_CHAPTER_STOPPED:
			return R.drawable.av_pause;
		case Chapter.STATUS_CHAPTER_NOTDOWNLOAD:
			return R.drawable.collections_cloud;
		}
		return 0;
	}
}
