package util;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.log4j.Logger;

public class JSONPathUtil {
    static Logger log = Logger.getLogger(JSONPathUtil.class.getName());

    public static <T> T extract(String json,String jsonPath){
        try {
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
            return JsonPath.read(document,jsonPath);
        }catch (Exception e){
            log.error("jsonpath error,jsonpath="+jsonPath +" json="+json,e);
            return null;
        }

    }
}
