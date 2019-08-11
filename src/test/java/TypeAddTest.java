import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JdbcDataUtil;
import util.Props;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TypeAddTest extends BaseTest {
    String path = "javamall/shop/admin/type!checkname.do";
    Props props;

    @Test(description = "添加类型")
    public void testAddType() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        String typeName = RandomStringUtils.random(4,"去玩儿推欧赔离开家和规范的撒的自行车展现出把你们");
        paramList.add(new BasicNameValuePair("goodsType.name", typeName));
        paramList.add(new BasicNameValuePair("goodsType.have_prop", "1"));
        paramList.add(new BasicNameValuePair("goodsType.have_parm", "1"));
        paramList.add(new BasicNameValuePair("goodsType.join_brand", "1"));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AddGoodsType响应结果-----------"+responseJson);
        String sqlStr = "SELECT type_id,have_prop,have_parm,join_brand from es_goods_type WHERE name = '"+typeName+"'ORDER BY type_id desc";
        String dbtypeid =JdbcDataUtil.getData(conn,sqlStr,0,0);
        Props.put("typeId",dbtypeid);
        String dbhaveprop = JdbcDataUtil.getData(conn,sqlStr,0,1);
        String dbhaveparm = JdbcDataUtil.getData(conn,sqlStr,0,2);
        String dbjoinbrand = JdbcDataUtil.getData(conn,sqlStr,0,3);
        Assert.assertEquals(dbhaveprop,"1");
        Assert.assertEquals(dbhaveparm,"1");
        Assert.assertEquals(dbjoinbrand,"1");

    }
}
