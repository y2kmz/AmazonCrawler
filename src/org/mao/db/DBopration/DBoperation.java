package org.mao.db.DBopration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.mao.util.Log;

/**
 * Base database connection and enhanced query method.
 * @author MAO
 *
 */
public class DBoperation {

	private Connection con = null;

	private Statement sm = null;
	
	public static DBoperation dbOpration;

	/**
	 * Constructor, connect database by specified info.
	 * @param url
	 * @param id
	 * @param pw
	 */
	public DBoperation(String url, String id, String pw) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance().getClass();

			con = java.sql.DriverManager.getConnection(url, id, pw);

			sm = con.createStatement();
		} catch (Exception e) {
			System.out.println("java.sql.connection initialing Exception");
			e.printStackTrace();
			Log.error("java.sql.connection initialing Exception");
			Log.error(e.getMessage());
		}
	}
	
	/**
	 * Execute query by SQL. <br/>
	 * Return a map list of result.
	 * @param sqlString
	 * @return
	 */
	public List<Map<String, Object>> queryBySql(String sqlString) {
		List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();

		try {
			ResultSet rs = sm.executeQuery(sqlString);
			//get column names.
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				Map<String, Object> rsHash = new Hashtable<String, Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					Object o;
					if (rs.getObject(i) == null)
						o = "";
					else
						o = rs.getObject(i);
					
					String name = rsmd.getColumnName(i);
					if(name == null || "".equals(name)){
						name = rsmd.getColumnLabel(i);
					}
					rsHash.put(name, o);
				}
				rsList.add(rsHash);
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("java.sql.Statement executeQuery Exception");
			e.printStackTrace();
			Log.error("java.sql.Statement executeQuery Exception");
			Log.error(e.getMessage());
		}
		return rsList;
	}

	/**
	 * Execute query by SQL and separated parameters.<br/>
	 * Return a map list of result.
	 * 
	 * @param sqlString
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> queryBySql(String sqlString,
			String[] parameters) {
		List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();

		try {
			PreparedStatement psm = con.prepareStatement(sqlString);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					psm.setString(i + 1, parameters[i]);
				}
			}
			ResultSet rs = psm.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				Map<String, Object> rsHash = new Hashtable<String, Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					Object o;
					if (rs.getObject(i) == null)
						o = "";
					else
						o = rs.getObject(i);
					String name = rsmd.getColumnName(i);
					if(name == null || "".equals(name)){
						name = rsmd.getColumnLabel(i);
					}
					rsHash.put(name, o);
				}
				rsList.add(rsHash);
			}
			rs.close();
			psm.close();
		} catch (Exception e) {
			System.out
					.println("java.sql.PreparedStatement executeQuery Exception");
			e.printStackTrace();
			Log.error("java.sql.PreparedStatement executeQuery Exception");
			Log.error(e.getMessage());
		}
		return rsList;
	}

	/**
	 * Execute by SQL.
	 * @param sqlString
	 * @return
	 */
	public boolean executeBySql(String sqlString) {
		boolean success = false;
		try {
			success = !sm.execute(sqlString);
		} catch (SQLException e) {
			System.out.println("java.sql.Statement executing Exception");
			e.printStackTrace();
			Log.error("java.sql.Statement executing Exception");
			Log.error(e.getMessage());
		}
		return success;
	}

	/**
	 * Execute by SQL and separated parameters.
	 * @param sqlString
	 * @param parameters
	 * @return
	 */
	public boolean executeBySql(String sqlString, String[] parameters) {
		boolean success = false;
		try {
			PreparedStatement psm = con.prepareStatement(sqlString);
			for (int i = 0; i < parameters.length; i++) {
				psm.setString(i + 1, parameters[i]);
			}
			success = psm.execute();
		} catch (SQLException e) {
			System.out.println("java.sql.Statement executing Exception");
			e.printStackTrace();
			Log.error("java.sql.Statement executing Exception");
			Log.error(e.getMessage());
		}
		return success;
	}

	/**
	 * Release connection.
	 */
	public void freeAll() {
		try {
			sm.close();
		} catch (SQLException e) {
			System.out.println("java.sql.Connection closing Exception");
			e.printStackTrace();
			Log.error("java.sql.Connection closing Exception");
			Log.error(e.getMessage());
		}
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("java.sql.Statement closing Exception");
			e.printStackTrace();
			Log.error("java.sql.Statement closing Exception");
			Log.error(e.getMessage());
		}
	}

}
