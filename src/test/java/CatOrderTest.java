import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JdbcDataUtil;

import java.io.IOException;
import java.util.Random;

public class CatOrderTest extends BaseTest {
    String path = "javamall/shop/admin/cat!saveSort.do";

    @Test(description = "分类排序")
    public void testOrderCat() throws IOException {
        int sort = new Random().nextInt(100);
        System.out.println("sort-----"+sort);
        String requestBody = "cat_sorts="+sort+"&cat_ids=136&cat_sorts=0&cat_ids=140&cat_sorts=0&cat_ids=141&cat_sorts=0&cat_ids=142&cat_sorts=0&cat_ids=143&cat_sorts=0&cat_ids=144&cat_sorts=0&cat_ids=145&cat_sorts=0&cat_ids=147&cat_sorts=1&cat_ids=118&cat_sorts=2&cat_ids=4&cat_sorts=0&cat_ids=5&cat_sorts=0&cat_ids=22&cat_sorts=0&cat_ids=23&cat_sorts=0&cat_ids=24&cat_sorts=0&cat_ids=25&cat_sorts=0&cat_ids=26&cat_sorts=0&cat_ids=27&cat_sorts=0&cat_ids=28&cat_sorts=0&cat_ids=29&cat_sorts=0&cat_ids=30&cat_sorts=0&cat_ids=31&cat_sorts=0&cat_ids=32&cat_sorts=0&cat_ids=33&cat_sorts=0&cat_ids=34&cat_sorts=3&cat_ids=1&cat_sorts=0&cat_ids=2&cat_sorts=0&cat_ids=6&cat_sorts=0&cat_ids=7&cat_sorts=0&cat_ids=8&cat_sorts=0&cat_ids=9&cat_sorts=0&cat_ids=3&cat_sorts=0&cat_ids=10&cat_sorts=0&cat_ids=11&cat_sorts=0&cat_ids=12&cat_sorts=0&cat_ids=13&cat_sorts=0&cat_ids=14&cat_sorts=0&cat_ids=15&cat_sorts=0&cat_ids=16&cat_sorts=0&cat_ids=17&cat_sorts=0&cat_ids=18&cat_sorts=0&cat_ids=19&cat_sorts=0&cat_ids=20&cat_sorts=0&cat_ids=21&cat_sorts=3&cat_ids=38&cat_sorts=0&cat_ids=39&cat_sorts=0&cat_ids=40&cat_sorts=0&cat_ids=41&cat_sorts=0&cat_ids=42&cat_sorts=0&cat_ids=43&cat_sorts=0&cat_ids=44&cat_sorts=0&cat_ids=45&cat_sorts=0&cat_ids=46&cat_sorts=0&cat_ids=47&cat_sorts=0&cat_ids=48&cat_sorts=0&cat_ids=49&cat_sorts=0&cat_ids=50&cat_sorts=0&cat_ids=51&cat_sorts=0&cat_ids=52&cat_sorts=0&cat_ids=53&cat_sorts=0&cat_ids=54&cat_sorts=0&cat_ids=55&cat_sorts=4&cat_ids=56&cat_sorts=0&cat_ids=57&cat_sorts=0&cat_ids=58&cat_sorts=0&cat_ids=59&cat_sorts=0&cat_ids=60&cat_sorts=0&cat_ids=61&cat_sorts=0&cat_ids=62&cat_sorts=0&cat_ids=63&cat_sorts=0&cat_ids=64&cat_sorts=0&cat_ids=65&cat_sorts=0&cat_ids=66&cat_sorts=0&cat_ids=67&cat_sorts=0&cat_ids=68&cat_sorts=0&cat_ids=69&cat_sorts=0&cat_ids=70&cat_sorts=0&cat_ids=71&cat_sorts=0&cat_ids=72&cat_sorts=0&cat_ids=73&cat_sorts=0&cat_ids=74&cat_sorts=0&cat_ids=75&cat_sorts=0&cat_ids=76&cat_sorts=0&cat_ids=77&cat_sorts=0&cat_ids=78&cat_sorts=5&cat_ids=79&cat_sorts=0&cat_ids=80&cat_sorts=0&cat_ids=81&cat_sorts=0&cat_ids=82&cat_sorts=0&cat_ids=83&cat_sorts=0&cat_ids=84&cat_sorts=6&cat_ids=116&cat_sorts=0&cat_ids=117&cat_sorts=12&cat_ids=124&cat_sorts=20&cat_ids=35&cat_sorts=0&cat_ids=36&cat_sorts=0&cat_ids=37&cat_sorts=20&cat_ids=85&cat_sorts=21&cat_ids=86&cat_sorts=0&cat_ids=91&cat_sorts=0&cat_ids=92&cat_sorts=0&cat_ids=93&cat_sorts=21&cat_ids=115&cat_sorts=22&cat_ids=87&cat_sorts=22&cat_ids=105&cat_sorts=22&cat_ids=130&cat_sorts=22&cat_ids=131&cat_sorts=22&cat_ids=132&cat_sorts=22&cat_ids=134&cat_sorts=23&cat_ids=88&cat_sorts=0&cat_ids=90&cat_sorts=23&cat_ids=125&cat_sorts=24&cat_ids=89&cat_sorts=25&cat_ids=94&cat_sorts=0&cat_ids=95&cat_sorts=0&cat_ids=133&cat_sorts=27&cat_ids=103&cat_sorts=0&cat_ids=104&cat_sorts=27&cat_ids=114&cat_sorts=28&cat_ids=110&cat_sorts=0&cat_ids=111&cat_sorts=37&cat_ids=119&cat_sorts=0&cat_ids=120&cat_sorts=37&cat_ids=121&cat_sorts=43&cat_ids=127&cat_sorts=45&cat_ids=128&cat_sorts=51&cat_ids=122&cat_sorts=0&cat_ids=123&cat_sorts=78&cat_ids=129";
        HttpPost postRequest = new HttpPost(host+path+"?"+requestBody);
        System.out.println("请求地址-----------"+host+path+"?"+requestBody);
        postRequest.addHeader(cookieHeader);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("OrderCat响应结果---"+responseJson);
        String sqlStr = "SELECT cat_order from es_goods_cat WHERE cat_id = '136'";
        String db_cat_order = JdbcDataUtil.getData(conn,sqlStr,0,0);
        Assert.assertEquals(sort,Integer.parseInt(db_cat_order));
    }
}
