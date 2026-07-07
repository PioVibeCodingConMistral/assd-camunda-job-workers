package com.example.camunda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityProvider {
    private String id;
    private String name;
    private String capabilityType;
    private double latitude;
    private double longitude;
    private String endpoint;
    private String apiKey;
}
