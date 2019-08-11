import net.minidev.json.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.HttpUtil;
import util.JSONPathUtil;
import util.JdbcDataUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TypeListTest extends BaseTest{
    String path = "javamall/shop/admin/type!listJson.do";

    @Test(description = "类型列表")
    public void testTypeList() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("page", "1");
        paramMap.put("rows", "1000");
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("typeList响应结果----"+responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray typenameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlCountStr = "SELECT count(*) from es_goods_type WHERE disabled = '0'";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT name from es_goods_type WHERE disabled = '0' ORDER BY type_id desc ";
        String[] dbtypenameList = new String[typenameArray.size()];
        for (int i = 0; i < dbtypenameList.length; i++) {
            dbtypenameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <typenameArray.size() ; i++) {
            Assert.assertEquals(typenameArray.get(i),dbtypenameList[i]);
        }

    }
}
