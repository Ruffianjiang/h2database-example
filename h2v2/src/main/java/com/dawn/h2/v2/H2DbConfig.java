package com.dawn.h2.v2;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;


@Slf4j
@Configuration
public class H2DbConfig {

    @Value("${datasource.h2.tcp-port:9092}")
    private String h2TcpPort;

    @Bean({"h2MemDataSource"})
    @ConfigurationProperties(
            prefix = "datasource.h2"
    )
    public DataSource h2MemDataSource() {
        return new DruidDataSource();
    }

    @Bean(
            name = {"h2MemTemplate"}
    )
    public JdbcTemplate h2MemJdbcTemplate() {
        return new JdbcTemplate(this.h2MemDataSource());
    }


    @Bean("h2TcpServer")
    public Server server() {

        try {
            String password = "123456";
            log.info("H2 TCP Server initialized with port: {}", h2TcpPort);
            // @doc tcp服务密码不设置，默认自动生成随机密码
            DruidDataSource memDruidDataSource = (DruidDataSource) this.h2MemDataSource();
            if(!ObjectUtils.isEmpty(memDruidDataSource.getPassword())) {
                password = memDruidDataSource.getPassword();
            } else {
                log.info("H2 password is empty, it will be set as default");
            }
            // 启动TCP服务
            Server server = Server.createTcpServer("-tcpPort", h2TcpPort, "-trace", "-ifNotExists", "-tcpAllowOthers",
                    "-tcpDaemon", "-tcpPassword", password);
            log.info("H2 TCP Serve url: {}", server.getURL());
            return server.start();
        } catch (Exception e) {
            log.error("error", e);
            System.exit(0);
        } finally {
            log.info("H2 TCP Server started on port: {}", h2TcpPort);
        }
        throw new RuntimeException("H2 TCP Server cannot be created!");
    }


    @Bean({"h2TcpDataSource"})
    public DataSource h2TcpDataSource() {
        DruidDataSource druidDataSource = (DruidDataSource) this.h2MemDataSource();
        druidDataSource.setUrl("jdbc:h2:" + this.server().getURL() + "/mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1;");
        return druidDataSource;
    }

    @Bean(
            name = {"h2TcpTemplate"}
    )
    public JdbcTemplate h2TcpJdbcTemplate() {
        return new JdbcTemplate(this.h2TcpDataSource());
    }


}
