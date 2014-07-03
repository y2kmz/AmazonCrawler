package org.mao.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.mao.db.dto.ItemInfo;

/**
 * Write data to specified TSV file.
 * 
 * @author MAO
 * 
 */
public class FileUtil {
	private static String logFilePath = "item_infomations.tsv";

	/**
	 * Clear all data in specified file.<br/>
	 * Write table tile of file.
	 */
	public static void writeTitle() {
		String title = "";
		title += "product title";
		title += "\t";
		title += "description";
		title += "\t";
		title += "price";
		title += "\t";
		title += "URL";
		writeLine(title, false);
	}

	/**
	 * Add a new row to specified file by give data.
	 * 
	 * @param iteminfo
	 */
	public static void writeItem(ItemInfo iteminfo) {
		String line = "";
		line += iteminfo.getTitle();
		line += "\t";
		line += iteminfo.getDescription();
		line += "\t";
		line += iteminfo.getPrice();
		line += "\t";
		line += iteminfo.getUrl();
		writeLine(line, true);
	}

	/**
	 * Write data to file.
	 * 
	 * @param str
	 * @param append
	 */
	private static void writeLine(String str, boolean append) {
		try {
			// Write to file.
			// Create new file if not exist.
			FileWriter fw = new FileWriter(logFilePath, append);
			BufferedWriter bf = new BufferedWriter(fw);

			bf.append(str);
			bf.append("\r\n");
			bf.flush();

			bf.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
