package anvu.bk.internetconnection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class HtmlHelperPageGetter {
	TagNode rootNode;
	int latestPage;
	public HtmlHelperPageGetter(URL htmlPage) throws IOException
    {
        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(htmlPage);
    }
	public String getFirstPageChapter() throws IOException,XPatherException {
    	String bitmapPageChapter;
    	Object firstsNodes[] = rootNode.evaluateXPath("//img[@id='image']");
    	TagNode firstNode = (TagNode)firstsNodes[0]; 
    	bitmapPageChapter = firstNode.getAttributeByName("src");
    	return bitmapPageChapter;
    }
    public int latestPage() throws IOException, XPatherException{
    	Object pageList[] = rootNode.evaluateXPath("//select[@class='m']/option");
    	latestPage =pageList.length/2-1;
    	return latestPage;
    }
    public List<String> getFileList() throws IOException, XPatherException {
    	latestPage();
    	List<String> fileList = new ArrayList<String>();
    	String firstPageBitmap = getFirstPageChapter();
    	String postFix = firstPageBitmap.substring(0,firstPageBitmap.length()-7);
    	for (int i = 1; i <= latestPage; i++) {
    		firstPageBitmap.substring(0, firstPageBitmap.length()-7);
			String fileSuffix = String.format("%03d", i) + ".jpg";
			System.out.println(postFix+fileSuffix);
			fileList.add(postFix+fileSuffix);
		}
    	return fileList;
    }
}
