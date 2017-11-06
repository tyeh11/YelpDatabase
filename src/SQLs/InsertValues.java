package SQLs;

import java.io.FileNotFoundException;

import org.json.JSONObject;

public class InsertValues {
	JsonFileReader reader;
	
	public InsertValues(){
		reader = new JsonFileReader();
	}
	
	public void insertFromBusiness(String fileName) throws FileNotFoundException {
		reader.loadNewFile(fileName);
		JSONObject jO;
		
		while ((jO = reader.nextJSONObject()) != null) {
			
		}
	}
	
}
