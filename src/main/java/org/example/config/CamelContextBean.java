package org.example.config;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@ApplicationScoped
public class CamelContextBean {
    private static final Logger LOGGER = Logger.getLogger(CamelContextBean.class.getName());

    private CamelContext camelContext;

    @PostConstruct
    public void init() {
        try {
            camelContext = new DefaultCamelContext();
            camelContext.start();
            LOGGER.info("CamelContext creato e avviato con successo!");
        } catch (Exception e) {
            LOGGER.severe("Errore durante l'avvio di Camel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CamelContext getContext() {
        return camelContext;
    }

    @PreDestroy
    public void shutdown() {
        try {
            if (camelContext != null) {
                camelContext.stop();
                LOGGER.info("CamelContext fermato.");
            }
        } catch (Exception e) {
            LOGGER.severe("Errore durante lo shutdown di Camel: " + e.getMessage());
        }
    }
}
