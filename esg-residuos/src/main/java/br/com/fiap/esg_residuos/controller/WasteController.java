package br.com.fiap.esg_residuos.controller;

import br.com.fiap.esg_residuos.dto.request.WasteRequest;
import br.com.fiap.esg_residuos.dto.response.WasteResponse;
import br.com.fiap.esg_residuos.service.WasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wastes")
@RequiredArgsConstructor
public class WasteController {

    private final WasteService service;

    @GetMapping
    public ResponseEntity<List<WasteResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<WasteResponse> findById(Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<WasteResponse> create(@RequestBody @Valid WasteRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WasteResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid WasteRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
