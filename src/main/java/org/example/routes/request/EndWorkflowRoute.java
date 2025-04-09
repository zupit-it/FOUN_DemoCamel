package org.example.routes.request;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class EndWorkflowRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(CamelEndpoint.CREA_PROTOCOLLO_REPLY.getUri())
                .log("Completamento workflow domanda")
                .multicast().parallelProcessing()
                .to("direct:depositaDocumento", "direct:depositaAllegato", "direct:eventoDomandaRegistrata");

        from("direct:depositaDocumento")
                .log("Invio comando di deposito documento")
                .to(CamelEndpoint.DEPOSITA_DOCUMENTO.getUri());

        from("direct:depositaAllegato")
                .log("Invio comando di deposito allegato")
                .to(CamelEndpoint.DEPOSITA_ALLEGATO.getUri());

        from("direct:eventoDomandaRegistrata")
                .log("Invio comando di domanda registrata")
                .to(CamelEndpoint.EVENTO_DOMANDA_REGISTRATA.getUri());
    }
}