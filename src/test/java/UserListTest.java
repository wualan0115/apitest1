
import net.minidev.json.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.HttpUtil;
import util.JSONPathUtil;
import util.JdbcDataUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserListTest extends BaseTest {
	String path = "javamall/shop/admin/member!memberlistJson.do";

	//@Test(description = "会员列表")
	public void testUserList() throws IOException {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("page", "1");
		paramMap.put("rows", "10");
		paramMap.put("sort", "member_id");
		paramMap.put("order", "asc");
		String urlParam = HttpUtil.getQueryParam(paramMap);
		System.out.println(host+path+urlParam);
		HttpGet getRequest = new HttpGet(host+path+urlParam);
		getRequest.addHeader(cookieHeader);
		HttpResponse response = httpClient.execute(getRequest);
		String responseJson = EntityUtils.toString(response.getEntity());
		System.out.println("userList响应结果-----"+responseJson);
		int total = JSONPathUtil.extract(responseJson,"$.total");
		JSONArray unameArray = JSONPathUtil.extract(responseJson,"$..uname");
		//String[] unameList = JSONPathUtil.extract(responseJson,"$..uname");
		String sqlCountStr = "SELECT count(*) from es_member";
		String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
		String sqlStr = "SELECT uname from es_member LIMIT 10";
		String[] dbunameList = new String[10];
		for (int i = 0; i < dbunameList.length; i++) {
			dbunameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
		}
		System.out.println("unameList内如如下----------------");
		for (int i = 0; i <unameArray.size() ; i++) {
			System.out.println(unameArray.get(i));
		}
		System.out.println("dbunameList内如如下----------------");
		for (int i = 0; i <dbunameList.length ; i++) {
			System.out.println(dbunameList[i]);
		}
		Assert.assertEquals(total,Integer.parseInt(dbCount));
		for (int i = 0; i <unameArray.size() ; i++) {
			Assert.assertEquals(unameArray.get(i),dbunameList[i]);
		}
	}

	//@Test(description = "缺少参数")
	public void testUserListDismissParam() throws IOException {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("page", "1");
		paramMap.put("rows", "10");
		paramMap.put("sort", "member_id");
		//paramMap.put("order", "asc");
		String urlParam = HttpUtil.getQueryParam(paramMap);
		System.out.println(host + path + urlParam);
		HttpGet getRequest = new HttpGet(host + path + urlParam);
		getRequest.addHeader(cookieHeader);
		HttpResponse response = httpClient.execute(getRequest);
		String responseJson = EntityUtils.toString(response.getEntity());
		System.out.println("userList响应结果-----" + responseJson);
		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("responseCode-----------------"+response.getStatusLine().getStatusCode());
		Assert.assertEquals(500,responseCode);
	}

	@Test(description = "根据用户名搜索")
	public void testSearchByuname() throws IOException {
		Map<String, String> paramMap = new HashMap<>();
		String uname = "admin";
		paramMap.put("page", "1");
		paramMap.put("rows", "10");
		paramMap.put("sort", "member_id");
		paramMap.put("order", "asc");
		paramMap.put("uname",uname);
		String urlParam = HttpUtil.getQueryParam(paramMap);
		System.out.println(host + path + urlParam);
		HttpGet getRequest = new HttpGet(host + path + urlParam);
		getRequest.addHeader(cookieHeader);
		HttpResponse response = httpClient.execute(getRequest);
		String responseJson = EntityUtils.toString(response.getEntity());
		System.out.println("userList响应结果-----" + responseJson);
		int total = JSONPathUtil.extract(responseJson,"$.total");
		JSONArray unameArray = JSONPathUtil.extract(responseJson,"$..uname");
		String sqlCountStr = "SELECT count(*) from es_member WHERE uname like '%"+uname+"%'";
		String dbCount = JdbcDataUtil.getData(conn,sqlCountStr,0,0);
		String sqlStr = "SELECT uname from es_member WHERE uname like '%"+uname+"%'";
		int length = JdbcDataUtil.getData(conn,sqlStr).length;
		String[] dbunameList = new String[length];
		for (int i = 0; i < dbunameList.length; i++) {
			dbunameList[i] = JdbcDataUtil.getData(conn,sqlStr,i,0);
		}
		Assert.assertEquals(total,Integer.parseInt(dbCount));
		for (int i = 0; i <unameArray.size() ; i++) {
			Assert.assertEquals(unameArray.get(i),dbunameList[i]);
		}

	}

}
