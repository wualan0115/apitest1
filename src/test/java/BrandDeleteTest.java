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

public class BrandDeleteTest extends BaseTest {
    String path = "javamall/shop/admin/brand!delete.do";
    Props props;

    @Test(description = "删除品牌")
    public void testDeleteBrand() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        String brand_id = Props.get("brandId");
        paramList.add(new BasicNameValuePair("brand_id", brand_id));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson= EntityUtils.toString(response.getEntity());
        System.out.println("deleteBrand响应结果-----------"+responseJson);
        //数据库断言  select disabled from es_brand where brand_id = '';
        String sqlStr = "select disabled from es_brand where brand_id = '"+brand_id +"'";
        String disable= JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        Assert.assertEquals(disable, "1");
    }
}
