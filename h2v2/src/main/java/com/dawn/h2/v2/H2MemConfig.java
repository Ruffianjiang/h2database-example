package com.dawn.h2.v2;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class H2MemConfig {

    @Bean({"h2DataSource"})
    @ConfigurationProperties(
            prefix = "datasource.h2"
    )
    public DataSource h2DataSource() {
        return new DruidDataSource();
    }

    @Bean(
            name = {"h2Template"}
    )
    public JdbcTemplate h2JdbcTemplate() {
        return new JdbcTemplate(this.h2DataSource());
    }

}
