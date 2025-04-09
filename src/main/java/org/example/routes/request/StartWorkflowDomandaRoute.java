package org.example.routes.request;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CamelEndpoint;
import org.springframework.stereotype.Component;

@Component
public class StartWorkflowDomandaRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(CamelEndpoint.DIRECT_WORKFLOW_DOMANDA.getUri())
                .log("Avvio workflow domanda")
                .multicast().parallelProcessing()
                .to("direct:eventoDomandaPresentata", "direct:creaFascicolo")
                .end();

        from("direct:eventoDomandaPresentata")
                .log("Invio evento domanda presentata: ${body}")
                .marshal().json()
                .to(CamelEndpoint.EVENTO_DOMANDA_PRESENTATA.getUri());

        from("direct:creaFascicolo")
                .log("Invio richiesta di creazione fascicolo e attesa risposta")
                .marshal().json()
                .to(CamelEndpoint.CREA_FASCICOLO_REQUEST.getUri());
    }
}