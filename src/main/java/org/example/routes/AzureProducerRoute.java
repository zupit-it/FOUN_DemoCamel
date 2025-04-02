package org.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CustomEndpoint;
import org.springframework.stereotype.Component;

@Component
public class AzureProducerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri())
                .routeId("azureSenderRoute")
                .setBody(constant("Ciao da Giovanni & AQMP Camel"))
                .to(CustomEndpoint.TEST.getExternalUri())
                .log("Messaggio inviato a Azure SB: ${body}");
    }
}