package config;

import model.Chapter;
import an.vu.mangadownloader.R;
import android.util.Log;


public class Config {
	public static final String TAG_FRAG_SEARCH = "frag_search";
	public static final String TAG_FRAG_FAVOURITE = "frag_favourite";
	public static final String TAG_FRAG_DOWNLOADING = "frag_downloading";
	public static final String TAG_FRAG_STORAGE = "frag_storage";
	public static final String TAG_FRAG_PARSE = "frag_parse";
	public static final String MANGA_FOX_SITE = "http://mangafox.me/directory/";
	public static final String TAG_LOG = "MangaDownloader";

	public static String chapterMangaLocation;

	public static void setchapterMangaRootLocation(String location) {
		chapterMangaLocation = location + "/data/manga";
	}

	public static int getResourceForStatus(int status) {
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

	public static String getPathForChapter(Chapter chapter) {
		String path = chapterMangaLocation + "/" + chapter.getMangaName().trim() + "/"
				+ chapter.getChapterName().trim();
		Log.d(TAG_LOG, path);
		return path;
	}

	public static String getPathForPage(String chapter_path, int position) {
		return chapter_path+"/"+String.format("%03d", position)+".png";
	}

}
