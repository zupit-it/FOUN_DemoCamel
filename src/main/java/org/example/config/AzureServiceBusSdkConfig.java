package org.example.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AzureServiceBusSdkConfig {

    @Value("${azure.servicebus.connection-string}")
    private String connectionString;

    @Bean
    public Map<String, ServiceBusSenderClient> senderClients() {
        Map<String, ServiceBusSenderClient> clients = new HashMap<>();

        Arrays.stream(AzureQueue.values()).forEach(endpoint -> {
            clients.put(endpoint.toString(), new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .queueName(endpoint.getGetQueueName())
                    .buildClient());
        });

        return clients;
    }
}
