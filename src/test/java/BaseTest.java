import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import util.JdbcDataUtil;
import util.PropertyUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class BaseTest {

    CloseableHttpClient httpClient;
    Properties httpProperties;
    Properties dbProperties;
    String host;
    Header cookieHeader;
    Connection conn;

    @BeforeClass
    public void setUp(){
        httpProperties = PropertyUtil.getProperties("/http.properties");
        String cookieValue = httpProperties.getProperty("http.cookie");
        host =httpProperties.getProperty("http.webhost");
        httpClient = HttpClients.createDefault();
        cookieHeader = new BasicHeader("Cookie","JSESSIONID="+cookieValue);

        dbProperties = PropertyUtil.getProperties("/db.properties");
        String dbUrl = dbProperties.getProperty("jdbc.url");
        String dbUsername = dbProperties.getProperty("jdbc.username");
        String dbPassword = dbProperties.getProperty("jdbc.password");
        conn = JdbcDataUtil.getConn(dbUrl,dbUsername,dbPassword);
    }

    @AfterClass
    public void tearDown() throws IOException, SQLException {
        httpClient.close();
        conn.close();
    }
}
