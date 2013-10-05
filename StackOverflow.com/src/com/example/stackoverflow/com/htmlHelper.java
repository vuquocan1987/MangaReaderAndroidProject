package com.example.stackoverflow.com;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class htmlHelper {
    TagNode rootNode;


    public htmlHelper(URL htmlPage) throws IOException
    {

        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(htmlPage);
    }
    public TagNode[] getFirstHeadNode (){
    	TagNode elements[];
    	try{
    	elements = rootNode.evaluateXPath("");
    	return elements;
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	
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
}
