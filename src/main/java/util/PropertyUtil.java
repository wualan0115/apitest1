package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyUtil {
	private static Map<String, Properties> propFileMap = new ConcurrentHashMap<String, Properties>();

	public static void main(String[] args) {
		Properties prop = PropertyUtil.getProperties("/http.properties");
		System.out.println("http.webhost----"+prop.getProperty("http.webhost"));
	}

	public static Properties getProperties() {
		return getProperties("/config.properties");
	}

	public static Properties getProperties(String fileName) {
		Properties prop = propFileMap.get(fileName);
		if(prop == null){
			prop = new Properties();
		}
		InputStream is = null;
		try {
			is = PropertyUtil.class.getResourceAsStream(fileName);
			prop.load(is);
			propFileMap.put(fileName,prop);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	
	
}
