package org.mao.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jsoup.select.Elements;
import org.mao.db.dao.UrlHistoryDao;
import org.mao.db.dao.UrlQueueDao;

/**
 * Tool of filtering given link list.
 * 
 * @author MAO
 * 
 */
public class UrlFilter {
	public static String domain;
	public static List<String> excludeList;

	/**
	 * Read configure informations from specified file.
	 */
	public static void getFilterConfig() {
		domain = "";
		excludeList = new ArrayList<String>();

		Document doc;
		SAXBuilder RSS_Factory = new SAXBuilder();
		try {
			doc = RSS_Factory.build("url_filter_config.xml");
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("can not load 'url_filter_config.xml'. : "
					+ e.getMessage());
			return;
		}
		Element root = doc.getRootElement();
		List<Element> eleList = root.getChildren();
		for (Element element : eleList) {
			if (element.getName().equals("domain")) {
				domain = element.getText();
				if (domain.endsWith("/")) {
					domain = domain.substring(0, domain.length() - 1);
				}
				if (!domain.startsWith("http://")) {
					domain = "http://" + domain;
				}
			}

			if (element.getName().equals("exclude-link")) {
				List<Element> keyList = element.getChildren();
				for (Element key : keyList) {
					excludeList.add(key.getText());
				}
			}

		}
	}

	/**
	 * Filer given link list.<br/>
	 * Return links which fit the rule.
	 * 
	 * @param items
	 * @return
	 */
	public static List<String> filtLinks(Elements items) {
		List<String> list = new ArrayList<String>();
		for (org.jsoup.nodes.Element item : items) {
			String url = item.attr("href");
			// Delete no-need links.
			if (StringUtil.isEmpty(url)) {
				continue;
			}
			if (url.startsWith("#")) {
				continue;
			}
			if (url.equals("/")) {
				continue;
			}

			// Delete link if contains exclude characters.
			if (isExculde(url)) {
				continue;
			}

			// Format the link to "http://domain..."
			if (url.startsWith("/")) {
				url = domain + url;
			}
			if (url.startsWith("www.")) {
				url = "http://" + url;
			}

			// Delete the link which is not specified domain.
			if (!url.startsWith(domain + "/")) {
				continue;
			}

			// Delete, if the link exist in Url_History table.
			boolean existInHistory = UrlHistoryDao.hasRecord(url);
			if (existInHistory) {
				continue;
			}
			// Delete, if the link exist in Url_Queue table.
			boolean existInQueue = UrlQueueDao.hasRecord(url);
			if (existInQueue) {
				continue;
			}

			list.add(url);
		}
		return list;
	}

	/**
	 * Judge if given URL contains exclude characters.
	 * 
	 * @param url
	 * @return
	 */
	private static boolean isExculde(String url) {
		boolean exist = false;
		if (StringUtil.isEmpty(url)) {
			return exist;
		}
		for (String pattern : excludeList) {
			if (url.contains(pattern)) {
				exist = true;
				break;
			}
		}
		return exist;
	}

}
