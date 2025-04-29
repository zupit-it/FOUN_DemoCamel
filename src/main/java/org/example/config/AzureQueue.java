package org.example.config;

public enum AzureQueue {
    EVENTO_DOMANDA_PRESENTATA("siag-sa-03-procedimenti-domandapresentata-request-queue"),
    CREA_FASCICOLO_REQUEST("siag-sa-03-procedimenti-creafascicolo-request-queue"),
    CREA_FASCICOLO_REPLY("siag-sa-03-procedimenti-creafascicolo-reply-queue"),
    CREA_PROTOCOLLO_REQUEST("siag-sa-03-procedimenti-creaprotocollo-request-queue"),
    CREA_PROTOCOLLO_REPLY("siag-sa-03-procedimenti-creaprotocollo-reply-queue"),
    DEPOSITA_DOCUMENTO("siag-sa-03-procedimenti-depositadocumento-request-queue"),
    DEPOSITA_ALLEGATO("siag-sa-03-procedimenti-depositaallegato-request-queue"),
    EVENTO_DOMANDA_REGISTRATA("siag-sa-03-procedimenti-domandaregistrata-request-queue");

    private final String getQueueName;

    AzureQueue(final String queueName) {
        this.getQueueName = queueName;
    }

    public String getGetQueueName() {
            return getQueueName;
    }
}
