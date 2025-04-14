package org.example.routes.agentiesterni;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class AgenteEsternoDepositaAllegato extends RouteBuilder {

    @Override
    public void configure() {
        from(CamelEndpoint.DEPOSITA_ALLEGATO_REQUEST.getUri())
                .log("Ricevuta richiesta deposita allegato")
                .delay(5000)
                .setBody(constant("{\"esito\": \"allegato acquisito\"}"))
                .setHeader("Content-Type", constant("application/json"))
                .to(CamelEndpoint.DEPOSITA_ALLEGATO_REPLY.getUri());
    }
}