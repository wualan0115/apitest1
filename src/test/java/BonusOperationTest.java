import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.HttpUtil;
import util.JSONPathUtil;
import util.JdbcDataUtil;
import util.Props;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonusOperationTest extends BaseTest {
    String sendBonusByMemLvPath = "javamall/shop/admin/bonus!sendForMemberLv.do";
    String sendBonusBuMemPath = "javamall/shop/admin/bonus!sendForMember.do";
    String bonusDetailPath = "javamall/shop/admin/bonus!listJson.do";
    String typeId;

    @Test(description = "按用户等级发放优惠券")
    public void testSendBonusByMemLv() throws IOException {
        HttpPost postRequest = new HttpPost(host+sendBonusByMemLvPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //String type_id = "17";
        typeId = Props.get("bonustypeId");
        paramList.add(new BasicNameValuePair("typeid", typeId));
        String lv_id = RandomStringUtils.random(1, "1234");
        paramList.add(new BasicNameValuePair("lvid", lv_id));
        // 执行前查询数据库中数量 SELECT create_num from es_bonus_type WHERE type_id = ''
        String sqlStr = "SELECT create_num from es_bonus_type WHERE type_id = '" + typeId + "'";
        String createNumBefore = JdbcDataUtil.getData(conn, sqlStr, 0, 0);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SendBonusByMemberLv响应结果----------" + responseJson);

        // 数据库断言
        int count = JSONPathUtil.extract(responseJson, "$.count");
        String createNumAfter = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        System.out.println(createNumBefore + "-----------" + createNumAfter + "--------" + count);
        Assert.assertEquals(Integer.parseInt(createNumAfter) - Integer.parseInt(createNumBefore), count);
    }

    @Test(description = "按用户发放优惠券")
    public void testSendBonusByMem() throws IOException {
        HttpPost postRequest = new HttpPost(host+sendBonusBuMemPath);
        postRequest.addHeader(cookieHeader);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //String type_id = "17";
        typeId = Props.get("bonustypeId");
        paramList.add(new BasicNameValuePair("typeid", typeId));
        paramList.add(new BasicNameValuePair("memberids", "1"));
        paramList.add(new BasicNameValuePair("memberids", "189"));

        // 执行前查询数据库中数量 SELECT create_num from es_bonus_type WHERE type_id = ''
        String sqlStr = "SELECT create_num from es_bonus_type WHERE type_id = '" + typeId + "'";
        String createNumBefore = JdbcDataUtil.getData(conn, sqlStr, 0, 0);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        postRequest.setEntity(entity);
        HttpResponse response = httpClient.execute(postRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("SendBonusByMember响应结果----------" + responseJson);

        //数据库断言
        int count = JSONPathUtil.extract(responseJson, "$.count");
        String createNumAfter = JdbcDataUtil.getData(conn, sqlStr, 0, 0);
        System.out.println(createNumBefore + "-----------" + createNumAfter + "--------" + count);
        Assert.assertEquals(Integer.parseInt(createNumAfter) - Integer.parseInt(createNumBefore), count);
    }

    @Test(description = "查看红包详情")
    public void testViewBonusDetail() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        //String typeId = "17";
        typeId = Props.get("bonustypeId");
        paramMap.put("typeid", typeId);
        paramMap.put("send_type", "0");
        paramMap.put("page", "1");
        paramMap.put("rows", "1000");
        String urlParam = HttpUtil.getQueryParam(paramMap);
        HttpGet getRequest = new HttpGet(host+bonusDetailPath+urlParam);
        System.out.println(host+bonusDetailPath+urlParam);
        getRequest.addHeader(cookieHeader);
        //执行请求
        HttpResponse response = httpClient.execute(getRequest);
        String responseJson = EntityUtils.toString(response.getEntity());
        System.out.println("BonusList响应结果------------"+responseJson);
        //断言 解析response
        int total =JSONPathUtil.extract(responseJson, "$.total");
        List<Object> memberNameList = JSONPathUtil.extract(responseJson, "$..member_name");
        //查数据库 SELECT count(*) from es_member_bonus WHERE bonus_type_id = ''
        // SELECT member_name from es_member_bonus WHERE bonus_type_id = ''
        String sqlNumStr = "SELECT count(*) from es_member_bonus WHERE bonus_type_id = '"+typeId+"'";
        String dbTotal = JdbcDataUtil.getData(conn, sqlNumStr, 0, 0);
        String sqlStr = "SELECT member_name from es_member_bonus WHERE bonus_type_id = '"+typeId +"'";
        Object[][] member_name =JdbcDataUtil.getData(conn, sqlStr);
        for (int i = 0; i < memberNameList.size(); i++) {
            System.out.println(memberNameList.get(i)+"----------"+member_name[i][0]);
            Assert.assertEquals(memberNameList.get(i), member_name[i][0]);

        }
        System.out.println(total+"-----------"+dbTotal);
        Assert.assertEquals(total, Integer.parseInt(dbTotal));
    }
}
