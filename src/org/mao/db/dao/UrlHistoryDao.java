package org.mao.db.dao;

import java.util.List;
import java.util.Map;

import org.mao.db.DBopration.DBoperation;
import org.mao.util.StringUtil;

/**
 * DB operations of url_history table.
 * 
 * @author MAO
 * 
 */
public class UrlHistoryDao {

	/**
	 * clear all data.
	 */
	public static void clearTable() {
		String queryStr = "delete from url_history";
		DBoperation.dbOpration.executeBySql(queryStr);
	}

	/**
	 * Insert given URL to table.
	 * 
	 * @param url
	 */
	public static void inserData(String url) {
		String queryStr = "INSERT INTO url_history VALUES (NULL, ? ,NULL)";
		String[] param = new String[1];
		param[0] = StringUtil.safeSql(url);

		DBoperation.dbOpration.executeBySql(queryStr, param);
	}

	/**
	 * Check if has specified URL in table.
	 * 
	 * @param url
	 * @return
	 */
	public static boolean hasRecord(String url) {
		long count = 0;
		String sqlStr = "select count(*) count from url_history where url = ?";
		String[] param = new String[1];
		param[0] = url;

		List<Map<String, Object>> postList = DBoperation.dbOpration.queryBySql(
				sqlStr, param);
		if (postList.size() > 0) {
			Map<String, Object> map = postList.get(0);
			if (map.get("count") != null) {
				count = ((Long) map.get("count")).longValue();
			}
		}

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
