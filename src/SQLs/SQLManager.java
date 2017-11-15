package SQLs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class SQLManager extends Observable {
	private Connector connector;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private List<String> mainCategory;
	private List<String> subCategory;
	private List<String> attributes;
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
	public String generateOrQuery(String select, String from, String[] where, String subQuery, String orderBy) {
		StringBuffer sql = new StringBuffer(generateQuery(select, from, null, null, null, null));
		StringBuffer wSection = new StringBuffer("\nwhere ");
		if (where != null) {
			for (String w: where) {
				wSection.append(w + " or ");
			}
			sql.append(wSection.subSequence(0, wSection.length() - 4));
			if (subQuery != null && !subQuery.equals("")) {
				sql.append(" and " + subQuery);
			}
		}
		else {
			if (subQuery != null && !subQuery.equals("")) {
				sql.append("where " + subQuery);
			}
		}

		if (orderBy != null && !orderBy.equals("")) {
			sql.append("\norder by " + orderBy);
		}
		//System.out.println(sql);
		return sql.toString();
	}
	
	public String generateAndQuery(String select, String from, String searchTarget, String[] where, String orderBy) {
		String sql = new String(generateQuery(searchTarget, from, where[0], null, null, null));
		
		for ( int i = 1; i < where.length - 1; i++) {
			sql = generateQuery(searchTarget, from, where[i], searchTarget, sql, null);
		}
		if (where.length > 1) {
			sql = generateQuery(searchTarget, from, where[where.length - 1], searchTarget, sql, null);
		}
		
		return sql;
	}
	
	public String generateQuery(String select, String from, String where, String searchTarget, String subQuery, String orderBy) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct " + select);
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
		
		while (resultSet.next()) {
			result.add(resultSet.getString(1));
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
	
}
