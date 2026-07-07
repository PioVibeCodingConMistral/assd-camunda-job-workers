package com.example.camunda.workers;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class CapabilityProviderFinderWorker {

    // Mock database of capability providers
    private static final List<Map<String, Object>> PROVIDER_DATABASE = List.of(
        createProvider("provider-1", "Fire Department NYC", "fire", 40.7128, -74.0060, "https://api.nyc-fire.gov/capability", "nyc-fire-key"),
        createProvider("provider-2", "Police Department LA", "police", 34.0522, -118.2437, "https://api.lapd.gov/capability", "lapd-key"),
        createProvider("provider-3", "Hospital Boston", "medical", 42.3601, -71.0589, "https://api.boston-hospital.gov/capability", "boston-med-key"),
        createProvider("provider-4", "Fire Department LA", "fire", 34.0522, -118.2437, "https://api.la-fire.gov/capability", "la-fire-key"),
        createProvider("provider-5", "Police Department NYC", "police", 40.7128, -74.0060, "https://api.nyc-police.gov/capability", "nyc-police-key"),
        createProvider("provider-6", "Hospital LA", "medical", 34.0522, -118.2437, "https://api.la-hospital.gov/capability", "la-med-key"),
        createProvider("provider-7", "Fire Department Chicago", "fire", 41.8781, -87.6298, "https://api.chicago-fire.gov/capability", "chicago-fire-key"),
        createProvider("provider-8", "Police Department Chicago", "police", 41.8781, -87.6298, "https://api.chicago-police.gov/capability", "chicago-police-key")
    );

    private static Map<String, Object> createProvider(String id, String name, String capabilityType, 
                                                       double latitude, double longitude, 
                                                       String endpoint, String apiKey) {
        return Map.of(
            "id", id,
            "name", name,
            "capabilityType", capabilityType,
            "latitude", latitude,
            "longitude", longitude,
            "endpoint", endpoint,
            "apiKey", apiKey
        );
    }

    @JobWorker(type = "capability-provider-finder")
    public List<Map<String, Object>> findCapabilityProviders(
            @Variable String capability,
            @Variable Map<String, Object> emergencyPosition) {
        
        double emergencyLat = emergencyPosition != null ? 
            Double.parseDouble(emergencyPosition.get("latitude").toString()) : 0.0;
        double emergencyLon = emergencyPosition != null ? 
            Double.parseDouble(emergencyPosition.get("longitude").toString()) : 0.0;
        
        // Filter providers by capability type
        List<Map<String, Object>> filteredProviders = PROVIDER_DATABASE.stream()
            .filter(provider -> provider.get("capabilityType").toString().equalsIgnoreCase(capability))
            .toList();
        
        // Sort by distance to emergency position (nearest first)
        return filteredProviders.stream()
            .sorted(Comparator.comparingDouble(provider -> 
                calculateDistance(emergencyLat, emergencyLon, 
                                Double.parseDouble(provider.get("latitude").toString()),
                                Double.parseDouble(provider.get("longitude").toString()))))
            .toList();
    }
    
    // Haversine formula to calculate distance between two points in kilometers
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}
