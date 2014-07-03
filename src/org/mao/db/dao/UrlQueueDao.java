package org.mao.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mao.db.DBopration.DBoperation;
import org.mao.util.StringUtil;

/**
 * DB operations of url_queue table.
 * 
 * @author MAO
 * 
 */
public class UrlQueueDao {

	/**
	 * Count the amount for records in queue table.
	 * 
	 * @return count
	 */
	public static long getCount() {
		long count = 0;
		try {
			String sqlStr = "select count(*) count from url_queue";
			List<Map<String, Object>> postList = DBoperation.dbOpration
					.queryBySql(sqlStr);
			if (postList.size() > 0) {
				Map<String, Object> map = postList.get(0);
				if (map.get("count") != null) {
					count = ((Long) map.get("count")).longValue();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * Check if has specified URL in table.
	 * 
	 * @param url
	 * @return
	 */
	public static boolean hasRecord(String url) {
		long count = 0;
		String sqlStr = "select count(*) count from url_queue where url = ?";
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

	/**
	 * Select top 200 records from queue table.
	 * 
	 * @return list
	 */
	public static List<Map<String, String>> getQueueTop200() {
		// Get top 200 records from table
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String sqlStr = "select * from url_queue ORDER BY id limit 200";
			List<Map<String, Object>> rsList = DBoperation.dbOpration
					.queryBySql(sqlStr);

			for (Map<String, Object> rsMap : rsList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "" + rsMap.get("id"));
				map.put("url", "" + rsMap.get("url"));
				list.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Delete a record by id.
	 * 
	 * @param id
	 */
	public static void removeURL(String id) {
		String queryStr = "delete from url_queue where id = ?";
		String[] param = new String[1];
		param[0] = id;

		DBoperation.dbOpration.executeBySql(queryStr, param);
	}

	/**
	 * clear all data.
	 */
	public static void clearTable() {
		String queryStr = "delete from url_queue";
		DBoperation.dbOpration.executeBySql(queryStr);
	}

	/**
	 * Add link list to queue table.
	 * 
	 * @param filteredUrlList
	 */
	public static void addData(List<String> filteredUrlList) {
		if (filteredUrlList.size() > 0) {
			String queryStr = "INSERT INTO url_queue VALUES ";
			for (String string : filteredUrlList) {
				queryStr += "(NULL, '" + StringUtil.safeSql(string)
						+ "' , NULL),";
			}
			queryStr = queryStr.substring(0, queryStr.length() - 1) + ";";
			DBoperation.dbOpration.executeBySql(queryStr);
		}
	}

}
