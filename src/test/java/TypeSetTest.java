import net.minidev.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TypeSetTest extends BaseTest {
    String propsPath = "javamall/shop/admin/type!saveProps.do";
    String paramPath = "javamall/shop/admin/type!saveParams.do";
    String brandPath = "javamall/shop/admin/type!saveBrand.do";
    Props props;
    String type_id;

    @Test(description = "属性")
    public void testSaveProps() throws IOException {
        String name = RandomStringUtils.randomAlphabetic(3, 8);
        String type = RandomStringUtils.random(1, "12345");
        String options = RandomStringUtils.randomAlphabetic(1)+","+RandomStringUtils.randomAlphabetic(1)+","+RandomStringUtils.randomAlphabetic(1);
        System.out.println("name-----"+name+"----type-------"+type+"----------options-----"+options);
        String propsOriginal = "[\r\n" +
                "    {\r\n" +
                "        \"status\":\"P\",\r\n" +
                "        \"name\":\"taste\",\r\n" +
                "        \"type\":\"3\",\r\n" +
                "        \"options\":\"sour,sweet,bitter,hot\",\r\n" +
                "        \"unit\":\"\",\r\n" +
                "        \"required\":\"0\",\r\n" +
                "        \"datatype\":\"\"\r\n" +
                "    }\r\n" +
                "]";
        String propsJson = "[\r\n" +
                "    {\r\n" +
                "        \"status\":\"P\",\r\n" +
                "        \"name\":\""+name+"\",\r\n" +
                "        \"type\":\""+type+"\",\r\n" +
                "        \"options\":\""+options+"\",\r\n" +
                "        \"unit\":\"\",\r\n" +
                "        \"required\":\"0\",\r\n" +
                "        \"datatype\":\"\"\r\n" +
                "    }\r\n" +
                "]";

        String props = "[{\"status\":\"P\",\"name\":\""+name+"\",\"type\":\""+type+"\",\"options\":\""+options+"\",\"unit\":\"\",\"required\":\"0\",\"datatype\":\"\"}]";
        System.out.println("props------------"+propsJson);
        HttpPost postRequest = new HttpPost(host+propsPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //String type_id ="65";
        type_id = Props.get("typeId");
        System.out.println("type_id-------------"+type_id);
        paramList.add(new BasicNameValuePair("typeId", type_id));
        paramList.add(new BasicNameValuePair("inserted", propsJson));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SaveProps响应结果---------"+responseJson);
        //数据库断言
        String sqlStr = "SELECT props from es_goods_type WHERE type_id = '"+type_id +"'";
        String db_props = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        JSONArray db_options = JSONPathUtil.extract(db_props, "$..options");
        Assert.assertEquals(db_options.get(db_options.size()-1),options);

    }

    @Test(description = "修改参数")
    public void testSaveParam() throws IOException {
        HttpPost postRequest = new HttpPost(host+paramPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //String type_id = "65";
        //paramList.add(new BasicNameValuePair("typeId", type_id));
        //paramList.add(new BasicNameValuePair("otherType", "3"));
        type_id = Props.get("typeId");
        paramList.add(new BasicNameValuePair("paramnum", "2"));
        String groupnames = RandomStringUtils.randomAlphabetic(3);
        String paramnames1 = RandomStringUtils.randomAlphabetic(6);
        String paramnames2 = RandomStringUtils.randomAlphabetic(6);
        paramList.add(new BasicNameValuePair("groupnames", groupnames));
        paramList.add(new BasicNameValuePair("paramnames", paramnames1));
        paramList.add(new BasicNameValuePair("paramnames", paramnames2));
        paramList.add(new BasicNameValuePair("typeId", type_id));
        System.out.println("paramList---------------"+paramList);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        System.out.println("entity--------------------"+entity);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SaveParams响应结果---------------"+responseJson);
        //数据库断言
        String sqlStr = "SELECT params from es_goods_type WHERE type_id = '"+type_id +"'";
        String db_params = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        Object db_names = JSONPathUtil.extract(db_params, "$..name");
        System.out.println("db_names"+db_names);
        Assert.assertEquals(db_names.toString(), "[\""+groupnames +"\",\""+paramnames1 +"\",\""+paramnames2+"\"]");
    }

    @Test
    public void testSaveBrand() throws IOException {
        HttpPost postRequest = new HttpPost(host+brandPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //String type_id = "65";
        type_id = Props.get("typeId");
        int chhoose_brands1 = new Random().nextInt(100)+1;
        int chhoose_brands2 = new Random().nextInt(100)+1;
        int brandlist = Math.max(chhoose_brands1, chhoose_brands2);
        System.out.println(chhoose_brands1+"------"+chhoose_brands2+"----------"+brandlist);
        paramList.add(new BasicNameValuePair("brandlist", String.valueOf(brandlist)));
        paramList.add(new BasicNameValuePair("chhoose_brands", String.valueOf(chhoose_brands1)));
        paramList.add(new BasicNameValuePair("chhoose_brands", String.valueOf(chhoose_brands2)));
        paramList.add(new BasicNameValuePair("typeId", type_id));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SaveBrand响应结果-------------------"+responseJson);
        //数据库断言
        String sqlStr = "SELECT brand_id from es_type_brand WHERE type_id = '" +type_id+"'";
        String brandId1 = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        String brandId2 = JdbcDataUtil.getData(conn, sqlStr, 1, 0);
        Assert.assertEquals(Math.max(chhoose_brands1, chhoose_brands2), Math.max(Integer.parseInt(brandId1), Integer.parseInt(brandId2)));
        Assert.assertEquals(Math.min(chhoose_brands1, chhoose_brands2), Math.min(Integer.parseInt(brandId1), Integer.parseInt(brandId2)));
    }
}
