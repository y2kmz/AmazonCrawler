package org.mao.db.dao;

import org.mao.db.DBopration.DBoperation;
import org.mao.db.dto.ItemInfo;
import org.mao.util.StringUtil;

/**
 * DB operations of item_info table.
 * 
 * @author MAO
 * 
 */
public class ItemInfoDao {

	/**
	 * clear all data.
	 */
	public static void clearTable() {
		String queryStr = "delete from item_info";
		DBoperation.dbOpration.executeBySql(queryStr);
	}

	/**
	 * insert item information to table.
	 * 
	 * @param iteminfo
	 */
	public static void inserData(ItemInfo iteminfo) {
		String queryStr = "INSERT INTO item_info VALUES (NULL, ?, ?, ?, ?, ?, NULL)";
		String[] param = new String[5];
		param[0] = StringUtil.safeSql(iteminfo.getItemcode());
		param[1] = StringUtil.safeSql(iteminfo.getTitle());
		param[2] = StringUtil.safeSql(iteminfo.getDescription());
		param[3] = StringUtil.safeSql(iteminfo.getPrice());
		param[4] = StringUtil.safeSql(iteminfo.getUrl());

		DBoperation.dbOpration.executeBySql(queryStr, param);
	}
}
