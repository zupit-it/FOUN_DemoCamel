package org.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.example.config.CamelContextBean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import java.util.logging.Logger;

@Singleton
@Startup
public class JmsRouteBuilder {
    private static final Logger LOGGER = Logger.getLogger(JmsRouteBuilder.class.getName());

    @Inject
    private CamelContextBean camelContextBean;  // Ora iniettiamo CamelContextBean

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @PostConstruct
    public void init() {
        try {
            LOGGER.info("Avvio Apache Camel...");

            camelContextBean.getContext().addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

            LOGGER.info("Componenti registrati in Camel: " + camelContextBean.getContext().getComponentNames());

            camelContextBean.getContext().addRoutes(getRouteBuilder("jms:queue:testQueue", 1));

            camelContextBean.getContext().addRoutes(getRouteBuilder("jms:queue:testQueue2", 2));

            camelContextBean.getContext().addRoutes(getRouteBuilder("jms:queue:testQueue3", 3));

            LOGGER.info("Apache Camel avviato con successo!");
        } catch (Exception e) {
            LOGGER.severe("Errore avviando Camel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static RouteBuilder getRouteBuilder(final String uri, final int indexQueue) {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(uri)
                        .log("[CODA " + indexQueue + "] - Ricevuto messaggio: ${body}");
            }
        };
    }
}