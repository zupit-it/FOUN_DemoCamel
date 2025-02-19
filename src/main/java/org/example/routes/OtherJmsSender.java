package org.example.routes;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class OtherJmsSender {
    public static void main(String[] args) throws Exception {
        // Configurazione per WildFly JNDI
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http-remoting://localhost:8080"); // Porta predefinita di WildFly
        env.put("jboss.naming.client.ejb.context", "true");

        // Creare il contesto JNDI
        InitialContext ctx = new InitialContext(env);

        // Ottenere la ConnectionFactory e la Queue con il nome corretto
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
        Queue queue = (Queue) ctx.lookup("java:/jms/queue/testQueue2");

        // Creare la connessione JMS
        Connection connection = factory.createConnection("jmsuser", "IlCavalloRosso1.");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(queue);

        // Inviare il messaggio
        TextMessage message = session.createTextMessage("Messaggio di test da Java alla coda 2!");
        producer.send(message);

        System.out.println("Messaggio inviato con successo alla coda 2!");

        session.close();
        connection.close();
    }
}
