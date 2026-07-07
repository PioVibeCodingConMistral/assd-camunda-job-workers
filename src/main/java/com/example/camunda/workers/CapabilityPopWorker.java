package com.example.camunda.workers;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CapabilityPopWorker {

    @JobWorker(type = "capability-pop")
    public Map<String, Object> popCapabilityProvider(Map<String, Object> variables) {
        List<Map<String, Object>> providerList = (List<Map<String, Object>>) variables.get("providerList");
        Integer counter = (Integer) variables.get("counter");
        
        Map<String, Object> provider = null;
        
        if (providerList != null && !providerList.isEmpty() && counter != null && counter >= 0 && counter < providerList.size()) {
            provider = providerList.get(counter);
        }
        
        return Map.of("provider", provider);
    }
}
