package org.example.service;

import org.apache.camel.ProducerTemplate;
import org.example.config.CamelEndpoint;
import org.example.service.request.DomandaRequest;
import org.example.service.request.validator.DomandaRequestValidator;
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
        if (!DomandaRequestValidator.isValid(domandaRequest)) {
            return ResponseEntity.badRequest().body("Richiesta non valida: dati mancanti");
        }

        final String requestId = getUuid();

        Map<String, Object> eventoDomandaPresentata = Map.of(
                "uuid", requestId,
                "stato", "Domanda presentata",
                "timestamp", Instant.now().toString(),
                "richiedente", domandaRequest.getRichiedente(),
                "domanda", domandaRequest.getDomanda()
        );

        producerTemplate.asyncSendBody(CamelEndpoint.DIRECT_WORKFLOW_DOMANDA.getUri(), eventoDomandaPresentata);

        return ResponseEntity.ok("Domanda ricevuta con ID: " + requestId);
    }

    // Andrà su DB nel caso reale
    private static String getUuid() {
        return "123e4567-e89b-12d3-a456-426614174000";
    }
}
