package com.example.camunda.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CapabilityPopWorker {

    @JobWorker(type = "capability-pop")
    public Map<String, Object> popCapabilityProvider(
            @Variable String capability,
            @Variable List<Map<String, Object>> providerList,
            @Variable int counter) {
        
        Map<String, Object> provider = null;
        
        if (providerList != null && !providerList.isEmpty() && counter >= 0 && counter < providerList.size()) {
            provider = providerList.get(counter);
        }
        
        return provider;
    }
}
