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
import java.util.HashMap;
import java.util.Map;

public class BonusListTest extends BaseTest {
    String path = "javamall/shop/admin/bonus-type!listJson.do";

    @Test(description = "优惠券列表")
    public void testBonusList() throws IOException {
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
        JSONArray recognitionArray = JSONPathUtil.extract(responseJson,"$..recognition");
        JSONArray typenameArray = JSONPathUtil.extract(responseJson,"$..type_name");
        String sqlCountStr = "SELECT count(*) from es_bonus_type ";
        String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
        String sqlStr = "SELECT type_name,recognition from es_bonus_type ORDER BY type_id desc";
        String[] dbtypenameList = new String[typenameArray.size()];
        String[] dbrecognitionList = new String[typenameArray.size()];
        for (int i = 0; i < dbtypenameList.length; i++) {
            dbtypenameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        for (int i = 0; i <dbrecognitionList.length ; i++) {
            dbrecognitionList[i] = JdbcDataUtil.getData(conn,sqlStr,i,1);
        }
        Assert.assertEquals(total,Integer.parseInt(dbCount));
        for (int i = 0; i <typenameArray.size() ; i++) {
            Assert.assertEquals(typenameArray.get(i),dbtypenameList[i]);
        }
        for (int i = 0; i <recognitionArray.size() ; i++) {
            Assert.assertEquals(recognitionArray.get(i),dbrecognitionList[i]);
        }

    }
}
