package org.example.config;

public enum CamelEndpoint {
    DIRECT_WORKFLOW_DOMANDA("direct:workflowDomanda"),
    DIRECT_CREA_PROTOCOLLO_ROUTE("direct:protocollo-route"),
    DIRECT_END_WORKFLOW("direct:endworkflow");

    private final String uri;

    CamelEndpoint(final String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
