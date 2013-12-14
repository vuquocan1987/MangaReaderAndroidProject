package InternetConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.example.mangadownloader.Model.Manga;

import android.content.Context;

public class HtmlMangaHelper extends htmlHelper {
	public static String MANGA_FOX_PAGE = "http://mangafox.me/directory/";
	public static int NUMBER_PAGE_MANGA = 270;
	public static String XPATH_MANGA_NAME = "//a[@class='title']";
	public int currentPageNo;
	public String currentPage;
	
	public HtmlMangaHelper() throws IOException {
		
		super(new URL(MANGA_FOX_PAGE));
		// TODO Auto-generated constructor stub
	}
	
	public List<Manga> getAllManga(int currentPage,int noPage) throws MalformedURLException, IOException, XPatherException{
		
		List<Manga> results = new ArrayList<Manga>();
		for (int i = currentPage; i < currentPage+noPage; i++) {
			resetRootNodeTo(MANGA_FOX_PAGE+i+".htm");
			Object nodes [] = rootNode.evaluateXPath(XPATH_MANGA_NAME);
			for (int j = 0; j < nodes.length; j++) {
				TagNode tmp = (TagNode) nodes[j];
				results.add(new Manga(tmp.getText().toString(),tmp.getAttributeByName("href")));
			}
		}
		
		return results;
	}
}
