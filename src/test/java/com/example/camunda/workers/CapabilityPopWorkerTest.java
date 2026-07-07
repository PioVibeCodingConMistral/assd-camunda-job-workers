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
        Map<String, Object> variables = new HashMap<>();
        
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("id", "provider-1");
        provider1.put("name", "Provider 1");
        
        Map<String, Object> provider2 = new HashMap<>();
        provider2.put("id", "provider-2");
        provider2.put("name", "Provider 2");
        
        List<Map<String, Object>> providerList = List.of(provider1, provider2);
        variables.put("providerList", providerList);
        variables.put("counter", 0);
        
        Map<String, Object> result = worker.popCapabilityProvider(variables);
        
        assertNotNull(result);
        assertTrue(result.containsKey("provider"));
        
        Map<String, Object> selectedProvider = (Map<String, Object>) result.get("provider");
        assertNotNull(selectedProvider);
        assertEquals("provider-1", selectedProvider.get("id"));
    }

    @Test
    void testPopCapabilityProvider_SecondProvider() {
        Map<String, Object> variables = new HashMap<>();
        
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("id", "provider-1");
        
        Map<String, Object> provider2 = new HashMap<>();
        provider2.put("id", "provider-2");
        
        List<Map<String, Object>> providerList = List.of(provider1, provider2);
        variables.put("providerList", providerList);
        variables.put("counter", 1);
        
        Map<String, Object> result = worker.popCapabilityProvider(variables);
        
        Map<String, Object> selectedProvider = (Map<String, Object>) result.get("provider");
        assertNotNull(selectedProvider);
        assertEquals("provider-2", selectedProvider.get("id"));
    }

    @Test
    void testPopCapabilityProvider_OutOfBounds() {
        Map<String, Object> variables = new HashMap<>();
        
        Map<String, Object> provider1 = new HashMap<>();
        provider1.put("id", "provider-1");
        
        List<Map<String, Object>> providerList = List.of(provider1);
        variables.put("providerList", providerList);
        variables.put("counter", 5);
        
        Map<String, Object> result = worker.popCapabilityProvider(variables);
        
        assertNotNull(result);
        assertTrue(result.containsKey("provider"));
        assertNull(result.get("provider"));
    }

    @Test
    void testPopCapabilityProvider_EmptyList() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("providerList", List.of());
        variables.put("counter", 0);
        
        Map<String, Object> result = worker.popCapabilityProvider(variables);
        
        assertNotNull(result);
        assertNull(result.get("provider"));
    }
}
