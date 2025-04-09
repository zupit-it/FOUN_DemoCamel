package org.example.routes.request;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class CreaProtocolloRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(CamelEndpoint.CREA_FASCICOLO_REPLY.getUri())
                .log("Invio richiesta di creazione protocollo e attesa risposta")
                .to(CamelEndpoint.CREA_PROTOCOLLO_REQUEST.getUri());
    }
}