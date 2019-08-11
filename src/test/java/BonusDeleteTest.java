import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.HttpUtil;
import util.JdbcDataUtil;
import util.Props;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BonusDeleteTest extends BaseTest {
    String path = "javamall/shop/admin/bonus-type!delete.do";
    String typeId;

    @Test
    public void testDeleteBonus() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        //String type_id = "21";
        typeId = Props.get("bonustypeId");
        paramMap.put("type_id", typeId);
        paramMap.put("ajax", "yes");
        String urlParam = HttpUtil.getQueryParam(paramMap);
        System.out.println(urlParam);
        HttpGet getRequest = new HttpGet(host+path+urlParam);
        getRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("DeleteBonus响应结果-----------"+responseJson);
        //断言 SELECT * from es_bonus_type WHERE type_id = '6'
        //SELECT * from es_member_bonus WHERE bonus_type_id = '6'
        String sqlStr1 = "SELECT * from es_bonus_type WHERE type_id = '"+typeId +"'";
        String sqlStr2 = "SELECT * from es_member_bonus WHERE bonus_type_id = '" +typeId+"'";
        Object[][] es_bonus_type= JdbcDataUtil.getData(conn, sqlStr1);
        Object[][] es_member_bonus=JdbcDataUtil.getData(conn, sqlStr2);
        Assert.assertNull(es_bonus_type);
        Assert.assertNull(es_member_bonus);
    }
}
