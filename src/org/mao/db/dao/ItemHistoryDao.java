package org.mao.db.dao;

import java.util.List;
import java.util.Map;

import org.mao.db.DBopration.DBoperation;
import org.mao.db.dto.ItemInfo;
import org.mao.util.StringUtil;

/**
 * DB operations of item_history table.
 * 
 * @author MAO
 * 
 */
public class ItemHistoryDao {
	/**
	 * clear all data.
	 */
	public static void clearTable() {
		String queryStr = "delete from item_history";
		DBoperation.dbOpration.executeBySql(queryStr);
	}

	/**
	 * Check if has specified item code in table.
	 * 
	 * @param itemCode
	 * @return
	 */
	public static boolean hasCode(String itemCode) {
		long count = 0;
		String sqlStr = "select count(*) count from item_history where itemcode = ?";
		String[] param = new String[1];
		param[0] = itemCode;

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
	 * Insert given data to table.
	 * 
	 * @param iteminfo
	 */
	public static void inserData(ItemInfo iteminfo) {
		String queryStr = "INSERT INTO item_history VALUES (NULL, ?, ? ,NULL)";
		String[] param = new String[2];
		param[0] = StringUtil.safeSql(iteminfo.getItemcode());
		param[1] = StringUtil.safeSql(iteminfo.getUrl());

		DBoperation.dbOpration.executeBySql(queryStr, param);
	}
}
