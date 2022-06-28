package com.dawn.h2.v1;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class H2MemClientTest {


    @Autowired
    @Qualifier("h2MemDataSource")
    private DataSource dataSource;


    @Autowired
    @Qualifier("h2MemTemplate")
    private JdbcTemplate jdbcTemplate;

    @Test
    public void dataSourceTest() throws SQLException {
        Connection conn = dataSource.getConnection();

        Statement stat = conn.createStatement();
        stat.execute("DROP TABLE IF EXISTS TEST_MEM_DATASORCE");
        stat.execute("CREATE TABLE TEST_MEM_DATASORCE(NAME VARCHAR(200))");
        stat.execute("INSERT INTO TEST_MEM_DATASORCE VALUES('jiang.liu')");
        stat.execute("INSERT INTO TEST_MEM_DATASORCE VALUES('jiang.li')");
        stat.execute("INSERT INTO TEST_MEM_DATASORCE VALUES('jiang.a')");
        stat.execute("INSERT INTO TEST_MEM_DATASORCE VALUES('jiang.b')");
        // use data
        ResultSet result = stat.executeQuery("select name from TEST_MEM_DATASORCE ");
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
        jdbcTemplate.execute("DROP TABLE IF EXISTS TEST_MEM_JDBC");
        jdbcTemplate.execute("CREATE TABLE TEST_MEM_JDBC(NAME VARCHAR(200))");
        jdbcTemplate.execute("INSERT INTO TEST_MEM_JDBC VALUES('jiang.liu')");
        jdbcTemplate.execute("INSERT INTO TEST_MEM_JDBC VALUES('jiang.li')");
        jdbcTemplate.execute("INSERT INTO TEST_MEM_JDBC VALUES('jiang.a')");
        jdbcTemplate.execute("INSERT INTO TEST_MEM_JDBC VALUES('jiang.b')");


        List<Map<String, Object>> result = jdbcTemplate.queryForList("select NAME from TEST_MEM_JDBC ");
        assert result.size() == 4;
        log.info(JSON.toJSONString(result));
    }

}
