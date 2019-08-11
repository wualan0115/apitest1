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

public class GoodsListTest extends BaseTest {
    String path = "javamall/shop/admin/goods!listJson.do";

    @Test(description = "商品列表")
    public void testGoodsList() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("page", "1");
        paramMap.put("rows", "10");
        paramMap.put("sort", "goods_id");
        paramMap.put("order", "asc");
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println(responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray goodsnameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlCountStr = "SELECT count(*) from es_goods WHERE disabled = '0'";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT name from es_goods WHERE disabled = '0' LIMIT 10";
        String[] dbgoodsnameList = new String[10];
        for (int i = 0; i < dbgoodsnameList.length; i++) {
            dbgoodsnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        System.out.println("total------------"+total);
        System.out.println("unameList内如如下----------------");
        for (int i = 0; i <goodsnameArray.size() ; i++) {
            System.out.println(goodsnameArray.get(i));
        }
        //断言
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <goodsnameArray.size() ; i++) {
            Assert.assertEquals(goodsnameArray.get(i),dbgoodsnameList[i]);
        }
    }

    @Test(description = "根据商品名称搜索")
    public void testSearchByGoodsname() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        String goodsname = "商";
        paramMap.put("page", "1");
        paramMap.put("rows", "10");
        paramMap.put("sort", "goods_id");
        paramMap.put("order", "asc");
        paramMap.put("stype","0");
        paramMap.put("keyword",goodsname);
        String urlParam = HttpUtil.getQueryParam(paramMap);
        System.out.println("host+path+urlParam------"+host+path+urlParam);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println(responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray goodsnameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlCountStr = "SELECT count(*) from es_goods WHERE disabled = '0' AND name LIKE '%" +goodsname +"%'";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT name from es_goods WHERE disabled = '0' and name LIKE '%"+goodsname+"%' LIMIT 10";
        String[] dbgoodsnameList = new String[10];
        for (int i = 0; i < dbgoodsnameList.length; i++) {
            dbgoodsnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        //断言
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <goodsnameArray.size() ; i++) {
            Assert.assertEquals(goodsnameArray.get(i),dbgoodsnameList[i]);
        }
    }

    @Test(description = "根据类别查询商品")
    public void testSearchGoodsByCat() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        String catId = "4";
        paramMap.put("page", "1");
        paramMap.put("rows", "10");
        paramMap.put("sort", "goods_id");
        paramMap.put("order", "desc");
        paramMap.put("stype","1");
        paramMap.put("catid",catId);
        String urlParam = HttpUtil.getQueryParam(paramMap);
        System.out.println("host+path+urlParam------"+host+path+urlParam);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println(responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray goodsnameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlCountStr = "SELECT count(*) from es_goods WHERE disabled = '0' and cat_id in (SELECT cat_id from es_goods_cat WHERE cat_path LIKE '%|"+ catId + "|%')";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT name from es_goods WHERE disabled = '0' and cat_id in (SELECT cat_id from es_goods_cat WHERE cat_path LIKE '%|" + catId + "|%') ORDER BY goods_id desc";
        String[] dbgoodsnameList = new String[10];
        for (int i = 0; i < dbgoodsnameList.length; i++) {
            dbgoodsnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        //断言
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <goodsnameArray.size() ; i++) {
            Assert.assertEquals(goodsnameArray.get(i),dbgoodsnameList[i]);
        }
    }
}
