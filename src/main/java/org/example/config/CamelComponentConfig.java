package org.example.config;

import jakarta.jms.ConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelComponentConfig {

    @Bean(name = "azureServiceBus")
    public JmsComponent jmsComponent(ConnectionFactory amqpConnectionFactory) {
        JmsComponent component = new JmsComponent();
        component.setConnectionFactory(amqpConnectionFactory);
        return component;
    }
}
