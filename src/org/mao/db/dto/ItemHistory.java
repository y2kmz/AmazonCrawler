package org.mao.db.dto;

import java.util.Date;

/**
 * Bean of item_history.
 * 
 * @author MAO
 * 
 */
public class ItemHistory {
	private int id;
	private String itemcode;
	private String url;
	private Date time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
