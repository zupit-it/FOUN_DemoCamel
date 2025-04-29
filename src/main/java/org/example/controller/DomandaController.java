package org.example.controller;

import org.example.service.DomandaService;
import org.example.service.request.DomandaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/domande")
public class DomandaController {

    private final DomandaService domandaService;

    public DomandaController(DomandaService domandaService) {
        this.domandaService = domandaService;
    }

    @PostMapping
    public ResponseEntity<String> presentareDomanda(@RequestBody DomandaRequest domandaRequest) {
        return domandaService.presentareDomanda(domandaRequest);
    }
}

