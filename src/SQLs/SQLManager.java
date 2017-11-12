package SQLs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Observable;

public class SQLManager extends Observable {
	private Connector connector;
	private Connection connection;
	private Statement statement;
	private List<String> mainCategory;
	private List<String> subCategory;
	private List<String> attributes;
	
	SQLManager() throws ClassNotFoundException {
		connector = new Connector();
	}
	
	public void connect() throws SQLException {
		connection = connector.connect();
		statement = connection.createStatement();
	}
	
	public String generateSQL(String[] select, String[] from, String[] whereAnd, String[] whereOr) {
		// aaa bb
		StringBuffer sql = new StringBuffer();
		
		StringBuffer sSection = new StringBuffer("select ");
		for (String s: select) {
			sSection.append(s + ",");
		}
		sql.append(sSection.subSequence(0, sSection.length()-1));
		
		StringBuffer fSection = new StringBuffer("from ");
		for (String f: from) {
			fSection.append(f + ",");
		}
		sql.append(fSection.subSequence(0, fSection.length()-1));
		
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
			sql.append(wSection.subSequence(0, wSection.length()-4));
		}
		
		return sql.toString();
	}
}
