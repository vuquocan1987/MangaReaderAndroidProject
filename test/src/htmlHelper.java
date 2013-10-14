import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class htmlHelper {
	final static String siteUrl = "http://mangafox.me/directory/";
	final static 
	
    TagNode rootNode;
    

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
    	return firstNode;
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
    public static List<String> getPageList(String firstPage){
    	
    }
    public static String binarySearch latestPage(String firstPage){
    	
    }
    public static void main(String[] args) throws IOException,XPatherException{
  
		htmlHelper myHTMLHelper = new htmlHelper(new URL(siteUrl));
    	TagNode myHead = myHTMLHelper.getFirstHeadNode();
    	String themangaLink = myHead.getAttributeByName("href");
    	System.out.println(themangaLink);
    	
    	htmlHelper mangalink = new htmlHelper(new URL(themangaLink));
    	TagNode mylink = mangalink.getmangalink();
    	String theLink = mylink.getAttributeByName("href");
    	System.out.println(theLink);
    	
    	htmlHelper mangaPage = new htmlHelper (new URL(theLink));
    	TagNode imgTag = mangaPage.getMangaPage();
    	String themangaPage = imgTag.getAttributeByName("src");
    	System.out.println(themangaPage);
    	
    	List<String> myPageList= getPageList(themangaPage);
	}
}
