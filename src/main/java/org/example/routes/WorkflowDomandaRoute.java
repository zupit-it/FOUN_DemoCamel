package org.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.example.config.CustomEndpoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WorkflowDomandaRoute extends RouteBuilder {

    private static final String SAP_EVENT_MESH_URL = "https://siag-d9aa9b13-42d2-4ef2-87cd-755d5bd74839.eu20.a.eventmesh.integration.cloud.sap:1443/messagingrest/v1/queues/";

    @Override
    public void configure() {
        from(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri())
                .log("Avvio workflow domanda")
                .multicast().parallelProcessing()
                .to("direct:eventoDomandaPresentata", "direct:creaFascicolo")
                .end();

        from("direct:eventoDomandaPresentata")
                // .marshal().json(JsonLibrary.Jackson)
                .log("Invio evento domanda presentata: ${body}")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.EVENTO_DOMANDA_PRESENTATA, exchange.getIn().getBody(String.class)));

        from("direct:creaFascicolo")
                .log("Invio richiesta di creazione fascicolo e attesa risposta")
                .process(exchange -> {
                    final String response = sendMessageToQueue(CustomEndpoint.CREA_FASCICOLO, exchange.getIn().getBody(String.class));
                    exchange.getIn().setBody(response);
                })
                .log("Risposta ricevuta per la creazione del fascicolo: ${body}")
                .to("direct:creaProtocollo");

        from("direct:creaProtocollo")
                .log("Invio richiesta di creazione protocollo e attesa risposta")
                .process(exchange -> {
                    final String response = sendMessageToQueue(CustomEndpoint.CREA_PROTOCOLLO, exchange.getIn().getBody(String.class));
                    exchange.getIn().setBody(response);
                })
                .log("Risposta ricevuta per la creazione del protocollo: ${body}")
                .multicast().parallelProcessing()
                .to("direct:depositaDocumento", "direct:depositaAllegato", "direct:eventoDomandaRegistrata");

        from("direct:depositaDocumento")
                .log("Invio comando di deposito documento")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.DEPOSITA_DOCUMENTO, exchange.getIn().getBody(String.class)));

        from("direct:depositaAllegato")
                .log("Invio comando di deposito allegato")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.DEPOSITA_ALLEGATO, exchange.getIn().getBody(String.class)));

        from("direct:eventoDomandaRegistrata")
                .log("Invio comando di deposito allegato")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.EVENTO_DOMANDA_PRESENTATA, exchange.getIn().getBody(String.class)));
    }

    private String sendMessageToQueue(CustomEndpoint queue, String body) {
        final String url = SAP_EVENT_MESH_URL + queue.getExternalUri() + "/messages";
        System.out.println("Sending message to endpoint: " + url);
        System.out.println("Sending body: " + body);

        final String response = switch (queue) {
            case CREA_FASCICOLO -> "123";
            case CREA_PROTOCOLLO -> "456";
            case DEPOSITA_DOCUMENTO -> "documento acquisito";
            case DEPOSITA_ALLEGATO -> "allegato acquisito";
            case EVENTO_DOMANDA_REGISTRATA -> "domanda domanda registrata";
            default -> "";
        };

        System.out.println("Response: " + response);
        return response;
    }

    private static void realSending(String body, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-qos", "1");
        headers.setBearerAuth("TOKEN_OAUTH2");

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}

