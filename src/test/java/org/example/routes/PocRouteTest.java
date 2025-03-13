package org.example.routes;


import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.example.config.CustomEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

class PocRouteTest {

    private CamelContext context;
    private ProducerTemplate template;

    @BeforeEach
    void setUp() throws Exception {
        context = new DefaultCamelContext();
        context.addRoutes(new WorkflowDomandaRoute());
        context.start();
        template = context.createProducerTemplate();
    }

    @Test
    void testHappyFlow() throws Exception {
        template.sendBody(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri(), getBody());
    }

    @Test
    void testCreaFascicoloNotResponding() throws Exception {
        template.sendBody(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri(), getBodyWithFailingCreaFascicolo());
    }

    @Test
    void testCreaProtocolloNotResponding() throws Exception {
        template.sendBody(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri(), getBodyWithFailingCreaProtocollo());
    }

    private Map<String, Object> getBody(){
        return Map.of(
                "uuid", UUID.randomUUID().toString(),
                "stato", "Domanda presentata",
                "timestamp", Instant.now().toString(),
                "richiedente", "Giovanni Pepe",
                "domanda", "Domanda 1234", "skip", "nothing");
    }

    private Map<String, Object> getBodyWithFailingCreaFascicolo(){
        return Map.of(
                "uuid", UUID.randomUUID().toString(),
                "stato", "Domanda presentata",
                "timestamp", Instant.now().toString(),
                "richiedente", "Giovanni Pepe",
                "domanda", "Domanda 1234", "skip", "fascicolo");
    }

    private Map<String, Object> getBodyWithFailingCreaProtocollo(){
        return Map.of(
                "uuid", UUID.randomUUID().toString(),
                "stato", "Domanda presentata",
                "timestamp", Instant.now().toString(),
                "richiedente", "Giovanni Pepe",
                "domanda", "Domanda 1234", "skip", "protocollo");
    }
}
