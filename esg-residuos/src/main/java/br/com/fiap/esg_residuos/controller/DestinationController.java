package br.com.fiap.esg_residuos.controller;

import br.com.fiap.esg_residuos.dto.request.DestinationRequest;
import br.com.fiap.esg_residuos.dto.response.DestinationResponse;
import br.com.fiap.esg_residuos.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService service;

    @GetMapping
    public ResponseEntity<List<DestinationResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DestinationResponse> create(@RequestBody @Valid DestinationRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationResponse> update(@PathVariable Long id,
                                                      @RequestBody @Valid DestinationRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
}
