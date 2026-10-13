package com.danpss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired(required = false)
    private DataSource dataSource;

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "DANPSS backend is running");

        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                response.put("database", "CONNECTED");
                response.put("dbUrl", conn.getMetaData().getURL());
            } catch (Exception e) {
                response.put("database", "FAILED");
                response.put("dbError", e.getMessage());
            }
        } else {
            response.put("database", "NO_DATASOURCE");
        }

        return response;
    }
}
