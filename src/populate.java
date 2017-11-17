import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.json.*;

import SQLs.Connector;
import SQLs.JsonFileReader;

public class populate {
	JsonFileReader reader;
	Connector connector;
	
	populate() {
		reader = new JsonFileReader();
		try {
			connector = new Connector(); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("fail to load jdbc driver");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		populate p = new populate();
		String tset = "";
		try {
			//p.reader.loadNewFile("./YelpDataset/yelp_business.json");
			Connection connection = p.connector.connect();
			Statement statement = connection.createStatement();
			//cleaning table
			for (String sql : p.cleanTableData()) {
				tset = sql;
				//statement.executeUpdate(sql);
			}
			
			//insert values
			p.insertBusiness("./YelpDataset/yelp_business.json", "./YelpDataset/yelp_checkin.json");
			System.out.println("insertBusiness done");
			//p.insertCheckin("./YelpDataset/yelp_checkin.json");
			p.insertReview("./YelpDataset/yelp_review.json", "./YelpDataset/yelp_user.json");
			connection.close();
			System.out.println("done");
			
		} catch (SQLException e) {
			System.out.println(tset);
			e.printStackTrace();
		}
	}
	
	public void insertBusiness(String fileName, String checkinFileName) {
		HashMap<String, Integer> checkinCount = checkinCount(checkinFileName);
		HashSet<String> mainCategory = new HashSet<String>();
		mainCategory.add("Active Life");
		mainCategory.add("Arts & Entertainment");
		mainCategory.add("Automotive");
		mainCategory.add("Car Rental");
		mainCategory.add("Cafes");
		mainCategory.add("Beauty & Spas");
		mainCategory.add("Convenience Stores");
		mainCategory.add("Dentists");
		mainCategory.add("Doctors");
		mainCategory.add("Drugstores");
		mainCategory.add("Department Stores");
		mainCategory.add("Education");
		mainCategory.add("Event Planning & Services");
		mainCategory.add("Flowers & Gifts");
		mainCategory.add("Food");
		mainCategory.add("Health & Medical");
		mainCategory.add("Home Services");
		mainCategory.add("Home & Garden");
		mainCategory.add("Hospitals");
		mainCategory.add("Hotels & Travel");
		mainCategory.add("Hardware Stores");
		mainCategory.add("Grocery");
		mainCategory.add("Medical Centers");
		mainCategory.add("Nurseries & Gardening");
		mainCategory.add("Nightlife");
		mainCategory.add("Restaurants");
		mainCategory.add("Shopping");
		mainCategory.add("Transportation");
		System.out.println(checkinCount.size());
		//String statement = "";
		try {
			Connection connection = connector.connect();
			PreparedStatement businessS = connection.prepareStatement("insert into YelpBusiness values (?,?,?,?,?,?,?,?,?)");
			PreparedStatement hourS = connection.prepareStatement("insert into Hours values (?,?,?,?)");
			PreparedStatement attributeS = connection.prepareStatement("insert into YelpBusinessAttributes values(?, ?)");
			PreparedStatement mainCategoryS = connection.prepareStatement("insert into YelpMainCategory values(?, ?)");
			PreparedStatement subCategoryS = connection.prepareStatement("insert into YelpSubCategory values(?, ?)");
//			PreparedStatement neighborS = connection.prepareStatement("insert into YelpBusinessNeighbors values(?, ?)");
			JsonFileReader reader = new JsonFileReader();
			reader.loadNewFile(fileName);
			JSONObject j;
			
			while ((j = reader.nextJSONObject()) != null) {
			
				//---------------YelpBuisiness---------------------------------------------				
				businessS.setString(1, j.getString("business_id"));
				businessS.setString(2, j.getString("city"));
				businessS.setString(3, j.getString("full_address"));
				businessS.setString(4, j.getString("name"));
				businessS.setString(5, String.valueOf(j.getBoolean("open")).substring(0, 1));
				businessS.setInt(6, j.getInt("review_count"));
				businessS.setDouble(7, j.getDouble("stars"));
				businessS.setString(8, j.getString("state"));
				Integer count = checkinCount.get(j.getString("business_id"));
				businessS.setInt(9, (count == null) ? 0 : count);
				
				businessS.executeUpdate();
				//-------------------hour----------------------------------------
				JSONObject hours = j.getJSONObject("hours");
				String days[] = JSONObject.getNames(hours);

				if (days != null) {
					hourS.setString(1, j.getString("business_id"));
					for (String day : days) { // format: Mon:12:00-13:00,Tue: 01:00-15:00
//						tempBuilder.append(day.substring(0, 3) + ": "); 
//						tempBuilder.append(hours.getJSONObject(day).getString("open") + ":");
//						tempBuilder.append(hours.getJSONObject(day).getString("close") + ",");
						hourS.setString(2, day);
						hourS.setString(3, hours.getJSONObject(day).getString("open"));
						hourS.setString(4, hours.getJSONObject(day).getString("close"));
						hourS.executeQuery();
					}
					//hourString = tempBuilder.toString();
				}
				//---------------YelpBusinessAttributes-------------------------------------
				
				JSONObject attribute = j.getJSONObject("attributes");
				List<String> values = new LinkedList<String>();
				if (attribute != null) {
					values.addAll(attributeToString(attribute, ""));
				}
				attributeS.setString(1, j.getString("business_id"));
				
				for (String value: values) {
					//statement = "insert into YelpBusinessAttributes values('" + j.getString("business_id") + "','" + value + "')";
					attributeS.setString(2,value);
					attributeS.executeUpdate();
				}
				
				//---------------YelpBusinessCategory----------------------------------------
				List<String> categorys = jsonArrayToStringList(j.getJSONArray("categories"));
				mainCategoryS.setString(1, j.getString("business_id"));
				subCategoryS.setString(1, j.getString("business_id"));
				for (String category: categorys) {
					if (mainCategory.contains(category)) {
						mainCategoryS.setString(2, category);
						mainCategoryS.executeUpdate();
					}
					else {
						subCategoryS.setString(2, category);
						subCategoryS.executeUpdate();
					}

				}
				
				//--------------YelpBusinessNeighbors-----------------------------------------
//				List<String> neighbors = jsonArrayToStringList(j.getJSONArray("neighborhoods"));
//				neighborS.setString(1,j.getString("business_id"));
//				for (String neighbor: neighbors) {
//					//statement = "inset into YelpBusinessNeighbors values ('" + j.getString("business_id") + "','" + neighbor + "')";
//					neighborS.setString(2, neighbor);
//					neighborS.executeUpdate();
//				}
				//return statements;
			} 
			connection.close();
			reader.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			//System.out.println(statement);
			e.printStackTrace();
		}
		//return null;
 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertCheckin(String fileName) {
		String statements = "";
		try {
			Connection connection = connector.connect();
			PreparedStatement checkinS = connection.prepareStatement("insert into Yelp_Checkin values (?,?,?)");
			JsonFileReader reader = new JsonFileReader();
			reader.loadNewFile(fileName);
			JSONObject j;
			
			while ((j = reader.nextJSONObject()) != null) {
				checkinS.setString(1, j.getString("business_id"));
				JSONObject checkin = j.getJSONObject("checkin_info");
				String[] times = JSONObject.getNames(checkin);
				if (times == null) continue;
				for (String time: times) {
					statements = "insert into Yelp_Checkin values('"+ j.getString("business_id") + "','" + time + "'," + checkin.getInt(time) + ")";
					System.out.println(statements);
					checkinS.setString(2, time);
					checkinS.setInt(3, checkin.getInt(time));
					checkinS.executeUpdate();
				}
			}
			connection.close();
			reader.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertReview(String reviewFileName, String userFileName) {
		try {
			Connection connection = connector.connect();
			PreparedStatement reviewS = connection.prepareStatement("insert into Yelp_Review values (?,?,?,?,?,?,?,?,?,?)");
			reader.loadNewFile(reviewFileName);
			HashMap<String, String> userMap = geteUserName(userFileName);
			JSONObject j;
			while ((j = reader.nextJSONObject()) != null) {
				reviewS.setString(1, j.getString("business_id"));
				reviewS.setDate(2, Date.valueOf(j.getString("date")));
				reviewS.setString(3, j.getString("review_id"));
				reviewS.setInt(4, j.getInt("stars"));
				reviewS.setString(5, j.getString("text"));
				reviewS.setString(6, j.getString("user_id"));
				reviewS.setInt(7, j.getJSONObject("votes").getInt("cool"));
				reviewS.setInt(8, j.getJSONObject("votes").getInt("funny"));
				reviewS.setInt(9, j.getJSONObject("votes").getInt("useful"));
				reviewS.setString(10, userMap.get(j.getString("user_id")));
				reviewS.executeUpdate();
			}
			connection.close();
			reader.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Integer> checkinCount(String fileName) {
		HashMap<String, Integer> checkinCount = new HashMap<String, Integer>(32768);
		
		JsonFileReader reader = new JsonFileReader();
		try {
			reader.loadNewFile(fileName);
			JSONObject j;
			
			while ((j = reader.nextJSONObject()) != null) {
				int count = 0;
				JSONObject checkin = j.getJSONObject("checkin_info");
				String[] checkinTimes = JSONObject.getNames(checkin);
				for (String checkinTime: checkinTimes) {
					count = count + checkin.getInt(checkinTime);
				}
				checkinCount.put(j.getString("business_id"), count);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkinCount;
	}
	
	public HashMap<String, String> geteUserName(String fileName) {
		HashMap<String, String> userMap = new HashMap<String, String>(32768);
		
		JsonFileReader reader = new JsonFileReader();
		JSONObject j;
		try {
			reader.loadNewFile(fileName);
			while((j = reader.nextJSONObject()) != null) {
				userMap.put(j.getString("user_id"), j.getString("name"));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userMap;
	}
	/**
	 * This method turns attributes,a JSONObject, to a list of string/ 
	 * @param j
	 * @param superName
	 * @return
	 */
	public List<String> attributeToString(JSONObject j, String superName) {
		List<String> values = new LinkedList<String>();
		if (j != null) {
			String attributeName[] = JSONObject.getNames(j);
			if (attributeName == null) return values;
			for (String key : attributeName) {
				//System.out.println(key);
				Object value = j.get(key);
				String dataType = value.getClass().getSimpleName();
				if (dataType.equals("JSONObject")){
					values.addAll(attributeToString((JSONObject)value, superName + key + "_"));
					}
				else if (dataType.equals("Boolean")){
					values.add(superName + key + "_" + value.toString());
				} 
				else {
					values.add(superName + key + "_" + j.get(key));
				}
			}
			return values;
		}
		return null;
	}
	
	public List<String> jsonArrayToStringList(JSONArray j) {
		List<String> values = new LinkedList<String>();
		if (j == null) return values;
		for (int i = 0; i < j.length(); i++) {
			values.add(j.getString(i));
		}
		return values;
	}
	
	public String[] cleanTableData() {
		String[] statements = {
				"delete from Hours",
				"delete from YelpBusinessNeighbors",
				"delete from YelpMainCategory",
				"delete from YelpSubCategory",
				"delete from YelpBusinessAttributes",
				"delete from YelpBusiness",
				"delete from Yelp_Review"
		};
		return statements;
	}
}
