package util;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HttpUtil {

    public static String getURL(String urlPrex, String path){
        return urlPrex + path;
    }

    public static String getURL(String propFile, String prefix, String path){
        Properties prop = PropertyUtil.getProperties(propFile);
        String urlPrex = prop.getProperty(prefix);
        return urlPrex + path;
    }

    public static String getQueryParam(Map<String, String> queryMap) throws UnsupportedEncodingException {
        StringBuilder accum = new StringBuilder();
        for (Map.Entry<String, String> entry:queryMap.entrySet()) {
            accum.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"utf-8"));
        }
        String queryParam = accum.toString().replaceFirst("&","?");
        return queryParam;
    }

    public static UrlEncodedFormEntity getFormEntity(Map<String,String> map){
        List<NameValuePair> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry:map.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
        return entity;
    }
}
