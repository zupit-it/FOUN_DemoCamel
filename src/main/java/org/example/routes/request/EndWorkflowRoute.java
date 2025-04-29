package org.example.routes.request;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.AzureQueue;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EndWorkflowRoute extends RouteBuilder {

    private final AzureServiceBusDynamicProcessor azureServiceBusDynamicProcessor;

    public EndWorkflowRoute(AzureServiceBusDynamicProcessor azureServiceBusDynamicProcessor) {
        this.azureServiceBusDynamicProcessor = azureServiceBusDynamicProcessor;
    }

    @Override
    public void configure() {
        from(CamelEndpoint.DIRECT_END_WORKFLOW.getUri())
                .log("Completamento workflow domanda")
                .multicast().parallelProcessing().synchronous()
                .to("direct:depositaDocumento", "direct:depositaAllegato", "direct:eventoDomandaRegistrata");

        from("direct:depositaDocumento")
                .log("Invio comando di deposito documento")
                .setHeader("QUEUE_NAME", constant(AzureQueue.DEPOSITA_DOCUMENTO.toString())).process(azureServiceBusDynamicProcessor);

        from("direct:depositaAllegato")
                .log("Invio comando di deposito allegato")
                .setHeader("QUEUE_NAME", constant(AzureQueue.DEPOSITA_ALLEGATO.toString())).process(azureServiceBusDynamicProcessor);

        from("direct:eventoDomandaRegistrata")
                .log("Invio comando di domanda registrata")
                .setBody(exchange -> {
                    Map<String, Object> body = new LinkedHashMap<>();
                    body.put("uuid", "123e4567-e89b-12d3-a456-426614174000");
                    body.put("stato", "Domanda registrata");
                    body.put("timestamp", java.time.Instant.now().toString());

                    Map<String, String> fascicolazione = new HashMap<>();
                    fascicolazione.put("codice-fascicolo", "897431251");
                    fascicolazione.put("numero-protocollo", "234102967");

                    body.put("fascicolazione", fascicolazione);
                    return body;
                })
                .marshal().json()
                .setHeader("QUEUE_NAME", constant(AzureQueue.EVENTO_DOMANDA_REGISTRATA.toString())).process(azureServiceBusDynamicProcessor);
    }
}