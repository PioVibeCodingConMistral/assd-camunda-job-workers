package com.example.camunda.workers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CapabilityPopWorkerTest {

    @Autowired
    private CapabilityPopWorker worker;

    @Test
    void testPopCapabilityProvider_FirstProvider() {
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("id", "provider-1");
        provider1.put("name", "Provider 1");
        
        Map<String, Object> provider2 = new HashMap<>();
        provider2.put("id", "provider-2");
        provider2.put("name", "Provider 2");
        
        List<Map<String, Object>> providerList = List.of(provider1, provider2);
        
        Map<String, Object> result = worker.popCapabilityProvider("fire", providerList, 0);
        
        assertNotNull(result);
        assertEquals("provider-1", result.get("id"));
    }

    @Test
    void testPopCapabilityProvider_SecondProvider() {
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("id", "provider-1");
        
        Map<String, Object> provider2 = new HashMap<>();
        provider2.put("id", "provider-2");
        
        List<Map<String, Object>> providerList = List.of(provider1, provider2);
        
        Map<String, Object> result = worker.popCapabilityProvider("fire", providerList, 1);
        
        assertNotNull(result);
        assertEquals("provider-2", result.get("id"));
    }

    @Test
    void testPopCapabilityProvider_OutOfBounds() {
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("id", "provider-1");
        
        List<Map<String, Object>> providerList = List.of(provider1);
        
        Map<String, Object> result = worker.popCapabilityProvider("fire", providerList, 5);
        
        assertNull(result);
    }

    @Test
    void testPopCapabilityProvider_EmptyList() {
        List<Map<String, Object>> providerList = List.of();
        
        Map<String, Object> result = worker.popCapabilityProvider("fire", providerList, 0);
        
        assertNull(result);
    }
}
