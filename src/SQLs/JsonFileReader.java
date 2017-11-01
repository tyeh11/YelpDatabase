package SQLs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONObject;
/**
 * This class is used for reading json data file while containing only one type json object.
 * @author TC_Yeh
 *
 */
public class JsonFileReader {
	File jsonFile;
	Scanner s;
	
	/**
	 * This constructor creates a scanner for reading json objects in a json file.
	 * @param fileName
	 * @throws FileNotFoundException 
	 */
	JsonFileReader(String fileName) throws FileNotFoundException {
		jsonFile = new File(fileName);
		s = new Scanner(jsonFile);
	}
	
	/**
	 * Go to and return next json object.
	 * If no more object, return null.
	 * @return
	 */
	public JSONObject nextJSONObject() {
		if (s.hasNextLine()){
			return new JSONObject(s.nextLine());
		}
		else return null;
	}
	
	public void close() {
		s.close();
	}
	
	public void resetReader() {
		s = s.reset();
	}
}
