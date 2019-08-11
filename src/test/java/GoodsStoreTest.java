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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GoodsStoreTest extends BaseTest {
    String saveStockPath ="javamall/shop/admin/goodsStore!saveStock.do";
    String goods_id;

    @Test(description = "新商品进货")
    public void testSaveStock() throws IOException {
        HttpPost postRequest = new HttpPost(host+saveStockPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        goods_id = Props.get("GoodsId");
        //进货前查一遍库存
        String sqlStr = "select store from es_product where goods_id = '" + goods_id +"'";
        String storeBefore = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        paramList.add(new BasicNameValuePair("goodsid", goods_id));
        paramList.add(new BasicNameValuePair("storeid", "0"));
        paramList.add(new BasicNameValuePair("depotid", "1"));
        String hd = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", hd));
        paramList.add(new BasicNameValuePair("storeid","0"));
        paramList.add(new BasicNameValuePair("depotid", "2"));
        String cy = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", cy));
        paramList.add(new BasicNameValuePair("storeid", "0"));
        paramList.add(new BasicNameValuePair("depotid", "3"));
        String cp = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", cp));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SaveStock响应结果--------------"+responseJson);
        //数据库断言
        String storeAfter = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        System.out.println(storeBefore+"-------"+hd+"------"+cy+"----------"+cp+"-----------"+storeAfter);
        Assert.assertEquals(Integer.parseInt(storeBefore)+Integer.parseInt(hd)+Integer.parseInt(cy)+Integer.parseInt(cp), Integer.parseInt(storeAfter));

    }

    @Test(description = "已有库存商品进货")
    public void testOldGoodsSaveStock() throws IOException {
        HttpPost postRequest = new HttpPost(host+saveStockPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        String old_goods_id = "327";
        //进货前查一遍库存
        String sqlStr = "select store from es_product where goods_id = '" + old_goods_id +"'";
        String storeBefore = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        paramList.add(new BasicNameValuePair("goodsid", old_goods_id));
        paramList.add(new BasicNameValuePair("storeid", "112"));
        paramList.add(new BasicNameValuePair("depotid", "1"));
        String hd = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", hd));
        paramList.add(new BasicNameValuePair("storeid","113"));
        paramList.add(new BasicNameValuePair("depotid", "2"));
        String cy = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", cy));
        paramList.add(new BasicNameValuePair("storeid", "114"));
        paramList.add(new BasicNameValuePair("depotid", "3"));
        String cp = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", cp));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SaveStock响应结果--------------"+responseJson);
        //数据库断言
        String storeAfter = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        System.out.println(storeBefore+"-------"+hd+"------"+cy+"----------"+cp+"-----------"+storeAfter);
        Assert.assertEquals(Integer.parseInt(storeBefore)+Integer.parseInt(hd)+Integer.parseInt(cy)+Integer.parseInt(cp), Integer.parseInt(storeAfter));

    }

    @Test(description = "库存维护",dependsOnMethods = "testSaveStock")
    public void testMaintanceStore() throws IOException {
        HttpPost postRequest = new HttpPost(host+saveStockPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        goods_id = Props.get("GoodsId");
        //查询商品的storeid,depotid,store
        String sqlStr = "SELECT storeid,depotid, store from es_product_store where goodsid = '" + goods_id +"'";
        Object[][] storeBeforeObj = JdbcDataUtil.getData(conn,sqlStr);
        int hdBefore = Integer.parseInt(storeBeforeObj[0][2].toString());
        int cyBefore = Integer.parseInt(storeBeforeObj[1][2].toString());
        int cpBefore = Integer.parseInt(storeBeforeObj[2][2].toString());
        paramList.add(new BasicNameValuePair("goodsid", goods_id));
        paramList.add(new BasicNameValuePair("storeid", storeBeforeObj[0][0].toString()));
        paramList.add(new BasicNameValuePair("depotid", storeBeforeObj[0][1].toString()));
        String hd = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", hd));
        paramList.add(new BasicNameValuePair("storeid",storeBeforeObj[1][0].toString()));
        paramList.add(new BasicNameValuePair("depotid",storeBeforeObj[1][1].toString()));
        String cy = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", cy));
        paramList.add(new BasicNameValuePair("storeid", storeBeforeObj[2][0].toString()));
        paramList.add(new BasicNameValuePair("depotid", storeBeforeObj[2][1].toString()));
        String cp = RandomStringUtils.randomNumeric(1, 3);
        paramList.add(new BasicNameValuePair("storeNum", cp));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SaveStock响应结果--------------"+responseJson);
        //断言
        Object[][] storeAfterObj = JdbcDataUtil.getData(conn, sqlStr);
        int hdAfter = Integer.parseInt(storeAfterObj[0][2].toString());
        int cyAfter = Integer.parseInt(storeAfterObj[1][2].toString());
        int cpAfter = Integer.parseInt(storeAfterObj[2][2].toString());
        System.out.println(hdAfter+"-----"+cyAfter+"--------"+cpAfter);
        Assert.assertEquals(hdBefore+Integer.parseInt(hd), hdAfter);
        Assert.assertEquals(cyBefore+Integer.parseInt(cy), cyAfter);
        Assert.assertEquals(cpBefore+Integer.parseInt(cp), cpAfter);

    }
}
