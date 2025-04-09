package org.example.config;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.jms.ConnectionFactory;

@Configuration
public class AzureServiceBusConfig {

    @Bean
    public ConnectionFactory amqpConnectionFactory() {
        String connectionURI = "amqps://siag-sa-03-procedimenti.servicebus.windows.net";

        JmsConnectionFactory factory = new JmsConnectionFactory(connectionURI);
        factory.setUsername("RootManageSharedAccessKey");
        factory.setPassword("");

        return factory;
    }
}

