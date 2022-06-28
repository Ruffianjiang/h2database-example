package com.dawn.h2.v1;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class H2TcpClientTest {


    @Autowired
    @Qualifier("h2TcpDataSource")
    private DataSource dataSource;


    @Autowired(required = false)
    @Qualifier("h2TcpTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${datasource.h2.tcp-port:9092}")
    private String h2TcpPort;

    private String h2Ip = "192.168.32.196";
//    private String h2Ip = "localhost";


    @Test
    public void connectTest() throws SQLException {

        log.info("==============================start");
        Connection conn = null;

        conn = DriverManager.
                getConnection("jdbc:h2:mem://" + h2Ip + ":" + h2TcpPort + "/mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL",
                        "",
                        "");
        conn = DriverManager.
                getConnection("jdbc:h2:tcp://" + h2Ip + ":" + h2TcpPort + "/mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL",
                        "sa",
                        "123456");
        conn = DriverManager.
                getConnection("jdbc:h2:tcp://" + h2Ip + ":" + h2TcpPort + "/mem:test_tcp;DB_CLOSE_DELAY=-1;MODE=MYSQL",
                        "sa",
                        "");
        conn = DriverManager.
                getConnection("jdbc:h2:tcp://" + h2Ip + ":" + h2TcpPort + "/mem:test_tcp2;DB_CLOSE_DELAY=-1;MODE=MYSQL",
                        "sa",
                        "");
        conn = DriverManager.
                getConnection("jdbc:h2:tcp://" + h2Ip + ":" + h2TcpPort + "/mem:test_tcp3;DB_CLOSE_DELAY=-1;" +
                                "MODE=MYSQL",
                        "sa",
                        "");
        conn = DriverManager.
                getConnection("jdbc:h2:tcp://" + h2Ip + ":" + h2TcpPort + "/mem:test_tcp4;DB_CLOSE_DELAY=-1;MODE=MYSQL",
                        "sa",
                        "");

        Statement stat = conn.createStatement();
        stat.execute("DROP TABLE IF EXISTS TEST_TCP_CON");
        stat.execute("CREATE TABLE TEST_TCP_CON(NAME VARCHAR(200))");
        stat.execute("INSERT INTO TEST_TCP_CON VALUES('jiang.liu')");
        stat.execute("INSERT INTO TEST_TCP_CON VALUES('jiang.li')");
        stat.execute("INSERT INTO TEST_TCP_CON VALUES('jiang.a')");
        stat.execute("INSERT INTO TEST_TCP_CON VALUES('jiang.b')");
        // use data
        ResultSet result = stat.executeQuery("select name from TEST_TCP_CON ");
        int i = 1;
        while (result.next()) {
            System.out.println(i++ + ":" + result.getString("name"));
        }

        result.close();
        stat.close();
        conn.close();

        log.info("==============================end");
    }

    @Test
    public void dataSourceTest() throws SQLException {

        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        log.info(druidDataSource.getUrl());

        Connection conn = dataSource.getConnection();

        Statement stat = conn.createStatement();
        stat.execute("DROP TABLE IF EXISTS TEST_TCP_DATASORCE");
        stat.execute("CREATE TABLE TEST_TCP_DATASORCE(NAME VARCHAR(200))");
        stat.execute("INSERT INTO TEST_TCP_DATASORCE VALUES('jiang.liu')");
        stat.execute("INSERT INTO TEST_TCP_DATASORCE VALUES('jiang.li')");
        stat.execute("INSERT INTO TEST_TCP_DATASORCE VALUES('jiang.a')");
        stat.execute("INSERT INTO TEST_TCP_DATASORCE VALUES('jiang.b')");
        // use data
        ResultSet result = stat.executeQuery("select name from TEST_TCP_DATASORCE ");
        int i = 1;
        while (result.next()) {
            System.out.println(i++ + ":" + result.getString("name"));
        }

        result.close();
        stat.close();
        conn.close();
    }


    @Test
    public void jdbcTemplateTest() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS TEST_TCP_JDBC");
        jdbcTemplate.execute("CREATE TABLE TEST_TCP_JDBC(NAME VARCHAR(200))");
        jdbcTemplate.execute("INSERT INTO TEST_TCP_JDBC VALUES('jiang.liu')");
        jdbcTemplate.execute("INSERT INTO TEST_TCP_JDBC VALUES('jiang.li')");
        jdbcTemplate.execute("INSERT INTO TEST_TCP_JDBC VALUES('jiang.a')");
        jdbcTemplate.execute("INSERT INTO TEST_TCP_JDBC VALUES('jiang.b')");


        List<Map<String, Object>> result = jdbcTemplate.queryForList("select name from TEST_TCP_JDBC ");
        assert result.size() == 4;
        log.info(JSON.toJSONString(result));
    }
}
