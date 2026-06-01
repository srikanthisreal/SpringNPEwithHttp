package com.example.httpnpedemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class NpeController {

    private static final Logger log = LoggerFactory.getLogger(NpeController.class);

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "spring-http-npe-demo");
        response.put("protocol", "HTTP");
        response.put("port", 8080);
        response.put("health", "/health");
        response.put("triggerNpe", "/trigger-npe");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("message", "Call /trigger-npe to intentionally generate NullPointerException");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trigger-npe")
    public ResponseEntity<Map<String, Object>> triggerNpe() {
        log.info("Received request to trigger intentional NullPointerException");

        String customerName = null;

        try {
            int length = customerName.length();

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("length", length);
            return ResponseEntity.ok(response);
        } catch (NullPointerException ex) {
            log.error("ERROR DevOpsAgentTestFailure: simulated NullPointerException in plain HTTP Spring Boot application", ex);
            throw ex;
        }
    }
}
