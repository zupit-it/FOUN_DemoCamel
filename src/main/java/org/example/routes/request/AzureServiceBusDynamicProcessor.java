package org.example.routes.request;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

import java.util.Map;

@Component
public class AzureServiceBusDynamicProcessor implements Processor {

    private final Map<String, ServiceBusSenderClient> senderClients;

    public AzureServiceBusDynamicProcessor(Map<String, ServiceBusSenderClient> senderClients) {
        this.senderClients = senderClients;
    }

    @Override
    public void process(Exchange exchange) {
        String queueName = exchange.getIn().getHeader("QUEUE_NAME", String.class);
        String messageBody = exchange.getIn().getBody(String.class);

        ServiceBusSenderClient client = senderClients.get(queueName);

        if (client == null) {
            throw new RuntimeException("Queue " + queueName + " not found!");
        }

        ServiceBusMessage message = new ServiceBusMessage(messageBody);
        client.sendMessage(message);
    }
}

