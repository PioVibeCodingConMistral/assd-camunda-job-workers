package com.example.camunda.workers;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CapabilityExecutionWorker {

    @JobWorker(type = "capability-execution")
    public Map<String, Object> executeCapability(
            @Variable Map<String, Object> provider,
            @Variable String capability) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (provider == null) {
            response.put("isCapabilityAvailable", false);
            response.put("message", "No provider available");
            return response;
        }
        
        // Mock the HTTP request - simulate calling the provider's endpoint
        boolean success = mockHttpRequest(provider, capability);
        
        if (success) {
            response.put("isCapabilityAvailable", true);
            response.put("message", "Capability executed successfully");
            response.put("providerId", provider.get("id"));
        } else {
            response.put("isCapabilityAvailable", false);
            response.put("message", "Capability execution failed");
            response.put("providerId", provider.get("id"));
        }
        
        return response;
    }
    
    // Mock HTTP request - simulates calling the provider's endpoint
    private boolean mockHttpRequest(Map<String, Object> provider, String capability) {
        // Simulate a successful response for most providers
        // You can customize this logic based on your needs
        String providerId = provider.get("id").toString();
        
        // Simulate some providers being unavailable
        if (providerId.contains("chicago")) {
            return false; // Chicago providers are "down"
        }
        
        // All other providers are available
        return true;
    }
}
