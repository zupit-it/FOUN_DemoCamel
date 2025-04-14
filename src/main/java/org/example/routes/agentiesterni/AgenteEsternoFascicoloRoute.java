package org.example.routes.agentiesterni;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class AgenteEsternoFascicoloRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(CamelEndpoint.CREA_FASCICOLO_REQUEST.getUri())
                .log("Ricevuta richiesta crea fascicolo")
                .delay(5000)
                .setBody(constant("{\"codice-fascicolo\": \"897431251\"}"))
                .setHeader("Content-Type", constant("application/json"))
                .to(CamelEndpoint.CREA_FASCICOLO_REPLY.getUri());
    }
}