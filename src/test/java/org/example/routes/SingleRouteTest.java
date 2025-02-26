package org.example.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleRouteTest {

    private CamelContext context;
    private ProducerTemplate template;

    private static final String URI_NOTIFICA_DOMANDA_PRESENTATA = "mock:1";
    private static final String URI_INVIA_MESSAGGIO_CREA_FASCICOLO = "mock:2";
    private static final String URI_INVIA_MESSAGGIO_CREA_PROTOCOLLO = "mock:3";
    private static final String URI_DEPOSITA_DOCUMENTO = "mock:4";
    private static final String URI_DEPOSITA_ALLEGATO_1 = "mock:5";
    private static final String URI_DEPOSITA_ALLEGATO_2 = "mock:6";
    private static final String URI_DOMANDA_REGISTRATA = "mock:7";

    @BeforeEach
    void setUp() throws Exception {
        context = new DefaultCamelContext();
        context.addRoutes(new MyTestRoute());
        context.start();
        template = context.createProducerTemplate();
    }

    @Test
    void testDomandaPresentata() throws Exception {
        MockEndpoint mockNotificaDomanda = context.getEndpoint(URI_NOTIFICA_DOMANDA_PRESENTATA, MockEndpoint.class);
        MockEndpoint mockCreaFascicolo = context.getEndpoint(URI_INVIA_MESSAGGIO_CREA_FASCICOLO, MockEndpoint.class);
        MockEndpoint mockCreaProtocollo = context.getEndpoint(URI_INVIA_MESSAGGIO_CREA_PROTOCOLLO, MockEndpoint.class);
        MockEndpoint mockDepositaDocumento = context.getEndpoint(URI_DEPOSITA_DOCUMENTO, MockEndpoint.class);
        MockEndpoint mockDepositaAllegato1 = context.getEndpoint(URI_DEPOSITA_ALLEGATO_1, MockEndpoint.class);
        MockEndpoint mockDepositaAllegato2 = context.getEndpoint(URI_DEPOSITA_ALLEGATO_2, MockEndpoint.class);
        MockEndpoint mockDomandaRegistrata = context.getEndpoint(URI_DOMANDA_REGISTRATA, MockEndpoint.class);

        mockNotificaDomanda.expectedMessageCount(1);
        mockCreaFascicolo.expectedMessageCount(1);
        mockCreaProtocollo.setExpectedMessageCount(0);
        mockDepositaDocumento.setExpectedMessageCount(0);
        mockDepositaAllegato1.setExpectedMessageCount(0);
        mockDepositaAllegato2.setExpectedMessageCount(0);
        mockDomandaRegistrata.setExpectedMessageCount(0);

        template.sendBody("direct:start", "Domanda presentata");

        MockEndpoint.assertIsSatisfied(context);
    }


    @Test
    void testFascicoloCreato() throws Exception {
        MockEndpoint mockNotificaDomanda = context.getEndpoint(URI_NOTIFICA_DOMANDA_PRESENTATA, MockEndpoint.class);
        MockEndpoint mockCreaFascicolo = context.getEndpoint(URI_INVIA_MESSAGGIO_CREA_FASCICOLO, MockEndpoint.class);
        MockEndpoint mockCreaProtocollo = context.getEndpoint(URI_INVIA_MESSAGGIO_CREA_PROTOCOLLO, MockEndpoint.class);
        MockEndpoint mockDepositaDocumento = context.getEndpoint(URI_DEPOSITA_DOCUMENTO, MockEndpoint.class);
        MockEndpoint mockDepositaAllegato1 = context.getEndpoint(URI_DEPOSITA_ALLEGATO_1, MockEndpoint.class);
        MockEndpoint mockDepositaAllegato2 = context.getEndpoint(URI_DEPOSITA_ALLEGATO_2, MockEndpoint.class);
        MockEndpoint mockDomandaRegistrata = context.getEndpoint(URI_DOMANDA_REGISTRATA, MockEndpoint.class);

        mockNotificaDomanda.expectedMessageCount(0);
        mockCreaFascicolo.expectedMessageCount(0);
        mockCreaProtocollo.setExpectedMessageCount(1);
        mockDepositaDocumento.setExpectedMessageCount(0);
        mockDepositaAllegato1.setExpectedMessageCount(0);
        mockDepositaAllegato2.setExpectedMessageCount(0);
        mockDomandaRegistrata.setExpectedMessageCount(0);

        template.sendBody("direct:start", "Fascicolo creato");

        MockEndpoint.assertIsSatisfied(context);
    }

    @Test
    void testProtocolloCreato() throws Exception {
        MockEndpoint mockNotificaDomanda = context.getEndpoint(URI_NOTIFICA_DOMANDA_PRESENTATA, MockEndpoint.class);
        MockEndpoint mockCreaFascicolo = context.getEndpoint(URI_INVIA_MESSAGGIO_CREA_FASCICOLO, MockEndpoint.class);
        MockEndpoint mockCreaProtocollo = context.getEndpoint(URI_INVIA_MESSAGGIO_CREA_PROTOCOLLO, MockEndpoint.class);
        MockEndpoint mockDepositaDocumento = context.getEndpoint(URI_DEPOSITA_DOCUMENTO, MockEndpoint.class);
        MockEndpoint mockDepositaAllegato1 = context.getEndpoint(URI_DEPOSITA_ALLEGATO_1, MockEndpoint.class);
        MockEndpoint mockDepositaAllegato2 = context.getEndpoint(URI_DEPOSITA_ALLEGATO_2, MockEndpoint.class);
        MockEndpoint mockDomandaRegistrata = context.getEndpoint(URI_DOMANDA_REGISTRATA, MockEndpoint.class);

        mockNotificaDomanda.expectedMessageCount(0);
        mockCreaFascicolo.expectedMessageCount(0);
        mockCreaProtocollo.setExpectedMessageCount(0);
        mockDepositaDocumento.setExpectedMessageCount(1);
        mockDepositaAllegato1.setExpectedMessageCount(1);
        mockDepositaAllegato2.setExpectedMessageCount(1);
        mockDomandaRegistrata.setExpectedMessageCount(1);

        template.sendBody("direct:start", "Protocollo creato");

        MockEndpoint.assertIsSatisfied(context);
    }

    private static class MyTestRoute extends RouteBuilder {
        @Override
        public void configure() {
            from("direct:start")
                    .log("Ricevuto messaggio: ${body}")
                    .choice()
                    .when(isADomandaPresentata())
                    .process(new DomandaPresentataProcessor())
                    .multicast().parallelProcessing()
                    .to(URI_NOTIFICA_DOMANDA_PRESENTATA, URI_INVIA_MESSAGGIO_CREA_FASCICOLO)
                    .end() // Fine multicast
                    .log("Domanda presentata gestita correttamente")
                    .end() // CHIUDE IL PRIMO CHOICE

                    .choice()  // APRE UN NUOVO CHOICE, SE SERVE
                    .when(isAFascicoloCreato())
                    .process(new FascicoloCreatoProcessor())
                    .to(URI_INVIA_MESSAGGIO_CREA_PROTOCOLLO)
                    .log("Fascicolo creato gestito correttamente")

                    .when(isAProtocolloCreato())
                    .process(new ProtocolloCreatoProcessor())
                    .multicast().parallelProcessing()
                    .to(URI_DEPOSITA_DOCUMENTO, URI_DEPOSITA_ALLEGATO_1, URI_DEPOSITA_ALLEGATO_2, URI_DOMANDA_REGISTRATA)
                    .end() // Fine multicast
                    .log("Protocollo creato gestito correttamente")
                    .end();
        }

        private static Predicate isADomandaPresentata() {
            return exchange -> {
                String body = exchange.getIn().getBody(String.class);
                return "Domanda presentata".equals(body);
            };
        }

        private static Predicate isAFascicoloCreato() {
            return exchange -> {
                String body = exchange.getIn().getBody(String.class);
                return "Fascicolo creato".equals(body);
            };
        }

        private static Predicate isAProtocolloCreato() {
            return exchange -> {
                String body = exchange.getIn().getBody(String.class);
                return "Protocollo creato".equals(body);
            };
        }

        private class DomandaPresentataProcessor implements org.apache.camel.Processor {
            @Override
            public void process(Exchange exchange) throws Exception {
                // chiamate ai servizi necessari o filtri
            }
        }

        private class FascicoloCreatoProcessor implements org.apache.camel.Processor {
            @Override
            public void process(Exchange exchange) throws Exception {
                // chiamate ai servizi necessari o filtri
            }
        }

        private class ProtocolloCreatoProcessor implements org.apache.camel.Processor {
            @Override
            public void process(Exchange exchange) throws Exception {
                // chiamate ai servizi necessari o filtri
            }
        }
    }
}
