package org.example.routes.agentiesterni;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class AgenteEsternoProtocolloRoute extends RouteBuilder {

    @Override
    public void configure() {
        // happyFlow();
        unhappyFlow();
    }

    private void happyFlow() {
        from(CamelEndpoint.CREA_PROTOCOLLO_REQUEST.getUri())
                .log("Ricevuta richiesta crea protocollo")
                .delay(5000)
                .setBody(constant("{\"codice-fascicolo\": \"897431251\", \"numero-protocollo\": \"234102967\"}"))
                .setHeader("Content-Type", constant("application/json"))
                .to(CamelEndpoint.CREA_PROTOCOLLO_REPLY.getUri());
    }

    private void unhappyFlow() {
        errorHandler(
                deadLetterChannel(CamelEndpoint.CREA_PROCOTOLLO_DLQ.getUri())
                        .maximumRedeliveries(3)
                        .redeliveryDelay(2000)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
        );

        from(CamelEndpoint.CREA_PROTOCOLLO_REQUEST.getUri())
                .log("Ricevuta richiesta crea protocollo")
                .delay(5000)
                .process(exchange -> {
                    throw new RuntimeException("Errore simulato per test DLQ");
                })
                .setBody(constant("{\"codice-fascicolo\": \"897431251\", \"numero-protocollo\": \"234102967\"}"))
                .setHeader("Content-Type", constant("application/json"))
                .to(CamelEndpoint.CREA_PROTOCOLLO_REPLY.getUri());
    }
}