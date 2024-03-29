/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ir.webcrawlerdef;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class MyCrawler extends WebCrawler {
    
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.contains("unal.edu.co");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();
        
        System.out.println("Docid: " + docid);
        System.out.println("URL: " + url);
        //System.out.println("Domain: '" + domain + "'");
        //System.out.println("Sub-domain: '" + subDomain + "'");
        //System.out.println("Path: '" + path + "'");
        System.out.println("Parent page: " + parentUrl);
        //System.out.println("Anchor text: " + anchor);
        JSONObject jsono = SingletonJSON.getInstance().getObj();
        SingletonJSON.getInstance().setObj(saveURL(jsono, url, parentUrl));
        
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

//                        String text = htmlParseData.getText();
//                        String html = htmlParseData.getHtml();
            List<WebURL> links = htmlParseData.getOutgoingUrls();
            for (WebURL myLink : links) {
                if (shouldVisit(myLink)) {
                    saveURL(jsono, myLink.getURL(), url);
                }
            }

//                        
//                        System.out.println("Text length: " + text.length());
//                        System.out.println("Html length: " + html.length());
//                        System.out.println("Number of outgoing links: " + links.size());
        }
//
//                Header[] responseHeaders = page.getFetchResponseHeaders();
//                if (responseHeaders != null) {
//                        System.out.println("Response headers:");
//                        for (Header header : responseHeaders) {
//                                System.out.println("\t" + header.getName() + ": " + header.getValue());
//                        }
//                }

        System.out.println("=============");
    }
    
    private JSONObject saveURL(JSONObject jsonObject, String urlToSave, String parentURL) {
        if (parentURL != null) {
            List<String> lista = (List<String>) jsonObject.get(parentURL);
            if (!lista.contains(urlToSave)) {
                lista.add(urlToSave);
                jsonObject.put(parentURL, lista);
            }
        }
        if (!jsonObject.containsKey(urlToSave)) {
            jsonObject.put(urlToSave, new ArrayList<String>());
        }
        return jsonObject;
    }
}
