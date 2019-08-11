import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JdbcDataUtil;

import java.io.IOException;

public class CatListTest extends BaseTest {
    String path = "javamall/shop/admin/cat!listJson.do";

    @Test
    public void testCatList() throws IOException {
        HttpGet getRequest = new HttpGet(host+path);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("catgoryList响应结果----"+responseJson);
        JSONArray root = JSONArray.parseArray(responseJson);
        String text =((JSONObject)root.get(0)).getString("text");
        System.out.println("text----------"+text);

        String[] textArray = new String[root.size()];
        for (int i = 0; i <textArray.length ; i++) {
            textArray[i] = ((JSONObject)root.get(i)).getString("text");
        }
        //打印textArray内容
        for (int i = 0; i <textArray.length ; i++) {
            System.out.println(textArray[i]);
        }
        //查询数据库
        String sqlStr = "SELECT name from es_goods_cat WHERE parent_id = 0 ORDER BY cat_order asc, cat_id asc";
        String[] dbnameList = new String[JdbcDataUtil.getData(conn,sqlStr).length];
        for (int i = 0; i <dbnameList.length ; i++) {
            dbnameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
        }
        for (int i = 0; i < textArray.length ; i++) {
            Assert.assertEquals(textArray[i],dbnameList[i]);
        }
    }
}
