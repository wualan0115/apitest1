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

public class GoodsStoreListTest extends BaseTest {
    String path = "javamall/shop/admin/goodsStore!listGoodsStoreJson.do";

    //@Test(description = "库存列表")
    public void testGoodsStoreList() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("page", "1");
        paramMap.put("rows", "400");
        paramMap.put("sort", "goods_id");
        paramMap.put("order", "asc");
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("GoodsStoreList响应结果---------------"+responseJson);
    }

    @Test(description = "库存列表根据名称或者编号搜索")
    public void testSearchStore() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        String keyword = "o";
        paramMap.put("page", "1");
        paramMap.put("rows", "400");
        paramMap.put("sort", "goods_id");
        paramMap.put("order", "asc");
        paramMap.put("stype","0");
        paramMap.put("keyword",keyword);
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("GoodsStoreList响应结果---------------"+responseJson);
        int total = JSONPathUtil.extract(responseJson,"$.total");
        JSONArray nameArray = JSONPathUtil.extract(responseJson,"$..name");
        String sqlcountStr = "SELECT count(*) from es_goods WHERE name like '%"+keyword+"%' or sn like '%"+keyword+"%'";
        String sqlStr = "SELECT name from es_goods WHERE name like '%"+keyword+"%' or sn like '%"+keyword+"%'";
        String dbCount = JdbcDataUtil.getData(conn,sqlcountStr,0,0);
        String[] dbgoodsnameList = new String[JdbcDataUtil.getData(conn,sqlStr).length];
        for (int i = 0; i < dbgoodsnameList.length; i++) {
            dbgoodsnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <dbgoodsnameList.length ; i++) {
            Assert.assertEquals(nameArray.get(i),dbgoodsnameList[i]);
        }
    }
}
