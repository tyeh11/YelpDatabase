package SQLs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
	String host;
	String dbName;
	int port;
	String oracleURL;
	String userName;
	String password;
	Connection connection;
	
	public Connector() throws ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		host = "localhost";
		dbName = "TCYDB";
		port = 1521;
		userName = "SCOTT";
		password = "tiger";
		oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
	}
	
	public Connection connect() throws SQLException {
		connection = DriverManager.getConnection(oracleURL, userName, password);
		return connection ;
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
}


	
