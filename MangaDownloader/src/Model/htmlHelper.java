package Model;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class htmlHelper {
	final static String siteUrl = "http://mangafox.me/directory/";
	final static int topMangaNumb = 20;
	String theMangaLink;
    TagNode rootNode;
    int latestPage;

    public htmlHelper(URL htmlPage) throws IOException
    {
        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(htmlPage);
    }
    public TagNode getFirstHeadNode() throws IOException,XPatherException {
    	TagNode elements[];
    	Object firstsNodes[] = rootNode.evaluateXPath("//a[@class='title']");
    	TagNode firstNode = (TagNode)firstsNodes[0]; 
    	return firstNode;
    }
    public TagNode getmangalink() throws IOException,XPatherException {
    	TagNode elements[];
    	Object firstsNodes[] = rootNode.evaluateXPath("//a[@class='tips']");
    	
    	TagNode firstNode = (TagNode)firstsNodes[0];
    	return firstNode;
    }
    public TagNode getMangaPage() throws IOException,XPatherException {
    	TagNode elements[];
    	Object firstsNodes[] = rootNode.evaluateXPath("//img[@id='image']");
    	TagNode firstNode = (TagNode)firstsNodes[0]; 
    	theMangaLink = firstNode.getAttributeByName("src");
    	return firstNode;
    }
    public int latestPage() throws IOException, XPatherException{
   
    	Object pageList[] = rootNode.evaluateXPath("//select[@class='m']/option");
    	latestPage =pageList.length/2-1;
    	return latestPage;
    }
    public List<String> getFileList(){
    	List<String> fileList = new ArrayList<String>();
    	String fullPath = FilenameUtils.getFullPath(theMangaLink);
    	String postFix = theMangaLink.substring(0,theMangaLink.length()-7);
    	for (int i = 1; i <= latestPage; i++) {
			theMangaLink.substring(0, theMangaLink.length()-7);
			String fileSuffix = String.format("%03d", i) + ".jpg";
			System.out.println(postFix+fileSuffix);
			fileList.add(postFix+fileSuffix);
			
		}
    	return fileList;
    }
    public List<String> getChapterList() throws IOException, XPatherException{
    	List<String> chapterList = new ArrayList<String>();
    	Object nodesList[] = rootNode.evaluateXPath("//select[@id='top_chapter_list']/option");
    	
    	TagNode tmp=(TagNode)nodesList[0];
    	System.out.println(tmp.getAttributeByName("style"));
    	System.out.println("you there, bro?" + nodesList.length);
    	for (int i = 0; i < nodesList.length; i++) {
    		tmp = (TagNode)nodesList[i];
    		System.out.println(tmp.getName());
    		chapterList.add(tmp.getText().toString());
    		
		}
    	return chapterList;
    }
    List<TagNode> getLinksByClass(String CSSClassname)
    {
        List<TagNode> linkList = new ArrayList<TagNode>();


        TagNode linkElements[] = rootNode.getElementsByName("a", true);
        for (int i = 0; linkElements != null && i < linkElements.length; i++)
        {

            String classType = linkElements[i].getAttributeByName("class");

            if (classType != null && classType.equals(CSSClassname))
            {
            	linkList.add(linkElements[i]);
            }
        }

        return linkList;
    }
    public static String directedMangaPage = "http://mangafox.me/manga/naruto/vTBD/c650/1.html";
    public static void main(String[] args) throws IOException,XPatherException{
  
//		htmlHelper myHTMLHelper = new htmlHelper(new URL(siteUrl));
//    	TagNode myHead = myHTMLHelper.getFirstHeadNode();
//    	String themangaLink = myHead.getAttributeByName("href");
//    	System.out.println(themangaLink);
//    	
//    	htmlHelper mangalink = new htmlHelper(new URL(themangaLink));
//    	TagNode mylink = mangalink.getmangalink();
//    	String theLink = mylink.getAttributeByName("href");
//    	System.out.println(theLink);
    	
    	htmlHelper mangaPage = new htmlHelper (new URL(directedMangaPage));
    	TagNode imgTag = mangaPage.getMangaPage();
    	String themangaPage = imgTag.getAttributeByName("src");
    	System.out.println(themangaPage);
    	System.out.println(mangaPage.latestPage());
    	mangaPage.getFileList();
    	mangaPage.getChapterList();
	}
	public List<String> get10mangalink() {
		// TODO Auto-generated method stub
		List<String> results = new ArrayList<String>();
		TagNode elements[];
    	Object firstsNodes[];
		try {
			firstsNodes = rootNode.evaluateXPath("//a[@class='title']");
			for (int i = 0; i < 10; i++) {
				TagNode tn = (TagNode)firstsNodes[i];
				results.add(new String(tn.getText()));
			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return results;
	}
}
