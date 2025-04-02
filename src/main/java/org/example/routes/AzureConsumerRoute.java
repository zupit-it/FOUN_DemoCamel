package org.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.example.config.CustomEndpoint;
import org.springframework.stereotype.Component;

@Component
public class AzureConsumerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(CustomEndpoint.TEST.getExternalUri())
                .routeId("azureConsumerRoute")
                .log("Ricevuto da Azure SB: ${body}");
    }
}
