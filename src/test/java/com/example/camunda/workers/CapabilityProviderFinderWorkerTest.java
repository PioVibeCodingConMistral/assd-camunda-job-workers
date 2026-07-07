package com.example.camunda.workers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CapabilityProviderFinderWorkerTest {

    @Autowired
    private CapabilityProviderFinderWorker worker;

    @Test
    void testFindCapabilityProviders_FireInNYC() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("capability", "fire");
        
        Map<String, Object> emergencyPosition = new HashMap<>();
        emergencyPosition.put("latitude", 40.7128);
        emergencyPosition.put("longitude", -74.0060);
        variables.put("emergencyPosition", emergencyPosition);
        
        Map<String, Object> result = worker.findCapabilityProviders(variables);
        
        assertNotNull(result);
        assertTrue(result.containsKey("providerList"));
        
        List<Map<String, Object>> providerList = (List<Map<String, Object>>) result.get("providerList");
        assertNotNull(providerList);
        assertFalse(providerList.isEmpty());
        
        // First provider should be the nearest to NYC
        Map<String, Object> firstProvider = providerList.get(0);
        assertEquals("Fire Department NYC", firstProvider.get("name"));
    }

    @Test
    void testFindCapabilityProviders_PoliceInLA() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("capability", "police");
        
        Map<String, Object> emergencyPosition = new HashMap<>();
        emergencyPosition.put("latitude", 34.0522);
        emergencyPosition.put("longitude", -118.2437);
        variables.put("emergencyPosition", emergencyPosition);
        
        Map<String, Object> result = worker.findCapabilityProviders(variables);
        
        assertNotNull(result);
        List<Map<String, Object>> providerList = (List<Map<String, Object>>) result.get("providerList");
        assertNotNull(providerList);
        assertFalse(providerList.isEmpty());
        
        // First provider should be the nearest to LA
        Map<String, Object> firstProvider = providerList.get(0);
        assertEquals("Police Department LA", firstProvider.get("name"));
    }

    @Test
    void testFindCapabilityProviders_MedicalInChicago() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("capability", "medical");
        
        Map<String, Object> emergencyPosition = new HashMap<>();
        emergencyPosition.put("latitude", 41.8781);
        emergencyPosition.put("longitude", -87.6298);
        variables.put("emergencyPosition", emergencyPosition);
        
        Map<String, Object> result = worker.findCapabilityProviders(variables);
        
        assertNotNull(result);
        List<Map<String, Object>> providerList = (List<Map<String, Object>>) result.get("providerList");
        assertNotNull(providerList);
        
        // Should return medical providers, with LA being closer to Chicago than Boston
        assertTrue(providerList.stream()
            .anyMatch(p -> p.get("name").equals("Hospital LA")));
    }
}
