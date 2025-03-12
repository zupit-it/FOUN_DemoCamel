package org.example.config;

public enum CustomEndpoint {
    DIRECT_WORKFLOW_DOMANDA("direct:workflowDomanda", null),
    EVENTO_DOMANDA_PRESENTATA("direct:eventoDomandaPresentata", "sa03_Procedimenti_Eventi_ToSiag"),
    CREA_FASCICOLO("direct:creaFascicolo", "sa03_Procedimenti_CreaFascicolo_ToSiag"),
    CREA_PROTOCOLLO("direct:creaProtocollo", "sa03_Procedimenti_CreaProtocollo_ToSiag"),
    DEPOSITA_DOCUMENTO("direct:depositaDocumento", "sa03_Procedimenti_DepositaDocumento_ToSiag"),
    DEPOSITA_ALLEGATO("direct:depositaAllegato", "sa03_Procedimenti_DepositaAllegato_ToSiag"),
    EVENTO_DOMANDA_REGISTRATA("direct:eventoDomandaRegistrata", "sa03_Procedimenti_Eventi_ToSiag");

    private final String internalUri;
    private final String externalUri;

    CustomEndpoint(String internalUri, String externalUri) {
        this.internalUri = internalUri;
        this.externalUri = externalUri;
    }

    public String getInternalUri() {
        return internalUri;
    }

    public String getExternalUri() {
        return externalUri;
    }
}
