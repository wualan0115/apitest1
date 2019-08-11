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

public class GoodsDeleteTest extends BaseTest {
    String deletePath ="javamall/shop/admin/goods!delete.do";
    String revertPath = "javamall/shop/admin/goods!revert.do";
    String cleanPath = "javamall/shop/admin/goods!clean.do";
    String goods_id;

    @Test(description = "删除商品")
    public void testDeleteGoods() throws IOException {
        HttpPost postRequest = new HttpPost(host+deletePath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        goods_id = Props.get("GoodsId");
        //goods_id = "307";
        paramList.add(new BasicNameValuePair("goods_id",goods_id));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("DeleteGoods响应结果--------------"+responseJson);
        //断言
        String sqlStr = "select disabled from es_goods where goods_id = '" + goods_id + "'";
        String disabled = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        Assert.assertEquals(disabled, "1");
    }

    //@Test(description = "还原商品",dependsOnMethods = "testDeleteGoods")
    public void testRevertGoods() throws IOException {
        HttpPost postRequest = new HttpPost(host+revertPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        goods_id = Props.get("GoodsId");
        paramList.add(new BasicNameValuePair("goods_id",goods_id));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("DeleteGoods响应结果--------------"+responseJson);
        //断言
        String sqlStr = "select disabled from es_goods where goods_id = '" + goods_id + "'";
        String disabled = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        Assert.assertEquals(disabled, "0");
    }

    //@Test(dependsOnMethods = "testRevertGoods",description = "清除商品")
    public void testCleanGoods() throws IOException {
        HttpPost postRequest = new HttpPost(host+cleanPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        goods_id = Props.get("GoodsId");
        paramList.add(new BasicNameValuePair("goods_id",goods_id));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("DeleteGoods响应结果--------------"+responseJson);
        //断言
        String sqlStr = "select disabled from es_goods where goods_id = '" + goods_id + "'";
        Assert.assertNull(JdbcDataUtil.getData(conn,sqlStr));
    }

}
