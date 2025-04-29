package org.example.service;

import org.apache.camel.ProducerTemplate;
import org.example.config.CamelEndpoint;
import org.example.service.request.DomandaRequest;
import org.example.service.request.validator.DomandaRequestValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DomandaService {

    @Value("${test.frequency}")
    private Integer stressTestFrequencyInMilliseconds;
    @Value("${test.request.max}")
    private Integer stressTestMaximumNumber;

    private final ProducerTemplate producerTemplate;
    private int counter = 0;

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

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(getRunnable(executorService, eventoDomandaPresentata), 0, stressTestFrequencyInMilliseconds, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok("Domanda ricevuta con ID: " + requestId);
    }

    // Andrà su DB nel caso reale
    private static String getUuid() {
        return "123e4567-e89b-12d3-a456-426614174000";
    }

    private Runnable getRunnable(ScheduledExecutorService executorService, Map<String, Object> eventoDomandaPresentata) {
        return () -> {
            if (counter == stressTestMaximumNumber){
                executorService.shutdown();
                return;
            } else {
                producerTemplate.asyncSendBody(CamelEndpoint.DIRECT_WORKFLOW_DOMANDA.getUri(), eventoDomandaPresentata);
                counter++;
                System.out.println("Richiesta numero " + counter);
            }
        };
    }
}
