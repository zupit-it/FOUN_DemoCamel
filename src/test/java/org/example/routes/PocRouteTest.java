package org.example.routes;


import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.example.config.CustomEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testDomandaPresentata() throws Exception {

        template.sendBody(CustomEndpoint.DIRECT_WORKFLOW_DOMANDA.getInternalUri(), "Domanda presentata");

        MockEndpoint.assertIsSatisfied(context);
    }
}
