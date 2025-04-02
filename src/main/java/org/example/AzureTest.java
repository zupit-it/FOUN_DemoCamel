package org.example;

import com.azure.messaging.servicebus.*;

public class AzureTest {
    public static void main(String[] args) {
        String connectionString = "INSERIRE_CONNECTION_STRING";
        String queueName = "siag-sa-03-procedimenti-creafascicolo-queue";

        try {
            ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .queueName(queueName)
                    .buildClient();

            senderClient.sendMessage(new ServiceBusMessage("Ciao da Java!"));

            System.out.println("✅ Messaggio inviato con successo");

        } catch (Exception e) {
            System.err.println("❌ Errore durante l'invio:");
            e.printStackTrace();
        }
    }
}
