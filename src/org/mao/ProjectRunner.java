package org.mao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.mao.db.DBopration.DBoperation;
import org.mao.db.dao.ItemHistoryDao;
import org.mao.db.dao.ItemInfoDao;
import org.mao.db.dao.UrlHistoryDao;
import org.mao.db.dao.UrlQueueDao;
import org.mao.util.FileUtil;
import org.mao.util.Log;
import org.mao.util.StringUtil;
import org.mao.util.UrlFilter;

/**
 * The entrance of project. <br/>
 * Initial program and start to run.
 * 
 * @author MAO
 * 
 */
public class ProjectRunner {

	// Link queue waiting to crawl one by one.
	private static List<Map<String, String>> queue;

	// Base information from configure file.
	private static String feedUrl = "";
	private static String startFromFeedFlag = "";
	private static String dbConnection = "";
	private static String dbId = "";
	private static String dbPw = "";

	/**
	 * <b>Project entrance. </b><br/>
	 * 
	 * 1. Initial link queue. <br/>
	 * 2. Start multithread crawling the links in queue.<br/>
	 * 3. Finish when queue is cleared.<br/>
	 * <br/>
	 * PS. Used wide-first crawling the web.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// load configure informations.
		getConfigInfo();
		UrlFilter.getFilterConfig();

		// Check the configure parameters.
		if (StringUtil.isEmpty(feedUrl)) {
			Log.error("Can not find feed URL in config file, program will exit.");
			return;
		}
		if (StringUtil.isEmpty(dbConnection)) {
			Log.error("Can not find database setting information in config file, program will exit.");
			return;
		}
		if (StringUtil.isEmpty(UrlFilter.domain)) {
			Log.error("Can not find domain in url_filter_config file, program will exit.");
			return;
		}

		// Initial queue.
		queue = new ArrayList<Map<String, String>>();
		// Initial database connection.
		DBoperation.dbOpration = new DBoperation(dbConnection, dbId, dbPw);

		// set start point: feed.
		long count = UrlQueueDao.getCount();
		if (count == 0 || "true".equals(startFromFeedFlag)) {
			setFeedToQueue();
		}

		Log.log("Program start.");

		// first run regularly to fetch the links filling the queue.
		// Because there is no links in queue at beginning.
		String url = getUrlFromQueue();
		Crawler firstCrawler = new Crawler(url);
		firstCrawler.run();

		// start multitheard fetching the pages.
		long i = 0;
		url = getUrlFromQueue();
		while (url != null) {
			try {
				Crawler crawler = new Crawler(url);
				Thread t = new Thread(crawler, "Thread" + i);
				t.start();

				// sleep for a while to prevent "ban".
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.error("Thread failed, id: " + i + ", URL: " + url);
			}
			i++;
			url = getUrlFromQueue();
		}
		// Program will finish when queue is empty.
		Log.log("Program finished.");
	}

	/**
	 * Load configure informations from configure file.
	 */
	private static void getConfigInfo() {
		Document doc;
		SAXBuilder RSS_Factory = new SAXBuilder();
		try {
			doc = RSS_Factory.build("config.xml");
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("can not load 'config.xml'. : " + e.getMessage());
			return;
		}
		Element root = doc.getRootElement();
		List<Element> eleList = root.getChildren();
		for (Element element : eleList) {
			if (element.getName().equals("url")) {
				feedUrl = element.getText();
			}
			if (element.getName().equals("newstart")) {
				startFromFeedFlag = element.getText();
			}

			if (element.getName().equals("db")) {
				List<Element> dbContents = element.getChildren();
				for (Element dbEle : dbContents) {
					if (dbEle.getName().equals("link")) {
						dbConnection = dbEle.getText();
					}
					if (dbEle.getName().equals("id")) {
						dbId = dbEle.getText();
					}
					if (dbEle.getName().equals("password")) {
						dbPw = dbEle.getText();
					}
				}
			}

		}
	}

	/**
	 * 1. Set given feed to queue.<br/>
	 * 2. Initial database.<br/>
	 * 3. Initial TSV file.
	 */
	private static void setFeedToQueue() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "0");
		map.put("url", feedUrl);
		queue.add(map);

		initData();
	}

	/**
	 * Initial database.(clear all tables.)
	 */
	private static void initData() {
		// clear database.
		UrlQueueDao.clearTable();
		ItemHistoryDao.clearTable();
		ItemInfoDao.clearTable();
		UrlHistoryDao.clearTable();

		// clear the file, write file title.
		FileUtil.writeTitle();
	}

	/**
	 * Push the first link of link queue.<br/>
	 * 1. Push: get first element, and then delete it both in queue and database.<br/>
	 * 2. If queue is empty, fetch top 200 records from queue table and set them into queue.<br/>
	 * 3. If both queue and queue table in database are empty, return null.<br/>
	 * @return
	 */
	private static String getUrlFromQueue() {
		String url = null;
		if (queue == null || queue.size() == 0) {
			queue = UrlQueueDao.getQueueTop200();
		}

		if (queue.size() > 0) {
			Map<String, String> map = queue.get(0);
			url = map.get("url");
			UrlQueueDao.removeURL(map.get("id"));
			queue.remove(0);
		}
		return url;
	}

}