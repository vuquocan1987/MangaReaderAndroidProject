package com.example.stackoverflow.com;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class htmlHelper {
	final static String siteUrl = "http://www.themoscowtimes.com/";
    TagNode rootNode;
    

    public htmlHelper(URL htmlPage) throws IOException
    {

        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(htmlPage);
    }
    public TagNode getFirstHeadNode() throws IOException,XPatherException {
    	TagNode elements[];
    	Object firstsNodes[] = rootNode.evaluateXPath("//");
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
    public static void main(String[] args) {
    	try{
		htmlHelper myHTMLHelper = new htmlHelper(new URL(siteUrl));
    	}
    	catch (IOException e){
    		System.out.println("wrong URL bro");
    	}
	}
}
