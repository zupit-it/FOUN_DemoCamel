package org.example.service.request.validator;

import org.example.service.request.DomandaRequest;
import org.springframework.http.ResponseEntity;

public class DomandaRequestValidator {

    private DomandaRequestValidator() {
    }

    public static boolean isValid(DomandaRequest domandaRequest) {
        return domandaRequest != null && domandaRequest.getRichiedente() != null && domandaRequest.getDomanda() != null;
    }
}
