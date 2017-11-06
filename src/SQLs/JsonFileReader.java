package SQLs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	public JsonFileReader(String fileName) throws FileNotFoundException {
		jsonFile = new File(fileName);
		s = new Scanner(new BufferedReader(new FileReader(fileName)));
	}
	
	/**
	 * This constructor do nothing.
	 */
	public JsonFileReader() {
		
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
	
	/**
	 * This method loads a new file based on file name.
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void loadNewFile(String fileName) throws FileNotFoundException {
		jsonFile = new File(fileName);
		close();
		s = new Scanner(jsonFile);
	}
	
	/**
	 * This method closes an existing scanner.
	 */
	public void close() {
		if (s != null) {
			s.close();
		}
	}
	
	/**
	 * This method resets an existing scanner.
	 */
	public void resetReader() {
		if (s != null) {
			s.reset();
		}
	}
}
