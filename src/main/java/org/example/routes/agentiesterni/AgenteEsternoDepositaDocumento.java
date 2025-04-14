package org.example.routes.agentiesterni;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class AgenteEsternoDepositaDocumento extends RouteBuilder {

    @Override
    public void configure() {
        from(CamelEndpoint.DEPOSITA_DOCUMENTO_REQUEST.getUri())
                .log("Ricevuta richiesta deposita documento")
                .delay(5000)
                .setBody(constant("{\"esito\": \"documento acquisito\"}"))
                .setHeader("Content-Type", constant("application/json"))
                .to(CamelEndpoint.DEPOSITA_DOCUMENTO_REPLY.getUri());
    }
}