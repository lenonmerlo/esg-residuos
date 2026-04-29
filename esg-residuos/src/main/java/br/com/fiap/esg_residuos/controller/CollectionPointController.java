package br.com.fiap.esg_residuos.controller;

import br.com.fiap.esg_residuos.dto.request.CollectionPointRequest;
import br.com.fiap.esg_residuos.dto.response.CollectionPointResponse;
import br.com.fiap.esg_residuos.service.CollectionPointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection-points")
@RequiredArgsConstructor
public class CollectionPointController {

    private final CollectionPointService service;

    @GetMapping
    public ResponseEntity<List<CollectionPointResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionPointResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CollectionPointResponse> create(@RequestBody @Valid CollectionPointRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionPointResponse> update(@PathVariable Long id,
                                                          @RequestBody @Valid CollectionPointRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
