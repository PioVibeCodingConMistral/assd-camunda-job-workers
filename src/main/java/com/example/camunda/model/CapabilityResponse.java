package com.example.camunda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityResponse {
    private boolean isCapabilityAvailable;
    private String providerId;
    private String message;
}
