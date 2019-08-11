import org.apache.commons.lang3.RandomStringUtils;
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
import util.TimeUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class BonusAddTest extends BaseTest {
    String path = "javamall/shop/admin/bonus-type!saveAdd.do";
    Props props;

    @Test(description = "新增优惠券")
    public void testAddBonus() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(host+path);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<>();
        String recognition = "YHQ"+ RandomStringUtils.randomNumeric(4);
        String name = RandomStringUtils.random(4,"去玩儿图与欧派离开家和规范的撒展现出帮你买难不成");
        String today = TimeUtil.getCurrentDate();
        String todayNextMonth = TimeUtil.getTodayNextMonth();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, new Random().nextInt(100));
        String useTimeStart = sf.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, new Random().nextInt(100));
        String useTimeEnd = sf.format(c.getTime());
        int money = new Random().nextInt(100)+1;
        int min_goods_amount = money +1;

        paramList.add(new BasicNameValuePair("bonusType.recognition", recognition));
        paramList.add(new BasicNameValuePair("bonusType.type_name", name));
        paramList.add(new BasicNameValuePair("bonusType.type_money", String.valueOf(money)));
        paramList.add(new BasicNameValuePair("bonusType.min_goods_amount", String.valueOf(min_goods_amount)));
        paramList.add(new BasicNameValuePair("bonusType.send_type", "0"));
        paramList.add(new BasicNameValuePair("sendTimeStart", today));
        paramList.add(new BasicNameValuePair("sendTimeEnd", todayNextMonth));
        paramList.add(new BasicNameValuePair("useTimeStart", useTimeStart));
        paramList.add(new BasicNameValuePair("useTimeEnd", useTimeEnd));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("AddBonus响应结果-----------" + responseJson);
        String sqlStr = "SELECT type_money,min_goods_amount,send_start_date,send_end_date,use_start_date,use_end_date,type_id from es_bonus_type WHERE type_name = '"+name+"' and recognition = '"+recognition+"' ORDER BY type_id desc";
        String dbtype_money = JdbcDataUtil.getData(conn,sqlStr,0,0);
        String dbmin_goods_amount = JdbcDataUtil.getData(conn,sqlStr,0,1);
        String dbsend_start_date = JdbcDataUtil.getData(conn,sqlStr,0,2);
        String dbsend_end_date = JdbcDataUtil.getData(conn,sqlStr,0,3);
        String dbuse_start_date = JdbcDataUtil.getData(conn,sqlStr,0,4);
        String dbuse_end_date = JdbcDataUtil.getData(conn,sqlStr,0,5);
        String dbtypeid = JdbcDataUtil.getData(conn,sqlStr,0,6);
        Props.put("bonustypeId",dbtypeid);
        System.out.println(Double.parseDouble(dbtype_money) +"--------"+(double)money);
        Assert.assertEquals(Double.parseDouble(dbtype_money),(double)money);
        Assert.assertEquals(Double.parseDouble(dbmin_goods_amount),(double)min_goods_amount);
        System.out.println(TimeUtil.getUnixDateByStr(today+" 00:00:00") +"----------" +Long.parseLong(dbsend_start_date)*1000);
        Assert.assertEquals(TimeUtil.getUnixDateByStr(today+" 00:00:00"),Long.parseLong(dbsend_start_date)*1000);
        Assert.assertEquals(TimeUtil.getUnixDateByStr(todayNextMonth+" 00:00:00"),Long.parseLong(dbsend_end_date)*1000);
        Assert.assertEquals(TimeUtil.getUnixDateByStr(useTimeStart+" 00:00:00"),Long.parseLong(dbuse_start_date)*1000);
        Assert.assertEquals(TimeUtil.getUnixDateByStr(useTimeEnd+" 00:00:00"),Long.parseLong(dbuse_end_date)*1000);



    }
}
