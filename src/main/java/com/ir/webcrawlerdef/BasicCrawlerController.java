/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ir.webcrawlerdef;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawlerController {

        public static void main(String[] args) throws Exception {
                if (args.length != 2) {
                        System.out.println("Needed parameters: ");
                        System.out.println("\t rootFolder (it will contain intermediate crawl data)");
                        System.out.println("\t numberOfCralwers (number of concurrent threads)");
                        return;
                }

                /*
                 * crawlStorageFolder is a folder where intermediate crawl data is
                 * stored.
                 */
                String crawlStorageFolder = args[0];

                /*
                 * numberOfCrawlers shows the number of concurrent threads that should
                 * be initiated for crawling.
                 */
                int numberOfCrawlers = Integer.parseInt(args[1]);

                CrawlConfig config = new CrawlConfig();

                config.setCrawlStorageFolder(crawlStorageFolder);

                /*
                 * Be polite: Make sure that we don't send more than 1 request per
                 * second (1000 milliseconds between requests).
                 */
                //config.setPolitenessDelay(1000);

                /*
                 * You can set the maximum crawl depth here. The default value is -1 for
                 * unlimited depth
                 */
                //config.setMaxDepthOfCrawling(2);

                /*
                 * You can set the maximum number of pages to crawl. The default value
                 * is -1 for unlimited number of pages
                 */
                //config.setMaxPagesToFetch(1000);
                
//                config.setProxyHost("proxyapp.unal.edu.co");
//                config.setProxyPort(8080);
//                config.setProxyUsername("wsmorar");
//                config.setProxyPassword("milianito2");
                /*
                 * Do you need to set a proxy? If so, you can use:
                 * config.setProxyHost("proxyserver.example.com");
                 * config.setProxyPort(8080);
                 * 
                 * If your proxy also needs authentication:
                 * config.setProxyUsername(username); config.getProxyPassword(password);
                 */

                /*
                 * This config parameter can be used to set your crawl to be resumable
                 * (meaning that you can resume the crawl from a previously
                 * interrupted/crashed crawl). Note: if you enable resuming feature and
                 * want to start a fresh crawl, you need to delete the contents of
                 * rootFolder manually.
                 */
                config.setResumableCrawling(false);

                /*
                 * Instantiate the controller for this crawl.
                 */
                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

                /*
                 * For each crawl, you need to add some seed urls. These are the first
                 * URLs that are fetched and then the crawler starts following links
                 * which are found in these pages
                 */

                controller.addSeed("http://www.unal.edu.co/");
                controller.addSeed("http://sinab.unal.edu.co/");
                controller.addSeed("http://www.agenciadenoticias.unal.edu.co/");
                controller.addSeed("http://disi.unal.edu.co/");
                controller.addSeed("http://www.admisiones.unal.edu.co/");
                controller.addSeed("http://www.agronomia.unal.edu.co/web/");
                controller.addSeed("http://www.ing.unal.edu.co/");
                controller.addSeed("http://www.humanas.unal.edu.co/");

                /*
                 * Start the crawl. This is a blocking operation, meaning that your code
                 * will reach the line after this only when crawling is finished.
                 */
                controller.startNonBlocking(MyCrawler.class, numberOfCrawlers);
                
                Thread.sleep(200 * 1000);

                // Send the shutdown request and then wait for finishing
                controller.shutdown();
                controller.waitUntilFinish();
                //System.out.println(SingletonJSON.getInstance().getObj().toString());
                try {
 
		FileWriter file = new FileWriter("C:\\Users\\USUARIO\\IR\\test.json");
		file.write(SingletonJSON.getInstance().getObj().toJSONString());
		file.flush();
		file.close();
 
	} catch (IOException e) {
		e.printStackTrace();
	}
        }
}
