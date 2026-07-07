package com.example.camunda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityRequest {
    private String capability;
    private double emergencyLatitude;
    private double emergencyLongitude;
}
