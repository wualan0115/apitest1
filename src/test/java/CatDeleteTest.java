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
import java.util.ArrayList;
import java.util.List;

public class CatDeleteTest extends BaseTest {
    String path = "javamall/shop/admin/cat!delete.do";
    String catId;
    String subCatId;

    @Test(description = "有子类的父类不能删除")
    public void testDeleteCat() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        catId = Props.get("catId");
        //catId = "136";
        paramList.add(new BasicNameValuePair("cat_id",catId));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("删除有子类的父类，响应结果-------"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String msg = JSONPathUtil.extract(responseJson,"$.message");
        Assert.assertEquals(result,0);
        Assert.assertEquals(msg,"此类别下存在子类别不能删除!");

    }

    @Test(description = "删除子类",dependsOnMethods = "testDeleteCat")
    public void testDeleteSubCat() throws IOException {
        subCatId = Props.get("subCatId");
        //subCatId = "135";
        HttpPost postRequest = new HttpPost(host+path+"?ajax=yes&cat_id="+subCatId);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("cat_id",subCatId));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("paramList-----"+paramList);
        System.out.println("删除子类，响应结果-------"+responseJson);
        //断言
        String sqlStr = "SELECT * from es_goods_cat WHERE cat_id = '"+subCatId+"'";
        Assert.assertNull(JdbcDataUtil.getData(conn,sqlStr));
    }
}
