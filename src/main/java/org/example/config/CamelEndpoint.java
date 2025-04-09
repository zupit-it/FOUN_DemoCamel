package org.example.config;

public enum CamelEndpoint {
    DIRECT_WORKFLOW_DOMANDA("direct:workflowDomanda"),
    EVENTO_DOMANDA_PRESENTATA("azureServiceBus:queue:siag-sa-03-procedimenti-domandapresentata-request-queue"),
    CREA_FASCICOLO_REQUEST("azureServiceBus:queue:siag-sa-03-procedimenti-creafascicolo-request-queue"),

    CREA_FASCICOLO_REPLY("azureServiceBus:queue:siag-sa-03-procedimenti-creafascicolo-reply-queue"),
    CREA_PROTOCOLLO_REQUEST("azureServiceBus:queue:siag-sa-03-procedimenti-creaprotocollo-request-queue"),

    CREA_PROTOCOLLO_REPLY("azureServiceBus:queue:siag-sa-03-procedimenti-creaprotocollo-reply-queue"),
    DEPOSITA_DOCUMENTO("azureServiceBus:queue:siag-sa-03-procedimenti-depositadocumento-request-queue"),
    DEPOSITA_ALLEGATO("azureServiceBus:queue:siag-sa-03-procedimenti-depositaallegato-request-queue"),
    EVENTO_DOMANDA_REGISTRATA("azureServiceBus:queue:siag-sa-03-procedimenti-domandaregistrata-request-queue");

    private final String uri;

    CamelEndpoint(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
