import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class UserEditTest extends BaseTest {
    String path = "javamall/shop/admin/member!saveEditMember.do";
    String member_id;

    //@Test(description = "修改会员老方法",dataProvider = "region")
    public void testEditMember(String province_id,String province,String city_id,String city,String region_id,String region) throws ClientProtocolException, IOException, IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        //1.新建名值对的列表，放多个参数
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //2.paramList添加参数
        String member_id =Props.get("userMemberId");
        paramList.add(new BasicNameValuePair("member.member_id", member_id));
        paramList.add(new BasicNameValuePair("member.password", RandomStringUtils.random(6, "abcdefghijklmn123456")));
        paramList.add(new BasicNameValuePair("birthday", TimeUtil.getRandomDate()));
        paramList.add(new BasicNameValuePair("member.email", RandomStringUtils.randomAlphabetic(4)+"@qq.com"));
        paramList.add(new BasicNameValuePair("member.sex", RandomStringUtils.random(1,"01")));
        paramList.add(new BasicNameValuePair("member.name", RandomStringUtils.randomAlphabetic(5)));
        paramList.add(new BasicNameValuePair("member.tel", RandomStringUtils.randomNumeric(8)));
        paramList.add(new BasicNameValuePair("member.mobile", "138"+RandomStringUtils.randomNumeric(8)));
        paramList.add(new BasicNameValuePair("member.lv_id", RandomStringUtils.random(1, "1234")));
        paramList.add(new BasicNameValuePair("province_id", province_id));
        String provinceEncode = new String(province.getBytes("UTF-8"),"ISO-8859-1");
        paramList.add(new BasicNameValuePair("province", provinceEncode));
        paramList.add(new BasicNameValuePair("city_id", city_id));
        String cityEncode = new String(city.getBytes("UTF-8"),"ISO-8859-1");
        paramList.add(new BasicNameValuePair("city", cityEncode));
        paramList.add(new BasicNameValuePair("region_id", region_id));
        String regionEncode = new String(region.getBytes("UTF-8"),"ISO-8859-1");
        paramList.add(new BasicNameValuePair("region", regionEncode));
        paramList.add(new BasicNameValuePair("member.zip", RandomStringUtils.randomNumeric(6)));
        //3.基于参数名值对的列表生成一个表单实体对象
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        //4.把表单对象传送给post请求
        postRequest.setEntity(entity);
        //5.httpclient发起post请求
        HttpResponse response = httpClient.execute(postRequest);
        //6.获取响应结果
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("EditMember响应结果--------------"+responseJson);
        //断言
        int result = JSONPathUtil.extract(responseJson, "$.result");
        assertEquals(result, 1);

    }

    @Test(description = "修改会员",dataProvider = "region")
    public void testEditMember1(String province_id,String province,String city_id,String city,String region_id,String region) throws ClientProtocolException, IOException, IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        member_id =Props.get("userMemberId");

        String password = RandomStringUtils.randomAlphanumeric(6);
        String birthday = TimeUtil.getRandomDate();
        String email = RandomStringUtils.randomAlphabetic(4)+"@qq.com";
        String sex = RandomStringUtils.random(1,"01");
        String name = RandomStringUtils.randomAlphabetic(5);
        String tel = RandomStringUtils.randomNumeric(8);
        String mobile = "138"+RandomStringUtils.randomNumeric(8);
        String lv_id = RandomStringUtils.random(1, "1234");
        String zip = RandomStringUtils.randomNumeric(6);
        paramList.add(new BasicNameValuePair("member.member_id", member_id));
        paramList.add(new BasicNameValuePair("member.password", password));
        paramList.add(new BasicNameValuePair("birthday",birthday ));
        paramList.add(new BasicNameValuePair("member.email", email));
        paramList.add(new BasicNameValuePair("member.sex", sex));
        paramList.add(new BasicNameValuePair("member.name", name));
        paramList.add(new BasicNameValuePair("member.tel",tel ));
        paramList.add(new BasicNameValuePair("member.mobile", mobile));
        paramList.add(new BasicNameValuePair("member.lv_id", lv_id));
        paramList.add(new BasicNameValuePair("province_id", province_id));
        paramList.add(new BasicNameValuePair("province", province));
        paramList.add(new BasicNameValuePair("city_id", city_id));
        paramList.add(new BasicNameValuePair("city", city));
        paramList.add(new BasicNameValuePair("region_id", region_id));
        paramList.add(new BasicNameValuePair("region", region));
        paramList.add(new BasicNameValuePair("member.zip",zip ));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("EditMember响应结果--------------"+responseJson);
        //断言
        String sqlStr = "SELECT password,email,sex,name,tel,mobile,lv_id,province_id,province,city_id,city,region_id,region,zip FROM es_member WHERE member_id = "+member_id;
        String dbpassword=JdbcDataUtil.getData(conn,sqlStr,0,0);
        String dbemail=JdbcDataUtil.getData(conn,sqlStr,0,1);
        String dbsex=JdbcDataUtil.getData(conn,sqlStr,0,2);
        String dbname=JdbcDataUtil.getData(conn,sqlStr,0,3);
        String dbtel=JdbcDataUtil.getData(conn,sqlStr,0,4);
        String dbmobile=JdbcDataUtil.getData(conn,sqlStr,0,5);
        String dblvid=JdbcDataUtil.getData(conn,sqlStr,0,6);
        String dbprovinceid=JdbcDataUtil.getData(conn,sqlStr,0,7);
        //String dbprovince=JdbcDataUtil.getData(conn,sqlStr,0,8);
        String dbcityid=JdbcDataUtil.getData(conn,sqlStr,0,9);
        //String dbcity=JdbcDataUtil.getData(conn,sqlStr,0,10);
        String dbregionid=JdbcDataUtil.getData(conn,sqlStr,0,11);
        //String dbregion=JdbcDataUtil.getData(conn,sqlStr,0,12);
        String dbzip=JdbcDataUtil.getData(conn,sqlStr,0,13);
        Assert.assertEquals(EncryptionUtil.md5(password),dbpassword);
        Assert.assertEquals(email,dbemail);
        Assert.assertEquals(sex,dbsex);
        Assert.assertEquals(name,dbname);
        Assert.assertEquals(tel,dbtel);
        Assert.assertEquals(mobile,dbmobile);
        Assert.assertEquals(lv_id,dblvid);
        Assert.assertEquals(province_id,dbprovinceid);
        Assert.assertEquals(city_id,dbcityid);
        Assert.assertEquals(region_id,dbregionid);
        Assert.assertEquals(zip,dbzip);
        int result = JSONPathUtil.extract(responseJson, "$.result");
        assertEquals(result, 1);

    }

    @DataProvider(name = "region")
    public Object[][] getRegionData(){
        return new Object[][] {
                {"1","北京","40","怀柔区","458","怀柔区"},
                {"2","上海","56","静安区","474","静安区"}
        };
    }

    @Test(description = "缺少参数")
    public void testDismissParam() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        member_id =Props.get("userMemberId");
        String password = RandomStringUtils.randomAlphanumeric(6);
        String email = RandomStringUtils.randomAlphabetic(4)+"@qq.com";
        paramList.add(new BasicNameValuePair("member.member_id", member_id));
        //paramList.add(new BasicNameValuePair("member.password",password));
        paramList.add(new BasicNameValuePair("member.email", email));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("EditMember响应结果--------------"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String msg = JSONPathUtil.extract(responseJson,"$.message");
        Assert.assertEquals(result,0);
        Assert.assertEquals(msg,"修改失败");
    }

    @Test(description = "特殊字符")
    public void testSpeChar() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //member_id =Props.get("userMemberId");
        String password = RandomStringUtils.randomAlphanumeric(6);
        String email = RandomStringUtils.randomAlphabetic(4)+"@qq.com";
        paramList.add(new BasicNameValuePair("member.member_id", "256"));
        paramList.add(new BasicNameValuePair("member.password",password));
        paramList.add(new BasicNameValuePair("member.email", email));
        paramList.add(new BasicNameValuePair("member.tel","1234abc"));
        paramList.add(new BasicNameValuePair("member.mobile","133abcd"));
        paramList.add(new BasicNameValuePair("member.sex", "9"));
        paramList.add(new BasicNameValuePair("province_id", "109"));
        paramList.add(new BasicNameValuePair("member.name", "你!@#%&*(){}"));
        paramList.add(new BasicNameValuePair("birthday", "ghtrhahtrsj"));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("EditMember响应结果--------------"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String msg = JSONPathUtil.extract(responseJson,"$.message");
        //Assert.assertEquals(result,0);
        //Assert.assertEquals(msg,"修改失败");
    }

}
