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
        String capability = "fire";
        Map<String, Object> emergencyPosition = new HashMap<>();
        emergencyPosition.put("latitude", 40.7128);
        emergencyPosition.put("longitude", -74.0060);
        
        List<Map<String, Object>> result = worker.findCapabilityProviders(capability, emergencyPosition);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // First provider should be the nearest to NYC
        Map<String, Object> firstProvider = result.get(0);
        assertEquals("Fire Department NYC", firstProvider.get("name"));
    }

    @Test
    void testFindCapabilityProviders_PoliceInLA() {
        String capability = "police";
        Map<String, Object> emergencyPosition = new HashMap<>();
        emergencyPosition.put("latitude", 34.0522);
        emergencyPosition.put("longitude", -118.2437);
        
        List<Map<String, Object>> result = worker.findCapabilityProviders(capability, emergencyPosition);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // First provider should be the nearest to LA
        Map<String, Object> firstProvider = result.get(0);
        assertEquals("Police Department LA", firstProvider.get("name"));
    }

    @Test
    void testFindCapabilityProviders_MedicalInChicago() {
        String capability = "medical";
        Map<String, Object> emergencyPosition = new HashMap<>();
        emergencyPosition.put("latitude", 41.8781);
        emergencyPosition.put("longitude", -87.6298);
        
        List<Map<String, Object>> result = worker.findCapabilityProviders(capability, emergencyPosition);
        
        assertNotNull(result);
        
        // Should return medical providers, with LA being closer to Chicago than Boston
        assertTrue(result.stream()
            .anyMatch(p -> p.get("name").equals("Hospital LA")));
    }
}
