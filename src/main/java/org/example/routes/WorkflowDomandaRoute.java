package org.example.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.example.config.CustomEndpoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;

@Component
public class WorkflowDomandaRoute extends RouteBuilder {

    private static final String SAP_EVENT_MESH_URL = "https://siag-d9aa9b13-42d2-4ef2-87cd-755d5bd74839.eu20.a.eventmesh.integration.cloud.sap:1443/messagingrest/v1/queues/";
    private String skip = "";
    private static final String token = "eyJhbGciOiJSUzI1NiIsImprdSI6Imh0dHBzOi8vc2lhZy5hdXRoZW50aWNhdGlvbi5ldTIwLmhhbmEub25kZW1hbmQuY29tL3Rva2VuX2tleXMiLCJraWQiOiJkZWZhdWx0LWp3dC1rZXktLTE0OTI0Nzc2MDYiLCJ0eXAiOiJKV1QiLCJqaWQiOiAiK0FETFhIRHlCWEVsNUYzVk9XYlBPcE5KZG1ZeUx4aHY1RWVlWHpJQ2IyQT0ifQ.eyJqdGkiOiIyNjFmOWI4ODdmMjQ0NTUxYTQxOThhODNjY2I2YmYxOCIsImV4dF9hdHRyIjp7ImVuaGFuY2VyIjoiWFNVQUEiLCJzdWJhY2NvdW50aWQiOiJkOWFhOWIxMy00MmQyLTRlZjItODdjZC03NTVkNWJkNzQ4MzkiLCJ6ZG4iOiJzaWFnIiwic2VydmljZWluc3RhbmNlaWQiOiI0MTE3OGMwMi0yZGE3LTQ1OWUtYjk3ZS1hMjAzZWM5ZWJiMTAifSwic3ViIjoic2ItREIwMkMyQUItNTg0Ny00Q0IxLUJERUMtNzg5Qjc2RUNFMzI2LTQxMTc4YzAyLTJkYTctNDU5ZS1iOTdlLWEyMDNlYzllYmIxMCFiMTAxMjN8ZXZlbnRpbmcteHN1YWEtYnJva2VyIWI2Mjg3MiIsImF1dGhvcml0aWVzIjpbInVhYS5yZXNvdXJjZSJdLCJzY29wZSI6WyJ1YWEucmVzb3VyY2UiXSwiY2xpZW50X2lkIjoic2ItREIwMkMyQUItNTg0Ny00Q0IxLUJERUMtNzg5Qjc2RUNFMzI2LTQxMTc4YzAyLTJkYTctNDU5ZS1iOTdlLWEyMDNlYzllYmIxMCFiMTAxMjN8ZXZlbnRpbmcteHN1YWEtYnJva2VyIWI2Mjg3MiIsImNpZCI6InNiLURCMDJDMkFCLTU4NDctNENCMS1CREVDLTc4OUI3NkVDRTMyNi00MTE3OGMwMi0yZGE3LTQ1OWUtYjk3ZS1hMjAzZWM5ZWJiMTAhYjEwMTIzfGV2ZW50aW5nLXhzdWFhLWJyb2tlciFiNjI4NzIiLCJhenAiOiJzYi1EQjAyQzJBQi01ODQ3LTRDQjEtQkRFQy03ODlCNzZFQ0UzMjYtNDExNzhjMDItMmRhNy00NTllLWI5N2UtYTIwM2VjOWViYjEwIWIxMDEyM3xldmVudGluZy14c3VhYS1icm9rZXIhYjYyODcyIiwiZ3JhbnRfdHlwZSI6ImNsaWVudF9jcmVkZW50aWFscyIsInJldl9zaWciOiIyOTY3ZjFjNiIsImlhdCI6MTc0MjIwNjM4NSwiZXhwIjoxNzQyMjQ5NTg1LCJpc3MiOiJodHRwczovL3NpYWcuYXV0aGVudGljYXRpb24uZXUyMC5oYW5hLm9uZGVtYW5kLmNvbS9vYXV0aC90b2tlbiIsInppZCI6ImQ5YWE5YjEzLTQyZDItNGVmMi04N2NkLTc1NWQ1YmQ3NDgzOSIsImF1ZCI6WyJ1YWEiLCJzYi1EQjAyQzJBQi01ODQ3LTRDQjEtQkRFQy03ODlCNzZFQ0UzMjYtNDExNzhjMDItMmRhNy00NTllLWI5N2UtYTIwM2VjOWViYjEwIWIxMDEyM3xldmVudGluZy14c3VhYS1icm9rZXIhYjYyODcyIl19.qBHTLjZMOi2I1aje9O9fWgJheCbDIjoUm4lz_KbrmuqZ5fzOeYYiBcUhGM8WWZFNM3Ntwl2cweE0TB3W-L7gxbAHP4vQunMD1pk9Ryvdx1r7-dGTU8wuQH-pVrEg6jeVDVj-FjYpZktXCFEKyBJIyTGOjdqPHnxaBM_5PEZS2qeuZ9RJz3wvsmUd0C-KDNRvvBhg1WU9CWeuB1e2KmEBVZk--wEoWYMljMH16Cg37LPN35qafu078WIfIaHoy5ADnfLPp1oZSyUQqC_ZOFmbeoP6kjmpE7xPbk9-qZKXdgeC9dd5jWtZj3-IB_UMU4KVRC_6hI584-fLZ-BQEGAfmg";
    private static final String randomCodiceFascicolo = randomDigits(4);
    private static final String randomCodiceProtocollo = randomDigits(4);
    private static String uuid = "";
    @Override
    public void configure() {
        from(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri())
                .log("Avvio workflow domanda")
                .multicast().parallelProcessing()
                .to("direct:eventoDomandaPresentata", "direct:creaFascicolo")
                .end();

        from("direct:eventoDomandaPresentata")
                .log("Invio evento domanda presentata: ${body}")
                .process(exchange -> sendMessageToQueue(CustomEndpoint.EVENTO_DOMANDA_PRESENTATA, exchange.getIn().getBody(Map.class)));

        from("direct:creaFascicolo")
                .log("Invio richiesta di creazione fascicolo e attesa risposta")
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    sendMessageToQueue(CustomEndpoint.CREA_FASCICOLO, exchange.getIn().getBody(Map.class));
                    final String response = getMessageFromQueue(CustomEndpoint.CREA_FASCICOLO, exchange.getIn().getBody(Map.class));
                    exchange.getIn().setBody(response);
                })
                .log("Risposta ricevuta per la creazione del fascicolo: ${body}")
                .to("direct:creaProtocollo");

        from("direct:creaProtocollo")
                .log("Invio richiesta di creazione protocollo e attesa risposta")
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    sendMessageToQueue(CustomEndpoint.CREA_PROTOCOLLO, exchange.getIn().getBody(Map.class));
                    final String response = getMessageFromQueue(CustomEndpoint.CREA_PROTOCOLLO, exchange.getIn().getBody(Map.class));
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

    private void sendMessageToQueue(CustomEndpoint queue, Map<String, Object> body) throws IOException, InterruptedException {
        final String uri = SAP_EVENT_MESH_URL + queue.getExternalUri() + "/messages";
        getStringHttpResponse(uri, "1", CustomEndpoint.CREA_FASCICOLO.equals(queue) ? "1": "2");
    }

    private String getMessageFromQueue(CustomEndpoint queue, Map<String, Object> body) throws InterruptedException, IOException {

        final String uri = SAP_EVENT_MESH_URL + queue.getExternalUri() + "/messages/consumption";
        boolean isCreaFascicolo = false;

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
            isCreaFascicolo = true;
        }

        if (CustomEndpoint.CREA_PROTOCOLLO == queue){
            // Introduco un delay di 10 secondi sulla risposta per verificare che il flusso non proceda
            if (skip.equals("protocollo")){
                Thread.sleep(10000);
            }

            // Introduco un delay di 2 secondo sulla risposta per verificare che le operazioni finali partano se e solo se ho ricevuto la risposta da protocollo
            Thread.sleep(2000);

        }

        HttpResponse<String> response = getStringHttpResponse(uri, "0", isCreaFascicolo ? "1" : "2");

        // response = getMockedResponse(queue);

        System.out.println("Response: " + response.body());
        return response.body();
    }

    private static HttpResponse<String> getStringHttpResponse(String uri, String xQosValue, String mode) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String body = "{}";

        if (mode == "1"){
            body = String.format("{\"codice-fascicolo\": \"%s\"}", randomCodiceFascicolo);
        } else if (mode == "2"){
            body = String.format("{\"codice-fascicolo\": \"%s\", \"numero-protocollo\": \"%s\" }", randomCodiceFascicolo, randomCodiceProtocollo);
        }

        System.out.println("Sending message to endpoint: " +  uri);
        System.out.println("Sending body: " + body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Authorization", "Bearer " + token)
                .header("x-qos", xQosValue)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private static String randomDigits(int lunghezza) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lunghezza; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private static Map<String, Object> getMockedResponse(CustomEndpoint queue) {
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

