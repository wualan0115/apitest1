import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JSONPathUtil;
import util.JdbcDataUtil;
import util.Props;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

public class GoodsAddTest extends BaseTest {
    String path = "javamall/shop/admin/goods!saveAdd.do";
    String goodssn;
    Props props;

    @Test(description = "新增商品")
    public void testAddGoods() throws IOException {
        String sqlcattypestr = "SELECT cat_id,type_id from es_goods_cat";
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        int random = new Random().nextInt(JdbcDataUtil.getData(conn,sqlcattypestr).length);
        String cat_id = JdbcDataUtil.getData(conn,sqlcattypestr,random,0);
        String type_id = JdbcDataUtil.getData(conn,sqlcattypestr,random,1);
        String goodsname = RandomStringUtils.random(4, "测试商品名称ghrqwuioplxmna");
        goodssn = "G"+RandomStringUtils.randomNumeric(4);
        DecimalFormat df = new DecimalFormat("0.00");
        String goodsmktprice = df.format(new Random().nextDouble()*100.00);
        String goodspoint = RandomStringUtils.randomNumeric(1,3);
        String goodsintro = RandomStringUtils.randomAlphanumeric(20);
        paramList.add(new BasicNameValuePair("goods.cat_id", cat_id));
        paramList.add(new BasicNameValuePair("goods.type_id", type_id));
        paramList.add(new BasicNameValuePair("goods.name", goodsname));
        paramList.add(new BasicNameValuePair("goods.sn", goodssn));
        paramList.add(new BasicNameValuePair("goods.market_enable", "1"));
        paramList.add(new BasicNameValuePair("goods.mktprice",goodsmktprice));
        paramList.add(new BasicNameValuePair("goods.point", goodspoint));
        paramList.add(new BasicNameValuePair("goods.intro", goodsintro));
        System.out.println("goodsname---"+goodsname+"--goodssn--"+goodssn);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AddGoods响应结果--------------"+responseJson);
        String sqlStr = "SELECT sn,mktprice,point,goods_id from es_goods WHERE name = '"+ goodsname + "' ORDER BY goods_id desc";
        String dbsn = JdbcDataUtil.getData(conn,sqlStr,0,0);
        String dbmktprice = JdbcDataUtil.getData(conn,sqlStr,0,1);
        String dbpoint = JdbcDataUtil.getData(conn,sqlStr,0,2);
        String dbgoodsid = JdbcDataUtil.getData(conn,sqlStr,0,3);
        props.put("GoodsId",dbgoodsid);
        Assert.assertEquals(goodssn,dbsn);
        Assert.assertEquals(goodsmktprice,dbmktprice);
        Assert.assertEquals(goodspoint,dbpoint);

    }

    @Test(dependsOnMethods = "testAddGoods",description = "添加重复商品")
    public void testAddDuplicateGoods() throws IOException {
        String sqlcattypestr = "SELECT cat_id,type_id from es_goods_cat";
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        int random = new Random().nextInt(JdbcDataUtil.getData(conn,sqlcattypestr).length);
        String cat_id = JdbcDataUtil.getData(conn,sqlcattypestr,random,0);
        String type_id = JdbcDataUtil.getData(conn,sqlcattypestr,random,1);
        String goodsname = RandomStringUtils.random(4, "测试商品名称ghrqwuioplxmna");
        //goodssn = "G"+RandomStringUtils.randomNumeric(4);
        DecimalFormat df = new DecimalFormat("0.00");
        String goodsmktprice = df.format(new Random().nextDouble()*100.00);
        String goodspoint = RandomStringUtils.randomNumeric(1,3);
        String goodsintro = RandomStringUtils.randomAlphanumeric(20);
        paramList.add(new BasicNameValuePair("goods.cat_id", cat_id));
        paramList.add(new BasicNameValuePair("goods.type_id", type_id));
        paramList.add(new BasicNameValuePair("goods.name", goodsname));
        paramList.add(new BasicNameValuePair("goods.sn", goodssn));
        paramList.add(new BasicNameValuePair("goods.market_enable", "1"));
        paramList.add(new BasicNameValuePair("goods.mktprice",goodsmktprice));
        paramList.add(new BasicNameValuePair("goods.point", goodspoint));
        paramList.add(new BasicNameValuePair("goods.intro", goodsintro));
        System.out.println("goodsname---"+goodsname+"--goodssn--"+goodssn);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AAddDuplicateGoods响应结果--------------"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String msg = JSONPathUtil.extract(responseJson,"$.message");
        Assert.assertEquals(result,0);
        Assert.assertEquals(msg,"添加商品出错货号["+ goodssn+"]重复");
    }

    @Test(description = "缺少参数")
    public void testDismissParam() throws IOException {
        String sqlcattypestr = "SELECT cat_id,type_id from es_goods_cat";
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        int random = new Random().nextInt(JdbcDataUtil.getData(conn,sqlcattypestr).length);
        String cat_id = JdbcDataUtil.getData(conn,sqlcattypestr,random,0);
        String type_id = JdbcDataUtil.getData(conn,sqlcattypestr,random,1);
        String goodsname = RandomStringUtils.random(4, "测试商品名称ghrqwuioplxmna");
//        goodssn = "G"+RandomStringUtils.randomNumeric(4);
        DecimalFormat df = new DecimalFormat("0.00");
        String goodsmktprice = df.format(new Random().nextDouble()*100.00);
        String goodspoint = RandomStringUtils.randomNumeric(1,3);
        String goodsintro = RandomStringUtils.randomAlphanumeric(20);
//        paramList.add(new BasicNameValuePair("goods.cat_id", cat_id));
//        paramList.add(new BasicNameValuePair("goods.type_id", type_id));
//        paramList.add(new BasicNameValuePair("goods.name", goodsname));
//        paramList.add(new BasicNameValuePair("goods.sn", goodssn));
//        paramList.add(new BasicNameValuePair("goods.market_enable", "1"));
//        paramList.add(new BasicNameValuePair("goods.mktprice",goodsmktprice));
//        paramList.add(new BasicNameValuePair("goods.point", goodspoint));
//        paramList.add(new BasicNameValuePair("goods.intro", goodsintro));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AddGoods响应结果--------------"+responseJson);
    }

}
