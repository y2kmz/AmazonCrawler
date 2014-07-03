package org.mao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log tool of project.<br/>
 * Write log to specified file.
 * 
 * @author MAO
 * 
 */
public class Log {
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss:SSS");

	private static String logFilePath = "crawler_log.log";

	/**
	 * Write log with tag [log].
	 * 
	 * @param log
	 */
	public static void log(String log) {
		writeMsg("log", log);
	}

	/**
	 * Write log with tag [error].
	 * 
	 * @param log
	 */
	public static void error(String log) {
		writeMsg("error", log);
	}

	/**
	 * Write data to file.
	 * 
	 * @param type
	 * @param log
	 */
	private static void writeMsg(String type, String log) {
		boolean append = false;
		File file = new File(logFilePath);
		try {
			if (file.exists()) {
				append = true;
			}
			// Write to file.
			// Create new file if not exist.
			FileWriter fw = new FileWriter(logFilePath, append);
			BufferedWriter bf = new BufferedWriter(fw);

			if ("log".equals(type)) {
				bf.append("@[ LOG ]");
			} else if ("error".equals(type)) {
				bf.append("@[ERROR]");
			} else {
				bf.append("@[OTHER]");
			}

			bf.append("[" + sdf.format(new Date()) + "]: ");
			bf.append("\t" + log + "\r\n");
			bf.flush();
			bf.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
