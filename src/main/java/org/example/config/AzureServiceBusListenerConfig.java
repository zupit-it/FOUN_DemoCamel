package org.example.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AzureServiceBusListenerConfig {

    @Value("${azure.servicebus.connection-string}")
    private String connectionString;

    @Bean
    public Map<String, ServiceBusProcessorClient> processorClients(ProducerTemplate producerTemplate) {
        Map<String, ServiceBusProcessorClient> clients = new HashMap<>();

        clients.put(AzureQueue.CREA_FASCICOLO_REPLY.toString(), createProcessorClient(AzureQueue.CREA_FASCICOLO_REPLY, producerTemplate));
        clients.put(AzureQueue.CREA_PROTOCOLLO_REPLY.toString(), createProcessorClient(AzureQueue.CREA_PROTOCOLLO_REPLY, producerTemplate));

        return clients;
    }

    private ServiceBusProcessorClient createProcessorClient(AzureQueue endpoint, ProducerTemplate producerTemplate) {
        return new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .queueName(endpoint.getGetQueueName())
                .processMessage(context -> {
                    String body = context.getMessage().getBody().toString();
                    producerTemplate.sendBodyAndHeader(getEndpointUri(endpoint.toString()), body, "QUEUE_NAME", endpoint);
                })
                .processError(context -> System.err.println("Errore ricezione sulla coda " + endpoint + ": " + context.getException()))
                .buildProcessorClient();
    }

    private String getEndpointUri(String queueName) {
        if (AzureQueue.CREA_FASCICOLO_REPLY.toString().equals(queueName)) {
            return CamelEndpoint.DIRECT_CREA_PROTOCOLLO_ROUTE.getUri();
        }

        return CamelEndpoint.DIRECT_END_WORKFLOW.getUri();
    }
}