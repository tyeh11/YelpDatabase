package SQLs;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CreateTable {	
	File sqlFile;
	Scanner s;
	CreateTable() {
		//tables = new LinkedList<String>();
	}
	


	public static String[] dropTable() {
		String[] tables = {"Yelp_User_Friend", "Yelp_User", "Yelp_Checkin", 
				"YelpBusinessAttributes", "YelpBusinessCategory", "YelpBusinessNeighbors", "YelpBusiness", "Yelp_Review"};
	    String[] dropStatement = new String[tables.length];
	    
	    for (int i = 0; i < tables.length; i++) {
	    	dropStatement[i] = "drop table " + tables[i]; 
	    }
	    return dropStatement;
	}
//	public static String getStatement() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
