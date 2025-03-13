package org.example.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.example.config.CustomEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class WorkflowDomandaRoute extends RouteBuilder {

    private static final String SAP_EVENT_MESH_URL = "https://siag-d9aa9b13-42d2-4ef2-87cd-755d5bd74839.eu20.a.eventmesh.integration.cloud.sap:1443/messagingrest/v1/queues/";
    private String skip = "";

    @Override
    public void configure() {
        from(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri())
                .log("Avvio workflow domanda")
                .multicast().parallelProcessing()
                .to("direct:eventoDomandaPresentata", "direct:creaFascicolo")
                .end();

        from("direct:eventoDomandaPresentata")
                .marshal().json(JsonLibrary.Jackson)
                .log("Invio evento domanda presentata: ${body}")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.EVENTO_DOMANDA_PRESENTATA, exchange.getIn().getBody(Map.class)));

        from("direct:creaFascicolo")
                .log("Invio richiesta di creazione fascicolo e attesa risposta")
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    final Map<String, Object> response = sendMessageToQueue(CustomEndpoint.CREA_FASCICOLO, exchange.getIn().getBody(Map.class));
                    exchange.getIn().setBody(response);
                })
                .log("Risposta ricevuta per la creazione del fascicolo: ${body}")
                .to("direct:creaProtocollo");

        from("direct:creaProtocollo")
                .log("Invio richiesta di creazione protocollo e attesa risposta")
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    final Map<String, Object> response = sendMessageToQueue(CustomEndpoint.CREA_PROTOCOLLO, exchange.getIn().getBody(Map.class));
                    exchange.getIn().setBody(response);
                })
                .log("Risposta ricevuta per la creazione del protocollo: ${body}")
                .multicast().parallelProcessing()
                .to("direct:depositaDocumento", "direct:depositaAllegato", "direct:eventoDomandaRegistrata");

        from("direct:depositaDocumento")
                .log("Invio comando di deposito documento")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.DEPOSITA_DOCUMENTO, exchange.getIn().getBody(Map.class)));

        from("direct:depositaAllegato")
                .log("Invio comando di deposito allegato")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.DEPOSITA_ALLEGATO, exchange.getIn().getBody(Map.class)));

        from("direct:eventoDomandaRegistrata")
                .log("Invio comando di domanda registrata")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.EVENTO_DOMANDA_REGISTRATA, exchange.getIn().getBody(Map.class)));
    }

    private Map<String, Object> sendMessageToQueue(CustomEndpoint queue, Map<String, Object> body) throws InterruptedException {
        final String url = SAP_EVENT_MESH_URL + queue.getExternalUri() + "/messages";
        System.out.println("Sending message to endpoint: " +  url);
        System.out.println("Sending body: " + body);

        List<CustomEndpoint> endpointsWithParallelOperations = List.of(CustomEndpoint.DEPOSITA_ALLEGATO, CustomEndpoint.DEPOSITA_DOCUMENTO, CustomEndpoint.EVENTO_DOMANDA_REGISTRATA);

        if (skip.isEmpty()){
            skip = String.valueOf(body.get("skip"));
        }

        // Introduco un delay di 2 secondi sulla risposta per verificare che le operazioni vadano in parallelo
        if (endpointsWithParallelOperations.contains(queue)){
            Thread.sleep(2000);
        }

        if (CustomEndpoint.CREA_FASCICOLO == queue){
            // Introduco un delay di 10 secondi sulla risposta per verificare che il flusso non proceda
            if (skip.equals("fascicolo")){
                Thread.sleep(10000);
            }

            // Introduco un delay di 2 secondi sulla risposta per verificare che le operazioni finali partano se e solo se ho ricevuto la risposta da protocollo
            Thread.sleep(2000);
        }

        if (CustomEndpoint.CREA_PROTOCOLLO == queue){
            // Introduco un delay di 10 secondi sulla risposta per verificare che il flusso non proceda
            if (skip.equals("protocollo")){
                Thread.sleep(10000);
            }

            // Introduco un delay di 2 secondo sulla risposta per verificare che le operazioni finali partano se e solo se ho ricevuto la risposta da protocollo
            Thread.sleep(2000);
        }

        final Map<String, Object> response = switch (queue) {
            case CREA_FASCICOLO -> Map.of("codice-fascicolo", "123");
            case CREA_PROTOCOLLO -> Map.of("codice-fascicolo", "123", "codice-protocollo", "456");
            case DEPOSITA_DOCUMENTO -> Map.of("esito", "documento acquisito");
            case DEPOSITA_ALLEGATO -> Map.of("esito", "allegato acquisito");
            case EVENTO_DOMANDA_REGISTRATA -> Map.of(
                    "uuid", "random",
                    "stato", "Domanda registrata",
                    "timestamp", Instant.now().toString(),
                    "richiedente", "Giovanni Pepe",
                    "fascicolazione", "info fascicolazione", "protocollazione", "info protocollazione"
            );
            default -> null;
        };

        System.out.println("Response: " + response);
        return response;
    }
}

