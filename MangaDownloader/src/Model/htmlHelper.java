package Model;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class htmlHelper extends AsyncTask<Void,Integer,List<Manga>>{
	
	final static String siteUrl = "http://mangafox.me/directory/";
	final static int topMangaNumb = 20;
	String theMangaLink;
    TagNode rootNode;
    int latestPage;
    Context c;
    CustomListener cl;
    public htmlHelper(URL htmlPage) throws IOException
    {
        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(htmlPage);
    }
    public htmlHelper(Context c, CustomListener cl) throws MalformedURLException, IOException{
    	this.cl = cl;
    	this.c = c;
    }
    public htmlHelper() throws MalformedURLException, IOException{
    	HtmlCleaner cleaner = new HtmlCleaner();
    	rootNode = cleaner.clean(new URL(siteUrl));
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
	@Override
	protected void onPreExecute() {
		Toast.makeText(c, "start parsing", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected List<Manga> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		List<Manga> results = new ArrayList<Manga>();
		List<String> mNames = new ArrayList<String>();
		List<String> mLinks = new ArrayList<String>();
		HtmlCleaner cleaner = new HtmlCleaner();
		try {
			String tmpsiteUrl;
			for (int i = 1; i < 10; i++) {
				tmpsiteUrl = siteUrl+ "/" + i + ".htm";
				rootNode = cleaner.clean(new URL(tmpsiteUrl));
				TagNode elements[];
		    	Object firstsNodes[];
					firstsNodes = rootNode.evaluateXPath("//a[@class='title']");
					for (int j = 0; j < firstsNodes.length; j++) {
						TagNode tn = (TagNode)firstsNodes[j];
						mNames.add(new String(tn.getText()));
						mLinks.add(tn.getAttributeByName("href"));
						Manga tManga = new Manga(new String(tn.getText()), tn.getAttributeByName("href"));
						results.add(tManga);
					}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	@Override
	protected void onPostExecute(List<Manga> result) {
		super.onPostExecute(result); 
		cl.onFinish(result);
		Toast.makeText(c, "done", Toast.LENGTH_SHORT).show();
	}
}
