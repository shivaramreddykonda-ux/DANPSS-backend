package com.danpss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username:4QdRFBPvK8sBDJh.root}")
    private String username;

    @Value("${spring.datasource.password:JzVaTpV16Zd8Pd51}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        String fixedUrl = url;
        if (fixedUrl == null || fixedUrl.isBlank()) {
            fixedUrl = "jdbc:mysql://gateway01.ap-northeast-1.prod.aws.tidbcloud.com:4000/danpss_db?sslMode=VERIFY_IDENTITY";
        }
        // Auto-correct /sys schema to application schema danpss_db
        if (fixedUrl.contains("/sys")) {
            fixedUrl = fixedUrl.replace("/sys", "/danpss_db");
        }
        if (fixedUrl.startsWith("jdbc:mysql://") && !fixedUrl.contains("sslMode")) {
            fixedUrl += (fixedUrl.contains("?") ? "&" : "?") + "sslMode=VERIFY_IDENTITY";
        }
        return DataSourceBuilder.create()
                .url(fixedUrl)
                .username(username)
                .password(password)
                .build();
    }
}
