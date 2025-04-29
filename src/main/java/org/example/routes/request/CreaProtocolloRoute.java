package org.example.routes.request;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.AzureQueue;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class CreaProtocolloRoute extends RouteBuilder {

    private final AzureServiceBusDynamicProcessor azureServiceBusDynamicProcessor;

    public CreaProtocolloRoute(AzureServiceBusDynamicProcessor azureServiceBusDynamicProcessor) {
        this.azureServiceBusDynamicProcessor = azureServiceBusDynamicProcessor;
    }

    @Override
    public void configure() {
        from(CamelEndpoint.DIRECT_CREA_PROTOCOLLO_ROUTE.getUri())
                .log("Invio richiesta di creazione protocollo e attesa risposta")
                .setHeader("QUEUE_NAME", constant(AzureQueue.EVENTO_DOMANDA_REGISTRATA.toString())).process(azureServiceBusDynamicProcessor);
    }
}