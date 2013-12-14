package InternetConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.example.mangadownloader.Model.Chapter;

import android.content.Context;

public class HtmlChapterHelper extends htmlHelper {
	public static String MANGA_FOX_PAGE = "http://mangafox.me/directory/";
	public static String XPATH_CHAPTER_NAME = "//a[@class='tips']";
	public int currentPageNo;
	public String currentPage;

	public HtmlChapterHelper(String mangaURL) throws IOException {

		super(new URL(mangaURL));
		// TODO Auto-generated constructor stub
	}

	public List<Chapter> getAllChapterLink()
			throws MalformedURLException, IOException, XPatherException {

		List<Chapter> results = new ArrayList<Chapter>();
		Object nodes[] = rootNode.evaluateXPath(XPATH_CHAPTER_NAME);
		Chapter c;
		for (int i = 0; i < nodes.length; i++) {
			TagNode tmp = (TagNode) nodes[i];
			c =  new Chapter(i+1,tmp.getAttributeByName("href"),tmp.getText().toString());
			results.add(c);
		}
		return results;
	}
}
