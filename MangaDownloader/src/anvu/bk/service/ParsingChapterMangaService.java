package anvu.bk.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.htmlcleaner.XPatherException;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import anvu.bk.config.Config;
import anvu.bk.database.connection.ChapterDataSource;
import anvu.bk.internetconnection.HtmlChapterHelper;
import anvu.bk.model.Chapter;

public class ParsingChapterMangaService extends IntentService {
	public static String PARSING_CHAPTER_SERVICE = "Parsing_Chapter_Service";
	public static String RESULT = "result";
	public static String NOTIFICATION = "com.example.service.ParsingChapterMangaService";
	public static final String URL = "URL";
	public static final String MANGA_NAME = "mangaName";
	public static final String POSITION = "position";
	public static final int RESULT_OK = 200;

	public ParsingChapterMangaService() {

		super(PARSING_CHAPTER_SERVICE);
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(Config.TAG_LOG, "In onHandleIntent - ParsingChapterMangaService");
		android.os.Debug.waitForDebugger();
		String urlPath = intent.getStringExtra(URL);
		String mangaName = intent.getStringExtra(MANGA_NAME);
		HtmlChapterHelper chapterHelper;

		try {
			 chapterHelper = new HtmlChapterHelper(urlPath);
//			List<Chapter> chapterList = getAllChapterLink(urlPath);
			 List<Chapter> chapterList = chapterHelper.getAllChapterLink();
			// List<Chapter> chapterList = getAllChapterLink( urlPath);
			ChapterDataSource chapterDataSource = new ChapterDataSource(
					getApplicationContext());
			chapterDataSource.open();
			chapterDataSource.addChapterList(chapterList, mangaName);
			chapterDataSource.close();
			int position = intent.getIntExtra(POSITION, -1);
			if (position == -1)
				System.out
						.println("You have to insert position b4 using parsing chapter service");
			publishResults(Activity.RESULT_OK, position);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
 catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

//	private List<Chapter> getAllChapterLink(String urlPath) throws IOException,
//			SAXException, ParserConfigurationException {
//
//		// URL url = new URL("http://giaitri.vnexpress.net/rss/trong-nuoc.rss");
//		URL url = new URL(urlPath);
//		InputStream i = (InputStream) url.getContent();
//		ReplacerInputStream is = new ReplacerInputStream(i);
//		DocumentBuilder db = DocumentBuilderFactory.newInstance()
//				.newDocumentBuilder();
//		db.parse(is);
//		String myString = IOUtils.toString(is, "UTF-8");
//
//		// System.out.println(myString);
//		String[] stringArray = myString.split("tips");
//		for (String string : stringArray) {
//			System.out.println(string);
//		}
//		System.out.println("wait");
//		return null;
//	}

	private void publishResults(int result, int position) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(POSITION, position);
		intent.putExtra(RESULT, RESULT_OK);
		sendBroadcast(intent);
	}

	/**
	 * @param args
	 */
	public static class ReplacerInputStream extends InputStream {

		private static final byte[] REPLACEMENT = "amp;".getBytes();
		private final byte[] readBuf = new byte[REPLACEMENT.length];
		private final Deque<Byte> backBuf = new ArrayDeque<Byte>();
		private final InputStream in;

		public ReplacerInputStream(InputStream in) {
			this.in = in;
		}

		@Override
		public int read() throws IOException {
			if (!backBuf.isEmpty()) {
				return backBuf.pop();
			}
			int first = in.read();
			if (first == '&') {
				peekAndReplace();
			}
			return first;
		}

		private void peekAndReplace() throws IOException {
			int read = super.read(readBuf, 0, REPLACEMENT.length);
			for (int i1 = read - 1; i1 >= 0; i1--) {
				backBuf.push(readBuf[i1]);
			}
			for (int i = 0; i < REPLACEMENT.length; i++) {
				if (read != REPLACEMENT.length || readBuf[i] != REPLACEMENT[i]) {
					for (int j = REPLACEMENT.length - 1; j >= 0; j--) {
						// In reverse order
						backBuf.push(REPLACEMENT[j]);
					}
					return;
				}
			}
		}
	}

}
