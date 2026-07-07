package com.example.camunda.workers;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class CapabilityExecutionWorker {

    private final RestTemplate restTemplate;

    public CapabilityExecutionWorker(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @JobWorker(type = "capability-execution")
    public Map<String, Object> executeCapability(Map<String, Object> variables) {
        Map<String, Object> provider = (Map<String, Object>) variables.get("provider");
        String capability = (String) variables.get("capability");
        
        Map<String, Object> response = new HashMap<>();
        
        if (provider == null) {
            response.put("isCapabilityAvailable", false);
            response.put("message", "No provider available");
            return Map.of("response", response);
        }
        
        String endpoint = (String) provider.get("endpoint");
        String apiKey = (String) provider.get("apiKey");
        
        try {
            // Create request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");
            
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("capability", capability);
            requestBody.put("timestamp", System.currentTimeMillis());
            
            // Create HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Send POST request to the provider's endpoint
            ResponseEntity<Map> result = restTemplate.exchange(
                endpoint, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            // If we get a successful response, capability is available
            if (result.getStatusCode().is2xxSuccessful()) {
                response.put("isCapabilityAvailable", true);
                response.put("message", "Capability executed successfully");
                response.put("providerId", provider.get("id"));
                response.put("statusCode", result.getStatusCodeValue());
                response.put("responseBody", result.getBody());
            } else {
                response.put("isCapabilityAvailable", false);
                response.put("message", "Capability execution failed: " + result.getStatusCode());
                response.put("providerId", provider.get("id"));
            }
            
        } catch (Exception e) {
            // If there's any exception (connection error, timeout, etc.), capability is not available
            response.put("isCapabilityAvailable", false);
            response.put("message", "Error executing capability: " + e.getMessage());
            response.put("providerId", provider != null ? provider.get("id") : "unknown");
        }
        
        return Map.of("response", response);
    }
}
