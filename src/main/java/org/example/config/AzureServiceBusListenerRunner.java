package org.example.config;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AzureServiceBusListenerRunner {

    private final Map<String, ServiceBusProcessorClient> processorClients;

    public AzureServiceBusListenerRunner(Map<String, ServiceBusProcessorClient> processorClients) {
        this.processorClients = processorClients;
    }

    @PostConstruct
    public void start() {
        processorClients.values().forEach(ServiceBusProcessorClient::start);
    }
}

