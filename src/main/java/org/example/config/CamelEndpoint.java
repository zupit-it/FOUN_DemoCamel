package org.example.config;

public enum CamelEndpoint {
    CREA_FASCICOLO_REQUEST("azureServiceBus:queue:siag-sa-03-procedimenti-creafascicolo-request-queue"),
    CREA_FASCICOLO_REPLY("azureServiceBus:queue:siag-sa-03-procedimenti-creafascicolo-reply-queue"),

    CREA_PROTOCOLLO_REQUEST("azureServiceBus:queue:siag-sa-03-procedimenti-creaprotocollo-request-queue"),
    CREA_PROTOCOLLO_REPLY("azureServiceBus:queue:siag-sa-03-procedimenti-creaprotocollo-reply-queue"),
    CREA_PROCOTOLLO_DLQ("azureServiceBus:queue:siag-sa-03-dead-letter-queue"),

    DEPOSITA_DOCUMENTO_REQUEST("azureServiceBus:queue:siag-sa-03-procedimenti-depositadocumento-request-queue"),
    DEPOSITA_DOCUMENTO_REPLY("azureServiceBus:queue:siag-sa-03-procedimenti-depositadocumento-reply-queue"),
    DEPOSITA_ALLEGATO_REQUEST("azureServiceBus:queue:siag-sa-03-procedimenti-depositaallegato-request-queue"),
    DEPOSITA_ALLEGATO_REPLY("azureServiceBus:queue:siag-sa-03-procedimenti-depositaallegato-reply-queue"),
    EVENTO_DOMANDA_REGISTRATA("azureServiceBus:queue:siag-sa-03-procedimenti-domandaregistrata-request-queue");

    private final String uri;

    CamelEndpoint(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
