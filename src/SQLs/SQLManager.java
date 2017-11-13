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
	public String generateQuery(String[] select, String[] from, String[] whereAnd, String[] whereOr, String orderBy) {
		// aaa bb
		StringBuffer sql = new StringBuffer();
		
		StringBuffer sSection = new StringBuffer("select distinct ");
		for (String s: select) {
			sSection.append(s + ",");
		}
		sql.append(sSection.subSequence(0, sSection.length()-1) + "\n");
		
		StringBuffer fSection = new StringBuffer("from ");
		for (String f: from) {
			fSection.append(f + ",");
		}
		sql.append(fSection.subSequence(0, fSection.length()-1) + "\n");
		
		StringBuffer wSection = new StringBuffer("where ");
		if (whereAnd != null) {
			for (String w: whereAnd) {
				wSection.append(w + " and ");
			}
			wSection.replace(wSection.length() - 6, wSection.length(), " or ");
		}
		
		if (whereOr != null) {
			for (String w: whereOr) {
				wSection.append(w + " or ");
			}
		}
		
		if (whereAnd != null || whereOr != null) {
			sql.append(wSection.subSequence(0, wSection.length()-4) + "\n");
		}
		
		if (orderBy != null && !orderBy.equals("")) {
			sql.append("order by " + orderBy);
		}
		
		return sql.toString();
	}
	
	public List<String> getCategories(String sql) throws SQLException {
		connect();
		List<String> result = new LinkedList<String>();
		System.out.println(sql);
		resultSet = statement.executeQuery(sql);
		
		while (resultSet.next()) {
			result.add(resultSet.getString(1));
		}
		disconnect();
		
		return result;
	}
}
