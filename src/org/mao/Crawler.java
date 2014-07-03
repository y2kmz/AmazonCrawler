package org.mao;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.mao.db.dao.ItemHistoryDao;
import org.mao.db.dao.ItemInfoDao;
import org.mao.db.dao.UrlHistoryDao;
import org.mao.db.dao.UrlQueueDao;
import org.mao.db.dto.ItemInfo;
import org.mao.util.FileUtil;
import org.mao.util.Log;
import org.mao.util.StringUtil;
import org.mao.util.UrlFilter;

/**
 * Thread class.<br/>
 * The thread is to crawl specified URL.
 * 
 * @author MAO
 * 
 */
public class Crawler implements Runnable {
	private String url;

	public Crawler(String url) {
		this.url = url;
	}

	/**
	 * 1. Request specified URL, get the contents.<br/>
	 * 2. If the page contains item information, save them to item_info table
	 * and TSV file.<br/>
	 * 3. Fetch all link from the page, filter them and save the filtered link
	 * list to queue.
	 */
	@Override
	public void run() {
		try {
			UrlHistoryDao.inserData(url);
			// get specified page contents by JSOUP.
//			System.out.println("start request url: " + url);
			Document doc = Jsoup.connect(url).timeout(30000).get();

			// If it is a item page, find specified informations and save them.
			String itemCode = StringUtil.getItemCode(url);
			if (itemCode != null) {
				if (!ItemHistoryDao.hasCode(itemCode)) {
					String title = doc.select("#title").text();
					if (StringUtil.isEmpty(title.trim())) {
						title = doc.select(".parseasinTitle").text();
					}

					String price = doc.select("#priceblock_ourprice").text();
					if (StringUtil.isEmpty(price.trim())) {
						price = doc.select("#buyingPriceValue").text();
					}

					String description = doc.select(
							".productDescriptionWrapper").text();
					if (StringUtil.isEmpty(description.trim())) {
						description = doc.select("#productDescription").text();
					}

					if (!StringUtil.isEmpty(title)
							&& !StringUtil.isEmpty(price)) {
						ItemInfo iteminfo = new ItemInfo();
						iteminfo.setItemcode(itemCode);
						iteminfo.setTitle(title);
						iteminfo.setPrice(price);
						iteminfo.setDescription(description);
						iteminfo.setUrl(url);

						// Set item information to table.
						ItemInfoDao.inserData(iteminfo);
						ItemHistoryDao.inserData(iteminfo);
						// Set item information to file.
						FileUtil.writeItem(iteminfo);
						// Set item code to history.

					}
				}
			}

			// Get all links form the page.
			Elements items = doc.select("a");
			// Filter the links, just left unvisited, specified rules links.
			List<String> filteredUrlList = UrlFilter.filtLinks(items);

			// Set specified links to the queue.
			UrlQueueDao.addData(filteredUrlList);

		} catch (Exception e) {
			Log.error("can not load URL. : " + url + "\t:" + e.getMessage());
			// e.printStackTrace();
		}
	}

}
