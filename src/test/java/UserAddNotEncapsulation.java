import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.JSONPathUtil;
import util.JdbcDataUtil;
import util.PropertyUtil;
import util.Props;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserAddNotEncapsulation {
    CloseableHttpClient httpClient;
    Properties httpProperties = PropertyUtil.getProperties("/http.properties");
    String host = httpProperties.getProperty("http.webhost");
    String cookieValue = httpProperties.getProperty("http.cookie");
    String path = "javamall/shop/admin/member!saveMember.do";
    Header cookieHeader =new BasicHeader("Cookie","JSESSIONID="+cookieValue);

    Properties dbProperties = PropertyUtil.getProperties("/db.properties");
    String dbUrl = dbProperties.getProperty("jdbc.url");
    String dbUsername = dbProperties.getProperty("jdbc.username");
    String dbPassword = dbProperties.getProperty("jdbc.password");
    Connection conn;

    Props props;


    @BeforeClass
    public void setUp(){
        httpClient = HttpClients.createDefault();
        conn = JdbcDataUtil.getConn(dbUrl,dbUsername,dbPassword);
    }

    @AfterClass
    public void tearDown() throws IOException {
        httpClient.close();
        JdbcDataUtil.closeConn(conn);
    }

    @Test
    public void testAddUser() throws IOException {
        System.out.println("host+path----------"+host+path);
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        String uname = RandomStringUtils.randomAlphabetic(5);
        paramList.add(new BasicNameValuePair("member.uname", uname));
        String password = RandomStringUtils.randomAlphanumeric(6);
        paramList.add(new BasicNameValuePair("member.password",password));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AddUser响应结果----"+responseJson);
        String userId = JSONPathUtil.extract(responseJson,"$.id");
        props.put("userMemberId",userId);

        String sqlStr ="SELECT uname,password FROM es_member WHERE member_id = "+userId;
        String dbuname = JdbcDataUtil.getData(conn,sqlStr,0,0);
        String dbpassword = JdbcDataUtil.getData(conn,sqlStr,0,1);
        Assert.assertEquals(uname,dbuname);



    }
}
