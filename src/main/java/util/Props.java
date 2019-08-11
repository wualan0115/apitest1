package util;

import java.util.HashMap;
import java.util.Map;

public class Props {
    private static Map<String,Object> propsMap = new HashMap<>();

    public void clear(){
        propsMap.clear();
    }

    public static Object getObject(String key){
        return propsMap.get(key);
    }

    public static Map<String,Object> putObject(String key,Object value){
        propsMap.put(key,value);
        return propsMap;
    }

    public static String get(String key){
        return ""+propsMap.get(key);
    }

    public static Map<String,Object> put(String key,Object value){
        propsMap.put(key,value);
        return propsMap;
    }

    public static Map<String,Object> remove(String key){
        propsMap.remove(key);
        return propsMap;
    }
}
