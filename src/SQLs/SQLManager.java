package SQLs;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import oracle.sql.CLOB;

public class SQLManager extends Observable {
	private Connector connector;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	public SQLManager() throws ClassNotFoundException {
		connector = new Connector();
	}
	
	public void connect() throws SQLException {
		connection = connector.connect();
		statement = connection.createStatement();
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	@SuppressWarnings("null")
	public String generateOrQuery(String select, String from, String[] where, String searchTarget, String subQuery, String orderBy) {
		StringBuffer sql = new StringBuffer(generateQuery(select, from, null, null, null, null, true));
		StringBuffer wSection = new StringBuffer("\nwhere ");
		if (where != null) {
			wSection.append("(");
			for (String w: where) {
				wSection.append(w + " or ");
			}
			sql.append(wSection.subSequence(0, wSection.length() - 4));
			sql.append(")");
			if (subQuery != null && !subQuery.equals("")) {
				sql.append(" and " + searchTarget + " in (" + subQuery + ")");
			}
		}
		else {
			if (subQuery != null && !subQuery.equals("")) {
				sql.append("where " + searchTarget + " in (" + subQuery + ")");
			}
		}

		if (orderBy != null && !orderBy.equals("")) {
			sql.append("\norder by " + orderBy);
		}
		//System.out.println(sql);
		return sql.toString();
	}
	
	public String generateAndQuery(String select, String from, String[] where, String searchTarget, String subQuery, String orderBy) {
		String sql = new String(generateQuery(searchTarget, from, where[0], searchTarget, subQuery, null, true));
		
		for ( int i = 1; i < where.length - 1; i++) {
			sql = generateQuery(searchTarget, from, where[i], searchTarget, sql, null, true);
		}
		if (where.length > 1) {
			sql = generateQuery(searchTarget, from, where[where.length - 1], searchTarget, sql, null, true);
		}
		
		return sql;
	}
	
	public String generateQuery(String select, String from, String where, String searchTarget, String subQuery, String orderBy, Boolean distinct) {
		StringBuffer sql = new StringBuffer();
		if (distinct) {
			sql.append("select distinct " + select);
		}
		else {
			sql.append("select " + select);
		}
		sql.append("\nfrom " + from);
		
		if (where != null && !where.equals("")) {
			sql.append("\nwhere " + where);
			if (subQuery != null && !subQuery.equals("")) {
				sql.append(" and " + searchTarget + " in (" +subQuery + ")");
			}
		}
		else {
			if (subQuery != null && !subQuery.equals("")) {
				sql.append("\nwhere " + searchTarget + " in ("+ subQuery + ")");
			}
		}
		
		if (orderBy != null && !orderBy.equals("")) {
			sql.append("\norder by " + orderBy);
		}
		return sql.toString();
	}
	
	public List<String> getCategories(String sql) throws SQLException {
		connect();
		List<String> result = new LinkedList<String>();
		resultSet = statement.executeQuery(sql);
		int columnNumber = resultSet.getMetaData().getColumnCount();
		
		while (resultSet.next()) {
			StringBuffer temp = new StringBuffer();
			for (int i = 1;  i < columnNumber + 1; i++) {
				temp.append(resultSet.getString(i) + ",");
			}
			result.add(temp.substring(0, temp.length()-1));
		}
		disconnect();
		
		return result;
	}
	
	
	public List<List<String>>  getBusiness(String sql) throws SQLException {
		//name, business_id, full_address, city, state, stars, review_count, checkin_count
		connect();
		List<List<String>> result = new LinkedList<List<String>>();
		resultSet = statement.executeQuery(sql);
		
		while (resultSet.next()) {
			LinkedList<String> temp = new LinkedList<String>();
			for (int i = 0; i < 8; i++) {
				temp.add(resultSet.getString(i+1));
			}
			result.add(temp);
		}
		disconnect();
		return result;
	}
	
	public List<List<String>>  getReview(String sql) throws SQLException {
		//name, business_id, full_address, city, state, stars, review_count, checkin_count
		connect();
		List<List<String>> result = new LinkedList<List<String>>();
		
		resultSet = statement.executeQuery(sql);
		
		while (resultSet.next()) {
			LinkedList<String> temp = new LinkedList<String>();
			for (int i = 0; i < 7; i++) {
				if (i == 0) {
					temp.add(resultSet.getString(i+1).substring(0,10));
				}
				else if (i == 2) {
					Clob c = resultSet.getClob(i+1);
					temp.add(c.getSubString(1, (int)c.length()));
				}
				else {
					temp.add(resultSet.getString(i+1));
				}
			}
			result.add(temp);
		}
		disconnect();
		return result;
	}
}
