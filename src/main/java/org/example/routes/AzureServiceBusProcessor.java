package org.example.routes;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AzureServiceBusProcessor implements Processor {

    @Value("${azure.servicebus.connection-string}")
    private String connectionString;

    private static final String QUEUE_NAME = "siag-sa-03-procedimenti-creafascicolo-queue";

    @Override
    public void process(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);

        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(QUEUE_NAME)
                .buildClient();

        try {
            ServiceBusMessage message = new ServiceBusMessage(body);
            senderClient.sendMessage(message);
            System.out.println("Messaggio inviato direttamente via Java SDK");
        } catch (Exception e) {
            System.err.println("Errore durante l'invio a Service Bus: " + e.getMessage());
            e.printStackTrace();
        } finally {
            senderClient.close();
        }
    }
}
