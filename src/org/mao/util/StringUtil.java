package org.mao.util;

/**
 * Text analysis tool class.
 * 
 * @author MAO
 * 
 */
public class StringUtil {

	/**
	 * Judge If given String is null or empty.
	 * 
	 * @param string
	 * @return boolean
	 */
	public static boolean isEmpty(String string) {
		if (string == null || string.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Judge if the given URL is item page.<br/>
	 * Return item code if it has,<br/>
	 * else return null.
	 * 
	 * @param url
	 * @return
	 */
	public static String getItemCode(String url) {
		String itemCode = null;
		int idx = url.indexOf("/dp/");
		if (idx > 0) {
			String subStr = url.substring(idx + 4) + "/";
			itemCode = subStr.substring(0, subStr.indexOf("/"));
		}
		return itemCode;
	}

	/**
	 * Convert escape character if given text exist Single quotation marks.<br/>
	 * Suit for prevent SQL error.
	 * 
	 * @param url
	 * @return
	 */
	public static String safeSql(String url) {
		return url.replace("'", "\\'");
	}

}
