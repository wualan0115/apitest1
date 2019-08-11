import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JdbcDataUtil;
import util.Props;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class BrandAddTest extends BaseTest {
    String path = "javamall/shop/admin/brand!save.do";
    Props props;

    @Test
    public void testAddBrand() throws IOException {
        String brandName = RandomStringUtils.random(4,"去玩儿图如啼眼亿欧普朗克激活国范德萨自行车版面");
        String brandUrl = "http://www."+RandomStringUtils.randomAlphabetic(5)+".com";
        System.out.println(brandName+"-----"+brandUrl);
        File file = new File("C:\\Users\\liuxing\\Pictures\\Saved Pictures\\xiaolongxia1.jpg");
        //创建表单
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(Charset.forName("utf-8"));
        ContentType strContent = ContentType.create("text/plain",Charset.forName("UTF-8"));
        builder.addTextBody("brand.name", brandName,strContent);
        builder.addTextBody("brand.url", brandUrl);
        builder.addTextBody("brand.brief", RandomStringUtils.randomAlphanumeric(1, 50));
        builder.addBinaryBody("logo", file);
        Request request = Request.Post(host+path);
        request.addHeader(cookieHeader);
        String responseJson=request.body(builder.build()).execute().returnContent().asString();
        System.out.println("AddBrand响应结果-------------"+responseJson);
        String sqlStr = "SELECT url,brand_id from es_brand WHERE name = '"+brandName+"'";
        String dburl = JdbcDataUtil.getData(conn,sqlStr,0,0);
        String dbbrandid = JdbcDataUtil.getData(conn,sqlStr,0,1);
        Props.put("brandId",dbbrandid);
        Assert.assertEquals(brandUrl,dburl);
    }
}
