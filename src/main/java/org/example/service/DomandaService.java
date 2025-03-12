package org.example.service;

import org.apache.camel.ProducerTemplate;
import org.example.config.CustomEndpoint;
import org.example.service.request.DomandaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class DomandaService {

    private final ProducerTemplate producerTemplate;

    public DomandaService(final ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public ResponseEntity<String> presentareDomanda(DomandaRequest domandaRequest) {
        if (domandaRequest == null || domandaRequest.getRichiedente() == null || domandaRequest.getDomanda() == null) {
            return ResponseEntity.badRequest().body("Richiesta non valida: dati mancanti");
        }

        String requestId = UUID.randomUUID().toString();

        Map<String, Object> eventoDomandaPresentata = Map.of(
                "uuid", requestId,
                "stato", "Domanda presentata",
                "timestamp", Instant.now().toString(),
                "richiedente", domandaRequest.getRichiedente(),
                "domanda", domandaRequest.getDomanda()
        );

        producerTemplate.asyncSendBody(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri(), eventoDomandaPresentata);

        return ResponseEntity.ok("Domanda ricevuta con ID: " + requestId);
    }
}
