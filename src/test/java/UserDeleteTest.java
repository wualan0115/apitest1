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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UserDeleteTest extends BaseTest {
    String path = "javamall/shop/admin/member!delete.do";
    String member_id;

    @Test(description = "正常删除")
    public void testDeleteUser() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        member_id = Props.get("userMemberId");
        //String member_id = "269";
        paramList.add(new BasicNameValuePair("member_id", member_id));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("DeleteUser响应结果--------------"+responseJson);
        String sqlStr = "SELECT * from es_member WHERE member_id = '" + member_id + "'";
        Assert.assertNull(JdbcDataUtil.getData(conn,sqlStr));
    }

    @Test(description = "删除已经不存在的数据",dependsOnMethods = "testDeleteUser")
    public void testDeleteNotExistUser() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        member_id = Props.get("userMemberId");
        //String member_id = "269";
        paramList.add(new BasicNameValuePair("member_id", member_id));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("DeleteUser响应结果--------------"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        Assert.assertEquals(result,0);
    }
}
