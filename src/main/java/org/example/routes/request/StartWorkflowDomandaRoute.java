package org.example.routes.request;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.AzureQueue;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class StartWorkflowDomandaRoute extends RouteBuilder {

    private final AzureServiceBusDynamicProcessor azureServiceBusDynamicProcessor;

    public StartWorkflowDomandaRoute(AzureServiceBusDynamicProcessor azureServiceBusDynamicProcessor) {
        this.azureServiceBusDynamicProcessor = azureServiceBusDynamicProcessor;
    }

    @Override
    public void configure() {
        from(CamelEndpoint.DIRECT_WORKFLOW_DOMANDA.getUri())
                .log("Avvio workflow domanda")
                .multicast().parallelProcessing().synchronous()
                .to("direct:eventoDomandaPresentata", "direct:creaFascicolo")
                .end();

        from("direct:eventoDomandaPresentata")
                .log("Invio evento domanda presentata: ${body}")
                .setHeader("QUEUE_NAME", constant(AzureQueue.EVENTO_DOMANDA_PRESENTATA.toString()))
                .marshal().json()
                .process(azureServiceBusDynamicProcessor);

        from("direct:creaFascicolo")
                .log("Invio richiesta di creazione fascicolo e attesa risposta")
                .setHeader("QUEUE_NAME", constant(AzureQueue.CREA_FASCICOLO_REQUEST.toString()))
                .marshal().json()
                .process(azureServiceBusDynamicProcessor);
    }
}