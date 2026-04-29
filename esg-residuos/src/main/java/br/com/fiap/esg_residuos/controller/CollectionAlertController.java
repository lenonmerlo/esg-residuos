package br.com.fiap.esg_residuos.controller;

import br.com.fiap.esg_residuos.dto.response.CollectionAlertResponse;
import br.com.fiap.esg_residuos.service.CollectionAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class CollectionAlertController {

    private final CollectionAlertService service;

    @GetMapping
    public ResponseEntity<List<CollectionAlertResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionAlertResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-point/{collectionPointId}")
    public ResponseEntity<List<CollectionAlertResponse>> findByCollectionPoint(
            @PathVariable Long collectionPointId) {
        return ResponseEntity.ok(service.findByCollectionPointId(collectionPointId));
    }
}