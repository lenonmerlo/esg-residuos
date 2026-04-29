package br.com.fiap.esg_residuos.controller;

import br.com.fiap.esg_residuos.dto.request.CollectionRequest;
import br.com.fiap.esg_residuos.dto.response.CollectionResponse;
import br.com.fiap.esg_residuos.service.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService service;

    @GetMapping
    public ResponseEntity<List<CollectionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CollectionResponse> create(@RequestBody @Valid CollectionRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<CollectionResponse> update(@PathVariable Long id,
                                                     @RequestBody @Valid CollectionRequest dto) {
        return ResponseEntity.ok(service.update(id,dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
