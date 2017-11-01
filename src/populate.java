import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.json.*;

import SQLs.CreateTable;
import SQLs.TableStatement;

public class populate {
	public static void main(String[] args) {
		TableStatement a = new TableStatement();
//		try {
//			a.loadJsonFile("./YelpDataset/yelp_checkin.json");
//			a.getAttributes();
//			a.closeFile();
//			System.out.println("done");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String host = "localhost";
			String dbName = "TCYDB";
			int port = 1521;
			
			String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
			Connection connection = DriverManager.getConnection(oracleURL, "SCOTT", "tiger");
			DatabaseMetaData dbMetaData = connection.getMetaData();
			String productName = dbMetaData.getDatabaseProductName();
			System.out.println(productName);
			Statement statement = connection.createStatement();
			//System.out.println(CreateTable.getStatement());
			String drops[] = CreateTable.dropTable();
			for (String abc : drops) {
				try{
					statement.executeUpdate(abc);
				} catch (SQLException e) {
					System.out.println(abc + " drop fail.");
				}
				
			}
			
			String statements[] = CreateTable.getStatement();
			for (String abc : statements) {
				System.out.println("123");
				statement.executeUpdate(abc);
			}
			connection.close();
			//Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException cbfe) {
			System.out.println("Error loading driver"+cbfe);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
