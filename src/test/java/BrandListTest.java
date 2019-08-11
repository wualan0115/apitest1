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

public class BrandListTest extends BaseTest {
    String path  = "javamall/shop/admin/brand!listJson.do";

    @Test(description = "品牌列表")
    public void testBrandList() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("page", "1");
        paramMap.put("rows", "1000");
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("brandList响应结果----"+responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray brandnameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlCountStr = "SELECT count(*) from es_brand WHERE disabled = '0'";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT name from es_brand WHERE disabled = '0' ORDER BY brand_id desc ";
        String[] dbbrandnameList = new String[brandnameArray.size()];
        for (int i = 0; i < dbbrandnameList.length; i++) {
            dbbrandnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <brandnameArray.size() ; i++) {
            Assert.assertEquals(brandnameArray.get(i),dbbrandnameList[i]);
        }
    }

    @Test(description = "根据名称搜索")
    public void testSearchBrand() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("page", "1");
        paramMap.put("rows", "1000");
        String keyword = "红袖";
        paramMap.put("keyword",keyword);
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("brandList响应结果----"+responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray brandnameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlCountStr = "SELECT count(*) from es_brand WHERE disabled = '0' and name like '%"+keyword+"%'";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT name from es_brand WHERE disabled = '0' and name like '%"+keyword+"%' ORDER BY brand_id desc ";
        String[] dbbrandnameList = new String[brandnameArray.size()];
        for (int i = 0; i < dbbrandnameList.length; i++) {
            dbbrandnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <brandnameArray.size() ; i++) {
            Assert.assertEquals(brandnameArray.get(i),dbbrandnameList[i]);
        }
    }
}
