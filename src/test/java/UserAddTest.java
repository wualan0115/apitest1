import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class UserAddTest extends BaseTest {
    private String path = "javamall/shop/admin/member!saveMember.do";
    Props props;
    String uname;

    @Test(description = "新增用户正常流程")
    public void testAddUser() throws IOException, ParseException {
        System.out.println("host+path----"+host+path);
        System.out.println("cookieHeader----"+cookieHeader);
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        uname = RandomStringUtils.randomAlphabetic(5);
        paramList.add(new BasicNameValuePair("member.uname", uname));
        String password = RandomStringUtils.randomAlphanumeric(6);
        paramList.add(new BasicNameValuePair("member.password",password));
        String birthday = TimeUtil.getRandomDate();
        //String birthday = "1990-01-01";
        paramList.add(new BasicNameValuePair("birthday",birthday));
        System.out.println("paramList--------"+paramList);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AddUser响应结果----"+responseJson);
        String userId = JSONPathUtil.extract(responseJson,"$.id");
        props.put("userMemberId",userId);

        String sqlStr ="SELECT uname,password,birthday FROM es_member WHERE member_id = "+userId;
        String dbuname = JdbcDataUtil.getData(conn,sqlStr,0,0);
        String dbpassword = JdbcDataUtil.getData(conn,sqlStr,0,1);
        String dbbirthday = JdbcDataUtil.getData(conn,sqlStr,0,2);
        //日期string long
        long dbbirthdaylong = Long.parseLong(dbbirthday);
        Assert.assertEquals(uname,dbuname);
        Assert.assertEquals(EncryptionUtil.md5(password),dbpassword);
        System.out.println("dbbirthday---"+dbbirthday+"--dbbirthdaylong--"+dbbirthdaylong+"--birthday--"+birthday);
        Assert.assertEquals(TimeUtil.getUnixDateByStr(birthday+" 00:00:00"), dbbirthdaylong*1000);
    }

    @Test(description = "添加重复的用户",dependsOnMethods = "testAddUser")
    public void testAddDuplicateUser() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("member.uname",uname));
        paramList.add(new BasicNameValuePair("member.password",RandomStringUtils.randomAlphanumeric(6)));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("addDuplicateUser响应结果----"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String msg = JSONPathUtil.extract(responseJson,"$.message");
        Assert.assertEquals(result,0);
        Assert.assertEquals(msg,"用户名已存在");

    }

    @Test(description = "缺少参数")
    public void testDismissParam() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("member.uname",RandomStringUtils.random(3,"测试缺少参数")));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        System.out.println("缺少参数返回结果---"+response);
        int responseCode=response.getStatusLine().getStatusCode();
        Assert.assertEquals(responseCode,500);
    }
}
