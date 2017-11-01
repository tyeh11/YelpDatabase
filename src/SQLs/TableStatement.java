package SQLs;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

/**
 * This class is used for generating DDL statement based on a json file containing only one type of json object. 
 * @author TC_Yeh
 *
 */
public class TableStatement {
	JsonFileReader reader;
	Set<String> attributes;
	Map<String, Integer> attributeLength;

	public TableStatement() {
	}
	
	public void loadJsonFile(String fileName) throws FileNotFoundException {
		reader = new JsonFileReader(fileName);
	}
    
	public void getAttributes() {
		attributeLength = new HashMap<String,Integer>();
		attributes = new HashSet<String>();
		JSONObject tempJson;
		
		reader.resetReader();
		while ((tempJson = reader.nextJSONObject()) != null) {
			jsonDataType(tempJson, "");
		}
		ArrayList<String> abc = new ArrayList<String>(128);
		
		for (Entry<String, Integer> a: attributeLength.entrySet()) {
			abc.add("+\"" + a.getKey() + " " + "VARCHAR(" + a.getValue() + "),\"");
		}
		
		for (String a: attributes) {
			abc.add("+\"" + a + ",\"");
		}
		String[] ccc = abc.toArray(new String[1]);
		Arrays.sort(ccc);
		for (String a: ccc) {
			System.out.println(a);
		}
	}
	
	public void jsonDataType(JSONObject j, String superName) {
		String[] fileds = JSONObject.getNames(j);
		if (fileds == null) return;
		for (String a:fileds) {
			String b = a.replace(' ', '_');
			Object temp = j.get(a);
			String datatype = temp.getClass().getSimpleName();
			if (datatype.equals("JSONObject")) {
				jsonDataType(j.getJSONObject(a), superName + b + "_");
			}
			else {
				//attributes.add(a);
				if (datatype.equals("String")) {
					String tempName = (String)temp;
					if (attributeLength.containsKey(superName + b)) {
						int tempLen = attributeLength.get(superName + b);
						if (tempLen > tempName.length()) {
							attributeLength.put(superName + b, tempLen);
						}
					} else {
						attributeLength.put(superName + b, tempName.length());
					}
				}
				else {
					attributes.add(superName + b + " " + datatype);
				}
			}
		}
	}
	
	public void closeFile() {
		reader.close();
	}
}
