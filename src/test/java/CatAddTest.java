import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JSONPathUtil;
import util.JdbcDataUtil;
import util.Props;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

public class CatAddTest extends BaseTest {
    String path ="javamall/shop/admin/cat!saveAdd.do";
    Props props;
    String catId;
    String subCatId;

    @Test(description = "添加分类")
    public void testAddCat() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        String catName = RandomStringUtils.random(4,"去问人体热一热通用图有偶评论开会就开会爱上自行车不能盲目立刻解决");
        File file = new File("C:\\Users\\liuxing\\Pictures\\Saved Pictures\\coffee1.jpg");
        //获取type_id
        String sqltypeidStr = "SELECT type_id from es_goods_type";
        JdbcDataUtil.getData(conn,sqltypeidStr);
        int random = new Random().nextInt(JdbcDataUtil.getData(conn,sqltypeidStr).length);
        String typeId = JdbcDataUtil.getData(conn,sqltypeidStr,random,0);
        //创建表单
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(Charset.forName("utf-8"));
        ContentType strContent = ContentType.create("text/plain",Charset.forName("UTF-8"));
        builder.addTextBody("cat.name",catName,strContent);
        builder.addTextBody("cattype","1");
        builder.addTextBody("cat.type_id",typeId);
        builder.addTextBody("cat.cat_order","0");
        builder.addTextBody("cat.list_show","1");
        builder.addBinaryBody("image",file);
        Request request = Request.Post(host+path);
        request.addHeader(cookieHeader);
        String responseJson = request.body(builder.build()).execute().returnContent().asString();
//        Response response =request.body(builder.setCharset(Charset.forName("utf-8")).build()).execute();
//        String responseJson =  response.returnContent().asString();
        System.out.println(typeId+"----------"+catName);
        System.out.println("AddCategory响应结果-----"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String sqlStr = "SELECT type_id,cat_id from es_goods_cat WHERE name = '"+catName+ "' ORDER BY cat_id desc";
        String dbtypeid = JdbcDataUtil.getData(conn,sqlStr,0,0);
        catId = JdbcDataUtil.getData(conn,sqlStr,0,1);
        Props.put("catId",catId);
        Assert.assertEquals(result,1);
        Assert.assertEquals(typeId,dbtypeid);
    }

    @Test(description = "添加子类",dependsOnMethods = "testAddCat")
    public void testAddsubCat() throws IOException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        String catName = RandomStringUtils.random(4,"去问人体热一热通用图有偶评论开会就开会爱上自行车不能盲目立刻解决");
        File file = new File("C:\\Users\\liuxing\\Pictures\\Saved Pictures\\coffee1.jpg");
        //获取type_id
        String sqltypeidStr = "SELECT type_id from es_goods_type";
        JdbcDataUtil.getData(conn,sqltypeidStr);
        int random = new Random().nextInt(JdbcDataUtil.getData(conn,sqltypeidStr).length);
        String typeId = JdbcDataUtil.getData(conn,sqltypeidStr,random,0);
        //创建表单
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(Charset.forName("utf-8"));
        ContentType strContent = ContentType.create("text/plain",Charset.forName("UTF-8"));
        builder.addTextBody("cat.name",catName,strContent);
        builder.addTextBody("cattype","0");
        builder.addTextBody("cat.type_id",typeId);
        builder.addTextBody("cat.cat_order","0");
        builder.addTextBody("cat.list_show","1");
        builder.addTextBody("cat.parent_id",catId);
        builder.addBinaryBody("image",file);
        Request request = Request.Post(host+path);
        request.addHeader(cookieHeader);
        String responseJson = request.body(builder.build()).execute().returnContent().asString();
        System.out.println(typeId+"----------"+catName);
        System.out.println("AddCategory响应结果-----"+responseJson);
        int result = JSONPathUtil.extract(responseJson,"$.result");
        String sqlStr = "SELECT type_id,cat_id from es_goods_cat WHERE name = '"+catName+ "' ORDER BY cat_id desc";
        String dbtypeid = JdbcDataUtil.getData(conn,sqlStr,0,0);
        subCatId = JdbcDataUtil.getData(conn,sqlStr,0,1);
        Props.put("subCatId",subCatId);
        Assert.assertEquals(result,1);
        Assert.assertEquals(typeId,dbtypeid);
    }
}
