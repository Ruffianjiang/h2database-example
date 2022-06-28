package com.dawn.h2.v2;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;


@Slf4j
@Configuration
public class H2TcpConfig {

    @Value("${datasource.h2.tcp-port:9092}")
    private String h2TcpPort;

    @Bean
    public Server server() throws SQLException {

        try {
            log.info("H2 TCP Server initialized with port: {}", h2TcpPort);
            // 启动TCP服务
            Server server = Server.createTcpServer("-tcpPort", h2TcpPort, "-trace", "-ifNotExists", "-tcpAllowOthers",
                    "-tcpDaemon");
            log.info("H2 TCP Serve url: {}", server.getURL());
            return server.start();
        } finally {
            log.info("H2 TCP Server started on port: {}", h2TcpPort);
        }
    }

    @Bean({"h2TcpDataSource"})
    @ConfigurationProperties(
            prefix = "datasource.h2"
    )
    public DataSource h2DataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:h2:" + this.server().getURL() + "/~/test2;MODE=MYSQL;" +
                "DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;");
        return druidDataSource;
    }

    @Bean(
            name = {"h2TcpTemplate"}
    )
    public JdbcTemplate h2JdbcTemplate() throws SQLException {
        return new JdbcTemplate(this.h2DataSource());
    }


}
